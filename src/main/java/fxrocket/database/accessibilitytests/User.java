package fxrocket.database.accessibilitytests;

import java.util.Date;

/**
 * Created by Felix Müllner on 02.05.2017.
 */
public class User {
    private int id;
    private String fullName;
    private Date birthdate;
    private String sex;
    private String category;

    public User(int id, String fullName, Date birthdate, String sex, String category) {
        this.id = id;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.sex = sex;
        this.category = category;
    }

    public int getId() {
        return this.id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Date getBirthdate() {
        return this.birthdate;
    }

    public String getSex() {
        return this.sex;
    }

    public String getCategory() {
        return this.category;
    }
}
