from django.db import models
from django.contrib.auth.models import User
from django.utils import timezone

class Person(models.Model):
    name = models.CharField(max_length=20)
    gender_options = (
        ('M', 'Male'),
        ('F', 'Female'),
        ('O', 'Other'),
    )
    blood_group_options = (
        ('B+', 'B+'),
        ('A+', 'A+'),
        ('O+', 'O+'),
        ('AB+', 'AB+'),
        ('B-', 'B-'),
        ('A-', 'A-'),
        ('O-', 'O-'),
        ('AB-', 'AB-'),
    )
    user = models.OneToOneField(User, on_delete = models.CASCADE, null = True)
    gender = models.CharField(max_length = 1, choices = gender_options)
    dob = models.DateField(default=timezone.now())
    pincode = models.IntegerField(blank = True, null = True)
    phoneNumber = models.CharField(max_length = 12)
    height = models.FloatField(blank = True, null = True)
    weight = models.FloatField(blank = True, null = True)
    bloodGroup = models.CharField(max_length = 3, choices = blood_group_options)

    def __str__(self):
        return self.name

class Medicine(models.Model):
    name = models.CharField(max_length = 50)
    medicine_type_options = (
        ('C', 'Capsule'),
        ('T', 'Tablet'),
        ('L', 'Liquid'),
        ('O', 'Other')
    )
    medicineType = models.CharField(max_length = 1, choices = medicine_type_options)

    def __str__(self):
        return self.name

class Prescription(models.Model):
    estimateEndTime = models.DateField(blank = True, null = True)
    medicine = models.OneToOneField(Medicine, on_delete = models.CASCADE)


class Disease(models.Model):
    name = models.CharField(max_length = 300)
    description = models.CharField(max_length = 1000, blank = True, null = True)

    def __str__(self):
        return self.name

class Prognosis(models.Model):
    startTime = models.DateField(default = timezone.now())
    endTime = models.DateField(blank = True, null = True)
    disease = models.OneToOneField(Disease, on_delete = models.CASCADE)
    medicines = models.ManyToManyField(Medicine)
    person = models.ForeignKey(Person, on_delete = models.CASCADE)
    isActive = models.BooleanField(default = True)
