package com.lx.gaomap.video;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.player.VideoView;
import com.lx.gaomap.R;
import com.lx.gaomap.bean.Fruit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShiPinActivity extends AppCompatActivity {

    String videoUrl = "https://fuyeba.oss-cn-beijing.aliyuncs.com/1591012690920.mp4";
    private RecyclerView recyclerView;

    List<Fruit> fruitList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shipin_activity);

        VideoView videoView = findViewById(R.id.videoView);

        videoView.setUrl(videoUrl); //设置视频地址
        StandardVideoController controller = new StandardVideoController(this);
        controller.addDefaultControlComponent("标题", false);
        videoView.setVideoController(controller); //设置控制器
        videoView.start(); //开始播放，不调用则不自动播放

        fruitList = new ArrayList<>();
        init();

        recyclerView = findViewById(R.id.recyclerView);

        // 		StaggeredGridLayoutManager的构造函数接收两个参数，
        //第一个指定列数为3，第二个指定布局的排列方向
        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layout);
        LiuAdapter adapter = new LiuAdapter(fruitList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new LiuAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int i, String name) {
                Toast.makeText(ShiPinActivity.this, "第" + i + "的名字--" + name, Toast.LENGTH_LONG).show();
            }
        });


    }

    private void init() {
        for (int i = 0; i < 50; i++) {
            Fruit fruit = new Fruit(R.mipmap.gongchanggaizaoxuqiu, getRandomLengthName(i));
            fruitList.add(fruit);
        }
    }

    private String getRandomLengthName(int i) {
        Random random = new Random();
        int Length = random.nextInt(20) + 1;

        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < Length; j++) {
            builder.append("name" + i + "  ");
        }
        return builder.toString();
    }
}
