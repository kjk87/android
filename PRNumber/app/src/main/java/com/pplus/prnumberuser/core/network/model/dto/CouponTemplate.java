package com.pplus.prnumberuser.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 김종경 on 2016-10-04.
 */

public class CouponTemplate implements Parcelable{

    private Long no;
    private String name;
    private String note;
    private Duration duration;
    private Integer downloadLimit;
    private String discountType;
    private String discount;
    private String condition;
    private String status;
    private Long downloadCount;
    private Long giftCount;
    private Long useCount;
    private String regDate;
    private String publisherType;
    private Page publisher;
    private ImgUrl icon;
    private boolean display;
    private boolean givePlus;
    private String type;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    public String getNote(){

        return note;
    }

    public void setNote(String note){

        this.note = note;
    }

    public Duration getDuration(){

        return duration;
    }

    public void setDuration(Duration duration){

        this.duration = duration;
    }

    public Integer getDownloadLimit(){

        return downloadLimit;
    }

    public void setDownloadLimit(Integer downloadLimit){

        this.downloadLimit = downloadLimit;
    }

    public String getDiscountType(){

        return discountType;
    }

    public void setDiscountType(String discountType){

        this.discountType = discountType;
    }

    public String getDiscount(){

        return discount;
    }

    public void setDiscount(String discount){

        this.discount = discount;
    }

    public String getCondition(){

        return condition;
    }

    public void setCondition(String condition){

        this.condition = condition;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public Long getDownloadCount(){

        return downloadCount;
    }

    public void setDownloadCount(Long downloadCount){

        this.downloadCount = downloadCount;
    }

    public Long getGiftCount(){

        return giftCount;
    }

    public void setGiftCount(Long giftCount){

        this.giftCount = giftCount;
    }

    public Long getUseCount(){

        return useCount;
    }

    public void setUseCount(Long useCount){

        this.useCount = useCount;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    public String getPublisherType(){

        return publisherType;
    }

    public void setPublisherType(String publisherType){

        this.publisherType = publisherType;
    }

    public Page getPublisher(){

        return publisher;
    }

    public void setPublisher(Page publisher){

        this.publisher = publisher;
    }

    public ImgUrl getIcon(){

        return icon;
    }

    public void setIcon(ImgUrl icon){

        this.icon = icon;
    }

    public boolean isDisplay(){

        return display;
    }

    public void setDisplay(boolean display){

        this.display = display;
    }

    public boolean isGivePlus(){

        return givePlus;
    }

    public void setGivePlus(boolean givePlus){

        this.givePlus = givePlus;
    }

    public String getType(){

        return type;
    }

    public void setType(String type){

        this.type = type;
    }

    @Override
    public String toString(){

        return "CouponTemplate{" + "no=" + no + ", name='" + name + '\'' + ", note='" + note + '\'' + ", duration=" + duration + ", downloadLimit=" + downloadLimit + ", discountType='" + discountType + '\'' + ", discount='" + discount + '\'' + ", condition='" + condition + '\'' + ", status='" + status + '\'' + ", downloadCount=" + downloadCount + ", giftCount=" + giftCount + ", useCount=" + useCount + ", regDate='" + regDate + '\'' + ", publisherType='" + publisherType + '\'' + ", publisher=" + publisher + ", icon=" + icon + ", display=" + display + ", givePlus=" + givePlus + ", type='" + type + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.name);
        dest.writeString(this.note);
        dest.writeParcelable(this.duration, flags);
        dest.writeValue(this.downloadLimit);
        dest.writeString(this.discountType);
        dest.writeString(this.discount);
        dest.writeString(this.condition);
        dest.writeString(this.status);
        dest.writeValue(this.downloadCount);
        dest.writeValue(this.giftCount);
        dest.writeValue(this.useCount);
        dest.writeString(this.regDate);
        dest.writeString(this.publisherType);
        dest.writeParcelable(this.publisher, flags);
        dest.writeParcelable(this.icon, flags);
        dest.writeByte(this.display ? (byte) 1 : (byte) 0);
        dest.writeByte(this.givePlus ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
    }

    public CouponTemplate(){

    }

    protected CouponTemplate(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.note = in.readString();
        this.duration = in.readParcelable(Duration.class.getClassLoader());
        this.downloadLimit = (Integer) in.readValue(Integer.class.getClassLoader());
        this.discountType = in.readString();
        this.discount = in.readString();
        this.condition = in.readString();
        this.status = in.readString();
        this.downloadCount = (Long) in.readValue(Long.class.getClassLoader());
        this.giftCount = (Long) in.readValue(Long.class.getClassLoader());
        this.useCount = (Long) in.readValue(Long.class.getClassLoader());
        this.regDate = in.readString();
        this.publisherType = in.readString();
        this.publisher = in.readParcelable(Page.class.getClassLoader());
        this.icon = in.readParcelable(ImgUrl.class.getClassLoader());
        this.display = in.readByte() != 0;
        this.givePlus = in.readByte() != 0;
        this.type = in.readString();
    }

    public static final Creator<CouponTemplate> CREATOR = new Creator<CouponTemplate>(){

        @Override
        public CouponTemplate createFromParcel(Parcel source){

            return new CouponTemplate(source);
        }

        @Override
        public CouponTemplate[] newArray(int size){

            return new CouponTemplate[size];
        }
    };
}
