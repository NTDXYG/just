package com.example.just.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * Created by Administrator on 2018/6/9.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            switch (networkInfo.getType()) {
                case TYPE_MOBILE:
                    Toasty.success(context, "正在使用2G/3G/4G网络", Toast.LENGTH_SHORT,true).show();
                    break;
                case TYPE_WIFI:
                    Toasty.success(context, "正在使用wifi上网", Toast.LENGTH_SHORT,true).show();
                    break;
                default:
                    break;
            }
        } else {
            Toasty.error(context, "当前网络不可用", Toast.LENGTH_LONG,true).show();
        }
    }
}
