from django.shortcuts import render, redirect, get_object_or_404
from django.contrib.auth.models import User
from .serializers import UserDetailsSerializer
from rest_framework.views import APIView
from django.contrib.auth.decorators import login_required
from rest_framework.response import Response
from rest_framework import status
from .models import Person
from rest_framework.authtoken.models import Token


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
    	serializer = Person(person, data = request.data, partial = True)
    	if serializer.is_valid():
    		serializer.save()
    		return Response(serializer.data, status = status.HTTP_200_OK)
    	return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)



