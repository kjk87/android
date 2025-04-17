package com.pplus.prnumberbiz.core.code.common;

/**
 * Created by kjk on 2017. 5. 22..
 */

public class EnumData{

    public enum GoodsType{
        hotdeal, plus, normal
    }

    public enum GoodsStatus{
        soldout(0), ing(1), finish(-1), stop(-2);

        private int status;
        GoodsStatus(int status){
            this.status = status;
        }

        public int getStatus(){
            return status;
        }
    }

    public enum SellerStatus{//approvalWait : '승인대기', approval : '승인', reject : '반려', secondRequest : '재요청', stop : '중지'
        approvalWait(0), approval(1), reject(2), secondRequest(3), stop(4);
        private int status;
        SellerStatus(int status){
            this.status = status;
        }

        public int getStatus(){
            return status;
        }
    }

    public enum OrderType{
        store(0), packing(1), delivery(2), shipping(3), nfc(4);

        private int type;
        OrderType(int type){
            this.type = type;
        }

        public int getType(){
            return type;
        }
    }

    public enum BuyGoodsProcess{

        ERROR(-2), //error
        DENIED(-1),//Denied
        WAIT(0),//Wait
        PAY(1),//Pay
        CANCEL(2),
        USE(3),
        REFUND(4),
        EXPIRE( 5) ,
        CANCEL_WAIT(6),
        USE_WAIT(7),
        REFUND_WAIT(8),
        EXPIRE_WAIT(9) ;

        private int process;
        BuyGoodsProcess(int status){
            this.process = status;
        }

        public int getProcess(){
            return process;
        }
    }

    public enum AgentStatus{
        pending, active, deactive, expired
    }

    public enum GenderType{
        male, female
    }

    public enum MODE{
        WRITE, UPDATE
    }

    public enum PostType{
        pr, review, sns
    }

    public enum SendKindsType{
        pr_shop, post, coupon
    }

    public enum SendType{
        reservation, immediately
    }

    public enum DayOfWeek{
        mon, tue, wed, thu, fri, sat, sun
    }

    public enum CustomerType{
        customer, plus
    }

    public enum SmsType{
        SMS, LMS
    }

    public enum MsgType{
        push, sms
    }

    public enum REPORT_TYPE{
        page, article, comment
    }

    public enum CouponStatus{
        ready, active, requse, used, expired
    }

    public enum AdsType{
        article, coupon
    }

    public enum AdsStatus{
        total, ing, ready, stop, finish/*종료후 환불전*/, complete/*종료후 환불완료*/
    }

    //    buy - 구매
    //    recvAdmin - 관리자에게 받음
    //    refundMsgFail - 메시지 전송 실패로 환불
    //    cancelSendMsg - 메시지 전송 취소로 환불
    //    useTargetPush - 타겟마케팅 PUSH 발신으로 사용
    //    usePush - 일반 Push 발신으로 사용
    //    useSms - 일반 문자 발신으로 사용
    //    useLBS - LBS Push 발신으로 사용
    //    useAdKeyword - 광고 키워드 신청으로 사용
    //    refundMobileGift - 모바일 상품권 발송 실패로 환불
    //    buyBol - BOL 구매에 사용
    //    useAdvertise - 광고에 사용
    //    refundAdvertise - 광고 사용분 환불

    public enum CashType{
        buy, recvAdmin, refundAdmin, refundMsgFail, cancelSendMsg, useTargetPush, usePush, useSms, useLBS, useAdKeyword, buyBol, useAdvertise, refundAdvertise
    }

    //    giftBol - 한 명에게 선물
    //    giftBols - 여러 명에게 선물
    //    exchange - 환전
    //    buyMobileGift - 모바일 상품권 구매
    //    buy - 구매로 인한 증가
    //    joinEvent - 이벤트 참여로 적립
    //    winEvent - 이벤트 당첨 적립
    //    invite - 초대자 적립
    //    invitee - 초대 가입자 적립
    //    sendPush - PUSH 발신으로 사용
    //    recvPush - PUSH 수신으로 적립
    //    review - 리뷰 포스트 작성으로 인한 적립
    //    rewardReview - 리뷰 작성 포상으로 사용
    //    comment - 댓글 작성으로 인한 적립
    //    rewardComment - 댓글 작성 포상으로 사용
    //    recvGift - 선물받음
    //    reqExchange - 환전 요청으로 사용
    //    denyExchange - 환전 거부로 적립
    //    denyRecv - 선물 거부로 적립
    //    winAdvertise - 광고 선착순 리워드
    //    purchaseMobileGift - 모바일 상품권 구매로 사용

    public enum BolType{
        giftBol, giftBols, exchange, buyMobileGift, buy, joinEvent, winEvent, invite, invitee, sendPush, recvPush, review, rewardReview, comment, rewardComment, recvGift,
        reqExchange, denyExchange, denyRecv, purchaseMobileGift, winAdvertise, refundAdvertise, useAdvertise, adpcReward, winRecommend, joinMember, joinReduceEvent,
        refundJoinEvent, admin, refundAdmin
    }

    public enum PrimaryType{
        increase, decrease
    }

    public enum InquiryType{
        inquiry, suggest, inquirycoop
    }

    public enum PageTypeCode{
        person, store, franchise, shop
    }

    public enum PageNumberType{
        royal, gold, normal
    }

    public enum PageStatus{
        ready, pending, redemand, refuse, normal
        //return(반려)
    }

    public enum MsgStatus{
        finish, reserved, cancelSend
    }

    //    discount - 할인
    //    dayOfWeek - 요일
    //    visit - 방문
    //    consult - 상담
    //    expert - 재능
    //    time - 시간
    //    etc - 기타

    public enum CouponType{
        discount, dayOfWeek, visit, consult, expert, time, etc
    }

    public enum MobileGiftSortType{
        lowPrice, highPrice, recent
    }

    public enum PGType{
        wcard, vbank, bank, mobile
    }

    //    normal - 일반
    //    dormant - 휴면
    //    waitingToLeave - 탈퇴대기
    //    leave - 탈퇴
    public enum UseStatus {
        normal, dormant, waitingToLeave, leave

    }

    public enum GoodsSort{
        asc, desc, seqNo, price
    }

    public enum BuyGoodsSort{
        asc, desc, seqNo, price
    }
}
