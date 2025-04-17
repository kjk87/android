package com.pplus.prnumberuser.core.network.model.request.params;

import com.pplus.prnumberuser.core.network.model.dto.No;
import com.pplus.prnumberuser.core.network.model.dto.Plus;

import java.util.List;

/**
 * Created by 김종경 on 2016-09-19.
 */
public class ParamsPlusGroup{
    private No group;
    private List<Plus> plusList;

    public No getGroup(){

        return group;
    }

    public void setGroup(No group){

        this.group = group;
    }

    public List<Plus> getPlusList(){

        return plusList;
    }

    public void setPlusList(List<Plus> plusList){

        this.plusList = plusList;
    }

    @Override
    public String toString(){

        return "ParamsPlusGroup{" + "group=" + group + ", plusList=" + plusList + '}';
    }
}
