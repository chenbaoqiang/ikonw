package com.feinno.androidbase.network.http.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.feinno.androidbase.utils.log.LogFeinno;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class ByteArrayRequest extends Request<ByteBuffer> {
    private final String Tag = "RF_ByteArrayRequest";
    private HttpListener<ByteBuffer> mListener;
    private HttpEntity entity;
    private int mStatuscode ;
    private Map<String, String> mHeader = new HashMap<String, String>();
    private Map<String, String> mResponseHeaders;
    private String contentType;
    private final int READ_TIME_OUT_MIS = 20 * 1000;
    private final int RETRY_COUNT = 0;

    public ByteArrayRequest(int method, String url, HttpEntity entity, String contentType, boolean isDeliver302,HttpListener<ByteBuffer> listener) {
        super(method, url, listener);
        listener.setUrl(url);
        this.mListener = listener;
        this.entity = entity;
        this.contentType = contentType;
        this.setShouldCache(false);
        this.setDeliver302(isDeliver302);
        setRetryPolicy(new DefaultRetryPolicy(READ_TIME_OUT_MIS, RETRY_COUNT, 1f));
    }
    public void addHeader(String key,String value){
        mHeader.put(key, value);
    }
    public void setHeader(Map<String, String> header){
        this.mHeader.putAll(header);
    }
    public Map<String, String> getResponseHeader(){
        return mResponseHeaders;
    }

    @Override
    protected void deliverResponse(ByteBuffer response) {
        LogFeinno.i(Tag, this.getUrl() + "==== > mStatuscode = " + mStatuscode + ";response " + response);
        if (mListener != null) {
            mListener.onResponse(mStatuscode,mResponseHeaders,response);
        }

    }

    @Override
    protected Response<ByteBuffer> parseNetworkResponse(NetworkResponse response) {
//        LogFeinno.e(Tag,"parseNetworkResponse");
        mResponseHeaders = response.headers;
        mStatuscode = response.statusCode;
        ByteBuffer parsed = null;
        try {
            parsed = ByteBuffer.wrap(response.data);
        } catch (Exception e) {
            LogFeinno.e(Tag,"parseNetworkResponse",e);
        }

        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
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
            LogFeinno.e(Tag, e.getMessage());
        }
        return null;
    }

    @Override
    public String getBodyContentType() {
        return contentType;
    }

}
