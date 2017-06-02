package com.feinno.androidbase.utils.log;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名：飞信 - Android客户端<br>
 * 描述：崩溃回调接口类；<br>
 * @author zhangguojunwx
 * @version 1.0
 * @since JDK1.5
 */
public interface OnCrash {
    /**
     * 准备终止操作
     * @param thread
     * @param ex
     */
    public void onPreTerminate(Thread thread,String errorinfo, Throwable ex);

    /**
     * 终止操作
     * @param thread
     * @param ex
     */
    public void onTerminate(Thread thread,String errorinfo, Throwable ex);
}
