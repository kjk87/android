package com.pplus.luckybol.core.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Contact {

    @Id
    private String mobileNumber;
    private String memberName;
    private Boolean delete;
    private Boolean update;
    @Generated(hash = 286270137)
    public Contact(String mobileNumber, String memberName, Boolean delete,
                   Boolean update) {
        this.mobileNumber = mobileNumber;
        this.memberName = memberName;
        this.delete = delete;
        this.update = update;
    }
    @Generated(hash = 672515148)
    public Contact() {
    }
    public String getMobileNumber() {
        return this.mobileNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    public String getMemberName() {
        return this.memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    public Boolean getDelete() {
        return this.delete;
    }
    public void setDelete(Boolean delete) {
        this.delete = delete;
    }
    public Boolean getUpdate() {
        return this.update;
    }
    public void setUpdate(Boolean update) {
        this.update = update;
    }

}
