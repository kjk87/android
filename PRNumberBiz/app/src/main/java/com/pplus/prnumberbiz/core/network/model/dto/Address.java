package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 김종경 on 2016-10-04.
 */

public class Address implements Parcelable{

    private String zipCode;
    private String roadBase;
    private String roadDetail;
    private String parcelBase;
    private String parcelDetail;

    public String getZipCode(){

        return zipCode;
    }

    public void setZipCode(String zipCode){

        this.zipCode = zipCode;
    }

    public String getRoadBase(){

        return roadBase;
    }

    public void setRoadBase(String roadBase){

        this.roadBase = roadBase;
    }

    public String getRoadDetail(){

        return roadDetail;
    }

    public void setRoadDetail(String roadDetail){

        this.roadDetail = roadDetail;
    }

    public String getParcelBase(){

        return parcelBase;
    }

    public void setParcelBase(String parcelBase){

        this.parcelBase = parcelBase;
    }

    public String getParcelDetail(){

        return parcelDetail;
    }

    public void setParcelDetail(String parcelDetail){

        this.parcelDetail = parcelDetail;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.zipCode);
        dest.writeString(this.roadBase);
        dest.writeString(this.roadDetail);
        dest.writeString(this.parcelBase);
        dest.writeString(this.parcelDetail);
    }

    public Address(){

    }

    protected Address(Parcel in){

        this.zipCode = in.readString();
        this.roadBase = in.readString();
        this.roadDetail = in.readString();
        this.parcelBase = in.readString();
        this.parcelDetail = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>(){

        @Override
        public Address createFromParcel(Parcel source){

            return new Address(source);
        }

        @Override
        public Address[] newArray(int size){

            return new Address[size];
        }
    };
}
