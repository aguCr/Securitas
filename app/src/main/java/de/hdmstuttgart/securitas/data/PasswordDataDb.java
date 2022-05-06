package de.hdmstuttgart.securitas.data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PasswordData.class}, version = 1)
public abstract class PasswordDataDb extends RoomDatabase {

    public abstract PasswordDataDao passwordDataDao();

    private static volatile PasswordDataDb INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    //to do db operations asynchronous on the background
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    //Singleton-Pattern to create the Db only once with a single instance
    static PasswordDataDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PasswordDataDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PasswordDataDb.class, "password_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}