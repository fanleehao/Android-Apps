package com.example.myapp_2.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp_2.database.MyDataB;
import com.example.myapp_2.R;
import com.example.myapp_2.media.SoundPlayer;
import com.example.myapp_2.database.User;

import static com.example.myapp_2.R.drawable.b;

/**
 * Created by 11141 on 2017/7/11.
 */

public class MenuActivity extends Activity {

    private static MenuActivity menuActivity = null;
    public MenuActivity(){
        menuActivity=this;
    }
    public static MenuActivity getMenuActivity(){
        return menuActivity;
    }
    private MyDataB myDB;
    private User user = new User("","",0,"");
    public int[] pictures = {
            R.drawable.a, b,R.drawable.c,R.drawable.d,
            R.drawable.e,R.drawable.f,R.drawable.g,
            R.drawable.h,R.drawable.i,R.drawable.j,
            R.drawable.gu1,R.drawable.gu2,
            R.drawable.gu3,R.drawable.gu4};
    private int guru = 0;
    public int getGuru(){
        return guru;
    }
    public void setGuru(int i){
        guru = i;
    }
    private int pos = 0;
    public void setPos(int pos){
        this.pos = pos;
    }
    public int getPos(){
        return pos;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SoundPlayer.init(this);
        SoundPlayer.startMusic();
        myDB = new MyDataB(this,"myDB.db",null,1);
        myDB.getWritableDatabase();
        for (int i = 1;i<=14;i++){
            myDB.insertd(i);
        }

    }
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
    public MyDataB getMyDB(){
        return myDB;
    }
    public void onClick(View view){
        SoundPlayer.move2(soundVolume);
        switch (view.getId()){
            case R.id.bt_start:
                start(this,MainActivity.class);
                break;
            case R.id.bt_rank:
                Intent intent = new Intent(MenuActivity.this,RankActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_set:
                set();
                break;
            case R.id.bt_exit:
                System.exit(0);
                break;
        }
    }

    public void start(final Context context,final Class<?> cls){
        if (user.getUsername().equals("")){
            final Dialog dialogStart = new Dialog(context,R.style.dialog);
            LayoutInflater inflaterStart = LayoutInflater.from(context);
            View dialogViewStart = inflaterStart.inflate(R.layout.dialog_sign,null);
            dialogStart.setContentView(dialogViewStart);
            dialogStart.setCanceledOnTouchOutside(false);
            dialogStart.show();
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.4f;
            getWindow().setAttributes(lp);
            TabHost tabHost = (TabHost)dialogViewStart.findViewById(R.id.th_dialog_sign);
            tabHost.setup();
            TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1")
                    .setIndicator("登录")
                    .setContent(R.id.ll_dialog_sign_in);
            tabHost.addTab(tab1);
            TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2")
                    .setIndicator("注册")
                    .setContent(R.id.ll_dialog_sign_up);
            tabHost.addTab(tab2);
            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

                View viewTabHost = tabHost.getTabWidget().getChildAt(i);
                viewTabHost.setBackgroundResource(R.drawable.selector);
                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                tv.setTextSize(30);
                tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格

            }
            final EditText editTextSignInName = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_in_name);
            final EditText editTextSignInPassword = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_in_password);
            final TextView textViewSignIn = (TextView)dialogViewStart.findViewById(R.id.tv_dialog_sign_in);
            Button buttonSignIn = (Button)dialogViewStart.findViewById(R.id.bt_dialog_sign_in);
            Button buttonSignIn2 = (Button)dialogViewStart.findViewById(R.id.bt_dialog_sign_in_exit);

            buttonSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPlayer.move2(soundVolume);
                    if (myDB.isSign(editTextSignInName.getText().toString(),
                            editTextSignInPassword.getText().toString())){
                        user = myDB.selectP(editTextSignInName.getText().toString());
                        dialogStart.dismiss();
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f;
                        getWindow().setAttributes(lp);
                        Intent intent = new Intent(context,cls);
                        startActivity(intent);
                    }else{
                        textViewSignIn.setText("账号或密码错误");
                    }
                }
            });

            buttonSignIn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPlayer.move2(soundVolume);
                    dialogStart.dismiss();
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                }
            });

            final EditText editTextSignUpUsername = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_up_username);
            final EditText editTextSignUpNickname = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_up_nickname);
            final EditText editTextSignUpPassword = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_up_password);
            final EditText editTextSignUpPassword2 = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_up_password2);
            final TextView textViewSignUp = (TextView)dialogViewStart.findViewById(R.id.tv_dialog_sign_up);
            Button buttonSignUp = (Button)dialogViewStart.findViewById(R.id.bt_dialog_sign_up);
            Button buttonSignUp2 = (Button)dialogViewStart.findViewById(R.id.bt_dialog_sign_up_exit);

            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPlayer.move2(soundVolume);
                    if (editTextSignUpUsername.getText().toString().equals("")
                            || editTextSignUpNickname.getText().toString().equals("")
                            || editTextSignUpPassword.getText().toString().equals("")
                            || editTextSignUpPassword2.getText().toString().equals("")){
                        textViewSignUp.setText("请完善注册信息");
                    }else if (myDB.isUsername(editTextSignUpUsername.getText().toString())){
                        textViewSignUp.setText("该账号已存在");
                    } else if (myDB.isNickname(editTextSignUpNickname.getText().toString())){
                        textViewSignUp.setText("昵称不能重复");
                    } else if (!(editTextSignUpPassword.getText().toString().equals(
                            editTextSignUpPassword2.getText().toString()))){
                        textViewSignUp.setText("两次密码不同");
                    }else {
                        myDB.insertd(editTextSignUpUsername.getText().toString(),
                                editTextSignUpNickname.getText().toString(),
                                editTextSignUpPassword.getText().toString());
                        textViewSignUp.setText("注册成功");
                    }
                }
            });

            buttonSignUp2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPlayer.move2(soundVolume);
                    dialogStart.dismiss();
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                }
            });
        }else{
            Intent intent = new Intent(context,cls);
            startActivity(intent);
        }
    }
    public void sign(final Context context){
        final Dialog dialogStart = new Dialog(context,R.style.dialog);
        LayoutInflater inflaterStart = LayoutInflater.from(context);
        View dialogViewStart = inflaterStart.inflate(R.layout.dialog_sign,null);
        dialogStart.setContentView(dialogViewStart);
        dialogStart.setCanceledOnTouchOutside(false);
        dialogStart.show();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        TabHost tabHost = (TabHost)dialogViewStart.findViewById(R.id.th_dialog_sign);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1")
                .setIndicator("登录")
                .setContent(R.id.ll_dialog_sign_in);
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2")
                .setIndicator("注册")
                .setContent(R.id.ll_dialog_sign_up);
        tabHost.addTab(tab2);
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View viewTabHost = tabHost.getTabWidget().getChildAt(i);
            viewTabHost.setBackgroundResource(R.drawable.selector);
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(30);
            tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
        }
        final EditText editTextSignInName = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_in_name);
        final EditText editTextSignInPassword = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_in_password);
        final TextView textViewSignIn = (TextView)dialogViewStart.findViewById(R.id.tv_dialog_sign_in);
        Button buttonSignIn = (Button)dialogViewStart.findViewById(R.id.bt_dialog_sign_in);
        Button buttonSignIn2 = (Button)dialogViewStart.findViewById(R.id.bt_dialog_sign_in_exit);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(soundVolume);
                if (myDB.isSign(editTextSignInName.getText().toString(),
                        editTextSignInPassword.getText().toString())){
                    user = myDB.selectP(editTextSignInName.getText().toString());
                    dialogStart.dismiss();
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                    Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
                }else{
                    textViewSignIn.setText("账号或密码错误");
                }
            }
        });
        buttonSignIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(soundVolume);
                dialogStart.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        final EditText editTextSignUpUsername = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_up_username);
        final EditText editTextSignUpNickname = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_up_nickname);
        final EditText editTextSignUpPassword = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_up_password);
        final EditText editTextSignUpPassword2 = (EditText)dialogViewStart.findViewById(R.id.et_dialog_sign_up_password2);
        final TextView textViewSignUp = (TextView)dialogViewStart.findViewById(R.id.tv_dialog_sign_up);
        Button buttonSignUp = (Button)dialogViewStart.findViewById(R.id.bt_dialog_sign_up);
        Button buttonSignUp2 = (Button)dialogViewStart.findViewById(R.id.bt_dialog_sign_up_exit);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(soundVolume);
                if (editTextSignUpUsername.getText().toString().equals("")
                        || editTextSignUpNickname.getText().toString().equals("")
                        || editTextSignUpPassword.getText().toString().equals("")
                        || editTextSignUpPassword2.getText().toString().equals("")){
                    textViewSignUp.setText("请完善注册信息");
                }else if (myDB.isUsername(editTextSignUpUsername.getText().toString())){
                    textViewSignUp.setText("该账号已存在");
                } else if (myDB.isNickname(editTextSignUpNickname.getText().toString())){
                    textViewSignUp.setText("昵称不能重复");
                } else if (!(editTextSignUpPassword.getText().toString().equals(
                        editTextSignUpPassword2.getText().toString()))){
                    textViewSignUp.setText("两次密码不同");
                }else {
                    myDB.insertd(editTextSignUpUsername.getText().toString(),
                            editTextSignUpNickname.getText().toString(),
                            editTextSignUpPassword.getText().toString());
                    textViewSignUp.setText("注册成功");
                }
            }
        });
        buttonSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(soundVolume);
                dialogStart.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }
    private int music = 100;
    private int sound = 100;
    private float soundVolume = (float)1.0;
    public float getSoundVolume(){
        return soundVolume;
    }
    public void set(){
        final Dialog dialogSet = new Dialog(this, R.style.dialog);
        LayoutInflater inflaterSet = LayoutInflater.from(this);
        View dialogViewSet = inflaterSet.inflate(R.layout.dialog_setting,null);
        dialogSet.setContentView(dialogViewSet);
        dialogSet.setCanceledOnTouchOutside(false);
        dialogSet.show();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        Button button = (Button)dialogViewSet.findViewById(R.id.bt_dialog_setting_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(soundVolume);
                dialogSet.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        final SeekBar seekBarMusic = (SeekBar)dialogViewSet.findViewById(R.id.sb_dialog_setting_music);
        seekBarMusic.setProgress(music);
        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SoundPlayer.setMediaPlayerVolume((float) (progress/100.0));
                music = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button button1 = (Button)dialogViewSet.findViewById(R.id.bt_dialog_setting_music);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(soundVolume);
                seekBarMusic.setProgress(0);
                SoundPlayer.setMediaPlayerVolume((float) (0.0));
                music = 0;
            }
        });
        final SeekBar seekBarSound = (SeekBar)dialogViewSet.findViewById(R.id.sb_dialog_setting_sound);
        seekBarSound.setProgress(sound);
        seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sound = progress;
                soundVolume = (float) (progress/100.0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button button2 = (Button)dialogViewSet.findViewById(R.id.bt_dialog_setting_sound);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(soundVolume);
                seekBarSound.setProgress(0);
                sound = 0;
                soundVolume = (float) 0.0;
            }
        });

        final TextView textView = (TextView)dialogViewSet.findViewById(R.id.tv_dialog_setting_user);
        textView.setText(user.getUsername());
        Button button3 = (Button)dialogViewSet.findViewById(R.id.bt_dialog_setting_user);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(soundVolume);
                sign(MenuActivity.this);
                textView.setText(user.getUsername());
                dialogSet.dismiss();
            }
        });
    }

}
