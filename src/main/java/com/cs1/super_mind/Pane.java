package com.cs1.super_mind;

import javafx.scene.layout.AnchorPane;

public abstract class Pane {
    public static void update_pane(AnchorPane A,TreeNode root){
        double last = Math.min(root.getLayoutY() + Draw.RecH / 2 - TreeNode.LBlockLen / 2,root.getLayoutY() + Draw.RecH / 2 - TreeNode.RBlockLen / 2);
        if(last<0){
            root.setLayoutY(root.getLayoutY()+Math.abs(last));
        }
        last=Math.min(310 + Draw.RecH / 2 - TreeNode.LBlockLen / 2,310 + Draw.RecH / 2 - TreeNode.RBlockLen / 2);
        if(last>0){
            root.setLayoutY(310);
        }
        last=Math.max(TreeNode.LBlockLen / 2,TreeNode.RBlockLen / 2);
        if(last>310){
            root.setLayoutY(last);
        }
        last=Math.max(root.getLayoutY() + Draw.RecH / 2 + TreeNode.LBlockLen / 2,root.getLayoutY() + Draw.RecH / 2 + TreeNode.RBlockLen / 2);
        A.setPrefHeight(Math.max(last,693));
        double l=root.getLayoutX()-TreeNode.LMaxLinkLen;
        if(l<0){
            root.setLayoutX(root.getLayoutX()+Math.abs(l));
        }
        l=500-TreeNode.LMaxLinkLen;
        if(l>0)root.setLayoutX(500);
        if(TreeNode.LMaxLinkLen>500)root.setLayoutX(TreeNode.LMaxLinkLen);
        double r=root.getLayoutX()+root.getTextLen()+TreeNode.RMaxLinkLen;
        A.setPrefWidth(Math.max(r,1043));
    }
}
