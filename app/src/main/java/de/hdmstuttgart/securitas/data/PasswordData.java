package de.hdmstuttgart.securitas.data;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

//table of db
@Entity(tableName = "password_data")
public class PasswordData {


    @PrimaryKey(autoGenerate = true)
    private int uid;

    //columns of table
    private final String category;
    private final String title;
    private final String userEMail;
    private final String password;
    private final String note;
    private final int quality;


    //getter+ setter for the Primary Key (db sets the uid)
    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }


    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getUserEMail() {
        return userEMail;
    }

    public String getPassword() {
        return password;
    }

    public String getNote() {
        return note;
    }

    public int getQuality() {
        return quality;
    }


    public PasswordData(String category, String title, String userEMail, String password, String note, int quality) {
        this.category = category;
        this.title = title;
        this.userEMail = userEMail;
        this.password = password;
        this.note = note;
        this.quality = quality;
    }
}