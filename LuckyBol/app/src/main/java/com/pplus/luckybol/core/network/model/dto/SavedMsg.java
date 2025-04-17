package com.pplus.luckybol.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 8..
 */

public class SavedMsg implements Parcelable{
    private Long no;
    private No user;
    private Integer priority;
    private SavedMsgProperties properties;
    private String regDate;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public No getUser(){

        return user;
    }

    public void setUser(No user){

        this.user = user;
    }

    public Integer getPriority(){

        return priority;
    }

    public void setPriority(Integer priority){

        this.priority = priority;
    }

    public SavedMsgProperties getProperties(){

        return properties;
    }

    public void setProperties(SavedMsgProperties properties){

        this.properties = properties;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    @Override
    public String toString(){

        return "SavedMsg{" + "no=" + no + ", user=" + user + ", priority=" + priority + ", properties=" + properties + ", regDate='" + regDate + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeParcelable(this.user, flags);
        dest.writeValue(this.priority);
        dest.writeParcelable(this.properties, flags);
        dest.writeString(this.regDate);
    }

    public SavedMsg(){

    }

    protected SavedMsg(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.user = in.readParcelable(No.class.getClassLoader());
        this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
        this.properties = in.readParcelable(SavedMsgProperties.class.getClassLoader());
        this.regDate = in.readString();
    }

    public static final Creator<SavedMsg> CREATOR = new Creator<SavedMsg>(){

        @Override
        public SavedMsg createFromParcel(Parcel source){

            return new SavedMsg(source);
        }

        @Override
        public SavedMsg[] newArray(int size){

            return new SavedMsg[size];
        }
    };
}
