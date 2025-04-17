//package com.pplus.prnumberuser.core.network.model.response.result;
//
//import com.pplus.prnumberuser.apps.common.ui.base.ApiModelProperty;
//import com.pplus.prnumberuser.core.code.custom.YnCode;
//import com.pplus.prnumberuser.core.network.model.dto.ChannelDomain;
//
//import java.util.List;
//
///**
// * Created by ksh on 2016-11-23.
// */
//
//public class ResultChannelList {
//    @ApiModelProperty(value = "sendbird 회원 여부")
//    private YnCode sendbirdUser;
//
//    @ApiModelProperty(value = "sendbird 채널 목록")
//    private List<ChannelDomain> channelList;
//
//    private String next; // 다음 리스트를 가져오기 위한 token
//
//    public YnCode getSendbirdUser(){
//
//        return sendbirdUser;
//    }
//
//    public void setSendbirdUser(YnCode sendbirdUser){
//
//        this.sendbirdUser = sendbirdUser;
//    }
//
//    public List<ChannelDomain> getChannelList(){
//
//        return channelList;
//    }
//
//    public void setChannelList(List<ChannelDomain> channelList){
//
//        this.channelList = channelList;
//    }
//
//    public String getNext(){
//
//        return next;
//    }
//
//    public void setNext(String next){
//
//        this.next = next;
//    }
//
//    @Override
//    public String toString(){
//
//        return "ResultChannelList{" +
//                "sendbirdUser=" + sendbirdUser +
//                ", channelList=" + channelList +
//                ", next='" + next + '\'' +
//                '}';
//    }
//}
