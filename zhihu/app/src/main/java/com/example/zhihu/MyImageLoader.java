package com.example.zhihu;

import android.content.Context;
import android.widget.ImageView;

public interface MyImageLoader {
    void displayImage(Context context, Object path, ImageView imageView);
}
