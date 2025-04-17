package com.pplus.utils.part.apps.permission;

import com.pplus.utils.part.utils.ArrayUtils;

import java.util.ArrayList;

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
