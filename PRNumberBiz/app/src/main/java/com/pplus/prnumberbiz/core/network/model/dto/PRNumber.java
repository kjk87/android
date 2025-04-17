package com.pplus.prnumberbiz.core.network.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by j2n on 2016. 7. 26..
 */

@SuppressLint("ParcelCreator")
public class PRNumber implements Parcelable{
    private String number;
    private String type;
    private String reserved;
    private Duration duration;
    private String openBound;
    private String actSrc;
    private User actor;
    private String actDate;
    private String note;
    private No batch;
    private No country;
    private String reservedTitle;
    private String reservedReason;
    private String reservedDesc;
    private String reservedDate;

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

    public String getReserved(){

        return reserved;
    }

    public void setReserved(String reserved){

        this.reserved = reserved;
    }

    public Duration getDuration(){

        return duration;
    }

    public void setDuration(Duration duration){

        this.duration = duration;
    }

    public String getOpenBound(){

        return openBound;
    }

    public void setOpenBound(String openBound){

        this.openBound = openBound;
    }

    public String getActSrc(){

        return actSrc;
    }

    public void setActSrc(String actSrc){

        this.actSrc = actSrc;
    }

    public User getActor(){

        return actor;
    }

    public void setActor(User actor){

        this.actor = actor;
    }

    public String getActDate(){

        return actDate;
    }

    public void setActDate(String actDate){

        this.actDate = actDate;
    }

    public String getNote(){

        return note;
    }

    public void setNote(String note){

        this.note = note;
    }

    public No getBatch(){

        return batch;
    }

    public void setBatch(No batch){

        this.batch = batch;
    }

    public No getCountry(){

        return country;
    }

    public void setCountry(No country){

        this.country = country;
    }

    public String getReservedTitle(){

        return reservedTitle;
    }

    public void setReservedTitle(String reservedTitle){

        this.reservedTitle = reservedTitle;
    }

    public String getReservedReason(){

        return reservedReason;
    }

    public void setReservedReason(String reservedReason){

        this.reservedReason = reservedReason;
    }

    public String getReservedDesc(){

        return reservedDesc;
    }

    public void setReservedDesc(String reservedDesc){

        this.reservedDesc = reservedDesc;
    }

    public String getReservedDate(){

        return reservedDate;
    }

    public void setReservedDate(String reservedDate){

        this.reservedDate = reservedDate;
    }

    @Override
    public String toString(){

        return "PRNumber{" + "number='" + number + '\'' + ", type='" + type + '\'' + ", reserved='" + reserved + '\'' + ", duration=" + duration + ", openBound='" + openBound + '\'' + ", actSrc='" + actSrc + '\'' + ", actor=" + actor + ", actDate='" + actDate + '\'' + ", note='" + note + '\'' + ", batch=" + batch + ", country=" + country + ", reservedTitle='" + reservedTitle + '\'' + ", reservedReason='" + reservedReason + '\'' + ", reservedDesc='" + reservedDesc + '\'' + ", reservedDate='" + reservedDate + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.number);
        dest.writeString(this.type);
        dest.writeString(this.reserved);
        dest.writeParcelable(this.duration, flags);
        dest.writeString(this.openBound);
        dest.writeString(this.actSrc);
        dest.writeParcelable(this.actor, flags);
        dest.writeString(this.actDate);
        dest.writeString(this.note);
        dest.writeParcelable(this.batch, flags);
        dest.writeParcelable(this.country, flags);
        dest.writeString(this.reservedTitle);
        dest.writeString(this.reservedReason);
        dest.writeString(this.reservedDesc);
        dest.writeString(this.reservedDate);
    }

    public PRNumber(){

    }

    protected PRNumber(Parcel in){

        this.number = in.readString();
        this.type = in.readString();
        this.reserved = in.readString();
        this.duration = in.readParcelable(Duration.class.getClassLoader());
        this.openBound = in.readString();
        this.actSrc = in.readString();
        this.actor = in.readParcelable(User.class.getClassLoader());
        this.actDate = in.readString();
        this.note = in.readString();
        this.batch = in.readParcelable(No.class.getClassLoader());
        this.country = in.readParcelable(No.class.getClassLoader());
        this.reservedTitle = in.readString();
        this.reservedReason = in.readString();
        this.reservedDesc = in.readString();
        this.reservedDate = in.readString();
    }

    public static final Parcelable.Creator<PRNumber> CREATOR = new Parcelable.Creator<PRNumber>(){

        @Override
        public PRNumber createFromParcel(Parcel source){

            return new PRNumber(source);
        }

        @Override
        public PRNumber[] newArray(int size){

            return new PRNumber[size];
        }
    };
}
