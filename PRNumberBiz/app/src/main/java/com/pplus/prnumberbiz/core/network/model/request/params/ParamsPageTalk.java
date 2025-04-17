package com.pplus.prnumberbiz.core.network.model.request.params;

import com.pplus.prnumberbiz.apps.common.ui.base.ApiModelProperty;
import com.pplus.prnumberbiz.core.code.common.TalkReceiveBoundsCode;
import com.pplus.prnumberbiz.core.network.model.request.BaseParams;

/**
 * Created by ksh on 2016-10-20.
 */

public class ParamsPageTalk extends BaseParams{
    @ApiModelProperty(value="톡 수신 범위 - everybody:전체공개, nobody:비공개, fan:FAN", allowableValues="everybody,nobody,fan")
    private TalkReceiveBoundsCode talkReceiveBounds;

    @ApiModelProperty(value = "톡 비수신 요일 - 비수신 요일을 comma 구분자로 입력,  (예. 전체 비수신이면 sun, mon, tue, wed, thu, fri, sat)")
    private String talkDenyDay;

    @ApiModelProperty(value = "톡 비수신 시작 시간")
    private String talkDenyStartTime;

    @ApiModelProperty(value = "톡 비수신 종료 시간")
    private String talkDenyEndTime;

    private Long seqNo;

    public TalkReceiveBoundsCode getTalkReceiveBounds(){

        return talkReceiveBounds;
    }

    public void setTalkReceiveBounds(TalkReceiveBoundsCode talkReceiveBounds){

        this.talkReceiveBounds = talkReceiveBounds;
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

    public Long getSeqNo(){

        return seqNo;
    }

    public void setSeqNo(Long pageSeqNo){

        this.seqNo = pageSeqNo;
    }

    @Override
    public String toString(){

        return "PageTalkDomain{" +
                "talkReceiveBounds=" + talkReceiveBounds +
                ", talkDenyDay='" + talkDenyDay + '\'' +
                ", talkDenyStartTime='" + talkDenyStartTime + '\'' +
                ", talkDenyEndTime='" + talkDenyEndTime + '\'' +
                ", seqNo=" + seqNo +
                '}';
    }

}
