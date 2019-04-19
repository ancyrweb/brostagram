package com.rewieer.brostagram.data.entity;

import androidx.annotation.Nullable;

public class LocalImage {
    public String path;
    public int height;
    public int width;
    public int ID;

    public @Nullable String name;
    public @Nullable String ext;

    public LocalImage(String path, int height, int width) {
        String name = null, ext = null;
        String[] folders = path.split("/");

        if (folders.length > 0) {
            name = folders[folders.length - 1];
            String[] names = name.split("\\.");
            if (names.length > 0) {
                ext = names[names.length - 1];
            }
        }

        this.name = name;
        this.ext = ext;
        this.path = path;
        this.height = height;
        this.width = width;
        this.ID = 0;
    }
}
