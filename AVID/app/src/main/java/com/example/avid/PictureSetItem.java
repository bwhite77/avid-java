package com.example.avid;

import java.util.List;

public class PictureSetItem {
    private int imgRes;
    private String title;
    private String desc;
    private List<Picture> pictures;

    public PictureSetItem(int imgRes, String title, String desc, List<Picture> pictures) {
        this.imgRes = imgRes;
        this.title = title;
        this.desc = desc;
        this.pictures = pictures;
    }

    public int getImgRes() {
        return imgRes;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public List<Picture> getPictures() {
        return pictures;
    }
}
