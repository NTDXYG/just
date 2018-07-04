package com.example.just.Avtivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.just.DB.MyDatabaseHelper;
import com.example.just.R;

import static com.example.just.Avtivity.BaseActivity.isNight;

public class DetailActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    public FloatingActionButton fab;
    public boolean isOnclick = false;
    public static String url = "";
    public static String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isNight) {
            setTheme(R.style.AppThemeNight);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dbHelper = new MyDatabaseHelper(this, "Love.db", null, 1);
        final Intent intent = getIntent();
        setTitle(intent.getStringExtra("title"));
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        //判断是否收藏
        if (dbHelper.isExist(intent.getStringExtra("title"))) {
            fab.setImageResource(R.drawable.islove);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isOnclick) {
                        fab.setImageResource(R.drawable.love);
                        dbHelper.deleteByName(intent.getStringExtra("title"));
                        Snackbar.make(view, "取消成功！", Snackbar.LENGTH_LONG)
                                .setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isOnclick = true;
                                    }
                                }).show();
                    } else {
                        fab.setImageResource(R.drawable.islove);
                        dbHelper.addData(intent.getStringExtra("title"), intent.getStringExtra("image"), intent.getStringExtra("url"),"","","","","");
                        Snackbar.make(view, "收藏成功！", Snackbar.LENGTH_LONG)
                                .setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isOnclick = false;
                                    }
                                }).show();
                    }

                }
            });
        } else {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isOnclick){
                        fab.setImageResource(R.drawable.islove);
                        //收藏
                        dbHelper.addData(intent.getStringExtra("title"), intent.getStringExtra("image"), intent.getStringExtra("url"),"","","","","");
                        Snackbar.make(view, "收藏成功！", Snackbar.LENGTH_LONG)
                                .setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isOnclick = true;
                                    }
                                }).show();
                    }else{
                        fab.setImageResource(R.drawable.love);
                        //收藏
                        dbHelper.deleteByName(intent.getStringExtra("title"));
                        Snackbar.make(view, "取消成功！", Snackbar.LENGTH_LONG)
                                .setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isOnclick = false;
                                    }
                                }).show();
                    }

                }
            });
        }

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl(intent.getStringExtra("url"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fenxiang, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fenxiang:
                Intent intent1=new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra(Intent.EXTRA_TEXT,"["+title+"]"+'\n'+url+'\n'+"(来自Just的分享)");
                startActivity(Intent.createChooser(intent1,"分享到..."));
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
