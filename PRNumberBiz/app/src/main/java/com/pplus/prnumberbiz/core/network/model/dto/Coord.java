package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 8. 23..
 */

public class Coord implements Parcelable{
    private Double x;
    private Double y;

    public Double getX(){

        return x;
    }

    public void setX(Double x){

        this.x = x;
    }

    public Double getY(){

        return y;
    }

    public void setY(Double y){

        this.y = y;
    }

    @Override
    public String toString(){

        return "Coord{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.x);
        dest.writeValue(this.y);
    }

    public Coord(){

    }

    protected Coord(Parcel in){

        this.x = (Double) in.readValue(Double.class.getClassLoader());
        this.y = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Coord> CREATOR = new Parcelable.Creator<Coord>(){

        @Override
        public Coord createFromParcel(Parcel source){

            return new Coord(source);
        }

        @Override
        public Coord[] newArray(int size){

            return new Coord[size];
        }
    };
}
