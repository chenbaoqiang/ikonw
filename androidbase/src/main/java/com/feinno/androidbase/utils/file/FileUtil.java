/*
 * 创建日期：2012-11-7
 */
package com.feinno.androidbase.utils.file;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.feinno.androidbase.utils.MD5Util;
import com.feinno.androidbase.utils.log.LogFeinno;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名：飞信 - Android客户端<br>
 * 描述：文件工具类<br>
 *
 * @version 1.0
 * @since JDK1.5
 */
public class FileUtil {
    private static final String TAG = "RF_FileUtil";
    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    /**
     * Returns a human-readable version of the file size, where the input
     * represents a specific number of bytes.
     *
     * @param size the number of bytes
     * @return a human-readable display value (includes units)
     */
    public static String formatSize(long size) {
        float ONE_KB = 1024F;
        float ONE_MB = ONE_KB * ONE_KB;
        float ONE_GB = ONE_KB * ONE_MB;
        String displaySize;
        DecimalFormat df = new DecimalFormat("0.00");
        if (size >= ONE_KB && size < ONE_MB) {
            displaySize = String.valueOf(df.format(size / ONE_KB)) + " KB";
        } else if (size >= ONE_MB && size < ONE_GB) {
            displaySize = String.valueOf(df.format(size / ONE_MB)) + " MB";
        } else if (size >= ONE_GB) {
            displaySize = String.valueOf(df.format(size / ONE_GB)) + " GB";
        } else {
            displaySize = String.valueOf(df.format(size)) + " B";
        }
        return displaySize;
    }

    public static String roughFormatSize(long size) {
        float ONE_KB = 1024F;
        float ONE_MB = ONE_KB * ONE_KB;
        float ONE_GB = ONE_KB * ONE_MB;
        String displaySize;
        DecimalFormat df = new DecimalFormat("0");
        if (size >= ONE_KB && size < ONE_MB) {
            displaySize = String.valueOf(df.format(size / ONE_KB)) + " KB";
        } else if (size >= ONE_MB && size < ONE_GB) {
            DecimalFormat df2 = new DecimalFormat("0.0");
            displaySize = String.valueOf(df2.format(size / ONE_MB)) + " MB";
        } else if (size >= ONE_GB) {
            displaySize = String.valueOf(df.format(size / ONE_GB)) + " G";
        } else {
            displaySize = String.valueOf(df.format(size)) + " Byte";
        }
        return displaySize;
    }

    /**
     * 递归删除文件目录
     *
     * @param dir 文件目录
     */
    public static void deleteFileDir(File dir) {
        if (LogFeinno.DEBUG) {
            LogFeinno.d(TAG, "deleteFileDir.dir = " + dir);
        }
        try {
            if (dir.exists() && dir.isDirectory()) {// 判断是文件还是目录
                if (dir.listFiles().length == 0) {// 若目录下没有文件则直接删除
                    dir.delete();
                } else {// 若有则把文件放进数组，并判断是否有下级目录
                    File delFile[] = dir.listFiles();
                    int len = dir.listFiles().length;
                    for (int j = 0; j < len; j++) {
                        if (delFile[j].isDirectory()) {
                            deleteFileDir(delFile[j]);// 递归调用deleteFileDir方法并取得子目录路径
                        } else {
                            boolean isDeltet = delFile[j].delete();// 删除文件
                            if (LogFeinno.DEBUG) {
                                LogFeinno.d(TAG, "deleteFileDir.delFile[" + j + "] = " + delFile[j] + ", isDeltet = " + isDeltet);
                            }
                        }
                    }
                    delFile = null;
                }
                deleteFileDir(dir);// 递归调用
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除单个文件
     *
     * @param file 文件目录
     */
    public static void deleteFile(File file) {
        if (LogFeinno.DEBUG) {
            LogFeinno.d(TAG, "deleteFile.file = " + file);
        }
        try {
            if (file != null && file.isFile() && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝文件
     *
     * @param sourceFile 源文件
     * @param destFile   目标文件
     * @return 是否拷贝成功
     */
    public static boolean copyFile(File sourceFile, File destFile) {
        boolean isCopyOk = false;
        byte[] buffer = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        // 如果此时没有文件夹目录就创建
        String canonicalPath = "";
        try {
            canonicalPath = destFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!destFile.exists()) {
            if (canonicalPath.lastIndexOf(File.separator) >= 0) {
                canonicalPath = canonicalPath.substring(0, canonicalPath.lastIndexOf(File.separator));
                File dir = new File(canonicalPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
        }

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFile), DEFAULT_BUFFER_SIZE);
            bos = new BufferedOutputStream(new FileOutputStream(destFile), DEFAULT_BUFFER_SIZE);
            buffer = new byte[DEFAULT_BUFFER_SIZE];
            int len = 0;
            while ((len = bis.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            isCopyOk = sourceFile != null && sourceFile.length() == destFile.length();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                    bos = null;
                }
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
                buffer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (LogFeinno.DEBUG) {
            LogFeinno.d(TAG, "copyFile.sourceFile = " + sourceFile + ", destFile = " + destFile + ", isCopyOk = " + isCopyOk);
        }
        return isCopyOk;
    }

    /**
     * 读取文件内容到字节数组
     *
     * @param file
     * @return
     */
    public static byte[] readFileToBytes(File file) {
        byte[] bytes = null;
        if (file.exists()) {
            byte[] buffer = null;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            ByteArrayOutputStream baos = null;
            try {
                bis = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
                baos = new ByteArrayOutputStream();
                bos = new BufferedOutputStream(baos, DEFAULT_BUFFER_SIZE);
                buffer = new byte[DEFAULT_BUFFER_SIZE];
                int len = 0;
                while ((len = bis.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
                    bos.write(buffer, 0, len);
                }
                bos.flush();
                bytes = baos.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bos != null) {
                        bos.close();
                        bos = null;
                    }
                    if (baos != null) {
                        baos.close();
                        baos = null;
                    }
                    if (bis != null) {
                        bis.close();
                        bis = null;
                    }
                    buffer = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (LogFeinno.DEBUG) {
            LogFeinno.d(TAG, "readFileToBytes.file = " + file + ", bytes.length = " + (bytes == null ? 0 : bytes.length));
        }
        return bytes;
    }

    public static byte[] getByetsFromFile(File file, long offset, int len) {
        byte[] bytes = null;
        FileInputStream is = null;
        try {
            if (file.exists() && offset >= 0 && len > 0 && offset < file.length()) {
                bytes = new byte[(int) len];
                try {
                    is = new FileInputStream(file);
                    long l = is.skip(offset);
                    int r = is.read(bytes, 0, len);
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                            is = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else
            {
                LogFeinno.e(TAG, " offset:" + offset + " len:" + len + " file.length():" + file.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static byte[] readFileToBytes(File file, long offset, long end) {
        byte[] bytes = null;
        try {
            if (file.exists() && offset >= 0 && end > offset && offset < file.length()) {
                RandomAccessFile raf = null;
                ByteArrayOutputStream bos = null;
                try {
                    raf = new RandomAccessFile(file, "r");
                    raf.seek(offset);
                    bos = new ByteArrayOutputStream();
                    int b = -1;
                    long count = offset;
                    while ((b = raf.read()) != -1 && count <= end) {
                        bos.write(b);
                        count++;
                    }
                    bos.flush();
                    bytes = bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (raf != null) {
                            raf.close();
                            raf = null;
                        }
                        if (bos != null) {
                            bos.close();
                            bos = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static byte[] fileToByteArray(Context context, String path) {
        InputStream is = null;
        byte[] data = null;
        try {
            File file = null;
            if (ContentResolver.SCHEME_CONTENT.equals(Uri.parse(path).getScheme())) {
                ContentResolver cr = context.getContentResolver();
                Uri imageUri = Uri.parse(path);
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = cr.query(imageUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                file = new File(cursor.getString(column_index));
            } else if (ContentResolver.SCHEME_FILE.equals(Uri.parse(path).getScheme())) {
                file = new File(Uri.parse(path).getPath());
            } else {
                file = new File(path);
            }
            is = new FileInputStream(file);
            data = new byte[is.available()];
            int i = 0;
            int temp = 0;
            while ((temp = is.read()) != -1) {
                data[i] = (byte) temp;
                i++;
            }
        } catch (Exception e) {
            LogFeinno.e(TAG, e.getMessage(), e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                LogFeinno.e(TAG, e.getMessage(), e);
            }
        }
        return data;
    }

    public static boolean writeBytesToFile(File file, byte[] bytes, long offset) {
        boolean isOk = false;
        try {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file.exists() && bytes != null && offset >= 0) {
                RandomAccessFile raf = null;
                try {
                    raf = new RandomAccessFile(file, "rw");
                    raf.seek(offset);
                    raf.write(bytes);
                    isOk = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (raf != null) {
                            raf.close();
                            raf = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOk;
    }

    /**
     * 读取文件内容到字符串
     *
     * @param file
     * @return
     */
    public static String readFileToString(File file) {
        return readFileToString(file, null);
    }

    /**
     * 读取文件内容到字符串
     *
     * @param file
     * @param encoding
     * @return
     */
    public static String readFileToString(File file, String encoding) {
        String result = null;
        try {
            if (file.exists()) {
                char[] buffer = null;
                BufferedReader br = null;
                InputStreamReader isr = null;
                BufferedWriter bw = null;
                StringWriter sw = new StringWriter();
                try {
                    isr = encoding == null ? new InputStreamReader(new FileInputStream(file)) : new InputStreamReader(
                            new FileInputStream(file), encoding);
                    br = new BufferedReader(isr);
                    bw = new BufferedWriter(sw);
                    buffer = new char[DEFAULT_BUFFER_SIZE];
                    int len = 0;
                    while ((len = br.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
                        bw.write(buffer, 0, len);
                    }
                    bw.flush();
                    result = sw.toString();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                            bw = null;
                        }
                        if (br != null) {
                            br.close();
                            br = null;
                        }
                        if (isr != null) {
                            isr.close();
                            isr = null;
                        }
                        if (sw != null) {
                            sw.close();
                            sw = null;
                        }
                        buffer = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (LogFeinno.DEBUG) {
                LogFeinno.d(TAG, "readFileToString.file = " + file + ", encoding = " + encoding + ", result = " + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写字符串到文件，文件父目录如果不存在，会自动创建
     *
     * @param file
     * @param content
     * @return
     */
    public static boolean writeStringToFile(File file, String content) {
        return writeStringToFile(file, content, false);
    }

    /**
     * 写字符串到文件，文件父目录如果不存在，会自动创建
     *
     * @param file
     * @param content
     * @param isAppend
     * @return
     */
    public static boolean writeStringToFile(File file, String content, boolean isAppend) {
        boolean isWriteOk = false;
        char[] buffer = null;
        int count = 0;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            if (!file.exists()) {
                createNewFileAndParentDir(file);
            }
            if (file.exists()) {
                br = new BufferedReader(new StringReader(content));
                bw = new BufferedWriter(new FileWriter(file, isAppend));
                buffer = new char[DEFAULT_BUFFER_SIZE];
                int len = 0;
                while ((len = br.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
                    bw.write(buffer, 0, len);
                    count += len;
                }
                bw.flush();
            }
            isWriteOk = content.length() == count;
        } catch (IOException e) {
            LogFeinno.e(TAG, "writeStringToFile Exception = ", e);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                    bw = null;
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                buffer = null;
            } catch (IOException e) {
                LogFeinno.e(TAG, "writeStringToFile IOException = ", e);
            }
        }
//        if (LogFeinno.DEBUG) {
//            LogFeinno.d(TAG, "writeStringToFile.file = " + file + ", content.length() = "
//                    + (content == null ? 0 : content.length() + ", isAppend = " + isAppend) + ", isWriteOk = " + isWriteOk);
//        }
        return isWriteOk;
    }

    /**
     * 写字节数组到文件，文件父目录如果不存在，会自动创建
     *
     * @param file
     * @param bytes
     * @return
     */
    public static boolean writeBytesToFile(File file, byte[] bytes) {
        return writeBytesToFile(file, bytes, false);
    }

    /**
     * 写字节数组到文件，文件父目录如果不存在，会自动创建
     *
     * @param file
     * @param bytes
     * @param isAppend
     * @return
     */
    public static boolean writeBytesToFile(File file, byte[] bytes, boolean isAppend) {
        boolean isWriteOk = false;
        byte[] buffer = null;
        int count = 0;
        ByteArrayInputStream bais = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if (!file.exists()) {
                createNewFileAndParentDir(file);
            }
            if (file.exists()) {
                bos = new BufferedOutputStream(new FileOutputStream(file, isAppend), DEFAULT_BUFFER_SIZE);
                bais = new ByteArrayInputStream(bytes);
                bis = new BufferedInputStream(bais, DEFAULT_BUFFER_SIZE);
                buffer = new byte[DEFAULT_BUFFER_SIZE];
                int len = 0;
                while ((len = bis.read(buffer, 0, DEFAULT_BUFFER_SIZE)) != -1) {
                    bos.write(buffer, 0, len);
                    count += len;
                }
                bos.flush();
            }
            isWriteOk = bytes.length == count;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                    bos = null;
                }
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
                if (bais != null) {
                    bais.close();
                    bais = null;
                }
                buffer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (LogFeinno.DEBUG) {
            LogFeinno.d(TAG, "writeByteArrayToFile.file = " + file + ", bytes.length = " + (bytes == null ? 0 : bytes.length)
                    + ", isAppend = " + isAppend + ", isWriteOk = " + isWriteOk);
        }
        return isWriteOk;
    }

    /**
     * 创建文件父目录
     *
     * @param file
     * @return
     */
    public static boolean createParentDir(File file) {
        boolean isMkdirs = true;
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (!dir.exists()) {
                isMkdirs = dir.mkdirs();
                if (LogFeinno.DEBUG) {
                    LogFeinno.d(TAG, "createParentDir.dir = " + dir + ", isMkdirs = " + isMkdirs);
                }
            }
        }
        return isMkdirs;
    }

    /**
     * 创建文件及其父目录
     *
     * @param file
     * @return
     */
    public static boolean createNewFileAndParentDir(File file) {
        boolean isCreateNewFileOk = true;
        try {
            isCreateNewFileOk = createParentDir(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 创建父目录失败，直接返回false，不再创建子文件
        if (isCreateNewFileOk) {
            if (!file.exists()) {
                try {
                    isCreateNewFileOk = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    isCreateNewFileOk = false;
                }
            }
        }
        if (LogFeinno.DEBUG) {
            LogFeinno.d(TAG, "createFileAndParentDir.file = " + file + ", isCreateNewFileOk = " + isCreateNewFileOk);
        }
        return isCreateNewFileOk;
    }

    /**
     * 根据文件名称获取文件的后缀字符串
     *
     * @param filename 文件的名称,可能带路径
     * @return 文件的后缀字符串
     */
    public static String getFileExtensionFromUrl(String filename) {
        if (!TextUtils.isEmpty(filename)) {
            int dotPos = filename.lastIndexOf('.');
            if (0 <= dotPos) {
                return filename.substring(dotPos + 1);
            }
        }
        return "";
    }

    public static boolean prepareDir(String filePath) {
        if (!filePath.endsWith(File.separator)) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists() || file.mkdirs()) {
            LogFeinno.d(TAG, "prepareDir_create folder:" + filePath + ",result:true");
            return true;
        } else {
            LogFeinno.d(TAG, "prepareDir_create folder:" + filePath + ",result:false");
            return false;
        }
    }

    /**
     * 将带file://的路径从/sdcard开始截取
     *
     * @param path
     * @return
     */
    public static String formatPath(String path) {
        if (path.indexOf("/sd") > -1) {
            return path.substring(path.indexOf("/sd"));
        }
        return path;
    }

    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    public static File getFile(Uri uri) {
        if (uri != null) {
            String filepath = uri.getPath();
            if (filepath != null) {
                return new File(filepath);
            }
        }
        return null;
    }

    public static File getFile(String curdir, String file) {
        String separator = "/";
        if (curdir.endsWith("/")) {
            separator = "";
        }
        File clickedFile = new File(curdir + separator + file);
        return clickedFile;
    }

    public static File contentUriToFile(Context context, Uri uri) {
        File file = null;
        if (uri != null) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = ((Activity) context).managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();

            String img_path = actualimagecursor.getString(actual_image_column_index);
            file = new File(img_path);
        }
        return file;
    }

    public static InputStream getFileInputStream(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static boolean deleteFile(String path) {
        boolean delete = false;
        try {
            File file = new File(path);
            if (file.exists()) {
                delete = file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return delete;
    }

    public static void deleteFolder(String path) {
        LogFeinno.d(TAG, "path = " + path);
        try {
            File file = new File(path);
            if (file.exists()) {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File f : files) {
                        if (f.isDirectory()) {
                            deleteFolder(f.getAbsolutePath());
                        } else {
                            boolean a = f.delete();
                            LogFeinno.d(TAG, "a = " + a);
                        }
                    }
                } else {
                    boolean b = file.delete();
                    LogFeinno.d(TAG, "b = " + b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getFile(File curdir, String file) {
        return getFile(curdir.getAbsolutePath(), file);
    }

    /**
     * 通过文件来获取路劲
     *
     * @param file
     * @return
     */
    public static File getPathWithoutFilename(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname = filepath.substring(0, filepath.length() - filename.length());
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }

    public static byte[] getByteArrayByFile(File file) {
        // modify by gaotong not The entire file into memory
        BufferedInputStream stream = null;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outsteam = null;
        try {
            FileInputStream in = new FileInputStream(file);
            stream = new BufferedInputStream(in);
            outsteam = new ByteArrayOutputStream();
            while (stream.read(buffer) != -1) {
                outsteam.write(buffer);
            }

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                    outsteam.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return outsteam.toByteArray();
    }

    /**
     * @param f    - 指定的目录
     * @param buff
     */
    public static void saveByteToFile(File f, byte[] buff) {
        FileOutputStream fOut = null;
        try {
            if (buff != null && buff.length != 0) {
                if (f.exists()) {
                    f.delete();
                }
                f.createNewFile();
                fOut = new FileOutputStream(f);
                fOut.write(buff);
                fOut.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveByteToPath(String path, ArrayList<byte[]> buffs) {
        FileOutputStream fOut = null;
        File f = new File(path);
        try {
            fOut = new FileOutputStream(f);
            for (int i = 0; i < buffs.size(); i++) {
                fOut.write(buffs.get(i));
            }
            fOut.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveByteToFile(File f, ArrayList<byte[]> buffs) {
        FileOutputStream fOut = null;
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            fOut = new FileOutputStream(f);
            for (int i = 0; i < buffs.size(); i++) {
                fOut.write(buffs.get(i));
            }
            fOut.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存图片到SDCard的默认路径下. 屏刷新图库
     *
     * @throws IOException
     */
    public static void saveByteToSDCard(Context context, File f, byte[] buff) throws IOException {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            fOut.write(buff);
            fOut.flush();
            refreshAlbum(context, f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存图片到data路径下
     */
    public static void saveByteToData(Context context, Bitmap bitmap, String fileName) throws IOException {
        try {
            File file = new File(fileName);

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void refreshAlbum(Context context, File imageFile) {
        if (context != null) {
            Uri localUri = Uri.fromFile(imageFile);
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            context.sendBroadcast(localIntent);
        }
    }

    public static void refreshAlbum(Context context, Uri localUri) {
        if (context != null) {
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            context.sendBroadcast(localIntent);
        }
    }

    public static boolean copyFile(InputStream inputStream, String destFilePath) {
        int bufferSize = 8 * 1024;
        OutputStream out = null;
        try {
            out = new FileOutputStream(destFilePath);
            byte[] buffer = new byte[bufferSize];
            int reacCount = 0;
            while ((reacCount = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, reacCount);
            }
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readFile(File file) {
        String data = "";
        try {
            FileInputStream stream = new FileInputStream(file);
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = stream.read()) != -1) {
                sb.append((char) c);
            }
            stream.close();
            data = sb.toString();
        }catch (Exception e) {
            LogFeinno.e(TAG, "读取文件异常 " + file.getAbsolutePath(), e);
        }
        return data;
    }

    public static String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; // 文件内容字符串
        // 打开文件
        File file = new File(path);
        // 如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            LogFeinno.e("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    // 分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                LogFeinno.e("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                LogFeinno.e("TestFile", e.getMessage());
            }
        }
        return content;
    }

    /**
     * 根据文件名称获取文件的后缀字符串
     *
     * @param filename 文件的名称,可能带路径
     * @return 文件的后缀字符串
     */
    public static String getFileExtension(String filename) {
        if (!TextUtils.isEmpty(filename)) {
            int dotPos = filename.lastIndexOf('.');
            if (0 <= dotPos) {
                return filename.substring(dotPos + 1);
            }
        }
        return "";
    }

    /**
     * 将文件从assets中拷贝到data目录下
     *
     * @param filename
     */
    public static void copyAssetsToData(Context context, String filename) {
        try {
            String path = context.getFilesDir().getAbsolutePath() + "/" + filename; // data/data目录
            File file = new File(path);
            if (file.exists()) {
//                new SharedPreferenceUtils(context,
//                        CommonVariables.SharedPreConfigName).setValue(
//                        CommonVariables.KeyCopySuccess, true);
//                return;
            }

            InputStream in = context.getAssets().open(filename); // 从assets目录下复制
            FileOutputStream out = new FileOutputStream(file);
            int length = -1;
            byte[] buf = new byte[1024];
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            out.flush();
            in.close();
            out.close();

//            new SharedPreferenceUtils(context,
//                    CommonVariables.SharedPreConfigName).setValue(
//                    CommonVariables.KeyCopySuccess, true);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 从assets下读取String文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getTxtFromAssets(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static long getFileSizes(File f) {
        try {
            long s = 0;
            if (f.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(f);

                s = fis.available();
            } else {
                f.createNewFile();
                System.out.println("文件不存在");
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String ReadTextFromDataDir(Context context, String fileName) {
        String str = null;
        try {
            // 获取文件
            FileInputStream fin = context.openFileInput(fileName);
            // 获得长度
            int length = fin.available();
            // 创建字节数组
            byte[] buffer = new byte[length];
            // 读取内容
            fin.read(buffer);
            // 获得编码格式
            // String type = codetype(buffer);
            // 按编码格式获得内容
            str = EncodingUtils.getString(buffer, "utf-8");

        } catch (Exception e) {
            // TODO: handle exception
        }
        return str;
    }

    public static void WriteTextToDataDir(Context context, String text, String fileName) {
        try {
            FileOutputStream outStream = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            OutputStreamWriter owrite = new OutputStreamWriter(outStream,
                    "UTF-8");
            BufferedWriter writer = new BufferedWriter(owrite);
            writer.write(text);
            writer.close();
            LogFeinno.d(TAG, "=======写入缓存成功=======");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = "";
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                ;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        } catch (Throwable e) {
            LogFeinno.e("RF_File", "getRealPathFromURI ", e);
        }
        return res;
    }

    public static String saveDownLoadFile(File tempFile, String md5, String path) {
        File file = new File(path);
        String strRename = "";
        if (!file.exists()) { //不存在同名文件
            tempFile.renameTo(new File(path));
        } else {
            String fileMd5 = MD5Util.getBigFileMD5(file);
            LogFeinno.i(TAG, "MD5Util.getBigFileMD5(file):" + MD5Util.getBigFileMD5(file));
            LogFeinno.i(TAG, "MD5Util.getMD5(file):" + MD5Util.getMD5(file));
            if (md5.equals(fileMd5)) {//存在同名文件并且是同一文件
                tempFile.delete();
            } else {//存在同名文件不是同一文件
                int i = 1;
                while (true) {
                    int pointIndex = path.lastIndexOf(".");
                    String filePath = path.substring(0, pointIndex) + "(" + i + ")" + path.substring(pointIndex);
                    File files = new File(filePath);
                    if (files.exists()) {//存在同名文件(1)
                        String filesMd5 = MD5Util.getBigFileMD5(files);
                        if (md5.equals(filesMd5)) {//存在同名文件(1)并且是同一文件
                            tempFile.delete();
                            break;
                        } else {//存在同名文件(1)不是同一文件
                            i += 1;
                        }
                    } else {//不存在同名文件(i)
                        tempFile.renameTo(new File(filePath));
                        strRename = "(" + i + ")";
                        break;
                    }
                }
            }
        }
        return strRename;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(java.io.File file) {

        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
}