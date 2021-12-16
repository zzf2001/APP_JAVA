package com.example.news;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView bottomLeft;
    private ImageView bottonMid;
    private ImageView bottonright;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        Transition explode = TransitionInflater.from(this).inflateTransition(.R.transition.explode);
//        getWindow().setEnterTransition(explode);
        replaceFragment(new newsShow());
        setContentView(R.layout.activity_main);
        bottomLeft=findViewById(R.id.hoemPag);
        bottomLeft.setOnClickListener(this);
        bottonMid=findViewById(R.id.publish);
        bottonMid.setOnClickListener(this);
        bottonright=findViewById(R.id.person);
        bottonright.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.hoemPag: {
                replaceFragment(new newsShow());
                break;
            }
            case R.id.publish:
                {
                    replaceFragment(new newsPublish());
                    break;

                }
            case R.id.person:
            {
                replaceFragment(new showPerson());
                break;

            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction transaction =fragmentManager.beginTransaction();
        transaction.replace(R.id.basic,fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }
}