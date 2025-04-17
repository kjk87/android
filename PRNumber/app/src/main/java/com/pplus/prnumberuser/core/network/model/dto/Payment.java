package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kjk on 2017. 5. 15..
 */

public class Payment implements Parcelable{

    private Long no;
    private String status;
    private String expireDate;
    private String payTransactionId;
    private String authTransactionId;
    private String payResultCode;
    private String authResultMsg;
    private String payInfo;
    private String payMethod;
    private PaymentProperties properties;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public String getExpireDate(){

        return expireDate;
    }

    public void setExpireDate(String expireDate){

        this.expireDate = expireDate;
    }

    public String getPayTransactionId(){

        return payTransactionId;
    }

    public void setPayTransactionId(String payTransactionId){

        this.payTransactionId = payTransactionId;
    }

    public String getAuthTransactionId(){

        return authTransactionId;
    }

    public void setAuthTransactionId(String authTransactionId){

        this.authTransactionId = authTransactionId;
    }

    public String getPayResultCode(){

        return payResultCode;
    }

    public void setPayResultCode(String payResultCode){

        this.payResultCode = payResultCode;
    }

    public String getAuthResultMsg(){

        return authResultMsg;
    }

    public void setAuthResultMsg(String authResultMsg){

        this.authResultMsg = authResultMsg;
    }

    public String getPayInfo(){

        return payInfo;
    }

    public void setPayInfo(String payInfo){

        this.payInfo = payInfo;
    }

    public String getPayMethod(){

        return payMethod;
    }

    public void setPayMethod(String payMethod){

        this.payMethod = payMethod;
    }

    public PaymentProperties getProperties(){

        return properties;
    }

    public void setProperties(PaymentProperties properties){

        this.properties = properties;
    }

    @Override
    public String toString(){

        return "Payment{" + "no=" + no + ", status='" + status + '\'' + ", expireDate='" + expireDate + '\'' + ", payTransactionId='" + payTransactionId + '\'' + ", authTransactionId='" + authTransactionId + '\'' + ", payResultCode='" + payResultCode + '\'' + ", authResultMsg='" + authResultMsg + '\'' + ", payInfo='" + payInfo + '\'' + ", payMethod='" + payMethod + '\'' + ", properties=" + properties + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.status);
        dest.writeString(this.expireDate);
        dest.writeString(this.payTransactionId);
        dest.writeString(this.authTransactionId);
        dest.writeString(this.payResultCode);
        dest.writeString(this.authResultMsg);
        dest.writeString(this.payInfo);
        dest.writeString(this.payMethod);
        dest.writeParcelable(this.properties, flags);
    }

    public Payment(){

    }

    protected Payment(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.status = in.readString();
        this.expireDate = in.readString();
        this.payTransactionId = in.readString();
        this.authTransactionId = in.readString();
        this.payResultCode = in.readString();
        this.authResultMsg = in.readString();
        this.payInfo = in.readString();
        this.payMethod = in.readString();
        this.properties = in.readParcelable(PaymentProperties.class.getClassLoader());
    }

    public static final Creator<Payment> CREATOR = new Creator<Payment>(){

        @Override
        public Payment createFromParcel(Parcel source){

            return new Payment(source);
        }

        @Override
        public Payment[] newArray(int size){

            return new Payment[size];
        }
    };
}
