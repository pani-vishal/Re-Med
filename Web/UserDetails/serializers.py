from rest_framework import serializers
from django.contrib.auth.models import User
from .models import Person, Disease, Medicine, Prognosis




class UserDetailsSerializer(serializers.ModelSerializer): 

    class Meta:
        model = Person
        exclude = ['user']

    def create(self, validated_data):
        person = Person(
            name = validated_data["name"],
            gender = validated_data["gender"],
            dob = validated_data["dob"],
            pincode = validated_data["pincode"],
            phoneNumber = validated_data["phoneNumber"],
            height = validated_data["height"],
            weight = validated_data["weight"],
            bloodGroup = validated_data["bloodGroup"],
        )
        person.save()
        return person

class DiseaseSerializer(serializers.ModelSerializer):
    class Meta:
        model = Disease
        fields = '__all__'

class MedicineSerializer(serializers.ModelSerializer):
    class Meta:
        model = Medicine
        fields = '__all__'

class PrognosisDiseaseSerializer(serializers.ModelSerializer):
    diseaseName = serializers.CharField(max_length = 300)
    class Meta:
        model = Prognosis
        exclude = ['disease', 'medicines', 'person', 'isActive', 'endTime']

class PrognosisMedicineSerializer(serializers.ModelSerializer):
    medicineName = serializers.CharField(max_length = 300)
    class Meta:
        
