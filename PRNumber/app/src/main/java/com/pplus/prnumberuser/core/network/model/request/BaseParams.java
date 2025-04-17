package com.pplus.prnumberuser.core.network.model.request;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by J2N on 16. 6. 20..
 */
public abstract class BaseParams {

    protected static final String LOG_TAG = BaseParams.class.getSimpleName();

    private REQUEST_TYPE requestType;

    public enum REQUEST_TYPE {
        POST , GET
    }

    public BaseParams(){

    }

    public REQUEST_TYPE getRequestType(){

        return requestType;
    }

    public void setRequestType(REQUEST_TYPE requestType){

        this.requestType = requestType;
    }

    public RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

    /**
     * @return Params를 맵으로 변환하여 반환합니다.
     * */
    public Map<String, Object> getParamsToMap(){

        Field[] fields = this.getClass().getDeclaredFields();

        Map<String,Object> map = new HashMap<>();

        for(int i=0;i < fields.length ;i++){
            fields[i].setAccessible(true);

            try {

                if(fields[i].isSynthetic()){
                    continue;
                }

                // value == null 이면 map으로 반환하지않는다.
                if(fields[i].get(this) == null){
                    continue;
                }

                String fieldName = null;
                if(fields[i].getAnnotation(SerializedName.class) != null){
                    fieldName = fields[i].getAnnotation(SerializedName.class).value();
                }else{
                    fieldName = fields[i].getName();
                }

                map.put(fieldName  , fields[i].get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
