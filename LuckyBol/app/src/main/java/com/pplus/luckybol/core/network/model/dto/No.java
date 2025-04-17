package com.pplus.luckybol.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kjk on 2017. 6. 20..
 */

public class No implements Parcelable{

    private Long no;

    public No(){

    }

    public No(Long no){

        this.no = no;
    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    @Override
    public String toString(){

        return "No{" + "no=" + no + '}';
    }


    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
    }

    protected No(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<No> CREATOR = new Creator<No>(){

        @Override
        public No createFromParcel(Parcel source){

            return new No(source);
        }

        @Override
        public No[] newArray(int size){

            return new No[size];
        }
    };
}
