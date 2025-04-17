package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 10. 31..
 */

public class PgApproval implements Parcelable{
    private String authTransactionId;//트랜잭션 ID
    private String orderKey; //OID

    @Override
    public String toString(){

        return "PgApproval{" + "authTransactionId='" + authTransactionId + '\'' + ", orderKey='" + orderKey + '\'' + '}';
    }

    public String getAuthTransactionId(){

        return authTransactionId;
    }

    public void setAuthTransactionId(String authTransactionId){

        this.authTransactionId = authTransactionId;
    }

    public String getOrderKey(){

        return orderKey;
    }

    public void setOrderKey(String orderKey){

        this.orderKey = orderKey;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.authTransactionId);
        dest.writeString(this.orderKey);
    }

    public PgApproval(){

    }

    protected PgApproval(Parcel in){

        this.authTransactionId = in.readString();
        this.orderKey = in.readString();
    }

    public static final Creator<PgApproval> CREATOR = new Creator<PgApproval>(){

        @Override
        public PgApproval createFromParcel(Parcel source){

            return new PgApproval(source);
        }

        @Override
        public PgApproval[] newArray(int size){

            return new PgApproval[size];
        }
    };
}
