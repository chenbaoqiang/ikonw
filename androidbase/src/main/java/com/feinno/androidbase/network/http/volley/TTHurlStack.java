package com.feinno.androidbase.network.http.volley;

import android.content.Context;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fangmin on 2016/8/17.
 */
public class TTHurlStack extends HurlStack {
    private Context mContext;

    public TTHurlStack(Context context) {
        this.mContext = context;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        if("https".equalsIgnoreCase(url.getProtocol())) {
            VolleyX509TrustManager.allowAllSSL();
        }
        return (HttpURLConnection) url.openConnection();
    }
}
