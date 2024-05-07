package com.cs1.super_mind;

import javafx.scene.control.TreeItem;

import java.io.Serializable;

public class TreeViewItem extends TreeItem<String> implements Serializable {

    TreeViewItem(String txt){
        super(txt);
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
