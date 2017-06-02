package com.bbq.iknow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenbaoqiang on 2017/3/15.
 */
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = UserInfoActivity.class.getSimpleName() ;
    List<Integer> list  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


    }


    @Override
    public void onClick(View v) {

    }
}
