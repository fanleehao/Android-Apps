package com.example.myapp_2.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapp_2.view.GameButton;
import com.example.myapp_2.R;
import com.example.myapp_2.media.SoundPlayer;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.myapp_2.R.drawable.e;

/**
 * Created by 11141 on 2017/7/3.
 */

public class GameActivity extends Activity {

    private static GameActivity gameActivity = null;
    public GameActivity(){
        gameActivity=this;
    }
    public static GameActivity getGameActivity(){
        return gameActivity;
    }
    private int type;
    public void setType(int type){
        this.type = type;
    }
    private GameButton easyButton, normalButton, hardButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ImageView imageView = (ImageView)findViewById(R.id.iv_game);
        if (MenuActivity.getMenuActivity().getPos() != -1){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    MenuActivity.getMenuActivity().pictures[MenuActivity.getMenuActivity().getPos()]);
            imageView.setImageBitmap(bitmap);
        }else{
            Bitmap bitmap = MainActivity.getMainActivity().getBitmap();
            imageView.setImageBitmap(bitmap);
        }
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        easyButton = (GameButton)findViewById(R.id.bt_easy);
        easyButton.setRC(3,3);
        easyButton.setType(1);
        normalButton = (GameButton)findViewById(R.id.bt_normal);
        normalButton.setRC(4,4);
        normalButton.setType(2);
        hardButton = (GameButton)findViewById(R.id.bt_hard);
        hardButton.setRC(5,5);
        hardButton.setType(3);
        type = 0;
        textView = (TextView)findViewById(R.id.tv_time);
        progressBar = (ProgressBar)findViewById(R.id.pb_time);
        progressBar.setProgress(100);
        Intent data = new Intent();
        setResult(RESULT_OK,data);
        Button helpButton = (Button)findViewById(R.id.bt_help);
        if (MenuActivity.getMenuActivity().getPos() >= 10){
            helpButton.setVisibility(View.INVISIBLE);
        }
    }

    public void setButtonColor(){
        if (type == 1){
            easyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
            normalButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
            hardButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
        }else if (type == 2){
            easyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
            normalButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
            hardButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
        }else if (type == 3){
            easyButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
            normalButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
            hardButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonl));
        }else{

        }
    }

    public void helpOnClick(View v){
        SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
        hideButton();
        if (type == 1){
            easyButton.remove();
        }else if (type == 2){
            normalButton.remove();
        }else if (type == 3){
            hardButton.remove();
        }else{

        }
    }

    public void showButton(){
        easyButton.setVisibility(View.VISIBLE);
        normalButton.setVisibility(View.VISIBLE);
        hardButton.setVisibility(View.VISIBLE);
    }
    public void hideButton(){
        easyButton.setVisibility(View.INVISIBLE);
        normalButton.setVisibility(View.INVISIBLE);
        hardButton.setVisibility(View.INVISIBLE);
    }
    private TextView textView;
    private ProgressBar progressBar;
    private int remainedTime = 0;
    public int getRemainedTime(){
        return remainedTime;
    }
    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (remainedTime>=0){
                        remainedTime--;
                        textView.setText(""+remainedTime);
                    }
                }
            });
        }
    };

    public void setTime(){
        remainedTime = 99;
        if (!timerTask.cancel()){
            timerTask.cancel();
            timer.cancel();
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (remainedTime == 4){
                            SoundPlayer.failed(MenuActivity.getMenuActivity().getSoundVolume());
                        }
                        if (remainedTime>=0){
                            textView.setText(""+remainedTime);
                            progressBar.setProgress(remainedTime);
                            remainedTime--;
                        }else {
                            final Dialog dialog = new Dialog(GameActivity.getGameActivity(),R.style.dialog);
                            LayoutInflater inflater = LayoutInflater.from(GameActivity.getGameActivity());
                            View dialogView = inflater.inflate(R.layout.dialog_game,null);
                            dialog.setContentView(dialogView);
                            TextView textView = (TextView)dialogView.findViewById(R.id.tv_dialog_game_message);
                            textView.setText(R.string.gameOver);
                            Button button1 = (Button)dialogView.findViewById(R.id.bt_dialog_game_restart);
                            button1.setText(R.string.gameRestart);
                            button1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                                    if (type == 1){
                                        easyButton.gameStart();
                                    }else if (type == 2){
                                        normalButton.gameStart();
                                    }else if (type == 3){
                                        hardButton.gameStart();
                                    }else{

                                    }
                                    dialog.dismiss();
                                    WindowManager.LayoutParams lp = GameActivity.getGameActivity().getWindow().getAttributes();
                                    lp.alpha = 1f;
                                    GameActivity.getGameActivity().getWindow().setAttributes(lp);
                                }
                            });
                            Button button2 = (Button)dialogView.findViewById(R.id.bt_dialog_game_remark);
                            button2.setText(R.string.gameExit);
                            button2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                                    dialog.dismiss();
                                    WindowManager.LayoutParams lp = GameActivity.getGameActivity().getWindow().getAttributes();
                                    lp.alpha = 1f;
                                    GameActivity.getGameActivity().getWindow().setAttributes(lp);
                                    GameActivity.getGameActivity().onBackPressed();
                                }
                            });
                            dialog.setCanceledOnTouchOutside(false);
                            WindowManager.LayoutParams lp = GameActivity.getGameActivity().getWindow().getAttributes();
                            lp.alpha = 0.4f;
                            GameActivity.getGameActivity().getWindow().setAttributes(lp);
                            dialog.show();
                            closeTime();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask,0,1000);

    }
    public void closeTime(){
        if (!timerTask.cancel()){
            timerTask.cancel();
            timer.cancel();
        }
        if (type != 3){
            progressBar.setProgress(100);
            textView.setText(R.string.defaultTime);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!timerTask.cancel()){
            timerTask.cancel();
            timer.cancel();
        }
        finish();
    }

}


