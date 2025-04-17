package com.pplus.prnumberuser.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 김종경 on 2016-10-04.
 */

public class Duration implements Parcelable{
    private String start;
    private String end;

    public String getStart(){

        return start;
    }

    public void setStart(String start){

        this.start = start;
    }

    public String getEnd(){

        return end;
    }

    public void setEnd(String end){

        this.end = end;
    }

    @Override
    public String toString(){

        return "Duration{" + "start='" + start + '\'' + ", end='" + end + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.start);
        dest.writeString(this.end);
    }

    public Duration(){

    }

    protected Duration(Parcel in){

        this.start = in.readString();
        this.end = in.readString();
    }

    public static final Creator<Duration> CREATOR = new Creator<Duration>(){

        @Override
        public Duration createFromParcel(Parcel source){

            return new Duration(source);
        }

        @Override
        public Duration[] newArray(int size){

            return new Duration[size];
        }
    };
}
