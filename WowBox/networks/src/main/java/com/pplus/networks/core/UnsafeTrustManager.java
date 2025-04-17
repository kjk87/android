package com.pplus.networks.core;

import java.security.cert.CertificateException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by j2n on 2016. 11. 1..
 */

public class UnsafeTrustManager implements X509TrustManager{

    private static TrustManager[] _ref = null;

    public static TrustManager[] getInstance(){

        if(_ref == null) {
            _ref = new TrustManager[1];
            _ref[0] = new UnsafeTrustManager();
        }
        return _ref;
    }

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException{
        // does not throw exceptions if invalid cert
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType){
        // does not throw exceptions if invalid cert
    }

    // returns an array of trusted CAs
    // -> return your proxy's CA here if other methods fail to work
    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers(){

        java.security.cert.X509Certificate[] certs = new java.security.cert.X509Certificate[0];
        return certs;
    }


}
