package com.cs1.super_mind;

import javafx.scene.control.TreeItem;

import java.io.Serializable;

public class TreeViewItem extends TreeItem<String> implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //给目录树的项添加ID标记，以便目录树定位到节点
    private int id;
    TreeViewItem(String txt){
        super(txt);
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
