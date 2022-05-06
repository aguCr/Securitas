package de.hdmstuttgart.securitas.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PwSharedViewModel extends ViewModel {

    //to pass password between Fragments
    private final MutableLiveData<String> password = new MutableLiveData<>();


    public void setPassword(String inputPw) {
        password.setValue(inputPw);
    }

    public LiveData<String> getPassword() {
        return password;
    }
}
