package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksh on 2016-11-23.
 */

public class MessageDomain implements Parcelable{
    private long created_at; // 메시지 받은 시간

    private String type; // 메시지나 file이나의 값

    private String message; // 마지막 메시지

    public long getCreated_at(){

        return created_at;
    }

    public void setCreated_at(long created_at){

        this.created_at = created_at;
    }

    public String getType(){

        return type;
    }

    public void setType(String type){

        this.type = type;
    }

    public String getMessage(){

        return message;
    }

    public void setMessage(String message){

        this.message = message;
    }

    @Override
    public String toString(){

        return "MessageDomain{" +
                "created_at=" + created_at +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeLong(this.created_at);
        dest.writeString(this.type);
        dest.writeString(this.message);
    }

    public MessageDomain(){

    }

    protected MessageDomain(Parcel in){

        this.created_at = in.readLong();
        this.type = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<MessageDomain> CREATOR = new Parcelable.Creator<MessageDomain>(){

        @Override
        public MessageDomain createFromParcel(Parcel source){

            return new MessageDomain(source);
        }

        @Override
        public MessageDomain[] newArray(int size){

            return new MessageDomain[size];
        }
    };
}
