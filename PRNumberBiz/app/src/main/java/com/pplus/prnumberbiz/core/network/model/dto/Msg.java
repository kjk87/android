package com.pplus.prnumberbiz.core.network.model.dto;



import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by imac on 2017. 8. 8..
 */

public class Msg implements Parcelable{
    private Long no;
    private String type;
    private boolean reserved;
    private String reserveDate;
    private String completeDate;
    private String regDate;
    private String status;
    private String subject;
    private String contents;
    private String moveType1 = "inner";
    private String moveType2;
    private No moveTarget;
    private String moveTargetString;
    private User auth;
    private List<MsgTarget> targetList;
    private MsgProperties properties;
    private int targetCount;
    private int successCount;
    private int failCount;
    private int readCount;
    private int totalPrice;

    @Override
    public String toString(){

        return "Msg{" + "no=" + no + ", type='" + type + '\'' + ", reserved=" + reserved + ", reserveDate='" + reserveDate + '\'' + ", completeDate='" + completeDate + '\'' + ", regDate='" + regDate + '\'' + ", status='" + status + '\'' + ", subject='" + subject + '\'' + ", contents='" + contents + '\'' + ", moveType1='" + moveType1 + '\'' + ", moveType2='" + moveType2 + '\'' + ", moveTarget=" + moveTarget + ", moveTargetString='" + moveTargetString + '\'' + ", auth=" + auth + ", targetList=" + targetList + ", properties=" + properties + ", targetCount=" + targetCount + ", successCount=" + successCount + ", failCount=" + failCount + ", readCount=" + readCount + ", totalPrice=" + totalPrice + '}';
    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getType(){

        return type;
    }

    public void setType(String type){

        this.type = type;
    }

    public boolean isReserved(){

        return reserved;
    }

    public void setReserved(boolean reserved){

        this.reserved = reserved;
    }

    public String getReserveDate(){

        return reserveDate;
    }

    public void setReserveDate(String reserveDate){

        this.reserveDate = reserveDate;
    }

    public String getCompleteDate(){

        return completeDate;
    }

    public void setCompleteDate(String completeDate){

        this.completeDate = completeDate;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public String getSubject(){

        return subject;
    }

    public void setSubject(String subject){

        this.subject = subject;
    }

    public String getContents(){

        return contents;
    }

    public void setContents(String contents){

        this.contents = contents;
    }

    public String getMoveType1(){

        return moveType1;
    }

    public void setMoveType1(String moveType1){

        this.moveType1 = moveType1;
    }

    public String getMoveType2(){

        return moveType2;
    }

    public void setMoveType2(String moveType2){

        this.moveType2 = moveType2;
    }

    public No getMoveTarget(){

        return moveTarget;
    }

    public void setMoveTarget(No moveTarget){

        this.moveTarget = moveTarget;
    }

    public String getMoveTargetString(){

        return moveTargetString;
    }

    public void setMoveTargetString(String moveTargetString){

        this.moveTargetString = moveTargetString;
    }

    public User getAuth(){

        return auth;
    }

    public void setAuth(User auth){

        this.auth = auth;
    }

    public List<MsgTarget> getTargetList(){

        return targetList;
    }

    public void setTargetList(List<MsgTarget> targetList){

        this.targetList = targetList;
    }

    public MsgProperties getProperties(){

        return properties;
    }

    public void setProperties(MsgProperties properties){

        this.properties = properties;
    }

    public int getTargetCount(){

        return targetCount;
    }

    public void setTargetCount(int targetCount){

        this.targetCount = targetCount;
    }

    public int getSuccessCount(){

        return successCount;
    }

    public void setSuccessCount(int successCount){

        this.successCount = successCount;
    }

    public int getFailCount(){

        return failCount;
    }

    public void setFailCount(int failCount){

        this.failCount = failCount;
    }

    public int getReadCount(){

        return readCount;
    }

    public void setReadCount(int readCount){

        this.readCount = readCount;
    }

    public int getTotalPrice(){

        return totalPrice;
    }

    public void setTotalPrice(int totalPrice){

        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.type);
        dest.writeByte(this.reserved ? (byte) 1 : (byte) 0);
        dest.writeString(this.reserveDate);
        dest.writeString(this.completeDate);
        dest.writeString(this.regDate);
        dest.writeString(this.status);
        dest.writeString(this.subject);
        dest.writeString(this.contents);
        dest.writeString(this.moveType1);
        dest.writeString(this.moveType2);
        dest.writeParcelable(this.moveTarget, flags);
        dest.writeString(this.moveTargetString);
        dest.writeParcelable(this.auth, flags);
        dest.writeTypedList(this.targetList);
        dest.writeParcelable(this.properties, flags);
        dest.writeInt(this.targetCount);
        dest.writeInt(this.successCount);
        dest.writeInt(this.failCount);
        dest.writeInt(this.readCount);
        dest.writeInt(this.totalPrice);
    }

    public Msg(){

    }

    protected Msg(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.type = in.readString();
        this.reserved = in.readByte() != 0;
        this.reserveDate = in.readString();
        this.completeDate = in.readString();
        this.regDate = in.readString();
        this.status = in.readString();
        this.subject = in.readString();
        this.contents = in.readString();
        this.moveType1 = in.readString();
        this.moveType2 = in.readString();
        this.moveTarget = in.readParcelable(No.class.getClassLoader());
        this.moveTargetString = in.readString();
        this.auth = in.readParcelable(User.class.getClassLoader());
        this.targetList = in.createTypedArrayList(MsgTarget.CREATOR);
        this.properties = in.readParcelable(MsgProperties.class.getClassLoader());
        this.targetCount = in.readInt();
        this.successCount = in.readInt();
        this.failCount = in.readInt();
        this.readCount = in.readInt();
        this.totalPrice = in.readInt();
    }

    public static final Creator<Msg> CREATOR = new Creator<Msg>(){

        @Override
        public Msg createFromParcel(Parcel source){

            return new Msg(source);
        }

        @Override
        public Msg[] newArray(int size){

            return new Msg[size];
        }
    };
}
