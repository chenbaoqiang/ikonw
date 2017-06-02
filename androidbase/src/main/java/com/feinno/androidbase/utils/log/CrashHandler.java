package com.feinno.androidbase.utils.log;

import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名： 飞信 - Android客户端<br>
 * 描述：以进程为单位，处理未捕捉的异常 <br>
 * @author zhangguojunwx
 * @version 1.0
 * @since JDK1.5
 */
public class CrashHandler {
	private final String TAG = "RF_CrashHandler";
//	private final String mTag;
	private long mPreTerminateMillis;
	private OnCrash mOnCrash;
	private String mVersion = "";
	private Context mContext;
	private String mUserid = "";
	private String mMobilenum = "";
	private String mNickname = "";

	public CrashHandler(Context context, String version) {
		this.mContext = context;
		this.mVersion = version;
	}

	public void setUserInfo(String userid,String mobilenum,String nickname){
		this.mUserid = userid;
		this.mMobilenum = mobilenum;
		this.mNickname = nickname;
	}

	public void init(long preTerminateMillis, OnCrash onCrash) {
		mPreTerminateMillis = preTerminateMillis;
		mOnCrash = onCrash;
		// 为所在进程内所有Thread设置一个默认的UncaughtExceptionHandler
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(final Thread thread, final Throwable ex) {
				if (LogFeinno.DEBUG) {
					LogFeinno.e(TAG, "mTag = " + TAG + ", thread = " + thread, ex);
				}

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				String info = "version = " + mVersion;
				info += " userid = " + mUserid;
				info += " mobile = " + mMobilenum;
				info += " nickname = " + mNickname;

				new Thread() {
					@Override
					public void run() {
						Looper.prepare();
						if (mOnCrash != null) {
							String info = "version = " + mVersion;
							info += " userid = " + mUserid;
							info += " mobile = " + mMobilenum;
							info += " nickname = " + mNickname;
							mOnCrash.onPreTerminate(thread, info, ex);
						}
						Looper.loop();
					}
				}.start();
				SystemClock.sleep(mPreTerminateMillis);
				if (mOnCrash != null) {
					mOnCrash.onTerminate(thread, info, ex);
				}
			}
		});
	}

}