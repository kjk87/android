package com.pple.pplus.utils.part.apps.permission;

import java.util.ArrayList;

import com.pple.pplus.utils.part.utils.ArrayUtils;

/**
 * Created by 안명훈 on 16. 7. 5..
 */
public class PermissionEvent {

    private ArrayList<String> deniedPermissions;

    public PermissionEvent(ArrayList<String> deniedPermissions) {
        this.deniedPermissions = deniedPermissions;
    }

    public ArrayList<String> getDeniedPermissions() {
        return deniedPermissions;
    }

    public boolean hasPermission() {
        return !ArrayUtils.isEmpty(deniedPermissions);
    }
}
