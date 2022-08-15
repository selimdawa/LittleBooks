package com.flatcode.littlebooks.Model;

public class Book {

    String publisher, id, title, description, categoryId, url, image;
    long timestamp;
    int viewsCount, downloadsCount, lovesCount,editorsChoice;

    public Book() {

    }

    public Book(String publisher, String id, String title, String description, String categoryId, String url,
                String image, long timestamp, int viewsCount, int downloadsCount, int lovesCount, int editorsChoice) {
        this.publisher = publisher;
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.url = url;
        this.image = image;
        this.timestamp = timestamp;
        this.viewsCount = viewsCount;
        this.downloadsCount = downloadsCount;
        this.lovesCount = lovesCount;
        this.editorsChoice = editorsChoice;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public int getDownloadsCount() {
        return downloadsCount;
    }

    public void setDownloadsCount(int downloadsCount) {
        this.downloadsCount = downloadsCount;
    }

    public int getLovesCount() {
        return lovesCount;
    }

    public void setLovesCount(int lovesCount) {
        this.lovesCount = lovesCount;
    }

    public int getEditorsChoice() {
        return editorsChoice;
    }

    public void setEditorsChoice(int editorsChoice) {
        this.editorsChoice = editorsChoice;
    }
}