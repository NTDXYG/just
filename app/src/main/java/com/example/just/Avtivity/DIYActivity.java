package com.example.just.Avtivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.just.Adapter.DIYAdapter;
import com.example.just.Adapter.LoveAdapter;
import com.example.just.Bean.Story;
import com.example.just.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/29.
 */
public class DIYActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    public List<Story> loveList = new ArrayList<>();
    private Handler handler;
    private DIYAdapter adapter;

    private void getList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    loveList.clear();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://120.79.65.201:80/Android/DIY")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responeData = response.body().string();
                    Gson gson = new Gson();
                    loveList = gson.fromJson(responeData, new TypeToken<List<Story>>(){}.getType());
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isNight) {
            setTheme(R.style.AppThemeNight);
        }
        setTitle("美食DIY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.diy_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.diy_rview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        getList();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    adapter = new DIYAdapter(loveList);
                    recyclerView.setAdapter(adapter);
                }
            }
        };
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.diy_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshStory();
            }
        });
    }

    private void refreshStory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getList();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(DIYActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;

        }
        return true;
    }

}
