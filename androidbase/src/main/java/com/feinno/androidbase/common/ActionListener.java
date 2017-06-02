/*
 * 创建日期：2012-11-17
 */
package com.feinno.androidbase.common;

import java.util.List;

import android.content.Intent;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名：飞信 - Android客户端<br>
 * 描述：<br>
 * 
 * @version 1.0
 * @param <T>
 * @since JDK1.5
 */
public interface ActionListener {

	List<String> getAction();

	void onHandleAction(Intent intent, Action<?> callback);
}
