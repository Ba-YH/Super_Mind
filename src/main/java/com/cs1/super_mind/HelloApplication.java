package com.cs1.super_mind;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 创建场景
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
        // 控制器类中使用stage
        Controller controller = fxmlLoader.getController();
        controller.setStage(stage);
        Image icon =new Image("img/icon.png");
        stage.getIcons().add(icon);
        stage.setTitle("无标题.smind");
        stage.setScene(scene);
        stage.show();

        // 关闭主窗口时弹出保存对话框
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Super_Mind");
            String title = stage.getTitle();
            String filename = title.substring(0, title.indexOf("."));
            alert.setHeaderText("你想将更改保存到 " + filename + " 吗");

            ButtonType saveButton = new ButtonType("保存");
            ButtonType unSaveButton = new ButtonType("不保存");
            ButtonType cancellButton = new ButtonType("取消");
            alert.getButtonTypes().setAll(saveButton, unSaveButton, cancellButton);

            // 显示对话框并等待用户选择
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType != saveButton) {
                    if (buttonType == unSaveButton)
                        // 如果用户选择“不保存”，则关闭舞台
                        stage.close();
                    else
                        // 如果用户选择“取消”，则阻止关闭事件
                        event.consume();
                } else {
                    // 如果用户选择“保存”，则触发保存操作，并关闭舞台
                    controller.Save_button.fire();
                    stage.close();
                }
            });
        });
    }

    public static void main(String[] args) {
        launch();
    }
}