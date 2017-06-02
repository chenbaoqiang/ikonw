/*
 * 创建日期：2012-11-29
 */
package com.feinno.androidbase.utils;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名：飞信 - Android客户端<br>
 * 描述：byte转换工具
 *  
 * @version	1.0
 * @since JDK1.5
 */
public class TypeUtil {
	private static String mHexDigits = "0123456789ABCDEF";

	/**
	 * 转换short为byte
	 * 
	 * @param b
	 * @param s
	 *            需要转换的short
	 * @param index
	 */
	public static void putShort(byte[] bytes, short s, int index) {
		bytes[index + 1] = (byte) (s >> 8);
		bytes[index + 0] = (byte) (s >> 0);
	}

	/**
	 * 通过byte数组取到short
	 * 
	 * @param bytes
	 * @param index
	 *            第几位开始取
	 * @return
	 */
	public static short getShort(byte[] bytes, int index) {
		return (short) (((bytes[index + 1] << 8) | bytes[index + 0] & 0xff));
	}

	/**
	 * 转换int为byte数组
	 * 
	 * @param bytes
	 * @param i
	 * @param index
	 */
	public static void putInt(byte[] bytes, int i, int index) {
		bytes[index + 3] = (byte) (i >> 24);
		bytes[index + 2] = (byte) (i >> 16);
		bytes[index + 1] = (byte) (i >> 8);
		bytes[index + 0] = (byte) (i >> 0);
	}

	public static byte[] getBytes(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (0xff & i);
		bytes[1] = (byte) ((0xff00 & i) >> 8);
		bytes[2] = (byte) ((0xff0000 & i) >> 16);
		bytes[3] = (byte) ((0xff000000 & i) >> 24);
		return bytes;
	}

	/**
	 * 通过byte数组取到int
	 * 
	 * @param bytes
	 * @param index
	 *            第几位开始
	 * @return
	 */
	public static int getInt(byte[] bytes, int index) {
		return ((((bytes[index + 3] & 0xff) << 24)
				| ((bytes[index + 2] & 0xff) << 16)
				| ((bytes[index + 1] & 0xff) << 8) | ((bytes[index + 0] & 0xff) << 0)));
	}

	/**
	 * 转换long型为byte数组
	 * 
	 * @param bytes
	 * @param l
	 * @param index
	 */
	public static void putLong(byte[] bytes, long l, int index) {
		bytes[index + 7] = (byte) (l >> 56);
		bytes[index + 6] = (byte) (l >> 48);
		bytes[index + 5] = (byte) (l >> 40);
		bytes[index + 4] = (byte) (l >> 32);
		bytes[index + 3] = (byte) (l >> 24);
		bytes[index + 2] = (byte) (l >> 16);
		bytes[index + 1] = (byte) (l >> 8);
		bytes[index + 0] = (byte) (l >> 0);
	}

	/**
	 * 通过byte数组取到long
	 * 
	 * @param bytes
	 * @param index
	 * @return
	 */
	public static long getLong(byte[] bytes, int index) {
		return ((((long) bytes[index + 7] & 0xff) << 56)
				| (((long) bytes[index + 6] & 0xff) << 48)
				| (((long) bytes[index + 5] & 0xff) << 40)
				| (((long) bytes[index + 4] & 0xff) << 32)
				| (((long) bytes[index + 3] & 0xff) << 24)
				| (((long) bytes[index + 2] & 0xff) << 16)
				| (((long) bytes[index + 1] & 0xff) << 8) | (((long) bytes[index + 0] & 0xff) << 0));
	}

	/**
	 * 字符到字节转换
	 * 
	 * @param c
	 * @return
	 */
	public static void putChar(byte[] bytes, char c, int index) {
		int temp = c;
		// byte[] b = new byte[2];
		for (int i = 0; i < 2; i ++ ) {
			bytes[index + i] = new Integer(temp & 0xff).byteValue(); // 将最高位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
	}

	/**
	 * 字节到字符转换
	 * 
	 * @param bytes
	 * @return
	 */
	public static char getChar(byte[] bytes, int index) {
		int s = 0;
		if (bytes[index + 1] > 0) {
			s += bytes[index + 1];
		} else {
			s += 256 + bytes[index + 0];
		}
		s *= 256;
		if (bytes[index + 0] > 0) {
			s += bytes[index + 1];
		} else {
			s += 256 + bytes[index + 0];
		}
		char ch = (char) s;
		return ch;
	}

	/**
	 * float转换byte
	 * 
	 * @param bytes
	 * @param f
	 * @param index
	 */
	public static void putFloat(byte[] bytes, float f, int index) {
		// byte[] b = new byte[4];
		int l = Float.floatToIntBits(f);
		for (int i = 0; i < 4; i++) {
			bytes[index + i] = new Integer(l).byteValue();
			l = l >> 8;
		}
	}

	/**
	 * 通过byte数组取得float
	 * 
	 * @param bb
	 * @param index
	 * @return
	 */
	public static float getFloat(byte[] bytes, int index) {
		int l;
		l = bytes[index + 0];
		l &= 0xff;
		l |= ((long) bytes[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) bytes[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) bytes[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}

	/**
	 * double转换byte
	 * 
	 * @param bytes
	 * @param d
	 * @param index
	 */
	public static void putDouble(byte[] bytes, double d, int index) {
		// byte[] b = new byte[8];
		long l = Double.doubleToLongBits(d);
		for (int i = 0; i < 4; i++) {
			bytes[index + i] = new Long(l).byteValue();
			l = l >> 8;
		}
	}

	/**
	 * 通过byte数组取得float
	 * 
	 * @param bb
	 * @param index
	 * @return
	 */
	public static double getDouble(byte[] bytes, int index) {
		long l;
		l = bytes[0];
		l &= 0xff;
		l |= ((long) bytes[1] << 8);
		l &= 0xffff;
		l |= ((long) bytes[2] << 16);
		l &= 0xffffff;
		l |= ((long) bytes[3] << 24);
		l &= 0xffffffffl;
		l |= ((long) bytes[4] << 32);
		l &= 0xffffffffffl;
		l |= ((long) bytes[5] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) bytes[6] << 48);
		l &= 0xffffffffffffffl;
		l |= ((long) bytes[7] << 56);
		return Double.longBitsToDouble(l);
	}

	/**
	 * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * @param bytes byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] bytes) {
		if (bytes != null) {
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {
				int v = bytes[i] & 0xFF;
				String hv = Integer.toHexString(v);
				if (hv.length() < 2) {
					stringBuffer.append(0);
				}
				stringBuffer.append(hv);
			}
			return stringBuffer.toString();
		}
		return null;
	}

	/**
	 * Convert hex string to byte[]
	 * @param hexString the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString != null) {
			hexString = hexString.toUpperCase();
			int length = hexString.length() / 2;
			char[] hexChars = hexString.toCharArray();
			byte[] d = new byte[length];
			for (int i = 0; i < length; i++) {
				int pos = i * 2;
				d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
			}
			return d;
		}
		return null;
	}

	/**
	 * Convert char to byte
	 * @param c char
	 * @return byte
	 */
	public static byte charToByte(char c) {
		return (byte) mHexDigits.indexOf(c);
	}

	public static String stringToHexString(String string) {
		if (string != null) {
			byte[] b = string.getBytes();
			StringBuffer sb = new StringBuffer(b.length);
			String sTemp;
			for (int i = 0; i < b.length; i++) {
				sTemp = Integer.toHexString(0xFF & b[i]);
				if (sTemp.length() < 2) {
					sb.append(0);
				}
				sb.append(sTemp.toUpperCase());
			}
			return sb.toString();
		}
		return null;
	}
}