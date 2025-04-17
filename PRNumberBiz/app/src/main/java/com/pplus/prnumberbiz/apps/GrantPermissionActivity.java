package com.pplus.prnumberbiz.apps;

import android.os.Bundle;
import android.view.View;

import com.pple.pplus.utils.part.apps.permission.Permission;
import com.pple.pplus.utils.part.apps.permission.PermissionListener;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.PPlusPermission;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;

import java.util.ArrayList;

public class GrantPermissionActivity extends BaseActivity{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_grant_permission;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){
        findViewById(R.id.text_grant_permission).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                PPlusPermission pPlusPermission = new PPlusPermission(GrantPermissionActivity.this);
//                pPlusPermission.addPermission(Permission.PERMISSION_KEY.PPLUS_PHONE);
//                pPlusPermission.addPermission(Permission.PERMISSION_KEY.LOCATION);
                pPlusPermission.addPermission(Permission.PERMISSION_KEY.CONTACTS);
                pPlusPermission.setPermissionListener(new PermissionListener(){

                    @Override
                    public void onPermissionGranted(){
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions){

                    }
                });
                pPlusPermission.checkPermission();
            }
        });
    }

}
