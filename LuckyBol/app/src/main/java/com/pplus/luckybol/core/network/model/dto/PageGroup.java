package com.pplus.luckybol.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 김종경 on 2016-09-22.
 */

public class PageGroup implements Parcelable{

    private Long no;
    private String name;
    private String platform;
    private int pageCount;
    private int priority;
    private List<Page> pageList;

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o instanceof PageGroup) {
            return (((PageGroup) o).no.equals(no));
        } else {
            return false;
        }
    }

    @Override
    public String toString(){

        return "PageGroup{" + "no=" + no + ", name='" + name + '\'' + ", platform='" + platform + '\'' + ", pageCount=" + pageCount + ", priority=" + priority + ", pageList=" + pageList + '}';
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

    public String getPlatform(){

        return platform;
    }

    public void setPlatform(String platform){

        this.platform = platform;
    }

    public int getPageCount(){

        return pageCount;
    }

    public void setPageCount(int pageCount){

        this.pageCount = pageCount;
    }

    public int getPriority(){

        return priority;
    }

    public void setPriority(int priority){

        this.priority = priority;
    }

    public List<Page> getPageList(){

        return pageList;
    }

    public void setPageList(List<Page> pageList){

        this.pageList = pageList;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.name);
        dest.writeString(this.platform);
        dest.writeInt(this.pageCount);
        dest.writeInt(this.priority);
        dest.writeTypedList(this.pageList);
    }

    public PageGroup(){

    }

    protected PageGroup(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.platform = in.readString();
        this.pageCount = in.readInt();
        this.priority = in.readInt();
        this.pageList = in.createTypedArrayList(Page.CREATOR);
    }

    public static final Creator<PageGroup> CREATOR = new Creator<PageGroup>(){

        @Override
        public PageGroup createFromParcel(Parcel source){

            return new PageGroup(source);
        }

        @Override
        public PageGroup[] newArray(int size){

            return new PageGroup[size];
        }
    };
}
