package com.feinno.androidbase.utils.log;

import android.os.Environment;
import android.util.Log;

import com.feinno.androidbase.threadpool.PriorityThreadFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ganshoucong on 2015/12/11.
 */
public final class LogFeinno  {
    public static final boolean DEBUG = true;
    private static boolean gIsDevMode = true;
    private static final String LEVEL_D = "D";
    private static final String LEVEL_I = "I";
    private static final String LEVEL_W = "W";
    private static final String LEVEL_E = "E";
    private static final ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(0, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("rongfly-log",
            android.os.Process.THREAD_PRIORITY_BACKGROUND), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if (r instanceof FutureTask<?>) {
                ((FutureTask<?>) r).cancel(true);
            }
        }
    });


    public static void setIsDevMode(boolean gIsDevMode) {
        LogFeinno.gIsDevMode = gIsDevMode;
    }

    /**
     * 打印debug级别的日志
     * @param tag Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int d(String tag, String msg) {
        if (msg == null) {
            msg = "null";
        }
//        if(gIsDevMode)
//            writeToFile(LEVEL_D, tag, msg, null);
        if(DEBUG){
            return Log.d(tag, msg);
        } else {
            return 0;
        }
    }

    public static int v(String tag,String msg) {
        return d(tag,msg);
    }

//    /**
//     * 打印debug级别的日志
//     * @param tag Used to identify the source of a log message. It usually identifies
//     * the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     * @param tr An exception to log
//     * @return The number of bytes written.
//     */
//    public static int d(String tag, String msg, Throwable tr) {
//        if (msg == null) {
//            msg = "null";
//        }
//        writeToFile(LEVEL_D, tag, msg, tr);
//        if(DEBUG){
//            return Log.d(tag, msg, tr);
//        } else {
//            return 0;
//        }
//    }

    /**
     * 打印info级别的日志
     * @param tag Used to identify the source of a log message. It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int i(String tag, String msg) {
        if (msg == null) {
            msg = "null";
        }
        writeToFile(LEVEL_I, tag, msg, null);
        if(DEBUG){
            return Log.i(tag, msg);
        } else {
            return 0;
        }
    }

//    /**
//     * 打印info级别的日志
//     * @param tag Used to identify the source of a log message. It usually identifies
//     * the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     * @param tr An exception to log
//     * @return The number of bytes written.
//     */
//    public static int i(String tag, String msg, Throwable tr) {
//        if (msg == null) {
//            msg = "null";
//        }
//        writeToFile(LEVEL_I, tag, msg, tr);
//        if(DEBUG){
//            return Log.d(tag, msg, tr);
//        } else {
//            return 0;
//        }
//    }



    public static int w(String tag, String msg) {
        if (msg == null) {
            msg = "null";
        }
        writeToFile(LEVEL_W, tag, msg, null);
        if(DEBUG){
            return Log.w(tag, msg);
        } else {
            return 0;
        }
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (msg == null) {
            msg = "null";
        }
        writeToFile(LEVEL_W, tag, msg, tr);
        if(DEBUG){
            return Log.w(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static int e(String tag, String msg) {
        if (msg == null) {
            msg = "null";
        }
        writeToFile(LEVEL_E, tag, msg, null);
        if(DEBUG){
            return Log.e(tag, msg);
        } else {
            return 0;
        }
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (msg == null) {
            msg = "null";
        }
        writeToFile(LEVEL_E, tag, msg, tr);
        if(DEBUG){
            return Log.e(tag, msg, tr);
        } else {
            return 0;
        }
    }

    public static void flush() {
        mExecutor.getQueue().clear();
    }

    public static void destroy() {
        if (!mExecutor.isShutdown()) {
            mExecutor.shutdown();
        }
    }


    private static void writeToFile(final String level, final String tag, final String msg, final Throwable tr) {
        mExecutor.execute(new FutureTask<Void>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(level).append(" ").append(new SimpleDateFormat(Format.LOG_MSG_DATE_FORMAT).format(new Date())).append(": ").append(android.os.Process.myPid()).append(" ")
                            .append(android.os.Process.myTid()).append(" ").append(tag).append(" ").append(msg);
                    if (tr != null) {
                        sb.append(System.getProperties().getProperty("line.separator")).append(Log.getStackTraceString(tr));
                    }
                    sb.append(System.getProperties().getProperty("line.separator"));
                    // FtConfig.getPublicDir(FtConfig.DIR_LOG)必须动态去获取值，否则从不登录到登录后，log还是写不到userId对应的目录下；
                    File rootFile = new File(Environment.getExternalStorageDirectory(),"FetionX/FetionX/logs");
                    File file = new File(rootFile, "log-" + new SimpleDateFormat(Format.TRACE_LOG_FORMAT).format(new Date())+".log");
                    String str = sb.toString();
                    writeStringToFile(file, str, true);
                    if(gIsDevMode && tag.startsWith(LogConfig.LogPreLogin)){
                        File loginfile = new File(rootFile, "Loginlog-" + new SimpleDateFormat(Format.TRACE_LOG_FORMAT).format(new Date())+".log");
                        writeStringToFile(loginfile, str, true);
                    }
//                    else if(gIsDevMode && tag.startsWith(LogConfig.LogPreSDK )){
//                        File sdklog = new File(rootFile, "SDKlog-" + new SimpleDateFormat(Format.TRACE_LOG_FORMAT).format(new Date())+".log");
//                        writeStringToFile(sdklog, str, true);
//                    }
                    else if(gIsDevMode && tag.startsWith(LogConfig.LogPreSession )){
//                        File sessionlog = new File(rootFile, "Sessionlog-" + new SimpleDateFormat(Format.TRACE_LOG_FORMAT).format(new Date())+".log");
//                        writeStringToFile(sessionlog, sb.toString(), true);
                    }else if(tag.startsWith(LogConfig.LogPreMsg) || (gIsDevMode && tag.startsWith(LogConfig.LogPreSession) )){
                        File msglog = new File(rootFile, "Msglog-" + new SimpleDateFormat(Format.TRACE_LOG_FORMAT).format(new Date())+".log");
                        writeStringToFile(msglog, str, true);
                    } else if(gIsDevMode && tag.startsWith(LogConfig.LogPreVoWifi)) {
                        File vowifilog = new File(rootFile, "VoWifi-" + new SimpleDateFormat(Format.TRACE_LOG_FORMAT).format(new Date())+".log");
                        writeStringToFile(vowifilog, str, true);
                    }
//                    else if(tag.startsWith("RF_Power")){
//                        File powerlog = new File(rootFile, "Powerlog-" + new SimpleDateFormat(Format.TRACE_LOG_FORMAT).format(new Date())+".log");
//                        writeStringToFile(powerlog, sb.toString(), true);
//                    }
                    sb.setLength(0);
                }
                return null;
            }
        }) {
        });
    }

    /**
     * 写字符串到文件，文件父目录如果不存在，会自动创建
     * @param file
     * @param content
     * @param isAppend
     * @return
     */
    private static boolean writeStringToFile(File file, String content, boolean isAppend) {
        boolean isWriteOk = false;
        int count = 0;
        try {
            if (!file.exists()) {
                File dir = file.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
            if (file.exists()) {
                FileWriter w = new FileWriter(file, isAppend);
                w.write(content);
                w.flush();
                w.close();
            }
            isWriteOk = content.length() == count;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isWriteOk;
    }

    public static class Format {
        public static final String YEAR_MOUTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";
        public static final String YEAR_MOUTH_DAY_HOUR_MINUTE_SECOND_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";
        public static final String CRASH_FILE_NAME_DATE_FORMAT = "yyyyMMdd-HHmmss-SSS";
        public static final String LOG_FILE_NAME_DATE_FORMAT = "yyyyMMdd-HH";
        public static final String LOG_MSG_DATE_FORMAT = "MM-dd HH:mm:ss.SSS";
        public static final String REGISTER_DATE_FORMAT = "yyyy/MM/dd";
        public static final String TRACE_LOG_FORMAT = "yyyyMMdd";
    }

    public static class AppOnCrash implements OnCrash {
        @Override
        public void onPreTerminate(Thread thread,String errorinfo, final Throwable ex) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File root = new File(Environment.getExternalStorageDirectory(),"FetionX/FetionX/crash");
                root.setReadable(true, false);
                root.setWritable(true, false);
                root.setExecutable(true, false);
                if (!root.exists()) {
                    root.mkdirs();
                }
                File file = new File(root, "crash-rongfly-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".log");
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String error = errorinfo + "\n" + sw.toString();
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                writeStringToFile(file, error, false);
            }
        }

        @Override
        public void onTerminate(Thread thread,String version, Throwable ex) {
            System.exit(0);
        }
    }
}

