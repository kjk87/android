package com.pplus.prnumberuser.core.network.upload;

import android.os.Handler;
import android.os.Looper;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberuser.PRNumberApplication;
import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder;
import com.pplus.prnumberuser.apps.common.builder.data.AlertData;
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberuser.core.network.model.request.BaseParams;
import com.pplus.prnumberuser.core.network.model.request.params.ParamsAttachment;
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
import com.pplus.prnumberuser.core.util.DebugConfig;
import com.pplus.prnumberuser.core.util.PplusCommonUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by j2n on 2016. 9. 30..
 */

public abstract class AbstractUpload<T extends Object>{

    public final String LOG_TAG = getClass().getSimpleName();

    private OkHttpClient okHttpClient;

    private Map<String, Call> callMap;

    private LinkedBlockingQueue<Call> callQueue;

    private ThreadPoolExecutor threadPool;

    private PplusUploadListener<T> pplusCallback;

    /* 업로드 주소 */
    abstract String getUploadUrl();

    /* 멀티 쓰레드 사용여부*/
    abstract boolean isMultiThreadEnable();

    /**
     * new TypeToken<ResultResponse<T>>() {}.getType();
     */
    abstract TypeToken<NewResultResponse<T>> getResultType();

    protected AbstractUpload(PplusUploadListener<T> pplusCallback){

        //        this.okHttpClient

        OkHttpClient.Builder clientbuilder = new OkHttpClient.Builder();
        clientbuilder.connectTimeout(30, TimeUnit.SECONDS);
        clientbuilder.writeTimeout(180, TimeUnit.SECONDS);
        clientbuilder.readTimeout(30, TimeUnit.SECONDS);
        clientbuilder.addNetworkInterceptor(new Interceptor(){

            @Override
            public Response intercept(Chain chain) throws IOException{

                Request original = chain.request();

                Request.Builder builder = original.newBuilder();
                builder.method(original.method(), original.body());

                if(LoginInfoManager.getInstance().isMember()) {
                    LogUtil.e(LOG_TAG, "add session : {}", LoginInfoManager.getInstance().getUser().getSessionKey());
                    builder.addHeader("sessionKey", LoginInfoManager.getInstance().getUser().getSessionKey());
                }

                return chain.proceed(builder.build());
            }
        });

        clientbuilder.retryOnConnectionFailure(true);

        this.okHttpClient = clientbuilder.build();

        this.pplusCallback = pplusCallback;

        this.callMap = new HashMap<>();

        this.callQueue = new LinkedBlockingQueue<>();

        //        if(isMultiThreadEnable()) {
        //            this.maxThreadCount = isMultiThreadEnable() ? 4 : 1;
        //        }

        //        2, 4, 60, TimeUnit.SECONDS
        //        this.threadPool = new ThreadPoolExecutor(Math.max(maxThreadCount / 2, 2), maxThreadCount, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        int maxThreadCount = 1;

        if(isMultiThreadEnable()) {
            maxThreadCount = getNumCores();
        }


        this.threadPool = new ThreadPoolExecutor(maxThreadCount, maxThreadCount, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }


    public void request(String tag, BaseParams baseParams){

        run(new UploadRunnable(tag, baseParams));
    }

    /**
     * 요청중인 콜에대해 취소합니다.
     */
    public void stop(PplusUploadCancelListener pplusUploadCancelListener){

        Iterator<String> callIterator = callMap.keySet().iterator();

        Set<String> stringSet = new HashSet<>();

        while (callIterator.hasNext()) {
            String tag = callIterator.next();

            if(callMap.containsKey(tag)) {
                callMap.get(tag).cancel();
                callMap.remove(tag);

                stringSet.add(tag);
            }
        }

        pplusUploadCancelListener.onCancel(stringSet);
    }

    private void run(Runnable runnable){

        threadPool.execute(runnable);
    }

    private class UploadRunnable implements Runnable{

        private String tag;
        private BaseParams baseParams;

        public UploadRunnable(String tag, BaseParams baseParams){

            this.tag = tag;
            this.baseParams = baseParams;
        }

        @Override
        public void run(){


            //            synchronized(callMap) {
            try {

                if(baseParams instanceof ParamsAttachment) {
                    synchronized(baseParams) {
                        ((ParamsAttachment) baseParams).buildFile();
                    }
                }

                Call call = okHttpClient.newCall(getRequest(baseParams.getParamsToMap()));

                callMap.put(tag, call);

                Response response = call.execute();

                if(DebugConfig.isDebugMode()) {

                    try {
                        LogUtil.e(LOG_TAG, "Successful = {} ", response.isSuccessful());

                        LogUtil.e(LOG_TAG, "request Url = {} ", response.request().url().toString());

                        LogUtil.e(LOG_TAG, "request header = {} ", response.request().header("sessionKey"));

                        final RequestBody copy = response.request().body();

                        if(copy != null) {
                            final Buffer buffer = new Buffer();
                            copy.writeTo(buffer);
                            LogUtil.e(LOG_TAG, "request body = {} ", buffer.readUtf8());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(!response.isSuccessful()) {

                    NewResultResponse resultResponse = new NewResultResponse();
                    resultResponse.setResultCode(500);

                    onFailure(tag, resultResponse);
                } else {

                    String responseResult = response.body().string();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.serializeNulls().disableHtmlEscaping().create();

                    Type collectionType = getResultType().getType();

                    NewResultResponse<T> resultResponse = gson.fromJson(responseResult, collectionType);

                    if(resultResponse.getResultCode() == 200) {
//                        int width = ((ParamsAttachment) baseParams).getWidth();
//                        int height = ((ParamsAttachment) baseParams).getHeight();
//                        ((Attachment) resultResponse.getData()).setWidth(width);
//                        ((Attachment) resultResponse.getData()).setHeight(height);
                        onResult(tag, resultResponse);
                    } else {
                        onFailure(tag, resultResponse);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();

                NewResultResponse resultResponse = new NewResultResponse();
                resultResponse.setResultCode(500);
                onFailure(tag, resultResponse);

            } finally {
                if(callMap.containsKey(tag)) callMap.remove(tag);

            }
            //            }
        }

        // 성공 처리
        void onResult(final String tag, final NewResultResponse<T> resultResponse){

            new Handler(Looper.getMainLooper()).post(new Runnable(){

                @Override
                public void run(){

                    pplusCallback.onResult(tag, resultResponse);
                }
            });
        }

        // 실패 처리
        void onFailure(final String tag, final NewResultResponse resultResponse){

            new Handler(Looper.getMainLooper()).post(new Runnable(){

                @Override
                public void run(){

                    pplusCallback.onFailure(tag, resultResponse);
                }
            });
        }
    }

    private Request getRequest(Map<String, Object> value){

        RequestBody requestBody = null;

        okhttp3.MultipartBody.Builder builder = new okhttp3.MultipartBody.Builder().setType(okhttp3.MultipartBody.FORM);

        Iterator<String> keys = value.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();

            Object object = value.get(key);

            if(object != null) {

                if(object instanceof File) {

                    final File file = (File) object;

                    //                    okhttp3.RequestBody fileBody = okhttp3.RequestBody.create(okhttp3.MediaType.parse(getMimeType(file)), file);

                    RequestBody fileBody = new RequestBody(){

                        @Override
                        public MediaType contentType(){

                            return MediaType.parse(getMimeType(file));
                        }

                        @Override
                        public long contentLength(){

                            return file.length();
                        }

                        @Override
                        public void writeTo(BufferedSink sink) throws IOException{

                            Source source = null;
                            try {
                                source = Okio.source(file);
                                sink.writeAll(source);
                                LogUtil.e(LOG_TAG, "file upload... source = {}", source);
                            } finally {
                                Util.closeQuietly(source);
                            }
                        }
                    };

                    builder.addFormDataPart(key, file.getName(), fileBody);

                } else if(object instanceof String) {
                    builder.addFormDataPart(key, (String) value.get(key));
                } else if(object instanceof Enum) {
                    builder.addFormDataPart(key, ((Enum) value.get(key)).name());
                } else if(object instanceof Long || object instanceof Integer || object instanceof Float) {
                    builder.addFormDataPart(key, String.valueOf(value.get(key)));
                }
            }
        }

        requestBody = builder.build();

        return new Request.Builder().url(getUploadUrl()).post(requestBody).build();
    }


    public String getMimeType(File file){

        return getMimeType(file.getPath());
    }

    public String getMimeType(String url){

        String type = null;

        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if(extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        if(StringUtils.isEmpty(type)) {
            type = "*/*";
        }

        return type;
    }

    public void checkSelfNetwork(){

        if(!PplusCommonUtil.Companion.hasNetworkConnection()) {
            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(PRNumberApplication.getContext().getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            builder.addContents(new AlertData.MessageData(PRNumberApplication.getContext().getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
            builder.addContents(new AlertData.MessageData(PRNumberApplication.getContext().getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.setLeftText(PRNumberApplication.getContext().getString(R.string.word_confirm));
            builder.builder().show(PRNumberApplication.getContext(), true);
            return;
        }
    }

    private int getNumCores(){

        //Private Class to display only CPU devices in the directory listing

        class CpuFilter implements FileFilter{

            @Override

            public boolean accept(File pathname){

                //Check if filename is "cpu", followed by a single digit number

                if(Pattern.matches("cpu[0-9]", pathname.getName())) {

                    return true;

                }

                return false;

            }

        }


        try {

            //Get directory containing CPU info

            File dir = new File("/sys/devices/system/cpu/");

            //Filter to only list the devices we care about

            File[] files = dir.listFiles(new CpuFilter());

            //Return the number of cores (virtual CPU devices)

            return files.length;

        } catch (Exception e) {

            return 1;

        }

    }
}
