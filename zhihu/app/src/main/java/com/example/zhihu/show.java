package com.example.zhihu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class show extends AppCompatActivity {
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    String id;
    String comment;
    String longcomment;
    String shortcomment;

    ImageView returnBack;
    TextView mytx;
    List<commentsShow> mNewsList = new ArrayList<>();
    commentsShow listen;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        mRecyclerView = findViewById(R.id.recyclerviewlong);

        returnBack = findViewById(R.id.returnBackweb);
        returnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mytx = findViewById(R.id.commentsTittle);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        comment=intent.getStringExtra("comment");
        longcomment=intent.getStringExtra("longcomment");
        shortcomment=intent.getStringExtra("shortcomment");

        mytx.setText(comment+"人评论");
        //用js换一下内容
        if (Integer.parseInt(longcomment)!=0) {
            listen = new commentsShow();
            listen.type = 1;
            listen.hint = longcomment + "条长评论";
            mNewsList.add(listen);
        }
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String temporary;





                    try {
                        URL url = new URL("https://news-at.zhihu.com/api/4/story/"+id+"/long-comments");

                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(15000);
                        connection.setReadTimeout(15000);

                        InputStream in = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(in));//存一下读取的东西
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
//                        response.append(line);

                            try {
                                JSONObject jsonObject = new JSONObject(line);
                                //把json下面的日期拿出来存进list

                                JSONArray jsonArray = new JSONArray(jsonObject.get("comments").toString());

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());


                                    listen = new commentsShow();
                                    listen.name = jsonObject1.getString("author");
                                    listen.commen = jsonObject1.getString("content");

                                    listen.like=jsonObject1.getString("likes");
                                    listen.url=jsonObject1.getString("avatar");
                                    String tem=jsonObject1.getString("time");
                                    //时间戳转换一下
                                    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

                                    Long time=new Long(Long.valueOf(tem));

                                    String d = format.format(time);

                                    listen.time=format.parse(d);

                                    listen.type=0;
                                    listen.id = jsonObject1.getString("id");
                                    mNewsList.add(listen);

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMyAdapter = new show.MyAdapter(show.this);
                                        mRecyclerView.setAdapter(mMyAdapter);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(show.this);
                                        mRecyclerView.setLayoutManager(layoutManager);
                                        //丑陋的写法
                                        if (Integer.parseInt(shortcomment)!=0)
                                        {
                                            listen = new commentsShow();
                                            listen.type=1;
                                            listen.hint=shortcomment+"条短评论";
                                            mNewsList.add(listen);

                                            new Thread(new Runnable() {
                                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                @Override
                                                public void run() {
                                                    HttpURLConnection connection = null;
                                                    BufferedReader reader = null;
                                                    String temporary;





                                                    try {
                                                        URL url = new URL("https://news-at.zhihu.com/api/4/story/"+id+"/short-comments");

                                                        connection = (HttpURLConnection) url.openConnection();
                                                        connection.setRequestMethod("GET");
                                                        connection.setConnectTimeout(15000);
                                                        connection.setReadTimeout(15000);

                                                        InputStream in = connection.getInputStream();
                                                        reader = new BufferedReader(new InputStreamReader(in));//存一下读取的东西
                                                        StringBuilder response = new StringBuilder();
                                                        String line;

                                                        while ((line = reader.readLine()) != null) {
//                        response.append(line);

                                                            try {
                                                                JSONObject jsonObject = new JSONObject(line);
                                                                //把json下面的日期拿出来存进list

                                                                JSONArray jsonArray = new JSONArray(jsonObject.get("comments").toString());

                                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());


                                                                    listen = new commentsShow();
                                                                    listen.name = jsonObject1.getString("author");
                                                                    listen.commen = jsonObject1.getString("content");

                                                                    listen.like=jsonObject1.getString("likes");
                                                                    listen.url=jsonObject1.getString("avatar");
                                                                    String tem=jsonObject1.getString("time");
                                                                    //时间戳转换一下
                                                                    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

                                                                    Long time=new Long(Long.valueOf(tem));

                                                                    String d = format.format(time);

                                                                    listen.time=format.parse(d);

                                                                    listen.type=0;
                                                                    listen.id = jsonObject1.getString("id");
                                                                    mNewsList.add(listen);

                                                                }

                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        mMyAdapter = new show.MyAdapter(show.this);
                                                                        mRecyclerView.setAdapter(mMyAdapter);
                                                                        LinearLayoutManager layoutManager = new LinearLayoutManager(show.this);
                                                                        mRecyclerView.setLayoutManager(layoutManager);

                                                                    }
                                                                });


                                                            } catch (JSONException e) {
                                                                System.out.println("存入不成功成功");
                                                                e.printStackTrace();
                                                            }


                                                        }



                                                    } catch (Exception e) {
                                                        //断网提示
                                                        e.printStackTrace();
                                                        System.out.println("这里有大问题");
                                                    } finally {
                                                        if (reader != null) {
                                                            try {
                                                                reader.close();
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                    }

                                                }

                                            }).start();
                                        }
                                    }
                                });


                            } catch (JSONException e) {
                                System.out.println("存入不成功成功");
                                e.printStackTrace();
                            }


                        }



                    } catch (Exception e) {
                        //断网提示
                        e.printStackTrace();
                        System.out.println("这里有大问题");
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

            }

        }).start();






    }












        class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
            private Context mContext;//上下文

            public  MyAdapter(Context context) {
                //构造方法，用于接收上下文和展示数据
                this.mContext=context;
            }

            @NonNull
            @Override
            public  RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType==0) {

                    View view = LayoutInflater.from(show.this).inflate(R.layout.item_long,parent,false);
                    //实例化一个发送的ViewHolder
                    show.MyViewHoder myViewHoder = new show.MyViewHoder(view);

                    return myViewHoder;
                }
                else {
                    View view = LayoutInflater.from(show.this).inflate(R.layout.long_short,parent,false);
                    //实例化一个发送的ViewHolder
                    show.dateshow myViewHoder = new show.dateshow(view);
                    return myViewHoder;
                }
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                if (getItemViewType(position)==0){
                    commentsShow news = mNewsList.get(position);
                    MyViewHoder sendViewHolder = (MyViewHoder) holder;
                    sendViewHolder.mname.setText(news.name);
                    sendViewHolder.mcomment.setText(news.commen);
                    sendViewHolder.thisgood.setText(news.like);
                    sendViewHolder.now_time.setText(date(news.time));
                    //

                    Glide.with(mContext).load(news.url)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(sendViewHolder.people);


                    
                    
                }else {
                    commentsShow news = mNewsList.get(position);
                    dateshow receiveViewHolder = (dateshow) holder;
                    //给控件显示内容
                    receiveViewHolder.Hint.setText(news.hint);

                }



            }


            @Override
            public int getItemViewType(int position) {

                return mNewsList.get(position).type;
            }
            @Override
            public int getItemCount() {
                return mNewsList.size();
            }




        }








        //缓存组件 用两个
        public class MyViewHoder extends RecyclerView.ViewHolder {
            TextView mname;
            TextView mcomment;
            TextView now_time;
            TextView thisgood;
            ImageView people;

            public MyViewHoder(@NonNull View itemView) {
                super(itemView);
                mname = itemView.findViewById(R.id.name);
                mcomment = itemView.findViewById(R.id.comment);
                now_time=itemView.findViewById(R.id.show_time);
                people=itemView.findViewById(R.id.people);

                //设置一下圆角
//                people.layer;
                thisgood= itemView.findViewById(R.id.d);
            }
        }




        public class dateshow extends RecyclerView.ViewHolder {
            TextView Hint;

            public dateshow(@NonNull View itemView) {
                super(itemView);
                Hint=itemView.findViewById(R.id.longshort);

            }
        }

        //把时间传进取处理一下
        public String date(Date whatTime)
        {
            SimpleDateFormat ft = new SimpleDateFormat ("MM-dd       hh:mm");
            return ft.format(whatTime);
        }


}
