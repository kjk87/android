package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kjk on 2017. 6. 20..
 */

public class ImgUrl implements Parcelable{
    private Long no;
    private String url;
    private Integer priority;

    public ImgUrl(Long no){

        this.no = no;
    }

    public ImgUrl(Long no, String url){

        this.no = no;
        this.url = url;
    }

    public ImgUrl(Long no, Integer priority){

        this.no = no;
        this.priority = priority;
    }

    public ImgUrl(String url, Integer priority){

        this.url = url;
        this.priority = priority;
    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getUrl(){

        return url;
    }

    public void setUrl(String url){

        this.url = url;
    }

    public Integer getPriority(){

        return priority;
    }

    public void setPriority(Integer priority){

        this.priority = priority;
    }

    @Override
    public String toString(){

        return "ImgUrl{" + "no=" + no + ", url='" + url + '\'' + ", priority=" + priority + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.url);
        dest.writeValue(this.priority);
    }

    protected ImgUrl(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.url = in.readString();
        this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ImgUrl> CREATOR = new Parcelable.Creator<ImgUrl>(){

        @Override
        public ImgUrl createFromParcel(Parcel source){

            return new ImgUrl(source);
        }

        @Override
        public ImgUrl[] newArray(int size){

            return new ImgUrl[size];
        }
    };
}
