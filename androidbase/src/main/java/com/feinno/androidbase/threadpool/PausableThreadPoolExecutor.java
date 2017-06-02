/*
 * 创建日期：2012-11-26
 */
package com.feinno.androidbase.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名：飞信 - Android客户端<br>
 * 描述：具有暂停和恢复功能的线程池
 *  
 * @version 1.0
 * @since JDK1.5
 */
public class PausableThreadPoolExecutor extends ThreadPoolExecutor {
	private final ReentrantLock mPauseLock = new ReentrantLock();
	private final Condition mUnpaused = mPauseLock.newCondition();
	private boolean mIsPaused;
	private final Queue<Runnable> mRunningTasks = new ConcurrentLinkedQueue<Runnable>();

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 */
	public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param threadFactory
	 */
	public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		mRunningTasks.add(r);
		super.beforeExecute(t, r);
		mPauseLock.lock();
		try {
			while (mIsPaused) {
				mUnpaused.await();
			}
		} catch (InterruptedException ie) {
			t.interrupt();
		} finally {
			mPauseLock.unlock();
		}
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		mRunningTasks.remove(r);
		super.afterExecute(r, t);
	}

	@Override
	public void shutdown() {
		mRunningTasks.clear();
		super.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		List<Runnable> list = new ArrayList<Runnable>();
		list.addAll(mRunningTasks);
		mRunningTasks.clear();
		list.addAll(super.shutdownNow());
		return list;
	}

	public boolean isRunning(Runnable r) {
		return mRunningTasks.contains(r);
	}

	public boolean contains(Runnable r) {
		return mRunningTasks.contains(r) && getQueue().contains(r);
	}

	public void pause() {
		if (!isPaused()) {
			mPauseLock.lock();
			try {
				mIsPaused = true;
			} finally {
				mPauseLock.unlock();
			}
		}
	}

	public void resume() {
		if (isPaused()) {
			mPauseLock.lock();
			try {
				mIsPaused = false;
				mUnpaused.signalAll();
			} finally {
				mPauseLock.unlock();
			}
		}
	}

	public boolean isPaused() {
		mPauseLock.lock();
		try {
			return mIsPaused;
		} finally {
			mPauseLock.unlock();
		}
	}
}