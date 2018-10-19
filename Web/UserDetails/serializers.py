from rest_framework import serializers
from django.contrib.auth.models import User
from .models import Person



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
