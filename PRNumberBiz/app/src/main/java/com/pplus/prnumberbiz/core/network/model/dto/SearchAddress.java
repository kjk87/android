package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by j2n on 2016. 7. 26..
 */

public class SearchAddress implements Parcelable{

    private Common common;
    private List<Juso> juso;

    public Common getCommon(){

        return common;
    }

    public void setCommon(Common common){

        this.common = common;
    }

    public List<Juso> getJuso(){

        return juso;
    }

    public void setJuso(List<Juso> juso){

        this.juso = juso;
    }

    public static class Common implements Parcelable{

        private String errorMessage;
        private String countPerPage;
        private Integer totalCount;
        private Integer errorCode;
        private Integer currentPage;

        public String getErrorMessage(){

            return errorMessage;
        }

        public void setErrorMessage(String errorMessage){

            this.errorMessage = errorMessage;
        }

        public String getCountPerPage(){

            return countPerPage;
        }

        public void setCountPerPage(String countPerPage){

            this.countPerPage = countPerPage;
        }

        public Integer getTotalCount(){

            return totalCount;
        }

        public void setTotalCount(Integer totalCount){

            this.totalCount = totalCount;
        }

        public Integer getErrorCode(){

            return errorCode;
        }

        public void setErrorCode(Integer errorCode){

            this.errorCode = errorCode;
        }

        public Integer getCurrentPage(){

            return currentPage;
        }

        public void setCurrentPage(Integer currentPage){

            this.currentPage = currentPage;
        }

        @Override
        public String toString(){

            return "Common{" + "errorMessage='" + errorMessage + '\'' + ", countPerPage='" + countPerPage + '\'' + ", totalCount=" + totalCount + ", errorCode=" + errorCode + ", currentPage=" + currentPage + '}';
        }

        @Override
        public int describeContents(){

            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags){

            dest.writeString(this.errorMessage);
            dest.writeString(this.countPerPage);
            dest.writeValue(this.totalCount);
            dest.writeValue(this.errorCode);
            dest.writeValue(this.currentPage);
        }

        public Common(){

        }

        protected Common(Parcel in){

            this.errorMessage = in.readString();
            this.countPerPage = in.readString();
            this.totalCount = (Integer) in.readValue(Integer.class.getClassLoader());
            this.errorCode = (Integer) in.readValue(Integer.class.getClassLoader());
            this.currentPage = (Integer) in.readValue(Integer.class.getClassLoader());
        }

        public static final Parcelable.Creator<Common> CREATOR = new Parcelable.Creator<Common>(){

            @Override
            public Common createFromParcel(Parcel source){

                return new Common(source);
            }

            @Override
            public Common[] newArray(int size){

                return new Common[size];
            }
        };
    }

    @Override
    public String toString(){

        return "SearchAddress{" + "common=" + common + ", juso=" + juso + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeParcelable(this.common, flags);
        dest.writeTypedList(this.juso);
    }

    public SearchAddress(){

    }

    protected SearchAddress(Parcel in){

        this.common = in.readParcelable(Common.class.getClassLoader());
        this.juso = in.createTypedArrayList(Juso.CREATOR);
    }

    public static final Parcelable.Creator<SearchAddress> CREATOR = new Parcelable.Creator<SearchAddress>(){

        @Override
        public SearchAddress createFromParcel(Parcel source){

            return new SearchAddress(source);
        }

        @Override
        public SearchAddress[] newArray(int size){

            return new SearchAddress[size];
        }
    };
}
