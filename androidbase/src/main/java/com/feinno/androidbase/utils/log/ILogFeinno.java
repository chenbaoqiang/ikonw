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
public interface ILogFeinno {

    int d(String tag, String msg);

    int v(String tag,String msg);

    int i(String tag, String msg);

    int w(String tag, String msg);

    int w(String tag, String msg, Throwable tr);

    int e(String tag, String msg);

    int e(String tag, String msg, Throwable tr);
}

