package com.example.just.Avtivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.just.Adapter.TaobaoAdapter;
import com.example.just.Bean.Taobao;
import com.example.just.DB.MyDatabaseHelper;
import com.example.just.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.just.Avtivity.BaseActivity.isNight;

public class TaobaoDetailActivity extends AppCompatActivity implements OnBannerListener {

    private MyDatabaseHelper dbHelper;
    public FloatingActionButton fab;
    public boolean isOnclick = false;
    public static String url = "";
    public static String title = "";
    public static String priceWap = "";
    public static String priceWithRate = "";
    public static String nick = "";
    public static String sold = "";
    public static String numiid = "";
    private TextView taobao_title;
    private TextView taobao_priceWap;
    private TextView taobao_priceWithRate;
    private TextView taobao_sold;
    private TextView taobao_nick;
    private Button btn;
    public Handler handler;
    public Banner banner;
    public ArrayList<String> list_path;
    public ArrayList<String> list_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isNight) {
            setTheme(R.style.AppThemeNight);
        }
        super.onCreate(savedInstanceState);
        dbHelper = new MyDatabaseHelper(this, "Love.db", null, 1);
        final Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        priceWap = intent.getStringExtra("priceWap");
        priceWithRate = intent.getStringExtra("priceWithRate");
        nick = intent.getStringExtra("nick");
        sold = intent.getStringExtra("sold");
        numiid = intent.getStringExtra("numiid");
        setContentView(R.layout.activity_taobao_detail);
        initView();
        setTitle(intent.getStringExtra("title"));
        btn = findViewById(R.id.goumai);
        taobao_title = findViewById(R.id.taobao_title);
        taobao_priceWap = findViewById(R.id.taobao_priceWap);
        taobao_priceWithRate = findViewById(R.id.taobao_priceWithRate);
        taobao_sold = findViewById(R.id.taobao_sold);
        taobao_nick = findViewById(R.id.taobao_nick);
        taobao_title.setText(intent.getStringExtra("title"));
        taobao_priceWap.setText(intent.getStringExtra("priceWap"));
        taobao_priceWap.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        taobao_priceWithRate.setText(intent.getStringExtra("priceWithRate"));
        taobao_sold.setText(intent.getStringExtra("sold"));
        taobao_nick.setText(intent.getStringExtra("nick"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("Android.intent.action.VIEW");
                Uri uri = Uri.parse(getIntent().getStringExtra("url")); // 商品地址
                intent.setData(uri);
                intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
                startActivity(intent);
            }
        });
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
                        dbHelper.addData(intent.getStringExtra("title"), intent.getStringExtra("image"), intent.getStringExtra("url"),intent.getStringExtra("priceWap"),intent.getStringExtra("priceWithRate"),intent.getStringExtra("nick"),intent.getStringExtra("sold"),intent.getStringExtra("numiid"));
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
                        dbHelper.addData(intent.getStringExtra("title"), intent.getStringExtra("image"), intent.getStringExtra("url"),intent.getStringExtra("priceWap"),intent.getStringExtra("priceWithRate"),intent.getStringExtra("nick"),intent.getStringExtra("sold"),intent.getStringExtra("numiid"));
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

    //轮播图
    public void initView() {
        banner = (Banner) findViewById(R.id.taobao_banner);
        //放图片地址的集合
        list_path = new ArrayList<>();
        getList();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    list_title = new ArrayList<>();
                    for (int i = 1;i<list_path.size()+1;i++){
                        list_title.add(String.valueOf(i));
                    }
                    //设置内置样式，共有六种可以点入方法内逐一体验使用。
                    banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
                    //设置图片加载器，图片加载器在下方
                    banner.setImageLoader(new TaobaoDetailActivity.MyLoader());
                    //设置图片网址或地址的集合
                    banner.setImages(list_path);
                    //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
                    banner.setBannerAnimation(Transformer.Default);
                    //设置轮播图的标题集合
                    banner.setBannerTitles(list_title);
                    //设置轮播间隔时间
                    banner.setDelayTime(3000);
                    //设置是否为自动轮播，默认是“是”。
                    banner.isAutoPlay(true);
                    //设置指示器的位置，小点点，左中右。
                    banner.setIndicatorGravity(BannerConfig.CENTER)
                            //必须最后调用的方法，启动轮播图。
                            .start();
                }
            }
        };


    }

    private void getList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.43.99:8080/Android/Numiid?numiid="+getIntent().getStringExtra("numiid"))
                            .build();
                    Response response = client.newCall(request).execute();
                    String responeData = response.body().string();
                    Gson gson = new Gson();
                    list_path = gson.fromJson(responeData, new TypeToken<List<String>>() {
                    }.getType());
                    Log.d("getList",String.valueOf(responeData));
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Toasty.info(TaobaoDetailActivity.this, "你点了第" + position + "张轮播图", Toast.LENGTH_SHORT, true).show();
    }

    public class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
