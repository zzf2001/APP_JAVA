package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//登录界面 加上存储
public class login extends AppCompatActivity {
    private EditText getId;
    private EditText getPassWord;
    private String token;
    private Button loginBtn;

    private String loginId;
    private String loginPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getId=findViewById(R.id.getid);
        getPassWord=findViewById(R.id.getPassWord);
        loginBtn=findViewById(R.id.login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                loginId=getId.getText().toString();
                loginPassword=getPassWord.getText().toString();
                int T=0;
                if(loginId.equals(""))
                {
                    T++;
                    Toast.makeText(login.this,"请输入账号",1).show();
                }
                if(loginPassword.equals("")&&T==0)
                {
                    T++;
                    Toast.makeText(login.this,"请输入账号",1).show();
                }

                //开线程 进行登录验证
                if (T==0)
                {
                    new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //联网
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"username\": \""+loginId+"\",\r\n    \"password\": \""+loginPassword+"\"\r\n}");
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/reglog/all-log")
                                .method("POST", body)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .addHeader("Content-Type", "application/json")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            if (jsonObject.getString("code").equals("1000")) {
                                JSONObject jsonObject1 = new JSONObject(jsonObject.get("data").toString());
                                token = jsonObject1.getString("token");
                                System.out.println(token);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(login.this,MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        //登录成功以后存储token
                                        SharedPreferences userSettings = getSharedPreferences("logining", login.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = userSettings.edit();
                                        editor.putString("token",token);
                                        editor.commit();
                                        startActivity(intent);

                                    }
                                });
                            }
                            else
                            {
                                runOnUiThread(new Runnable() {
                                    @SuppressLint("WrongConstant")
                                    @Override
                                    public void run() {
                                        Toast.makeText(login.this, "账号或密码错误", 1).show();
                                        getPassWord.setText("");
                                    }
                                });
                            }

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

                }

            }
        });



    }
}