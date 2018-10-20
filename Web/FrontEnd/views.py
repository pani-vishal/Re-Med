from django.shortcuts import render

# Create your views here.

def new_user(request):
	return render(request, 'register/new_user.html', {})
