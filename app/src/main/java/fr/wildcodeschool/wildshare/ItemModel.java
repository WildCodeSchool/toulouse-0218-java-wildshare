package fr.wildcodeschool.wildshare;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wilder on 26/03/18.
 */

class ItemModel implements Parcelable {

    private String name;
    private int image;
    private String description;
    private String ownerFirstame;
    private String ownerLastame;
    private int ownerImage;

    public ItemModel(String name, int image, String description, String ownerFirstame, String ownerLastame, int ownerImage) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.ownerFirstame = ownerFirstame;
        this.ownerLastame = ownerLastame;
        this.ownerImage = ownerImage;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getImage() {return image;}
    public void setImage(int image) {this.image = image;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getOwnerFirstame() {return ownerFirstame;}
    public void setOwnerFirstame(String ownerFirstame) {this.ownerFirstame = ownerFirstame;}
    public String getOwnerLastame() {return ownerLastame;}
    public void setOwnerLastame(String ownerLastame) {this.ownerLastame = ownerLastame;}
    public int getOwnerImage() {return ownerImage;}
    public void setOwnerImage(int ownerImage) {this.ownerImage = ownerImage;}

    protected ItemModel(Parcel in) {
        name = in.readString();
        image = in.readInt();
        description = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(image);
        parcel.writeString(description);
    }
}
