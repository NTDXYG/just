package com.example.just.Avtivity;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.just.Adapter.LoveAdapter;
import com.example.just.Adapter.StoryAdapter;
import com.example.just.Bean.Story;
import com.example.just.DB.MyDatabaseHelper;
import com.example.just.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoveActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private LoveAdapter adapter;
    public Handler handler;
    public List<Story> loveList = new ArrayList<>();
    public MyDatabaseHelper dbHelper  = new MyDatabaseHelper(this, "Love.db", null, 1);;

    private void getList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    loveList.clear();
                    loveList = dbHelper.queryData();
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
        if (isNight){
            setTheme(R.style.AppThemeNight);
        }
        setTitle("我的收藏");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);

        Toolbar toolbar = (Toolbar) findViewById(R.id.love_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.love_rview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getList();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    adapter = new LoveAdapter(loveList);
                    recyclerView.setAdapter(adapter);
                }
            }
        };

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.love_refresh);
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
                        Toasty.success(LoveActivity.this, "刷新成功", Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.love_toolbar, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getList();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoveActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("确定要全部删除所有收藏吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper.deleteAll();
                        refreshStory();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;

        }
        return true;
    }
}
