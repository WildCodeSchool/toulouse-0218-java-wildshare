package fr.wildcodeschool.wildshare;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;

/**
 * Created by wilder on 26/03/18.
 */

class ItemModel implements Parcelable {

    private String name;
    private String imageURL;
    private String description;
    private String ownerID;
    private String ownerImage;



    public ItemModel(String name, String imageURL, String ownerID) {
        this.name = name;
        this.imageURL = imageURL;
        this.ownerID = ownerID;
    }


    public ItemModel() {
    }

    public ItemModel(String name) {
        this.name = name;
    }

    protected ItemModel(Parcel in) {
        name = in.readString();
        imageURL = in.readString();
        description = in.readString();
        ownerID = in.readString();
        ownerImage = in.readString();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getImageURL() {return imageURL;}

    public void setImageURL(String imageURL) {this.imageURL = imageURL;}

    public String getOwnerID() {return ownerID;}

    public void setOwnerID(String ownerID) {this.ownerID = ownerID;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);

    }
}
