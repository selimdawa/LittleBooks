package com.flatcode.littlebooksadmin.Model;

public class Main {

    public String title;
    int image, number;
    public Class c;

    public Main(int image, String title,int number, Class c) {
        this.image = image;
        this.number = number;
        this.title = title;
        this.c = c;
    }

    public Main() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getC() {
        return c;
    }

    public void setC(Class c) {
        this.c = c;
    }
}