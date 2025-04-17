package com.pplus.luckybol.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 6. 27..
 */

public class InstalledApp implements Parcelable{
    private String appKey;
    private String version;
    private String pushKey;
    private boolean pushActivate;
    private String pushMask;

    public String getAppKey(){

        return appKey;
    }

    public void setAppKey(String appKey){

        this.appKey = appKey;
    }

    public String getVersion(){

        return version;
    }

    public void setVersion(String version){

        this.version = version;
    }

    public String getPushKey(){

        return pushKey;
    }

    public void setPushKey(String pushKey){

        this.pushKey = pushKey;
    }

    public boolean isPushActivate(){

        return pushActivate;
    }

    public void setPushActivate(boolean pushActivate){

        this.pushActivate = pushActivate;
    }

    public String getPushMask(){

        return pushMask;
    }

    public void setPushMask(String pushMask){

        this.pushMask = pushMask;
    }

    @Override
    public String toString(){

        return "InstalledApp{" + "appKey='" + appKey + '\'' + ", version='" + version + '\'' + ", pushKey='" + pushKey + '\'' + ", pushActivate=" + pushActivate + ", pushMask='" + pushMask + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.appKey);
        dest.writeString(this.version);
        dest.writeString(this.pushKey);
        dest.writeByte(this.pushActivate ? (byte) 1 : (byte) 0);
        dest.writeString(this.pushMask);
    }

    public InstalledApp(){

    }

    protected InstalledApp(Parcel in){

        this.appKey = in.readString();
        this.version = in.readString();
        this.pushKey = in.readString();
        this.pushActivate = in.readByte() != 0;
        this.pushMask = in.readString();
    }

    public static final Creator<InstalledApp> CREATOR = new Creator<InstalledApp>(){

        @Override
        public InstalledApp createFromParcel(Parcel source){

            return new InstalledApp(source);
        }

        @Override
        public InstalledApp[] newArray(int size){

            return new InstalledApp[size];
        }
    };
}
