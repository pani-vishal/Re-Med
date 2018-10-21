from django.urls import path
from . import views
from django.contrib.auth import views as auth_views


urlpatterns = [
    path('user_details/', views.RegularUserAPI.as_view(), name = "user_details"),
    path('disease_list/', views.DiseaseAPIView.as_view(), name = 'disease_list'),
    path('medicine_list/', views.MedicineAPIView.as_view(), name = 'medicine_list'),
    path('prognosis_disease_add/', views.PrognosisDiseaseView.as_view(), name = 'prognosis_disease_add'),
    path('prognosis_medicine_add/', views.PrognosisPrescriptionView.as_view(), name = 'prognosis_medicine_add'),
    path('prognosis_list/', views.PrognosisDetailView.as_view(), name = 'prognosis_list'),
    path('area_statistics/', views.AreaStatisticsView.as_view(), name = 'area_statistics'),
]