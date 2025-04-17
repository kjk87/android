package com.pplus.luckybol.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 10. 27..
 */

public class MobileGiftImage implements Parcelable{
    private Long no;
    private String code;
    private String path;
    private int width;
    private int height;

    @Override
    public String toString(){

        return "MobileGiftImage{" + "no=" + no + ", code='" + code + '\'' + ", path='" + path + '\'' + ", width=" + width + ", height=" + height + '}';
    }

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

    public String getPath(){

        return path;
    }

    public void setPath(String path){

        this.path = path;
    }

    public int getWidth(){

        return width;
    }

    public void setWidth(int width){

        this.width = width;
    }

    public int getHeight(){

        return height;
    }

    public void setHeight(int height){

        this.height = height;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.code);
        dest.writeString(this.path);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
    }

    public MobileGiftImage(){

    }

    protected MobileGiftImage(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.code = in.readString();
        this.path = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Creator<MobileGiftImage> CREATOR = new Creator<MobileGiftImage>(){

        @Override
        public MobileGiftImage createFromParcel(Parcel source){

            return new MobileGiftImage(source);
        }

        @Override
        public MobileGiftImage[] newArray(int size){

            return new MobileGiftImage[size];
        }
    };
}
