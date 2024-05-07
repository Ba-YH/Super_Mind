package com.cs1.super_mind;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public abstract class Draw {
    public static final double RecH = 24;//固定矩阵高
    public static final double RecW = 54;//固定矩阵长
    public static final double Block_dis = 24;////固定块距
    public static TreeNode CurNode;

    public static void update_len(TreeNode a) {//用于更新整棵树的所有节点的块上下大小，传入一个根节点即可
        double len = 0;
        if (a.isRoot()) {
            TreeNode.LMaxLinkLen=0;
            for (TreeNode tmp : TreeNode.getLchildren()) {
                tmp.setType(-1);
                update_len(tmp);
                TreeNode.LMaxLinkLen= Math.max(TreeNode.LMaxLinkLen,tmp.getMaxLinkLen()+(100-Draw.RecW)+tmp.getTextLen());
                len += tmp.getBlockLen();
            }
            len += (TreeNode.getLchildren().size() - 1) * Block_dis;
            TreeNode.LBlockLen = (max(len, RecH));
            len = 0;
            TreeNode.RMaxLinkLen=0;
            for (TreeNode tmp : TreeNode.getRchildren()) {
                tmp.setType(1);
                update_len(tmp);
                TreeNode.RMaxLinkLen= Math.max(TreeNode.RMaxLinkLen,tmp.getMaxLinkLen()+(100-Draw.RecW)+tmp.getTextLen());
                len += tmp.getBlockLen();
            }
            len += (TreeNode.getRchildren().size() - 1) * Block_dis;
            TreeNode.RBlockLen = (max(len, RecH));
        } else {
            a.setMaxLinkLen(0);
            for (TreeNode tmp : a.getchildren()) {
                tmp.setType(a.getType());
                update_len(tmp);
                a.setMaxLinkLen(max(a.getMaxLinkLen(),tmp.getMaxLinkLen()+(100-Draw.RecW)+tmp.getTextLen()));
                len += tmp.getBlockLen();
            }
            len += (a.getchildren().size() - 1) * Block_dis;
            a.setBlockLen(max(len, RecH));
        }
    }

    public static void update_pos(TreeNode a, int op) {//更新树在图上的位置，需要先执行update_len
        if (!a.isRoot() && a.getchildren().isEmpty()) return;
        if (op == 1) a.setBlockLen(TreeNode.RBlockLen);
        if (op == -1) a.setBlockLen(TreeNode.LBlockLen);
        double last = a.getLayoutY() + Draw.RecH / 2 - a.getBlockLen() / 2;
        if (op != 0) {
            if (op == -1) {
                for (TreeNode tmp : TreeNode.getLchildren()) {
                    last = SetSon(a, last, tmp);
                }
                for (TreeNode tmp : TreeNode.getLchildren()) {
                    update_pos(tmp, 0);
                }
            } else {
                for (TreeNode tmp : TreeNode.getRchildren()) {
                    last = SetSon(a, last, tmp);
                }
                for (TreeNode tmp : TreeNode.getRchildren()) {
                    update_pos(tmp, 0);
                }
            }
        } else {
            for (TreeNode tmp : a.getchildren()) {
                last = SetSon(a, last, tmp);
            }
            for (TreeNode tmp : a.getchildren()) {
                update_pos(tmp, 0);
            }
        }
    }

    private static double SetSon(TreeNode a, double last, TreeNode tmp) {
        double pos = last + tmp.getBlockLen() / 2 - Draw.RecH / 2;
        tmp.setLayoutY(pos);
        if(tmp.getType()==1) {//右边节点画线
            tmp.setLayoutX(a.getLayoutX()+a.getTextLen()-Draw.RecW + 100 * tmp.getType());
            tmp.getLine().SetLine(a.getLayoutX() + a.getTextLen(), a.getLayoutY() + Draw.RecH / 2, tmp.getLayoutX(), tmp.getLayoutY() + Draw.RecH / 2);
        }
        else{//左边节点画线
            tmp.setLayoutX(a.getLayoutX() + (100-Draw.RecW) * tmp.getType()-tmp.getTextLen());
            tmp.getLine().SetLine(a.getLayoutX(), a.getLayoutY() + Draw.RecH / 2, tmp.getLayoutX()+tmp.getTextLen(), tmp.getLayoutY() + Draw.RecH / 2);
        }
        last = pos + tmp.getBlockLen() / 2 + Block_dis + Draw.RecH / 2;
        return last;
    }

    public static void DelNode(TreeNode a, AnchorPane A) {//只是删除了图上的还没删除list里的
        if (a.isRoot()) return;
        for (TreeNode tmp : a.getchildren()) {
            DelNode(tmp, A);
        }
        A.getChildren().remove(a);
        A.getChildren().remove(a.getLine());
    }
    public static void update(TreeNode root,AnchorPane A1){
        Draw.update_len(root);
        Pane.update_pane(A1,root);
        Draw.update_pos(root, 1);
        Draw.update_pos(root,-1);
    }
    public static void reload(TreeNode root,TreeNode a,AnchorPane A1){
        a.initNode(root,A1);
        a.getparent().getView().getChildren().add(a.getView());
        A1.getChildren().add(a);
        A1.getChildren().add(a.getLine());
        for(TreeNode tmp : a.getchildren()){
            reload(root,tmp,A1);
        }
    }
    public static void setHint(Label hint,String txt){
        hint.setText(txt);
    }
    public static void GetDp(){
        int sum=0;int idx=0;
        ArrayList<TreeNode>tmplist =new ArrayList<>();
        for(TreeNode tmp:TreeNode.getLchildren()){
            DFS(tmp);sum+=tmp.getSonSize();
            tmplist.add(tmp);
        }
        for(TreeNode tmp:TreeNode.getRchildren()){
            DFS(tmp);sum+=tmp.getSonSize();
            tmplist.add(tmp);
        }
        int [][]dp=new int[tmplist.size()+1][sum+1];
        int [][]pre=new int[tmplist.size()+1][sum+1];
        dp[0][0]=1;
        for(int i=1;i<=tmplist.size();i++){
            for(int j=0;j<=sum;j++){
                if(j-tmplist.get(i-1).getSonSize()>=0&&dp[i-1][j-tmplist.get(i-1).getSonSize()]==1){
                    dp[i][j]=1;
                    pre[i][j]=i;
                }
                else if(dp[i-1][j]==1){
                    dp[i][j]=1;
                    pre[i][j]=0;
                }
            }
        }
        int p=0;int minn=99999999;
        boolean []st=new boolean[tmplist.size()+1];
        for(int i=0;i<=sum;i++){
            if(dp[tmplist.size()][i]==1&&abs(sum-2*i)<minn){
                minn=abs(sum-2*i);
                p=i;
            }
        }
        for(int i=tmplist.size();i>=1;i--){
            if(pre[i][p]!=0){
                st[pre[i][p]]=true;
                p-=tmplist.get(pre[i][p]-1).getSonSize();
            }
        }
        TreeNode.getLchildren().clear();TreeNode.getRchildren().clear();
        for(int i=1;i<=tmplist.size();i++){
            if(st[i]==true){
                TreeNode.getLchildren().add(tmplist.get(i-1));
            }
            else{
                TreeNode.getRchildren().add(tmplist.get(i-1));
            }
        }
        tmplist.clear();
    }
    public static void DFS(TreeNode u){
        int res=0;
        for(TreeNode tmp:u.getchildren()){
            DFS(tmp);
            res+=tmp.getSonSize();
        }
        u.setSonSize(res+1);
    }
}