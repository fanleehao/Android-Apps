package com.example.myapp_2.database;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.myapp_2.activity.MenuActivity;

/**
 * Created by 11141 on 2017/7/17.
 */

public class MyAdapterGuru extends BaseAdapter {

    private Context context;
    private int[] pictures;
    private int level;
    public MyAdapterGuru(Context context){
        this.context = context;
        pictures = new int[4];
        for (int i=0;i<4;i++){
            pictures[i] = MenuActivity.getMenuActivity().pictures[i+10];
        }
        String username = MenuActivity.getMenuActivity().getUser().getUsername();
        level = MenuActivity.getMenuActivity().getMyDB().selectP(username).getLevel()-10;
    }

    @Override
    public int getCount() {
        return pictures.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setAdjustViewBounds(true);
        imageView.setImageResource(pictures[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (position > level){
            imageView.setAlpha(0.5f);
        }
        return imageView;
    }
}
