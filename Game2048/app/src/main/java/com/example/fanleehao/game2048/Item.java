package com.example.fanleehao.game2048;
/**
 * 用于存储每一个组块的数字和坐标
 * Created by fanleehao on 2017/6/30.
 */
public class Item {
    int index, number;
    int x, y;
    Item(int index,int number){
        this.index = index;
        this.number = number;
        this.x = index%4;
        this.y = index/4;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
}
