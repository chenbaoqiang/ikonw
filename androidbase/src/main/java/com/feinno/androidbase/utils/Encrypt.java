package com.feinno.androidbase.utils;

import com.feinno.androidbase.utils.log.LogFeinno;

import org.apache.http.util.EncodingUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ganshoucong on 2015/12/21.
 */
public class Encrypt {
    private final String TAG = "Encrypt";
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String encrypt(int userId, String password, String domain, String encryptTime) {
        byte[] clientKey = SHA1(mergeArrays(TypeUtil.getBytes(userId), SHA1((domain + ":" + password).getBytes())));
        String nonce = createNonceUseRandom();
        if (LogFeinno.DEBUG) {
            LogFeinno.d(TAG, "encrypt.userId = " + userId + ", password = ..." + ", domain = " + domain + ", encryptTime = "
                    + encryptTime + ", nonce = " + nonce + ", clientKey = " + clientKey);
        }
        return AES(TypeUtil.bytesToHexString(clientKey).toUpperCase() + "%" + nonce + "%" + encryptTime, MD5Util.getMD5(clientKey).toUpperCase());
    }

    public String generatePasswd(final String ka, final String kb) {
        try {
            return TypeUtil.bytesToHexString(MD5(SHA1((ka + ":" + kb).getBytes()))).toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

    public static String aesDecrypt(final String kd, final String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(MD5(key.getBytes("UTF-8")), "AES"));
            byte[] origin = cipher.doFinal(TypeUtil.hexStringToBytes(kd));
//            LogFeinno.d("RF_Encrypt","new String(origin) is " + new String(origin));
//            LogFeinno.d("RF_Encrypt","new String(xx) is " + new String(TypeUtil.hexStringToBytes(new String(origin))));
            return new String(origin);
        } catch (Exception e) {
            LogFeinno.e("RF_Encrypt","aesDecrypt exception",e);
        }
        return "";
    }

    public String aesDecryptChunLei(final String kd, final String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            byte[] bx = MD5(key.getBytes("UTF-8"));
            SecretKeySpec k = new SecretKeySpec(bx, "AES");
            cipher.init(Cipher.DECRYPT_MODE, k);
            byte[] by = TypeUtil.hexStringToBytes(kd);
            byte[] origin = cipher.doFinal(by);
            return new String(origin);// String(hexStringToBytes(new String(origin)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String decryptKey(String kd, String kc, String kb, int userid) {

        try {
            byte[] sha1BC = SHA1(String.format("%s%s", kb, kc).getBytes());
            byte[] bUseId = String.valueOf(userid).getBytes();
            byte[] tmpArr = new byte[bUseId.length + sha1BC.length];
            System.arraycopy(bUseId, 0, tmpArr, 0, bUseId.length);
            System.arraycopy(sha1BC, 0, tmpArr, bUseId.length, sha1BC.length);
            String decrypt = aesDecrypt(kd, TypeUtil.bytesToHexString(tmpArr).toUpperCase());
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String SMSEncrypt(String smsPwd, String stime) {
        String rsp = "";

        try {
            String cnonce = createNonceUseRandom();
            String time = stime;

            if (time == null || time.equals("")) {
                Date date = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = df.format(date);
            }
            rsp = AES(smsPwd.toUpperCase() + "%" + cnonce + "%" + time, smsPwd.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsp;
    }

    private byte[] SHA1(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(bytes);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String toMd5(String s)
    {
        try
        {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            String result = encode(digester.digest(EncodingUtils.getAsciiBytes(s)));
            return result;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
    /** 16进制字符数组 */
    private static final char[] HEXADECIMAL = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /**
     * 将byte数组转成字符串
     *
     * @param binaryData
     *            代转byte数组
     * @return 返回字符串
     */
    private static String encode(byte[] binaryData)
    {
        int n = binaryData.length;
        char[] buffer = new char[n * 2];
        for (int i = 0; i < n; i++)
        {
            int low = (binaryData[i] & 0x0f);
            int high = ((binaryData[i] & 0xf0) >> 4);
            buffer[i * 2] = HEXADECIMAL[high];
            buffer[(i * 2) + 1] = HEXADECIMAL[low];
        }
        return new String(buffer);
    }

    private static byte[] MD5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String AES(String str, String aesKey) {
        byte[] result = null;
        try {
            Cipher out = Cipher.getInstance("AES");
            out.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(MD5(aesKey.getBytes("UTF-8")), "AES"));
            result = out.doFinal(str.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TypeUtil.bytesToHexString(result);
    }

    private byte[] mergeArrays(byte[] arrayI, byte[] arrayII) {
        byte[] result = new byte[arrayI.length + arrayII.length];
        System.arraycopy(arrayI, 0, result, 0, arrayI.length);
        System.arraycopy(arrayII, 0, result, arrayI.length, arrayII.length);
        return result;
    }

    private String createNonceUseRandom() {
        Random ra = new Random();
        int n1 = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        n1 = ra.nextInt();
        n2 = ra.nextInt();
        n3 = ra.nextInt();
        n4 = ra.nextInt();
        if (n1 >> 24 < 16) {
            n1 += 0x10000000;
        }
        if (n2 >> 24 < 16) {
            n2 += 0x10000000;
        }
        if (n3 >> 24 < 16) {
            n3 += 0x10000000;
        }
        if (n4 >> 24 < 16) {
            n4 += 0x10000000;
        }
        return String.format("%1$8X%2$8X%3$8X%4$8X", n1, n2, n3, n4);
    }
}
