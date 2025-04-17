package com.pplus.prnumberuser.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by j2n on 2016. 9. 23..
 */
public class Device implements Parcelable{

    private Long no;
    private String deviceId;
    private String lastAccessDate;
    private String platform;
    private String token;
    private String snsToken;
    private InstalledApp installedApp;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getDeviceId(){

        return deviceId;
    }

    public void setDeviceId(String deviceId){

        this.deviceId = deviceId;
    }

    public String getLastAccessDate(){

        return lastAccessDate;
    }

    public void setLastAccessDate(String lastAccessDate){

        this.lastAccessDate = lastAccessDate;
    }

    public String getPlatform(){

        return platform;
    }

    public void setPlatform(String platform){

        this.platform = platform;
    }

    public String getToken(){

        return token;
    }

    public void setToken(String token){

        this.token = token;
    }

    public String getSnsToken(){

        return snsToken;
    }

    public void setSnsToken(String snsToken){

        this.snsToken = snsToken;
    }

    public InstalledApp getInstalledApp(){

        return installedApp;
    }

    public void setInstalledApp(InstalledApp installedApp){

        this.installedApp = installedApp;
    }

    @Override
    public String toString(){

        return "Device{" + "no=" + no + ", deviceId='" + deviceId + '\'' + ", lastAccessDate='" + lastAccessDate + '\'' + ", platform='" + platform + '\'' + ", token='" + token + '\'' + ", snsToken='" + snsToken + '\'' + ", installedApp=" + installedApp + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.deviceId);
        dest.writeString(this.lastAccessDate);
        dest.writeString(this.platform);
        dest.writeString(this.token);
        dest.writeString(this.snsToken);
        dest.writeParcelable(this.installedApp, flags);
    }

    public Device(){

    }

    protected Device(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.deviceId = in.readString();
        this.lastAccessDate = in.readString();
        this.platform = in.readString();
        this.token = in.readString();
        this.snsToken = in.readString();
        this.installedApp = in.readParcelable(InstalledApp.class.getClassLoader());
    }

    public static final Creator<Device> CREATOR = new Creator<Device>(){

        @Override
        public Device createFromParcel(Parcel source){

            return new Device(source);
        }

        @Override
        public Device[] newArray(int size){

            return new Device[size];
        }
    };
}
