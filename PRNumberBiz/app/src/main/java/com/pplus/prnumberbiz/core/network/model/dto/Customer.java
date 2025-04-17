package com.pplus.prnumberbiz.core.network.model.dto;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 김종경 on 2016-09-22.
 */

public class Customer implements Parcelable{

    private Long no;
    private String name;
    private String mobile;
    private String inputType;
    private String status;
    private boolean plus;
    private boolean block;
    private No page;
    private User target;

    @Override
    public boolean equals(Object o){

        if(o == null) return false;

        if(o instanceof Customer) {
            return (((Customer) o).no.equals(no));
        } else {
            return false;
        }
    }

    public Long getNo(){

        return no;
    }

    public void setNo(Long no){

        this.no = no;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    public String getMobile(){

        return mobile;
    }

    public void setMobile(String mobile){

        this.mobile = mobile;
    }

    public String getInputType(){

        return inputType;
    }

    public void setInputType(String inputType){

        this.inputType = inputType;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public boolean isPlus(){

        return plus;
    }

    public void setPlus(boolean plus){

        this.plus = plus;
    }

    public boolean isBlock(){

        return block;
    }

    public void setBlock(boolean block){

        this.block = block;
    }

    public No getPage(){

        return page;
    }

    public void setPage(No page){

        this.page = page;
    }

    public User getTarget(){

        return target;
    }

    public void setTarget(User target){

        this.target = target;
    }

    @Override
    public String toString(){

        return "Customer{" + "no=" + no + ", name='" + name + '\'' + ", mobile='" + mobile + '\'' + ", inputType='" + inputType + '\'' + ", status='" + status + '\'' + ", plus=" + plus + ", block=" + block + ", page=" + page + ", target=" + target + '}';
    }

    @Override
    public int describeContents(){

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){

        dest.writeValue(this.no);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.inputType);
        dest.writeString(this.status);
        dest.writeByte(this.plus ? (byte) 1 : (byte) 0);
        dest.writeByte(this.block ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.page, flags);
        dest.writeParcelable(this.target, flags);
    }

    public Customer(){

    }

    protected Customer(Parcel in){

        this.no = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.mobile = in.readString();
        this.inputType = in.readString();
        this.status = in.readString();
        this.plus = in.readByte() != 0;
        this.block = in.readByte() != 0;
        this.page = in.readParcelable(No.class.getClassLoader());
        this.target = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>(){

        @Override
        public Customer createFromParcel(Parcel source){

            return new Customer(source);
        }

        @Override
        public Customer[] newArray(int size){

            return new Customer[size];
        }
    };
}
