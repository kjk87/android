package com.pplus.prnumberbiz.core.network.model.request.params;

import com.pplus.prnumberbiz.core.network.model.dto.Device;

/**
 * Created by 김종경 on 2016-09-19.
 */
public class ParamsRegDevice{

    private Long no;
    private Device device;

    public ParamsRegDevice(){

    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public Device getDevice(){

        return device;
    }

    public void setDevice(Device device){

        this.device = device;
    }

    @Override
    public String toString(){

        return "ParamsRegDevice{" + "no=" + no + ", device=" + device + '}';
    }
}
