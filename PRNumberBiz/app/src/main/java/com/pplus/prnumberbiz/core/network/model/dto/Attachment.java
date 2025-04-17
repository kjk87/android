package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by j2n on 2016. 9. 29..
 */

public class Attachment implements Parcelable{

    private Long no;

    private Long targetNo;

    private String targetType;

    private String originName;

    private String filePath;

    private String fileName;

    private String fileSize;

    private String extension;

    private String url;

    private int width;

    private int height;

    private int rotate;

    private String id;

    @Override
    public boolean equals(Object o){

        if(o == null) return false;

        if(o instanceof Attachment) {
            return (((Attachment) o).no.equals(no));
        } else {
            return false;
        }
    }

    public String getId(){

        return id;
    }

    public void setId(String id){

        this.id = id;
    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public Long getTargetNo(){

        return targetNo;
    }

    public void setTargetNo(Long targetNo){

        this.targetNo = targetNo;
    }

    public String getTargetType(){

        return targetType;
    }

    public void setTargetType(String targetType){

        this.targetType = targetType;
    }

    public String getOriginName(){

        return originName;
    }

    public void setOriginName(String originName){

        this.originName = originName;
    }

    public String getFilePath(){

        return filePath;
    }

    public void setFilePath(String filePath){

        this.filePath = filePath;
    }

    public String getFileName(){

        return fileName;
    }

    public void setFileName(String fileName){

        this.fileName = fileName;
    }

    public String getFileSize(){

        return fileSize;
    }

    public void setFileSize(String fileSize){

        this.fileSize = fileSize;
    }

    public String getExtension(){

        return extension;
    }

    public void setExtension(String extension){

        this.extension = extension;
    }

    public String getUrl(){

        return url;
    }

    public void setUrl(String url){

        this.url = url;
    }

    public int getWidth(){

        return width;
    }

    public void setWidth(int width){

        this.width = width;
    }

    public int getHeight(){

        return height;
    }

    public void setHeight(int height){

        this.height = height;
    }

    public int getRotate(){

        return rotate;
    }

    public void setRotate(int rotate){

        this.rotate = rotate;
    }

    @Override
    public String toString(){

        return "Attachment{" + "no=" + no + ", targetNo=" + targetNo + ", targetType='" + targetType + '\'' + ", originName='" + originName + '\'' + ", filePath='" + filePath + '\'' + ", fileName='" + fileName + '\'' + ", fileSize='" + fileSize + '\'' + ", extension='" + extension + '\'' + ", url='" + url + '\'' + ", width=" + width + ", height=" + height + ", rotate=" + rotate + '}';
    }

    public Attachment(){

    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeValue(this.targetNo);
        dest.writeString(this.targetType);
        dest.writeString(this.originName);
        dest.writeString(this.filePath);
        dest.writeString(this.fileName);
        dest.writeString(this.fileSize);
        dest.writeString(this.extension);
        dest.writeString(this.url);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.rotate);
        dest.writeString(this.id);
    }

    protected Attachment(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.targetNo = (Long) in.readValue(Long.class.getClassLoader());
        this.targetType = in.readString();
        this.originName = in.readString();
        this.filePath = in.readString();
        this.fileName = in.readString();
        this.fileSize = in.readString();
        this.extension = in.readString();
        this.url = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.rotate = in.readInt();
        this.id = in.readString();
    }

    public static final Creator<Attachment> CREATOR = new Creator<Attachment>(){

        @Override
        public Attachment createFromParcel(Parcel source){

            return new Attachment(source);
        }

        @Override
        public Attachment[] newArray(int size){

            return new Attachment[size];
        }
    };
}
