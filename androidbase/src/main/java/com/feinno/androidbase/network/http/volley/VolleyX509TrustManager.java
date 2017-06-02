package com.feinno.androidbase.network.http.volley;

import android.content.Context;

import com.feinno.androidbase.utils.log.LogFeinno;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by fangmin on 2016/8/17.
 */
public class VolleyX509TrustManager implements X509TrustManager{
    private static final String KEY_STORE_CLIENT_PATH = "client.csr";//客户端要给服务器端认证的证书
    private static final String KEY_STORE_PASSWORD = "123456";// 客户端证书密码

    private static TrustManager[] trustManagers;
    private static final X509Certificate[] _AcceptedIssuers = new
            X509Certificate[] {};

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }

    public static void allowAllSSL(Context context) {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                // TODO Auto-generated method stub
                return true;
            }

        });
        SSLContext sslContext = getSSLContext(context);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    public static void allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                // TODO Auto-generated method stub
                return true;
            }

        });
        SSLContext context = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[] {
                    new VolleyX509TrustManager()
            };
        }
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
    }


    private static KeyStore buildKeyStore(Context context, int certRawResId)
            throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);

        Certificate cert = readCert(context, certRawResId);
        keyStore.setCertificateEntry("ca", cert);

        return keyStore;
    }

    private static Certificate readCert(Context context, int certResourceID) {
        InputStream inputStream = context.getResources().openRawResource(
                certResourceID);
        Certificate ca = null;

        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(inputStream);

        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return ca;
    }


    /**
     * 获取SSLContext
     *
     * @param context 上下文
     * @return SSLContext
     */
    private static SSLContext getSSLContext(Context context) {
        try {
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance("BKS");
            InputStream ksIn = context.getResources().getAssets().open(KEY_STORE_CLIENT_PATH);
            try {
                keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
            } catch (Exception e) {
                LogFeinno.e("RF_VolleyX509TrustManager", "getSSLContext KeyStore.load Exception = ", e);
            } finally {
                try {
                    ksIn.close();
                } catch (Exception e) {
                    LogFeinno.e("RF_VolleyX509TrustManager", "getSSLContext InputStream.close() Exception = ", e);
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
//            sslContext.init(null, getTrustManagerFactory(context).getTrustManagers(), null);
            return sslContext;
        } catch (Exception e) {
            LogFeinno.e("RF_VolleyX509TrustManager", "getSSLContext Exception = ", e);
        }
        return null;
    }

    private static TrustManagerFactory getTrustManagerFactory(Context context) {
        TrustManagerFactory tmf = null;
        InputStream caInput = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            caInput = context.getResources().getAssets().open(KEY_STORE_CLIENT_PATH);
            Certificate ca = cf.generateCertificate(caInput);
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        } catch (Exception e){
            LogFeinno.e("RF_VolleyX509TrustManager", "getTrustManagerFactory Exception = ", e);
        } finally {
            if(caInput != null) {
                try {
                    caInput.close();
                } catch (IOException e) {
                    LogFeinno.e("RF_VolleyX509TrustManager", "InputStream.close Exception = ", e);
                }
            }
        }
        return tmf;
    }

}
