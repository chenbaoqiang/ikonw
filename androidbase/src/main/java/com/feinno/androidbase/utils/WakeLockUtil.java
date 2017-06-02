/*
 * 创建日期：2013-3-30
 */
package com.feinno.androidbase.utils;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;

import com.feinno.androidbase.system.SystemUtils;
import com.feinno.androidbase.utils.log.LogFeinno;

/**
 * 版权所有 2007-2013 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名：飞信 - Android客户端<br>
 * 描述：
 *  
 * @version 1.0
 * @since JDK1.5
 *
 */
public class WakeLockUtil {
	private final String mTag = "RF_Power" + WakeLockUtil.class.getSimpleName();
	private final WakeLock mWakeLock;
	long sumcount = 0;
	long sumconsume = 0;
	long lastAcquireTime = 0;
	public static long KeepAlive_Timeout = 60 * 1000; //3s
	public static long Connect_Timeout = 45 * 1000; //5s
	public static long Provision_Timeout = 15 * 1000; //15s
	private Handler mHandler = new Handler();
	Context mContext = null;
	public WakeLockUtil(Context context) {
		mContext = context;
		mWakeLock = ((PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , mTag); // 只关注CPU锁
		mWakeLock.setReferenceCounted(false);
//		mHandler.postDelayed(runnable,30000);
	}
//	private Runnable runnable = new Runnable() {
//		@Override
//		public void run() {
//			LogFeinno.i(mTag, "runnable run");
//			mHandler.postDelayed(runnable,30000);
//		}
//	};


	/**
	 * 获得休眠锁
	 */
	public void acquireWakeLock(final long timeout) {
//		if (mWakeLock != null) {
//			if (LogFeinno.DEBUG) {
//				LogFeinno.i(mTag, "----acquireWakeLock.mWakeLock.isHeld() = " + mWakeLock.isHeld() + ";timeout = " + timeout);
//			}
//			if (!mWakeLock.isHeld()) {
//				sumcount++;
//				mWakeLock.acquire(timeout);
//				lastAcquireTime = SystemClock.elapsedRealtime();
//				LogFeinno.i(mTag, "acquire sumcount = " + sumcount + ";total consume = " + sumconsume + "ms");
//			}
//		}
	}

	/**
	 * 释放休眠锁
	 */
	public void releaseWakeLock() {
//		if (mWakeLock != null) {
//			if (LogFeinno.DEBUG) {
//				LogFeinno.i(mTag, "releaseWakeLock.mWakeLock.isHeld() = " + mWakeLock.isHeld());
//			}
//			if (mWakeLock.isHeld()) {
//				long last = SystemClock.elapsedRealtime() - lastAcquireTime;
//				sumconsume += last;
//				LogFeinno.i(mTag, "release sumcount = " + sumcount + ";total consume = " + sumconsume + "ms");
//				mWakeLock.release();
//			}
//		}
	}
}