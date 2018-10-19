from django.urls import path
from . import views
from django.contrib.auth import views as auth_views


urlpatterns = [
    path('user_details/', views.RegularUserAPI.as_view(), name = "user_details"),
]