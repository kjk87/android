package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 10. 10..
 */

public class OutgoingNumber implements Parcelable{
    private String mobile;
    private String regDate;

    public String getMobile(){

        return mobile;
    }

    public void setMobile(String mobile){

        this.mobile = mobile;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    @Override
    public String toString(){

        return "OutgoingNumber{" + "mobile='" + mobile + '\'' + ", regDate='" + regDate + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.mobile);
        dest.writeString(this.regDate);
    }

    public OutgoingNumber(){

    }

    protected OutgoingNumber(Parcel in){

        this.mobile = in.readString();
        this.regDate = in.readString();
    }

    public static final Parcelable.Creator<OutgoingNumber> CREATOR = new Parcelable.Creator<OutgoingNumber>(){

        @Override
        public OutgoingNumber createFromParcel(Parcel source){

            return new OutgoingNumber(source);
        }

        @Override
        public OutgoingNumber[] newArray(int size){

            return new OutgoingNumber[size];
        }
    };
}
