package de.hdmstuttgart.securitas.data;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PasswordDataRepository {

    private final PasswordDataDao passwordDataDao;
    private final LiveData<List<PasswordData>> allPasswordData;


    public PasswordDataRepository(Application application) {
        PasswordDataDb db = PasswordDataDb.getDatabase(application);
        passwordDataDao = db.passwordDataDao();
        allPasswordData = passwordDataDao.getAllPwData();
    }


    //calling ExecutorService to operate asynchronous
    public void insert(PasswordData passwordData) {
        PasswordDataDb.databaseWriteExecutor.execute(() -> passwordDataDao.insert(passwordData));
    }

    public void update(PasswordData passwordData) {
        PasswordDataDb.databaseWriteExecutor.execute(() -> passwordDataDao.update(passwordData));
    }

    public void delete(PasswordData passwordData) {
        PasswordDataDb.databaseWriteExecutor.execute(() -> passwordDataDao.delete(passwordData));
    }

    //notifies observers on main thread when the data changes
    public LiveData<List<PasswordData>> getAllPwData() {
        return allPasswordData;
    }

}
