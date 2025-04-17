package com.pplus.luckybol.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 11. 22..
 */

public class MobileGiftHistoryProperties implements Parcelable{
    private String use;
    private String title;

    public String getUse(){

        return use;
    }

    public void setUse(String use){

        this.use = use;
    }

    public String getTitle(){

        return title;
    }

    public void setTitle(String title){

        this.title = title;
    }

    @Override
    public String toString(){

        return "MobileGiftHistoryProperties{" + "use='" + use + '\'' + ", title='" + title + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.use);
        dest.writeString(this.title);
    }

    public MobileGiftHistoryProperties(){

    }

    protected MobileGiftHistoryProperties(Parcel in){

        this.use = in.readString();
        this.title = in.readString();
    }

    public static final Creator<MobileGiftHistoryProperties> CREATOR = new Creator<MobileGiftHistoryProperties>(){

        @Override
        public MobileGiftHistoryProperties createFromParcel(Parcel source){

            return new MobileGiftHistoryProperties(source);
        }

        @Override
        public MobileGiftHistoryProperties[] newArray(int size){

            return new MobileGiftHistoryProperties[size];
        }
    };
}
