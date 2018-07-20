package com.loveluo.arcview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loveluo.arcview.entity.DateEntity;
import com.loveluo.arcview.view.ArcView;
import com.loveluo.arcview.view.ReatFView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luofuqing
 */
public class MainActivity extends AppCompatActivity {
    private ArcView arcView;
    private ReatFView reatFView;
    List<DateEntity> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        arcView = findViewById(R.id.arcView);
        reatFView = findViewById(R.id.reatFView);
        list.add(new DateEntity(1.0f, Color.RED));
        list.add(new DateEntity(2.0f,Color.GREEN));
        list.add(new DateEntity(7.0f,Color.BLUE));
        list.add(new DateEntity(9.0f,Color.YELLOW));
        list.add(new DateEntity(3.0f,Color.CYAN));
        list.add(new DateEntity(8.0f,Color.MAGENTA));
        list.add(new DateEntity(5.0f,Color.DKGRAY));
        arcView.setList(list);
        reatFView.setList(list);
    }
}
