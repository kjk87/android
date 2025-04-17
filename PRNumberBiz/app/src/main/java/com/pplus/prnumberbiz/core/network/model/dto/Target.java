package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 8..
 */

public class Target implements Parcelable{
    private String status;
    private String sendDate;
    private String mobile;
    private boolean readed;
    private Customer customer;
    private User user;
    private String name;

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

    public String getMobile(){

        return mobile;
    }

    public void setMobile(String mobile){

        this.mobile = mobile;
    }

    public boolean isReaded(){

        return readed;
    }

    public void setReaded(boolean readed){

        this.readed = readed;
    }

    public Customer getCustomer(){

        return customer;
    }

    public void setCustomer(Customer customer){

        this.customer = customer;
    }

    public User getUser(){

        return user;
    }

    public void setUser(User user){

        this.user = user;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return "Target{" + "status='" + status + '\'' + ", sendDate='" + sendDate + '\'' + ", mobile='" + mobile + '\'' + ", readed=" + readed + ", customer=" + customer + ", user=" + user + ", name='" + name + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.status);
        dest.writeString(this.sendDate);
        dest.writeString(this.mobile);
        dest.writeByte(this.readed ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.customer, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.name);
    }

    public Target(){

    }

    protected Target(Parcel in){

        this.status = in.readString();
        this.sendDate = in.readString();
        this.mobile = in.readString();
        this.readed = in.readByte() != 0;
        this.customer = in.readParcelable(Customer.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Creator<Target> CREATOR = new Creator<Target>(){

        @Override
        public Target createFromParcel(Parcel source){

            return new Target(source);
        }

        @Override
        public Target[] newArray(int size){

            return new Target[size];
        }
    };
}
