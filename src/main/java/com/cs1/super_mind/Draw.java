package com.cs1.super_mind;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public abstract class Draw {
    public static  double RecH = 48;//节点高
    public static  double RecW = 108;//节点宽
    public static  double length_dis = 200; //横向间距
    public static  final double Block_dis = 24;//纵向间距固定
    public static TreeNode CurNode;
    public static int number;
    /**
     * 更新整棵树的所有节点的块上下大小
     *
     * @param a 根节点
     */
    public static void update_len(TreeNode a) {
        double len = 0; // 初始化长度为0
        if (a.isRoot()) { // 如果传入的节点是根节点
            TreeNode.LMaxLinkLen = 0; // 初始化左子树的最长子链长度为0
            // 遍历根节点的左子树
            for (TreeNode tmp : TreeNode.getLchildren()) {
                tmp.setType(-1); // 设置节点类型为向左
                update_len(tmp); // 递归更新左子节点的块大小
                // 计算左子树的最长子链长度，包括节点本身的文本长度以及节点之间的距离
                TreeNode.LMaxLinkLen = Math.max(TreeNode.LMaxLinkLen, tmp.getMaxLinkLen() + (length_dis - Draw.RecW) + tmp.getTextLen());
                len += tmp.getBlockLen(); // 累加左子树的块长度
            }
            len += (TreeNode.getLchildren().size() - 1) * Block_dis; // 考虑节点之间的距离
            TreeNode.LBlockLen = (max(len, RecH)); // 更新左子树的块上下大小为累加的长度或者固定高度中的较大值
            len = 0; // 重置长度为0
            TreeNode.RMaxLinkLen = 0; // 初始化右子树的最长子链长度为0
            // 遍历根节点的右子树
            for (TreeNode tmp : TreeNode.getRchildren()) {
                tmp.setType(1); // 设置节点类型为向右
                update_len(tmp); // 递归更新右子节点的块大小
                // 计算右子树的最长子链长度，包括节点本身的文本长度以及节点之间的距离
                TreeNode.RMaxLinkLen = Math.max(TreeNode.RMaxLinkLen, tmp.getMaxLinkLen() + (length_dis - Draw.RecW) + tmp.getTextLen());
                len += tmp.getBlockLen(); // 累加右子树的块长度
            }
            len += (TreeNode.getRchildren().size() - 1) * Block_dis; // 考虑节点之间的距离
            TreeNode.RBlockLen = (max(len, RecH)); // 更新右子树的块上下大小为累加的长度或者固定高度中的较大值
        } else { // 如果传入的节点不是根节点
            a.setMaxLinkLen(0); // 初始化节点的最长子链长度为0
            // 遍历当前节点的子节点
            for (TreeNode tmp : a.getchildren()) {
                tmp.setType(a.getType()); // 设置子节点的类型与当前节点相同
                update_len(tmp); // 递归更新子节点的块大小
                // 更新节点的最长子链长度为子节点的最长子链长度与节点之间的距离及节点本身文本长度的较大值
                a.setMaxLinkLen(max(a.getMaxLinkLen(), tmp.getMaxLinkLen() + (length_dis - Draw.RecW) + tmp.getTextLen()));
                len += tmp.getBlockLen(); // 累加子节点的块长度
            }
            len += (a.getchildren().size() - 1) * Block_dis; // 考虑子节点之间的距离
            a.setBlockLen(max(len, RecH)); // 更新节点的块上下大小为累加的长度或者固定高度中的较大值
        }
    }

    /**
     * 更新树节点在图上的位置
     *
     * @param a  当前节点
     * @param op 操作类型，1表示更新右子树位置，-1表示更新左子树位置，0表示更新当前节点的子节点位置
     */
    public static void update_pos(TreeNode a, int op) {
        // 如果当前节点不是根节点且没有子节点，则直接返回
        if (!a.isRoot() && a.getchildren().isEmpty()) return;

        // 根据操作类型设置当前节点的块大小
        if (op == 1) a.setBlockLen(TreeNode.RBlockLen);
        if (op == -1) a.setBlockLen(TreeNode.LBlockLen);

        // 计算当前节点的上一个节点的位置
        double last = a.getLayoutY() - a.getBlockLen() / 2;//块上界

        // 根据操作类型更新节点位置
        if (op != 0) {
            // 更新左子树位置
            if (op == -1) {
                // 遍历左子树节点，更新节点位置并获取新的上一个节点位置
                for (TreeNode tmp : TreeNode.getLchildren()) {
                    last = SetSon(a, last, tmp);
                }
                // 递归更新左子树节点的位置
                for (TreeNode tmp : TreeNode.getLchildren()) {
                    update_pos(tmp, 0);
                }
            } else { // 更新右子树位置
                // 遍历右子树节点，更新节点位置并获取新的上一个节点位置
                for (TreeNode tmp : TreeNode.getRchildren()) {
                    last = SetSon(a, last, tmp);
                }
                // 递归更新右子树节点的位置
                for (TreeNode tmp : TreeNode.getRchildren()) {
                    update_pos(tmp, 0);
                }
            }
        } else { // 更新当前节点子节点的位置
            // 遍历当前节点的子节点，更新节点位置并获取新的上一个节点位置
            for (TreeNode tmp : a.getchildren()) {
                last = SetSon(a, last, tmp);
            }
            // 递归更新子节点的位置
            for (TreeNode tmp : a.getchildren()) {
                update_pos(tmp, 0);
            }
        }
    }

    /**
     * 设置节点的位置，并绘制节点之间的连线
     * @param a 父节点
     * @param last 上一个节点的位置
     * @param tmp 当前节点
     * @return 更新后的当前节点的位置
     */
    private static double SetSon(TreeNode a, double last, TreeNode tmp) {
        // 计算当前节点的位置
        double pos = last + tmp.getBlockLen() / 2;
        tmp.setLayoutY(pos); // 设置当前节点的Y轴位置

        // 根据节点类型设置节点的X轴位置和连线的起点终点坐标
        if (tmp.getType() == 1) { // 右边节点
            // 设置节点的X轴位置为父节点的X轴位置加上父节点文本长度再加上固定值
            tmp.setLayoutX(a.getLayoutX() + a.getTextLen() - Draw.RecW + length_dis * tmp.getType());
            // 绘制连线，起点为父节点的右侧，终点为当前节点的左侧
            tmp.getLine().SetLine(a.getLayoutX() + a.getTextLen(), a.getLayoutY() + Draw.RecH / 2, tmp.getLayoutX(), tmp.getLayoutY() + Draw.RecH / 2);
        } else { // 左边节点
            // 设置节点的X轴位置为父节点的X轴位置减去固定值再减去当前节点文本长度
            tmp.setLayoutX(a.getLayoutX() + (length_dis - Draw.RecW) * tmp.getType() - tmp.getTextLen());
            // 绘制连线，起点为父节点的左侧，终点为当前节点的右侧
            tmp.getLine().SetLine(a.getLayoutX(), a.getLayoutY() + Draw.RecH / 2, tmp.getLayoutX() + tmp.getTextLen(), tmp.getLayoutY() + Draw.RecH / 2);
        }

        // 更新当前节点的位置，考虑节点之间的距离
        last = pos + tmp.getBlockLen() / 2 + Block_dis ;
        return last;
    }

    /**
     * 删除节点及其子节点在图上的显示
     * @param a 待删除的节点
     * @param A 包含节点的AnchorPane
     */
    public static void DelNode(TreeNode a, AnchorPane A) {//只是删除图里的还没删除list里的
        // 如果待删除节点是根节点，则直接返回，不执行删除操作
        if (a.isRoot()) return;

        // 遍历待删除节点的子节点，递归删除子节点及其子节点的子节点...
        for (TreeNode tmp : a.getchildren()) {
            DelNode(tmp, A);
        }

        // 从AnchorPane中移除待删除节点及其连线
        A.getChildren().remove(a); // 移除节点
        A.getChildren().remove(a.getLine()); // 移除节点的连线
    }

    public static void update(TreeNode root, AnchorPane A1) {
        Draw.update_len(root);
        Pane.update_pane(A1, root);
        Draw.update_pos(root, 1);
        Draw.update_pos(root, -1);
    }

    /**
     * 重新加载节点及其子节点到AnchorPane中，并更新显示
     * @param root 树的根节点
     * @param a 待重新加载的节点
     * @param A1 包含节点的AnchorPane
     */
    public static void reload(TreeNode root, TreeNode a, AnchorPane A1) {
        a.initNode(root, A1); // 初始化待重新加载的节点
        a.getView().setId(++number);
        a.getparent().getView().getChildren().add(a.getView()); // 将节点视图添加到其父节点的视图中
        A1.getChildren().add(a); // 将节点添加到AnchorPane中
        A1.getChildren().add(a.getLine()); // 将节点的连线添加到AnchorPane中

        // 递归重新加载子节点
        for (TreeNode tmp : a.getchildren()) {
            reload(root, tmp, A1);
        }
    }


    public static void setHint(Label hint, String txt) {
        hint.setText(txt);
        //设置字体颜色
        hint.setTextFill(Color.RED);
    }

    public static void GetDp() {
        int sum = 0;
        int idx = 0;
        //获取节点的所有孩子个数
        ArrayList<TreeNode> tmplist = new ArrayList<>();
        for (TreeNode tmp : TreeNode.getLchildren()) {
            DFS(tmp);
            sum += tmp.getSonSize();
            tmplist.add(tmp);
        }
        for (TreeNode tmp : TreeNode.getRchildren()) {
            DFS(tmp);
            sum += tmp.getSonSize();
            tmplist.add(tmp);
        }
        //动态规划 类01背包问题+逆向求解背包
        //dp[i][j]=1/0 表示前i个节点构成的集合 能1否0 选出一个子集和为j
        //select[i][j]=1/0 构成上诉选出的子集中 能1不能0 出现第i个数值
        int[][] dp = new int[tmplist.size() + 1][sum + 1];
        int[][] select = new int[tmplist.size() + 1][sum + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= tmplist.size(); i++) {
            for (int j = 0; j <= sum; j++) {
                if (j - tmplist.get(i - 1).getSonSize() >= 0 && dp[i - 1][j - tmplist.get(i - 1).getSonSize()] == 1) {
                    //dp[i-1][j-value]需要选tmplist[i]的情况
                    dp[i][j] = 1;
                    select[i][j] = 1;
                }
                if (dp[i - 1][j] == 1) {
                    //dp[i-1][j] 不能选的情况，这里不需要设置pre[i][j]
                    dp[i][j] = 1;
                }
            }
        }
        //遍历得到二分集的差的最小值
        int p = 0;
        int minn = 99999999;
        boolean[] st = new boolean[tmplist.size() + 1];
        for (int i = 0; i <= sum; i++) {
            if (dp[tmplist.size()][i] == 1 && abs(sum - 2 * i) < minn) {
                minn = abs(sum - 2 * i);
                p = i;
            }
        }
        //逆向解背包，根据select数组判断要不要选当前的数值添加到集合
        for (int i = tmplist.size(); i >= 1; i--) {
            if (select[i][p] != 0) {
                st[i] = true;
                p -= tmplist.get(i - 1).getSonSize();
            }
        }
        //将两个集合分散到根节点左右两边表示
        TreeNode.getLchildren().clear();
        TreeNode.getRchildren().clear();
        for (int i = 1; i <= tmplist.size(); i++) {
            if (st[i] == true) {
                TreeNode.getLchildren().add(tmplist.get(i - 1));
            } else {
                TreeNode.getRchildren().add(tmplist.get(i - 1));
            }
        }
        tmplist.clear();
    }

    /**
     * 计算节点的子节点个数
     * @param u 当前节点
     */
    public static void DFS(TreeNode u) {
        int res = 0; // 初始化子节点个数为0
        // 遍历当前节点的所有子节点
        for (TreeNode tmp : u.getchildren()) {
            // 递归计算当前子节点的子节点个数
            DFS(tmp);
            // 更新当前节点的子节点个数，加上当前子节点的子节点个数
            res += tmp.getSonSize();
        }
        // 设置当前节点的子节点个数为计算结果加1（加1表示包括当前节点自身）
        u.setSonSize(res + 1);
    }
}