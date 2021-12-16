package com.example.zhihu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class showcollection extends AppCompatActivity {

    ImageView backhome;
    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    List<item> mNewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcollection);
        mRecyclerView = findViewById(R.id.recyclerviewcollcation);
        //返回
        backhome=findViewById(R.id.returnBackhomePag);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 构造一些数据

        for (int i=0;i<100;i++) {
            String z=String.valueOf(i);
            item news = new item();
            SharedPreferences sharedPreferences = getSharedPreferences("collect"+z, web.MODE_PRIVATE);
            // 使用getString方法获得value，注意第2个参数是value的默认值
            String title = sharedPreferences.getString("name", "no");
            String Url = sharedPreferences.getString("URL", "no");
            news.title = title;
            news.imgurl=Url;
            System.out.println(title);
            if(!title.equals("no"))
                mNewsList.add(news);
        }
        mMyAdapter = new MyAdapter(showcollection.this);
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(showcollection.this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        private Context mContext;//上下文

        public  MyAdapter(Context context) {
            //构造方法，用于接收上下文和展示数据
            this.mContext=context;
        }

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(showcollection.this, R.layout.item_list, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            item news = mNewsList.get(position);
            holder.mTitleTv.setText(news.title);
            Glide.with(mContext).load(news.imgurl)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(holder.img);

        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }

    class MyViewHoder extends RecyclerView.ViewHolder {
        TextView mTitleTv;
        ImageView img;



        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.textView);
            img=itemView.findViewById(R.id.item_image);

        }


    }
}