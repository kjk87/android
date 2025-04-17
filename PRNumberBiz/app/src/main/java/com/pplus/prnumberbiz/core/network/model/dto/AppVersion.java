package com.pplus.prnumberbiz.core.network.model.dto;

import android.annotation.SuppressLint;

import com.pplus.prnumberbiz.core.network.model.dto.ClientProp;
import com.pplus.prnumberbiz.core.network.model.dto.VersionProp;

/**
 * Created by j2n on 2016. 7. 26..
 */

@SuppressLint("ParcelCreator")
public class AppVersion{

    private String appKey;
    private String status;
    private String version;
    private ClientProp clientProp;
    private VersionProp versionProp;

    public String getAppKey(){

        return appKey;
    }

    public void setAppKey(String appKey){

        this.appKey = appKey;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public String getVersion(){

        return version;
    }

    public void setVersion(String version){

        this.version = version;
    }

    public ClientProp getClientProp(){

        return clientProp;
    }

    public void setClientProp(ClientProp clientProp){

        this.clientProp = clientProp;
    }

    public VersionProp getVersionProp(){

        return versionProp;
    }

    public void setVersionProp(VersionProp versionProp){

        this.versionProp = versionProp;
    }

    @Override
    public String toString(){

        return "AppVersion{" + "appKey='" + appKey + '\'' + ", status='" + status + '\'' + ", version='" + version + '\'' + ", clientProp=" + clientProp + ", versionProp=" + versionProp + '}';
    }
}
