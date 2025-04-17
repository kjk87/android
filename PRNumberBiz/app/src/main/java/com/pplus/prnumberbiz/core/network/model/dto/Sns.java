package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by j2n on 2016. 9. 23..
 */
public class Sns implements Parcelable{

    private Long no;
    private No page;
    private String type;
    private boolean linkage;
    private String url;
    private String regDate;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public No getPage(){

        return page;
    }

    public void setPage(No page){

        this.page = page;
    }

    public String getType(){

        return type;
    }

    public void setType(String type){

        this.type = type;
    }

    public boolean isLinkage(){

        return linkage;
    }

    public void setLinkage(boolean linkage){

        this.linkage = linkage;
    }

    public String getUrl(){

        return url;
    }

    public void setUrl(String url){

        this.url = url;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    @Override
    public String toString(){

        return "Sns{" + "no=" + no + ", page=" + page + ", type='" + type + '\'' + ", linkage=" + linkage + ", url='" + url + '\'' + ", regDate='" + regDate + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeParcelable(this.page, flags);
        dest.writeString(this.type);
        dest.writeByte(this.linkage ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
        dest.writeString(this.regDate);
    }

    public Sns(){

    }

    protected Sns(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.page = in.readParcelable(No.class.getClassLoader());
        this.type = in.readString();
        this.linkage = in.readByte() != 0;
        this.url = in.readString();
        this.regDate = in.readString();
    }

    public static final Parcelable.Creator<Sns> CREATOR = new Parcelable.Creator<Sns>(){

        @Override
        public Sns createFromParcel(Parcel source){

            return new Sns(source);
        }

        @Override
        public Sns[] newArray(int size){

            return new Sns[size];
        }
    };
}
