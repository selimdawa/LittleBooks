package com.flatcode.littlebooks.Model;

public class Setting {

    String id, name;
    int image, number;
    Class c;

    public Setting() {

    }

    public Setting(String id, String name, int image, int number, Class c) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.number = number;
        this.c = c;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Class getC() {
        return c;
    }

    public void setC(Class c) {
        this.c = c;
    }
}