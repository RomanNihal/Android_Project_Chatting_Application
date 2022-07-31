package com.example.ourchat;

public class ModelOfUsers {
    String Name, Email, PictureLink;

    public ModelOfUsers() {
    }

    public ModelOfUsers(String name, String email, String pictureLink) {
        Name = name;
        Email = email;
        PictureLink = pictureLink;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPictureLink() {
        return PictureLink;
    }

    public void setPictureLink(String pictureLink) {
        PictureLink = pictureLink;
    }
}
