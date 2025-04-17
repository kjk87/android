package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by imac on 2017. 10. 27..
 */

public class MobileGiftPurchase implements Parcelable{
    private MobileGift mobileGift;
    private int countPerTarget;
    private Long totalCost;
    private Long pgCost;
    private boolean includeMe;
    private String msg;
    private List<MsgTarget> targetList;
    private PgApproval approval;

    public MobileGift getMobileGift(){

        return mobileGift;
    }

    public void setMobileGift(MobileGift mobileGift){

        this.mobileGift = mobileGift;
    }

    public int getCountPerTarget(){

        return countPerTarget;
    }

    public void setCountPerTarget(int countPerTarget){

        this.countPerTarget = countPerTarget;
    }

    public Long getTotalCost(){

        return totalCost;
    }

    public void setTotalCost(Long totalCost){

        this.totalCost = totalCost;
    }

    public Long getPgCost(){

        return pgCost;
    }

    public void setPgCost(Long pgCost){

        this.pgCost = pgCost;
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

    public List<MsgTarget> getTargetList(){

        return targetList;
    }

    public void setTargetList(List<MsgTarget> targetList){

        this.targetList = targetList;
    }

    public PgApproval getApproval(){

        return approval;
    }

    public void setApproval(PgApproval approval){

        this.approval = approval;
    }

    @Override
    public String toString(){

        return "MobileGiftPurchase{" + "mobileGift=" + mobileGift + ", countPerTarget=" + countPerTarget + ", totalCost=" + totalCost + ", pgCost=" + pgCost + ", includeMe=" + includeMe + ", msg='" + msg + '\'' + ", targetList=" + targetList + ", approval=" + approval + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeParcelable(this.mobileGift, flags);
        dest.writeInt(this.countPerTarget);
        dest.writeValue(this.totalCost);
        dest.writeValue(this.pgCost);
        dest.writeByte(this.includeMe ? (byte) 1 : (byte) 0);
        dest.writeString(this.msg);
        dest.writeTypedList(this.targetList);
        dest.writeParcelable(this.approval, flags);
    }

    public MobileGiftPurchase(){

    }

    protected MobileGiftPurchase(Parcel in){

        this.mobileGift = in.readParcelable(MobileGift.class.getClassLoader());
        this.countPerTarget = in.readInt();
        this.totalCost = (Long) in.readValue(Long.class.getClassLoader());
        this.pgCost = (Long) in.readValue(Long.class.getClassLoader());
        this.includeMe = in.readByte() != 0;
        this.msg = in.readString();
        this.targetList = in.createTypedArrayList(MsgTarget.CREATOR);
        this.approval = in.readParcelable(PgApproval.class.getClassLoader());
    }

    public static final Creator<MobileGiftPurchase> CREATOR = new Creator<MobileGiftPurchase>(){

        @Override
        public MobileGiftPurchase createFromParcel(Parcel source){

            return new MobileGiftPurchase(source);
        }

        @Override
        public MobileGiftPurchase[] newArray(int size){

            return new MobileGiftPurchase[size];
        }
    };
}
