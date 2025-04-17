package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kjk on 2017. 6. 22..
 */

public class BolGift implements Parcelable{
    private boolean received;
    private Bol history;
    private String amount;
    private String receiveDate;

    @Override
    public String toString(){

        return "BolGift{" + "received=" + received + ", history=" + history + ", amount='" + amount + '\'' + ", receiveDate='" + receiveDate + '\'' + '}';
    }

    public boolean isReceived(){

        return received;
    }

    public void setReceived(boolean received){

        this.received = received;
    }

    public Bol getHistory(){

        return history;
    }

    public void setHistory(Bol history){

        this.history = history;
    }

    public String getAmount(){

        return amount;
    }

    public void setAmount(String amount){

        this.amount = amount;
    }

    public String getReceiveDate(){

        return receiveDate;
    }

    public void setReceiveDate(String receiveDate){

        this.receiveDate = receiveDate;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeByte(this.received ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.history, flags);
        dest.writeString(this.amount);
        dest.writeString(this.receiveDate);
    }

    public BolGift(){

    }

    protected BolGift(Parcel in){

        this.received = in.readByte() != 0;
        this.history = in.readParcelable(Bol.class.getClassLoader());
        this.amount = in.readString();
        this.receiveDate = in.readString();
    }

    public static final Creator<BolGift> CREATOR = new Creator<BolGift>(){

        @Override
        public BolGift createFromParcel(Parcel source){

            return new BolGift(source);
        }

        @Override
        public BolGift[] newArray(int size){

            return new BolGift[size];
        }
    };
}
