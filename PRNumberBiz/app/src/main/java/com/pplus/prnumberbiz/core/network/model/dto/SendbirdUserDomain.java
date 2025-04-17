package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksh on 2016-11-23.
 */

public class SendbirdUserDomain implements Parcelable{

    private String user_id; // userSeq

    private String nickname; // 닉네임 or 홍보명

    private String profile_url; // 프로필 URL

    private String targetType; // member or page

    private String targetSeqNo; // userSeq or pageSeq: Type에 따라 바뀜.

    private String pageSeqNo; // PageSeqNo

    private String storeName; // 페이지의 홍보명

    public String getUser_id(){

        return user_id;
    }

    public void setUser_id(String user_id){

        this.user_id = user_id;
    }

    public String getNickname(){

        return nickname;
    }

    public void setNickname(String nickname){

        this.nickname = nickname;
    }

    public String getProfile_url(){

        return profile_url;
    }

    public void setProfile_url(String profile_url){

        this.profile_url = profile_url;
    }

    public String getTargetType(){

        return targetType;
    }

    public void setTargetType(String targetType){

        this.targetType = targetType;
    }

    public String getTargetSeqNo(){

        return targetSeqNo;
    }

    public void setTargetSeqNo(String targetSeqNo){

        this.targetSeqNo = targetSeqNo;
    }

    public String getPageSeqNo(){

        return pageSeqNo;
    }

    public void setPageSeqNo(String pageSeqNo){

        this.pageSeqNo = pageSeqNo;
    }

    public String getStoreName(){

        return storeName;
    }

    public void setStoreName(String storeName){

        this.storeName = storeName;
    }

    @Override
    public String toString(){

        return "SendbirdUserDomain{" +
                "user_id='" + user_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profile_url='" + profile_url + '\'' +
                ", targetType='" + targetType + '\'' +
                ", targetSeqNo='" + targetSeqNo + '\'' +
                ", pageSeqNo='" + pageSeqNo + '\'' +
                ", storeName='" + storeName + '\'' +
                '}';
    }


    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.user_id);
        dest.writeString(this.nickname);
        dest.writeString(this.profile_url);
        dest.writeString(this.targetType);
        dest.writeString(this.targetSeqNo);
        dest.writeString(this.pageSeqNo);
        dest.writeString(this.storeName);
    }

    public SendbirdUserDomain(){

    }

    protected SendbirdUserDomain(Parcel in){

        this.user_id = in.readString();
        this.nickname = in.readString();
        this.profile_url = in.readString();
        this.targetType = in.readString();
        this.targetSeqNo = in.readString();
        this.pageSeqNo = in.readString();
        this.storeName = in.readString();
    }

    public static final Parcelable.Creator<SendbirdUserDomain> CREATOR = new Parcelable.Creator<SendbirdUserDomain>(){

        @Override
        public SendbirdUserDomain createFromParcel(Parcel source){

            return new SendbirdUserDomain(source);
        }

        @Override
        public SendbirdUserDomain[] newArray(int size){

            return new SendbirdUserDomain[size];
        }
    };
}
