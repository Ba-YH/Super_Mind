package com.cs1.super_mind;

import javafx.scene.layout.AnchorPane;

public abstract class Pane {
    public static void update_pane(AnchorPane A, TreeNode root) {
        // 计算根节点在垂直方向上的布局位置
        double last = Math.min(root.getLayoutY() + Draw.RecH / 2 - TreeNode.LBlockLen / 2, root.getLayoutY() + Draw.RecH / 2 - TreeNode.RBlockLen / 2);
        // 如果位置小于0，则调整根节点的布局位置使其不小于0
        if (last < 0)
            root.setLayoutY(root.getLayoutY() + Math.abs(last));
        last = Math.min(310 + Draw.RecH / 2 - TreeNode.LBlockLen / 2, 310 + Draw.RecH / 2 - TreeNode.RBlockLen / 2);
        // 如果位置大于0，则将根节点的布局位置设为310
        if (last > 0)
            root.setLayoutY(310);
        last = Math.max(TreeNode.LBlockLen / 2, TreeNode.RBlockLen / 2);
        // 如果位置大于310，则将根节点的布局位置设为last
        if (last > 310)
            root.setLayoutY(last);
        last = Math.max(root.getLayoutY() + Draw.RecH / 2 + TreeNode.LBlockLen / 2, root.getLayoutY() + Draw.RecH / 2 + TreeNode.RBlockLen / 2);
        // 设置 AnchorPane 的高度，取最大值为693
        A.setPrefHeight(Math.max(last, 693));
        // 计算根节点在水平方向上的布局位置
        double l = root.getLayoutX() - TreeNode.LMaxLinkLen;
        // 如果位置小于0，则调整根节点的布局位置使其不小于0
        if (l < 0)
            root.setLayoutX(root.getLayoutX() + Math.abs(l));
        l = 500 - TreeNode.LMaxLinkLen;
        // 如果位置大于0，则将根节点的布局位置设为500
        if (l > 0) root.setLayoutX(500);
        // 如果最大链长大于500，则将根节点的布局位置设为最大链长
        if (TreeNode.LMaxLinkLen > 500)
            root.setLayoutX(TreeNode.LMaxLinkLen);
        double r = root.getLayoutX() + root.getTextLen() + TreeNode.RMaxLinkLen;
        // 设置 AnchorPane 的宽度，取最大值为1043
        A.setPrefWidth(Math.max(r, 1043));
    }
}
