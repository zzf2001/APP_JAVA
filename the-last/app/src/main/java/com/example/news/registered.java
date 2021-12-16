package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class registered extends AppCompatActivity {
    //获得注册的账号 密码 邮箱 确认全都输入
    private String RegisteredID;
    private String RegisteredPassWord;
    private String surePassWord;
    private String RegisteredEmail;
    private String Registeredcode;

    //
    private EditText RegisteredIDET;
    private EditText RegisteredPassWordET;
    private EditText surePassWordET;
    private EditText RegisteredEmailET;
    private EditText RegisteredcodeET;
    //注册按钮 和 获得验证码
     private Button RegisteredGo;
     private TextView requireCode;

     //点了之后出来提示发送成功并且60s不能在点
    private int time=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        RegisteredIDET=findViewById(R.id.getRegisteredid);
        RegisteredPassWordET=findViewById(R.id.getRegisteredPassWord);
        surePassWordET=findViewById(R.id.surePassWord);
        RegisteredEmailET=findViewById(R.id.getQQ);
        RegisteredcodeET=findViewById(R.id.getRegisteredcode);


        RegisteredGo=findViewById(R.id.registered);
        requireCode=findViewById(R.id.requirecode);

        //获得验证码
        requireCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(time<1) {
                    RegisteredEmail = RegisteredEmailET.getText().toString();
                    if (RegisteredEmail.equals("")) {
                        //请输入邮箱
                    } else {
                        time=60;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpClient client = new OkHttpClient().newBuilder()
                                        .build();
                                MediaType mediaType = MediaType.parse("application/json");
                                RequestBody body = RequestBody.create(mediaType, "{\n    \"email\": \"" + RegisteredEmail + "\",\n \"usage\": 1\n}");
                                Request request = new Request.Builder()
                                        .url("http://39.106.195.109/itnews/api/reglog/code-reg")
                                        .method("POST", body)
                                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                        .addHeader("Content-Type", "application/json")
                                        .build();
                                System.out.println("成功发送");

                                try {
                                    Response response = client.newCall(request).execute();
                                    JSONObject  jsonObject=new JSONObject(response.body().string());
                                    if (!jsonObject.getString("code").toString().equals("1000"))
                                    {
                                        runOnUiThread(new Runnable() {
                                            @SuppressLint("WrongConstant")
                                            @Override
                                            public void run() {
                                                RegisteredEmailET.setText("");
                                                Toast.makeText(registered.this,"邮箱输入有误",1).show();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        final Timer MT=new Timer();
                        MT.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                time--;
                                if(time<1)
                                {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            requireCode.setText("重新发送");
                                        }
                                    });
                                    MT.cancel();

                                }
                                else
                                    {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            requireCode.setText("重新发送"+time);
                                        }
                                    });
                                }


                            }
                        },0,100);

                    }

                }
                else {


                    //此时应该不让别人点
                    Toast.makeText(registered.this,"请稍后再试",Toast.LENGTH_SHORT);
                    System.out.println(time);
                }
            }
        });

        //获得所有内容
        RegisteredGo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                int T=0;
                RegisteredID=RegisteredIDET.getText().toString();
                RegisteredPassWord=RegisteredPassWordET.getText().toString();
                surePassWord=surePassWordET.getText().toString();
                Registeredcode=RegisteredcodeET.getText().toString();

                //开始验证
                //确定每个框都有输入
                if(RegisteredID.equals(""))
                {
                    T++;
                    Toast.makeText(registered.this,"请输入账号",1).show();
                }
                //先确定两次输入密码一样
                    if(RegisteredPassWord.equals("")&&T==0)
                {
                    T++;
                    Toast.makeText(registered.this,"请输入密码",1).show();
                }
                if(surePassWord.equals("")&&T==0)
                {
                    T++;
                    Toast.makeText(registered.this,"请确认密码",1).show();
                }
                if(Registeredcode.equals("")&&T==0)
                {
                    T++;
                    Toast.makeText(registered.this,"请输入验证码",1).show();
                }

                if(!RegisteredPassWord.equals(surePassWord)&& T==0)
                {
                    T=1;
                    RegisteredPassWordET.setText("");
                    Toast.makeText(registered.this,"两次密码不一样",1).show();
                }
                if (T==0)
                {
                    new Thread(new TimerTask() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"username\": \""+RegisteredID+"\",\r\n    \"password\": \""+RegisteredPassWord+",\r\n    \"email\": \""+RegisteredEmail+"\",\r\n    \"verify\": \""+Registeredcode+"\"\r\n}");
                            Request request = new Request.Builder()
                                    .url("http://39.106.195.109/itnews/api/reglog/all-reg")
                                    .method("POST", body)
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .addHeader("Content-Type", "application/json")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                JSONObject jsonObject=new JSONObject(response.body().toString());
                                if (jsonObject.getString("code").toString().equals("1000"))
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();

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

            }
        });


    }


}