/*
 * 创建日期：2012-11-16
 */
package com.feinno.androidbase.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.feinno.androidbase.threadpool.PriorityThreadFactory;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名：飞信 - Android客户端<br>
 * 描述：<br>
 * @author zhangguojunwx
 * @version 1.0
 * @since JDK1.5
 */
public final class TimerTaskManager {
	private final Looper mLooper;
	private final Handler mHandler;
	private final Map<Integer, Runnable> mTaskPool;
	private final ReentrantReadWriteLock mLock;

	public TimerTaskManager(String tag) {
		HandlerThread handlerThread = new HandlerThread(tag, PriorityThreadFactory.THREAD_PRIORITY_DEFAULT_LESS);
		handlerThread.start();
		mLooper = handlerThread.getLooper();
		mHandler = new Handler(mLooper);
		mTaskPool = new LinkedHashMap<Integer, Runnable>();
		mLock = new ReentrantReadWriteLock();
	}

	public void addTask(int id, Runnable task, long delay) {
		mLock.writeLock().lock();
		try {
			mHandler.postDelayed(task, delay);
			mTaskPool.put(id, task);
		} finally {
			mLock.writeLock().unlock();
		}
	}

	public Runnable getTask(int id) {
		mLock.readLock().lock();
		try {
			return mTaskPool.get(id);
		} finally {
			mLock.readLock().unlock();
		}
	}

	public boolean containsTask(int id) {
		mLock.readLock().lock();
		try {
			return mTaskPool.containsKey(id);
		} finally {
			mLock.readLock().unlock();
		}
	}

	public void addTask(Runnable task, long delay) {
		addTask(task.hashCode(), task, delay);
	}

	public List<Runnable> getAllTasksByList() {
		mLock.readLock().lock();
		try {
			return new ArrayList<Runnable>(mTaskPool.values());
		} finally {
			mLock.readLock().unlock();
		}
	}

	public int getAllTasksSize() {
		mLock.readLock().lock();
		try {
			return mTaskPool.size();
		} finally {
			mLock.readLock().unlock();
		}
	}

	public Runnable cancelTask(int id) {
		mLock.writeLock().lock();
		try {
			Runnable task = mTaskPool.remove(id);
			if (task != null) {
				mHandler.removeCallbacks(task);
			}
			return task;
		} finally {
			mLock.writeLock().unlock();
		}
	}

	public void cancelTask(Runnable task) {
		cancelTask(task.hashCode());
	}

	public void cancelAllTask() {
		List<Runnable> collection = getAllTasksByList();
		for (Runnable timerTask : collection) {
			mHandler.removeCallbacks(timerTask);
		}
		collection.clear();
		mLock.writeLock().lock();
		try {
			mTaskPool.clear();
		} finally {
			mLock.writeLock().unlock();
		}
		mLooper.quit();
	}
}