package com.rapidhelp.models;

import java.util.List;

public class LevelReport {

    private int levelNo;
    private List<Object> itemList;

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public List<Object> getItemList() {
        return itemList;
    }

    public void setItemList(List<Object> itemList) {
        this.itemList = itemList;
    }
}
