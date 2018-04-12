package fr.wildcodeschool.wildshare;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wilder on 03/04/18.
 */

public class FriendModel implements Parcelable{

    private String firstname;
    private String lastname;
    private Drawable avatar;

    public FriendModel(String firstname, String lastname, Drawable avatar) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.avatar = avatar;
    }

    public String getFirstname() {return firstname;}
    public void setFirstname(String firstname) {this.firstname = firstname;}
    public String getLastname() {return lastname;}
    public void setLastname(String lastname) {this.lastname = lastname;}
    public Drawable getAvatar() {return avatar;}
    public void setAvatar(Drawable avatar) {this.avatar = avatar;}



    protected FriendModel(Parcel in) {
        firstname = in.readString();
        lastname = in.readString();
    }

    public static final Creator<FriendModel> CREATOR = new Creator<FriendModel>() {
        @Override
        public FriendModel createFromParcel(Parcel in) {
            return new FriendModel(in);
        }

        @Override
        public FriendModel[] newArray(int size) {
            return new FriendModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstname);
        parcel.writeString(lastname);
    }
}
