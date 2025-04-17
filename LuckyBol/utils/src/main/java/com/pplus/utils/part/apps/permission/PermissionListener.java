package com.pplus.utils.part.apps.permission;

import java.util.ArrayList;

/**
 * Created by 안명훈 on 16. 7. 4..
 */
public interface PermissionListener {

    void onPermissionGranted();

    void onPermissionDenied(ArrayList<String> deniedPermissions);
}
