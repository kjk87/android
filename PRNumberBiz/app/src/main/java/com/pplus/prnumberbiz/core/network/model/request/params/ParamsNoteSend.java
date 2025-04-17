package com.pplus.prnumberbiz.core.network.model.request.params;

import android.os.Parcel;
import android.os.Parcelable;

import com.pplus.prnumberbiz.core.network.model.dto.No;

import java.util.List;

/**
 * Created by imac on 2017. 9. 28..
 */

public class ParamsNoteSend implements Parcelable{
    private Long no;
    private String contents;
    private No origin;
    private List<No> receiverList;

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getContents(){

        return contents;
    }

    public void setContents(String contents){

        this.contents = contents;
    }

    public No getOrigin(){

        return origin;
    }

    public void setOrigin(No origin){

        this.origin = origin;
    }

    public List<No> getReceiverList(){

        return receiverList;
    }

    public void setReceiverList(List<No> receiverList){

        this.receiverList = receiverList;
    }

    public ParamsNoteSend(){

    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.contents);
        dest.writeParcelable(this.origin, flags);
        dest.writeTypedList(this.receiverList);
    }

    protected ParamsNoteSend(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.contents = in.readString();
        this.origin = in.readParcelable(No.class.getClassLoader());
        this.receiverList = in.createTypedArrayList(No.CREATOR);
    }

    public static final Creator<ParamsNoteSend> CREATOR = new Creator<ParamsNoteSend>(){

        @Override
        public ParamsNoteSend createFromParcel(Parcel source){

            return new ParamsNoteSend(source);
        }

        @Override
        public ParamsNoteSend[] newArray(int size){

            return new ParamsNoteSend[size];
        }
    };
}
