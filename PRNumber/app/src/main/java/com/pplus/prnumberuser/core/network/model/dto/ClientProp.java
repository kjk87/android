package com.pplus.prnumberuser.core.network.model.dto;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by j2n on 2016. 7. 26..
 */

@SuppressLint("ParcelCreator")
public class ClientProp implements Parcelable{

    private String platform;

    public String getPlatform(){

        return platform;
    }

    public void setPlatform(String platform){

        this.platform = platform;
    }

    @Override
    public String toString(){

        return "ClientProp{" + "platform='" + platform + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.platform);
    }

    public ClientProp(){

    }

    protected ClientProp(Parcel in){

        this.platform = in.readString();
    }

    public static final Creator<ClientProp> CREATOR = new Creator<ClientProp>(){

        @Override
        public ClientProp createFromParcel(Parcel source){

            return new ClientProp(source);
        }

        @Override
        public ClientProp[] newArray(int size){

            return new ClientProp[size];
        }
    };
}
