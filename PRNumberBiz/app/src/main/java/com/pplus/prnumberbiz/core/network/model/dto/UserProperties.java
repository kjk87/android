package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 10. 19..
 */

public class UserProperties implements Parcelable{
    private boolean existsNewNote;
    private int newNoteCount;
    private int newMsgCount;

    @Override
    public String toString(){

        return "UserProperties{" + "existsNewNote=" + existsNewNote + ", newNoteCount=" + newNoteCount + ", newMsgCount=" + newMsgCount + '}';
    }

    public boolean isExistsNewNote(){

        return existsNewNote;
    }

    public void setExistsNewNote(boolean existsNewNote){

        this.existsNewNote = existsNewNote;
    }

    public int getNewNoteCount(){

        return newNoteCount;
    }

    public void setNewNoteCount(int newNoteCount){

        this.newNoteCount = newNoteCount;
    }

    public int getNewMsgCount(){

        return newMsgCount;
    }

    public void setNewMsgCount(int newMsgCount){

        this.newMsgCount = newMsgCount;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeByte(this.existsNewNote ? (byte) 1 : (byte) 0);
        dest.writeInt(this.newNoteCount);
        dest.writeInt(this.newMsgCount);
    }

    public UserProperties(){

    }

    protected UserProperties(Parcel in){

        this.existsNewNote = in.readByte() != 0;
        this.newNoteCount = in.readInt();
        this.newMsgCount = in.readInt();
    }

    public static final Creator<UserProperties> CREATOR = new Creator<UserProperties>(){

        @Override
        public UserProperties createFromParcel(Parcel source){

            return new UserProperties(source);
        }

        @Override
        public UserProperties[] newArray(int size){

            return new UserProperties[size];
        }
    };
}
