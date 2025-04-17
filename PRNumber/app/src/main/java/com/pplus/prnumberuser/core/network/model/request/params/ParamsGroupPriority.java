package com.pplus.prnumberuser.core.network.model.request.params;

import android.annotation.SuppressLint;

import com.pplus.prnumberuser.core.network.model.dto.Group;
import com.pplus.prnumberuser.core.network.model.request.BaseParams;

import java.util.List;

/**
 * Created by j2n on 2016. 7. 26..
 */
@SuppressLint("ParcelCreator")
public class ParamsGroupPriority extends BaseParams{
    private List<Group> groupList;

    public List<Group> getGroupList(){

        return groupList;
    }

    public void setGroupList(List<Group> groupList){

        this.groupList = groupList;
    }

    @Override
    public String toString(){

        return "ParamsGroupPriority{" + "groupList=" + groupList + '}';
    }
}
