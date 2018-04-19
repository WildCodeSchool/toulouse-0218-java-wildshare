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
    private Drawable imageDraw;
    private Bitmap imageBit;
    private String imageURL;
    private Uri imageURI;
    private String description;
    private String ownerID;
    private int ownerImage;

    public ItemModel(String name, Drawable image, String ownerID) {
        this.name = name;
        this.imageDraw = image;
        this.ownerID = ownerID;
    }

    public ItemModel(String name, String imageURL, String ownerID) {
        this.name = name;
        this.imageURL = imageURL;
        this.ownerID = ownerID;
    }

    public ItemModel(String name, Bitmap imageBit, String ownerID) {
        this.name = name;
        this.imageBit = imageBit;
        this.ownerID = ownerID;
    }

    public ItemModel(String name, Uri imageURI, String ownerID) {
        this.name = name;
        this.imageURI = imageURI;
        this.ownerID = ownerID;
    }

    public ItemModel(String name, Drawable image) {
        this.name = name;
        this.imageDraw = image;
    }


    public ItemModel() {
    }


    protected ItemModel(Parcel in) {
        name = in.readString();
        imageBit = in.readParcelable(Bitmap.class.getClassLoader());
        imageURL = in.readString();
        imageURI = in.readParcelable(Uri.class.getClassLoader());
        description = in.readString();
        ownerID = in.readString();
        ownerImage = in.readInt();
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

    public Drawable getImageDraw() {
        return imageDraw;
    }

    public void setImageDraw(Drawable image) {
        this.imageDraw = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(int ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getImageURL() {return imageURL;}

    public void setImageURL(String imageURL) {this.imageURL = imageURL;}

    public String getOwnerID() {return ownerID;}

    public void setOwnerID(String ownerID) {this.ownerID = ownerID;}

    public Bitmap getImageBit() {return imageBit;}

    public void setImageBit(Bitmap imageBit) {this.imageBit = imageBit;}

    public Uri getImageURI() {return imageURI;}

    public void setImageURI(Uri imageURI) {this.imageURI = imageURI;}

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
