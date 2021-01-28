package com.iwz.WzFramwork.mod.biz.popups.model;

public class DialogItem {
    private int id;
    private String name;
    private int imgType;
    private String jumpUrl;
    private String imgUrl;

    public DialogItem() {
        this.id = 0;
        this.name = "";
        this.imgType = 0;
        this.jumpUrl = "";
        this.imgUrl = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
