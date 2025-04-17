package com.pplus.prnumberuser;

/**
 * Created by ksh on 2016-10-19.
 */


/**
 * Bus Provider를 정의하기 위한 Data
 */
public class BusProviderData{

    public static final int BUS_MAIN = 100; // 메인으로 이동 // 이동시 AppMainActivity의 main ~ contact의 값이 필요
    public static final int BUS_PLUS = 101; // plus

    private int type;
    private Object data;
    private Object subData;


    public int getType(){

        return type;
    }

    public BusProviderData setType(int type){

        this.type = type;

        return this;
    }

    public Object getData(){

        return data;
    }

    public BusProviderData setData(Object data){

        this.data = data;
        return this;
    }

    public static int getBusMain(){

        return BUS_MAIN;
    }

    public Object getSubData(){

        return subData;
    }

    public BusProviderData setSubData(Object subData){

        this.subData = subData;
        return this;
    }

    @Override
    public String toString(){

        return "BusProviderData{" +
                "type=" + type +
                ", data=" + data +
                ", subData=" + subData +
                '}';
    }
}
