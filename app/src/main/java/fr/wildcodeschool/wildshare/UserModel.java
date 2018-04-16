package fr.wildcodeschool.wildshare;

import android.graphics.drawable.Drawable;

/**
 * Created by wilder on 27/03/18.
 */

public class UserModel {
    private String firstname;
    private String lastname;
    private Drawable profilPic;

    public UserModel() {
    }

    public UserModel(String firstname, String lastname, Drawable profilPic) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.profilPic = profilPic;
    }

    public UserModel(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public UserModel setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public UserModel setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public Drawable getProfilPic() {
        return profilPic;
    }

    public UserModel setProfilPic(Drawable profilPic) {
        this.profilPic = profilPic;
        return this;
    }
}