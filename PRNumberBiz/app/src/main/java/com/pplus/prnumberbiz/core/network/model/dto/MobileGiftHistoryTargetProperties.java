package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 11. 22..
 */

public class MobileGiftHistoryTargetProperties implements Parcelable{

    private String PINNUMBER;
    private String useStartDate;
    private String useEndDate;

    public String getPINNUMBER(){

        return PINNUMBER;
    }

    public void setPINNUMBER(String PINNUMBER){

        this.PINNUMBER = PINNUMBER;
    }

    public String getUseStartDate(){

        return useStartDate;
    }

    public void setUseStartDate(String useStartDate){

        this.useStartDate = useStartDate;
    }

    public String getUseEndDate(){

        return useEndDate;
    }

    public void setUseEndDate(String useEndDate){

        this.useEndDate = useEndDate;
    }

    @Override
    public String toString(){

        return "MobileGiftHistoryTargetProperties{" + "PINNUMBER='" + PINNUMBER + '\'' + ", useStartDate='" + useStartDate + '\'' + ", useEndDate='" + useEndDate + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.PINNUMBER);
        dest.writeString(this.useStartDate);
        dest.writeString(this.useEndDate);
    }

    public MobileGiftHistoryTargetProperties(){

    }

    protected MobileGiftHistoryTargetProperties(Parcel in){

        this.PINNUMBER = in.readString();
        this.useStartDate = in.readString();
        this.useEndDate = in.readString();
    }

    public static final Creator<MobileGiftHistoryTargetProperties> CREATOR = new Creator<MobileGiftHistoryTargetProperties>(){

        @Override
        public MobileGiftHistoryTargetProperties createFromParcel(Parcel source){

            return new MobileGiftHistoryTargetProperties(source);
        }

        @Override
        public MobileGiftHistoryTargetProperties[] newArray(int size){

            return new MobileGiftHistoryTargetProperties[size];
        }
    };
}
