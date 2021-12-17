package com.alatheer.shebinbook.home;

public class AskModel {
    int image;
    String comment;
    String name;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AskModel(int image, String comment, String name) {
        this.image = image;
        this.comment = comment;
        this.name = name;
    }
}
