package com.pplus.prnumberbiz.core.network.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by j2n on 2016. 7. 26..
 */

@SuppressLint("ParcelCreator")
public class VersionProp implements Parcelable{

    private String lastVersion;
    private boolean mustUpdate;
    private String downloadUrl;

    public String getLastVersion(){

        return lastVersion;
    }

    public void setLastVersion(String lastVersion){

        this.lastVersion = lastVersion;
    }

    public boolean isMustUpdate(){

        return mustUpdate;
    }

    public void setMustUpdate(boolean mustUpdate){

        this.mustUpdate = mustUpdate;
    }

    public String getDownloadUrl(){

        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl){

        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString(){

        return "VersionProp{" + "lastVersion='" + lastVersion + '\'' + ", mustUpdate=" + mustUpdate + ", downloadUrl='" + downloadUrl + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.lastVersion);
        dest.writeByte(this.mustUpdate ? (byte) 1 : (byte) 0);
        dest.writeString(this.downloadUrl);
    }

    public VersionProp(){

    }

    protected VersionProp(Parcel in){

        this.lastVersion = in.readString();
        this.mustUpdate = in.readByte() != 0;
        this.downloadUrl = in.readString();
    }

    public static final Parcelable.Creator<VersionProp> CREATOR = new Parcelable.Creator<VersionProp>(){

        @Override
        public VersionProp createFromParcel(Parcel source){

            return new VersionProp(source);
        }

        @Override
        public VersionProp[] newArray(int size){

            return new VersionProp[size];
        }
    };
}
