package com.yanbo.videodlnascreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.yanbo.lib_screen.entity.RemoteItem;
import com.yanbo.lib_screen.manager.ClingManager;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button button1;
    RecyclerView recyclerView;
    String  url1="http://220.170.49.101:8080/6/a/o/w/z/aowzdtjxyzarajszckljusoswxkskk/hc.yinyuetai.com/44E4016521C693F23F7E9344AEBF5AF0.mp4?sc=44cf623e14029025&br=781&vid=3266995&aid=35&area=ML&vst=0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        button1=findViewById(R.id.button1);
        recyclerView=findViewById(R.id.recycler_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceListActivity.startSelf(MainActivity.this);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteItem itemurl1 = new RemoteItem("一路之下", "425703", "张杰",
                        107362668, "00:04:33", "1280x720", url1);
                ClingManager.getInstance().setRemoteItem(itemurl1);
                MediaPlayActivity.startSelf(MainActivity.this);
            }
        });
    }
}
