package com.feinno.androidbase.network.tcp;

import com.feinno.androidbase.common.FtConfig;
import com.feinno.androidbase.threadpool.PriorityThreadFactory;
import com.feinno.androidbase.utils.log.LogFeinno;
import com.feinno.androidbase.utils.log.LogTool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


public class SocketNioClient implements ISocketClentEvent {
	private static final String TAG = FtConfig.FtLogNet+ SocketNioClient.class.getSimpleName();

	InetSocketAddress address = null;
	public static final int DEFAULT_SEND_BUFFER_SIZE = 64 * 1024;
	public static final int DEFAULT_RECEIVE_BUFFER_SIZE = 64 * 1024;
	public static final int RECEIVE_BUFFER_SIZE = 16 * 1024;
	public static final int DEFAULT_CONNECT_TIMEOUT = 30000;
	public static final int DEFAULT_READ_TIMEOUT = 1000;
	IConnectionReadEvent readEvent;
	private PriorityThreadFactory mPriorityThreadFactory;
	SelectionKey mKey;
	SocketChannel mChannel;
	Selector selector;
	ByteBuffer readBuffer;

	public SocketNioClient() {
		mPriorityThreadFactory = new PriorityThreadFactory(SocketNioClient.class.getSimpleName());
	}

	public SocketNioClient(InetSocketAddress address) {
		this();
		this.address = address;
	}

	public void setReadEvent(IConnectionReadEvent readEvent) {
		this.readEvent = readEvent;
	}

	@Override
	public synchronized void connect() throws IOException {
		if (LogFeinno.DEBUG) {
			LogFeinno.e(TAG, "connect.mIsConnected = " + isConnected() + ", mAddress = " + address.toString());
		}
		mChannel = SocketChannel.open(); // 创建Channel
		mChannel.connect(address); // 使Channel连接到服务器

		// 创建Selector，并将Channel注册到Selector上
		selector = Selector.open(); // 创建Selector
		mChannel.configureBlocking(false); // 将Channel设置成非阻塞
		mChannel.socket().setTcpNoDelay(true);
		mChannel.socket().setReceiveBufferSize(RECEIVE_BUFFER_SIZE);//TODO CHECK
		mKey = mChannel.register(selector, SelectionKey.OP_READ); // 注册Channel
		// register方法的第二个参数表示Channel可以进行的操作，共有READ，WRITE，ACCEPT和CONNECT四种操作
		// 在将Channel注册到Selector之前，必须保证是非阻塞的，否则将抛出IllegalBlockingModeException异常
		// 通过Selector轮询可以进行操作的Channel，并通过SelectionKey对Channel进行操作
		readBuffer = ByteBuffer.allocateDirect(DEFAULT_RECEIVE_BUFFER_SIZE);
		LogFeinno.e(TAG, "readBuffer.capacity()" + readBuffer.capacity());
		LogFeinno.e(TAG, "readBuffer.array().length" + readBuffer.array().length);
		mPriorityThreadFactory.newThread(read).start();
		if (LogFeinno.DEBUG) {
			LogFeinno.e(TAG, "connect.mIsConnected = " + isConnected() + ", mAddress = " + address.toString());
		}
	}

	@Override
	public void send(byte[] bytes) throws IOException {
		ByteBuffer bf = ByteBuffer.wrap(bytes);
		write(bf);
	}

	@Override
	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	Runnable read = new Runnable() {

		@Override
		public void run() {
			while (true) {
				int readyCount;
				try {
					readyCount = selector.select(DEFAULT_READ_TIMEOUT);
					if (readyCount == 0)
						continue; // 如果没有可以进行操作的Channel，则不执行任何操作
					Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
					while (iter.hasNext()) {
						SelectionKey key = iter.next();
						iter.remove();
						if (key.isValid()) {
							handleKey(key);
						}
					}
				} catch (Exception e) {
					if (LogFeinno.DEBUG) {
						LogFeinno.e(TAG, "==========read==========" + mChannel.socket().hashCode()
								+ " .Exception:" + e.getMessage());
					}
				}
			}
		}
	};

	@Override
	public synchronized void close() {
		if (mKey != null)
			mKey.cancel();
		try {
			if (mChannel != null) {
				if (LogFeinno.DEBUG) {
					LogFeinno.e(this.getClass().getName(), "==========close==========" + mChannel.socket().hashCode());
				}
				mChannel.close();
			}

		} catch (Exception e) {
			if (LogFeinno.DEBUG) {
				LogFeinno.e(TAG, "close.Exception : " + e.getMessage());
			}
		}
		// TODO INVOKE CLOSE EVENT
	}

	@Override
	public boolean isConnected() {
		if (null == mChannel)
			return false;
		return mChannel.isConnected();
		// return ((!mChannel.socket().isClosed()) &&
		// mChannel.socket().isConnected());
	}

	private void handleKey(SelectionKey key) throws IOException {
		if (key.isReadable()) {
			if (readEvent != null) {
//				readBuffer = ByteBuffer.allocateDirect(DEFAULT_RECEIVE_BUFFER_SIZE);
				SocketChannel channel = (SocketChannel) key.channel();
				int ret = -1;
				while ((ret = channel.read(readBuffer)) > 0) {
//					LogFeinno.e(TAG, "readBuffer.capacity()" + readBuffer.capacity());
//					LogFeinno.e(TAG, "readBuffer.array().length" + readBuffer.array().length);
					readBuffer.flip();
//					LogFeinno.e(TAG, "after flip readBuffer.capacity()" + readBuffer.capacity());
//					LogFeinno.e(TAG, "after flip readBuffer.array().length" + readBuffer.array().length);
					if (readBuffer.capacity() == readBuffer.limit()) {
						LogFeinno.e(TAG, "readBuffer.capacity()==readBuffer.limit()");
					}
					readEvent.onConnectionNioRead(readBuffer);
					readBuffer.clear();
				}
				LogFeinno.i(TAG, "ret = " + ret);
				if(ret == -1)
				{
					close();
				}

			}
		}
	}

	public synchronized void write(final ByteBuffer b) throws IOException {
		// TODO check channel is connected()
		if (!isConnected()) {
			return;
		}
		// keep writing until the the socket can't write any more, or the
		// data is exhausted.
		int before = b.remaining();
		mChannel.write(b);
		if (b.remaining() > 0) {
			// register for a write notification if a write fails
			mKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		} else {
			mKey.interestOps(SelectionKey.OP_READ);
		}
	}

	@Override
	public void registerReadEvent(IConnectionReadEvent event) {
		readEvent = event;
	}
}
