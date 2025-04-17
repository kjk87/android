package com.pplus.prnumberbiz.core.network.model.dto;

import android.os.Parcel;
import android.os.Parcelable;


public class GalleryData implements Parcelable{

    private long id;
    private String imageUrl;
    private String imageType;
    private String folder;
    private String mimeType;
    private int size;
    private int orientation;
    private boolean checked;
    private boolean isBroken;


    @Override
    public boolean equals(Object obj){

        if(obj == this) {
            return true;
        }

        if(!(obj instanceof GalleryData)) {
            return false;
        }

        GalleryData other = (GalleryData) obj;

        return this.imageUrl.equals(other.imageUrl);
    }

    public GalleryData(){

        this.checked = false;
        this.isBroken = false;
    }

    public void setChecked(boolean checked){

        this.checked = checked;
    }

    public boolean getChecked(){

        return this.checked;
    }

    public boolean isBroken(){

        return isBroken;
    }

    public void setIsBroken(boolean isBroken){

        this.isBroken = isBroken;
    }

    public long getId(){

        return id;
    }

    public void setId(long id){

        this.id = id;
    }

    public String getFolder(){

        return folder;
    }

    public void setFolder(String folder){

        this.folder = folder;
    }

    public int getOrientation(){

        return orientation;
    }

    public void setOrientation(int orientation){

        this.orientation = orientation;
    }

    public String getImageUrl(){

        return imageUrl;
    }

    public void setImageUrl(String imageUrl){

        this.imageUrl = imageUrl;
    }

    public String getImageType(){

        return imageType;
    }

    public void setImageType(String imageType){

        this.imageType = imageType;
    }

    public String getMimeType(){

        return mimeType;
    }

    public void setMimeType(String mimeType){

        this.mimeType = mimeType;
    }

    public int getSize(){

        return size;
    }

    public void setSize(int size){

        this.size = size;
    }

    @Override
    public String toString(){

        return "GalleryData{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageType='" + imageType + '\'' +
                ", folder='" + folder + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", size=" + size +
                ", orientation=" + orientation +
                ", checked=" + checked +
                ", isBroken=" + isBroken +
                '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeLong(this.id);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageType);
        dest.writeString(this.folder);
        dest.writeString(this.mimeType);
        dest.writeInt(this.size);
        dest.writeInt(this.orientation);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isBroken ? (byte) 1 : (byte) 0);
    }

    protected GalleryData(Parcel in){

        this.id = in.readLong();
        this.imageUrl = in.readString();
        this.imageType = in.readString();
        this.folder = in.readString();
        this.mimeType = in.readString();
        this.size = in.readInt();
        this.orientation = in.readInt();
        this.checked = in.readByte() != 0;
        this.isBroken = in.readByte() != 0;
    }

    public static final Creator<GalleryData> CREATOR = new Creator<GalleryData>(){

        @Override
        public GalleryData createFromParcel(Parcel source){

            return new GalleryData(source);
        }

        @Override
        public GalleryData[] newArray(int size){

            return new GalleryData[size];
        }
    };
}
