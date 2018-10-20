from django.urls import path
from . import views

urlpatterns = [
  path('set_new_user/', views.new_user, name = 'new_user'),
  path('dashboard/', views.dashboard, name = 'dashboard'),
  path('user/', views.user, name = 'user'),
  path('stats/', views.stats, name = 'stats'),
  path('diseases/', views.diseases, name = 'diseases'),  
  path('user_details/', views.user_details, name = 'user_details'),
  path('change_user_details/', views.change_user_details, name = 'change_user_details'),
  path('change_password/', views.change_password, name = 'change_password'),
  path('login/', views.login, name = 'login'),
  path('logout/', views.logout, name = 'logout'),
]