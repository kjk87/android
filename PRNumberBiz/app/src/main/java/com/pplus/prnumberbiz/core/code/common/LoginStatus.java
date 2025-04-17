package com.pplus.prnumberbiz.core.code.common;

/**
 * Created by ksh on 2016-10-06.
 */

public enum LoginStatus{

    //"상태 - normal:정상, dormant:휴면, watingToLeave:탈퇴대기"

    normal,// 정상

    dormant, //휴면

    waitingToLeave, //탈퇴대기

    leave,//탈퇴

    duplication, //중복

    pending

}
