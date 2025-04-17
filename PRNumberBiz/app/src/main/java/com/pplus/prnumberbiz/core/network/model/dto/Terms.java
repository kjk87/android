package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by j2n on 2016. 7. 26..
 */

public class Terms implements Parcelable{

    private Long no;
    private String code;
    private String status;
    private String subject;
    private boolean compulsory;
    private String name;
    private String contents;
    private String regDate;
    private String modDate;
    private String url;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getCode(){

        return code;
    }

    public void setCode(String code){

        this.code = code;
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

    public boolean isCompulsory(){

        return compulsory;
    }

    public void setCompulsory(boolean compulsory){

        this.compulsory = compulsory;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
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

    public String getModDate(){

        return modDate;
    }

    public void setModDate(String modDate){

        this.modDate = modDate;
    }

    public String getUrl(){

        return url;
    }

    public void setUrl(String url){

        this.url = url;
    }

    @Override
    public String toString(){

        return "Terms{" + "no=" + no + ", code='" + code + '\'' + ", status='" + status + '\'' + ", subject='" + subject + '\'' + ", compulsory=" + compulsory + ", name='" + name + '\'' + ", contents='" + contents + '\'' + ", regDate='" + regDate + '\'' + ", modDate='" + modDate + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.code);
        dest.writeString(this.status);
        dest.writeString(this.subject);
        dest.writeByte(this.compulsory ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeString(this.contents);
        dest.writeString(this.regDate);
        dest.writeString(this.modDate);
        dest.writeString(this.url);
    }

    public Terms(){

    }

    protected Terms(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.code = in.readString();
        this.status = in.readString();
        this.subject = in.readString();
        this.compulsory = in.readByte() != 0;
        this.name = in.readString();
        this.contents = in.readString();
        this.regDate = in.readString();
        this.modDate = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Terms> CREATOR = new Parcelable.Creator<Terms>(){

        @Override
        public Terms createFromParcel(Parcel source){

            return new Terms(source);
        }

        @Override
        public Terms[] newArray(int size){

            return new Terms[size];
        }
    };
}
