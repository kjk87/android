package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kjk on 2017. 6. 22..
 */

public class Cash implements Parcelable{
    private Long no;
    private No user;
    private String primaryType;
    private String secondaryType;
    private String amount;
    private String regDate;
    private String subject;
    private CashProperties properties;
    private CashBillingProperties paymentProperties;
    private No target;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public No getUser(){

        return user;
    }

    public void setUser(No user){

        this.user = user;
    }

    public String getPrimaryType(){

        return primaryType;
    }

    public void setPrimaryType(String primaryType){

        this.primaryType = primaryType;
    }

    public String getSecondaryType(){

        return secondaryType;
    }

    public void setSecondaryType(String secondaryType){

        this.secondaryType = secondaryType;
    }

    public String getAmount(){

        return amount;
    }

    public void setAmount(String amount){

        this.amount = amount;
    }

    public No getTarget(){

        return target;
    }

    public void setTarget(No target){

        this.target = target;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    public String getSubject(){

        return subject;
    }

    public void setSubject(String subject){

        this.subject = subject;
    }

    public CashProperties getProperties(){

        return properties;
    }

    public void setProperties(CashProperties properties){

        this.properties = properties;
    }

    public CashBillingProperties getPaymentProperties(){

        return paymentProperties;
    }

    public void setPaymentProperties(CashBillingProperties paymentProperties){

        this.paymentProperties = paymentProperties;
    }

    @Override
    public String toString(){

        return "Cash{" + "no=" + no + ", user=" + user + ", primaryType='" + primaryType + '\'' + ", secondaryType='" + secondaryType + '\'' + ", amount='" + amount + '\'' + ", regDate='" + regDate + '\'' + ", subject='" + subject + '\'' + ", properties=" + properties + ", target=" + target + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.primaryType);
        dest.writeString(this.secondaryType);
        dest.writeString(this.amount);
        dest.writeString(this.regDate);
        dest.writeString(this.subject);
        dest.writeParcelable(this.properties, flags);
        dest.writeParcelable(this.paymentProperties, flags);
        dest.writeParcelable(this.target, flags);
    }

    public Cash(){

    }

    protected Cash(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.user = in.readParcelable(No.class.getClassLoader());
        this.primaryType = in.readString();
        this.secondaryType = in.readString();
        this.amount = in.readString();
        this.regDate = in.readString();
        this.subject = in.readString();
        this.properties = in.readParcelable(CashProperties.class.getClassLoader());
        this.paymentProperties = in.readParcelable(CashBillingProperties.class.getClassLoader());
        this.target = in.readParcelable(No.class.getClassLoader());
    }

    public static final Creator<Cash> CREATOR = new Creator<Cash>(){

        @Override
        public Cash createFromParcel(Parcel source){

            return new Cash(source);
        }

        @Override
        public Cash[] newArray(int size){

            return new Cash[size];
        }
    };
}
