package com.example.just.Avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.example.just.R;

/**
 * Created by Administrator on 2018/5/19.
 */

public class BeginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view =View.inflate(this, R.layout.activity_begin,null);     //将启动界面的xml文件转换为view

        setContentView(view);

        AlphaAnimation aa=new AlphaAnimation(0f, 1f);           //设置动画效果
        aa.setDuration(2500);
        view.startAnimation(aa);

        aa.setAnimationListener(new Animation.AnimationListener() {


            @Override
            public void onAnimationEnd(Animation animation) {
// TODO 自动生成的方法存根
                Intent intent= new Intent(BeginActivity.this,MainActivity.class);         //设置监听,
                startActivity(intent);
                finish();
            }


            @Override
            public void onAnimationStart(Animation animation) {
// TODO 自动生成的方法存根

            }


            @Override
            public void onAnimationRepeat(Animation animation) {
// TODO 自动生成的方法存根

            }
        });

    }
}

