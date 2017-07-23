package com.example.myapp_2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.example.myapp_2.R;

/**
 * Created by W--Inarius on 2017/7/13.
 */

public class AnimationActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ani);
        ImageView imageView=(ImageView)findViewById(R.id.om);

        imageView.setImageResource(R.drawable.dfssf);
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(6000);
        alphaAnimation.setFillAfter(true);
        animationSet.addAnimation(alphaAnimation);

        imageView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        imageView.startAnimation(animationSet);

        new Handler().postDelayed(new Runnable(){
            public void run() {
                Intent i = new Intent(AnimationActivity.this,MenuActivity.class);
                //通过Intent打开最终真正的主界面Main这个Activity
                AnimationActivity.this.startActivity(i);
                AnimationActivity.this.finish();    //关闭自己这个开场屏
            }
        }, 5000);
    }
}
