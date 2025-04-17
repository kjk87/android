package com.pplus.networks.core;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.pplus.networks.service.AbstractService;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by J2N on 16. 6. 20.. 하나의 코어엔 하나의 서비스만 작동합니다.
 */
public class NetworkCore{

    private Retrofit retrofit;

    private AbstractService coreService;

    public NetworkCore(AbstractService service){

        coreService = service;

        initialize();
    }

    /**
     * Enables https connections
     */
    public void handleSSLHandshake(OkHttpClient.Builder builder){



        try {

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){

                public X509Certificate[] getAcceptedIssuers(){

                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType){

                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType){

                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

            builder.hostnameVerifier(new HostnameVerifier(){

                @Override
                public boolean verify(String arg0, SSLSession arg1){

                    return true;
                }
            });

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            Log.e("NetworkCore", e.toString());
        }
    }

    private void initialize(){

        coreService.initializeHeader(coreService.getHeaders());

        //        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //         set your desired log level
        //        logging.setLevel(Level.BODY);

        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

        httpBuilder.connectTimeout(20, TimeUnit.SECONDS);
        httpBuilder.writeTimeout(20, TimeUnit.SECONDS);
        httpBuilder.readTimeout(20, TimeUnit.SECONDS);

        httpBuilder.addInterceptor(new Interceptor(){

            @Override
            public Response intercept(Chain chain) throws IOException{

                Request original = chain.request();

                Request.Builder builder = original.newBuilder();
                builder.method(original.method(), original.body());

                /**
                 * 서버로 전송하는 기본헤더를 정의합니다.
                 * */
                if(coreService.isHeader()) {
                    Map<String, String> headers = coreService.getHeaders().getHeaderMap();
                    Iterator<String> keys = headers.keySet().iterator();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        builder.header(key, headers.get(key));
                    }
                }

                return chain.proceed(builder.build());
            }
        });

        //        httpBuilder.addInterceptor(new Interceptor(){
        //
        //            @Override
        //            public okhttp3.Response intercept(Chain chain) throws IOException{
        //
        //                Request original = chain.request();
        //
        //                Request.Builder builder = original.newBuilder();
        //                builder.method(original.method(), original.body());
        //
        //                /**
        //                 * 서버로 전송하는 기본헤더를 정의합니다.
        //                 * */
        //                if(coreService.isHeader()) {
        //                    Map<String, String> headers = coreService.getHeaders().getHeaderMap();
        //                    Iterator<String> keys = headers.keySet().iterator();
        //                    while (keys.hasNext()) {
        //                        String key = keys.next();
        //                        builder.header(key, headers.get(key));
        //                    }
        //                }
        //
        //                return chain.proceed(builder.build());
        //            }
        //        });

        OkHttpClient httpClient = null;

        /**
         * http client를 처리함
         * */
        if(this.coreService.getCustomHttpClient() == null) {

            // ssl 적용 url 일 경우
            if("https".equals(coreService.getEndPoint())) {

                ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256).build();

                try {

                    HostnameVerifier hostnameVerifier = new HostnameVerifier(){

                        @Override
                        public boolean verify(String s, SSLSession sslSession){

                            return true;
                        }
                    };

                    httpClient = httpBuilder.sslSocketFactory(new TLSSocketFactory(), new UnsafeTrustManager()).hostnameVerifier(hostnameVerifier).connectionSpecs(Collections.singletonList(spec)).build();
                } catch (KeyManagementException | NoSuchAlgorithmException e) {
                    Log.e("NetworkCore", e.toString());
                }

            } else {
                httpClient = httpBuilder.build();
            }
        } else {
            httpClient = coreService.getCustomHttpClient();
        }

        Converter.Factory factory = null;

        if(this.coreService.getCustomConverterFactory() == null) {
            TypeAdapterFactory typeAdapterFactory = new JsonTypeAdapterFactory();
            Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).setLenient().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").create();
            factory = GsonConverterFactory.create(gson);
        } else {
            factory = this.coreService.getCustomConverterFactory();
        }

        String baseUrl = this.coreService.getEndPoint();

        if(httpClient != null){
            retrofit = new Retrofit.Builder().baseUrl(baseUrl).client(httpClient).addConverterFactory(factory).validateEagerly(false).build();
        }

    }

    public Retrofit getRetrofit(){

        return retrofit;
    }

    public void updateHeaders(){

        coreService.initializeHeader(coreService.getHeaders());
    }

    public NetworkHeader getHeader(){

        return coreService.getHeaders();
    }

    /**
     * Get trust manager to permit all of certifications.
     *
     * @return TrustManager array with one X509TrustManager element
     */
    private static TrustManager[] getTrustAllCerts(){

        return new TrustManager[]{new X509TrustManager(){

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException{

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException{

            }

            public X509Certificate[] getAcceptedIssuers(){

                return new X509Certificate[]{};
            }
        }};
    }

}
