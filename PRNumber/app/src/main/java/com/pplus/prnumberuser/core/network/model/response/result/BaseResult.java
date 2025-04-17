package com.pplus.prnumberuser.core.network.model.response.result;//package com.pplus.prnumberuser.core.network.model.response.result;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//import android.util.Log;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by j2n on 2016. 7. 25..
// */
//public class BaseResult implements Parcelable{
//
//    public BaseResult(){
//
//    }
//
//    /**
//     * 객체 직렬화를 위해 선언
//     */
//    public final static Creator CREATOR = new Creator(){
//
//        public final String LOG_TAG = getClass().getSimpleName();
//
//        public BaseResult createFromParcel(Parcel source){
//
//            try {
//                Object entity = Class.forName((source.readString())).newInstance();
//
//                for(Field field : entity.getClass().getDeclaredFields()) {
//                    try {
//                        field.setAccessible(true);
//                        if(field.getType().equals(List.class)) {
//                            ArrayList list = new ArrayList();
//                            source.readList(list, Class.forName(field.getDeclaringClass().getName()).getClassLoader());
//                            field.set(entity, list);
//                        }else if(field.getType().equals(Map.class)) {
//                            Map map = new HashMap();
//                            source.readMap(map, Class.forName(field.getDeclaringClass().getName()).getClassLoader());
//                            field.set(entity, map);
//                        }else{
//                            field.set(entity, source.readValue(field.getType().getClassLoader()));
//                        }
//
//
//                    } catch (Exception err) {
//                        Log.w(LOG_TAG, err.toString());
//                    }
//                }
//
//                return (BaseResult) entity;
//
//            } catch (Exception err) {
//                return null;
//            }
//
//        }
//
//        public BaseResult[] newArray(int size){
//
//            return this.newArray(size);
//        }
//    };
//
//    @Override
//    public int describeContents(){
//
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags){
//
//        dest.writeString(this.getClass().getCanonicalName());
//
//        for(Field field : this.getClass().getDeclaredFields()) {
//            try {
//
//                if(field.getType().equals(List.class)) {
//                    field.setAccessible(true);
//                    dest.writeList((List) field.get(this));
//
//                } else if(field.getType().equals(Map.class)) {
//                    field.setAccessible(true);
//                    dest.writeMap((Map) field.get(this));
//
//                } else {
//                    field.setAccessible(true);
//                    dest.writeValue(field.get(this));
//
//                }
//
//
//
//            } catch (Exception err) {
//                Log.w("BaseResult", err.toString());
//            }
//        }
//    }
//
//}
