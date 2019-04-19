package com.rewieer.brostagram.data.entity;

public class LocalImage {
    public String path;
    public int height;
    public int width;
    public int ID;

    public LocalImage(String path, int height, int width) {
        this.path = path;
        this.height = height;
        this.width = width;
        this.ID = 0;
    }
}
