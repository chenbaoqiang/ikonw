package com.bbq.iknow.custom.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bbq.iknow.R;
import com.bbq.iknow.custom.view.CustomCircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenbaoqiang on 2017/3/15.
 */
public class CustomViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = CustomViewActivity.class.getSimpleName();
    List<Integer> list = new ArrayList<>();
    int radiu =100;
    private CustomCircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        initView();



    }

    private void initView() {
        circleView = (CustomCircleView) findViewById(R.id.view_custom_cirle);
        circleView.setRadiu(100);

        Button btChange = (Button) findViewById(R.id.view_set_custom_cirle);
        btChange.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.view_set_custom_cirle) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean  isBig = false;
                   while (true){
                       if (radiu == 500) {
                           isBig = true;
                           radiu -= 20;
                       } else if (radiu == 0){
                           isBig = false;
                           radiu += 20;
                       }else{
                           if(isBig){
                               radiu -= 20;
                           }else{
                               radiu += 20;
                           }
                       }
                       circleView.setRadiu(radiu);
                       try {
                           Thread.sleep(100);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
                }
            }).start();


        }
    }
}
