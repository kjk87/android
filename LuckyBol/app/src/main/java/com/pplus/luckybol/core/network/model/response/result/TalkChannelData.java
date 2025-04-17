package com.pplus.luckybol.core.network.model.response.result;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksh on 2016-10-20.
 */

public class TalkChannelData implements Parcelable{

    private String message; // 받은 메세지
    private String unread_message_count; // 읽지 않은 메시지 카운트
    private String channel_url; // 채널 URL
    private String created_at; // 서버시간
    private String sender_target_type; // member, page
    private String sender_target_seq_no; // userSeqNo or pageSeqNo
    private String sender_nickname; // 보낸사람 닉네임
    private String sender_profile_url; // 보낸사람 url
    private String sender_page_seq_no; // 보낸사람 PageSeqNo
    private String sender_store_name; // 보낸사람 페이지 홍보명
    private String recipient_nickname; // 받는사람 닉네임
    private String recipient_profile_url; // 받는사람 url

    public String getMessage(){

        return message;
    }

    public void setMessage(String message){

        this.message = message;
    }

    public String getUnread_message_count(){

        return unread_message_count;
    }

    public void setUnread_message_count(String unread_message_count){

        this.unread_message_count = unread_message_count;
    }

    public String getChannel_url(){

        return channel_url;
    }

    public void setChannel_url(String channel_url){

        this.channel_url = channel_url;
    }

    public String getCreated_at(){

        return created_at;
    }

    public void setCreated_at(String created_at){

        this.created_at = created_at;
    }

    public String getSender_target_type(){

        return sender_target_type;
    }

    public void setSender_target_type(String sender_target_type){

        this.sender_target_type = sender_target_type;
    }

    public String getSender_target_seq_no(){

        return sender_target_seq_no;
    }

    public void setSender_target_seq_no(String sender_target_seq_no){

        this.sender_target_seq_no = sender_target_seq_no;
    }

    public String getSender_nickname(){

        return sender_nickname;
    }

    public void setSender_nickname(String sender_nickname){

        this.sender_nickname = sender_nickname;
    }

    public String getSender_profile_url(){

        return sender_profile_url;
    }

    public void setSender_profile_url(String sender_profile_url){

        this.sender_profile_url = sender_profile_url;
    }

    public String getSender_store_name(){

        return sender_store_name;
    }

    public void setSender_store_name(String sender_store_name){

        this.sender_store_name = sender_store_name;
    }

    public String getRecipient_nickname(){

        return recipient_nickname;
    }

    public void setRecipient_nickname(String recipient_nickname){

        this.recipient_nickname = recipient_nickname;
    }

    public String getRecipient_profile_url(){

        return recipient_profile_url;
    }

    public void setRecipient_profile_url(String recipient_profile_url){

        this.recipient_profile_url = recipient_profile_url;
    }

    public String getSender_page_seq_no(){

        return sender_page_seq_no;
    }

    public void setSender_page_seq_no(String sender_page_seq_no){

        this.sender_page_seq_no = sender_page_seq_no;
    }

    @Override
    public String toString(){

        return "TalkChannelData{" +
                "message='" + message + '\'' +
                ", unread_message_count='" + unread_message_count + '\'' +
                ", channel_url='" + channel_url + '\'' +
                ", created_at='" + created_at + '\'' +
                ", sender_target_type='" + sender_target_type + '\'' +
                ", sender_target_seq_no='" + sender_target_seq_no + '\'' +
                ", sender_nickname='" + sender_nickname + '\'' +
                ", sender_profile_url='" + sender_profile_url + '\'' +
                ", sender_page_seq_no='" + sender_page_seq_no + '\'' +
                ", sender_store_name='" + sender_store_name + '\'' +
                ", recipient_nickname='" + recipient_nickname + '\'' +
                ", recipient_profile_url='" + recipient_profile_url + '\'' +
                '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.message);
        dest.writeString(this.unread_message_count);
        dest.writeString(this.channel_url);
        dest.writeString(this.created_at);
        dest.writeString(this.sender_target_type);
        dest.writeString(this.sender_target_seq_no);
        dest.writeString(this.sender_nickname);
        dest.writeString(this.sender_profile_url);
        dest.writeString(this.sender_page_seq_no);
        dest.writeString(this.sender_store_name);
        dest.writeString(this.recipient_nickname);
        dest.writeString(this.recipient_profile_url);
    }

    public TalkChannelData(){

    }

    protected TalkChannelData(Parcel in){

        this.message = in.readString();
        this.unread_message_count = in.readString();
        this.channel_url = in.readString();
        this.created_at = in.readString();
        this.sender_target_type = in.readString();
        this.sender_target_seq_no = in.readString();
        this.sender_nickname = in.readString();
        this.sender_profile_url = in.readString();
        this.sender_page_seq_no = in.readString();
        this.sender_store_name = in.readString();
        this.recipient_nickname = in.readString();
        this.recipient_profile_url = in.readString();
    }

    public static final Creator<TalkChannelData> CREATOR = new Creator<TalkChannelData>(){

        @Override
        public TalkChannelData createFromParcel(Parcel source){

            return new TalkChannelData(source);
        }

        @Override
        public TalkChannelData[] newArray(int size){

            return new TalkChannelData[size];
        }
    };
}
