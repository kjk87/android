package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 7. 5..
 */

public class Verification implements Parcelable{
    private String token;
    private String media;
    private String number;
    private String type;
    private String regDate;
    private String email;

    public String getToken(){

        return token;
    }

    public void setToken(String token){

        this.token = token;
    }

    public String getMedia(){

        return media;
    }

    public void setMedia(String media){

        this.media = media;
    }

    public String getNumber(){

        return number;
    }

    public void setNumber(String number){

        this.number = number;
    }

    public String getType(){

        return type;
    }

    public void setType(String type){

        this.type = type;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    public String getEmail(){

        return email;
    }

    public void setEmail(String email){

        this.email = email;
    }

    @Override
    public String toString(){

        return "Verification{" + "token='" + token + '\'' + ", media='" + media + '\'' + ", number='" + number + '\'' + ", type='" + type + '\'' + ", regDate='" + regDate + '\'' + ", email='" + email + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.token);
        dest.writeString(this.media);
        dest.writeString(this.number);
        dest.writeString(this.type);
        dest.writeString(this.regDate);
        dest.writeString(this.email);
    }

    public Verification(){

    }

    protected Verification(Parcel in){

        this.token = in.readString();
        this.media = in.readString();
        this.number = in.readString();
        this.type = in.readString();
        this.regDate = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<Verification> CREATOR = new Parcelable.Creator<Verification>(){

        @Override
        public Verification createFromParcel(Parcel source){

            return new Verification(source);
        }

        @Override
        public Verification[] newArray(int size){

            return new Verification[size];
        }
    };
}
