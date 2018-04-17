package fr.wildcodeschool.wildshare;

import android.graphics.drawable.Drawable;

/**
 * Created by wilder on 27/03/18.
 */

public class UserModel {
    private String pseudo;
    private Drawable profilPic;

    public UserModel() {

    }

    public UserModel(String pseudo, Drawable profilPic) {
        this.pseudo = pseudo;
        this.profilPic = profilPic;
    }

    public UserModel(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return pseudo;
    }

    public UserModel setPseudo(String pseudo) {
        this.pseudo = pseudo;
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