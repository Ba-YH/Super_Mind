package com.cs1.super_mind;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.ArrayList;

public class FileManger {
    // 保存文件
    public void Save_File(TreeNode a, File file) {
        WriteObject(a, file);
    }

    // 打开文件
    public Object Open_File(File file) {
        return readObject(file);
    }

    // 写入
    private void WriteObject(Object obj, File file) {
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);// 文件输出流
            ObjectOutputStream objout = new ObjectOutputStream(out);// 对象输出流
            // 序列化并写入文件多个属性和整个树节点
            objout.writeObject(TreeNode.LMaxLinkLen);
            objout.writeObject(TreeNode.RMaxLinkLen);
            objout.writeObject(TreeNode.LBlockLen);
            objout.writeObject(TreeNode.RBlockLen);
            objout.writeObject(TreeNode.getLchildren());
            objout.writeObject(TreeNode.getRchildren());
            objout.writeObject(obj);
            objout.flush();
            objout.close();
            System.out.println("write success");
        } catch (IOException e) {
            System.out.println("write failed");
            e.printStackTrace();
        }
    }

    // 读出
    private Object readObject(File file) {
        Object tmp = null;
        FileInputStream in;
        try {
            in = new FileInputStream(file);// 文件输入流
            ObjectInputStream objin = new ObjectInputStream(in);// 对象输入流
            // 反序列化并读取文件中的属性
            TreeNode.LMaxLinkLen = (double) objin.readObject();
            TreeNode.RMaxLinkLen = (double) objin.readObject();
            TreeNode.LBlockLen = (double) objin.readObject();
            TreeNode.RBlockLen = (double) objin.readObject();
            TreeNode.setLchildren((ArrayList<TreeNode>) objin.readObject());
            TreeNode.setRchildren((ArrayList<TreeNode>) objin.readObject());
            tmp = objin.readObject();
            objin.close();
            System.out.println("read success");
        } catch (IOException e) {
            System.out.println("read failed");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return tmp;
    }

    // 导出图像
    public void export(AnchorPane A1, File file) {
        // 创建AnchorPane的快照
        WritableImage image = A1.snapshot(new SnapshotParameters(), null);
        try {
            // 将快照写入文件，保存为PNG格式
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
            System.out.println("保存成功");
        } catch (IOException ex) {
            System.out.println("保存失败" + ex.getMessage());
        }
    }
}
