package com.pplus.prnumberuser.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 8..
 */

public class MsgProperties implements Parcelable{
    private String senderNo;
    private boolean advertise;

    public String getSenderNo(){

        return senderNo;
    }

    public void setSenderNo(String senderNo){

        this.senderNo = senderNo;
    }

    public boolean isAdvertise(){

        return advertise;
    }

    public void setAdvertise(boolean advertise){

        this.advertise = advertise;
    }

    @Override
    public String toString(){

        return "MsgProperties{" + "senderNo='" + senderNo + '\'' + ", advertise=" + advertise + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.senderNo);
        dest.writeByte(this.advertise ? (byte) 1 : (byte) 0);
    }

    public MsgProperties(){

    }

    protected MsgProperties(Parcel in){

        this.senderNo = in.readString();
        this.advertise = in.readByte() != 0;
    }

    public static final Creator<MsgProperties> CREATOR = new Creator<MsgProperties>(){

        @Override
        public MsgProperties createFromParcel(Parcel source){

            return new MsgProperties(source);
        }

        @Override
        public MsgProperties[] newArray(int size){

            return new MsgProperties[size];
        }
    };
}
