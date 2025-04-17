package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 11. 27..
 */

public class StepAddress implements Parcelable{
    private double y_coor;
    private double x_coor;
    private String full_addr;
    private String addr_name;
    private String cd;

    public double getY_coor(){

        return y_coor;
    }

    public void setY_coor(double y_coor){

        this.y_coor = y_coor;
    }

    public double getX_coor(){

        return x_coor;
    }

    public void setX_coor(double x_coor){

        this.x_coor = x_coor;
    }

    public String getFull_addr(){

        return full_addr;
    }

    public void setFull_addr(String full_addr){

        this.full_addr = full_addr;
    }

    public String getAddr_name(){

        return addr_name;
    }

    public void setAddr_name(String addr_name){

        this.addr_name = addr_name;
    }

    public String getCd(){

        return cd;
    }

    public void setCd(String cd){

        this.cd = cd;
    }

    @Override
    public String toString(){

        return "StepAddress{" + "y_coor=" + y_coor + ", x_coor=" + x_coor + ", full_addr='" + full_addr + '\'' + ", addr_name='" + addr_name + '\'' + ", cd='" + cd + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeDouble(this.y_coor);
        dest.writeDouble(this.x_coor);
        dest.writeString(this.full_addr);
        dest.writeString(this.addr_name);
        dest.writeString(this.cd);
    }

    public StepAddress(){

    }

    protected StepAddress(Parcel in){

        this.y_coor = in.readDouble();
        this.x_coor = in.readDouble();
        this.full_addr = in.readString();
        this.addr_name = in.readString();
        this.cd = in.readString();
    }

    public static final Parcelable.Creator<StepAddress> CREATOR = new Parcelable.Creator<StepAddress>(){

        @Override
        public StepAddress createFromParcel(Parcel source){

            return new StepAddress(source);
        }

        @Override
        public StepAddress[] newArray(int size){

            return new StepAddress[size];
        }
    };
}
