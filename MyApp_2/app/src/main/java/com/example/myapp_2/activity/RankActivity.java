package com.example.myapp_2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp_2.R;
import com.example.myapp_2.database.Rec;
import com.example.myapp_2.media.SoundPlayer;

import java.util.ArrayList;

/**
 * Created by 11141 on 2017/7/12.
 */

public class RankActivity extends Activity{

    private TextView[][] textViews;
    private int pos = 0;
    private int maxlevel = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initGridLayout();
        initSpinner();
        MenuActivity.getMenuActivity().setPos(0);
    }

    public void initSpinner(){
        Spinner spinner = (Spinner)findViewById(R.id.s_rank_select);
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 1; i <= 14; i++){
            arrayList.add(i);
        }
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(
                this,android.R.layout.simple_spinner_item,arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showRank(""+(position+1));
                MenuActivity.getMenuActivity().setPos(position);
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initGridLayout(){
        textViews = new TextView[11][4];
        ArrayList<Rec> arrayList = MenuActivity.getMenuActivity().getMyDB().selectn(1+"");
        GridLayout gridLayout = (GridLayout)findViewById(R.id.gl_rank);
        gridLayout.setRowCount(11);
        gridLayout.setColumnCount(4);
        gridLayout.setBackgroundColor(gridLayout.getResources().getColor(R.color.colorGameBackGround));
        WindowManager wm = this.getWindowManager();
        int w = wm.getDefaultDisplay().getWidth();
        int h = wm.getDefaultDisplay().getHeight();
        int blockWidth = (w-100)/4;
        int blockHeight = (h - 20 * 25)/ 12;
        int blockSpace = 100/(5);
        for (int i=0;i<11;i++){
            for (int j=0;j<4;j++){
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.width = blockWidth;
                layoutParams.height = blockHeight;
                if (i != 10){
                    layoutParams.setMargins(blockSpace,blockSpace,0,0);
                }else {
                    layoutParams.setMargins(blockSpace,blockSpace,0,blockSpace);
                }

                TextView textView = new TextView(this);
                textView.setTextSize(20);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(this.getResources().getColor(R.color.colorBlockBackGround));
                textView.setTextColor(this.getResources().getColor(R.color.colorBlack));

                textViews[i][j] = textView;

                Button button = new Button(this);
                button.setTextSize(15);
                button.setGravity(Gravity.CENTER);
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
                button.setTextColor(this.getResources().getColor(R.color.colorWhite));
                button.setText("挑战");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                        if (MenuActivity.getMenuActivity().getUser().getUsername().equals("")){
                            MenuActivity.getMenuActivity().sign(RankActivity.this);
                        } else {
                            String username = MenuActivity.getMenuActivity().getUser().getUsername();
                            maxlevel = MenuActivity.getMenuActivity().getMyDB().selectP(username).getLevel();
                            if (pos > maxlevel){
                                Toast.makeText(RankActivity.this,"请先通关上一关卡",Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(RankActivity.this,GameActivity.class);
                                startActivityForResult(intent,0);
                            }
                        }
                    }
                });
                if (i==0){
                    if (j == 0){
                        textView.setText("名次");
                    } else if (j == 1) {
                        textView.setText("昵称");
                    } else if (j == 2) {
                        textView.setText("用时");
                    } else if (j == 3) {
                        textView.setText("挑战");
                    }
                    gridLayout.addView(textView,layoutParams);
                }else {
                    if (j == 0){
                        textView.setText(""+(i));
                        gridLayout.addView(textView,layoutParams);
                    } else if (j == 3){
                        gridLayout.addView(button,layoutParams);
                    }else if (j == 1){
                        if (i - 1 < arrayList.size()){
                            textView.setText(arrayList.get(i-1).getName());
                        }
                        gridLayout.addView(textView,layoutParams);
                    }else {
                        if (i - 1 < arrayList.size()){
                            textView.setText(arrayList.get(i-1).getTime());
                        }
                        gridLayout.addView(textView,layoutParams);
                    }
                }

            }
        }
    }
    public void showRank(String ll){
        ArrayList<Rec> arrayList = MenuActivity.getMenuActivity().getMyDB().selectn(ll);
        for (int i=0;i<11;i++){
            for (int j=0;j<4;j++){
                TextView textView = textViews[i][j];
                if ( i != 0) {
                    if (j == 1){
                        if (i - 1 < arrayList.size()){
                            textView.setText(arrayList.get(i-1).getName());
                        } else {
                            textView.setText("");
                        }
                    }else if (j == 2) {
                        if (i - 1 < arrayList.size()){
                            textView.setText(arrayList.get(i-1).getTime());
                        } else {
                            textView.setText("");
                        }
                    }
                }
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            showRank((pos+1)+"");
        }
    }
}
