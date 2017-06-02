/*
 * 创建日期：2012-11-29
 */
package com.feinno.androidbase.utils;

import com.feinno.androidbase.utils.log.LogFeinno;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名：飞信 - Android客户端<br>
 * 描述：MD5码工具类
 *
 * @version 1.0
 * @since JDK1.5
 */
public class MD5Util {
    private static String sTag = "RF_MD5Util";
    private static MessageDigest mMessageDigest;

    static {
        try {
            mMessageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            if (LogFeinno.DEBUG) {
                LogFeinno.e(sTag, "MessageDigest.init.Exception : " + e.getMessage());
            }
        }
    }

    public static String getBigFileMD5(File file) {
        // 缓冲区大小（这个可以抽出一个参数）
        LogFeinno.e(sTag, "getBigFileMD5 begin ");
        int bufferSize = 256 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 使用DigestInputStream
            fileInputStream = new FileInputStream(file);
            digestInputStream = new DigestInputStream(fileInputStream, mMessageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer = new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0) ;
            // 获取最终的MessageDigest
            mMessageDigest = digestInputStream.getMessageDigest();
            LogFeinno.e(sTag, "getBigFileMD5 end ");
            // 同样，把字节数组转换成字符串
            return TypeUtil.bytesToHexString(mMessageDigest.digest());

        } catch (Exception e) {
            LogFeinno.e(sTag, "getBigFileMD5 Exception : ", e);
            return null;
        } finally {
            try {
                digestInputStream.close();
            } catch (Exception e) {
            }
            try {
                fileInputStream.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取文件的MD5码
     *
     * @param file
     * @return
     */
    public static String getMD5(File file) {
        if (mMessageDigest != null) {
            FileInputStream fis = null;
            MappedByteBuffer byteBuffer = null;
            try {
                fis = new FileInputStream(file);
                FileChannel channel = fis.getChannel();
                byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                mMessageDigest.update(byteBuffer);
            } catch (FileNotFoundException e) {
                LogFeinno.e(sTag, "getFileMD5.Exception : " + e.getMessage());
            } catch (IOException e) {
                LogFeinno.e(sTag, "getFileMD5.Exception : " + e.getMessage());
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        LogFeinno.e(sTag, "getFileMD5.Exception : " + e.getMessage());
                    }
                }
                if (byteBuffer != null) {
                    byteBuffer.clear();
                }
            }
            return TypeUtil.bytesToHexString(mMessageDigest.digest());
        }
        return null;
    }

    /**
     * 字符串的MD5码
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        if (str != null) {
            return getMD5(str.getBytes());
        }
        return null;
    }

    /**
     * 获取字节数组的MD5码
     *
     * @param bytes
     * @return
     */
    public static String getMD5(byte[] bytes) {
        if (mMessageDigest != null && bytes != null) {
            mMessageDigest.update(bytes);
            return TypeUtil.bytesToHexString(mMessageDigest.digest());
        }
        return null;
    }

    /**
     * 获取字节数组的MD5码
     *
     * @param bytes
     * @return
     */
    public static byte[] getMD5Bytes(byte[] bytes) {
        if (mMessageDigest != null && bytes != null) {
            mMessageDigest.update(bytes);
            return mMessageDigest.digest();
        }
        return null;
    }

    /**
     * 校验MD5码
     *
     * @param str
     * @param md5Str
     * @return
     */
    public static boolean checkMD5(String str, String md5Str) {
        if (str != null && md5Str != null) {
            return getMD5(str).equals(md5Str);
        }
        return false;
    }
}