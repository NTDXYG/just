package com.example.just.Bean;

/**
 * Created by yg on 18-7-2.
 */

public class Taobao {
    String title;
    String img;
    String priceWap;
    String priceWithRate;
    String nick;
    String sold;
    String url;
    String numiid;

    public Taobao(String title, String img, String priceWap, String priceWithRate, String nick, String sold, String url, String numiid) {
        this.title = title;
        this.img = img;
        this.priceWap = priceWap;
        this.priceWithRate = priceWithRate;
        this.nick = nick;
        this.sold = sold;
        this.url = url;
        this.numiid = numiid;
    }

    public String getNumiid() {
        return numiid;
    }

    public void setNumiid(String numiid) {
        this.numiid = numiid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPriceWap() {
        return priceWap;
    }

    public void setPriceWap(String priceWap) {
        this.priceWap = priceWap;
    }

    public String getPriceWithRate() {
        return priceWithRate;
    }

    public void setPriceWithRate(String priceWithRate) {
        this.priceWithRate = priceWithRate;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
