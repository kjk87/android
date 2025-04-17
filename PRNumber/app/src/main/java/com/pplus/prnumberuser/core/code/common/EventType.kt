package com.pplus.prnumberuser.core.code.common

/**
 * Created by imac on 2018. 2. 21..
 */
class EventType {

    enum class EventStatus{
        active, pending, announce
    }

    //    [primaryType]
//    insert - 번호 입력형
//    join - 참여형
//    move - 이동형
//    biz - Biz 컨텐츠형

    enum class PrimaryType {
        insert, join, move, tune, benefit, biz, goodluck, number, lotto, lottoPlaybol, page
    }

//[secondaryType]
//    duration - 기간형
//    time - 타임형
//    none - 타입 없음

    enum class SecondaryType {
        duration, time, none
    }

//[winAnnounceType]
//    immediately - 즉시 발표
//    special - 지정된 날 발표
//    none - 없음

    enum class WinAnnounceType {
        immediately, special, limit, none
    }

//[winSelectType]
//    random - 랜덤
//    select - 지정
//    none - 없음

    enum class WinSelectType {
        random, select, none
    }

//[displayTimeType]
//    time - 시간대 설정

    enum class DisplayTimeType {
        time
    }

//[joinType]
//    event - 이벤트 전체
//    daily - 하루
//    weekly - 1주일
//    monthly - 한달
//    always - 항상

    enum class JoinType {
        event, daily, weekly, monthly, always
    }

//[rewardType]
//    always - 제한 없음.
//    event - 최초 아무 배너 클릭시 적립
//    banner - 특정 배너 최초 클릭시 적립
//    none - 적립 없음.

    enum class RewardType {
        always, event, banner, none
    }

//    [bizType]
//    none - biz 선정 없음
//    all - 전체 biz
//    category - 카테고리 직접 지정
//    page - 페이지 직접 지정

    enum class BizType {
        none, all, category, page
    }

//[adType]
//    all - 글 + 쿠폰
//    coupon - 쿠폰
//    post - 글

    enum class AdType {
        all, coupon, post
    }

//[adRegistType]
//    none - 제한없음
//    event- 이벤트 기준
//    daily - 일별 기준

    enum class AdRegistType {
        none, event, daily
    }

//[targetType]
//    all - 전체 노출
//    target - 조건 노출
//    user - 사용자 직접 지정
//    none - 노출 없음

    enum class TargetType {
        all, target, user, none
    }

//    none - 없음
//    all - 전체
//    join - 이벤트 참여
//    confirm - 당첨 확인

    enum class MoveMethod{
        none, all, join, confirm
    }

    enum class MoveType{
        external
    }

    enum class ContentsType{
        html, outerLink
    }
}