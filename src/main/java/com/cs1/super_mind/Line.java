package com.cs1.super_mind;

import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

import java.io.Serializable;

public class Line extends CubicCurve implements Serializable {
    public void SetLine(double startX,double startY,double endX,double endY){
        super.setStartX(startX);super.setStartY(startY);
        super.setEndX(endX);super.setEndY(endY);
        super.setControlX1((startX+endX)/2);super.setControlY1(startY);
        super.setControlX2((startX+endX)/2);super.setControlY2(endY);
        super.setStrokeWidth(1);
        super.setFill(Color.TRANSPARENT);
        super.setStroke(Color.BLACK);
        //super.setStroke(Color.web(String.valueOf(Color.YELLOW)));
    }
}
