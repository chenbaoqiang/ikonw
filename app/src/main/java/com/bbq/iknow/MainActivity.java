package com.bbq.iknow;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bbq.iknow.custom.activity.CustomViewActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        Button btModel = (Button) findViewById(R.id.bt_model);
        Button btGetActivity = (Button) findViewById(R.id.bt_get_activity);
        Button btCustomView = (Button) findViewById(R.id.bt_custom_view);


        btModel.setOnClickListener(this);
        btGetActivity.setOnClickListener(this);
        btCustomView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_model) {
            Intent intent = new Intent(this, ChatMainActivity.class);
            startActivity(intent);
        }else  if (id == R.id.bt_get_activity) {
            //dumpsys activity activities
            ArrayList activities = getActivities(this);

        }else  if (id == R.id.bt_custom_view) {
            Intent intent = new Intent(this, CustomViewActivity.class);
            startActivity(intent);
        }
    }


    public static ArrayList getActivities(Context ctx) {
        ArrayList result = new ArrayList();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(ctx.getPackageName());
        for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(intent, 0)) {
            result.add(info.activityInfo.name);
            Log.d(TAG, "MainActivity  getActivities  name  = " + info.activityInfo.name);

        }
        return result;
    }
}
