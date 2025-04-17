package com.pplus.prnumberuser.core.network.model.response.result;

import android.os.Parcel;
import android.os.Parcelable;

import com.pplus.prnumberuser.core.network.model.dto.StepAddress;

import java.util.List;

/**
 * Created by imac on 2017. 11. 27..
 */

public class ResultStepAddress implements Parcelable{

    private String id;
    private List<StepAddress> result;

    public String getId(){

        return id;
    }

    public void setId(String id){

        this.id = id;
    }

    public List<StepAddress> getResult(){

        return result;
    }

    public void setResult(List<StepAddress> result){

        this.result = result;
    }

    @Override
    public String toString(){

        return "ResultStepAddress{" + "id='" + id + '\'' + ", result=" + result + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.id);
        dest.writeTypedList(this.result);
    }

    public ResultStepAddress(){

    }

    protected ResultStepAddress(Parcel in){

        this.id = in.readString();
        this.result = in.createTypedArrayList(StepAddress.CREATOR);
    }

    public static final Parcelable.Creator<ResultStepAddress> CREATOR = new Parcelable.Creator<ResultStepAddress>(){

        @Override
        public ResultStepAddress createFromParcel(Parcel source){

            return new ResultStepAddress(source);
        }

        @Override
        public ResultStepAddress[] newArray(int size){

            return new ResultStepAddress[size];
        }
    };
}
