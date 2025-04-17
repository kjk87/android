package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 김종경 on 2016-09-22.
 */

public class FranchiseGroup implements Parcelable{

    private Long no;
    private String name;
    private String status;
    private Integer priority;

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o instanceof FranchiseGroup) {
            return (((FranchiseGroup) o).no.equals(no));
        } else {
            return false;
        }
    }

    @Override
    public String toString(){

        return "FranchiseGroup{" + "no=" + no + ", name='" + name + '\'' + ", status='" + status + '\'' + ", priority=" + priority + '}';
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

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public Integer getPriority(){

        return priority;
    }

    public void setPriority(Integer priority){

        this.priority = priority;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.name);
        dest.writeString(this.status);
        dest.writeValue(this.priority);
    }

    public FranchiseGroup(){

    }

    protected FranchiseGroup(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.status = in.readString();
        this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<FranchiseGroup> CREATOR = new Creator<FranchiseGroup>(){

        @Override
        public FranchiseGroup createFromParcel(Parcel source){

            return new FranchiseGroup(source);
        }

        @Override
        public FranchiseGroup[] newArray(int size){

            return new FranchiseGroup[size];
        }
    };
}
