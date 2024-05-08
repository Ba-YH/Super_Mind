package com.cs1.super_mind;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.ArrayList;
import static com.cs1.super_mind.Draw.*;

public class TreeNode extends TextField implements Serializable {
    public static String backgroundColor="#F9AA33";
    public static double LBlockLen=Draw.RecH;
    public static double RBlockLen=Draw.RecH;
    public static String font_type="微软雅黑";
    public static double LMaxLinkLen;
    public static double RMaxLinkLen;
    private static ArrayList<TreeNode> Lchildren=new ArrayList<>();//根节点的左子树
    private static ArrayList<TreeNode> Rchildren=new ArrayList<>();//根节点的右子树子树
    public static ArrayList<TreeNode> getLchildren() {
        return Lchildren;
    }
    public static ArrayList<TreeNode> getRchildren() {
        return Rchildren;
    }
    public static void setLchildren(ArrayList<TreeNode> lchildren) {
        Lchildren = lchildren;
    }

    public static void setRchildren(ArrayList<TreeNode> rchildren) {
        Rchildren = rchildren;
    }

    //
    private double BlockLen;//块长度
    private int SonSize;
    private double TextLen;//文本宽度
    private double MaxLinkLen;//最长子链
    private int type;//该节点向左还是向右 type =-1 向左，=1向右
    private boolean isroot;
    private TreeNode parent;
    private TreeViewItem view;//该节点在TreeView上的视图
    private ArrayList<TreeNode> children;
    private Line line;
    private String txt;

    TreeNode(String txt1) {
        setTxt(txt1);
        BlockLen = Draw.RecH;
        super.setPrefHeight(Draw.RecH);
        super.setPrefWidth(50);
        setTextLen(RecW);
        type = 1;
        children = new ArrayList<>();
        line = new Line();
        this.setFont(new Font(font_type,20));
    }
    public String getTxt() {
        return txt;
    }
    public void setTxt(String txt) {
        this.txt = txt;
    }

    public TreeViewItem getView() {
        return view;
    }
    public TreeNode getparent() {
        return parent;
    }
    public double getMaxLinkLen() {
        return MaxLinkLen;
    }
    public double getTextLen() {
        return TextLen;
    }

    public void setTextLen(double textLen) {
        TextLen = textLen;
    }

    public void setMaxLinkLen(double maxLinkLen) {
        MaxLinkLen = maxLinkLen;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return isroot;
    }

    public void setIsroot(boolean flag) {
        isroot = flag;
    }

    public ArrayList<TreeNode> getchildren() {
        return children;
    }
    public void initNode(TreeNode root,AnchorPane A1) {
        this.setOnMouseClicked(event ->{
            if(CurNode!=null) {
                CurNode.setEditable(false);
                CurNode.setStyle("-fx-control-inner-background:"+backgroundColor);
            }
            CurNode = this;
            CurNode.setStyle("-fx-control-inner-background:"+backgroundColor+";"+"-fx-border-color: #FF1493;"+"-fx-border-radius: 2px;");
            if(event.getClickCount()==2){
                super.setCursor(Cursor.TEXT);
                super.setEditable(true);
            }
        });
        super.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                txt=TreeNode.super.getText();
                view.setValue(txt);
                //更新节点的宽度
                TreeNode.super.setPrefWidth(Math.max(TreeNode.super.getText().length()*24, RecW));
                TextLen=TreeNode.super.getPrefWidth();
                update(root,A1);
            }
        });
        //设置节点的背景颜色
        super.setAlignment(Pos.CENTER);
        super.setPrefHeight(Draw.RecH);
        super.setPrefWidth(this.TextLen);
        super.setStyle("-fx-control-inner-background:"+backgroundColor);
        super.setText(this.txt);
        super.setEditable(false);
        super.setOnMouseEntered(event->{
            super.setCursor(Cursor.DEFAULT);
            super.setStyle("-fx-control-inner-background:"+backgroundColor+";"+"-fx-border-color:#00BFFF;"+"-fx-border-radius: 2px;");
        });
        super.setOnMouseExited(event->{
            if(CurNode!=this) {
                this.setStyle("-fx-control-inner-background:"+backgroundColor+";");
            }
            else {
                this.setStyle("-fx-control-inner-background:"+backgroundColor+";"+"-fx-border-color:#FF1493;"+"-fx-border-radius: 2px;");
            }
        });
        view = new TreeViewItem(this.txt);
        view.setExpanded(true);
    }
    public Line getLine() {
        return line;
    }
    public int getType() {

        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public double getBlockLen() {
        return BlockLen;
    }
    public void setBlockLen(double blockLen) {
        BlockLen = blockLen;
    }

    public int getSonSize() {
        return SonSize;
    }

    public void setSonSize(int sonSize) {
        SonSize = sonSize;
    }
}
