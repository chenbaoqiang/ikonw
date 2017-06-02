package com.bbq.iknow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenbaoqiang on 2017/3/15.
 */
public class ChatMainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ChatMainActivity.class.getSimpleName() ;
    List<Integer> list  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        Button btAdd = (Button) findViewById(R.id.bt_add);
        btAdd.setOnClickListener(this);
        Button btGet = (Button) findViewById(R.id.bt_get);
        btGet.setOnClickListener(this);
        Button bt_entry = (Button) findViewById(R.id.bt_entry);
        bt_entry.setOnClickListener(this);

        Button bt_entry_other = (Button) findViewById(R.id.bt_entry_other);
        bt_entry_other.setOnClickListener(this);


        Log.d(TAG,"ChatMainActivity onCreate");

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG,"ChatMainActivity onNewIntent");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"ChatMainActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"ChatMainActivity onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"ChatMainActivity onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"ChatMainActivity onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"ChatMainActivity onDestroy");

    }



    @Override
    public void finish() {
        /**
         * 记住不要执行此句 super.finish(); 因为这是父类已经实现了改方法
         * 设置该activity永不过期，即不执行onDestroy()

         */
        Log.d(TAG,"ChatMainActivity finish");

        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bt_add){
            list.add(1);
        }else if (id == R.id.bt_get){
            Log.d(TAG,"ChatMainActivity list.size = " + list.size());

        }else if (id == R.id.bt_entry){
            Intent intent  = new  Intent(this,UserInfoActivity.class);
            startActivity(intent);

        }else if (id == R.id.bt_entry_other){
            Intent intent  = new  Intent(this,UserInfoActivity.class);
            startActivityForResult(intent,1000);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
        }
    }

}
