package com.example.just.Avtivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.just.Adapter.CateAdapter;
import com.example.just.Bean.Cate;
import com.example.just.Bean.Person;
import com.example.just.Adapter.StoryAdapter;
import com.example.just.Bean.Story;
import com.example.just.DB.MyDatabaseHelper;
import com.example.just.R;
import com.example.just.Receiver.NetworkChangeReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.just.Adapter.StoryAdapter.count;


public class MainActivity extends BaseActivity implements OnBannerListener {
    public long mExitTime = System.currentTimeMillis();
    public DrawerLayout mDrawerLayout;
    public RecyclerView recyclerView;
    public static Handler handler;
    public static String mail = "";
    public static String name = "";
    public static int img = R.drawable.nav_icon;
    public TextView user_mail;
    public TextView user_name;
    public static CircleImageView user_image;
    public static List<Story> storyList = new ArrayList<>();
    public StoryAdapter adapter;
    public SwipeRefreshLayout swipeRefresh;
    public MyDatabaseHelper dbHelper;
    public Banner banner;
    public ArrayList<String> list_path;
    public ArrayList<String> list_title;
    public List<Cate> cateList = new ArrayList<>();
    public static String id = "0";
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private NavigationView navigationView;

    //getList_id切换主题
    public static void getList(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    storyList.clear();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("id", id)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.79.65.201:80/Android/Hot")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responeData = response.body().string();
                    Gson gson = new Gson();
                    storyList = gson.fromJson(responeData, new TypeToken<List<Story>>() {
                    }.getType());
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //轮播图
    public void initView() {
        banner = (Banner) findViewById(R.id.banner);
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add("http://www.dzwww.com/2018/shzzqdfh/shqd/xw_159406/201806/W020180611355709365993.jpg");
        list_path.add("http://img1.gtimg.com/rcdimg/20180611/11/5348433295_990x477.jpg");
        list_path.add("http://img1.gtimg.com/rcdimg/20180611/14/6071795317_990x477.jpg");
        list_path.add("http://img1.gtimg.com/rcdimg/20180610/17/2710065103_990x477.jpg");
        list_title.add("习近平主持上合组织青岛峰会并发表重要讲话");
        list_title.add("白血病粉丝过生日，收到胡歌送来的花：愿你一生温暖纯良");
        list_title.add("杨幂终于找到适合自己的古装发型了");
        list_title.add("残缺的全家福：G7峰会官方公布大合影 特朗普不在其中");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
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
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Toasty.info(MainActivity.this, "你点了第" + position + "张轮播图", Toast.LENGTH_SHORT, true).show();
    }

    public class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //配置布局文件
        if (isNight) {
            setTheme(R.style.AppThemeNight);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
        initView();
        dbHelper = new MyDatabaseHelper(this, "Love.db", null, 1);
        dbHelper.getWritableDatabase();

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("首页"));
        mTabLayout.addTab(mTabLayout.newTab().setText("综艺"));
        mTabLayout.addTab(mTabLayout.newTab().setText("体育"));
        mTabLayout.addTab(mTabLayout.newTab().setText("明星"));
        mTabLayout.addTab(mTabLayout.newTab().setText("科技"));
        mTabLayout.addTab(mTabLayout.newTab().setText("军事"));
        mTabLayout.addTab(mTabLayout.newTab().setText("旅游"));
        mTabLayout.addTab(mTabLayout.newTab().setText("文化"));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                id = String.valueOf(tab.getPosition());
                getList(id);
                count = 0;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.main_rview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        getList(id);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    adapter = new StoryAdapter(storyList);
                    recyclerView.setAdapter(adapter);
                    if (count == 1) {
                        recyclerView.scrollToPosition(10);
                    }
                }
            }
        };


        //配置ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //配置滑动布局DrawerLayout并设置menu的点击事件
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.category);
        }

        //配置滑动菜单页的点击事件
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemBackgroundResource(R.color.background);
        /*navigationView.setCheckedItem(R.id.nav_diy);*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (!isLogin) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("请先登录！");
                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                } else {
                    //各种操作
                    switch (item.getItemId()) {
                        case R.id.nav_diy:
                            Intent intent = new Intent(MainActivity.this, DIYActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_location:
                            Intent intent1 = new Intent(MainActivity.this, BaiduActivity.class);
                            startActivity(intent1);
                            break;
                        case R.id.nav_changetheme:
                            if (isNight) {
                                isNight = false;
                                recreate();
                            } else {
                                isNight = true;
                                recreate();
                            }
                            break;
                        case R.id.nav_message:
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("嘿呀");
                            dialog.setMessage("做我女朋友吧");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("好呀", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                    dialog.setTitle("Nice");
                                    dialog.setMessage("爱你，mua~");
                                    dialog.setCancelable(true);
                                    dialog.show();
                                }
                            });
                            dialog.setNegativeButton("不行", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                    dialog.setTitle("嘿呀");
                                    dialog.setMessage("房产证写你名字呢");
                                    dialog.setCancelable(false);
                                    dialog.setPositiveButton("嗯...", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                            dialog.setTitle("嘿呀");
                                            dialog.setMessage("我妈会游泳呢");
                                            dialog.setCancelable(false);
                                            dialog.setPositiveButton("嗯...", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                                    dialog.setTitle("嘿呀");
                                                    dialog.setMessage("保大保大！");
                                                    dialog.setCancelable(false);
                                                    dialog.setPositiveButton("同意了~", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                                            dialog.setTitle("Nice");
                                                            dialog.setMessage("爱你，mua~");
                                                            dialog.setCancelable(true);
                                                            dialog.show();
                                                        }
                                                    });
                                                    dialog.show();
                                                }
                                            });
                                            dialog.show();
                                        }
                                    });
                                    dialog.show();
                                }
                            });
                            dialog.show();
                            break;
                        case R.id.nav_love:
                            Intent intent2 = new Intent(MainActivity.this, LoveActivity.class);
                            startActivity(intent2);
                            break;
                        case R.id.nav_exit:
                            isLogin = false;
                            name = "";
                            mail = "";
                            user_mail.setText(mail);
                            user_name.setText(name);
                            user_image.setImageResource(R.drawable.nav_icon);
                            Toasty.success(MainActivity.this, "退出成功", Toast.LENGTH_SHORT, true).show();
                            break;
                        case R.id.contact:
                            //短信、电话、QQ
                            showDialog(recyclerView);
                            break;
                        case R.id.banben:
                            Toasty.info(MainActivity.this, "当前已是最新版本", Toast.LENGTH_SHORT, true).show();
                            break;
                        default:
                    }
                }
                return true;
            }
        });

        //配置nav_header中的textview和图片
        View headerView = navigationView.getHeaderView(0);
        user_name = (TextView) findViewById(R.id.user_name);
        user_mail = (TextView) findViewById(R.id.user_mail);
        user_image = (CircleImageView) findViewById(R.id.user_image);
        user_name = headerView.findViewById(R.id.user_name);
        user_mail = headerView.findViewById(R.id.user_mail);
        user_image = headerView.findViewById(R.id.user_image);
        user_name.setText(name);
        user_mail.setText(mail);
        user_image.setImageResource(img);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogin) {
                    //启动登录activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    //启动查看个人信息
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        });


        //悬浮按钮事件返回顶部
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(0);
            }
        });

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                count = 0;
                refreshStory();
            }
        });


    }

    public void refreshStory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getList(id);
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                        Toasty.success(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    //设置back键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mDrawerLayout.closeDrawers();
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //设置toolbar中的快捷菜单和点击事件
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tianqi:
                Intent intent1 = new Intent(this,WeatherActivity.class);
                startActivity(intent1);
                break;
            case R.id.settings:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                //启动设置activity
                break;
            case android.R.id.home:
                //点击展开滑动菜单
                mDrawerLayout.openDrawer(GravityCompat.START);
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Person person = (Person) data.getSerializableExtra("person");
                    mail = person.getEmail();
                    name = person.getName();
                    img = R.drawable.user;
                    user_mail.setText(mail);
                    user_name.setText(name);
                    user_image.setImageResource(img);

                }
        }
    }

    public void showDialog(View view) {
        final BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.dialog, null);
        Button QQ_contanct = (Button) dialogView.findViewById(R.id.QQ_contanct);
        Button call_contanct = (Button) dialogView.findViewById(R.id.call_contanct);
        Button message_contanct = (Button) dialogView.findViewById(R.id.message_contanct);
        Button tvCancel = (Button) dialogView.findViewById(R.id.tv_cancel);

        QQ_contanct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=744621980";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.warning(MainActivity.this, "请检查是否安装QQ", Toast.LENGTH_SHORT, true).show();
                }
                dialog.dismiss();
            }
        });
        call_contanct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:17805054371"));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        message_contanct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:17805054371");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "Hello~");
                startActivity(it);
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(dialogView);
        dialog.show();
    }

}


