package com.alifia.mymaxxio.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String emailUser;
    private String idMember;
    private String userId;
    private String photoProfile;
    private List<String> likes = new ArrayList<>();

    public User() {}

    public User(String emailUser, String idMember, String userId, List<String> likes, String photoProfile) {
        this.emailUser = emailUser;
        this.idMember = idMember;
        this.userId = userId;
        this.likes = likes;
        this.photoProfile = photoProfile;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getPhotoProfile() {
        return photoProfile;
    }

    public void setPhotoProfile(String photoProfile) {
        this.photoProfile = photoProfile;
    }
}
