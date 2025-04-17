package com.pplus.prnumberbiz.core.database.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity mapped to table "CONTACT".
 */
public class Contact implements Parcelable{

    private String mobileNumber;
    private String imageUrl;
    private String memberName;
    private String memberNickname;
    private Long memberNo;
    private String pageName;
    private Long pageNo;
    private String virtualNumber;
    private Boolean delete;
    private Boolean update;
    private String blogUrl;
    private String homepageUrl;
    private String pageType;
    private String pageProfileImageUrl;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Contact() {
    }

    public Contact(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Contact(String mobileNumber, String imageUrl, String memberName, String memberNickname, Long memberNo, String pageName, Long pageNo, String virtualNumber, Boolean delete, Boolean update, String blogUrl, String homepageUrl, String pageType, String pageProfileImageUrl) {
        this.mobileNumber = mobileNumber;
        this.imageUrl = imageUrl;
        this.memberName = memberName;
        this.memberNickname = memberNickname;
        this.memberNo = memberNo;
        this.pageName = pageName;
        this.pageNo = pageNo;
        this.virtualNumber = virtualNumber;
        this.delete = delete;
        this.update = update;
        this.blogUrl = blogUrl;
        this.homepageUrl = homepageUrl;
        this.pageType = pageType;
        this.pageProfileImageUrl = pageProfileImageUrl;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public Long getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(Long memberNo) {
        this.memberNo = memberNo;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public String getVirtualNumber() {
        return virtualNumber;
    }

    public void setVirtualNumber(String virtualNumber) {
        this.virtualNumber = virtualNumber;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public String getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getPageProfileImageUrl() {
        return pageProfileImageUrl;
    }

    public void setPageProfileImageUrl(String pageProfileImageUrl) {
        this.pageProfileImageUrl = pageProfileImageUrl;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.mobileNumber);
        dest.writeString(this.imageUrl);
        dest.writeString(this.memberName);
        dest.writeString(this.memberNickname);
        dest.writeValue(this.memberNo);
        dest.writeString(this.pageName);
        dest.writeValue(this.pageNo);
        dest.writeString(this.virtualNumber);
        dest.writeValue(this.delete);
        dest.writeValue(this.update);
        dest.writeString(this.blogUrl);
        dest.writeString(this.homepageUrl);
        dest.writeString(this.pageType);
        dest.writeString(this.pageProfileImageUrl);
    }

    protected Contact(Parcel in){

        this.mobileNumber = in.readString();
        this.imageUrl = in.readString();
        this.memberName = in.readString();
        this.memberNickname = in.readString();
        this.memberNo = (Long) in.readValue(Long.class.getClassLoader());
        this.pageName = in.readString();
        this.pageNo = (Long) in.readValue(Long.class.getClassLoader());
        this.virtualNumber = in.readString();
        this.delete = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.update = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.blogUrl = in.readString();
        this.homepageUrl = in.readString();
        this.pageType = in.readString();
        this.pageProfileImageUrl = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>(){

        @Override
        public Contact createFromParcel(Parcel source){

            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size){

            return new Contact[size];
        }
    };
}
