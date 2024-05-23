package com.cs1.super_mind;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTreeView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.cs1.super_mind.Draw.*;
import static com.cs1.super_mind.TreeNode.*;

public class Controller implements Initializable {
    @FXML
    private AnchorPane A1;// 主操作面板
    static TreeNode root;
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
    JFXButton Save_button;
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
    public static double orgSceneX = 500, orgSceneY = 310;// 滚动条初始位置
    private Stage stage;// 应用程序的Stage对象
    private int number;// 节点在树形视图中唯一ID
    private final Map<String, Runnable> keyActionMap = new HashMap<>();// 快捷键Hash表

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeKeyActionMap();
        //初始化根节点
        root = new TreeNode("根节点");
        Scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);//滚动条
        root.setIsroot(true);
        root.setLayoutX(orgSceneX);
        root.setLayoutY(orgSceneY);
        A1.getChildren().add(root);
        root.initNode(root, A1);
        treeview.setRoot(root.getView());
        Menubar.setSpacing(10);

        //添加子节点按键
        AddSon_Button.setOnAction(event -> {
            if (CurNode == null) {
                setHint(Hint, "请选择一个节点");
                return;
            }
            TreeNode tmp = new TreeNode("子节点");
            tmp.initNode(root, A1);

            if (CurNode.isRoot()) {
                if (getLchildren().size() < getRchildren().size()) {
                    getLchildren().add(tmp);
                    tmp.setType(-1);
                } else {
                    getRchildren().add(tmp);
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
            tmp.getView().setId(++number);
            CurNode.getView().getChildren().add(tmp.getView());//添加视图
            update(root, A1);
        });

        //添加兄弟节点按键
        AddBro_Button.setOnAction(event -> {
            if (CurNode == null) {
                setHint(Hint, "请选择一个节点");
                return;
            }
            if (CurNode.isRoot()) {
                setHint(Hint, "根节点无法添加兄弟节点！");
                return;
            }
            TreeNode tmp = new TreeNode("子节点");
            tmp.initNode(root, A1);
            if (CurNode.getparent().isRoot()) {
                if (CurNode.getType() == -1) {
                    getLchildren().add(tmp);
                    tmp.setType(-1);
                } else {
                    getRchildren().add(tmp);
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
            tmp.getView().setId(++number);
            CurNode.getparent().getView().getChildren().add(tmp.getView());//添加视图
            update(root, A1);
        });

        //删除节点按键
        Del_Button.setOnAction(event -> {
            if (CurNode == null) {
                setHint(Hint, "请选择一个节点");
                return;
            }
            if (CurNode.isRoot()) {
                setHint(Hint, "根节点无法被删除");
                return;
            }
            DelNode(CurNode, A1);
            if (CurNode.getparent().isRoot()) {
                if (getLchildren().contains(CurNode)) {
                    getLchildren().remove(CurNode);
                    root.getView().getChildren().remove(CurNode.getView());
                } else {
                    getRchildren().remove(CurNode);
                    root.getView().getChildren().remove(CurNode.getView());
                }
            } else {
                CurNode.getparent().getchildren().remove(CurNode);
                CurNode.getparent().getView().getChildren().remove(CurNode.getView());
            }
            update(root, A1);
            CurNode = null;
        });

        //左布局按键
        left_layout_button.setOnAction(event -> {
            for (TreeNode tmp : getRchildren()) {
                getLchildren().add(tmp);
            }
            getRchildren().clear();
            update(root, A1);
        });

        //右布局按键
        right_layout_button.setOnAction(event -> {
            for (TreeNode tmp : getLchildren()) {
                getRchildren().add(tmp);
            }
            getLchildren().clear();
            update(root, A1);
        });

        //右布局按键
        Automatic_layout_button.setOnAction(event -> {
            GetDp();
            update(root, A1);
        });

        //打开文件按键
        Open_button.setOnAction(event -> {
            Stage tmpstage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("打开");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("super_mind files", "*.smind"));
            FileManger fm = new FileManger();
            TreeNode tmp = null;
            File file = fileChooser.showOpenDialog(tmpstage);
            stage.setTitle(file.getName());
            if (tmp != null) {
                A1.getChildren().clear();
                root = tmp;
                root.initNode(root, A1);
                A1.getChildren().add(root);
                treeview.setRoot(root.getView());
                for (TreeNode tmp1 : getRchildren()) {
                    reload(root, tmp1, A1);
                }
                for (TreeNode tmp1 : getLchildren()) {
                    reload(root, tmp1, A1);
                }
                update(root, A1);
                setHint(Hint, "文件打开成功");
                CurNode = null;
            } else {
                setHint(Hint, "文件打开失败，文件已经损坏");
            }
        });

        //保存文件按键
        Save_button.setOnAction(event -> {
            FileManger fm = new FileManger();
            Stage tmpstage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("保存");
            fileChooser.setInitialFileName("test1");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("super_mind files", "*.smind"));
            File file = fileChooser.showSaveDialog(tmpstage);
            stage.setTitle(file.getName());
            if (file == null) return;
            try {
                fm.Save_File(root, file);
            } catch (Exception e) {
                //不使用printStackTrace
                setHint(Hint, "文件保存失败");
            }
        });

        //新建文件按键
        New_button.setOnAction(event -> {
            RecH = 48;
            RecW = RecH * 2.25;
            length_dis = 200;
            //新建前保存
            Save_button.fire();
            getRchildren().clear();
            getLchildren().clear();
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

        //导出图像按键
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
                setHint(Hint, "导出成功");
            }
        });

        //更改外观按键
        Appearance_button.setOnAction(actionEvent -> {
            //网格布局
            GridPane gridPane = new GridPane();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setVgap(10);
            gridPane.setHgap(10);
            gridPane.setPadding(new Insets(10));


            Label label1 = new Label("请选择节点颜色：");
            GridPane.setConstraints(label1, 0, 0);

            ColorPicker colorPicker = new ColorPicker();
            colorPicker.setValue(Color.valueOf(backgroundColor));
            GridPane.setConstraints(colorPicker, 1, 0);

            Label label2 = new Label("请选择字体");
            GridPane.setConstraints(label2, 0, 1);

            ComboBox<String> fontComboBox = new ComboBox<>();
            fontComboBox.getItems().addAll("微软雅黑", "Arial", "Times New Roman", "Courier New");
            fontComboBox.setValue("微软雅黑");
            GridPane.setConstraints(fontComboBox, 1, 1);


            Button button = new Button("确定");
            button.setMaxWidth(Double.MAX_VALUE);
            GridPane.setConstraints(button, 0, 2, 2, 1);

            gridPane.getChildren().addAll(label1, colorPicker, label2, fontComboBox, button);
            Scene scene = new Scene(gridPane, 400, 200);
            Stage window = new Stage();
            window.getIcons().add(new Image("img/icon.png"));
            window.setScene(scene);
            window.setTitle("外观设置");
            window.show();
            button.setOnAction(actionEvent1 -> {
                        String selectedFont = fontComboBox.getValue();
                        TreeNode.font_type = selectedFont;

                        Color color = colorPicker.getValue();
                        String colorHex = toHex(color);
                        backgroundColor = colorHex;
                        //borderColor = toHex(getComplementaryColor(color));
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
                        for (Node node : A1.getChildren()) {
                            node.setStyle(style2);
                        }
                        //确定自动关闭窗口
                        window.close();
                    }
            );
        });

        //面板获取焦点
        Scrollpane.setOnMouseClicked(event -> {
            //收起文件菜单
            if (Menubar.isExpanded()) {
                File_button.fire();
            }
            //选中节点失去焦点
            if (CurNode != null) {
                CurNode.setEditable(false);
                CurNode.setStyle(
                        "-fx-background-color:" + backgroundColor + ";" +
                                "-fx-background-radius:" + Integer.toString(radius)
                );
                CurNode = null;
            }
        });

        //按键键盘触发事件
        Scrollpane.setOnKeyPressed(event -> {
            shortcutSet(event);
        });

        //添加ctrl+wheel 事件实现缩放
        Scrollpane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown()) {
                Scrollpane.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
                    //向下滚动缩小
                    //tips:纵向块距不改变
                    double zoomAmplitude = 0.02; //缩放幅度
                    double scaleRatio = 3;       //块与间距的缩放比例，块大小缩放要更快
                    if (scrollEvent.getDeltaY() < 0) {
                        if (RecH > 10) {
                            RecH *= (1 - scaleRatio * zoomAmplitude);
                            RecW =RecH* 2.25;
                            length_dis = RecW * 2 * (1 - zoomAmplitude);
                            refresh(root);
                        }
                    } else {
                        if (RecH < 100) {
                            RecH *= (1 + scaleRatio * zoomAmplitude);
                            RecW =RecH* 2.25;
                            length_dis = RecW * 2 * (1 + zoomAmplitude);
                            refresh(root);
                        }
                    }
                });
            }
        });

        //实现目录树定位到节点
        treeview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TreeViewItem item = (TreeViewItem) newValue;
                int id = item.getId();
                TreeNode findNode = find(root, id);
                String style = "-fx-background-color:" + backgroundColor +
                        ";-fx-background-radius:" + Integer.toString(radius) + ";";
                String borderStyle = "-fx-border-width: 2px;-fx-control-inner-background:" + backgroundColor +
                        ";-fx-border-color:" + borderColor +
                        ";-fx-border-radius:" + Integer.toString(radius);

                if (CurNode != null) {
                    CurNode.setStyle(style);
                }
                CurNode = findNode;
                //播放动画实现闪烁效果
                Timeline blinkAnimation = new Timeline(
                        new KeyFrame(Duration.seconds(0.5),
                                event -> CurNode.setStyle(style + borderStyle)),
                        new KeyFrame(Duration.seconds(0.1),
                                event -> CurNode.setStyle(style))
                );
                blinkAnimation.setCycleCount(3);     // 闪烁两次，总共两秒
                blinkAnimation.setAutoReverse(true); // 使动画在第二次播放时逆向执行，回到初始状态
                blinkAnimation.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        CurNode.setStyle(style + borderStyle);
                    }
                });
                blinkAnimation.play();
            }
        });

        //在目录树选中节点也可直接操作节点
        treeview.setOnKeyPressed(event -> {
            shortcutSet(event);
        });
    }

    // 通过id查找TreeViewItem的节点
    TreeNode find(TreeNode cur, int id) {
        if (root.getView().getId() == id) return root;
        TreeNode tmp = null;
        //左边的所有二级节点
        for (TreeNode node : getRchildren()) {
            tmp = findSon(node, id);
            if (tmp != null) return tmp;
        }
        //右边的所有二级节点
        for (TreeNode node : getLchildren()) {
            tmp = findSon(node, id);
            if (tmp != null) return tmp;
        }
        return null;
    }

    // 通过id查找子节点
    TreeNode findSon(TreeNode curRoot, int id) {
        System.out.println(curRoot.getView().getId());
        if (curRoot.getView().getId() == id) {
            return curRoot;
        }
        TreeNode tmp;
        for (TreeNode node : curRoot.getchildren()) {
            tmp = findSon(node, id);
            if (tmp != null) return tmp;
        }
        return null;
    }

    // color转换Hex编码
    public static String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // 缩放后，不改变节点相对位置而刷新页面
    void refresh(TreeNode root) {
        A1.getChildren().clear();
        root.initNode(root, A1);
        A1.getChildren().add(root);
        //目录树要重新编号
        Draw.number = 0;
        treeview.setRoot(null);
        treeview.setRoot(root.getView());
        for (TreeNode node : getLchildren()) {
            reload(root, node, A1);
        }
        for (TreeNode node : getRchildren()) {
            reload(root, node, A1);
        }
        update(root, A1);
    }

    // 快捷键Hash表
    private void initializeKeyActionMap() {
        keyActionMap.put("CONTROL_SHIFT_ENTER", () -> AddBro_Button.fire());
        keyActionMap.put("CONTROL_SHIFT_DELETE", () -> Del_Button.fire());
        keyActionMap.put("CONTROL_SHIFT_L", () -> left_layout_button.fire());
        keyActionMap.put("CONTROL_SHIFT_R", () -> right_layout_button.fire());
        keyActionMap.put("CONTROL_SHIFT_A", () -> Automatic_layout_button.fire());
        keyActionMap.put("CONTROL_ENTER", () -> AddSon_Button.fire());
        keyActionMap.put("CONTROL_O", () -> Open_button.fire());
        keyActionMap.put("CONTROL_S", () -> Save_button.fire());
        keyActionMap.put("CONTROL_P", () -> Export_button.fire());
    }

    // 设置快捷键
    public void shortcutSet(KeyEvent event) {
        if (event.getCode() != null) {
            // 根据是否按下Control修饰键，添加"CONTROL_"前缀
            // 根据是否按下Shift修饰键，添加"SHIFT_"前缀
            // 添加键的名称，如"A", "S", "ENTER"等
            String keyCombination = (event.isControlDown() ? "CONTROL_" : "") +
                    (event.isShiftDown() ? "SHIFT_" : "") + event.getCode().name();
            // 在keyActionMap中查找当前键组合对应的动作
            Runnable action = keyActionMap.get(keyCombination);
            // 找到，执行
            if (action != null)
                action.run();
        }
    }
}