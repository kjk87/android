package com.pplus.luckybol.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by imac on 2017. 9. 28..
 */

public class NoteSend implements Parcelable{
    private Long no;
    private String contents;
    private String regDate;
    private Long originNo;
    private User mainReceiver;
    private Integer receiverCount;
    private List<No> receiverList;

    @Override
    public String toString(){

        return "NoteSend{" + "no=" + no + ", contents='" + contents + '\'' + ", regDate='" + regDate + '\'' + ", originNo=" + originNo + ", mainReceiver=" + mainReceiver + ", receiverCount=" + receiverCount + ", receiverList=" + receiverList + '}';
    }

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

    public Long getOriginNo(){

        return originNo;
    }

    public void setOriginNo(Long originNo){

        this.originNo = originNo;
    }

    public User getMainReceiver(){

        return mainReceiver;
    }

    public void setMainReceiver(User mainReceiver){

        this.mainReceiver = mainReceiver;
    }

    public Integer getReceiverCount(){

        return receiverCount;
    }

    public void setReceiverCount(Integer receiverCount){

        this.receiverCount = receiverCount;
    }

    public List<No> getReceiverList(){

        return receiverList;
    }

    public void setReceiverList(List<No> receiverList){

        this.receiverList = receiverList;
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
        dest.writeValue(this.originNo);
        dest.writeParcelable(this.mainReceiver, flags);
        dest.writeValue(this.receiverCount);
        dest.writeTypedList(this.receiverList);
    }

    public NoteSend(){

    }

    protected NoteSend(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.contents = in.readString();
        this.regDate = in.readString();
        this.originNo = (Long) in.readValue(Long.class.getClassLoader());
        this.mainReceiver = in.readParcelable(User.class.getClassLoader());
        this.receiverCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.receiverList = in.createTypedArrayList(No.CREATOR);
    }

    public static final Creator<NoteSend> CREATOR = new Creator<NoteSend>(){

        @Override
        public NoteSend createFromParcel(Parcel source){

            return new NoteSend(source);
        }

        @Override
        public NoteSend[] newArray(int size){

            return new NoteSend[size];
        }
    };
}
