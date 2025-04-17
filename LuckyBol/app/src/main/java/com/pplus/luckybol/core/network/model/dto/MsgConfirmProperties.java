package com.pplus.luckybol.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 8..
 */

public class MsgConfirmProperties implements Parcelable{
    private String confirmKey;

    public String getConfirmKey(){

        return confirmKey;
    }

    public void setConfirmKey(String confirmKey){

        this.confirmKey = confirmKey;
    }

    @Override
    public String toString(){

        return "MsgConfirmProperties{" + "confirmKey='" + confirmKey + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.confirmKey);
    }

    public MsgConfirmProperties(){

    }

    protected MsgConfirmProperties(Parcel in){

        this.confirmKey = in.readString();
    }

    public static final Creator<MsgConfirmProperties> CREATOR = new Creator<MsgConfirmProperties>(){

        @Override
        public MsgConfirmProperties createFromParcel(Parcel source){

            return new MsgConfirmProperties(source);
        }

        @Override
        public MsgConfirmProperties[] newArray(int size){

            return new MsgConfirmProperties[size];
        }
    };
}
