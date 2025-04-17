package com.pplus.luckybol.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 9. 28..
 */

public class NoteReceive implements Parcelable{
    private Long no;
    private String contents;
    private String regDate;
    private boolean readed;
    private String readDate;
    private Long replyNo;
    private User author;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getContents(){

        return contents;
    }

    public void setContents(String contents){

        this.contents = contents;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    public boolean isReaded(){

        return readed;
    }

    public void setReaded(boolean readed){

        this.readed = readed;
    }

    public String getReadDate(){

        return readDate;
    }

    public void setReadDate(String readDate){

        this.readDate = readDate;
    }

    public Long getReplyNo(){

        return replyNo;
    }

    public void setReplyNo(Long replyNo){

        this.replyNo = replyNo;
    }

    public User getAuthor(){

        return author;
    }

    public void setAuthor(User author){

        this.author = author;
    }

    @Override
    public String toString(){

        return "NoteReceive{" + "no=" + no + ", contents='" + contents + '\'' + ", regDate='" + regDate + '\'' + ", readed=" + readed + ", readDate='" + readDate + '\'' + ", replyNo=" + replyNo + ", author=" + author + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.contents);
        dest.writeString(this.regDate);
        dest.writeByte(this.readed ? (byte) 1 : (byte) 0);
        dest.writeString(this.readDate);
        dest.writeValue(this.replyNo);
        dest.writeParcelable(this.author, flags);
    }

    public NoteReceive(){

    }

    protected NoteReceive(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.contents = in.readString();
        this.regDate = in.readString();
        this.readed = in.readByte() != 0;
        this.readDate = in.readString();
        this.replyNo = (Long) in.readValue(Long.class.getClassLoader());
        this.author = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<NoteReceive> CREATOR = new Creator<NoteReceive>(){

        @Override
        public NoteReceive createFromParcel(Parcel source){

            return new NoteReceive(source);
        }

        @Override
        public NoteReceive[] newArray(int size){

            return new NoteReceive[size];
        }
    };
}
