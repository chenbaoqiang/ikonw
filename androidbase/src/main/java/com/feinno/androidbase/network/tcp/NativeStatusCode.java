/*
 * 创建日期：2012-11-16
 */
package com.feinno.androidbase.network.tcp;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名：飞信 - Android客户端<br>
 * 描述：NativeStatusCode<br>
 *  
 * @version 1.0
 * @since JDK1.5
 */
public class NativeStatusCode {
	/**
	 * 本地返回码：网络响应超时
	 */
	public static final int NC_NETWORK_TIMEOUT = -100;
	/**
	 * 本地返回码：网络未连接
	 */
	public static final int NC_NETWORK_DISCONNECTED = -101;
	/**
	 * 本地返回码：Socket或者Http请求发送失败，包括IO异常、请求队列被取消的请求
	 */
	public static final int NC_REQUEST_SEND_FAILED = -102;
	/**
	 * 本地返回码：网络请求池已满
	 */
	public static final int NC_REQUEST_QUEUE_FULLED = -104;

	public static boolean isNativeStatusCode(int statusCode) {
		return NC_NETWORK_TIMEOUT == statusCode || NC_NETWORK_DISCONNECTED == statusCode || NC_REQUEST_SEND_FAILED == statusCode
				|| NC_REQUEST_QUEUE_FULLED == statusCode;
	}
}
