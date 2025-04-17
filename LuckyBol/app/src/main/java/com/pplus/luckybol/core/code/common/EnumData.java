package com.pplus.luckybol.core.code.common;

/**
 * Created by kjk on 2017. 5. 22..
 */

public class EnumData {

    // 배송 상태값. 1:상품준비중, 2:주문취소, 3:배송중, 4:배송완료, 5:환불수거중, 6:환불수거완료, 7:교환수거중, 8:교환상품준비중, 9:교환배송중, 10:교환배송완료, 99:구매확정
    public enum DeliveryStatus {
        READY_BEFORE(0), READY_ING(1), CANCEL(2), ING(3), COMPLETE(4), REFUND_ING(5), REFUND_COMPLETE(6),
        EXCHANGE_ING(7), EXCHANGE_RETURN_READY(8), EXCHANGE_RETURN_ING(9), EXCHANGE_RETURN_COMPLETE(10),
        PURCHASE_COMPLETE(99);
        private final int status;

        private DeliveryStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return this.status;
        }
    }

    public enum PurchaseProductStatus {
        FAIL(-1),
        PAY_REQ(1), PAY(2),
        CANCEL_REQ(11), CANCEL_COMPLETE(12),
        REFUND_REQ(21), REFUND_COMPLETE(22),
        EXCHANGE_REQ(31), EXCHANGE_COMPLETE(32),
        COMPLETE(99);
        private final int status;

        private PurchaseProductStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return this.status;
        }
    }

    public enum PurchaseStatus {
        FAIL(-1),
        PAY_REQ(1), PAY(2),
        CANCEL_REQ(11), PART_CANCEL_REQ(12), CANCEL_COMPLETE(13), PART_CANCEL_COMPLETE(14),
        REFUND_REQ(21), PART_REFUND_REQ(22), REFUND_COMPLETE(23), PART_REFUND_COMPLETE(24),
        EXCHANGE_REQ(31), PART_EXCHANGE_REQ(32), EXCHANGE_COMPLETE(33), PART_EXCHANGE_COMPLETE(34),
        COMPLETE(99);
        private final int status;

        private PurchaseStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return this.status;
        }
    }

    public enum OptionType {
        single, union
    }

    public enum DeliveryType {// 1:무료, 2:유료, 3:조건부 무료
        free(1), pay(2), conditionPay(3);
        private int type;

        DeliveryType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum CardCode {
        CCSU,//수협
        CCSS,//삼성카드
        CCNH,//NH카드
        CCLO,//롯데카드
        CCLG,//신한카드
        CCKM,//국민카드
        CCKJ,//광주은행
        CCKE,//외환은행
        CCJB,//전북은행
        CCHN,//하나SK카드
        CCDI,//현대카드
        CCCT,//씨티은행
        CCCJ,//제주은행
        CCBC,//비씨카드
    }

    public enum GiftStatus {
        wait(0), use(1), reviewWrite(2), expired(3);
        private int status;

        GiftStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    public enum GoodsNoticeType {
        text, select
    }

    public enum OpenType {
        every(0), weekend(1), each(2);
        private int type;

        OpenType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum OrderType {
        store(0), packing(1), delivery(2), shipping(3), nfc(4);

        private int type;

        OrderType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum PageTypeCode {
        person, store, franchise, shop
    }

    public enum GoodsStatus {
        soldout(0), ing(1), finish(-1), stop(-2);

        private int status;

        GoodsStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    //0 :  대기, 1:승인 2:승인후 취소, 3:사용, 4:사용후환불, -1: 우리서버 에러, -2: PG 에러
    public enum BuyGoodsProcess {

        ERROR(-2), //error
        DENIED(-1),//Denied
        WAIT(0),//Wait
        PAY(1),//Pay
        CANCEL(2),
        USE(3),
        REFUND(4),
        EXPIRE(5),
        CANCEL_WAIT(6),
        USE_WAIT(7),
        REFUND_WAIT(8),
        EXPIRE_WAIT(9),
        BIZ_CANCEL(10);

        private int process;

        BuyGoodsProcess(int status) {
            this.process = status;
        }

        public int getProcess() {
            return process;
        }
    }

    public enum BuyGoodsOrderProcess {
        ready(0), //접수대기
        confirm(1), //상품중비중
        shipping(2), // 배송중
        shipping_complete(3), //배송완료
        refund_wait(4), //환불대기
        refund(5),//환불완료
        exchange_wait(6),//교환신청
        exchange(7),//교환
        complete(8); //구매확정

        private int process;

        BuyGoodsOrderProcess(int status) {
            this.process = status;
        }

        public int getProcess() {
            return process;
        }
    }


    public enum OrderProcess {
        ready(0), //접수대기
        ing(1), //처리중
        complete(2), // 완료
        cancel(3), //주분 취소
        shipping(4); //배송중

        private int process;

        OrderProcess(int status) {
            this.process = status;
        }

        public int getProcess() {
            return process;
        }
    }

    public enum RankType {
        recommend, reward
    }

    public enum DurationType {
        week, month, total
    }

    public enum GenderType {
        male, female
    }

    public enum JobType {
        office, professional, freelancer, selfEmployed, manufactureOrConstruction, logistics, functionary, student, inoccupation
    }

    public enum MODE {
        WRITE, UPDATE
    }

    public enum PostType {
        pr, review, sns, member
    }

    public enum SendKindsType {
        pr_shop, post, coupon
    }

    public enum SendType {
        reservation, immediately
    }

    public enum DayOfWeek {
        mon, tue, wed, thu, fri, sat, sun
    }

    public enum CustomerType {
        customer, plus
    }

    public enum SmsType {
        SMS, LMS
    }

    public enum MsgType {
        push, sms
    }

    public enum REPORT_TYPE {
        page, article, comment, goods
    }

    public enum CouponStatus {
        ready, active, requse, used, expired
    }

    public enum InquiryType {
        inquiry, suggest, inquirycoop
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

    public enum BolType {
        giftBol, giftBols, exchange, buyMobileGift, buy, buyCancel, joinEvent, winEvent, invite, invitee, sendPush, recvPush, review, rewardReview, comment, rewardComment, recvGift,
        reqExchange, denyExchange, denyRecv, purchaseMobileGift, refundMobileGift, winAdvertise, refundAdvertise, useAdvertise, adpcReward, winRecommend, joinMember, joinReduceEvent,
        refundJoinEvent, admin, refundAdmin, buyGoodsUse, buyInvitee
    }

    //    discount - 할인
    //    dayOfWeek - 요일
    //    visit - 방문
    //    consult - 상담
    //    expert - 재능
    //    time - 시간
    //    etc - 기타

    public enum CouponType {
        discount, dayOfWeek, visit, consult, expert, time, etc
    }

    public enum MobileGiftSortType {
        lowPrice, highPrice, recent
    }

    public enum PGType {
        wcard, vbank, bank, mobile
    }

    public enum AdsType {
        article, coupon
    }

    //    normal - 일반
    //    dormant - 휴면
    //    waitingToLeave - 탈퇴대기
    //    leave - 탈퇴
    public enum UseStatus {
        normal, dormant, waitingToLeave, leave

    }

    public enum GoodsSort {
        asc, desc, seqNo, price, distance, news_datetime
    }

    public enum BuyGoodsSort {
        asc, desc, seqNo, price, regDatetime
    }
}
