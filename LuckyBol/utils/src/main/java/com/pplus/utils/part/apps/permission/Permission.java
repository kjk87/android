package com.pplus.utils.part.apps.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.pplus.utils.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 안명훈 on 16. 7. 4..
 * <p>
 * <pre>
 *     4.4 이전버전에서는 파일을 읽어오려면 READ_EXTERNAL_STORAGE 권한이 꼭 필요 했었습니다.
 *     권한이 없다면 getExternalStoragePublicDirectory()를 사용할 수 없었습니다.
 *     하지만 4.4 킷캣이상부터는 권한이 필요 없이 getExternalFilesDir(String), getExternalCacheDir()를 통해 파일을 가져올 수
 * 있습니다.
 * </pre>
 */

public class Permission{

    private Activity activity;

    public enum PERMISSION_KEY{

        CALENDAR(CALENDAR_GROUP),
        CAMERA(CAMERA_GROUP),
        CONTACTS(CONTACTS_GROUP),
        LOCATION(LOCATION_GROUP),
        MICROPHONE(MICROPHONE_GROUP),
        PHONE(PHONE_GROUP),
        PPLUS_PHONE(PPLUS_PHONE_GROUP),
        SENSORS(SENSORS_GROUP),
        SMS(SMS_GROUP), PPLUS_SMS(PPLUS_SMS_GROUP), STORAGE(STORAGE_GROUP);

        private String[] value;

        PERMISSION_KEY(String[] value){

            this.value = value;
        }

        public String[] getValue(){

            return value;
        }
    }

    public static final String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    public static final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String[] CALENDAR_GROUP = {READ_CALENDAR, WRITE_CALENDAR};

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String[] CAMERA_GROUP = {CAMERA};

    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    public static final String GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String[] CONTACTS_GROUP = {READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS};

    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String[] LOCATION_GROUP = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};

    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String[] MICROPHONE_GROUP = {RECORD_AUDIO};

    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    public static final String WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
    public static final String ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    public static final String USE_SIP = Manifest.permission.USE_SIP;
    public static final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    public static final String[] PHONE_GROUP = {READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG, WRITE_CALL_LOG, ADD_VOICEMAIL, USE_SIP, PROCESS_OUTGOING_CALLS};
    public static final String[] PPLUS_PHONE_GROUP = {READ_PHONE_STATE, CALL_PHONE};

    public static final String BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    public static final String[] SENSORS_GROUP = {BODY_SENSORS};

    public static final String SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    public static final String READ_SMS = Manifest.permission.READ_SMS;
    public static final String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
    public static final String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
    public static final String[] SMS_GROUP = {SEND_SMS, RECEIVE_SMS, READ_SMS, RECEIVE_WAP_PUSH, RECEIVE_MMS};
    public static final String[] PPLUS_SMS_GROUP = {READ_SMS, RECEIVE_MMS};

    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String[] STORAGE_GROUP = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    public Permission(Activity activity){

        this.activity = activity;
    }

    /**
     * @return <pre>
     * true : <br>
     * 이 권한이 필요한 이유에 대해 설명해야한다. <br>
     * 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다 <br>
     * 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다 <br>
     * <br>
     * false : <br>
     * 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다 <br>
     * </pre>
     */
    public boolean shouldShowRequestPermissionRationale(String permission){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.shouldShowRequestPermissionRationale(permission);
        } else {
            return false;
        }

    }

    /**
     *
     * */
    public static boolean checkSelfPermission(Context context, PERMISSION_KEY permission_key){

        for(String permission : permission_key.getValue()) {
            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);

            if(permissionCheck == PackageManager.PERMISSION_DENIED) {
                // 권한 없음
                return false;
            }
        }

        return true;
    }

    public static String convertToPermission(ArrayList<String> permissions){

        Set<String> convertStringSet = new HashSet<>();

        for(String permission : permissions) {
            if(Arrays.asList(CAMERA_GROUP).contains(permission)) {
                addUnduplicatedValue(convertStringSet, "카메라");
            } else if(Arrays.asList(CALENDAR_GROUP).contains(permission)) {
                addUnduplicatedValue(convertStringSet, "캘린더");
            } else if(Arrays.asList(CONTACTS_GROUP).contains(permission)) {
                addUnduplicatedValue(convertStringSet, "연락처");
            } else if(Arrays.asList(LOCATION_GROUP).contains(permission)) {
                addUnduplicatedValue(convertStringSet, "위치");
            } else if(Arrays.asList(STORAGE_GROUP).contains(permission)) {
                addUnduplicatedValue(convertStringSet, "저장소");
            } else if(Arrays.asList(PHONE_GROUP).contains(permission)) {
                addUnduplicatedValue(convertStringSet, "기기정보");
            } else if(Arrays.asList(SENSORS_GROUP).contains(permission)) {
                addUnduplicatedValue(convertStringSet, "센서");
            } else if(Arrays.asList(SMS_GROUP).contains(permission)) {
                addUnduplicatedValue(convertStringSet, "SMS");
            }
        }

        return String.format(Config.PERMISSION_MESSAGE, convertStringSet.toString());
        // return [카메라 , 캘린더];
    }

    private static Set<String> addUnduplicatedValue(Set<String> stringSet, String value){

        if(!stringSet.contains(value)) {
            stringSet.add(value);
        }
        return stringSet;
    }
}
