package com.pplus.prnumberuser.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kjk on 2017. 5. 15..
 */

public class BankData implements Parcelable{
    private String name;
    private String code;

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    public String getCode(){

        return code;
    }

    public void setCode(String code){

        this.code = code;
    }

    @Override
    public String toString(){

        return "BankData{" + "name='" + name + '\'' + ", code='" + code + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.name);
        dest.writeString(this.code);
    }

    public BankData(){

    }

    protected BankData(Parcel in){

        this.name = in.readString();
        this.code = in.readString();
    }

    public static final Creator<BankData> CREATOR = new Creator<BankData>(){

        @Override
        public BankData createFromParcel(Parcel source){

            return new BankData(source);
        }

        @Override
        public BankData[] newArray(int size){

            return new BankData[size];
        }
    };
}
