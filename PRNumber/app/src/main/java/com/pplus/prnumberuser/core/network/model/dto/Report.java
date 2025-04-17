package com.pplus.prnumberuser.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by j2n on 2016. 10. 10..
 */

public class Report implements Parcelable{
    private Long no;
    private No reporter;
    private No target;
    private String targetType;
    private String regDate;
    private String reason;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public No getReporter(){

        return reporter;
    }

    public void setReporter(No reporter){

        this.reporter = reporter;
    }

    public No getTarget(){

        return target;
    }

    public void setTarget(No target){

        this.target = target;
    }

    public String getTargetType(){

        return targetType;
    }

    public void setTargetType(String targetType){

        this.targetType = targetType;
    }

    public String getRegDate(){

        return regDate;
    }

    public void setRegDate(String regDate){

        this.regDate = regDate;
    }

    public String getReason(){

        return reason;
    }

    public void setReason(String reason){

        this.reason = reason;
    }

    @Override
    public String toString(){

        return "Report{" + "no=" + no + ", reporter=" + reporter + ", target=" + target + ", targetType='" + targetType + '\'' + ", regDate='" + regDate + '\'' + ", reason='" + reason + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeParcelable(this.reporter, flags);
        dest.writeParcelable(this.target, flags);
        dest.writeString(this.targetType);
        dest.writeString(this.regDate);
        dest.writeString(this.reason);
    }

    public Report(){

    }

    protected Report(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.reporter = in.readParcelable(No.class.getClassLoader());
        this.target = in.readParcelable(No.class.getClassLoader());
        this.targetType = in.readString();
        this.regDate = in.readString();
        this.reason = in.readString();
    }

    public static final Creator<Report> CREATOR = new Creator<Report>(){

        @Override
        public Report createFromParcel(Parcel source){

            return new Report(source);
        }

        @Override
        public Report[] newArray(int size){

            return new Report[size];
        }
    };
}
