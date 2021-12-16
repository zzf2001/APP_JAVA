package com.example.zhihu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class web extends AppCompatActivity {
    private String url;
    private String workId;
    private String title;

    private LinearLayout setreturnBack;
    private LinearLayout setcomments;
    private LinearLayout good;
    private boolean goodTrue = false;

    private TextView setgood;
    private TextView setsumcomments;
    private boolean collectionTrue = false;

    private LinearLayout collection;
    private ImageView collectionset;
    private ImageView goodset;

    extra listen;
    private String position;
    private String imgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        workId = intent.getStringExtra("id");
        title=intent.getStringExtra("title");
        position=intent.getStringExtra("position");
        imgurl=intent.getStringExtra("img");
        WebView myWebView = findViewById(R.id.webview);

        //判断一下之前收藏没
        SharedPreferences userSettings = getSharedPreferences("collect"+position, web.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        collectionset=findViewById(R.id.collectionset);
        SharedPreferences sharedPreferences= getSharedPreferences("collect"+position, web.MODE_PRIVATE);
        String True =sharedPreferences.getString("name", "no");
        if (True.equals("no"))

        Glide.with(web.this).load(R.drawable.shoucang).into(collectionset);
        else
        {
            collectionTrue=true;
            Glide.with(web.this).load(R.drawable.inshoucang).into(collectionset);
        }
        listen = new extra();
        myWebView.setWebViewClient(new WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        System.out.println(url);
        myWebView.loadUrl(url);
        myWebView.getSettings().setJavaScriptEnabled(true);

        setgood = findViewById(R.id.goodsum);
        setsumcomments = findViewById(R.id.commentssum);


        //加载一下json；
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String temporary;

                try {
                    URL url = new URL(" https://news-at.zhihu.com/api/3/story-extra/" + workId);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(15000);


                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));//存一下读取的东西
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {


                        try {
                            JSONObject jsonObject = new JSONObject(line);

                            listen.Comments = jsonObject.getString("comments");
                            listen.lonhComments = jsonObject.getString("long_comments");
                            listen.shortComments = jsonObject.getString("short_comments");
                            listen.goodSum = jsonObject.getString("popularity");
                        } catch (JSONException e) {
                            System.out.println("没解析上");
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setgood.setText(listen.goodSum);
                                setsumcomments.setText(listen.Comments);
                            }
                        });


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        good=findViewById(R.id.good);
        goodset=findViewById(R.id.setgood);
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodTrue)
                {
                    setgood.setText(listen.goodSum);
                    Glide.with(web.this).load(R.drawable.good).into(goodset);
                    goodTrue=false;
                }
                else
                {
                    setgood.setText(String.valueOf(Integer.parseInt(listen.goodSum)+1));
                    Glide.with(web.this).load(R.drawable.ingood).into(goodset);
                    goodTrue=true;
                }
            }
        });


        //sp方法的准备

        //收藏
        collection=findViewById(R.id.collection);






        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectionTrue)
                {

                    Glide.with(web.this).load(R.drawable.shoucang).into(collectionset);
                    collectionTrue=false;

                    editor.clear();
                    editor.commit();
                }
                else
                {

                    Glide.with(web.this).load(R.drawable.inshoucang).into(collectionset);
                    collectionTrue=true;


                    editor.putString("name",title);
                    editor.putString("URL",imgurl);
                    editor.commit();
                }
            }
        });





        //返回事件
        setreturnBack = findViewById(R.id.returnBack);
        setreturnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //评论
        setcomments = findViewById(R.id.comments);
        setcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入下一个界面
                Intent intent = new Intent(web.this, show.class);
                //把show的几个东西传进去 id 长短+总数
                intent.putExtra("id", workId);
                intent.putExtra("comment", listen.Comments);
                intent.putExtra("longcomment", listen.lonhComments);
                intent.putExtra("shortcomment", listen.shortComments);


                startActivity(intent);


            }
        });


    }
}