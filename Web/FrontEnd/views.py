from django.shortcuts import render

# Form Views

def login(request):
	return render(request, 'register/user_login.html', {})


def logout(request):
	return render(request, 'register/user_logout.html', {})


def new_user(request):
	return render(request, 'register/new_user.html', {})
	

def user_details(request):
	return render(request, 'register/user_details.html', {})

def change_user_details(request):
	return render(request, 'register/change_user_details.html', {})

def change_password(request):
    return render(request, 'register/change_password.html', {})


def example_login(request):
    return render(request, 'register/login-page.html', {})

def dashboard(request):
    return render(request, 'template.html', {})

def user(request):
    return render(request, 'user.html', {})

def diseases(request):
    return render(request, 'diseases.html', {})	

def stats(request):
    return render(request, 'stats.html', {})	
