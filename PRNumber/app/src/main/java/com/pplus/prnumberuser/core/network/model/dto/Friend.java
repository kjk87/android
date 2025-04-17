package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 31..
 */

public class Friend implements Parcelable{
    private String mobile;
    private User friend;
    private boolean existsPageInContact;

    public String getMobile(){

        return mobile;
    }

    public void setMobile(String mobile){

        this.mobile = mobile;
    }

    public User getFriend(){

        return friend;
    }

    public void setFriend(User friend){

        this.friend = friend;
    }

    public boolean isExistsPageInContact(){

        return existsPageInContact;
    }

    public void setExistsPageInContact(boolean existsPageInContact){

        this.existsPageInContact = existsPageInContact;
    }

    @Override
    public String toString(){

        return "Friend{" + "mobile='" + mobile + '\'' + ", friend=" + friend + ", existsPageInContact=" + existsPageInContact + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.mobile);
        dest.writeParcelable(this.friend, flags);
        dest.writeByte(this.existsPageInContact ? (byte) 1 : (byte) 0);
    }

    public Friend(){

    }

    protected Friend(Parcel in){

        this.mobile = in.readString();
        this.friend = in.readParcelable(User.class.getClassLoader());
        this.existsPageInContact = in.readByte() != 0;
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>(){

        @Override
        public Friend createFromParcel(Parcel source){

            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size){

            return new Friend[size];
        }
    };
}
