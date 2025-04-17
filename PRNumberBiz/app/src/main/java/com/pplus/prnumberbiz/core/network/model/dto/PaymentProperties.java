package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 7. 27..
 */

public class PaymentProperties implements Parcelable{

    private String P_VACT_NUM;
    private String P_VACT_BANK_CODE;
    private String P_VACT_BANK_NAME;
    private String P_VACT_NAME;
    private String P_NOTI;
    private String P_OID;
    private String P_NOTEURL;
    private String P_AMT;
    private String P_MID;
    private String P_NEXT_URL;
    private String P_TYPE;
    private String P_AUTH_DT;
    private String P_STATUS;
    private String P_FN_CD1;
    private String P_UNAME;
    private String P_TID;
    private String P_VACT_DATE;
    private String P_MNAME;
    private String P_RMESG2;
    private String P_RMESG1;
    private String P_AUTH_NO;
    private String P_VACT_TIME;

    public String getP_VACT_NUM(){

        return P_VACT_NUM;
    }

    public void setP_VACT_NUM(String p_VACT_NUM){

        P_VACT_NUM = p_VACT_NUM;
    }

    public String getP_VACT_BANK_CODE(){

        return P_VACT_BANK_CODE;
    }

    public void setP_VACT_BANK_CODE(String p_VACT_BANK_CODE){

        P_VACT_BANK_CODE = p_VACT_BANK_CODE;
    }

    public String getP_VACT_BANK_NAME(){

        return P_VACT_BANK_NAME;
    }

    public void setP_VACT_BANK_NAME(String p_VACT_BANK_NAME){

        P_VACT_BANK_NAME = p_VACT_BANK_NAME;
    }

    public String getP_VACT_NAME(){

        return P_VACT_NAME;
    }

    public void setP_VACT_NAME(String p_VACT_NAME){

        P_VACT_NAME = p_VACT_NAME;
    }

    public String getP_NOTI(){

        return P_NOTI;
    }

    public void setP_NOTI(String p_NOTI){

        P_NOTI = p_NOTI;
    }

    public String getP_OID(){

        return P_OID;
    }

    public void setP_OID(String p_OID){

        P_OID = p_OID;
    }

    public String getP_NOTEURL(){

        return P_NOTEURL;
    }

    public void setP_NOTEURL(String p_NOTEURL){

        P_NOTEURL = p_NOTEURL;
    }

    public String getP_AMT(){

        return P_AMT;
    }

    public void setP_AMT(String p_AMT){

        P_AMT = p_AMT;
    }

    public String getP_MID(){

        return P_MID;
    }

    public void setP_MID(String p_MID){

        P_MID = p_MID;
    }

    public String getP_NEXT_URL(){

        return P_NEXT_URL;
    }

    public void setP_NEXT_URL(String p_NEXT_URL){

        P_NEXT_URL = p_NEXT_URL;
    }

    public String getP_TYPE(){

        return P_TYPE;
    }

    public void setP_TYPE(String p_TYPE){

        P_TYPE = p_TYPE;
    }

    public String getP_AUTH_DT(){

        return P_AUTH_DT;
    }

    public void setP_AUTH_DT(String p_AUTH_DT){

        P_AUTH_DT = p_AUTH_DT;
    }

    public String getP_STATUS(){

        return P_STATUS;
    }

    public void setP_STATUS(String p_STATUS){

        P_STATUS = p_STATUS;
    }

    public String getP_FN_CD1(){

        return P_FN_CD1;
    }

    public void setP_FN_CD1(String p_FN_CD1){

        P_FN_CD1 = p_FN_CD1;
    }

    public String getP_UNAME(){

        return P_UNAME;
    }

    public void setP_UNAME(String p_UNAME){

        P_UNAME = p_UNAME;
    }

    public String getP_TID(){

        return P_TID;
    }

    public void setP_TID(String p_TID){

        P_TID = p_TID;
    }

    public String getP_VACT_DATE(){

        return P_VACT_DATE;
    }

    public void setP_VACT_DATE(String p_VACT_DATE){

        P_VACT_DATE = p_VACT_DATE;
    }

    public String getP_MNAME(){

        return P_MNAME;
    }

    public void setP_MNAME(String p_MNAME){

        P_MNAME = p_MNAME;
    }

    public String getP_RMESG2(){

        return P_RMESG2;
    }

    public void setP_RMESG2(String p_RMESG2){

        P_RMESG2 = p_RMESG2;
    }

    public String getP_RMESG1(){

        return P_RMESG1;
    }

    public void setP_RMESG1(String p_RMESG1){

        P_RMESG1 = p_RMESG1;
    }

    public String getP_AUTH_NO(){

        return P_AUTH_NO;
    }

    public void setP_AUTH_NO(String p_AUTH_NO){

        P_AUTH_NO = p_AUTH_NO;
    }

    public String getP_VACT_TIME(){

        return P_VACT_TIME;
    }

    public void setP_VACT_TIME(String p_VACT_TIME){

        P_VACT_TIME = p_VACT_TIME;
    }

    @Override
    public String toString(){

        return "Properties{" + "P_VACT_NUM='" + P_VACT_NUM + '\'' + ", P_VACT_BANK_CODE='" + P_VACT_BANK_CODE + '\'' + ", P_VACT_BANK_NAME='" + P_VACT_BANK_NAME + '\'' + ", P_VACT_NAME='" + P_VACT_NAME + '\'' + ", P_NOTI='" + P_NOTI + '\'' + ", P_OID='" + P_OID + '\'' + ", P_NOTEURL='" + P_NOTEURL + '\'' + ", P_AMT='" + P_AMT + '\'' + ", P_MID='" + P_MID + '\'' + ", P_NEXT_URL='" + P_NEXT_URL + '\'' + ", P_TYPE='" + P_TYPE + '\'' + ", P_AUTH_DT='" + P_AUTH_DT + '\'' + ", P_STATUS='" + P_STATUS + '\'' + ", P_FN_CD1='" + P_FN_CD1 + '\'' + ", P_UNAME='" + P_UNAME + '\'' + ", P_TID='" + P_TID + '\'' + ", P_VACT_DATE='" + P_VACT_DATE + '\'' + ", P_MNAME='" + P_MNAME + '\'' + ", P_RMESG2='" + P_RMESG2 + '\'' + ", P_RMESG1='" + P_RMESG1 + '\'' + ", P_AUTH_NO='" + P_AUTH_NO + '\'' + ", P_VACT_TIME='" + P_VACT_TIME + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.P_VACT_NUM);
        dest.writeString(this.P_VACT_BANK_CODE);
        dest.writeString(this.P_VACT_BANK_NAME);
        dest.writeString(this.P_VACT_NAME);
        dest.writeString(this.P_NOTI);
        dest.writeString(this.P_OID);
        dest.writeString(this.P_NOTEURL);
        dest.writeString(this.P_AMT);
        dest.writeString(this.P_MID);
        dest.writeString(this.P_NEXT_URL);
        dest.writeString(this.P_TYPE);
        dest.writeString(this.P_AUTH_DT);
        dest.writeString(this.P_STATUS);
        dest.writeString(this.P_FN_CD1);
        dest.writeString(this.P_UNAME);
        dest.writeString(this.P_TID);
        dest.writeString(this.P_VACT_DATE);
        dest.writeString(this.P_MNAME);
        dest.writeString(this.P_RMESG2);
        dest.writeString(this.P_RMESG1);
        dest.writeString(this.P_AUTH_NO);
        dest.writeString(this.P_VACT_TIME);
    }

    public PaymentProperties(){

    }

    protected PaymentProperties(Parcel in){

        this.P_VACT_NUM = in.readString();
        this.P_VACT_BANK_CODE = in.readString();
        this.P_VACT_BANK_NAME = in.readString();
        this.P_VACT_NAME = in.readString();
        this.P_NOTI = in.readString();
        this.P_OID = in.readString();
        this.P_NOTEURL = in.readString();
        this.P_AMT = in.readString();
        this.P_MID = in.readString();
        this.P_NEXT_URL = in.readString();
        this.P_TYPE = in.readString();
        this.P_AUTH_DT = in.readString();
        this.P_STATUS = in.readString();
        this.P_FN_CD1 = in.readString();
        this.P_UNAME = in.readString();
        this.P_TID = in.readString();
        this.P_VACT_DATE = in.readString();
        this.P_MNAME = in.readString();
        this.P_RMESG2 = in.readString();
        this.P_RMESG1 = in.readString();
        this.P_AUTH_NO = in.readString();
        this.P_VACT_TIME = in.readString();
    }

    public static final Creator<PaymentProperties> CREATOR = new Creator<PaymentProperties>(){

        @Override
        public PaymentProperties createFromParcel(Parcel source){

            return new PaymentProperties(source);
        }

        @Override
        public PaymentProperties[] newArray(int size){

            return new PaymentProperties[size];
        }
    };
}
