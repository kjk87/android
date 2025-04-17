package com.pplus.luckybol.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 28..
 */

public class CashProperties implements Parcelable{
    private String payType;
    private String cardName;

    public String getPayType(){

        return payType;
    }

    public void setPayType(String payType){

        this.payType = payType;
    }

    public String getCardName(){

        return cardName;
    }

    public void setCardName(String cardName){

        this.cardName = cardName;
    }

    @Override
    public String toString(){

        return "CashProperties{" + "payType='" + payType + '\'' + ", cardName='" + cardName + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.payType);
        dest.writeString(this.cardName);
    }

    public CashProperties(){

    }

    protected CashProperties(Parcel in){

        this.payType = in.readString();
        this.cardName = in.readString();
    }

    public static final Creator<CashProperties> CREATOR = new Creator<CashProperties>(){

        @Override
        public CashProperties createFromParcel(Parcel source){

            return new CashProperties(source);
        }

        @Override
        public CashProperties[] newArray(int size){

            return new CashProperties[size];
        }
    };
}
