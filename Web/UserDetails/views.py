from django.shortcuts import render, redirect, get_object_or_404
from django.contrib.auth.models import User
from .serializers import *
from rest_framework.views import APIView
from django.contrib.auth.decorators import login_required
from rest_framework.response import Response
from rest_framework import (
    status, 
    viewsets
)

from .models import Person, Disease, Medicine, Prognosis, Prescription
from rest_framework.authtoken.models import Token
from rest_framework.generics import ListAPIView



class RegularUserAPI(APIView):
    serializer_class = UserDetailsSerializer

    def post(self, request, format='json'):
        serializer = UserDetailsSerializer(data=request.data)
        if serializer.is_valid():
            person = serializer.save()
            person.user = request.user
            token = Token.objects.get(user = request.user)
            person.save()
            if person:
                json = serializer.data
                return Response(json, status=status.HTTP_201_CREATED)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

    def get(self, request, format = 'json'):
        person = Person.objects.get(user = request.user)
        serializer = UserDetailsSerializer(person)
        return Response(serializer.data, status = status.HTTP_200_OK)


    def put(self, request, format = 'json'):
        person = Person.objects.get(user = request.user)
        print(person)
        serializer = UserDetailsSerializer(person, data = request.data, partial = True)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status = status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class DiseaseAPIView(ListAPIView):
    serializer_class = DiseaseSerializer
    queryset = Disease.objects.all()


class MedicineAPIView(ListAPIView):
    serializer_class = MedicineSerializer
    queryset = Medicine.objects.all()


class PrognosisDiseaseView(APIView):
    serializer_class = PrognosisDiseaseSerializer

    def post(self, request, format = 'json'):
        serializer = PrognosisDiseaseSerializer(data = request.data)
        if serializer.is_valid():
            person = Person.objects.get(user = request.user)
            disease = Disease.objects.get_or_create(name = serializer.validated_data['diseaseName'])
            disease = Disease.objects.get(name = serializer.validated_data['diseaseName'])
            Prognosis.objects.create(person = person, startTime = serializer.validated_data['startTime'], disease = disease)
            return Response(serializer.data, status = status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status = status.HTTP_400_BAD_REQUEST)


class PrognosisPrescriptionView(APIView):
    serializer_class = PrognosisPrescriptionSerializer

    def post(self, request, format = 'json'):
        serializer = PrognosisPrescriptionSerializer(data = request.data)
        if serializer.is_valid():
            medicine = Medicine.objects.get_or_create(name = serializer.validated_data['medicineName'])
            medicine = Medicine.objects.get(name = serializer.validated_data['medicineName'])
            prognosis = Prognosis.objects.get(id = serializer.validated_data['prognosisID'])
            prescription = Prescription.objects.create(medicine = medicine,
                morning = serializer.validated_data['morning'],
                afternoon = serializer.validated_data['afternoon'],
                evening = serializer.validated_data['evening'],
                night = serializer.validated_data['night']
            )
            prescription.save()
            prognosis.prescription.add(prescription)
            prognosis.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class PrognosisDetailView(ListAPIView):
    serializer_class = PrognosisDetailSerializer
    
    def get_queryset(self):
        person = Person.objects.get(user = self.request.user)
        return Prognosis.objects.filter(person = person)

    def list(self, request):
        queryset = self.get_queryset();
        serializer = PrognosisDetailSerializer(queryset, many = True)
        new_list = list()
        for data in serializer.data:
            new_dict = dict()
            disease = Disease.objects.get(id = data['disease'])
            diseaseName = disease.name
            diseaseDescription = disease.description
            new_dict.update(data)
            temp_dict = {
                'diseaseName' : diseaseName,
                'diseaseDescription' : diseaseDescription,
            }
            new_temp_list = list()
            for pid in data['prescription']:
                new_temp_dict = dict()
                prescription = Prescription.objects.get(id = pid)
                new_temp_dict = {
                    'id' : pid,
                    'medicineName' : prescription.medicine.name,
                    'morning' : prescription.morning,
                    'afternoon' : prescription.afternoon,
                    'evening' : prescription.evening,
                    'night' : prescription.night
                }
                new_temp_list.append(new_temp_dict)
            new_dict['prescription'] = new_temp_list
            new_dict.update(temp_dict)
            new_list.append(new_dict)
            
            
        # print(new_list)
            
        return Response(new_list)


class PrescriptionDeleteView(APIView):
    serializer_class = PrescriptionDeleteSerializer

    def post(self, request, format = 'json'):
        serializer = PrescriptionDeleteSerializer(data = request.data)
        if serializer.is_valid():
            print(serializer.validated_data['id'])
            prescription = Prescription.objects.get(id = serializer.validated_data['id'])
            prescription.delete()
            return Response(serializer.data, status = status.HTTP_200_OK)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    
class AreaStatisticsView(APIView):
    serializer_class = AreaStatisticsSerializer

    def post(self, request):
        serializer = AreaStatisticsSerializer(data = request.data)
        data ={}
        if serializer.is_valid():
            people = Person.objects.filter(pincode = serializer.validated_data['pincode'])
            print(people)
            map = dict()
            for person in people:
                prognosisList = Prognosis.objects.filter(person = person)
                print(prognosisList)
                for p in prognosisList:
                    try:
                        map[p.disease.name] += 1
                    except:
                        map.update({p.disease.name : 1})
            labels = map.keys()
            series = map.values()
            data = {
                'labels' : labels,
                'series' : series
            }
            print(data)

        return Response(data, status = status.HTTP_200_OK)

