package com.feinno.androidbase.network.tcp;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.feinno.androidbase.threadpool.PriorityThreadFactory;
import com.feinno.androidbase.utils.log.LogFeinno;


public class SocketClient implements ISocketClentEvent{
	private final String fTag = "RF_SocketClient";
	public static final int DEFAULT_SEND_BUFFER_SIZE = 64 * 1024;
	public static final int DEFAULT_RECEIVE_BUFFER_SIZE = 64 * 1024;
	public static final int DEFAULT_CONNECT_TIMEOUT = 30000;
	IConnectionReadEvent mListener;
	private Socket mSocket;
	// private boolean mIsConnected;
	private InputStream mInputStream;
	private OutputStream mOutputStream;
	private int mConnectTimeout = DEFAULT_CONNECT_TIMEOUT;
	private final PriorityThreadFactory mPriorityThreadFactory;
	private FutureTask<?> mSocketReceiveTask;
	private final Object object = new Object();
	private static SSLContext sc;
	InetSocketAddress address = null;

	/**
	 * 构造一个Socket客户机
	 * 
	 * @param listener
	 */
	public SocketClient() {
		mPriorityThreadFactory = new PriorityThreadFactory(fTag);
	}


	public void setConnectTimeout(int connectTimeout) {
		mConnectTimeout = connectTimeout;
	}
	
	private void trustAllHttpsCertificates() throws Exception {  
        TrustManager[] trustAllCerts = new TrustManager[1];  
        TrustManager tm = new miTM();  
        trustAllCerts[0] = tm;  
        sc = SSLContext.getInstance("SSL");  
        sc.init(null, trustAllCerts, null);  
        HttpsURLConnection.setDefaultSSLSocketFactory(  
                sc.getSocketFactory());  
  
    }  
  
    public static class miTM implements TrustManager, X509TrustManager {  
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
            return null;  
        }  
  
        public boolean isServerTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public boolean isClientTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public void checkServerTrusted(  
                java.security.cert.X509Certificate[] certs, String authType) throws  
                java.security.cert.CertificateException {  
            return;  
        }  
  
        public void checkClientTrusted(  
                java.security.cert.X509Certificate[] certs, String authType) throws  
                java.security.cert.CertificateException {  
            return;  
        }  
    }  

    /**
	 * 连接Socket
	 * 
	 * @return
	 */
	@Override
	public synchronized void connect() {
		if (LogFeinno.DEBUG) {
			LogFeinno.d(fTag, "connect.mIsConnected = " + isConnected() + ", address = " + address);
		}
		if (!isConnected() && address != null) {
			try {
//				trustAllHttpsCertificates();
//				SSLSocketFactory socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
//				mSocket = sc.getSocketFactory().createSocket(host, port);
//				mSocket.setSoTimeout(mConnectTimeout);
				mSocket = new Socket();
				mSocket.connect(address, mConnectTimeout); // 连接服务器
				if (LogFeinno.DEBUG) {
					LogFeinno.e(this.getClass().getName(), "==========connect==========" + mSocket.hashCode());
				}
				try {
					mSocket.setSendBufferSize(DEFAULT_SEND_BUFFER_SIZE); // 发送时使用的缓冲区大小
					mSocket.setReceiveBufferSize(DEFAULT_RECEIVE_BUFFER_SIZE); // 接收时使用的缓冲区大小
					mSocket.setTcpNoDelay(true); // 关闭Nagle算法，数据不作缓冲，立即发包
					mSocket.setSoLinger(true, 0); // 关闭时立即释放资源，底层的Socket也会立即关闭，所有未发送完的剩余数据被丢弃
				} catch (SocketException e) {
					if (LogFeinno.DEBUG) {
						LogFeinno.e(fTag, "connect.SocketException" + e.getMessage());
					}
				}
				mInputStream = mSocket.getInputStream();
				mOutputStream = mSocket.getOutputStream();
			} catch (IOException e) {
				if (LogFeinno.DEBUG) {
					LogFeinno.e(fTag, "connect.IOException" + e);
				}
			}
			if (isConnected() && mListener != null) {
				mSocketReceiveTask = new FutureTask<Void>(new Callable<Void>() {
					@Override
					public Void call() throws IOException {
						while (isConnected()) {
							// TODO
							if (mInputStream.available() == 0) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									if (LogFeinno.DEBUG) {
										LogFeinno.e(fTag, "InterruptedException", e);
									}
								} catch (Exception ex) {
									if (LogFeinno.DEBUG) {
										LogFeinno.e(fTag, "Exception", ex);
									}
								}
								continue;
							}
							synchronized (object) {
								mListener.onConnectionRead(mInputStream);
							}
						}
						return null;
					}
				}) {
					@Override
					protected void done() {
						try {
							get();
						} catch (InterruptedException e) {
							if (LogFeinno.DEBUG) {
								LogFeinno.e(fTag, "InterruptedException", e);
							}
						} catch (ExecutionException e) {
							if (e.getCause() instanceof EOFException) { // 先抛IOException的子类
								if (LogFeinno.DEBUG) {
									LogFeinno.e(fTag, "EOFException", e);
								}
							} else { // 注销会EOF异常，其他异常都重登
								if (LogFeinno.DEBUG) {
									LogFeinno.e(fTag, "Exception", e);
								}
							}
							mListener.onConnectionRead(null);
						} catch (CancellationException e) {
							if (LogFeinno.DEBUG) {
								LogFeinno.e(fTag, "CancellationException", e);
							}
						} catch (Exception e) {
							if (LogFeinno.DEBUG) {
								LogFeinno.e(fTag, "Exception", e);
							}
						}
					}
				};
				mPriorityThreadFactory.newThread(mSocketReceiveTask).start();
			}
		}
		if (LogFeinno.DEBUG) {
			LogFeinno.d(fTag, "connect.isConnected = " + isConnected());
		}
	}
	

	/**
	 * 关闭Socket wifi、gprs网络切换导致的IO异常，必须断开socket，否则会造成网络流读写异常
	 * IO异常后socket就不能再继续使用了，否则会一直报java.net.SocketException: sendto failed: EPIPE
	 * (Broken pipe)
	 */
	public synchronized void close() {
		if (isConnected()) {
			try {
				if (mSocketReceiveTask != null && !mSocketReceiveTask.isCancelled()) {
					mSocketReceiveTask.cancel(true);
				}
			} catch (Exception e) {
				if (LogFeinno.DEBUG) {
					LogFeinno.e(fTag, "mSocketReceiveTask  cancel: " + e.getMessage());
				}
			}
			if (mSocket != null && !mSocket.isClosed()) {
				try {
					try {
						if (!mSocket.isInputShutdown()) {
							mSocket.shutdownInput();
						}
						if (!mSocket.isOutputShutdown()) {
							mSocket.shutdownOutput();
						}
					} catch (Exception e) {
						if (LogFeinno.DEBUG) {
							LogFeinno.e(fTag, "Input and Out Shutdown: " + e.getMessage());
						}
					}
					synchronized (object) {
						mSocket.close();
						if (LogFeinno.DEBUG) {
							LogFeinno.e(this.getClass().getName(), "==========close==========" + mSocket.hashCode());
						}
					}
				} catch (Exception e) {
					if (LogFeinno.DEBUG) {
						LogFeinno.e(fTag, "close.Exception : " + e.getMessage());
					}
				} finally {

				}

			}
			if (LogFeinno.DEBUG) {
				LogFeinno.d(fTag, "close.isClosed = " + !isConnected());
			}
		}
	}

	/**
	 * 检查Socket是否已经连接，没有连接时连接Socket；
	 * 
	 * @return
	 */
	public boolean checkConnect() {
		connect();
		if (LogFeinno.DEBUG) {
			LogFeinno.d(fTag, "checkConnect.isConnected = " + isConnected());
		}
		return isConnected();
	}

	public void send(byte[] bytes) throws IOException {
		if (LogFeinno.DEBUG) {
			LogFeinno.d(fTag,
					"send.mIsConnected = " + isConnected() + ", bytes.length = "
							+ (bytes == null ? 0 : bytes.length));
		}
		if (mOutputStream != null && bytes != null) {
			mOutputStream.write(bytes);
			mOutputStream.flush();
		}
	}

	@Override
	public boolean isConnected() {
		if (null == mSocket)
			return false;
		return ((!mSocket.isClosed()) && mSocket.isConnected());
	}

	@Override
	public void setAddress(InetSocketAddress address) {
		this.address = address;

	}

	@Override
	public void registerReadEvent(IConnectionReadEvent event) {
		mListener=event;
	}

}