package com.pplus.luckybol.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by imac on 2017. 6. 29..
 */

public class SearchAddressJuso implements Parcelable{
    private String detBdNmList;
    private String engAddr;
    private String rn;
    private String emdNm;
    private String zipNo;
    private String roadAddrPart2;
    private String emdNo;
    private String sggNm;
    private String jibunAddr;
    private String siNm;
    private String roadAddrPart1;
    private String bdNm;
    private String admCd;
    private String udrtYn;
    private String lnbrMnnm;
    private String roadAddr;
    private String lnbrSlno;
    private String buldMnnm;
    private String bdKdcd;
    private String liNm;
    private String rnMgtSn;
    private String mtYn;
    private String bdMgtSn;
    private String buldSlno;

    public String getDetBdNmList(){

        return detBdNmList;
    }

    public void setDetBdNmList(String detBdNmList){

        this.detBdNmList = detBdNmList;
    }

    public String getEngAddr(){

        return engAddr;
    }

    public void setEngAddr(String engAddr){

        this.engAddr = engAddr;
    }

    public String getRn(){

        return rn;
    }

    public void setRn(String rn){

        this.rn = rn;
    }

    public String getEmdNm(){

        return emdNm;
    }

    public void setEmdNm(String emdNm){

        this.emdNm = emdNm;
    }

    public String getZipNo(){

        return zipNo;
    }

    public void setZipNo(String zipNo){

        this.zipNo = zipNo;
    }

    public String getRoadAddrPart2(){

        return roadAddrPart2;
    }

    public void setRoadAddrPart2(String roadAddrPart2){

        this.roadAddrPart2 = roadAddrPart2;
    }

    public String getEmdNo(){

        return emdNo;
    }

    public void setEmdNo(String emdNo){

        this.emdNo = emdNo;
    }

    public String getSggNm(){

        return sggNm;
    }

    public void setSggNm(String sggNm){

        this.sggNm = sggNm;
    }

    public String getJibunAddr(){

        return jibunAddr;
    }

    public void setJibunAddr(String jibunAddr){

        this.jibunAddr = jibunAddr;
    }

    public String getSiNm(){

        return siNm;
    }

    public void setSiNm(String siNm){

        this.siNm = siNm;
    }

    public String getRoadAddrPart1(){

        return roadAddrPart1;
    }

    public void setRoadAddrPart1(String roadAddrPart1){

        this.roadAddrPart1 = roadAddrPart1;
    }

    public String getBdNm(){

        return bdNm;
    }

    public void setBdNm(String bdNm){

        this.bdNm = bdNm;
    }

    public String getAdmCd(){

        return admCd;
    }

    public void setAdmCd(String admCd){

        this.admCd = admCd;
    }

    public String getUdrtYn(){

        return udrtYn;
    }

    public void setUdrtYn(String udrtYn){

        this.udrtYn = udrtYn;
    }

    public String getLnbrMnnm(){

        return lnbrMnnm;
    }

    public void setLnbrMnnm(String lnbrMnnm){

        this.lnbrMnnm = lnbrMnnm;
    }

    public String getRoadAddr(){

        return roadAddr;
    }

    public void setRoadAddr(String roadAddr){

        this.roadAddr = roadAddr;
    }

    public String getLnbrSlno(){

        return lnbrSlno;
    }

    public void setLnbrSlno(String lnbrSlno){

        this.lnbrSlno = lnbrSlno;
    }

    public String getBuldMnnm(){

        return buldMnnm;
    }

    public void setBuldMnnm(String buldMnnm){

        this.buldMnnm = buldMnnm;
    }

    public String getBdKdcd(){

        return bdKdcd;
    }

    public void setBdKdcd(String bdKdcd){

        this.bdKdcd = bdKdcd;
    }

    public String getLiNm(){

        return liNm;
    }

    public void setLiNm(String liNm){

        this.liNm = liNm;
    }

    public String getRnMgtSn(){

        return rnMgtSn;
    }

    public void setRnMgtSn(String rnMgtSn){

        this.rnMgtSn = rnMgtSn;
    }

    public String getMtYn(){

        return mtYn;
    }

    public void setMtYn(String mtYn){

        this.mtYn = mtYn;
    }

    public String getBdMgtSn(){

        return bdMgtSn;
    }

    public void setBdMgtSn(String bdMgtSn){

        this.bdMgtSn = bdMgtSn;
    }

    public String getBuldSlno(){

        return buldSlno;
    }

    public void setBuldSlno(String buldSlno){

        this.buldSlno = buldSlno;
    }

    @Override
    public String toString(){

        return "Juso{" + "detBdNmList='" + detBdNmList + '\'' + ", engAddr='" + engAddr + '\'' + ", rn='" + rn + '\'' + ", emdNm='" + emdNm + '\'' + ", zipNo='" + zipNo + '\'' + ", roadAddrPart2='" + roadAddrPart2 + '\'' + ", emdNo='" + emdNo + '\'' + ", sggNm='" + sggNm + '\'' + ", jibunAddr='" + jibunAddr + '\'' + ", siNm='" + siNm + '\'' + ", roadAddrPart1='" + roadAddrPart1 + '\'' + ", bdNm='" + bdNm + '\'' + ", admCd='" + admCd + '\'' + ", udrtYn='" + udrtYn + '\'' + ", lnbrMnnm='" + lnbrMnnm + '\'' + ", roadAddr='" + roadAddr + '\'' + ", lnbrSlno='" + lnbrSlno + '\'' + ", buldMnnm='" + buldMnnm + '\'' + ", bdKdcd='" + bdKdcd + '\'' + ", liNm='" + liNm + '\'' + ", rnMgtSn='" + rnMgtSn + '\'' + ", mtYn='" + mtYn + '\'' + ", bdMgtSn='" + bdMgtSn + '\'' + ", buldSlno='" + buldSlno + '\'' + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeString(this.detBdNmList);
        dest.writeString(this.engAddr);
        dest.writeString(this.rn);
        dest.writeString(this.emdNm);
        dest.writeString(this.zipNo);
        dest.writeString(this.roadAddrPart2);
        dest.writeString(this.emdNo);
        dest.writeString(this.sggNm);
        dest.writeString(this.jibunAddr);
        dest.writeString(this.siNm);
        dest.writeString(this.roadAddrPart1);
        dest.writeString(this.bdNm);
        dest.writeString(this.admCd);
        dest.writeString(this.udrtYn);
        dest.writeString(this.lnbrMnnm);
        dest.writeString(this.roadAddr);
        dest.writeString(this.lnbrSlno);
        dest.writeString(this.buldMnnm);
        dest.writeString(this.bdKdcd);
        dest.writeString(this.liNm);
        dest.writeString(this.rnMgtSn);
        dest.writeString(this.mtYn);
        dest.writeString(this.bdMgtSn);
        dest.writeString(this.buldSlno);
    }

    public SearchAddressJuso(){

    }

    protected SearchAddressJuso(Parcel in){

        this.detBdNmList = in.readString();
        this.engAddr = in.readString();
        this.rn = in.readString();
        this.emdNm = in.readString();
        this.zipNo = in.readString();
        this.roadAddrPart2 = in.readString();
        this.emdNo = in.readString();
        this.sggNm = in.readString();
        this.jibunAddr = in.readString();
        this.siNm = in.readString();
        this.roadAddrPart1 = in.readString();
        this.bdNm = in.readString();
        this.admCd = in.readString();
        this.udrtYn = in.readString();
        this.lnbrMnnm = in.readString();
        this.roadAddr = in.readString();
        this.lnbrSlno = in.readString();
        this.buldMnnm = in.readString();
        this.bdKdcd = in.readString();
        this.liNm = in.readString();
        this.rnMgtSn = in.readString();
        this.mtYn = in.readString();
        this.bdMgtSn = in.readString();
        this.buldSlno = in.readString();
    }

    public static final Creator<SearchAddressJuso> CREATOR = new Creator<SearchAddressJuso>(){

        @Override
        public SearchAddressJuso createFromParcel(Parcel source){

            return new SearchAddressJuso(source);
        }

        @Override
        public SearchAddressJuso[] newArray(int size){

            return new SearchAddressJuso[size];
        }
    };
}
