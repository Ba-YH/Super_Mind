package com.cs1.super_mind;

import com.sun.source.tree.Tree;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cs1.super_mind.Draw.*;

public class TreeNode extends TextField implements Serializable {
    public static String backgroundColor = "#F9AA33";//节点背景颜色
    public static String borderColor = "#FF1493";//边框颜色
    public static int radius = 8;// 边框圆角大小
    public static double LBlockLen = Draw.RecH;// 左子树块长度
    public static double RBlockLen = Draw.RecH;// 右子树块长度
    public static String font_type = "微软雅黑";// 字体类型

    public static double LMaxLinkLen;//最大左子链长度
    public static double RMaxLinkLen;//最大右子链长度
    private static ArrayList<TreeNode> Lchildren = new ArrayList<>();//根节点的左子树
    private static ArrayList<TreeNode> Rchildren = new ArrayList<>();//根节点的右子树

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


    private double BlockLen;//块长度
    private int SonSize;//子节点总数
    private double TextLen;//文本宽度
    private double MaxLinkLen;//最长子链
    private int type;//该节点向左还是向右 type = -1 向左，= 1 向右
    private boolean isroot;//是否根节点
    private TreeNode parent;//父节点
    private TreeViewItem view;//该节点在TreeView上的视图
    private ArrayList<TreeNode> children;//子节点列表
    private Line line;//连线
    private String txt;//文本

    TreeNode(String txt1) {
        setTxt(txt1);
        BlockLen = Draw.RecH;
        super.setPrefHeight(RecH);
        super.setPrefWidth(RecW);
        setTextLen(108);
        type = 1;
        children = new ArrayList<>();
        line = new Line();
        this.setFont(new Font(font_type, 20));
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

    //初始化节点
    public void initNode(TreeNode root, AnchorPane A1) {
        //默认节点样式，使用原子类安全的更新全局共享状态
        AtomicReference<String> defaultStyle = new AtomicReference<>("-fx-background-color:" + backgroundColor
            + ";-fx-background-radius:" + Integer.toString(radius) + ";");//必须带分号，后面还要添加

        this.setOnMouseClicked(event -> {
            defaultStyle.set("-fx-background-color:" + backgroundColor
                + ";-fx-background-radius:" + Integer.toString(radius) + ";");
            String borderStyle = "-fx-border-width: 2px;-fx-control-inner-background:" + backgroundColor +
                ";-fx-border-color:" + borderColor + ";-fx-border-radius:" + Integer.toString(radius);
            System.out.println("当前节点 type属性："+this.type);
            if (CurNode != null) {
                //上一个选中节点回到默认样式
                CurNode.setEditable(false);
                CurNode.setStyle(defaultStyle.get());
            }
            //选中就给当前节点加上边框样式
            CurNode = this;
            CurNode.setStyle(defaultStyle + borderStyle);
            if (event.getClickCount() == 2) {
                super.setCursor(Cursor.TEXT);
                super.setEditable(true);
            }
        });
        //选中节点 + enter实现编辑
        this.setOnKeyPressed(event -> {
            //检测是否按下ctrl 避免冲突
            if (CurNode != null && event.getCode() == KeyCode.ENTER && !event.isControlDown()) {
                //回车可编辑，再次回车编辑完成
                if (!super.isEditable()) {
                    super.setCursor(Cursor.TEXT);
                    super.setEditable(true);
                } else {
                    super.setEditable(false);
                    super.setStyle(defaultStyle.get());
                }
            }
        });

        //文本变化监听器
        super.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                txt = TreeNode.super.getText();
                view.setValue(txt);
                String text=TreeNode.super.getText();
                //更新节点的宽度
                TreeNode.super.setPrefWidth(Math.max(getvisualLength(text) * 26, RecW));
                TextLen = TreeNode.super.getPrefWidth();
                update(root, A1);
            }
        });
        //设置节点的背景颜色
        super.setAlignment(Pos.CENTER);
        super.setPrefHeight(RecH);
        super.setPrefWidth(this.TextLen);
        super.setStyle(defaultStyle.get());
        super.setText(this.txt);
        super.setFont(new Font(font_type,20));
        super.setEditable(false);
        //删除鼠标悬停事件，节点只有点击事件
        view = new TreeViewItem(this.txt);
        view.setExpanded(true);
    }
    public int getvisualLength(String text){
            Pattern pattern = Pattern.compile("[a-zA-Z]");
            //[\u4E00-\u9FA5]是unicode2的中文区间
            Matcher matcher = pattern.matcher(text);
            String Chinese = matcher.replaceAll("");
            return Chinese.length()+(text.length()-Chinese.length())/2;
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
