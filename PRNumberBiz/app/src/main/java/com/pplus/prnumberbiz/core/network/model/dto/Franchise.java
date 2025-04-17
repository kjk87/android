package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 김종경 on 2016-09-22.
 */

public class Franchise implements Parcelable{

    private Long no;
    private String name;
    private Page mainPage;

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o instanceof Franchise) {
            return (((Franchise) o).no.equals(no));
        } else {
            return false;
        }
    }

    @Override
    public String toString(){

        return "Franchise{" + "no=" + no + ", name='" + name + '\'' + ", mainPage=" + mainPage + '}';
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

    public Page getMainPage(){

        return mainPage;
    }

    public void setMainPage(Page mainPage){

        this.mainPage = mainPage;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.name);
        dest.writeParcelable(this.mainPage, flags);
    }

    public Franchise(){

    }

    protected Franchise(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.mainPage = in.readParcelable(Page.class.getClassLoader());
    }

    public static final Creator<Franchise> CREATOR = new Creator<Franchise>(){

        @Override
        public Franchise createFromParcel(Parcel source){

            return new Franchise(source);
        }

        @Override
        public Franchise[] newArray(int size){

            return new Franchise[size];
        }
    };
}
