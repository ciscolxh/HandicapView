package com.loveluo.arcview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


/**
 * @author 罗富清
 */
public class SelectActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initView();

    }

    private void initView() {
        /*
      扇形统计图
     */
        TextView btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        /*
      深度图1
     */
        TextView btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        /*
      深度图2
     */
        TextView btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        /*
      深度图3
     */
        TextView btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            default:
                break;
            case R.id.btn1:
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn2:
                intent.setClass(this, MyTestActivity.class);
                startActivity(intent);
                break;
            case R.id.btn3:
                intent.setClass(this, DepthMapOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.btn4:

                break;
        }
    }


}
