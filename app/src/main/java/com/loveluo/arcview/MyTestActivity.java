package com.loveluo.arcview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loveluo.arcview.utils.DataRequestUtils;
import com.loveluo.arcview.view.HandicapView;

/**
 * @author 罗富清
 * @date 2018/07/12.
 */
public class MyTestActivity extends AppCompatActivity implements HandicapView.SelectItemListener {
    private HandicapView handicapView;
    private TextView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        initView();
        initListener();

    }

    private void initListener() {
        handicapView.setSelectItemListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initData() {
        String json = DataRequestUtils.getStringFromAssert(this, "depth_list.json");
        handicapView.updateData(json);
    }

    private void initView() {
        handicapView = findViewById(R.id.handicap_view);
        btn = findViewById(R.id.btn);
    }

    @Override
    public void selectPrice(String selectPrice) {
        Toast.makeText(this,selectPrice,Toast.LENGTH_SHORT).show();
    }
}
