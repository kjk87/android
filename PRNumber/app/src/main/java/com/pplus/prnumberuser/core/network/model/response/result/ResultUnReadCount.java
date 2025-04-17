package com.pplus.prnumberuser.core.network.model.response.result;

import com.pplus.prnumberuser.core.code.custom.YnCode;

/**
 * Created by ksh on 2016-11-23.
 */

public class ResultUnReadCount {
    private YnCode sendbirdUser;

    private long unread_count;

    public YnCode getSendbirdUser(){

        return sendbirdUser;
    }

    public void setSendbirdUser(YnCode sendbirdUser){

        this.sendbirdUser = sendbirdUser;
    }

    public long getUnread_count(){

        return unread_count;
    }

    public void setUnread_count(long unread_count){

        this.unread_count = unread_count;
    }

    @Override
    public String toString(){

        return "ResultUnReadCount{" +
                "sendbirdUser=" + sendbirdUser +
                ", unread_count=" + unread_count +
                '}';
    }
}
