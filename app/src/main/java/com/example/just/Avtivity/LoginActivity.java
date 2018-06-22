package com.example.just.Avtivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.just.Bean.Person;
import com.example.just.Http.HttpUtil;
import com.example.just.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/22.
 */
public class LoginActivity extends BaseActivity{

    private EditText username;
    private EditText password;
    private Button login_btn;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        login_btn = (Button) findViewById(R.id.login_button);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass = (CheckBox) findViewById(R.id.login_rememberpassword);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember){
            String account = pref.getString("account", "");
            String password1= pref.getString("password", "");
            username.setText(account);
            password.setText(password1);
            rememberPass.setChecked(true);
        }

        //登录
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( username.getText().toString().equals("")||password.getText().toString().equals("")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    dialog.setMessage("必填项不能为空！");
                    dialog.setCancelable(true);
                    dialog.show();
                }else{
                    HttpUtil.loginRequest("http://120.79.65.201:80/Android/Test",
                            username.getText().toString(),
                            password.getText().toString(),
                            new okhttp3.Callback(){
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Gson gson = new Gson();
                                    String responseData = response.body().string();
                                    List<Person> list = gson.fromJson(responseData, new TypeToken<List<Person>>()
                                    {}.getType());
                                    for (Person person : list){
                                        if (!person.isExit()){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(LoginActivity.this);
                                                    dialog1.setMessage("用户名密码错误！");
                                                    dialog1.setCancelable(true);
                                                    dialog1.show();
                                                }
                                            });
                                        }else {
                                            isLogin = true;
                                            editor = pref.edit();
                                            if (rememberPass.isChecked()){
                                                editor.putBoolean("remember_password", true);
                                                editor.putString("account",username.getText().toString());
                                                editor.putString("password",password.getText().toString());
                                            }else {
                                                editor.clear();
                                            }
                                            editor.apply();
                                            Intent intent = new Intent();
                                            intent.putExtra("person",person);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    }
                                }
                            });

                }
            }
        });
    }
}
