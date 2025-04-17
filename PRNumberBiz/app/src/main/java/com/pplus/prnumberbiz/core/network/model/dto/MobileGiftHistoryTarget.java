package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 11. 22..
 */

public class MobileGiftHistoryTarget implements Parcelable{
    private Long no;
    private String mobile;
    private String name;
    private String status;
    private String confirmKey;
    private String resultMsg;
    private MobileGiftHistoryTargetProperties properties;

    @Override
    public String toString(){

        return "MobileGiftHistoryTarget{" + "no=" + no + ", mobile='" + mobile + '\'' + ", name='" + name + '\'' + ", status='" + status + '\'' + ", confirmKey='" + confirmKey + '\'' + ", resultMsg='" + resultMsg + '\'' + ", properties=" + properties + '}';
    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getMobile(){

        return mobile;
    }

    public void setMobile(String mobile){

        this.mobile = mobile;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public String getConfirmKey(){

        return confirmKey;
    }

    public void setConfirmKey(String confirmKey){

        this.confirmKey = confirmKey;
    }

    public String getResultMsg(){

        return resultMsg;
    }

    public void setResultMsg(String resultMsg){

        this.resultMsg = resultMsg;
    }

    public MobileGiftHistoryTargetProperties getProperties(){

        return properties;
    }

    public void setProperties(MobileGiftHistoryTargetProperties properties){

        this.properties = properties;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.mobile);
        dest.writeString(this.name);
        dest.writeString(this.status);
        dest.writeString(this.confirmKey);
        dest.writeString(this.resultMsg);
        dest.writeParcelable(this.properties, flags);
    }

    public MobileGiftHistoryTarget(){

    }

    protected MobileGiftHistoryTarget(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.mobile = in.readString();
        this.name = in.readString();
        this.status = in.readString();
        this.confirmKey = in.readString();
        this.resultMsg = in.readString();
        this.properties = in.readParcelable(MobileGiftHistoryTargetProperties.class.getClassLoader());
    }

    public static final Parcelable.Creator<MobileGiftHistoryTarget> CREATOR = new Parcelable.Creator<MobileGiftHistoryTarget>(){

        @Override
        public MobileGiftHistoryTarget createFromParcel(Parcel source){

            return new MobileGiftHistoryTarget(source);
        }

        @Override
        public MobileGiftHistoryTarget[] newArray(int size){

            return new MobileGiftHistoryTarget[size];
        }
    };
}
