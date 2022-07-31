package com.example.ourchat;

public class ModelOfMessage {
    String mailOfUser, nameOfUser, pictureOfUser, messageOfUser, idOfKey;

    public ModelOfMessage() {
    }

    public ModelOfMessage(String mailOfUser, String nameOfUser, String pictureOfUser, String messageOfUser, String idOfKey) {
        this.mailOfUser = mailOfUser;
        this.nameOfUser = nameOfUser;
        this.pictureOfUser = pictureOfUser;
        this.messageOfUser = messageOfUser;
        this.idOfKey = idOfKey;
    }

    public String getMailOfUser() {
        return mailOfUser;
    }

    public void setMailOfUser(String mailOfUser) {
        this.mailOfUser = mailOfUser;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public String getPictureOfUser() {
        return pictureOfUser;
    }

    public void setPictureOfUser(String pictureOfUser) {
        this.pictureOfUser = pictureOfUser;
    }

    public String getMessageOfUser() {
        return messageOfUser;
    }

    public void setMessageOfUser(String messageOfUser) {
        this.messageOfUser = messageOfUser;
    }

    public String getIdOfKey() {
        return idOfKey;
    }

    public void setIdOfKey(String idOfKey) {
        this.idOfKey = idOfKey;
    }
}
