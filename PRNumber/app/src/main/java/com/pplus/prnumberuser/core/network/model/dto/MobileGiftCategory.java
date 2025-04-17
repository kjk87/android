package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 10. 27..
 */

public class MobileGiftCategory implements Parcelable{
    private Long no;
    private String name;
    private Long hierarchy;

    @Override
    public String toString(){

        return "MobileGiftCategory{" + "no=" + no + ", name='" + name + '\'' + ", hierarchy=" + hierarchy + '}';
    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    public Long getHierarchy(){

        return hierarchy;
    }

    public void setHierarchy(Long hierarchy){

        this.hierarchy = hierarchy;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.name);
        dest.writeValue(this.hierarchy);
    }

    public MobileGiftCategory(){

    }

    protected MobileGiftCategory(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.hierarchy = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<MobileGiftCategory> CREATOR = new Parcelable.Creator<MobileGiftCategory>(){

        @Override
        public MobileGiftCategory createFromParcel(Parcel source){

            return new MobileGiftCategory(source);
        }

        @Override
        public MobileGiftCategory[] newArray(int size){

            return new MobileGiftCategory[size];
        }
    };
}
