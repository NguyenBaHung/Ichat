package com.example.nbhung318.ichat.entity;

public class User {
    private String userImage;
    private String userName;
    private String userStatus;

    public User() {
    }

    public User(String userImage, String userName, String userStatus) {
        this.userImage = userImage;
        this.userName = userName;
        this.userStatus = userStatus;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
