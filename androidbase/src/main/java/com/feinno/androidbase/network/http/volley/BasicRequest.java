package com.feinno.androidbase.network.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.feinno.androidbase.utils.log.LogFeinno;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BasicRequest extends StringRequest {
    private final String tag = "RF_BasicRequest";
    private HttpListener<String> mListener;
    private HttpEntity entity;
    private int mStatuscode;
    private Map<String, String> mHeader = new HashMap<String, String>();
    private Map<String, String> mResponseHeaders;
    private String contentType;
    private final int READ_TIME_OUT_MIS = 20 * 1000;
    private final int RETRY_COUNT = 0;

    public BasicRequest(int method, String url, HttpEntity entity, String contentType, boolean isDeliver302, HttpListener<String> listener) {
        super(method, url, null, listener);
        listener.setUrl(url);
        this.mListener = listener;
        this.entity = entity;
        this.contentType = contentType;
        this.setShouldCache(false);
        this.setDeliver302(isDeliver302);
        setRetryPolicy(new DefaultRetryPolicy(READ_TIME_OUT_MIS, RETRY_COUNT, 1f));
    }

    public void addHeader(String key, String value) {
        mHeader.put(key, value);
    }

    public void setHeader(Map<String, String> header) {
        this.mHeader.putAll(header);
    }

    public Map<String, String> getResponseHeader() {
        return mResponseHeaders;
    }

    @Override
    protected void deliverResponse(String response) {
//    	LogFeinno.i(tag, this.getUrl() + "  ==== > mStatuscode = " + mStatuscode + ";response " + response);
        if (mListener != null) {
            mListener.onResponse(mStatuscode, mResponseHeaders, response);
        }

    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        mResponseHeaders = response.headers;
        mStatuscode = response.statusCode;
        //重写StringRequest的实现，避免304的时候data为空的空指针异常
        //不等于304的空指针异常不处理
        String parsed = "";
        if (mStatuscode == 304 && null == response.data) {

        } else {
            try {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                LogFeinno.i(tag,"response.headers:"+response.headers+"response.data:"+response.data+"HttpHeaderParser.parseCharset(response.headers):"+HttpHeaderParser.parseCharset(response.headers));
//                SystemUtils.showLogCompletion("response.headers:" + response.headers + "response.data:" + response.data + "HttpHeaderParser.parseCharset(response.headers):" + HttpHeaderParser.parseCharset(response.headers), 100);
            } catch (UnsupportedEncodingException e) {
                parsed = new String(response.data);
                LogFeinno.i(tag, "exception parseNetworkResponse parsed:" + parsed + "response.headers:" + response.headers + "response.data:" + response.data);
            }
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));

//        LogFeinno.e("RF_","parseNetworkResponse");
//        LogFeinno.e("RF_","parseNetworkResponse mResponseHeaders is null? " + mResponseHeaders);
//        if(null != mResponseHeaders){
//            for (String key : mResponseHeaders.keySet()) {
//                LogFeinno.e("RF_", "parseNetworkResponse key= " + key + " and value= " + mResponseHeaders.get(key));
//            }
//        }
//        String charset = HttpHeaderParser.parseCharset(response.headers);
//        String s = "{\"userInfo\":{\"mobileNo\":13810439707,\"userId\":200306038,\"sid\":0,\"nickName\":\"甘总\"},\"credential\":\"59c197d4eebfcebec3c38f2416abf160\",\"token\":\"MjAwMzA2MDM4OmMxOGY5ZTFkOWQyYmY5MzE4OWE4NTIwZmU1YWNlYjRh\\n\",\"respCode\":200,\"respDesc\":\"\",\"unitedTokens\":{\"token\":[]},\"clientLoginInfo\":{\"currentLoginTime\":\"Sun, 20 Mar 2016 09:43:31 GMT\",\"lastLoginIp\":\"10.0.0.1\",\"lastLoginPlace\":\"\",\"lastLoginTime\":\"Mon, 29 Jun 2015 08:16:36 GMT\",\"lastLogoffTime\":\"Mon, 29 Jun 2015 08:16:52 GMT\",\"loginPlace\":\"\",\"publicIp\":\"10.0.0.1\",\"passwordType\":\"NORMAL\",\"unknownFieldSet\":{},\"serializationFieldSet\":[0,1,1,1,1,1,1,1,1]},\"configs\":{\"configExList\":[],\"displayModInSms\":\"Front\",\"alv2Setwarn\":0,\"win8Push\":0,\"unknownFieldSet\":{},\"serializationFieldSet\":[0,0,1]},\"customConfig\":{\"version\":0,\"configValue\":\"\",\"unknownFieldSet\":{},\"serializationFieldSet\":[0,0,1]},\"userCapability\":{\"basicCaps\":0,\"contactCaps\":0,\"extendedCaps\":0,\"contactCapsMap\":{},\"unknownFieldSet\":{},\"serializationFieldSet\":[]},\"portraitCrc\":-1801255584,\"quota\":{\"maxBuddies\":0,\"maxGroupadminCount\":0,\"maxJoingroupCount\":0,\"sendSmsDayLimit\":0,\"sendSmsDayCount\":0,\"sendSmsMonthLimit\":0,\"sendSmsMonthCount\":0,\"freeDirectSmsDayLimit\":0,\"freeDirectSmsDayCount\":0,\"freeDirectSmsMonthLimit\":0,\"freeDirectSmsMonthCount\":0,\"unknownFieldSet\":{},\"serializationFieldSet\":[]}}";
//        try {
//            LogFeinno.e(tag, "s ==== >" + s.getBytes("UTF-8").length);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        LogFeinno.e(tag, "charset ==== >" + charset + ";response.data.length = " + response.data.length);
//        String parsed;
//        try {
//            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//        } catch (UnsupportedEncodingException var4) {
//            LogFeinno.e(tag, "UnsupportedEncodingException ==== >");
//            parsed = new String(response.data);
//        }
//        LogFeinno.e(tag, "parsed ==== >" + parsed);
//        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeader;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        HttpEntity httpEntity = entity;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpEntity.writeTo(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            LogFeinno.e(tag, e.getMessage());
        }
        return null;
    }

    @Override
    public String getBodyContentType() {
        return contentType;
    }

}
