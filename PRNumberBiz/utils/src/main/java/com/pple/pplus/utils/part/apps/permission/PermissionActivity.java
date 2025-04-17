package com.pple.pplus.utils.part.apps.permission;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pple.pplus.utils.BusProvider;
import com.pple.pplus.utils.part.logs.LogUtil;

import java.util.ArrayList;

/**
 * Created by 안명훈 on 16. 7. 4..
 */
public class PermissionActivity extends AppCompatActivity{

    public static final int REQ_CODE_PERMISSION_REQUEST = 10;
    public static final int REQ_CODE_REQUEST_SETTING = 20;

    public static final String EXTRA_PERMISSIONS = "permissions";
    public static final String EXTRA_RATIONALE_MESSAGE = "rationale_message";
    public static final String EXTRA_DENY_MESSAGE = "deny_message";
    public static final String EXTRA_PACKAGE_NAME = "package_name";
    public static final String EXTRA_SETTING_BUTTON = "setting_button";
    public static final String EXTRA_SETTING_BUTTON_TEXT = "setting_button_text";
    public static final String EXTRA_RATIONALE_CONFIRM_TEXT = "rationale_confirm_text";
    public static final String EXTRA_DENIED_DIALOG_CLOSE_TEXT = "denied_dialog_close_text";

    private String rationale_message;
    private String denyMessage;
    private String[] permissions;
    private String packageName;
    private boolean hasSettingButton;
    private String settingButtonText;

    private String deniedCloseButtonText;
    private String rationaleConfirmText;

    private final String LOG_TAG = PermissionActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        ImageView view = new ImageView(this);
        view.setBackgroundColor(Color.parseColor("#000000"));

        setContentView(view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }

        setupFromSavedInstanceState(savedInstanceState);

        LogUtil.e(LOG_TAG, "PermissionActivity.onCreate");

        checkPermissions(false);
    }

    private void permissionGranted(){

        try {
            BusProvider.getInstance().post(new PermissionEvent(null));
        } catch (Exception e){
            LogUtil.e(LOG_TAG , e);
        }

        finish();
        overridePendingTransition(0, 0);
    }

    private void permissionDenied(ArrayList<String> deniedpermissions){

        try {
            BusProvider.getInstance().post(new PermissionEvent(deniedpermissions));
        } catch (Exception e){
            LogUtil.e(LOG_TAG , e);
        }

        finish();
        overridePendingTransition(0, 0);
    }

    private void checkPermissions(boolean fromOnActivityResult){


        LogUtil.e(LOG_TAG, "checkPermissions");

        ArrayList<String> needPermissions = new ArrayList<>();

        /**
         * 실제 허용된 퍼미션에 대해 처리진행!
         * */
        for(String permission : permissions) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(permission);
            }
        }

        /**
         * 권한을 획득해야하는 퍼미션이 없을 경우!
         * */
        if(needPermissions.isEmpty()) {
            permissionGranted();
            return;
        } else {
            /**
             * 권한 요청 진행!
             * */
            requestPermissions(needPermissions);
            return;
        }
    }

    public void requestPermissions(ArrayList<String> needPermissions){

        LogUtil.e(LOG_TAG, "requestPermissions", needPermissions);

        ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQ_CODE_PERMISSION_REQUEST);
    }

    private void showPermissionDenyDialog(final ArrayList<String> deniedPermissions){

        //        hasSettingButton

        if(hasSettingButton) {

            Spanned message = Html.fromHtml("<h5 style='text-align: center'>원활한 서비스 제공을 위해 꼭 필요한<br /> 권한을 허용하셔야 앱 이용이 가능합니다.</h5>" +
                    "<h5 style='text-align: center;'><span style='color: #ff0000;'>다음단계 팝업에서 권한을 허용해주세요</span><br /><span style='color: #ff0000;'>(권한을 허용하지 않을 경우 앱이 종료됩니다)</span></h5>" +
                    "<h6 style='text-align: center;'>※ 권한 허용팝업이 뜨지 않는 경우<br />'단말설정 &gt; 앱 관리 &gt; myApp &gt; 접근권한'<br />메뉴에서 권한을 허용 할 수 있습니다.</h6>");

            new AlertDialog.Builder(this).setMessage(message).setCancelable(false).setTitle("안내").setNegativeButton(deniedCloseButtonText, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i){

                    permissionDenied(deniedPermissions);
                }
            }).setPositiveButton(settingButtonText, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which){

                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + packageName));
                        startActivityForResult(intent, REQ_CODE_REQUEST_SETTING);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        LogUtil.e(LOG_TAG, e);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        startActivityForResult(intent, REQ_CODE_REQUEST_SETTING);
                    }
                }
            }).show();

        } else {
            permissionDenied(deniedPermissions);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){

        switch (requestCode) {
            case REQ_CODE_PERMISSION_REQUEST:

                ArrayList<String> denienList = new ArrayList<>();
                int success = 0;
                int fail = 0;

                for(int i = 0; i < permissions.length; i++) {
                    if(grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        LogUtil.e(LOG_TAG, "permission was granted permission = {} , grantResults = {}", permissions[i], grantResults[i]);
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        success++;
                    } else {

                        denienList.add(permissions[i]);
                        LogUtil.e(LOG_TAG, "permission denied permission = {} , grantResults = {}", permissions[i], grantResults[i]);
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        fail++;
                    }
                }

                if(!denienList.isEmpty()) {
                    /**
                     * 요청한 권한을 획득하지 못하였습니다!
                     * */
                    showPermissionDenyDialog(denienList);
                } else {
                    /**
                     * 성공하였습니다.
                     * */
                    permissionGranted();
                }

                LogUtil.e(LOG_TAG, "permission count success = {} , fail = {}", success, fail);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode) {
            case REQ_CODE_REQUEST_SETTING:
                checkPermissions(true);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void setupFromSavedInstanceState(Bundle savedInstanceState){

        if(savedInstanceState != null) {
            permissions = savedInstanceState.getStringArray(EXTRA_PERMISSIONS);
            rationale_message = savedInstanceState.getString(EXTRA_RATIONALE_MESSAGE);
            denyMessage = savedInstanceState.getString(EXTRA_DENY_MESSAGE);
            packageName = savedInstanceState.getString(EXTRA_PACKAGE_NAME);

            hasSettingButton = savedInstanceState.getBoolean(EXTRA_SETTING_BUTTON, true);

            rationaleConfirmText = savedInstanceState.getString(EXTRA_RATIONALE_CONFIRM_TEXT);
            deniedCloseButtonText = savedInstanceState.getString(EXTRA_DENIED_DIALOG_CLOSE_TEXT);

            settingButtonText = savedInstanceState.getString(EXTRA_SETTING_BUTTON_TEXT);
        } else {

            Intent intent = getIntent();
            permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS);
            rationale_message = intent.getStringExtra(EXTRA_RATIONALE_MESSAGE);
            denyMessage = intent.getStringExtra(EXTRA_DENY_MESSAGE);
            packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
            hasSettingButton = intent.getBooleanExtra(EXTRA_SETTING_BUTTON, true);
            rationaleConfirmText = intent.getStringExtra(EXTRA_RATIONALE_CONFIRM_TEXT);
            deniedCloseButtonText = intent.getStringExtra(EXTRA_DENIED_DIALOG_CLOSE_TEXT);
            settingButtonText = intent.getStringExtra(EXTRA_SETTING_BUTTON_TEXT);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){

        outState.putStringArray(EXTRA_PERMISSIONS, permissions);
        outState.putString(EXTRA_RATIONALE_MESSAGE, rationale_message);
        outState.putString(EXTRA_DENY_MESSAGE, denyMessage);
        outState.putString(EXTRA_PACKAGE_NAME, packageName);
        outState.putBoolean(EXTRA_SETTING_BUTTON, hasSettingButton);
        outState.putString(EXTRA_SETTING_BUTTON, deniedCloseButtonText);
        outState.putString(EXTRA_RATIONALE_CONFIRM_TEXT, rationaleConfirmText);
        outState.putString(EXTRA_SETTING_BUTTON_TEXT, settingButtonText);

        super.onSaveInstanceState(outState);
    }


}
