package com.example.just.Avtivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.just.Adapter.ChatAdapter;
import com.example.just.Bean.Ask;
import com.example.just.Bean.Chat;
import com.example.just.Bean.Take;
import com.example.just.Gson.JsonParser;
import com.example.just.Http.API;
import com.example.just.R;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static com.example.just.Avtivity.BaseActivity.isNight;
import static org.litepal.LitePalApplication.getContext;

public class TulingActivity extends AppCompatActivity {
    private static final String TAG = TulingActivity.class.getSimpleName();

    //  聊天消息列表
    private RecyclerView recyclerView;

    //  输入框
    private EditText editText;
    //  发送按钮
    private Button btn_send;

    private ImageView yuyin;

    private HashMap<String,String> mIatResults = new LinkedHashMap<>();

    //    对话信息集合
    private List<Chat> list = new ArrayList<>();

    //    适配器
    private ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isNight) {
            setTheme(R.style.AppThemeNight);
        }
        setTitle("图灵机器人");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //      初始化数据
        initView();
        initSpeech();
        //加载数据
        initData();
        //设置布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(this, list);
        recyclerView.setAdapter(chatAdapter);

    }

    private void initSpeech() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=5b3822b3");
    }

    private void initData() {
        Chat c1 = new Chat("你好，我叫布丁", Chat.TYPE_RECEIVED);
        list.add(c1);
        Chat c2 = new Chat("你好，你现在会些什么呢？", Chat.TYPE_SENT);
        list.add(c2);
        Chat c3 = new Chat("我还在成长中，很多东西还不懂，但是你可以考考我", Chat.TYPE_RECEIVED);
        list.add(c3);
    }

    private void initView() {
        recyclerView = findViewById(R.id.tuling_recycler);
        editText = findViewById(R.id.tuling_et_text);
        btn_send = findViewById(R.id.tuling_btn_send);
        yuyin = findViewById(R.id.yuyinshuru);
        yuyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechDialog();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 1，获取输入框的内容
                 * 2，判断是否为空
                 * 3，发送后清空当前的输入框
                 */
//              1,获取输入框的内容
                String text = editText.getText().toString();
//              2,判断是否为空
                if (!TextUtils.isEmpty(text)) {
                    //把要发送的数据添加到addData方法中，并把数据类型也填入，这里我们的类型是TYPE_SENT，发送数据类型
                    addData(text, Chat.TYPE_SENT);
//              3，清空输入框
                    editText.setText("");
                    request(text);
                }

            }
        });
    }

    private void startSpeechDialog() {
        RecognizerDialog mDialog = new RecognizerDialog(this,new MyInitListener());
        mDialog.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mDialog.setParameter(SpeechConstant. ACCENT, "mandarin" );
        mDialog.setListener( new MyRecognizerDialogListener()) ;
        mDialog.show() ;
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener{

        @Override
        public void onResult(RecognizerResult results, boolean b) {
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

            mIatResults.put(sn, text) ;//没有得到一句，添加到

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults .get(key));
            }

            editText.setText(resultBuffer.toString());// 设置输入框的文本
            editText.setSelection(editText.length()) ;//把光标定位末尾
        }

        @Override
        public void onError(SpeechError speechError) {

        }
    }

    class MyInitListener implements InitListener{

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS){
                Toast.makeText(TulingActivity.this,"初始化失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startSpeech(){
        //1. 创建SpeechRecognizer对象，第二个参数： 本地识别时传 InitListener
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer( this, null); //语音识别器
        //2. 设置听写参数，详见《 MSC Reference Manual》 SpeechConstant类
        mIat.setParameter(SpeechConstant. DOMAIN, "iat" );// 短信和日常用语： iat (默认)
        mIat.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mIat.setParameter(SpeechConstant. ACCENT, "mandarin" );// 设置普通话
        //3. 开始听写
        mIat.startListening( mRecoListener);
    }

    private RecognizerListener mRecoListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {

        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };



    public static class MySynthesizerListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }


    private void addData(String text, int type) {
        Chat c = new Chat(text, type);
        list.add(c);
        //当有新消息时，刷新显示
        chatAdapter.notifyItemInserted(list.size() - 1);
        //定位的最后一行
        recyclerView.scrollToPosition(list.size() - 1);
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

    /**
     * 请求数据
     *
     * @param text 输入框的发送数据
     */
    private void request(String text) {
//      把输入的文本数据存储在请求实体类中
        Ask ask = new Ask();
        Ask.UserInfoBean info = new Ask.UserInfoBean();
        info.setApiKey("79f03711ab3d4e9cb92380a43d2a7d0e");//将机器人的key值填入
        info.setUserId("285381");//将用户id填入
        ask.setUserInfo(info);
        Ask.PerceptionBean.InputTextBean pre = new Ask.PerceptionBean.InputTextBean(text);//将要发送给机器人书文本天趣
        ask.setPerception(new Ask.PerceptionBean(pre));

//       创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi.tuling123.com/")//设置网络请求url，后面一段写在网络请求接口里面
                .addConverterFactory(GsonConverterFactory.create())//Gson解析
                .build();
//       创建网络请求接口的实例
        API api = retrofit.create(API.class);
//      Take为响应实体类，用来接受机器人返回的回复数据
        Call<Take> call = api.request(ask);
//
        call.enqueue(new Callback<Take>() {
            //          请求成功
            @Override
            public void onResponse(Call<Take> call, Response<Take> response) {
//              接受到的机器人回复的数据
                String mText= response.body().getResults().get(0).getValues().getText();
//              把接受到的数据传入addData方法中，类型是TYPE_RECEIVED接受数据
                addData(mText, Chat.TYPE_RECEIVED);
                Log.d("test","接受到的机器人回复的数据： "+mText);
            }
            //            请求失败
            @Override
            public void onFailure(Call<Take> call, Throwable t) {
                Log.d("test","请求失败： "+t.toString());
            }
        });
    }

}
