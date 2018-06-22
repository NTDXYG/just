package com.example.just.Bean;

/**
 * Created by Administrator on 2018/5/21.
 */

public class Story {
    String name;
    String url;
    String image;
    Boolean isLove;

    public Story(String name, String url, String image, Boolean isLove) {
        this.name = name;
        this.url = url;
        this.image = image;
        this.isLove = isLove;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getLove() {
        return isLove;
    }

    public void setLove(Boolean love) {
        isLove = love;
    }
}
