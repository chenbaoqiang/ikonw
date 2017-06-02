package com.feinno.androidbase.system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 版权所有 新媒传信科技有限公司。保留所有权利。<br>
 * 作者：wangxiaohong on 2016/1/5 11:02
 * 项目名：和飞信 - Android客户端<br>
 * 描述：网络类型，网络连接帮助类
 *
 * @version 1.0
 * @since JDK1.7.0_51
 */
public class NetConnectUtils {
    /**
     * Cmwap网络是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnectedByCmwap(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getExtraInfo() != null && networkInfo.getExtraInfo().toLowerCase().contains("cmwap");
    }

    /**
     * Wifi网络是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnectedByWifi(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 网络是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 中国移动网络是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isCMCCConnected(Context context) {
        boolean isCMCC = false;
        NetworkInfo networkInfo =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            String _strSubTypeName = networkInfo.getSubtypeName();
            // TD-SCDMA   networkType is 17
            int networkType = networkInfo.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS://2.5G
                case TelephonyManager.NETWORK_TYPE_EDGE://2.75G
                case TelephonyManager.NETWORK_TYPE_HSPA://3.5G
                case TelephonyManager.NETWORK_TYPE_HSDPA://3.5G
                case TelephonyManager.NETWORK_TYPE_HSUPA://3.5G
                    isCMCC = true;
                    break;
                default: {
                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA"))//3G
                    {
                        isCMCC = true;
                    } else {
                        isCMCC = false;
                    }
                }
                break;
            }
        }

        return isCMCC;
    }


    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static String getNetState(Context context) {
        String netType = "unknown";
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (isNetworkConnectedByWifi(context)) {
            netType = "WIFI";
        } else if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int mType = info.getSubtype();
            switch (mType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    netType = "GPRS";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    netType = "UMTS";
                    break;
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    netType = "HSDPA";
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    netType = "EDGE";
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    netType = "CDMA";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    netType = "EVDO";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    netType = "EVDO";
                    break;
            }
        }
        return netType;
    }

    /**
     * 2G网络是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnectedBy2G(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return networkInfo != null && null != mTelephony
                && (networkInfo.getSubtype() == mTelephony.NETWORK_TYPE_GPRS || networkInfo.getSubtype() == mTelephony.NETWORK_TYPE_CDMA || networkInfo.getSubtype() == mTelephony.NETWORK_TYPE_EDGE);
    }

    /**
     * 是否开启飞行模式
     *
     * @param context
     * @return
     */
    public static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    /**
     * 判断手机是否是移动sim卡且没有开启飞行模式
     *
     * @param context
     * @return
     */
    public static boolean isSIMAvailable(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int mode = Settings.System.getInt(context.getContentResolver(),
                "airplane_mode_on", 0);
        if (mode == 1)
            return false;
        String operator = manager.getSimOperator();
        String imsi = SIMUtils.getInstance(context).getIMSI();
        // 当操作码与当前IMSI不一致的时候以IMSI为准
        if (!TextUtils.isEmpty(imsi)) {
            if (!imsi.startsWith(operator)) {
                operator = imsi.substring(0, 5);
            }
        } else {
            return false;
        }
        if (manager.getSimState() == 5) {
            if (operator.equals("46000") || operator.equals("46002")
                    || operator.equals("46007")) {
                // 中国移动
                return true;
            }
        }
        return false;
    }
}
