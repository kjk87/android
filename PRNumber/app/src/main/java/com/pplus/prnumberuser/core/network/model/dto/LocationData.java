package com.pplus.prnumberuser.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 김종경 on 2016-10-06.
 */

public class LocationData implements Parcelable{

    private Double latitude;
    private Double longitude;
    private String address;

    public Double getLatitude(){

        return latitude;
    }

    public void setLatitude(Double latitude){

        this.latitude = latitude;
    }

    public Double getLongitude(){

        return longitude;
    }

    public void setLongitude(Double longitude){

        this.longitude = longitude;
    }

    public String getAddress(){

        return address;
    }

    public void setAddress(String address){

        this.address = address;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeString(this.address);
    }

    public LocationData(){

    }

    protected LocationData(Parcel in){

        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.address = in.readString();
    }

    public static final Creator<LocationData> CREATOR = new Creator<LocationData>(){

        @Override
        public LocationData createFromParcel(Parcel source){

            return new LocationData(source);
        }

        @Override
        public LocationData[] newArray(int size){

            return new LocationData[size];
        }
    };
}
