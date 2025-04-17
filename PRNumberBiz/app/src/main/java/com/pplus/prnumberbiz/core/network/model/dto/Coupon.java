package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 9. 6..
 */

public class Coupon implements Parcelable{
    private Long no;
    private No receiver;
    private CouponTemplate template;
    private String code;
    private int useCount;
    private String method;
    private String useDate;
    private String regDate;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public No getReceiver(){

        return receiver;
    }

    public void setReceiver(No receiver){

        this.receiver = receiver;
    }

    public CouponTemplate getTemplate(){

        return template;
    }

    public void setTemplate(CouponTemplate template){

        this.template = template;
    }

    public String getCode(){

        return code;
    }

    public void setCode(String code){

        this.code = code;
    }

    public int getUseCount(){

        return useCount;
    }

    public void setUseCount(int useCount){

        this.useCount = useCount;
    }

    public String getMethod(){

        return method;
    }

    public void setMethod(String method){

        this.method = method;
    }

    public String getUseDate(){

        return useDate;
    }

    public void setUseDate(String useDate){

        this.useDate = useDate;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    @Override
    public String toString(){

        return "Coupon{" + "no=" + no + ", receiver=" + receiver + ", template=" + template + ", code='" + code + '\'' + ", useCount=" + useCount + ", method='" + method + '\'' + ", useDate='" + useDate + '\'' + ", regDate='" + regDate + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeParcelable(this.receiver, flags);
        dest.writeParcelable(this.template, flags);
        dest.writeString(this.code);
        dest.writeInt(this.useCount);
        dest.writeString(this.method);
        dest.writeString(this.useDate);
        dest.writeString(this.regDate);
    }

    public Coupon(){

    }

    protected Coupon(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.receiver = in.readParcelable(No.class.getClassLoader());
        this.template = in.readParcelable(CouponTemplate.class.getClassLoader());
        this.code = in.readString();
        this.useCount = in.readInt();
        this.method = in.readString();
        this.useDate = in.readString();
        this.regDate = in.readString();
    }

    public static final Parcelable.Creator<Coupon> CREATOR = new Parcelable.Creator<Coupon>(){

        @Override
        public Coupon createFromParcel(Parcel source){

            return new Coupon(source);
        }

        @Override
        public Coupon[] newArray(int size){

            return new Coupon[size];
        }
    };
}
