package com.pplus.luckybol.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 김종경 on 2016-09-22.
 */

public class Group implements Parcelable{

    private Long no;
    private String name;
    private boolean defaultGroup;
    private int count;
    private int priority;
    private No page;

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o instanceof Group) {
            return (((Group) o).no.equals(no));
        } else {
            return false;
        }
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

    public boolean isDefaultGroup(){

        return defaultGroup;
    }

    public void setDefaultGroup(boolean defaultGroup){

        this.defaultGroup = defaultGroup;
    }

    public int getCount(){

        return count;
    }

    public void setCount(int count){

        this.count = count;
    }

    public int getPriority(){

        return priority;
    }

    public void setPriority(int priority){

        this.priority = priority;
    }

    public No getPage(){

        return page;
    }

    public void setPage(No page){

        this.page = page;
    }

    @Override
    public String toString(){

        return "Group{" + "no=" + no + ", name='" + name + '\'' + ", defaultGroup=" + defaultGroup + ", count=" + count + ", priority=" + priority + ", page=" + page + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.name);
        dest.writeByte(this.defaultGroup ? (byte) 1 : (byte) 0);
        dest.writeInt(this.count);
        dest.writeInt(this.priority);
        dest.writeParcelable(this.page, flags);
    }

    public Group(){

    }

    protected Group(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.defaultGroup = in.readByte() != 0;
        this.count = in.readInt();
        this.priority = in.readInt();
        this.page = in.readParcelable(No.class.getClassLoader());
    }

    public static final Creator<Group> CREATOR = new Creator<Group>(){

        @Override
        public Group createFromParcel(Parcel source){

            return new Group(source);
        }

        @Override
        public Group[] newArray(int size){

            return new Group[size];
        }
    };
}
