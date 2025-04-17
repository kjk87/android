package com.pplus.prnumberuser.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 8..
 */

public class MsgTarget implements Parcelable{
    private MsgConfirmProperties confirmProperties;
    private String status;
    private String sendDate;
    private No user;
    private String mobile;
    private String name;

    @Override
    public String toString(){

        return "MsgTarget{" + "confirmProperties=" + confirmProperties + ", status='" + status + '\'' + ", sendDate='" + sendDate + '\'' + ", user=" + user + ", mobile='" + mobile + '\'' + ", name='" + name + '\'' + '}';
    }

    public MsgConfirmProperties getConfirmProperties(){

        return confirmProperties;
    }

    public void setConfirmProperties(MsgConfirmProperties confirmProperties){

        this.confirmProperties = confirmProperties;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public String getSendDate(){

        return sendDate;
    }

    public void setSendDate(String sendDate){

        this.sendDate = sendDate;
    }

    public No getUser(){

        return user;
    }

    public void setUser(No user){

        this.user = user;
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

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeParcelable(this.confirmProperties, flags);
        dest.writeString(this.status);
        dest.writeString(this.sendDate);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.mobile);
        dest.writeString(this.name);
    }

    public MsgTarget(){

    }

    protected MsgTarget(Parcel in){

        this.confirmProperties = in.readParcelable(MsgConfirmProperties.class.getClassLoader());
        this.status = in.readString();
        this.sendDate = in.readString();
        this.user = in.readParcelable(No.class.getClassLoader());
        this.mobile = in.readString();
        this.name = in.readString();
    }

    public static final Creator<MsgTarget> CREATOR = new Creator<MsgTarget>(){

        @Override
        public MsgTarget createFromParcel(Parcel source){

            return new MsgTarget(source);
        }

        @Override
        public MsgTarget[] newArray(int size){

            return new MsgTarget[size];
        }
    };
}
