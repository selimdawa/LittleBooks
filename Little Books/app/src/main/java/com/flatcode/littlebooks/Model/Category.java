package com.flatcode.littlebooks.Model;

public class Category {

    String id, category, image, publisher;
    long timestamp;

    public Category() {

    }

    public Category(String id, String category, String image, String publisher, long timestamp) {
        this.id = id;
        this.category = category;
        this.publisher = publisher;
        this.image = image;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
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
}
