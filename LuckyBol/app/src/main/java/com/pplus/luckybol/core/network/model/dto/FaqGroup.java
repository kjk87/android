package com.pplus.luckybol.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 23..
 */

public class FaqGroup implements Parcelable{
    private Long no;
    private String name;
    private Integer priority;
    private String regDate;

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

    public Integer getPriority(){

        return priority;
    }

    public void setPriority(Integer priority){

        this.priority = priority;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    @Override
    public String toString(){

        return "FaqGroup{" + "no=" + no + ", name='" + name + '\'' + ", priority=" + priority + ", regDate='" + regDate + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.name);
        dest.writeValue(this.priority);
        dest.writeString(this.regDate);
    }

    public FaqGroup(){

    }

    protected FaqGroup(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
        this.regDate = in.readString();
    }

    public static final Creator<FaqGroup> CREATOR = new Creator<FaqGroup>(){

        @Override
        public FaqGroup createFromParcel(Parcel source){

            return new FaqGroup(source);
        }

        @Override
        public FaqGroup[] newArray(int size){

            return new FaqGroup[size];
        }
    };
}
