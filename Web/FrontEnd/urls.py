from django.urls import path
from . import views

urlpatterns = [
  path('set_new_user/', views.new_user, name = 'new_user')
]