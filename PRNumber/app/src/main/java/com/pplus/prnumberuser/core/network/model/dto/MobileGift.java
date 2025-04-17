package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by imac on 2017. 10. 27..
 */

public class MobileGift implements Parcelable{
    private Long no;
    private String name;
    private String code;
    private String companyName;
    private String companyCode;
    private Long userPrice;
    private Long salesPrice;
    private String useArea;
    private boolean tax;
    private String useLimit;
    private String useNote;
    private String outputType;
    private boolean sales;
    private String oldCode;
    private int offerPrice;
    private String useTerm;
    private boolean includeVat;
    private String baseImage;
    private String viewImage1;
    private String viewImage2;
    private String viewImage3;
    private String detailImage;
    private String modDate;
    private List<MobileGiftImage> imageList;

    @Override
    public String toString(){

        return "MobileGift{" + "no=" + no + ", name='" + name + '\'' + ", code=" + code + ", companyName='" + companyName + '\'' + ", companyCode='" + companyCode + '\'' + ", userPrice=" + userPrice + ", salesPrice=" + salesPrice + ", useArea='" + useArea + '\'' + ", tax=" + tax + ", useLimit='" + useLimit + '\'' + ", useNote='" + useNote + '\'' + ", outputType='" + outputType + '\'' + ", sales=" + sales + ", oldCode='" + oldCode + '\'' + ", offerPrice=" + offerPrice + ", useTerm='" + useTerm + '\'' + ", includeVat=" + includeVat + ", baseImage='" + baseImage + '\'' + ", viewImage1='" + viewImage1 + '\'' + ", viewImage2='" + viewImage2 + '\'' + ", viewImage3='" + viewImage3 + '\'' + ", detailImage='" + detailImage + '\'' + ", modDate='" + modDate + '\'' + ", imageList=" + imageList + '}';
    }

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

    public String getCode(){

        return code;
    }

    public void setCode(String code){

        this.code = code;
    }

    public String getCompanyName(){

        return companyName;
    }

    public void setCompanyName(String companyName){

        this.companyName = companyName;
    }

    public String getCompanyCode(){

        return companyCode;
    }

    public void setCompanyCode(String companyCode){

        this.companyCode = companyCode;
    }

    public Long getUserPrice(){

        return userPrice;
    }

    public void setUserPrice(Long userPrice){

        this.userPrice = userPrice;
    }

    public Long getSalesPrice(){

        return salesPrice;
    }

    public void setSalesPrice(Long salesPrice){

        this.salesPrice = salesPrice;
    }

    public String getUseArea(){

        return useArea;
    }

    public void setUseArea(String useArea){

        this.useArea = useArea;
    }

    public boolean isTax(){

        return tax;
    }

    public void setTax(boolean tax){

        this.tax = tax;
    }

    public String getUseLimit(){

        return useLimit;
    }

    public void setUseLimit(String useLimit){

        this.useLimit = useLimit;
    }

    public String getUseNote(){

        return useNote;
    }

    public void setUseNote(String useNote){

        this.useNote = useNote;
    }

    public String getOutputType(){

        return outputType;
    }

    public void setOutputType(String outputType){

        this.outputType = outputType;
    }

    public boolean isSales(){

        return sales;
    }

    public void setSales(boolean sales){

        this.sales = sales;
    }

    public String getOldCode(){

        return oldCode;
    }

    public void setOldCode(String oldCode){

        this.oldCode = oldCode;
    }

    public int getOfferPrice(){

        return offerPrice;
    }

    public void setOfferPrice(int offerPrice){

        this.offerPrice = offerPrice;
    }

    public String getUseTerm(){

        return useTerm;
    }

    public void setUseTerm(String useTerm){

        this.useTerm = useTerm;
    }

    public boolean isIncludeVat(){

        return includeVat;
    }

    public void setIncludeVat(boolean includeVat){

        this.includeVat = includeVat;
    }

    public String getBaseImage(){

        return baseImage;
    }

    public void setBaseImage(String baseImage){

        this.baseImage = baseImage;
    }

    public String getViewImage1(){

        return viewImage1;
    }

    public void setViewImage1(String viewImage1){

        this.viewImage1 = viewImage1;
    }

    public String getViewImage2(){

        return viewImage2;
    }

    public void setViewImage2(String viewImage2){

        this.viewImage2 = viewImage2;
    }

    public String getViewImage3(){

        return viewImage3;
    }

    public void setViewImage3(String viewImage3){

        this.viewImage3 = viewImage3;
    }

    public String getDetailImage(){

        return detailImage;
    }

    public void setDetailImage(String detailImage){

        this.detailImage = detailImage;
    }

    public String getModDate(){

        return modDate;
    }

    public void setModDate(String modDate){

        this.modDate = modDate;
    }

    public List<MobileGiftImage> getImageList(){

        return imageList;
    }

    public void setImageList(List<MobileGiftImage> imageList){

        this.imageList = imageList;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.name);
        dest.writeString(this.code);
        dest.writeString(this.companyName);
        dest.writeString(this.companyCode);
        dest.writeValue(this.userPrice);
        dest.writeValue(this.salesPrice);
        dest.writeString(this.useArea);
        dest.writeByte(this.tax ? (byte) 1 : (byte) 0);
        dest.writeString(this.useLimit);
        dest.writeString(this.useNote);
        dest.writeString(this.outputType);
        dest.writeByte(this.sales ? (byte) 1 : (byte) 0);
        dest.writeString(this.oldCode);
        dest.writeInt(this.offerPrice);
        dest.writeString(this.useTerm);
        dest.writeByte(this.includeVat ? (byte) 1 : (byte) 0);
        dest.writeString(this.baseImage);
        dest.writeString(this.viewImage1);
        dest.writeString(this.viewImage2);
        dest.writeString(this.viewImage3);
        dest.writeString(this.detailImage);
        dest.writeString(this.modDate);
        dest.writeTypedList(this.imageList);
    }

    public MobileGift(){

    }

    protected MobileGift(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.code = in.readString();
        this.companyName = in.readString();
        this.companyCode = in.readString();
        this.userPrice = (Long) in.readValue(Long.class.getClassLoader());
        this.salesPrice = (Long) in.readValue(Long.class.getClassLoader());
        this.useArea = in.readString();
        this.tax = in.readByte() != 0;
        this.useLimit = in.readString();
        this.useNote = in.readString();
        this.outputType = in.readString();
        this.sales = in.readByte() != 0;
        this.oldCode = in.readString();
        this.offerPrice = in.readInt();
        this.useTerm = in.readString();
        this.includeVat = in.readByte() != 0;
        this.baseImage = in.readString();
        this.viewImage1 = in.readString();
        this.viewImage2 = in.readString();
        this.viewImage3 = in.readString();
        this.detailImage = in.readString();
        this.modDate = in.readString();
        this.imageList = in.createTypedArrayList(MobileGiftImage.CREATOR);
    }

    public static final Creator<MobileGift> CREATOR = new Creator<MobileGift>(){

        @Override
        public MobileGift createFromParcel(Parcel source){

            return new MobileGift(source);
        }

        @Override
        public MobileGift[] newArray(int size){

            return new MobileGift[size];
        }
    };
}
