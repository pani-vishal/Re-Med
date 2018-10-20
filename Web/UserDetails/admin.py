from django.contrib import admin
from .models import *

# Register your models here.
@admin.register(Person)
class PersonAdmin(admin.ModelAdmin):
    list_display = [
        'name',
        'gender'
    ]

@admin.register(Medicine)
class MedicineAdmin(admin.ModelAdmin):
    list_display = [
        'name'
    ]

@admin.register(Disease)
class DiseaseAdmin(admin.ModelAdmin):
    list_display = [
        'name'
    ]

@admin.register(Prognosis)
class PrognosisAdmin(admin.ModelAdmin):
    list_display = [
        'person',
        'disease'
    ]

admin.site.register(Prescription)