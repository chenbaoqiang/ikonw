package com.feinno.androidbase.network.http.volley;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.feinno.androidbase.utils.log.LogFeinno;

import java.util.Map;

public abstract class HttpListener<T> implements Response.ErrorListener {
    private final String TAG = "RF_HttpListener";
    protected String url;

    public void setUrl(String url){
        this.url = url;
    }

    abstract public void onResponse(int statusCode,Map<String, String> respHeaders, T response);

    abstract public void onFailedResposnse(int statusCode,String errormsg);

    @Override
    public void onErrorResponse(VolleyError error) {
        LogFeinno.e(TAG, "onErrorResponse url = " + url,error);

        int statuscode = HttpLocalErrorCode.Other;
        String msg = "";
        if(null != error.networkResponse){
            statuscode = error.networkResponse.statusCode;
            LogFeinno.e(TAG, "onErrorResponse error statusCode = " + error.networkResponse.statusCode + " ;url = " + url );
            if(statuscode == 302 || statuscode == 301){
                Map<String,String> responseHeaders = error.networkResponse.headers;
                if(null != responseHeaders){
                    String newUrl = responseHeaders.get("Location");
                    msg = newUrl;
                    LogFeinno.e(TAG, "onErrorResponse 302 new url = " + msg);
                }
            }else if(null != error.networkResponse.data) {
                msg = new String(error.networkResponse.data);
                LogFeinno.e(TAG, "onErrorResponse error data = " + msg);
            }
        }
        if(statuscode == HttpLocalErrorCode.Other){
            if(error instanceof TimeoutError){
                statuscode = HttpLocalErrorCode.TimeOut;
            }
            else if(error instanceof NoConnectionError){
                statuscode = HttpLocalErrorCode.NoConnection;
            }
        }
        onFailedResposnse(statuscode, msg);
    }

}
