package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class welcome extends AppCompatActivity {
        private Button left;
        private Button right;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_welcome);
            left=findViewById(R.id.left);
            right=findViewById(R.id.right);
            //状态栏改色
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //跳转动画 h后期做
            //再开个线程去加载一下内容；
            startMainActivity();
        }

        private void startMainActivity(){


            TimerTask delayTask = new TimerTask() {
                @Override
                public void run() {
                    //这里用一下token 获取一下信息
                    SharedPreferences sharedPreferences = getSharedPreferences("logining", login.MODE_PRIVATE);
                    final String token = sharedPreferences.getString("token", "no");
                    //开个线程取读取一下内容

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient().newBuilder()
                                .build();
                        Request request = new Request.Builder()
                                .url("http://39.106.195.109/itnews/api/self/info")
                                .method("GET", null)
                                .addHeader("Authorization", token)
                                .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            if(jsonObject.getString("code").equals("1000"))
                            {
                                Intent intent = new Intent(welcome.this,MainActivity.class);
                                startActivity(intent);
                                welcome.this.finish();
                            }
                            else
                            {
                                //没get到东西直接跳转到这里
                                //这里弹出登录和注册两个按钮 跳转
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        left.setVisibility(View.VISIBLE);
                                        right.setVisibility(View.VISIBLE);
                                        left.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(welcome.this,login.class);
                                                startActivity(intent);
                                            }
                                        });
                                        right.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(welcome.this,registered.class);
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                });


                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();




                }
            };
            Timer timer = new Timer();
            timer.schedule(delayTask,2000);//延时两秒执行 run 里面的操作
        }

    }
