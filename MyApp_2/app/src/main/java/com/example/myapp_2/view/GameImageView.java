package com.example.myapp_2.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 11141 on 2017/7/4.
 */

public class GameImageView extends ImageView {

    private int id;
    private int i;
    private int j;
    public GameImageView(Context context) {
        super(context);
    }

    public GameImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void setIJ(int i,int j){
        this.i = i;
        this.j = j;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getI(){
        return i;
    }
    public int getJ(){
        return j;
    }
    public int getId(){
        return id;
    }
}
