package com.pplus.luckybol.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 6. 29..
 */

public class ResultAddress implements Parcelable{
    private SearchAddress results;

    public SearchAddress getResults(){

        return results;
    }

    public void setResults(SearchAddress results){

        this.results = results;
    }

    @Override
    public String toString(){

        return "ResultAddress{" + "results=" + results + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeParcelable(this.results, flags);
    }

    public ResultAddress(){

    }

    protected ResultAddress(Parcel in){

        this.results = in.readParcelable(SearchAddress.class.getClassLoader());
    }

    public static final Creator<ResultAddress> CREATOR = new Creator<ResultAddress>(){

        @Override
        public ResultAddress createFromParcel(Parcel source){

            return new ResultAddress(source);
        }

        @Override
        public ResultAddress[] newArray(int size){

            return new ResultAddress[size];
        }
    };
}
