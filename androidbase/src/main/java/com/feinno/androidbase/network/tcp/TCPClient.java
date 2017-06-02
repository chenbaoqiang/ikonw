package com.feinno.androidbase.network.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.feinno.androidbase.common.FtConfig;
import com.feinno.androidbase.utils.log.LogFeinno;


public class TCPClient {
	private static final String TAG = FtConfig.FtLogNet+ TCPClient.class.getSimpleName();

	/** 网络敏感度时间，即网络断开了开始计时到该时间统一返回所有请求 */
	public static final long TIMEOVER_SENSITIVITY = 5 * 1000;
	/** socket请求发送时等待服务器响应的时间 */
	public static final long TIMEOUT_SOCKET_RESPONSE = 1 * 60 * 1000;
	/** 消息类的socket请求发送时等待服务器响应的时间 */
	public static final long TIMEOUT_MESSAGE_SOCKET_RESPONSE = 3 * 60 * 1000;

	private final String fHttpDefaultAcceptCharset = "UTF-8";
	private final String fHttpDefaultAcceptLanguage = "zh";
	private final ISocketClentEvent mSocketClient;
	private final int CONNECT_TIMEOUT = 20000;
	private boolean mIsNetworkConnected = true;
	public final WakeLockProxy mWakeLockProxy;

	/**
	 * 构造网络管理模块；
	 * 
	 * @param service
	 */
	public TCPClient(WakeLockProxy wakeLockProxy, IConnectionReadEvent socketListener) {
		mWakeLockProxy = wakeLockProxy;
		mSocketClient = SampleSocketClientFactory.createSocketClient(SampleSocketClientFactory.NIO);
		// TODO
		// mSocketClient.setConnectTimeout(CONNECT_TIMEOUT);
		mSocketClient.registerReadEvent(socketListener);
	}

//	private int getQueueSize() {
//		return mExecutorHttpRequest.getActiveCount() + mExecutorHttpRequest.getQueue().size();
//	}

	public void setSocketAddress(String address) {
		if (LogFeinno.DEBUG) {
			LogFeinno.e(TAG, "setSocketAddress.address = " + address);
		}
		String host = null;
		int port = 0;
		int dotPos = address.indexOf(':');
		if (dotPos > 0) {
			host = address.substring(0, dotPos).trim();
			port = Integer.parseInt(address.substring(dotPos + 1).trim());
		} else if (dotPos < 0) {
			host = address;
		}
		InetSocketAddress addr = new InetSocketAddress(host, port);
		mSocketClient.setAddress(addr);
	}

	// public void onNetworkChanged(boolean isNetworkConnected, int apnType) {
	// mIsNetworkConnected = isNetworkConnected;
	// mCurrentApnType = apnType;
	// if (LogF.DEBUG) {
	// LogF.d(fTag, "onNetworkChanged.mIsNetworkConnected = " +
	// mIsNetworkConnected + ", mCurrentApnType = " + mCurrentApnType);
	// }
	// if (mIsNetworkConnected) {
	// mTaskManagerSensitivityTimeOver.cancelTask(mTimerTaskSensitivityTimeOver);
	// } else {
	// pauseSocketRequest();
	// closeSocket();
	// mTaskManagerSensitivityTimeOver.addTask(mTimerTaskSensitivityTimeOver,
	// TIMEOVER_SENSITIVITY);
	// }
	// }

	/**
	 * wifi、gprs网络切换导致的IO异常，必须断开socket，否则会造成网络流读写异常
	 * IO异常后socket就不能再继续使用了，否则会一直报java.net.SocketException: sendto failed: EPIPE
	 * (Broken pipe)
	 */

	public boolean sendSocketRequest(byte[] bytes) {
		try {
			mSocketClient.send(bytes);
			return true;
		} catch (Exception e) {
			LogFeinno.e(TAG,"sendSocketRequest",e);
			closeSocket();
			return false;
		}
	}

	public boolean connectSocket() {
		LogFeinno.e(TAG,"connectSocket()");
		try {
			mSocketClient.connect();
		} catch (IOException e) {
			if (LogFeinno.DEBUG) {
				LogFeinno.e(TAG, "IOException", e);
			}
		}
		return mSocketClient.isConnected();
	}

	public void closeSocket() {
		mSocketClient.close();
	}

	public boolean isConnected(){
		return mSocketClient.isConnected();
	}

	/**
	 * 销毁，关闭Socket、结束所有线程池、清空所有请求和响应队列；
	 */
	public void destroy() {
		if (LogFeinno.DEBUG) {
			LogFeinno.d(TAG, "destroy");
		}
		closeSocket();
	}

	public interface OnSocketResponse {
		/**
		 * 响应Socket的接口方法；
		 * 
		 * @param isServerResponse
		 *            是服务器的响应
		 * @param nativeStatusCode
		 *            <dd>
		 *            <li>{@link NativeStatusCode#NC_NETWORK_TIMEOUT}<br>
		 *            <dd>
		 *            <li>{@link NativeStatusCode#NC_NETWORK_DISCONNECTED}<br>
		 *            <dd>
		 *            <li>{@link NativeStatusCode#NC_SOCKET_REQUEST_SEND_FAILED}
		 *            <br>
		 *            <br>
		 * @param mResponse
		 *            SocketResponse
		 */
		public void onSocketResponse(boolean isServerResponse, int nativeStatusCode);
	}

	public interface WakeLockProxy {
		/**
		 * 获取休眠锁
		 */
		public void acquireWakeLock();

		/**
		 * 释放休眠锁
		 */
		public void releaseWakeLock();
	}
}