package com.feinno.androidbase.system;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.feinno.androidbase.utils.log.LogFeinno;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 关于android手机识别双卡双待：
 * 双卡双待识别实现起来比较繁琐,因为Android平台是一个多样型的平台，不同的手机获取ITelephony接口不同，用一种方法实现双卡双待管理是不可取的。
 * 为了更好的管理双卡双待的问题，我们新建一个双卡双待模块静态库，用来保存厂商的SDK的资料，如果手机厂商新出个双卡手机，还需要实时维护。
 */
public class SIMUtils {

	private Context context;
	private TelephonyManager telephonyManager;

	private static SIMUtils instance;

	private SIMUtils(Context context) {
		this.context = context;
		this.telephonyManager = getTelephonyManager();
	}


	public static final SIMUtils getInstance(Context context) {
		if (instance == null) {
			instance = new SIMUtils(context);
		}
		return instance;
	}

	private TelephonyManager getTelephonyManager() {
		return (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * check the SIM Card is avaliable
	 *
	 * @return
	 */
	public boolean SIMAvaliable() {
		return telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
	}

	/**
	 * 获取IMSI
	 *
	 * @return
	 */
	public String getIMSI() {
		String imsi = telephonyManager.getSubscriberId();
		if (imsi == null)
			imsi = "";
		if (!imsi.startsWith("4600")) {
			imsi = "";
		}
		return imsi;
	}

	/**
	 * 获取网络提供商
	 *
	 * @return
	 */
	public String getNetworkOperator() {
		return telephonyManager.getNetworkOperator();
	}

	/**
	 * 获取网络提供商名称
	 *
	 * @return
	 */
	public String getNetworkOperatorName() {
		return telephonyManager.getNetworkOperatorName();
	}

	/**
	 * Returns the ISO country code equivalent of the current registered
	 * operator's MCC
	 *
	 * @return
	 */
	public String getNetworkCountryIso() {
		return telephonyManager.getNetworkCountryIso();
	}

	/**
	 * 获取SIM卡提供商
	 *
	 * @return
	 */
	public String getSimOperator() {
		return telephonyManager.getSimOperator();
	}

	/**
	 * 获取SIM卡提供商名称
	 *
	 * @return
	 */
	public String getSimOperatorName() {
		return telephonyManager.getSimOperatorName();
	}

	/**
	 * <p>
	 * 获取设备号
	 * <p>
	 * Returns the unique device ID, for example, the IMEI for GSM and the MEID
	 * or ESN for CDMA phones. Return null if device ID is not available
	 *
	 * @return
	 */
	public String getDeviceId() {
		String deviceId = telephonyManager.getDeviceId();
		if (deviceId == null)
			deviceId = "";
		return deviceId;
	}
	/**
	 * 取得sim卡状态
	 * 0 正常 1GSM卡槽内有卡 2没插入WCDMA卡槽 3飞行模式 * * @param cct * @return
	 */
	public final static String getSimState() {
		StringBuilder sb = new StringBuilder();
		try {
			Class<?> mClass = Class.forName("android.telephony.MSimTelephonyManager");
			Method mGetSimState = mClass.getMethod("getSimState", new Class[]{int.class});
			Method mGetDefault = mClass.getMethod("getDefault");
			Object mPo = mGetDefault.invoke(mClass);
			int mSim1State = (Integer) mGetSimState.invoke(mPo, new Object[]{0});
			int mSim2State = (Integer) mGetSimState.invoke(mPo, new Object[]{1});
			// 正常状态 5 禁用状态 10 没插卡 1 飞行状态 0
			if (mSim1State == 0 || mSim2State == 0) {
				sb.append("飞行状态或者未插卡 ");
			} else if (mSim1State == 10) {
				sb.append("禁用sim卡1 ");
			} else if (mSim2State == 10) {
				sb.append("禁用sim卡2 ");
			} else if (mSim2State == 1) {
				sb.append("没插入sim卡2 ");
			} else if (mSim1State == 1) {
				sb.append("没插入sim卡1 ");
			}
			if (mSim2State == 5) {
				sb.append("插入了sim卡2 ");
			}
			if (mSim1State == 5) {
				sb.append("插入了sim卡1 ");
			}


		} catch (Exception e) {
		}
		return sb.toString();
	}

	/**
	 * 判断是否是双卡手机
	 * @return
	 */
	public boolean isDoubleSIM() {
		boolean status1 = getSimState(0) == TelephonyManager.SIM_STATE_READY;
		boolean status2 = getSimState(1) == TelephonyManager.SIM_STATE_READY;
		if(status1 && status2) {
			return  status1 && status2;
		} else {
			boolean isMtkDouble = isMtkDoubleSim();
			boolean isQualDouble = isQualcommDoubleSim();
			if(isMtkDouble) {
				return isMtkDouble;
			} else if(isQualDouble) {
				return isQualDouble;
			} else {
				return false;
			}
		}
	}

	/**
	 * 获取SIM卡状态
	 * @param slotID   0（卡1）和1（卡2）
	 * @return SIM卡状态 ：
	 * TelephonyManager.SIM_STATE_READY
	   TelephonyManager.IM_STATE_ABSENT
	   TelephonyManager.SIM_STATE_NETWORK_LOCKED
	   TelephonyManager.SIM_STATE_PIN_REQUIRED
	   TelephonyManager.SIM_STATE_UNKNOWN
	 */
	private int getSimState(int slotID) {
		int status = 0;
		try {
			TelephonyManager mTelephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Class<TelephonyManager> clz = (Class<TelephonyManager>) mTelephonyManager.getClass();
			Method mtd = clz.getMethod("getSimState", int.class);
			mtd.setAccessible(true);
			status = (Integer) mtd.invoke(mTelephonyManager, slotID);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return status;
	}

	private boolean isMtkDoubleSim() {
		boolean isDouble = false;
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			Class c = Class.forName("com.android.internal.telephony.Phone");
			Field fields1 = c.getField("GEMINI_SIM_1");
			fields1.setAccessible(true);
			int simId_1 = (Integer) fields1.get(null);
			Field fields2 = c.getField("GEMINI_SIM_2");
			fields2.setAccessible(true);
			int simId_2 = (Integer) fields2.get(null);
			Method m = TelephonyManager.class.getDeclaredMethod("getSubscriberIdGemini", int.class);
			String imsi_1 = (String) m.invoke(tm, simId_1);
			String imsi_2 = (String) m.invoke(tm, simId_2);

			Method m1 = TelephonyManager.class.getDeclaredMethod("getDeviceIdGemini", int.class);
			String imei_1 = (String) m1.invoke(tm, simId_1);
			String imei_2 = (String) m1.invoke(tm, simId_2);

			Method mx = TelephonyManager.class.getDeclaredMethod("getPhoneTypeGemini", int.class);
			int phoneType_1 = (Integer) mx.invoke(tm, simId_1);
			int phoneType_2 = (Integer) mx.invoke(tm, simId_2);
			if(!TextUtils.isEmpty(imsi_1) && !TextUtils.isEmpty(imsi_2)) {
				isDouble = true;
			}
		} catch (Exception e) {
			isDouble = false;
		}
		return isDouble;
	}

	private boolean isQualcommDoubleSim() {
		boolean isDouble = false;
		int simId_1 = 0;
		int simId_2 = 1;
		try {
			Class cx = Class.forName("android.telephony.MSimTelephonyManager");
			Object obj = context.getSystemService("phone_msim");

			Method md = cx.getMethod("getDeviceId", int.class);
			Method ms = cx.getMethod("getSubscriberId", int.class);

			String imei_1 = (String) md.invoke(obj, simId_1);
			String imei_2 = (String) md.invoke(obj, simId_2);
			String imsi_1 =(String) ms.invoke(obj, simId_1);
			String imsi_2 =(String) ms.invoke(obj, simId_2);
			if(!TextUtils.isEmpty(imsi_1) && !TextUtils.isEmpty(imsi_2)) {
				isDouble = true;
			}
		} catch (Exception e) {
			isDouble = false;
		}
		return isDouble;
	}

	/**
	 * 根据SimId获取id
	 * @param simId  0:卡槽1   1:卡槽2
	 * @return
	 */
	public int getIdInDBBySimId(int simId) {
		if(Build.VERSION.SDK_INT > 20) {
			Uri uri = Uri.parse("content://telephony/siminfo");
			Cursor cursor = null;
			ContentResolver resolver = context.getContentResolver();
			try {
				cursor = resolver.query(uri, new String[]{"_id", "sim_id"}, "sim_id = ?",
						new String[]{String.valueOf(simId)}, null);
				if(cursor != null && cursor.moveToFirst()) {
					return cursor.getInt(cursor.getColumnIndex("_id"));
				}
			} catch (Exception e) {
			} finally {
				if (null != cursor)
					cursor.close();
			}
		}
		return -1;
	}

	/**
	 *
	 * @param simId   0:卡槽1   1:卡槽2
	 * @param destinationAddress
	 * @param scAddress
	 * @param destinationPort
	 * @param data
	 * @param sentIntent
	 * @param deliveryIntent
	 */
	public void sendDataMessage(int simId, String destinationAddress, String scAddress, short destinationPort,
								byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
		try {
//			int subId = getIdInDBBySimId(simId);
			SmsManager smsManager = SmsManager.getDefault();
			Class smClass = SmsManager.class;
			//通过反射查到了SmsManager有个叫做mSubId的属性
			Field field = smClass.getDeclaredField("mSubId");
			field.setAccessible(true);
			SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
			int sendId = subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(simId).getSubscriptionId();
			field.set(smsManager, sendId);
			smsManager.sendDataMessage(destinationAddress, scAddress, destinationPort, data, sentIntent, deliveryIntent);
		} catch (Exception e) {
		}
	}

//	/**
//	* @param simId   0:卡槽1   1:卡槽2
//	* @param destinationAddress
//	* @param scAddress
//	* @param destinationPort
//	* @param data
//	* @param sentIntent
//	* @param deliveryIntent
//	*/
//	public void sendDataMessage(int simId, String destinationAddress, String scAddress, short destinationPort,
//								byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
//		try {
//			int subId = getIdInDBBySimId(simId);
//			LogFeinno.i("zfm", "subId = " + subId);
//			SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(subId);
//			smsManager.sendDataMessage(destinationAddress, scAddress, destinationPort, data, sentIntent, deliveryIntent);
//		} catch (Exception e) {
//			LogFeinno.e("zfm", "sendTextSms Exception:", e);
//		}
//	}


}
