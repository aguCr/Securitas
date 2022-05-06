package de.hdmstuttgart.securitas.viewmodels;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.hdmstuttgart.securitas.data.PasswordData;
import de.hdmstuttgart.securitas.data.PasswordDataRepository;

public class PasswordDataViewModel extends AndroidViewModel {

    private final PasswordDataRepository passwordDataRepository;
    private final LiveData<List<PasswordData>> allPasswordData;


    //extends AndroidViewModel to be able to pass a Context
    public PasswordDataViewModel(Application application) {
        super(application);
        passwordDataRepository = new PasswordDataRepository(application);
        allPasswordData = passwordDataRepository.getAllPwData();
    }


    //wrapper methods to decouple UI from business logic
    public void insert(PasswordData passwordData) {
        passwordDataRepository.insert(passwordData);
    }

    public void delete(PasswordData passwordData) {
        passwordDataRepository.delete(passwordData);
    }

    public void update(PasswordData passwordData) {
        passwordDataRepository.update(passwordData);
    }

    //returns cached list of PasswordData
    public LiveData<List<PasswordData>> getAllPasswordData() {
        return allPasswordData;
    }

}
