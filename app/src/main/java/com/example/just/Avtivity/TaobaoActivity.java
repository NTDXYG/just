package com.example.just.Avtivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.just.Adapter.TaobaoAdapter;
import com.example.just.Bean.Taobao;
import com.example.just.Gson.JsonParser;
import com.example.just.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.just.Avtivity.BaseActivity.isNight;

public class TaobaoActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private TaobaoAdapter adapter;
    public Handler handler;
    public List<Taobao> taobaoList = new ArrayList<>();
    private ImageView yuyin;
    private HashMap<String,String> mIatResults = new LinkedHashMap<>();
    private void getList(final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    taobaoList.clear();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name", name)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.79.65.201:80" +
                                    "-/Android/Taobao")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responeData = response.body().string();
                    Gson gson = new Gson();
                    taobaoList = gson.fromJson(responeData, new TypeToken<List<Taobao>>() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isNight) {
            setTheme(R.style.AppThemeNight);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taobao);
        editText = findViewById(R.id.edit_taobao);
        button = findViewById(R.id.btn_search);
        recyclerView = (RecyclerView) findViewById(R.id.taobao_rview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.taobao_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshStory();
            }
        });
        yuyin = findViewById(R.id.yuyinshuru);
        yuyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechDialog();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        initSpeech();

    }

    private void search(){
        if (!TextUtils.isEmpty(editText.getText().toString())) {

            getList(editText.getText().toString());
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        adapter = new TaobaoAdapter(taobaoList);
                        recyclerView.setAdapter(adapter);
                    }
                }
            };

        }
    }

    private void startSpeechDialog() {
        //1. 创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener()) ;
        //2. 设置accent、 language等参数
        mDialog.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mDialog.setParameter(SpeechConstant. ACCENT, "mandarin" );
        // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解
        // 结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener( new MyRecognizerDialogListener()) ;
        //4. 显示dialog，接收语音输入
        mDialog.show() ;
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param results
         * @param isLast  是否说完了
         */
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String result = results.getResultString(); //为解析的

            String text = JsonParser.parseIatResult(result) ;//解析过后的

            String sn = null;
            // 读取json结果中的 sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString()) ;
                sn = resultJson.optString("sn" );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults .put(sn, text) ;//没有得到一句，添加到

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults .get(key));
            }
            String result_text = resultBuffer.toString().substring(0,resultBuffer.toString().length()-1);
            editText.setText(result_text);// 设置输入框的文本
            editText.setSelection(editText.length()) ;//把光标定位末尾
            search();
        }

        @Override
        public void onError(SpeechError speechError) {

        }
    }
    class MyInitListener implements InitListener {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Log.d("init","初始化失败 ");
            }

        }
    }

    // 听写监听器
    private RecognizerListener mRecoListener = new RecognizerListener() {
        // 听写结果回调接口 (返回Json 格式结果，用户可参见附录 13.1)；
//一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
//关于解析Json的代码可参见 Demo中JsonParser 类；
//isLast等于true 时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {

        }

        // 会话发生错误回调接口
        public void onError(SpeechError error) {

        }

        // 开始录音
        public void onBeginOfSpeech() {

        }

        //volume 音量值0~30， data音频数据
        public void onVolumeChanged(int volume, byte[] data) {

        }

        // 结束录音
        public void onEndOfSpeech() {

        }

        // 扩展用接口
        public void onEvent(int eventType, int arg1 , int arg2, Bundle obj) {
        }
    };

    /**
     * 语音识别
     */
    private void startSpeech() {
        //1. 创建SpeechRecognizer对象，第二个参数： 本地识别时传 InitListener
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer( this, null); //语音识别器
        //2. 设置听写参数，详见《 MSC Reference Manual》 SpeechConstant类
        mIat.setParameter(SpeechConstant. DOMAIN, "iat" );// 短信和日常用语： iat (默认)
        mIat.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mIat.setParameter(SpeechConstant. ACCENT, "mandarin" );// 设置普通话
        //3. 开始听写
        mIat.startListening( mRecoListener);
    }

    private void initSpeech() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=5b3822b3");
    }

    private void refreshStory() {
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
                        if (!TextUtils.isEmpty(editText.getText().toString())) {
                            getList(editText.getText().toString());
                            adapter.notifyDataSetChanged();
                            swipeRefresh.setRefreshing(false);
                            Toast.makeText(TaobaoActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TaobaoActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).start();
    }
}
