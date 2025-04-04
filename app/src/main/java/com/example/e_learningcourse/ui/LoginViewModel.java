package com.example.e_learningcourse.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.e_learningcourse.model.response.LoginResponse;
import com.example.e_learningcourse.repository.AuthenticationRepository;

public class LoginViewModel extends ViewModel {
    private final AuthenticationRepository repository;
    public MutableLiveData<String> email = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> loginResponse = new MutableLiveData<>();

    public LoginViewModel() {
        repository = new AuthenticationRepository();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponse;
    }

    public void login() {
        isLoading.setValue(true);

        repository.login(email.getValue(), password.getValue()).observeForever(response -> {
            isLoading.setValue(false);
            loginResponse.setValue(response);
        });
    }
}
