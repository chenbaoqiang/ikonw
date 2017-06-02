package com.feinno.androidbase.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class ActivityMgrUtils {

    private static final String TAG = ActivityMgrUtils.class.getSimpleName();

    public static List<Activity> activityStack = Collections.synchronizedList(new ArrayList<Activity>());

    private static ActivityMgrUtils instance;

    private ActivityMgrUtils() {
    }

    /**
     * 单一实例
     */
    public synchronized static ActivityMgrUtils getInstance() {
        if (instance == null) {
            instance = new ActivityMgrUtils();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        synchronized (activityStack){
            activityStack.add(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        synchronized (activityStack){
            if (null !=activityStack && activityStack.size() > 0) {
                Activity activity = activityStack.get(activityStack.size()-1);
                return activity;
            }
            return null;
        }
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        synchronized (activityStack) {
            Activity activity = activityStack.get(activityStack.size() - 1);
            activityStack.remove(activity);
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        synchronized (activityStack) {
            if (activity != null && activityStack.contains(activity)) {
                activityStack.remove(activity);
                activity.finish();
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Class<?> cls1, Class<?> cls2) {
        synchronized (activityStack) {
            for (Iterator iterator = activityStack.iterator(); iterator.hasNext(); ) {
                Activity activity = (Activity) iterator.next();
                if (activity.getClass().equals(cls1) || activity.getClass().equals(cls2)) {
                    if (activity != null && activityStack.contains(activity)) {
                        iterator.remove();
                        activity.finish();
                    }
                }
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        synchronized (activityStack) {
            for (Iterator<Activity> it = activityStack.iterator(); it.hasNext(); ) {
                Activity activity = it.next();
                if (activity != null) {
                    if (activity.getClass().equals(cls) && activityStack.contains(activity)) {
                        it.remove();
                        activity.finish();
                    }

                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        synchronized (activityStack) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }


    /**
     * 结束除主界面和聊天界面所有的activity
     */

    public void finishMultiActivity(Class<?> cls, Class<?> cls1) {
        synchronized (activityStack) {
            if (activityStack != null && activityStack.size() > 0) {
                Iterator<Activity> iterator = activityStack.iterator();
                while (iterator != null && iterator.hasNext()) {
                    Activity activity = iterator.next();
                    if (!activity.getClass().equals(cls) && !activity.getClass().equals(cls1)) {
                        iterator.remove();
                        activity.finish();
                    }

                }
            }
        }
    }

    /**
     * 结束除主界面所有的activity
     */
    public void finishMultiActivity(Class<?> cls) {
        synchronized (activityStack) {
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator != null && iterator.hasNext()) {
                Activity activity = iterator.next();
                if (!activity.getClass().equals(cls)) {
                    iterator.remove();
                    activity.finish();
                }

            }
        }
    }


    public boolean findHasActivity(Class<?> cls) {
        synchronized (activityStack) {
            if (activityStack != null && activityStack.size() > 0) {
                for (Activity activity : activityStack) {
                    if (activity.getClass().equals(cls)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
            return false;
        }
    }

}
