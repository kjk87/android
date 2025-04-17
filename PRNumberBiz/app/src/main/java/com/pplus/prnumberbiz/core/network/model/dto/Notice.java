package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksh on 2016-09-28.
 */

public class Notice implements Parcelable{

    private Long no;
    private String status;
    private String subject;
    private String contents;
    private Integer priority;
    private Duration duration;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public String getSubject(){

        return subject;
    }

    public void setSubject(String subject){

        this.subject = subject;
    }

    public String getContents(){

        return contents;
    }

    public void setContents(String contents){

        this.contents = contents;
    }

    public Integer getPriority(){

        return priority;
    }

    public void setPriority(Integer priority){

        this.priority = priority;
    }

    public Duration getDuration(){

        return duration;
    }

    public void setDuration(Duration duration){

        this.duration = duration;
    }

    @Override
    public String toString(){

        return "Notice{" + "no=" + no + ", status='" + status + '\'' + ", subject='" + subject + '\'' + ", contents='" + contents + '\'' + ", priority=" + priority + ", duration=" + duration + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.status);
        dest.writeString(this.subject);
        dest.writeString(this.contents);
        dest.writeValue(this.priority);
        dest.writeParcelable(this.duration, flags);
    }

    public Notice(){

    }

    protected Notice(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.status = in.readString();
        this.subject = in.readString();
        this.contents = in.readString();
        this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
        this.duration = in.readParcelable(Duration.class.getClassLoader());
    }

    public static final Parcelable.Creator<Notice> CREATOR = new Parcelable.Creator<Notice>(){

        @Override
        public Notice createFromParcel(Parcel source){

            return new Notice(source);
        }

        @Override
        public Notice[] newArray(int size){

            return new Notice[size];
        }
    };
}
