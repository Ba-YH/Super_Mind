package com.cs1.super_mind;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTreeView;
import com.sun.source.tree.Tree;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
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
    private JFXButton File_button;
    @FXML
    private JFXButton panebutton;
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
    @FXML
    private JFXButton Appearance_button;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //初始化根节点
        root = new TreeNode("根节点");
        Scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);//滚动条
        root.setIsroot(true);
        root.setLayoutX(500);
        root.setLayoutY(310);
        A1.getChildren().add(root);
        root.initNode(root, A1);
        treeview.setRoot(root.getView());
        Menubar.setSpacing(10);

        AddSon_Button.setOnAction(event -> {//添加节点按键
            if (CurNode == null) {
                Draw.setHint(Hint, "请选择一个节点");
                return;
            }
            TreeNode tmp = new TreeNode("子节点");
            tmp.initNode(root, A1);

            if (CurNode.isRoot()) {
                if (TreeNode.getLchildren().size() < TreeNode.getRchildren().size()) {
                    TreeNode.getLchildren().add(tmp);
                    tmp.setType(-1);
                } else {
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
            CurNode.getView().getChildren().add(tmp.getView());//添加视图
            Draw.update(root, A1);
        });
        AddBro_Button.setOnAction(event -> {
            if (CurNode == null) {
                Draw.setHint(Hint, "请选择一个节点");
                return;
            }
            if (CurNode.isRoot()) {
                Draw.setHint(Hint, "根节点无法添加兄弟节点！");
                return;
            }
            TreeNode tmp = new TreeNode("子节点");
            tmp.initNode(root, A1);
            if (CurNode.getparent().isRoot()) {
                if (CurNode.getType() == -1) {
                    TreeNode.getLchildren().add(tmp);
                    tmp.setType(-1);
                } else {
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
            CurNode.getparent().getView().getChildren().add(tmp.getView());//添加视图
            Draw.update(root, A1);
        });
        Del_Button.setOnAction(event -> {//删除节点按键
            if (CurNode == null) {
                Draw.setHint(Hint, "请选择一个节点");
                return;
            }
            if (CurNode.isRoot()) {
                Draw.setHint(Hint, "根节点无法被删除");
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
            Draw.update(root, A1);
            CurNode = null;
        });
        left_layout_button.setOnAction(event -> {
            for (TreeNode tmp : TreeNode.getRchildren()) {
                TreeNode.getLchildren().add(tmp);
            }
            TreeNode.getRchildren().clear();
            Draw.update(root, A1);
        });
        right_layout_button.setOnAction(event -> {
            for (TreeNode tmp : TreeNode.getLchildren()) {
                TreeNode.getRchildren().add(tmp);
            }
            TreeNode.getLchildren().clear();
            Draw.update(root, A1);
        });
        Automatic_layout_button.setOnAction(event -> {
            Draw.GetDp();
            Draw.update(root, A1);
        });
        Open_button.setOnAction(event -> {

            Stage tmpstage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("打开");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("super_mind files", "*.smind"));
            FileManger fm = new FileManger();
            TreeNode tmp = null;
            tmp = (TreeNode) fm.Open_File(fileChooser.showOpenDialog(tmpstage));
            if (tmp != null) {
                A1.getChildren().clear();
                root = tmp;
                root.initNode(root, A1);
                A1.getChildren().add(root);
                treeview.setRoot(root.getView());
                for (TreeNode tmp1 : TreeNode.getRchildren()) {
                    reload(root, tmp1, A1);
                }
                for (TreeNode tmp1 : TreeNode.getLchildren()) {
                    reload(root, tmp1, A1);
                }
                update(root, A1);
                Draw.setHint(Hint, "文件打开成功");
                CurNode = null;
            } else {
                Draw.setHint(Hint, "文件打开失败，文件已经损坏");
            }
        });
        Save_button.setOnAction(event -> {
            FileManger fm = new FileManger();
            Stage tmpstage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("保存");
            fileChooser.setInitialFileName("test1");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("super_mind files", "*.smind"));
            File file = fileChooser.showSaveDialog(tmpstage);
            if (file == null) return;
            try {
                fm.Save_File(root, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        New_button.setOnAction(event -> {
            //新建前保存
            Save_button.fire();
            TreeNode.getRchildren().clear();
            TreeNode.getLchildren().clear();
            A1.getChildren().clear();
            root = new TreeNode("根节点");
            root.initNode(root, A1);
            root.setIsroot(true);
            root.setLayoutX(500);
            root.setLayoutY(310);
            treeview.setRoot(root.getView());
            A1.getChildren().add(root);
            CurNode = null;
        });
        Export_button.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("导出");
            fileChooser.setInitialFileName("test1");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                FileManger fm = new FileManger();
                fm.export(A1, file);
                Draw.setHint(Hint, "导出成功");
            }
        });
        //添加外观按钮
        Appearance_button.setOnAction(actionEvent -> {
            HashMap<String, String> color = new HashMap<>();
            color.put("Red", "#FF0000");
            color.put("Yellow", "#F9AA33");
            color.put("Bule", "#0000FF");
            ComboBox<String> colorComboBox = new ComboBox<>();
            colorComboBox.getItems().addAll("Red", "Yellow", "Blue");
            colorComboBox.setPromptText("请选择按钮与节点颜色");

            ComboBox<String> fontComboBox = new ComboBox<>();
            fontComboBox.getItems().addAll("Arial", "Times New Roman", "Courier New");
            fontComboBox.setPromptText("请选择字体大小");


            Button button = new Button("确定");
            button.setOnAction(actionEvent1 -> {
                    String selectedFont = fontComboBox.getValue();
                    TreeNode.font_type = selectedFont;

                    String selcetedColor = colorComboBox.getValue();
                    String colorHex = color.get(selcetedColor);
                    TreeNode.backgroundColor = colorHex;
                    String style = "-fx-background-color: " + colorHex + "; -fx-background-radius: 10px;";
                    String style2 = "-fx-background-color:" + colorHex;
                    //改变按钮颜色
                    AddBro_Button.setStyle(style);
                    AddSon_Button.setStyle(style);
                    Del_Button.setStyle(style);
                    File_button.setStyle(style);
                    Appearance_button.setStyle(style);
                    Save_button.setStyle(style2);
                    Open_button.setStyle(style2);
                    Export_button.setStyle(style2);
                    New_button.setStyle(style2);
                    //改变节点颜色
                    root.setStyle(style2);
                    for (TreeNode leftNode : root.getLchildren()) {
                        dfs(leftNode, style);
                    }
                    for (TreeNode rightNode : root.getRchildren()) {
                        dfs(rightNode, style);
                    }

                }
            );
            VBox vBox = new VBox(10);
            vBox.getChildren().addAll(colorComboBox, fontComboBox, button);
            Scene scene = new Scene(vBox, 200, 150);
            Stage window = new Stage();
            window.setScene(scene);
            window.setTitle("外观设置");
            window.show();
        });
        //鼠标悬停打开按钮
        /*
        File_button.setOnMouseEntered(event -> {
            File_button.fire();
        })
         */
        //面板获取焦点退出
        Scrollpane.setOnMouseClicked(event -> {
            if (Menubar.isExpanded()) {
                File_button.fire();
            }
        });
        //为面板设立按钮的键盘触发事件
        Scrollpane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && event.isShiftDown() && event.isControlDown()) {
                AddBro_Button.fire();
            } else if (event.getCode() == KeyCode.ENTER && event.isControlDown()) {
                AddSon_Button.fire();
            } else if (event.getCode() == KeyCode.DELETE && event.isShiftDown() && event.isControlDown()) {
                Del_Button.fire();
            } else if (event.getCode() == KeyCode.O && event.isControlDown()) {
                Open_button.fire();
            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                Save_button.fire();
            } else if (event.getCode() == KeyCode.P && event.isControlDown()) {
                Export_button.fire();
            } else if (event.getCode() == KeyCode.L && event.isControlDown() && event.isShiftDown()) {
                left_layout_button.fire();
            } else if (event.getCode() == KeyCode.R && event.isControlDown() && event.isShiftDown()) {
                right_layout_button.fire();
            } else if (event.getCode() == KeyCode.A && event.isControlDown() && event.isShiftDown()) {
                Automatic_layout_button.fire();
            }
        });
    }

    //递归改变所有节点颜色
    void dfs(TreeNode curNode, String style) {
        curNode.setStyle(style);
        for(TreeNode node : curNode.getchildren()){
            node.setStyle(style);
            dfs(curNode,style);
        }
    }
}