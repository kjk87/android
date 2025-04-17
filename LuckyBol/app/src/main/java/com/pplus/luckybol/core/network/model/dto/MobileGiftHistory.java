package com.pplus.luckybol.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by imac on 2017. 10. 27..
 */

public class MobileGiftHistory implements Parcelable{
    private Long no;
    private User user;
    private MobileGift mobileGift;
    private String status;
    private int countPerTarget;
    private int totalCost;
    private int pgCost;
    private int targetCount;
    private int successCount;
    private int failCount;
    private boolean includeMe;
    private String msg;
    private String mainName;
    private String mainMobile;
    private String regDate;
    private MobileGiftHistoryProperties properties;
    private List<MobileGiftHistoryTarget> targetList;

    @Override
    public String toString(){

        return "MobileGiftHistory{" + "no=" + no + ", user=" + user + ", mobileGift=" + mobileGift + ", status='" + status + '\'' + ", countPerTarget=" + countPerTarget + ", totalCost=" + totalCost + ", pgCost=" + pgCost + ", targetCount=" + targetCount + ", successCount=" + successCount + ", failCount=" + failCount + ", includeMe=" + includeMe + ", msg='" + msg + '\'' + ", properties=" + properties + ", targetList=" + targetList + '}';
    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public User getUser(){

        return user;
    }

    public void setUser(User user){

        this.user = user;
    }

    public MobileGift getMobileGift(){

        return mobileGift;
    }

    public void setMobileGift(MobileGift mobileGift){

        this.mobileGift = mobileGift;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public int getCountPerTarget(){

        return countPerTarget;
    }

    public void setCountPerTarget(int countPerTarget){

        this.countPerTarget = countPerTarget;
    }

    public int getTotalCost(){

        return totalCost;
    }

    public void setTotalCost(int totalCost){

        this.totalCost = totalCost;
    }

    public int getPgCost(){

        return pgCost;
    }

    public void setPgCost(int pgCost){

        this.pgCost = pgCost;
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

    public boolean isIncludeMe(){

        return includeMe;
    }

    public void setIncludeMe(boolean includeMe){

        this.includeMe = includeMe;
    }

    public String getMsg(){

        return msg;
    }

    public void setMsg(String msg){

        this.msg = msg;
    }

    public MobileGiftHistoryProperties getProperties(){

        return properties;
    }

    public void setProperties(MobileGiftHistoryProperties properties){

        this.properties = properties;
    }

    public List<MobileGiftHistoryTarget> getTargetList(){

        return targetList;
    }

    public void setTargetList(List<MobileGiftHistoryTarget> targetList){

        this.targetList = targetList;
    }

    public String getMainName(){

        return mainName;
    }

    public void setMainName(String mainName){

        this.mainName = mainName;
    }

    public String getMainMobile(){

        return mainMobile;
    }

    public void setMainMobile(String mainMobile){

        this.mainMobile = mainMobile;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.mobileGift, flags);
        dest.writeString(this.status);
        dest.writeInt(this.countPerTarget);
        dest.writeInt(this.totalCost);
        dest.writeInt(this.pgCost);
        dest.writeInt(this.targetCount);
        dest.writeInt(this.successCount);
        dest.writeInt(this.failCount);
        dest.writeByte(this.includeMe ? (byte) 1 : (byte) 0);
        dest.writeString(this.msg);
        dest.writeString(this.mainName);
        dest.writeString(this.mainMobile);
        dest.writeString(this.regDate);
        dest.writeParcelable(this.properties, flags);
        dest.writeTypedList(this.targetList);
    }

    public MobileGiftHistory(){

    }

    protected MobileGiftHistory(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.mobileGift = in.readParcelable(MobileGift.class.getClassLoader());
        this.status = in.readString();
        this.countPerTarget = in.readInt();
        this.totalCost = in.readInt();
        this.pgCost = in.readInt();
        this.targetCount = in.readInt();
        this.successCount = in.readInt();
        this.failCount = in.readInt();
        this.includeMe = in.readByte() != 0;
        this.msg = in.readString();
        this.mainName = in.readString();
        this.mainMobile = in.readString();
        this.regDate = in.readString();
        this.properties = in.readParcelable(MobileGiftHistoryProperties.class.getClassLoader());
        this.targetList = in.createTypedArrayList(MobileGiftHistoryTarget.CREATOR);
    }

    public static final Creator<MobileGiftHistory> CREATOR = new Creator<MobileGiftHistory>(){

        @Override
        public MobileGiftHistory createFromParcel(Parcel source){

            return new MobileGiftHistory(source);
        }

        @Override
        public MobileGiftHistory[] newArray(int size){

            return new MobileGiftHistory[size];
        }
    };
}
