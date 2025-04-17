package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by imac on 2017. 9. 7..
 */

public class SystemBoard implements Parcelable{
    private List<Post> cashList;

    public List<Post> getCashList(){

        return cashList;
    }

    public void setCashList(List<Post> cashList){

        this.cashList = cashList;
    }

    @Override
    public String toString(){

        return "SystemBoard{" + "cashList=" + cashList + '}';
    }


    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeTypedList(this.cashList);
    }

    public SystemBoard(){

    }

    protected SystemBoard(Parcel in){

        this.cashList = in.createTypedArrayList(Post.CREATOR);
    }

    public static final Parcelable.Creator<SystemBoard> CREATOR = new Parcelable.Creator<SystemBoard>(){

        @Override
        public SystemBoard createFromParcel(Parcel source){

            return new SystemBoard(source);
        }

        @Override
        public SystemBoard[] newArray(int size){

            return new SystemBoard[size];
        }
    };
}
