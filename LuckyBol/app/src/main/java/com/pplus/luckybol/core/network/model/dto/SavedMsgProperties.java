package com.pplus.luckybol.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 8..
 */

public class SavedMsgProperties implements Parcelable{
    private String msg;
    private boolean advertise;

    public String getMsg(){

        return msg;
    }

    public void setMsg(String msg){

        this.msg = msg;
    }

    public boolean isAdvertise(){

        return advertise;
    }

    public void setAdvertise(boolean advertise){

        this.advertise = advertise;
    }

    @Override
    public String toString(){

        return "SavedMsgProperties{" + "msg='" + msg + '\'' + ", advertise=" + advertise + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.msg);
        dest.writeByte(this.advertise ? (byte) 1 : (byte) 0);
    }

    public SavedMsgProperties(){

    }

    protected SavedMsgProperties(Parcel in){

        this.msg = in.readString();
        this.advertise = in.readByte() != 0;
    }

    public static final Creator<SavedMsgProperties> CREATOR = new Creator<SavedMsgProperties>(){

        @Override
        public SavedMsgProperties createFromParcel(Parcel source){

            return new SavedMsgProperties(source);
        }

        @Override
        public SavedMsgProperties[] newArray(int size){

            return new SavedMsgProperties[size];
        }
    };
}
