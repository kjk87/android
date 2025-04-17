package com.pplus.prnumberuser.core.network.model.response.result;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by imac on 2017. 8. 30..
 */

public class ResultDaumKeyword implements Parcelable{

    private Meta meta;
    private List<Document> documents;

    public Meta getMeta(){

        return meta;
    }

    public void setMeta(Meta meta){

        this.meta = meta;
    }

    public List<Document> getDocuments(){

        return documents;
    }

    public void setDocuments(List<Document> documents){

        this.documents = documents;
    }

    @Override
    public String toString(){

        return "ResultDaumAddress{" + "meta=" + meta + ", documents=" + documents + '}';
    }

    public static class Document implements Parcelable{
        private String place_name;
        private String distance;
        private String place_url;
        private String category_name;
        private String address_name;
        private String road_address_name;
        private String id;
        private String phone;
        private String category_group_code;
        private String category_group_name;
        private Double x;
        private Double y;

        public String getPlace_name(){

            return place_name;
        }

        public void setPlace_name(String place_name){

            this.place_name = place_name;
        }

        public String getDistance(){

            return distance;
        }

        public void setDistance(String distance){

            this.distance = distance;
        }

        public String getPlace_url(){

            return place_url;
        }

        public void setPlace_url(String place_url){

            this.place_url = place_url;
        }

        public String getCategory_name(){

            return category_name;
        }

        public void setCategory_name(String category_name){

            this.category_name = category_name;
        }

        public String getAddress_name(){

            return address_name;
        }

        public void setAddress_name(String address_name){

            this.address_name = address_name;
        }

        public String getRoad_address_name(){

            return road_address_name;
        }

        public void setRoad_address_name(String road_address_name){

            this.road_address_name = road_address_name;
        }

        public String getId(){

            return id;
        }

        public void setId(String id){

            this.id = id;
        }

        public String getPhone(){

            return phone;
        }

        public void setPhone(String phone){

            this.phone = phone;
        }

        public String getCategory_group_code(){

            return category_group_code;
        }

        public void setCategory_group_code(String category_group_code){

            this.category_group_code = category_group_code;
        }

        public String getCategory_group_name(){

            return category_group_name;
        }

        public void setCategory_group_name(String category_group_name){

            this.category_group_name = category_group_name;
        }

        public Double getX(){

            return x;
        }

        public void setX(Double x){

            this.x = x;
        }

        public Double getY(){

            return y;
        }

        public void setY(Double y){

            this.y = y;
        }

        @Override
        public int describeContents(){

            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags){

            dest.writeString(this.place_name);
            dest.writeString(this.distance);
            dest.writeString(this.place_url);
            dest.writeString(this.category_name);
            dest.writeString(this.address_name);
            dest.writeString(this.road_address_name);
            dest.writeString(this.id);
            dest.writeString(this.phone);
            dest.writeString(this.category_group_code);
            dest.writeString(this.category_group_name);
            dest.writeValue(this.x);
            dest.writeValue(this.y);
        }

        public Document(){

        }

        protected Document(Parcel in){

            this.place_name = in.readString();
            this.distance = in.readString();
            this.place_url = in.readString();
            this.category_name = in.readString();
            this.address_name = in.readString();
            this.road_address_name = in.readString();
            this.id = in.readString();
            this.phone = in.readString();
            this.category_group_code = in.readString();
            this.category_group_name = in.readString();
            this.x = (Double) in.readValue(Double.class.getClassLoader());
            this.y = (Double) in.readValue(Double.class.getClassLoader());
        }

        public static final Creator<Document> CREATOR = new Creator<Document>(){

            @Override
            public Document createFromParcel(Parcel source){

                return new Document(source);
            }

            @Override
            public Document[] newArray(int size){

                return new Document[size];
            }
        };
    }

    public static class Meta implements Parcelable{

        private int total_count;

        public int getTotal_count(){

            return total_count;
        }

        public void setTotal_count(int total_count){

            this.total_count = total_count;
        }

        @Override
        public String toString(){

            return "Meta{" + "total_count=" + total_count + '}';
        }

        @Override
        public int describeContents(){

            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags){

            dest.writeInt(this.total_count);
        }

        public Meta(){

        }

        protected Meta(Parcel in){

            this.total_count = in.readInt();
        }

        public static final Creator<Meta> CREATOR = new Creator<Meta>(){

            @Override
            public Meta createFromParcel(Parcel source){

                return new Meta(source);
            }

            @Override
            public Meta[] newArray(int size){

                return new Meta[size];
            }
        };
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeParcelable(this.meta, flags);
        dest.writeTypedList(this.documents);
    }

    public ResultDaumKeyword(){

    }

    protected ResultDaumKeyword(Parcel in){

        this.meta = in.readParcelable(Meta.class.getClassLoader());
        this.documents = in.createTypedArrayList(Document.CREATOR);
    }

    public static final Creator<ResultDaumKeyword> CREATOR = new Creator<ResultDaumKeyword>(){

        @Override
        public ResultDaumKeyword createFromParcel(Parcel source){

            return new ResultDaumKeyword(source);
        }

        @Override
        public ResultDaumKeyword[] newArray(int size){

            return new ResultDaumKeyword[size];
        }
    };
}
