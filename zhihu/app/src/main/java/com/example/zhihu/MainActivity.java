package com.example.zhihu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhihu.utils.SPUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存


    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    List<item> mNewsList = new ArrayList<>();
    public int pastday;
    private RequestQueue mQueue;
    private onclickListen myuse;
    private TextView tx1;
    private TextView tx2;
    private TextView tx3;
    String today;
    private ImageView img1;
    RefreshLayout refreshLayout;



    //
    private Banner banner;
    private MyImageLoader myImageLoader;
    private ArrayList<String> imagePath;
    private ArrayList<String> imageTitle;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //刷新头
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败




            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败




            }
        });

//        final RefreshLayout zzf = (RefreshLayout) findViewById(R.id.refreshLayout);
//        zzf.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));

        //设置一下导航栏
        final Calendar today = Calendar.getInstance();
        int day = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH) + 1;
        mRecyclerView = findViewById(R.id.recyclerview);
        tx1=findViewById(R.id.mathday);
        tx2=findViewById(R.id.chineseday);
        tx3=findViewById(R.id.hellow);
        tx1.setText(Integer.toString(day));
        tx2.setText(getMonthC(month));
        img1=findViewById(R.id.people);
        //进入个人主页

        String imageUrl = SPUtils.getString("imageUrl",null,this);
        if(imageUrl != null){
            Glide.with(this).load(imageUrl).apply(requestOptions).into(img1);
        }


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,homepag.class);

                startActivity(intent);
            }
        });




        final String date = new SimpleDateFormat("HH").format(today.getTime());
        tx3.setText(hellow(Integer.parseInt(date)));

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String temporary;
                item listen;
                pastday=0;
                for (int a = 0; a < 4; a++) {
                    if (a==0)
                    {
                        temporary=new String("https://news-at.zhihu.com/api/4/news/latest");
                    }
                    else {
                        temporary=new String("https://news-at.zhihu.com/api/4/news/before/" + getDate());
                        pastday++;
                    }

                    try {
                        URL url = new URL(temporary);

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
                                if(a!=0)
                                {
                                    listen=new item();
                                    listen.date=jsonObject.getString("date");
                                    listen.type=1;

                                    mNewsList.add(listen);
                                }
                                JSONArray jsonArray = new JSONArray(jsonObject.get("stories").toString());

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                                    JSONArray images = jsonObject1.getJSONArray("images");

                                    listen = new item();
                                    listen.title = jsonObject1.getString("title");
                                    listen.hint = jsonObject1.getString("hint");
                                    listen.url=jsonObject1.getString("url");
                                listen.imgurl=images.getString(0);
                                    listen.date = getDate();
                                    listen.type=0;
                                    listen.id = jsonObject1.getString("id");
                                    mNewsList.add(listen);

                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        mMyAdapter = new MyAdapter(MainActivity.this);
                                        mRecyclerView.setAdapter(mMyAdapter);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
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
            }

        }).start();


        setListener(new onclickListen() {
            @Override
            public void onRecycler(int position) {
                Intent intent=new Intent(MainActivity.this, web.class);
                item news = mNewsList.get(position);
                intent.putExtra("url",news.url);
                intent.putExtra("id",news.id);
                intent.putExtra("title",news.title);
                intent.putExtra("img",news.imgurl);
                intent.putExtra("position",String.valueOf(position));
                startActivity(intent);
            }
        });


        //最后在加载刷新头和内容
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        //如果最新等于刷新出来的 就不加载 不然刷新
                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        String temporary;
                        item listen;
                        pastday=0;
                        for (int a = 0; a < 4; a++) {
                            if (a==0)
                            {
                                temporary=new String("https://news-at.zhihu.com/api/4/news/latest");
                            }
                            else {
                                temporary=new String("https://news-at.zhihu.com/api/4/news/before/" + getDate());
                                pastday++;
                            }

                            try {
                                URL url = new URL(temporary);

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
                                        if(a==0)
                                        {
                                            listen=new item();
                                            listen.date=jsonObject.getString("date");
                                            if (date.equals(today)) break;
                                            mNewsList=new ArrayList<>();
                                        }
                                        else {
                                            listen=new item();
                                            listen.date=jsonObject.getString("date");
                                            listen.type=1;

                                            mNewsList.add(listen);

                                        }
                                        JSONArray jsonArray = new JSONArray(jsonObject.get("stories").toString());

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                                            JSONArray images = jsonObject1.getJSONArray("images");

                                            listen = new item();
                                            listen.title = jsonObject1.getString("title");
                                            listen.hint = jsonObject1.getString("hint");
                                            listen.url=jsonObject1.getString("url");
                                            listen.imgurl=images.getString(0);
                                            listen.date = getDate();
                                            listen.type=0;
                                            listen.id = jsonObject1.getString("id");
                                            mNewsList.add(listen);

                                        }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mMyAdapter = new MyAdapter(MainActivity.this);
                                                mRecyclerView.setAdapter(mMyAdapter);
                                                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
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
                                refreshlayout.finishRefresh(false/*,false*/);//传入false表示刷新失败
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
                    }

                }).start();





            }
        });


        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败

                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        String temporary;
                        item listen;

                        for (int a = 0; a < 4; a++) {

                                temporary=new String("https://news-at.zhihu.com/api/4/news/before/" + getDate());
                                pastday++;


                            try {
                                URL url = new URL(temporary);

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

                                            listen=new item();
                                            listen.date=jsonObject.getString("date");
                                            listen.type=1;

                                            mNewsList.add(listen);

                                        JSONArray jsonArray = new JSONArray(jsonObject.get("stories").toString());

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
                                            JSONArray images = jsonObject1.getJSONArray("images");

                                            listen = new item();
                                            listen.title = jsonObject1.getString("title");
                                            listen.hint = jsonObject1.getString("hint");
                                            listen.url=jsonObject1.getString("url");
                                            listen.imgurl=images.getString(0);
                                            listen.date = getDate();
                                            listen.type=0;
                                            listen.id = jsonObject1.getString("id");
                                            mNewsList.add(listen);

                                        }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
//                                                mMyAdapter = new MyAdapter(MainActivity.this);
//                                                mRecyclerView.setAdapter(mMyAdapter);
//                                                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//                                                mRecyclerView.setLayoutManager(layoutManager);


                                                mMyAdapter.notifyDataSetChanged();

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
                                refreshlayout.finishLoadMore(false);
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
                    }

                }).start();


            }
        });



    }




    //子函数
    //构造的类
    //重写的子函数



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

                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list,parent,false);
                //实例化一个发送的ViewHolder
                MyViewHoder myViewHoder = new MyViewHoder(view);

                return myViewHoder;
            }
            else {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.date,parent,false);
                //实例化一个发送的ViewHolder
                dateshow myViewHoder = new dateshow(view);
                return myViewHoder;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position)==0){
                item news = mNewsList.get(position);
                MyViewHoder sendViewHolder = (MyViewHoder) holder;

                sendViewHolder.mTitleTv.setText(news.title);
                sendViewHolder.mTitlehint.setText(news.hint);
                Glide.with(mContext).load(news.imgurl).into(sendViewHolder.img);

                Glide.with(mContext).load(news.imgurl)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                        .into(sendViewHolder.img);

            }else {
                item news = mNewsList.get(position);
                dateshow receiveViewHolder = (dateshow) holder;
                //给控件显示内容
                receiveViewHolder.leftday.setText(Tr(news.date));
                System.out.println(Tr(news.date));

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
        TextView mTitleTv;//大标题
        TextView mTitlehint;//标题下面那个
        ImageView img;//显示的图片

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.textView);
            mTitlehint = itemView.findViewById(R.id.textView2);
            img=itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myuse!=null)
                    {
                        myuse.onRecycler(getAdapterPosition());
                    }
                }
            });

        }
    }




    public class dateshow extends RecyclerView.ViewHolder {

        TextView leftday;//在这里当一下日期吧

        public dateshow(@NonNull View itemView) {
            super(itemView);
            leftday=itemView.findViewById(R.id.dayRecy);

        }
    }





    public void setListener(onclickListen listen)
    {
        myuse=listen;
    }
    public interface onclickListen{
        void onRecycler(int position);
    }

    private String getDate(){
        //获取当前需要加载的数据的日期
        Calendar d = Calendar.getInstance();
        d.setTime(new Date());
        d.add(Calendar.DAY_OF_MONTH, -pastday);//几天前

        String date = new SimpleDateFormat("yyyyMMdd").format(d.getTime());

        return date;

    }

    //写个蛋疼的子函数
    //返回月份的中文形式
    public String getMonthC(int month)
    {
        switch (month) {
            case 1:return "一月";
            case 2:return "二月";
            case 3:return "三月";
            case 4:return "四月";
            case 5:return "五月";
            case 6:return "六月";
            case 7:return "七月";
            case 8:return "八月";
            case 9:return "九月";
            case 10:return "十月";
            case 11:return "十一月";
            case 12:return "十二月";

        }
    return "没有这个月份";
    }

    //蛋疼的子函数二 返回早中晚
    public String hellow(int time)
    {
        if (time<6)
            return "凌晨了！";
        if (time<12)
            return "早上好！";
        if (time<17)
            return "中午好!";
        if (time<20)
            return "下午好!";
        if(time<24)
            return "晚上好!";
        return "什么点了！";

    }
    //转换data为几月几日
    public String Tr(String date)
    {

        return date.substring(4,6)+"月"+date.substring(6,8)+"日";
    }



    //轮播

//        private void initBanner() {
//
//
//            for (int i=0;i<4;i++)
//            {
//                item news = mNewsList.get(i);
//                imagePath.add(news.imgurl);
//                imageTitle.add(news.title);
//
//
//            }
//            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//            banner.setImageLoader(new MyImageLoader());
//            banner.setImages(imagePath);
//            banner.setBannerTitles(imageTitle);
//            banner.setBannerAnimation(Transformer.Default);
//            //切换频率
//            banner.setDelayTime(2000);
//            //自动启动
//            banner.isAutoPlay(true);
//            //位置设置
//            banner.setIndicatorGravity(BannerConfig.CENTER);
//            //开始运行
//            banner.start();
//        }
//
//
//    public class  MyImageLoader extends ImageLoader {
//        /**
//         * Constructs a new ImageLoader.
//         *
//         * @param queue      The RequestQueue to use for making image requests.
//         * @param imageCache The cache to use as an L1 cache.
//         */
//        public MyImageLoader(RequestQueue queue, ImageCache imageCache) {
//            super(queue, imageCache);
//        }
//
//        @Override
//        public void displayImage(Context context, Object path, ImageView imageView) {
//            Glide.with(context.getApplicationContext())
//                    .load(path)
//                    .into(imageView);
//        }
//    }







}

