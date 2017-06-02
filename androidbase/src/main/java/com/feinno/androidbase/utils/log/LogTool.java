package com.feinno.androidbase.utils.log;

import java.util.List;

/**
 * Created by ganshoucong on 2015/12/22.
 */
public class LogTool {

    public static int logThreadInfo(String tag, String msg) {
        long id = Thread.currentThread().getId();
        String name = Thread.currentThread().getName();
        String info = "current thread id is " + id + ";name is " + name;
        return LogFeinno.e(tag,msg +"--" +  info);
    }

    public static <T> String listToString(List<T> lists){
        StringBuffer s = new StringBuffer();
        if(lists == null){
            s.append(" is null");
            return s.toString();
        }
        for(T t : lists){
            s.append(t.toString());
            s.append(";");
        }
        return s.toString();
    }
}
