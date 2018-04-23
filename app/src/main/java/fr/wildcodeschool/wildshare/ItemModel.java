package fr.wildcodeschool.wildshare;

/**
 * Created by wilder on 26/03/18.
 */

public class ItemModel {

    private String name;
    private String image;
    private String description;
    private String ownerPseudo;
    private String ownerProfilPic;
    private String ownerId;

    public ItemModel(){}

    public ItemModel(String name, String image, String description, String ownerPseudo, String ownerProfilPic, String ownerId) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.ownerPseudo = ownerPseudo;
        this.ownerProfilPic = ownerProfilPic;
        this.ownerId = ownerId;
    }

    public ItemModel(String name, String image, String description, String ownerId) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.ownerId = ownerId;
    }

    public ItemModel(String name, String image, String ownerProfilPic) {
        this.name = name;
        this.image = image;
        this.ownerProfilPic = ownerProfilPic;
    }

    public ItemModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public ItemModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ItemModel setImage(String image) {
        this.image = image;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOwnerPseudo() {
        return ownerPseudo;
    }

    public ItemModel setOwnerPseudo(String ownerPseudo) {
        this.ownerPseudo = ownerPseudo;
        return this;
    }

    public String getOwnerProfilPic() {
        return ownerProfilPic;
    }

    public ItemModel setOwnerProfilPic(String ownerProfilPic) {
        this.ownerProfilPic = ownerProfilPic;
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public ItemModel setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }


}
