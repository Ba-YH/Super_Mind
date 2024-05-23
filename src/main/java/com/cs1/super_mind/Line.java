package com.cs1.super_mind;

import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

import java.io.Serializable;

public class Line extends CubicCurve implements Serializable {
    public void SetLine(double startX, double startY, double endX, double endY) {
        // 设置贝塞尔曲线起点的 X 坐标
        super.setStartX(startX);
        // 设置贝塞尔曲线起点的 Y 坐标
        super.setStartY(startY);
        // 设置贝塞尔曲线终点的 X 坐标
        super.setEndX(endX);
        // 设置贝塞尔曲线终点的 Y 坐标
        super.setEndY(endY);
        // 设置贝塞尔曲线控制点1的 X 坐标为起点和终点X坐标的中点
        super.setControlX1((startX + endX) / 2);
        // 设置贝塞尔曲线控制点1的 Y 坐标为起点Y坐标
        super.setControlY1(startY);
        // 设置贝塞尔曲线控制点2的 X 坐标为起点和终点X坐标的中点
        super.setControlX2((startX + endX) / 2);
        // 设置贝塞尔曲线控制点2的 Y 坐标为终点Y坐标
        super.setControlY2(endY);
        // 设置贝塞尔曲线的线宽
        super.setStrokeWidth(1);
        // 设置贝塞尔曲线的填充颜色为透明
        super.setFill(Color.TRANSPARENT);
        // 设置贝塞尔曲线的描边颜色为黑色
        super.setStroke(Color.BLACK);
    }
}
