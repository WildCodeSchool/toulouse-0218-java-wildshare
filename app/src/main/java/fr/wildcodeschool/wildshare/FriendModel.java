package fr.wildcodeschool.wildshare;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wilder on 03/04/18.
 */

public class FriendModel {

    private String pseudo;
    private String profilPic;

    public FriendModel() {}

    public FriendModel(String pseudo, String profilPic) {
        this.pseudo = pseudo;
        this.profilPic = profilPic;
    }

    public String getPseudo() {
        return pseudo;
    }

    public FriendModel setPseudo(String pseudo) {
        this.pseudo = pseudo;
        return this;
    }

    public String getProfilPic() {
        return profilPic;
    }

    public FriendModel setProfilPic(String profilPic) {
        this.profilPic = profilPic;
        return this;
    }
}
