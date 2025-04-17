package com.pplus.luckybol.core.network.model.response.result;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by imac on 2017. 8. 30..
 */

public class ResultDaumAddress implements Parcelable{

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

        private RoadAddress road_address;
        private Address address;

        public RoadAddress getRoad_address(){

            return road_address;
        }

        public void setRoad_address(RoadAddress road_address){

            this.road_address = road_address;
        }

        public Address getAddress(){

            return address;
        }

        public void setAddress(Address address){

            this.address = address;
        }

        @Override
        public String toString(){

            return "Document{" + "road_address=" + road_address + ", address=" + address + '}';
        }

        public static class RoadAddress implements Parcelable{

            private String address_name;
            private String zone_no;

            public String getAddress_name(){

                return address_name;
            }

            public void setAddress_name(String address_name){

                this.address_name = address_name;
            }

            public String getZone_no(){

                return zone_no;
            }

            public void setZone_no(String zone_no){

                this.zone_no = zone_no;
            }

            @Override
            public String toString(){

                return "RoadAddress{" + "address_name='" + address_name + '\'' + ", zone_no='" + zone_no + '\'' + '}';
            }

            @Override
            public int describeContents(){

                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags){

                dest.writeString(this.address_name);
                dest.writeString(this.zone_no);
            }

            public RoadAddress(){

            }

            protected RoadAddress(Parcel in){

                this.address_name = in.readString();
                this.zone_no = in.readString();
            }

            public static final Creator<RoadAddress> CREATOR = new Creator<RoadAddress>(){

                @Override
                public RoadAddress createFromParcel(Parcel source){

                    return new RoadAddress(source);
                }

                @Override
                public RoadAddress[] newArray(int size){

                    return new RoadAddress[size];
                }
            };
        }

        public static class Address implements Parcelable{
            private String address_name;
            private String zip_code;

            public String getAddress_name(){

                return address_name;
            }

            public void setAddress_name(String address_name){

                this.address_name = address_name;
            }

            public String getZip_code(){

                return zip_code;
            }

            public void setZip_code(String zip_code){

                this.zip_code = zip_code;
            }

            @Override
            public String toString(){

                return "Address{" + "address_name='" + address_name + '\'' + ", zip_code='" + zip_code + '\'' + '}';
            }

            @Override
            public int describeContents(){

                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags){

                dest.writeString(this.address_name);
                dest.writeString(this.zip_code);
            }

            public Address(){

            }

            protected Address(Parcel in){

                this.address_name = in.readString();
                this.zip_code = in.readString();
            }

            public static final Creator<Address> CREATOR = new Creator<Address>(){

                @Override
                public Address createFromParcel(Parcel source){

                    return new Address(source);
                }

                @Override
                public Address[] newArray(int size){

                    return new Address[size];
                }
            };
        }

        @Override
        public int describeContents(){

            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags){

            dest.writeParcelable(this.road_address, flags);
            dest.writeParcelable(this.address, flags);
        }

        public Document(){

        }

        protected Document(Parcel in){

            this.road_address = in.readParcelable(RoadAddress.class.getClassLoader());
            this.address = in.readParcelable(Address.class.getClassLoader());
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

    public ResultDaumAddress(){

    }

    protected ResultDaumAddress(Parcel in){

        this.meta = in.readParcelable(Meta.class.getClassLoader());
        this.documents = in.createTypedArrayList(Document.CREATOR);
    }

    public static final Creator<ResultDaumAddress> CREATOR = new Creator<ResultDaumAddress>(){

        @Override
        public ResultDaumAddress createFromParcel(Parcel source){

            return new ResultDaumAddress(source);
        }

        @Override
        public ResultDaumAddress[] newArray(int size){

            return new ResultDaumAddress[size];
        }
    };
}
