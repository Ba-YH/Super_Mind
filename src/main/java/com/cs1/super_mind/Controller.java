package com.cs1.super_mind;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTreeView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.cs1.super_mind.Draw.*;

public class Controller implements Initializable {
    @FXML
    private AnchorPane A1;//添加节点的地方
    private static TreeNode root;
    @FXML
    private JFXTreeView treeview;
    @FXML
    private JFXButton AddSon_Button;
    @FXML
    private JFXButton AddBro_Button;
    @FXML
    private JFXButton Del_Button;
    @FXML
    private JFXButton left_layout_button;
    @FXML
    private JFXButton right_layout_button;
    @FXML
    private JFXButton Automatic_layout_button;
    @FXML
    private ScrollPane Scrollpane;
    @FXML
    private JFXButton Save_button;
    @FXML
    private JFXButton Open_button;
    @FXML 
    private JFXButton New_button;
    @FXML
    private JFXButton Export_button;
    @FXML
    private JFXNodesList Menubar;
    @FXML
    private Label Hint;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //初始化根节点
        root = new TreeNode("主题1");
        Scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        root.setIsroot(true);
        root.setLayoutX(500);
        root.setLayoutY(310);
        A1.getChildren().add(root);
        root.initNode(root,A1);
        treeview.setRoot(root.getView());
        Menubar.setSpacing(10);
        //
        AddSon_Button.setOnAction(event -> {//添加节点按键
            if (CurNode == null) {
                Draw.setHint(Hint,"请选择一个节点");
                return;
            }
            TreeNode tmp = new TreeNode("子主题");
            tmp.initNode(root,A1);
            if (CurNode.isRoot()) {
                if(TreeNode.getLchildren().size()<TreeNode.getRchildren().size()){
                    TreeNode.getLchildren().add(tmp);
                    tmp.setType(-1);
                }
                else {
                    TreeNode.getRchildren().add(tmp);
                    tmp.setType(1);
                }
                tmp.setParent(CurNode);
            } else {
                CurNode.getchildren().add(tmp);
                tmp.setParent(CurNode);
                tmp.setType(CurNode.getType());
            }
            A1.getChildren().add(tmp);//添加节点
            A1.getChildren().add(tmp.getLine());//添加线
            CurNode.getView().getChildren().add(tmp.getView());//添加试图
            Draw.update(root,A1);
        });
        AddBro_Button.setOnAction(event -> {
            if (CurNode == null) {
                Draw.setHint(Hint,"请选择一个节点");
                return;
            }
            if(CurNode.isRoot()){
                Draw.setHint(Hint,"根节点无法添加兄弟节点！");
                return;
            }
            TreeNode tmp = new TreeNode("子主题");
            tmp.initNode(root,A1);
            if (CurNode.getparent().isRoot()) {
                if(CurNode.getType()==-1){
                    TreeNode.getLchildren().add(tmp);
                    tmp.setType(-1);
                }
                else {
                    TreeNode.getRchildren().add(tmp);
                    tmp.setType(1);
                }
                tmp.setParent(CurNode.getparent());
            } else {
                CurNode.getparent().getchildren().add(tmp);
                tmp.setParent(CurNode.getparent());
                tmp.setType(CurNode.getType());
            }
            A1.getChildren().add(tmp);//添加节点
            A1.getChildren().add(tmp.getLine());//添加线
            CurNode.getparent().getView().getChildren().add(tmp.getView());//添加试图
            Draw.update(root,A1);
        });
        Del_Button.setOnAction(event -> {//删除节点按键
            if(CurNode==null){
                Draw.setHint(Hint,"请选择一个节点");
                return;
            }
            if(CurNode.isRoot()){
                Draw.setHint(Hint,"根节点无法被删除");
                return;
            }
            Draw.DelNode(CurNode, A1);
            if (CurNode.getparent().isRoot()) {
                if (TreeNode.getLchildren().contains(CurNode)) {
                    TreeNode.getLchildren().remove(CurNode);
                    root.getView().getChildren().remove(CurNode.getView());
                } else {
                    TreeNode.getRchildren().remove(CurNode);
                    root.getView().getChildren().remove(CurNode.getView());
                }
            } else {
                CurNode.getparent().getchildren().remove(CurNode);
                CurNode.getparent().getView().getChildren().remove(CurNode.getView());
            }
            Draw.update(root,A1);
            CurNode=null;
        });
        left_layout_button.setOnAction(event ->{
            for(TreeNode tmp: TreeNode.getRchildren()){
                TreeNode.getLchildren().add(tmp);
            }
            TreeNode.getRchildren().clear();
            Draw.update(root,A1);
        });
        right_layout_button.setOnAction(event ->{
            for(TreeNode tmp: TreeNode.getLchildren()){
                TreeNode.getRchildren().add(tmp);
            }
            TreeNode.getLchildren().clear();
            Draw.update(root,A1);
        });
        Automatic_layout_button.setOnAction(event ->{
            /*
            while(TreeNode.getLchildren().size()>TreeNode.getRchildren().size()){
                TreeNode.getRchildren().add(TreeNode.getLchildren().get(TreeNode.getLchildren().size()-1));
                TreeNode.getLchildren().remove(TreeNode.getLchildren().size()-1);
            }
            while(TreeNode.getLchildren().size()<TreeNode.getRchildren().size()){
                TreeNode.getLchildren().add(TreeNode.getRchildren().get(TreeNode.getRchildren().size()-1));
                TreeNode.getRchildren().remove(TreeNode.getRchildren().size()-1);
            }*/
            Draw.GetDp();
            Draw.update(root,A1);
        });
        Open_button.setOnAction(event ->{
            Stage tmpstage=new Stage();
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("打开");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CCmind files","*.cmid"));
            FileManger fm=new FileManger();
            TreeNode tmp=null;
            tmp= (TreeNode) fm.Open_File(fileChooser.showOpenDialog(tmpstage));
            if(tmp!=null){
                A1.getChildren().clear();
                root=tmp;
                root.initNode(root,A1);
                A1.getChildren().add(root);
                treeview.setRoot(root.getView());
                for(TreeNode tmp1:TreeNode.getRchildren()){
                    reload(root,tmp1,A1);
                }
                for(TreeNode tmp1:TreeNode.getLchildren()){
                    reload(root,tmp1,A1);
                }
                update(root,A1);
                Draw.setHint(Hint,"文件打开成功");
                CurNode=null;
            }
            else{
                Draw.setHint(Hint,"文件打开失败，文件已经损坏");
            }
        });
        Save_button.setOnAction(event->{
            FileManger fm=new FileManger();
            Stage tmpstage=new Stage();
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("保存");
            fileChooser.setInitialFileName("test1");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CCmind files","*.cmid"));
            File file=fileChooser.showSaveDialog(tmpstage);
            if(file==null)return;
            try{
                fm.Save_File(root,file);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        New_button.setOnAction(event->{
            TreeNode.getRchildren().clear();
            TreeNode.getLchildren().clear();
            A1.getChildren().clear();
            root=new TreeNode("主题1");
            root.initNode(root,A1);
            root.setIsroot(true);
            root.setLayoutX(500);
            root.setLayoutY(310);
            treeview.setRoot(root.getView());
            A1.getChildren().add(root);
            CurNode=null;
        });
        Export_button.setOnAction(event->{
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("导出");
            fileChooser.setInitialFileName("test1");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG","*.png"),
                    new FileChooser.ExtensionFilter("JPG","*.jpg"));
            File file=fileChooser.showSaveDialog(new Stage());
            if(file!=null){
                FileManger fm=new FileManger();
                fm.export(A1,file);
                Draw.setHint(Hint,"导出成功");
            }
        });
    }

}