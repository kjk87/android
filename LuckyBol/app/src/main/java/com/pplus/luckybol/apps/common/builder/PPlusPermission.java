package com.pplus.luckybol.apps.common.builder;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.pplus.utils.part.apps.permission.Permission;
import com.pplus.utils.part.apps.permission.PermissionBuilder;
import com.pplus.utils.part.apps.permission.PermissionListener;
import com.pplus.luckybol.apps.common.builder.data.AlertData;

import java.util.ArrayList;

/**
 * Created by j2n on 2016. 9. 12..
 */
public class PPlusPermission implements PermissionListener {

    private PermissionListener permissionListener;

    private PermissionBuilder builder;

    public static final int REQ_CODE_REQUEST_SETTING = 0x1111;

    private OnAlertResultListener onAlertResultListener;

    private Context context;

    String[] permission;

    public PPlusPermission(Activity activity){

        context = activity;
        builder = PermissionBuilder.create(activity);

    }


    public void setPermissionListener(PermissionListener permissionListener){

        this.permissionListener = permissionListener;
    }

    public void setDefaultAlert(OnAlertResultListener onAlertResultListener){

        this.onAlertResultListener = onAlertResultListener;
    }

    @Override
    public void onPermissionGranted(){

        this.permissionListener.onPermissionGranted();
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions){

        if(onAlertResultListener != null) {

            AlertBuilder.Builder alertBuilder = new AlertBuilder.Builder();
            alertBuilder.setTitle("안내");
            alertBuilder.addContents(new AlertData.MessageData("원활한 서비스 제공을 위해 꼭 필요한\n권한을 허용하셔야 앱 이용이 가능합니다.", AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            alertBuilder.addContents(new AlertData.MessageData("다음단계 팝업에서 권한을 허용해주세요.", AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            alertBuilder.addContents(new AlertData.MessageData("권한 허용팝업이 뜨지 않는 경우\n'단말설정 > 앱 관리 > 접근권한'\n메뉴에서 권한을 허용 할 수 있습니다.", AlertBuilder.MESSAGE_TYPE.TEXT, 3));
            alertBuilder.setAutoCancel(false);
            alertBuilder.setDefaultMaxLine(2);
            alertBuilder.setLeftText("닫기");
            alertBuilder.setRightText("설정");
            alertBuilder.setOnAlertResultListener(onAlertResultListener);
            alertBuilder.builder().show(context);

        } else {
            this.permissionListener.onPermissionDenied(deniedPermissions);
        }
    }

    //    public void checkPermission(){
    //
    //        new TedPermission(context).
    //                setPermissionListener(this).
    ////                setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]").
    //                setPermissions(permission).check();
    //
    //    }
    //
    //    public void addPermission(Permission.PERMISSION_KEY permission_key){
    //
    //        if(permission != null) {
    //
    //            String[] newPermission = permission_key.getValue();
    //            int thenSize = permission.length;
    //            int newSize = permission_key.getValue().length;
    //            String[] newArray = new String[thenSize + newSize];
    //
    //            for(int cnt = 0; cnt < thenSize; cnt++) {
    //                newArray[cnt] = permission[cnt];
    //            }
    //
    //            for(int cnt = 0; cnt < newSize; cnt++) {
    //                newArray[thenSize + cnt] = newPermission[cnt];
    //            }
    //
    //            permission = newArray;
    //
    //        } else {
    //            permission = permission_key.getValue();
    //        }
    //
    //    }

    public boolean checkSelf(){

        return builder.getDeniedPermission().isEmpty();
    }


    public void checkPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.enableSettingBtn(false);
            builder.setPermissionListener(this);
            builder.checkPermissions();
        } else {
            this.permissionListener.onPermissionGranted();
        }
    }

    public void addPermission(Permission.PERMISSION_KEY permission_key){

        builder.addPermission(permission_key);
    }


    public Intent getSettingIntent(){

        Intent intent;

        try {
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + context.getPackageName()));
        } catch (ActivityNotFoundException e) {
            intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
        }

        return intent;

    }
}
