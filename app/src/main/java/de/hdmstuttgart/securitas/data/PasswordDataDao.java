package de.hdmstuttgart.securitas.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PasswordDataDao {

    @Insert
    void insert(PasswordData passwordData);

    @Update
    void update(PasswordData passwordData);

    @Delete
    void delete(PasswordData passwordData);


    @Query("SELECT * FROM password_data ")
    LiveData<List<PasswordData>> getAllPwData();
}