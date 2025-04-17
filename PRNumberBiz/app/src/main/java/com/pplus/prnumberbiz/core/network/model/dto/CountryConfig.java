package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by imac on 2017. 8. 29..
 */

public class CountryConfig implements Parcelable{
    private Long no;
    private String number;
    private String code;
    private String engName;
    private String currency;
    private String status;
    private CountryConfigProperties properties;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getNumber(){

        return number;
    }

    public void setNumber(String number){

        this.number = number;
    }

    public String getCode(){

        return code;
    }

    public void setCode(String code){

        this.code = code;
    }

    public String getEngName(){

        return engName;
    }

    public void setEngName(String engName){

        this.engName = engName;
    }

    public String getCurrency(){

        return currency;
    }

    public void setCurrency(String currency){

        this.currency = currency;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public CountryConfigProperties getProperties(){

        return properties;
    }

    public void setProperties(CountryConfigProperties properties){

        this.properties = properties;
    }

    @Override
    public String toString(){

        return "CountryConfig{" + "no=" + no + ", number='" + number + '\'' + ", code='" + code + '\'' + ", engName='" + engName + '\'' + ", currency='" + currency + '\'' + ", status='" + status + '\'' + ", properties=" + properties + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.number);
        dest.writeString(this.code);
        dest.writeString(this.engName);
        dest.writeString(this.currency);
        dest.writeString(this.status);
        dest.writeParcelable(this.properties, flags);
    }

    public CountryConfig(){

    }

    protected CountryConfig(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.number = in.readString();
        this.code = in.readString();
        this.engName = in.readString();
        this.currency = in.readString();
        this.status = in.readString();
        this.properties = in.readParcelable(CountryConfigProperties.class.getClassLoader());
    }

    public static final Creator<CountryConfig> CREATOR = new Creator<CountryConfig>(){

        @Override
        public CountryConfig createFromParcel(Parcel source){

            return new CountryConfig(source);
        }

        @Override
        public CountryConfig[] newArray(int size){

            return new CountryConfig[size];
        }
    };
}
