package com.feinno.androidbase.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.feinno.androidbase.utils.log.LogFeinno;

/**
 * Created by wangqiaoqiao on 2016/5/18.
 */
public class ScreenBroadcastReceiver extends BroadcastReceiver {
    private String action = null;

    public static void register(Context ctx) {
        ScreenBroadcastReceiver receiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        ctx.registerReceiver(receiver, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            // 开屏
            SystemUtils.setScreenState(false);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            // 锁屏
            SystemUtils.setScreenState(true);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            // 解锁
            SystemUtils.setScreenState(false);
        }
        LogFeinno.i("LOCK","SystemUtils.isScreenLock:"+SystemUtils.getScreenState()+"");

    }
}
