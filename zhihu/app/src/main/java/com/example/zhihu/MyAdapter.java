//package com.example.zhihu;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//
//public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private Context mContext;//上下文
//
//    public  MyAdapter(Context context) {
//        //构造方法，用于接收上下文和展示数据
//        this.mContext=context;
//    }
//
//
//
//    @NonNull
//    @Override
//    public  RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        RecyclerView.ViewHolder holder=null;
//        if (viewType==0) {
//
//            View view = LayoutInflater.from(mContext).inflate(R.layout.item_list,parent,false);
//            //实例化一个发送的ViewHolder
//            holder = new MyViewHoder(view);
//
//        }
//        else {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.item_list,parent,false);
//            //实例化一个发送的ViewHolder
//            holder = new dateshow(view);
//
//        }
//        assert holder!=null;
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MainActivity.MyViewHoder holder, int position) {
//        item news = mNewsList.get(position);
//        holder.mTitleTv.setText(news.title);
//        holder.mTitlehint.setText(news.hint);
//        Glide.with(mContext).load(news.imgurl).into(holder.img);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mNewsList.size();
//    }
//}
////缓存组件 用两个
//class MyViewHoder extends RecyclerView.ViewHolder {
//    TextView mTitleTv;//大标题
//    TextView mTitlehint;//标题下面那个
//    ImageView img;//显示的图片
//
//    public MyViewHoder(@NonNull View itemView) {
//        super(itemView);
//        mTitleTv = itemView.findViewById(R.id.textView);
//        mTitlehint = itemView.findViewById(R.id.textView2);
//        img=itemView.findViewById(R.id.item_image);
//
//    }
//}
//class dateshow extends RecyclerView.ViewHolder {
//    //在这里面不用剩下的
//    TextView mTitleTv;//在这里当一下日期吧
//    TextView mTitlehint;//标题下面那个
//    ImageView img;//显示的图片
//    public dateshow(@NonNull View itemView) {
//        super(itemView);
//        mTitleTv=itemView.findViewById(R.id.dayRecy);
//
//    }
//}