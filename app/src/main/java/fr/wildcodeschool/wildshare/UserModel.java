package fr.wildcodeschool.wildshare;

import android.graphics.drawable.Drawable;
import android.net.Uri;

/**
 * Created by wilder on 27/03/18.
 */

public class UserModel {
    private String pseudo;
    private String profilPic;

    public UserModel() {

    }

    public UserModel(String pseudo, String profilPic) {
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

    public String getProfilPic() {
        return profilPic;
    }

    public UserModel setProfilPic(String profilPic) {
        this.profilPic = profilPic;
        return this;
    }
}