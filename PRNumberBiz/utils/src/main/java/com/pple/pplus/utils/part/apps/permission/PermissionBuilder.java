package com.pple.pplus.utils.part.apps.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;

import com.pple.pplus.utils.BusProvider;

/**
 * Created by 안명훈 on 16. 7. 4..
 */
public class PermissionBuilder{

    private PermissionListener permissionListener;
    private Context context;

    private ArrayList<String> permissions;
    private String rationaleMessage;
    private String denyMessage;
    private boolean hasSettingBtn = false;

    private String settingButtonText = "설정";
    private String deniedCloseButtonText = "닫기";
    private String rationaleConfirmText = "확인";

    public String LOG_TAG = this.getClass().getSimpleName();

    private boolean isRegister = false;

    private PermissionBuilder(Context context){

        this.context = context;
        permissions = new ArrayList<>();
    }

    public static PermissionBuilder create(Context context){

        return new PermissionBuilder(context);
    }

    public PermissionBuilder setPermissionListener(PermissionListener permissionListener){

        this.permissionListener = permissionListener;
        return this;
    }

    public PermissionBuilder setDeniedMessage(String deniedMessage){

        this.denyMessage = deniedMessage;
        return this;
    }

    public PermissionBuilder addPermission(Permission.PERMISSION_KEY permission){

        addPermissions(permission.getValue());
        return this;
    }

    public PermissionBuilder addPermission(String permission){

        this.permissions.add(permission);
        return this;
    }

    public PermissionBuilder addPermissions(String[] permission){

        Collections.addAll(this.permissions, permission);
        return this;
    }

    public PermissionBuilder enableSettingBtn(boolean enable){

        hasSettingBtn = enable;
        return this;
    }

    public PermissionBuilder setRationaleMessage(String rationaleMessage){

        this.rationaleMessage = rationaleMessage;
        return this;
    }

    public PermissionListener getPermissionListener(){

        return permissionListener;
    }

    public Context getContext(){

        return context;
    }

    public void checkPermissions(){

        if(!isRegister) {
            isRegister = true;
            BusProvider.getInstance().register(this);
        }

        //        ArrayList<String> arrayList = getDeniedPermission();
        //        if(!arrayList.isEmpty()) {
        //            onPermissionResult(new PermissionEvent(arrayList));
        //            return;
        //        }

        Intent intent = new Intent(context, PermissionActivity.class);

        String[] resultPermission = new String[permissions.size()];
        permissions.toArray(resultPermission);
        intent.putExtra(PermissionActivity.EXTRA_PERMISSIONS, resultPermission);

        intent.putExtra(PermissionActivity.EXTRA_RATIONALE_MESSAGE, rationaleMessage);
        intent.putExtra(PermissionActivity.EXTRA_DENY_MESSAGE, denyMessage);
        intent.putExtra(PermissionActivity.EXTRA_PACKAGE_NAME, context.getPackageName());
        intent.putExtra(PermissionActivity.EXTRA_SETTING_BUTTON, hasSettingBtn);
        intent.putExtra(PermissionActivity.EXTRA_DENIED_DIALOG_CLOSE_TEXT, deniedCloseButtonText);
        intent.putExtra(PermissionActivity.EXTRA_RATIONALE_CONFIRM_TEXT, rationaleConfirmText);
        intent.putExtra(PermissionActivity.EXTRA_SETTING_BUTTON_TEXT, settingButtonText);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Subscribe
    public void onPermissionResult(PermissionEvent event){

        if(!event.hasPermission()) {
            permissionListener.onPermissionGranted();
        } else {
            LogUtil.e(LOG_TAG, "event --> {}", event.getDeniedPermissions());

            LogUtil.e(LOG_TAG, "convertToPermission --> {}", Permission.convertToPermission(event.getDeniedPermissions()));

            permissionListener.onPermissionDenied(event.getDeniedPermissions());
        }

        if(isRegister) {
            isRegister = false;
            BusProvider.getInstance().unregister(this);
        }

    }

    /**
     * self permission check..
     */
    public ArrayList<String> getDeniedPermission(){

        // 권한을 획득하지 않은 퍼미션만 반환한다.
        ArrayList<String> deniedPermissions = new ArrayList<>();

        for(String permission : this.permissions) {

            int permissionCheck = ContextCompat.checkSelfPermission(context, permission);

            if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                // 권한 없음
                deniedPermissions.add(permission);

                LogUtil.e(LOG_TAG, "permission PERMISSION_DENIED --> {}", permission);
            }
        }

        return deniedPermissions;
    }

    //    public boolean shouldShowRequestPermissionRationale(String permission){
    //
    //        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    //            return getContext().shouldShowRequestPermissionRationale(permission);
    //        } else {
    //            return false;
    //        }
    //
    //    }
}
