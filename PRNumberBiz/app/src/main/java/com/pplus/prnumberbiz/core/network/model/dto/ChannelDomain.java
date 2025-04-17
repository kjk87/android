package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksh on 2016-11-23.
 */

public class ChannelDomain implements Parcelable{

    private boolean is_distinct; // true이면 기존 대화방 활용, false이면 새 대화방 개설

    private int unread_message_count; // 안 읽은 메시지

    private MessageDomain last_message; // 마지막메시지

    private long created_at; // 발송시간

    private String data;  // 서버용도

    private String channel_url; // 채널 URL

    private SendbirdUserDomain user; // My

    private SendbirdUserDomain target_user; // 상대방

    public boolean is_distinct(){

        return is_distinct;
    }

    public void setIs_distinct(boolean is_distinct){

        this.is_distinct = is_distinct;
    }

    public int getUnread_message_count(){

        return unread_message_count;
    }

    public void setUnread_message_count(int unread_message_count){

        this.unread_message_count = unread_message_count;
    }

    public MessageDomain getLast_message(){

        return last_message;
    }

    public void setLast_message(MessageDomain last_message){

        this.last_message = last_message;
    }

    public long getCreated_at(){

        return created_at;
    }

    public void setCreated_at(long created_at){

        this.created_at = created_at;
    }

    public String getData(){

        return data;
    }

    public void setData(String data){

        this.data = data;
    }

    public String getChannel_url(){

        return channel_url;
    }

    public void setChannel_url(String channel_url){

        this.channel_url = channel_url;
    }

    public SendbirdUserDomain getUser(){

        return user;
    }

    public void setUser(SendbirdUserDomain user){

        this.user = user;
    }

    public SendbirdUserDomain getTarget_user(){

        return target_user;
    }

    public void setTarget_user(SendbirdUserDomain target_user){

        this.target_user = target_user;
    }

    @Override
    public String toString(){

        return "ChannelDomain{" +
                "is_distinct=" + is_distinct +
                ", unread_message_count=" + unread_message_count +
                ", last_message=" + last_message +
                ", created_at=" + created_at +
                ", data='" + data + '\'' +
                ", channel_url='" + channel_url + '\'' +
                ", user=" + user +
                ", target_user=" + target_user +
                '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeByte(this.is_distinct ? (byte) 1 : (byte) 0);
        dest.writeInt(this.unread_message_count);
        dest.writeParcelable(this.last_message, flags);
        dest.writeLong(this.created_at);
        dest.writeString(this.data);
        dest.writeString(this.channel_url);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.target_user, flags);
    }

    public ChannelDomain(){

    }

    protected ChannelDomain(Parcel in){

        this.is_distinct = in.readByte() != 0;
        this.unread_message_count = in.readInt();
        this.last_message = in.readParcelable(MessageDomain.class.getClassLoader());
        this.created_at = in.readLong();
        this.data = in.readString();
        this.channel_url = in.readString();
        this.user = in.readParcelable(SendbirdUserDomain.class.getClassLoader());
        this.target_user = in.readParcelable(SendbirdUserDomain.class.getClassLoader());
    }

    public static final Parcelable.Creator<ChannelDomain> CREATOR = new Parcelable.Creator<ChannelDomain>(){

        @Override
        public ChannelDomain createFromParcel(Parcel source){

            return new ChannelDomain(source);
        }

        @Override
        public ChannelDomain[] newArray(int size){

            return new ChannelDomain[size];
        }
    };
}
