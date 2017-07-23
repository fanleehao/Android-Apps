package com.example.fanleehao.game2048;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
/**
 * 数字格子对应的数字存储结构
 * Created by fanleehao on 2017/6/30.
 */
public class NumList {
    //记录坐标
    private List<Integer> stuList = new ArrayList<Integer>();
    //记录数字
    private List<Integer> numList = new ArrayList<Integer>();
    //添加
    public void add(int index, int number){
        stuList.add(index);
        numList.add(number);
    }
    public boolean contains(int index){
        return stuList.contains(index);
    }
    //移除
    public void remove(int index){
        int order = stuList.indexOf(index);
        numList.remove(order);
        stuList.remove(order);
    }
    //数字乘2，相当于两个格子合并
    public void levelup(int index){
        int order = stuList.indexOf(index);
        numList.set(order, numList.get(order)+1);
    }
    //更新格子
    public void changeIndex(int index, int newIndex){
        stuList.set(stuList.indexOf(index), newIndex);
    }

    public int getNumberByIndex(int index){
        int order = stuList.indexOf(index);
        return numList.get(order) ;
    }

    public int getNumberByXY(int x,int y){
        if(x<0 || x>3 || y<0 || y>3)
            return -1;
        else {
            int order = stuList.indexOf(4*x+y);
            return numList.get(order) ;
        }
    }
    //清空
    public void clear(){
        numList.clear();
        stuList.clear();
    }

    public boolean hasChance(){
        for(int x = 0;x<=3;x++){
            for(int y=0;y<=3;y++){
                if(y<3){
                    if(getNumberByXY(x,y)==getNumberByXY(x, y+1))
                        return true;
                }
                if(x<3){
                    if(getNumberByXY(x,y)==getNumberByXY(x+1, y))
                        return true;
                }
            }
        }
        return false;
    }
}
