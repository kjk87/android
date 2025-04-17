package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 28..
 */

public class BolProperties implements Parcelable{

    private String from;
    private String message;

    @Override
    public String toString(){

        return "BolProperties{" + "from='" + from + '\'' + ", message='" + message + '\'' + '}';
    }

    public String getFrom(){

        return from;
    }

    public void setFrom(String from){

        this.from = from;
    }

    public String getMessage(){

        return message;
    }

    public void setMessage(String message){

        this.message = message;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.from);
        dest.writeString(this.message);
    }

    public BolProperties(){

    }

    protected BolProperties(Parcel in){

        this.from = in.readString();
        this.message = in.readString();
    }

    public static final Creator<BolProperties> CREATOR = new Creator<BolProperties>(){

        @Override
        public BolProperties createFromParcel(Parcel source){

            return new BolProperties(source);
        }

        @Override
        public BolProperties[] newArray(int size){

            return new BolProperties[size];
        }
    };
}
