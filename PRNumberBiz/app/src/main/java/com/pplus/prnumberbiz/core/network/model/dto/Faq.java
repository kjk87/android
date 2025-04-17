package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ksh on 2016-09-28.
 */

public class Faq implements Parcelable{

    private Long no;
    private FaqGroup group;
    private String status;
    private String subject;
    private String contents;
    private Integer priority;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public FaqGroup getGroup(){

        return group;
    }

    public void setGroup(FaqGroup group){

        this.group = group;
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

    @Override
    public String toString(){

        return "Faq{" + "no=" + no + ", group=" + group + ", status='" + status + '\'' + ", subject='" + subject + '\'' + ", contents='" + contents + '\'' + ", priority=" + priority + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeParcelable(this.group, flags);
        dest.writeString(this.status);
        dest.writeString(this.subject);
        dest.writeString(this.contents);
        dest.writeValue(this.priority);
    }

    public Faq(){

    }

    protected Faq(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.group = in.readParcelable(FaqGroup.class.getClassLoader());
        this.status = in.readString();
        this.subject = in.readString();
        this.contents = in.readString();
        this.priority = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Faq> CREATOR = new Parcelable.Creator<Faq>(){

        @Override
        public Faq createFromParcel(Parcel source){

            return new Faq(source);
        }

        @Override
        public Faq[] newArray(int size){

            return new Faq[size];
        }
    };
}
