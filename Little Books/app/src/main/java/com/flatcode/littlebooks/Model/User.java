package com.flatcode.littlebooks.Model;

public class User {

    String id, username, profileImage, email;
    long timestamp;
    int version, booksCount, adLoad, adClick;

    public User() {

    }

    public User(String id, String username, String profileImage, String email, long timestamp,
                int version, int booksCount, int adLoad, int adClick) {
        this.id = id;
        this.username = username;
        this.profileImage = profileImage;
        this.email = email;
        this.timestamp = timestamp;
        this.version = version;
        this.booksCount = booksCount;
        this.adLoad = adLoad;
        this.adClick = adClick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getBooksCount() {
        return booksCount;
    }

    public void setBooksCount(int booksCount) {
        this.booksCount = booksCount;
    }

    public int getAdLoad() {
        return adLoad;
    }

    public void setAdLoad(int adLoad) {
        this.adLoad = adLoad;
    }

    public int getAdClick() {
        return adClick;
    }

    public void setAdClick(int adClick) {
        this.adClick = adClick;
    }
}