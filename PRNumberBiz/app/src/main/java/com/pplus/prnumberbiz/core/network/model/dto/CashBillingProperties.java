package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 28..
 */

public class CashBillingProperties implements Parcelable{
    private String packageName;
    private String productId;
    private String purchaseToken;

    public String getPackageName(){

        return packageName;
    }

    public void setPackageName(String packageName){

        this.packageName = packageName;
    }

    public String getProductId(){

        return productId;
    }

    public void setProductId(String productId){

        this.productId = productId;
    }

    public String getPurchaseToken(){

        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken){

        this.purchaseToken = purchaseToken;
    }

    @Override
    public String toString(){

        return "CashBillingProperties{" + "packageName='" + packageName + '\'' + ", productId='" + productId + '\'' + ", purchaseToken='" + purchaseToken + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.packageName);
        dest.writeString(this.productId);
        dest.writeString(this.purchaseToken);
    }

    public CashBillingProperties(){

    }

    protected CashBillingProperties(Parcel in){

        this.packageName = in.readString();
        this.productId = in.readString();
        this.purchaseToken = in.readString();
    }

    public static final Creator<CashBillingProperties> CREATOR = new Creator<CashBillingProperties>(){

        @Override
        public CashBillingProperties createFromParcel(Parcel source){

            return new CashBillingProperties(source);
        }

        @Override
        public CashBillingProperties[] newArray(int size){

            return new CashBillingProperties[size];
        }
    };
}
