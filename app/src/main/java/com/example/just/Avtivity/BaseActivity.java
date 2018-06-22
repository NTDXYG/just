package com.example.just.Avtivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Administrator on 2018/5/21.
 */

public class BaseActivity extends AppCompatActivity {
    public static boolean isLogin = false;
    public static boolean isNight = false;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d("Activity", getClass().getSimpleName());
    }
}
