package com.pplus.prnumberbiz.apps.billing.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 11. 13..
 */

public class BillingData implements Parcelable{
    private String amount;
    private String point;
    private String id;

    public BillingData(String amount, String id){

        this.amount = amount;
        this.id = id;
    }

    public BillingData(String amount, String point, String id){

        this.amount = amount;
        this.point = point;
        this.id = id;
    }

    public String getAmount(){

        return amount;
    }

    public void setAmount(String amount){

        this.amount = amount;
    }

    public String getId(){

        return id;
    }

    public void setId(String id){

        this.id = id;
    }

    public String getPoint(){

        return point;
    }

    public void setPoint(String point){

        this.point = point;
    }

    @Override
    public String toString(){

        return "BillingData{" + "amount='" + amount + '\'' + ", point='" + point + '\'' + ", id='" + id + '\'' + '}';
    }


    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.amount);
        dest.writeString(this.point);
        dest.writeString(this.id);
    }

    protected BillingData(Parcel in){

        this.amount = in.readString();
        this.point = in.readString();
        this.id = in.readString();
    }

    public static final Creator<BillingData> CREATOR = new Creator<BillingData>(){

        @Override
        public BillingData createFromParcel(Parcel source){

            return new BillingData(source);
        }

        @Override
        public BillingData[] newArray(int size){

            return new BillingData[size];
        }
    };
}
