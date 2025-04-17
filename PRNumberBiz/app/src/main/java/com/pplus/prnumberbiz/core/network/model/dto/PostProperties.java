package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kjk on 2017. 6. 20..
 */

public class PostProperties implements Parcelable{
    private String luckyBol;
    private String starPoint;
    private String email;

    @Override
    public String toString(){

        return "PostProperties{" + "luckyBol='" + luckyBol + '\'' + ", starPoint='" + starPoint + '\'' + ", email='" + email + '\'' + '}';
    }

    public String getLuckyBol(){

        return luckyBol;
    }

    public void setLuckyBol(String luckyBol){

        this.luckyBol = luckyBol;
    }

    public String getStarPoint(){

        return starPoint;
    }

    public void setStarPoint(String starPoint){

        this.starPoint = starPoint;
    }

    public String getEmail(){

        return email;
    }

    public void setEmail(String email){

        this.email = email;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.luckyBol);
        dest.writeString(this.starPoint);
        dest.writeString(this.email);
    }

    public PostProperties(){

    }

    protected PostProperties(Parcel in){

        this.luckyBol = in.readString();
        this.starPoint = in.readString();
        this.email = in.readString();
    }

    public static final Creator<PostProperties> CREATOR = new Creator<PostProperties>(){

        @Override
        public PostProperties createFromParcel(Parcel source){

            return new PostProperties(source);
        }

        @Override
        public PostProperties[] newArray(int size){

            return new PostProperties[size];
        }
    };
}
