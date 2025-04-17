package com.pplus.prnumberbiz.core.network.model.request.params;

import com.pplus.prnumberbiz.core.network.model.dto.Fan;
import com.pplus.prnumberbiz.core.network.model.dto.No;


import java.util.List;

/**
 * Created by 김종경 on 2016-09-19.
 */
public class ParamsFanGroup {
    private No group;
    private List<Fan> fanList;

    public No getGroup(){

        return group;
    }

    public void setGroup(No group){

        this.group = group;
    }

    public List<Fan> getFanList(){

        return fanList;
    }

    public void setFanList(List<Fan> fanList){

        this.fanList = fanList;
    }

    @Override
    public String toString(){

        return "ParamsFanGroup{" + "group=" + group + ", fanList=" + fanList + '}';
    }
}
