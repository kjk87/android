package com.pplus.prnumberbiz.core.network.model.dto;



import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by imac on 2017. 8. 17..
 */

public class Fan implements Parcelable{
    private Long fanNo;
    private Long no;
    private String loginId;
    private String name;
    private String password;
    private String accountType = "pplus";
    private String platform = "aos";
    private String memberType; //MemberTypeCode
    private String useStatus;
    private String restrictionStatus; //RestrictionStatusCode
    private String restrictionClsDate;
    private String nickname;
    private String mobile;
    private Verification verification;
    private String joinDate;
    private String regType;
    private Page page;
    private String lastLoginDate;
    private Integer loginFailCount;
    private Long contactVersion;
    private String reqLeaveDate;
    private boolean calculated;
    private boolean sendbirdUser;
    private String talkRecvBound; // TalkReceiveBoundsCode
    private String talkDenyDay;
    private String talkDenyStartTime;
    private String talkDenyEndTime;
    private String modDate;
    private Device device;
    private String sessionKey;
    private List<No> termsList;
    private No country = new No(1l);
    private int totalCash;
    private int totalBol;
    private ImgUrl profileImage;
    private String recommendKey;
    private String normalNumberCount;
    private PRNumber number;
    private String gender;
    private String birthday;
    private String recommendationCode;//가입시 추천인 코드

    @Override
    public boolean equals(Object o){

        if(o == null) return false;

        if(o instanceof Fan) {
            return (((Fan) o).no.equals(no));
        } else {
            return false;
        }
    }

    @Override
    public String toString(){

        return "Fan{" + "fanNo=" + fanNo + ", no=" + no + ", loginId='" + loginId + '\'' + ", name='" + name + '\'' + ", password='" + password + '\'' + ", accountType='" + accountType + '\'' + ", platform='" + platform + '\'' + ", memberType='" + memberType + '\'' + ", useStatus='" + useStatus + '\'' + ", restrictionStatus='" + restrictionStatus + '\'' + ", restrictionClsDate='" + restrictionClsDate + '\'' + ", nickname='" + nickname + '\'' + ", mobile='" + mobile + '\'' + ", verification=" + verification + ", joinDate='" + joinDate + '\'' + ", regType='" + regType + '\'' + ", page=" + page + ", lastLoginDate='" + lastLoginDate + '\'' + ", loginFailCount=" + loginFailCount + ", contactVersion=" + contactVersion + ", reqLeaveDate='" + reqLeaveDate + '\'' + ", calculated=" + calculated + ", sendbirdUser=" + sendbirdUser + ", talkRecvBound='" + talkRecvBound + '\'' + ", talkDenyDay='" + talkDenyDay + '\'' + ", talkDenyStartTime='" + talkDenyStartTime + '\'' + ", talkDenyEndTime='" + talkDenyEndTime + '\'' + ", modDate='" + modDate + '\'' + ", device=" + device + ", sessionKey='" + sessionKey + '\'' + ", termsList=" + termsList + ", country=" + country + ", totalCash=" + totalCash + ", totalBol=" + totalBol + ", profileImage=" + profileImage + ", recommendKey='" + recommendKey + '\'' + ", normalNumberCount='" + normalNumberCount + '\'' + ", number=" + number + ", gender='" + gender + '\'' + ", birthday='" + birthday + '\'' + ", recommendationCode='" + recommendationCode + '\'' + '}';
    }

    public Long getFanNo(){

        return fanNo;
    }

    public void setFanNo(Long fanNo){

        this.fanNo = fanNo;
    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getLoginId(){

        return loginId;
    }

    public void setLoginId(String loginId){

        this.loginId = loginId;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    public String getPassword(){

        return password;
    }

    public void setPassword(String password){

        this.password = password;
    }

    public String getAccountType(){

        return accountType;
    }

    public void setAccountType(String accountType){

        this.accountType = accountType;
    }

    public String getPlatform(){

        return platform;
    }

    public void setPlatform(String platform){

        this.platform = platform;
    }

    public String getMemberType(){

        return memberType;
    }

    public void setMemberType(String memberType){

        this.memberType = memberType;
    }

    public String getUseStatus(){

        return useStatus;
    }

    public void setUseStatus(String useStatus){

        this.useStatus = useStatus;
    }

    public String getRestrictionStatus(){

        return restrictionStatus;
    }

    public void setRestrictionStatus(String restrictionStatus){

        this.restrictionStatus = restrictionStatus;
    }

    public String getRestrictionClsDate(){

        return restrictionClsDate;
    }

    public void setRestrictionClsDate(String restrictionClsDate){

        this.restrictionClsDate = restrictionClsDate;
    }

    public String getNickname(){

        return nickname;
    }

    public void setNickname(String nickname){

        this.nickname = nickname;
    }

    public String getMobile(){

        return mobile;
    }

    public void setMobile(String mobile){

        this.mobile = mobile;
    }

    public Verification getVerification(){

        return verification;
    }

    public void setVerification(Verification verification){

        this.verification = verification;
    }

    public String getJoinDate(){

        return joinDate;
    }

    public void setJoinDate(String joinDate){

        this.joinDate = joinDate;
    }

    public String getRegType(){

        return regType;
    }

    public void setRegType(String regType){

        this.regType = regType;
    }

    public Page getPage(){

        return page;
    }

    public void setPage(Page page){

        this.page = page;
    }

    public String getLastLoginDate(){

        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate){

        this.lastLoginDate = lastLoginDate;
    }

    public Integer getLoginFailCount(){

        return loginFailCount;
    }

    public void setLoginFailCount(Integer loginFailCount){

        this.loginFailCount = loginFailCount;
    }

    public Long getContactVersion(){

        return contactVersion;
    }

    public void setContactVersion(Long contactVersion){

        this.contactVersion = contactVersion;
    }

    public String getReqLeaveDate(){

        return reqLeaveDate;
    }

    public void setReqLeaveDate(String reqLeaveDate){

        this.reqLeaveDate = reqLeaveDate;
    }

    public boolean isCalculated(){

        return calculated;
    }

    public void setCalculated(boolean calculated){

        this.calculated = calculated;
    }

    public boolean isSendbirdUser(){

        return sendbirdUser;
    }

    public void setSendbirdUser(boolean sendbirdUser){

        this.sendbirdUser = sendbirdUser;
    }

    public String getTalkRecvBound(){

        return talkRecvBound;
    }

    public void setTalkRecvBound(String talkRecvBound){

        this.talkRecvBound = talkRecvBound;
    }

    public String getTalkDenyDay(){

        return talkDenyDay;
    }

    public void setTalkDenyDay(String talkDenyDay){

        this.talkDenyDay = talkDenyDay;
    }

    public String getTalkDenyStartTime(){

        return talkDenyStartTime;
    }

    public void setTalkDenyStartTime(String talkDenyStartTime){

        this.talkDenyStartTime = talkDenyStartTime;
    }

    public String getTalkDenyEndTime(){

        return talkDenyEndTime;
    }

    public void setTalkDenyEndTime(String talkDenyEndTime){

        this.talkDenyEndTime = talkDenyEndTime;
    }

    public String getModDate(){

        return modDate;
    }

    public void setModDate(String modDate){

        this.modDate = modDate;
    }

    public Device getDevice(){

        return device;
    }

    public void setDevice(Device device){

        this.device = device;
    }

    public String getSessionKey(){

        return sessionKey;
    }

    public void setSessionKey(String sessionKey){

        this.sessionKey = sessionKey;
    }

    public List<No> getTermsList(){

        return termsList;
    }

    public void setTermsList(List<No> termsList){

        this.termsList = termsList;
    }

    public No getCountry(){

        return country;
    }

    public void setCountry(No country){

        this.country = country;
    }

    public int getTotalCash(){

        return totalCash;
    }

    public void setTotalCash(int totalCash){

        this.totalCash = totalCash;
    }

    public int getTotalBol(){

        return totalBol;
    }

    public void setTotalBol(int totalBol){

        this.totalBol = totalBol;
    }

    public ImgUrl getProfileImage(){

        return profileImage;
    }

    public void setProfileImage(ImgUrl profileImage){

        this.profileImage = profileImage;
    }

    public String getRecommendKey(){

        return recommendKey;
    }

    public void setRecommendKey(String recommendKey){

        this.recommendKey = recommendKey;
    }

    public String getNormalNumberCount(){

        return normalNumberCount;
    }

    public void setNormalNumberCount(String normalNumberCount){

        this.normalNumberCount = normalNumberCount;
    }

    public PRNumber getNumber(){

        return number;
    }

    public void setNumber(PRNumber number){

        this.number = number;
    }

    public String getGender(){

        return gender;
    }

    public void setGender(String gender){

        this.gender = gender;
    }

    public String getBirthday(){

        return birthday;
    }

    public void setBirthday(String birthday){

        this.birthday = birthday;
    }

    public String getRecommendationCode(){

        return recommendationCode;
    }

    public void setRecommendationCode(String recommendationCode){

        this.recommendationCode = recommendationCode;
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.fanNo);
        dest.writeValue(this.no);
        dest.writeString(this.loginId);
        dest.writeString(this.name);
        dest.writeString(this.password);
        dest.writeString(this.accountType);
        dest.writeString(this.platform);
        dest.writeString(this.memberType);
        dest.writeString(this.useStatus);
        dest.writeString(this.restrictionStatus);
        dest.writeString(this.restrictionClsDate);
        dest.writeString(this.nickname);
        dest.writeString(this.mobile);
        dest.writeParcelable(this.verification, flags);
        dest.writeString(this.joinDate);
        dest.writeString(this.regType);
        dest.writeParcelable(this.page, flags);
        dest.writeString(this.lastLoginDate);
        dest.writeValue(this.loginFailCount);
        dest.writeValue(this.contactVersion);
        dest.writeString(this.reqLeaveDate);
        dest.writeByte(this.calculated ? (byte) 1 : (byte) 0);
        dest.writeByte(this.sendbirdUser ? (byte) 1 : (byte) 0);
        dest.writeString(this.talkRecvBound);
        dest.writeString(this.talkDenyDay);
        dest.writeString(this.talkDenyStartTime);
        dest.writeString(this.talkDenyEndTime);
        dest.writeString(this.modDate);
        dest.writeParcelable(this.device, flags);
        dest.writeString(this.sessionKey);
        dest.writeTypedList(this.termsList);
        dest.writeParcelable(this.country, flags);
        dest.writeInt(this.totalCash);
        dest.writeInt(this.totalBol);
        dest.writeParcelable(this.profileImage, flags);
        dest.writeString(this.recommendKey);
        dest.writeString(this.normalNumberCount);
        dest.writeParcelable(this.number, flags);
        dest.writeString(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.recommendationCode);
    }

    public Fan(){

    }

    protected Fan(Parcel in){

        this.fanNo = (Long) in.readValue(Long.class.getClassLoader());
        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.loginId = in.readString();
        this.name = in.readString();
        this.password = in.readString();
        this.accountType = in.readString();
        this.platform = in.readString();
        this.memberType = in.readString();
        this.useStatus = in.readString();
        this.restrictionStatus = in.readString();
        this.restrictionClsDate = in.readString();
        this.nickname = in.readString();
        this.mobile = in.readString();
        this.verification = in.readParcelable(Verification.class.getClassLoader());
        this.joinDate = in.readString();
        this.regType = in.readString();
        this.page = in.readParcelable(Page.class.getClassLoader());
        this.lastLoginDate = in.readString();
        this.loginFailCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.contactVersion = (Long) in.readValue(Long.class.getClassLoader());
        this.reqLeaveDate = in.readString();
        this.calculated = in.readByte() != 0;
        this.sendbirdUser = in.readByte() != 0;
        this.talkRecvBound = in.readString();
        this.talkDenyDay = in.readString();
        this.talkDenyStartTime = in.readString();
        this.talkDenyEndTime = in.readString();
        this.modDate = in.readString();
        this.device = in.readParcelable(Device.class.getClassLoader());
        this.sessionKey = in.readString();
        this.termsList = in.createTypedArrayList(No.CREATOR);
        this.country = in.readParcelable(No.class.getClassLoader());
        this.totalCash = in.readInt();
        this.totalBol = in.readInt();
        this.profileImage = in.readParcelable(ImgUrl.class.getClassLoader());
        this.recommendKey = in.readString();
        this.normalNumberCount = in.readString();
        this.number = in.readParcelable(PRNumber.class.getClassLoader());
        this.gender = in.readString();
        this.birthday = in.readString();
        this.recommendationCode = in.readString();
    }

    public static final Creator<Fan> CREATOR = new Creator<Fan>(){

        @Override
        public Fan createFromParcel(Parcel source){

            return new Fan(source);
        }

        @Override
        public Fan[] newArray(int size){

            return new Fan[size];
        }
    };
}
