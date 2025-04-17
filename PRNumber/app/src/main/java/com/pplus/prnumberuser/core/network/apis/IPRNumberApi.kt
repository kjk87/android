package com.pplus.prnumberuser.core.network.apis

import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.request.params.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by 김종경 on 2016-10-06.
 */
interface IPRNumberApi {
    //중복 확인
    @FormUrlEncoded
    @POST("auth/existsUser")
    fun existsUser(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //회원가입
    @POST("auth/join")
    fun join(@Body params: User): Call<NewResultResponse<User>>

    //앱 버전 가져오기
    @FormUrlEncoded
    @POST("auth/getAppVersion")
    fun appVersion(@FieldMap params: Map<String, String>): Call<NewResultResponse<AppVersion>>

    //앱 로그인
    @FormUrlEncoded
    @POST("auth/login")
    fun login(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    //디바이스 존재여부 체크
    @FormUrlEncoded
    @POST("auth/existsDevice")
    fun existsDevice(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @POST("auth/registDevice")
    fun registDevice(@Body params: ParamsRegDevice): Call<NewResultResponse<User>>

    //약관 리스트
    @FormUrlEncoded
    @POST("auth/getActiveTermsAllByAppType")
    fun getActiveTermsAll(@Field("appType") appType: String): Call<NewResultResponse<Terms>>

    //미동의 약관 리스트
    @FormUrlEncoded
    @POST("auth/getNotSignedActiveTermsAllByAppType")
    fun getNotSignedActiveTermsAll(@Field("appType") appType: String): Call<NewResultResponse<Terms>>

    @POST("auth/activatePage")
    fun requestActivatePage(@Body params: User): Call<NewResultResponse<User>>

    @get:POST("auth/getSession")
    val session: Call<NewResultResponse<User>>

    @POST("auth/reloadSession")
    fun requestReloadSession(): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("auth/getUserByVerification")
    fun getUserByVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("auth/getUserByLoginIdAndMobile")
    fun getUserByLoginIdAndMobile(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("auth/agreeTermsList")
    fun requestAgreeTermsList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("auth/changePasswordByVerification")
    fun requestChangePasswordByVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("auth/updateMobileByVerification")
    fun updateMobileByVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:POST("my/getMe")
    val me: Call<NewResultResponse<User>>

    //프로필이미지 세팅
    @FormUrlEncoded
    @POST("my/updateProfileImage")
    fun updateProfileImage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("my/updateNickname")
    fun updateNickname(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //push 세팅
    @FormUrlEncoded
    @POST("my/updatePushConfig")
    fun updatePushConfig(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("my/updateUser")
    fun updateUser(@Body params: User): Call<NewResultResponse<User>>

    @POST("my/updateExternal")
    fun updateExternal(@Body params: User): Call<NewResultResponse<User>>

    //카테고리 리스트
    @FormUrlEncoded
    @POST("page/getCategoryAll")
    fun getCategoryAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<Category>>

    @POST("page/updatePage")
    fun updatePage(@Body page: Page): Call<NewResultResponse<Page>>

    //프로필이미지 세팅
    @FormUrlEncoded
    @POST("page/updateProfileImage")
    fun updatePageProfileImage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //백그라운 세팅
    @FormUrlEncoded
    @POST("page/updateBackgroundImage")
    fun updatePageBackgroundImage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //소개이미지 등록
    @POST("page/updateIntroImageList")
    fun updateIntroImageList(@Body params: ParamsIntroImage): Call<NewResultResponse<Page>>

    //소개이미지 리스트
    @FormUrlEncoded
    @POST("page/getIntroImageAll")
    fun getIntroImageAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<Attachment>>

    @FormUrlEncoded
    @POST("page/getPageImageAll")
    fun getPageImageAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<PageImage>>

    //sns list
    @FormUrlEncoded
    @POST("page/getSnsLinkAll")
    fun getSnsLinkAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<Sns>>

    @POST("page/saveSnsLink")
    fun requestSaveSnsLink(@Body params: Sns): Call<NewResultResponse<Sns>>

    @FormUrlEncoded
    @POST("page/deleteSnsLinkByType")
    fun requestDeleteSnsLinkByType(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("page/getPage")
    fun getPage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    @GET("page/getPage2")
    fun getPage2(@QueryMap params: Map<String, String>): Call<NewResultResponse<Page2>>

    @FormUrlEncoded
    @POST("page/getPageByNumber")
    fun getPageByNumber(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    @FormUrlEncoded
    @POST("page/getPageCountByTheme")
    fun getPageCountByTheme(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("page/getPageListByTheme")
    fun getPageListByTheme(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    @FormUrlEncoded
    @POST("page/getPageCount")
    fun getPageCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("page/getPageList")
    fun getPageList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    @FormUrlEncoded
    @POST("page/getPageCountByManageSeqNo")
    fun getPageCountByManageSeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("page/getPageListByManageSeqNo")
    fun getPageListByManageSeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    @FormUrlEncoded
    @POST("page/getPageCountByArea")
    fun getPageCountByArea(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("page/getPageListByArea")
    fun getPageListByArea(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    @FormUrlEncoded
    @POST("page/getPageListByAreaAndManageSeqNo")
    fun getPageListByAreaAndManageSeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    @FormUrlEncoded
    @POST("page/getPageCountByAreaByTheme")
    fun getPageCountByAreaByTheme(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("page/getPageListByAreaByTheme")
    fun getPageListByAreaByTheme(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    //추가동영상 리스트
    @FormUrlEncoded
    @POST("page/getIntroMovieAll")
    fun getIntroMovieAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<ImgUrl>>

    //포스트 리스트 카운트
    @FormUrlEncoded
    @POST("post/getBoardPostCount")
    fun getBoardPostCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    //포스트 리스트
    @FormUrlEncoded
    @POST("post/getBoardPostList")
    fun getBoardPostList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Post>>

    //포스트 등록
    @FormUrlEncoded
    @POST("post/getPostWithAttachment")
    fun getPost(@Field("postNo") postNo: Long): Call<NewResultResponse<Post>>

    //포스트 등록
    @POST("post/insertPost")
    fun insertPost(@Body params: Post): Call<NewResultResponse<Post>>

    //포스트 수정
    @POST("post/updatePost")
    fun updatePost(@Body params: Post): Call<NewResultResponse<Post>>

    //포스트 삭제
    @FormUrlEncoded
    @POST("post/deletePost")
    fun requestDeletePost(@Field("postNo") postNo: Long): Call<NewResultResponse<Any>>

    //댓글 리스트
    @FormUrlEncoded
    @POST("post/getCommentAll")
    fun getCommentAll(@Field("postNo") postNo: Long): Call<NewResultResponse<Comment>>

    //댓글입력
    @POST("post/insertComment")
    fun requestInsertComment(@Body params: Comment): Call<NewResultResponse<Comment>>

    //댓글수정
    @POST("post/updateComment")
    fun updateComment(@Body params: Comment): Call<NewResultResponse<Comment>>

    //댓글삭제
    @FormUrlEncoded
    @POST("post/deleteComment")
    fun requestDeleteComment(@Field("commentNo") commentNo: Long): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("common/copyAttachment")
    fun requestCopyAttachment(@FieldMap params: Map<String, String>): Call<NewResultResponse<Attachment>>

    @FormUrlEncoded
    @POST("common/searchAddress")
    fun requestSearchAddress(@FieldMap params: Map<String, String>): Call<ResultAddress>

    @FormUrlEncoded
    @POST("common/getCoord2Address")
    fun getCoord2Address(@FieldMap params: Map<String, String>): Call<ResultAddress>

    @get:POST("common/getDefaultImageList")
    val defaultImageList: Call<NewResultResponse<Attachment>>

    @FormUrlEncoded
    @POST("common/getCoordByAddress")
    fun getCoordByAddress(@FieldMap params: Map<String, String>): Call<NewResultResponse<Coord>>

    @FormUrlEncoded
    @POST("common/getNoticeCount")
    fun getNoticeCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("common/getNoticeList")
    fun getNoticeList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Notice>>

    @FormUrlEncoded
    @POST("common/getNotice")
    fun getNotice(@FieldMap params: Map<String, String>): Call<NewResultResponse<Notice>>

    @FormUrlEncoded
    @POST("common/getFaqGroupAll")
    fun getFaqGroupAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<FaqGroup>>

    @FormUrlEncoded
    @POST("common/getFaqCountByAppType")
    fun getFaqCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("common/getFaqListByAppType")
    fun getFaqList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Faq>>

    @FormUrlEncoded
    @POST("common/getFaq")
    fun getFaq(@FieldMap params: Map<String, String>): Call<NewResultResponse<Faq>>

    @get:POST("common/getCountryConfigAll")
    val countryConfigAll: Call<NewResultResponse<CountryConfig>>

    @POST("common/reporting")
    fun requestReporting(@Body report: Report): Call<NewResultResponse<Report>>

    //인증 요청
    @FormUrlEncoded
    @POST("verification/request")
    fun requestVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<Verification>>

    //인증 요청
    @FormUrlEncoded
    @POST("verification/confirm")
    fun confirmVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //탈퇴 요청
    @FormUrlEncoded
    @POST("verification/leave")
    fun requestLeave(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("verification/cancelLeave")
    fun requestCancelLeave(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("cash/getHistoryList")
    fun requestCashHistoryList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Cash>>

    @FormUrlEncoded
    @POST("cash/getHistoryTotalAmount")
    fun getCashHistoryTotalAmount(@FieldMap params: Map<String, String>): Call<NewResultResponse<String>>

    @get:POST("number/getPrefixNumber")
    val prefixNumber: Call<NewResultResponse<String>>

    //그룹 추가
    @POST("plus/insertGroup")
    fun requestInsertPlusGroup(@Body params: Group): Call<NewResultResponse<Any>>

    //그룹명 수정
    @FormUrlEncoded
    @POST("plus/updateGroupName")
    fun updatePlusGroupName(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //그룹 정렬
    @POST("plus/updateGroupPriorityAll")
    fun updatePlusGroupPriorityAll(@Body params: ParamsGroupPriority): Call<NewResultResponse<Any>>

    //그룹명 삭제
    @FormUrlEncoded
    @POST("plus/deleteGroup")
    fun requestDeletePlusGroup(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:POST("plus/getGroupAll")
    val plusGroupAll: Call<NewResultResponse<Group>>

    //그룸원 리스트 추가
    @POST("plus/addListToGroup")
    fun requestAddPlusListToGroup(@Body params: ParamsPlusGroup): Call<NewResultResponse<Any>>

    //그룸원 리스트 삭제
    @POST("plus/removeListFromGroup")
    fun requestRemovePlusListFromGroup(@Body params: ParamsPlusGroup): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("plus/getList")
    fun getPlusList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Plus>>

    @FormUrlEncoded
    @POST("plus/getOnlyPlus")
    fun getOnlyPlus(@FieldMap params: Map<String, String>): Call<NewResultResponse<Plus>>

    @FormUrlEncoded
    @POST("plus/getCount")
    fun getPlusCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("plus/getExcludeCount")
    fun getExcludePlusCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("plus/getExcludeList")
    fun getExcludePlusList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Plus>>

    @POST("plus/insert")
    fun requestInsertPlus(@Body params: Plus): Call<NewResultResponse<Plus>>

    @FormUrlEncoded
    @POST("plus/delete")
    fun requestDeletePlus(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("plus/deleteByPage")
    fun requestDeletePlusByPage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("plus/getExistFan")
    fun requestExistPlusByPage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //문자 보내기
    @POST("msg/insertSmsMsg")
    fun requestInsertSmsMsg(@Body params: Msg): Call<NewResultResponse<Msg>>

    //문자 보내기
    @POST("msg/insertPushMsg")
    fun requestInsertPushMsg(@Body params: Msg): Call<NewResultResponse<Msg>>

    //문자 보내기
    @POST("msg/insertSavedMsg")
    fun requestInsertSavedMsg(@Body params: SavedMsg): Call<NewResultResponse<SavedMsg>>

    @FormUrlEncoded
    @POST("msg/deleteSavedMsg")
    fun requestDeleteSavedMsg(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:POST("msg/getSavedMsgCount")
    val savedMsgCount: Call<NewResultResponse<Int>>

    //문자 보내기
    @FormUrlEncoded
    @POST("msg/getSavedMsgList")
    fun getSavedMsgList(@FieldMap params: Map<String, String>): Call<NewResultResponse<SavedMsg>>

    //예약 메세지 리스
    @FormUrlEncoded
    @POST("msg/getReservedMsgAll")
    fun getReservedMsgAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<Msg>>

    @FormUrlEncoded
    @POST("msg/readComplete")
    fun requestReadComplete(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:POST("msg/getMsgCountInBox")
    val msgCountInBox: Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("msg/getMsgListInBox")
    fun getMsgListInBox(@FieldMap params: Map<String, String>): Call<NewResultResponse<Msg>>

    @FormUrlEncoded
    @POST("msg/deleteMsgInBox")
    fun requestDeleteMsgInBox(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //연락처 업데이트
    @POST("contact/updateList")
    fun updateContactList(@Body params: ParamsContact): Call<NewResultResponse<Any>>

    //연락처 삭제
    @POST("contact/deleteList")
    fun requestDeleteContactList(@Body params: ParamsContact): Call<NewResultResponse<Any>>

    //친구 리스트 카운트
    @get:POST("contact/getAllFriendCount")
    val allFriendCount: Call<NewResultResponse<Int>>

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getAllFriendList")
    fun getAllFriendList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Friend>>

    //친구 리스트 카운트
    @get:POST("contact/getFriendCount")
    val friendCount: Call<NewResultResponse<Int>>

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getFriendList")
    fun getFriendList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Friend>>

    //같은 친구 리스트
    @FormUrlEncoded
    @POST("contact/getSameFriendAll")
    fun getSameFriendAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    //친구 리스트 카운트
    @FormUrlEncoded
    @POST("contact/getFriendPageCount")
    fun getFriendPageCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getFriendPageList")
    fun getFriendPageList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    //친구 리스트 카운트
    @get:POST("contact/getUserFriendCount")
    val userFriendCount: Call<NewResultResponse<Int>>

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getUserFriendList")
    fun getUserFriendList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Friend>>

    //친구 리스트 카운트
    @get:POST("contact/getExistsNicknameFriendCount")
    val existsNicknameFriendCount: Call<NewResultResponse<Int>>

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getExistsNicknameFriendList")
    fun getExistsNicknameFriendList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Friend>>

    @FormUrlEncoded
    @POST("bol/getHistoryList")
    fun getBolHistoryList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Bol>>

    @POST("bol/exchange")
    fun bolExchange(@Body params: Exchange): Call<NewResultResponse<Any>>


    @FormUrlEncoded
    @POST("bol/getHistoryCount")
    fun getBolHistoryCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("bol/getHistoryWithTargetList")
    fun getBolHistoryWithTargetList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Bol>>

    @FormUrlEncoded
    @POST("bol/getHistoryWithTarget")
    fun getBolHistoryWithTarget(@FieldMap params: Map<String, String>): Call<NewResultResponse<Bol>>

    @FormUrlEncoded
    @POST("user/getExistsNicknameUserCount")
    fun getExistsNicknameUserCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("user/getExistsNicknameUserList")
    fun getExistsNicknameUserList(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("user/getUserCountByRecommendKey")
    fun getUserCountByRecommendKey(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("user/getUserListByRecommendKey")
    fun getUserListByRecommendKey(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("page/checkAuthCodeForUser")
    fun requestCheckAuthCodeForUser(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getCount")
    fun getEventCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getEventList")
    fun getEventList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("event/getCountByPageSeqNo")
    fun getEventCountByPageSeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getEventListByPageSeqNo")
    fun getEventListByPageSeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("event/getActiveEventByPageSeqNo")
    fun getActiveEventByPageSeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @get:POST("event/getGroupAll")
    val eventGroupAll: Call<NewResultResponse<EventGroup>>

    @FormUrlEncoded
    @POST("event/getCountByGroup")
    fun getEventCountByGroup(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getEventListByGroup")
    fun getEventListByGroup(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("event/getByNumber")
    fun getEventByNumber(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("event/join")
    fun joinEvent(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventResult>>

    @FormUrlEncoded
    @POST("event/serializableJoin")
    fun serializableJoinEvent(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventResult>>

    @POST("event/joinWithProperties")
    fun requestJoinWithPropertiesEvent(@Body params: ParamsJoinEvent): Call<NewResultResponse<EventResult>>

    @FormUrlEncoded
    @POST("event/writeImpression")
    fun writeImpression(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("event/updateEventWinAddress")
    fun updateEventWinAddress(@Body params: EventWin): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("event/getBannerAll")
    fun getBannerAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventBanner>>

    @FormUrlEncoded
    @POST("event/getGiftAll")
    fun getGiftAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventGift>>

    @FormUrlEncoded
    @POST("event/getWinCount")
    fun getWinCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getWinList")
    fun getWinList(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @FormUrlEncoded
    @POST("event/getEventWinCountByGiftSeqNo")
    fun getEventWinCountByGiftSeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getEventWinListByGiftSeqNo")
    fun getEventWinListByGiftSeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @FormUrlEncoded
    @POST("event/getWinBySeqNo")
    fun getWinBySeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @get:POST("event/getUserWinCount")
    val userWinCount: Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getUserWinList")
    fun getUserWinList(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @FormUrlEncoded
    @POST("event/existsResult")
    fun requestExistsEventResult(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventExist>>

    @FormUrlEncoded
    @POST("event/getWinAll")
    fun getWinAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @FormUrlEncoded
    @POST("event/get")
    fun getEvent(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("event/getByCode")
    fun getEventByCode(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("ranking/getInviteRankingView")
    fun getInviteRanking(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("ranking/getInviteRankingView")
    fun getRewardRanking(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @get:POST("ranking/getMyInviteRanking")
    val myInviteRanking: Call<NewResultResponse<User>>

    @get:POST("ranking/getMyRewardRanking")
    val myRewardRanking: Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("event/getWinAnnounceCount")
    fun getWinAnnounceCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getWinAnnounceList")
    fun getWinAnnounceList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    //상품api
    //상품 리뷰 api
    @POST("goodsReview")
    fun postGoodsReview(@Body goods: GoodsReview): Call<NewResultResponse<Any>>

    @PUT("goodsReview")
    fun putGoodsReview(@Body goods: GoodsReview): Call<NewResultResponse<Any>>

    @GET("goodsReview/detail")
    fun getGoodsReview(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<GoodsReview>>>

    @DELETE("goodsReview")
    fun requestDeleteGoodsReview(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //페이지 상품 api
    @GET("page/location")
    fun getPageLisWithGoods(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("page/eval")
    fun getPageEval(@QueryMap params: Map<String, String>): Call<NewResultResponse<PageEval>>

    //구매 pai
    @POST("buy")
    fun postBuy(@Body params: Buy): Call<NewResultResponse<Buy>>

    @POST("buy/hot")
    fun postBuyHot(@Body params: Buy): Call<NewResultResponse<Buy>>

    @POST("buy/shop")
    fun postBuyShop(@Body params: Buy): Call<NewResultResponse<Buy>>

    @POST("buy/ship")
    fun postBuyShip(@Body params: Buy): Call<NewResultResponse<Buy>>

    @POST("buy/qr")
    fun postBuyQr(@Body params: Buy): Call<NewResultResponse<Buy>>

    @POST("buy/lpng/pay")
    fun postBuyLpng(@Body params: Lpng): Call<NewResultResponse<Lpng>>

    @POST("buy/ftlink/pay")
    fun postBuyFTLinkPay(@Body params: FTLink): Call<NewResultResponse<FTLink>>

    @DELETE("buy")
    fun requestDeleteBuy(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("buy")
    fun getOneBuy(@QueryMap params: Map<String, String>): Call<NewResultResponse<Buy>>

    @GET("buy/detail")
    fun getOneBuyDetail(@QueryMap params: Map<String, String>): Call<NewResultResponse<Buy>>

    @GET("buy/detail")
    fun getBuy(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Buy>>>

    @get:GET("buy/orderId")
    val buyOrderId: Call<NewResultResponse<String>>

    @get:GET("buy/orderId/lpng")
    val lpngOrderId: Call<NewResultResponse<String>>

    @GET("buyGoods/detail")
    fun getBuyGoods(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<BuyGoods>>>

    @GET("buyGoods/detail")
    fun getOneBuyGoods(@QueryMap params: Map<String, String>): Call<NewResultResponse<BuyGoods>>

    @GET("goodsReview/count")
    fun getGoodsReviewCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Count>>

    @GET("buyGoods/buyCount")
    fun getBuyCountByGoodsSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<Count>>

    @GET("goodsLike/count")
    fun getGoodsLikeCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Count>>

    @POST("goodsLike")
    fun postGoodsLike(@Body goods: GoodsLike): Call<NewResultResponse<GoodsLike>>

    @HTTP(method = "DELETE", path = "goodsLike", hasBody = true)
    fun requestDeleteGoodsLike(@Body goods: GoodsLike): Call<NewResultResponse<GoodsLike>>

    @GET("goodsLike")
    fun getGoodsLikeOne(@QueryMap params: Map<String, String>): Call<NewResultResponse<GoodsLike>>

    @GET("goodsLike/detail")
    fun getGoodsLike(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<GoodsLike>>>

    @GET("goodsLike/detailBySalesType")
    fun getGoodsLikeBySalesType(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<GoodsLike>>>

    @GET("page/getCategoryList")
    fun getCategoryList(@QueryMap params: Map<String, String>): Call<NewResultResponse<Category>>

    @FormUrlEncoded
    @POST("user/sns/account")
    fun putSnsAccount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("user/check")
    fun userCheck(@QueryMap params: Map<String, String>): Call<NewResultResponse<String>>

    @GET("pageGoodsCategory")
    fun getPageGoodsCategory(@QueryMap params: Map<String, String>): Call<NewResultResponse<PageGoodsCategory>>

    @GET("page/management")
    fun getPageManagement(@QueryMap params: Map<String, String>): Call<NewResultResponse<PageManagement>>


    @get:GET("event/lotto")
    val lotto: Call<NewResultResponse<Lotto>>

    @GET("event/lottoPlaybol/gift")
    fun getLottoPlayGiftList(@QueryMap params: Map<String, String>): Call<NewResultResponse<LottoGift>>

    @GET("event/lotto/winner/count")
    fun getLottoWinnerCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("event/lotto/winner")
    fun getLottoWinnerList(@QueryMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @GET("event/lotto/getCount")
    fun getLottoCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("event/lotto/getList")
    fun getLottoList(@QueryMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @GET("event/join/ticketHistory/count")
    fun getTicketHistoryCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("event/join/ticketHistory")
    fun getTicketHistory(@QueryMap params: Map<String, String>): Call<NewResultResponse<Bol>>

    @GET("event/lotto/ticketHistory/count")
    fun getLottoTicketHistoryCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("event/lotto/ticketHistory")
    fun getLottoTicketHistory(@QueryMap params: Map<String, String>): Call<NewResultResponse<Bol>>

    @GET("event/lotto/winner/user")
    fun getLottoResult(@QueryMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @GET("event/lotto/user/join/count")
    fun getLottoJoinCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("event/lotto/user/join")
    fun getLottoJoinList(@QueryMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @get:GET("event/random/prnumber")
    val randomPrnumber: Call<NewResultResponse<String>>

    @get:GET("page/hashtag/list")
    val hashTagList: Call<NewResultResponse<HashTag>>

    @FormUrlEncoded
    @POST("common/cpeReport")
    fun requestCpeReport(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("common/cpaReport")
    fun requestCpaReport(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("auth/updatePushKey")
    fun updatePushKey(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("buy/bootpay/check")
    fun requestCheckBootPay(@FieldMap params: Map<String, String>): Call<NewResultResponse<Buy>>

    @GET("plus/getListWithPlusGoods")
    fun getPlusListWithPlusGoods(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Plus2>>>

    @GET("plus/getPlusListWithNews")
    fun getPlusListWithNews(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Plus2>>>

    @GET("goods/detail/pageSeqNo")
    fun getGoodsListByPageSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Goods>>>

    @POST("user/updatePayPassword")
    fun updatePayPassword(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("user/updatePayPasswordWithVerification")
    fun updatePayPasswordWithVerification(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("user/checkPayPassword")
    fun checkPayPassword(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:GET("card/list")
    val cardList: Call<NewResultResponse<Card>>

    @GET("card/updateRepresent")
    fun updateRepresentCard(@QueryMap params: Map<String, String>): Call<NewResultResponse<Card>>

    @POST("card/register")
    fun postCardRegister(@Body params: CardReq): Call<NewResultResponse<Card>>

    @DELETE("card/delete")
    fun deleteCard(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("user/updateBuyPlusTerms")
    fun updateBuyPlusTerms(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("user/updatePlusPush")
    fun updatePlusPush(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("plus/updatePushActivate")
    fun updatePushActivate(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("plus/updatePlusGift")
    fun updatePlusGift(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:GET("shippingSite/list")
    val shippingSiteList: Call<NewResultResponse<ShippingSite>>

    @POST("shippingSite/insert")
    fun insertShippingSite(@Body params: ShippingSite): Call<NewResultResponse<ShippingSite>>

    @PUT("shippingSite/update")
    fun updateShippingSite(@Body params: ShippingSite): Call<NewResultResponse<ShippingSite>>

    @DELETE("shippingSite/delete")
    fun deleteShippingSite(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:GET("category/majorOnly/list")
    val categoryMajorOnlyList: Call<NewResultResponse<CategoryMajor>>

    @GET("category/majorOnly")
    fun getCategoryMajorOnly(@QueryMap params: Map<String, String>): Call<NewResultResponse<CategoryMajor>>

    @get:GET("category/major/list")
    val categoryMajor: Call<NewResultResponse<CategoryMajor>>

    @GET("category/minor/list")
    fun getCategoryMinorList(@QueryMap params: Map<String, String>): Call<NewResultResponse<CategoryMinor>>

    @GET("goodsImage/list")
    fun getGoodsImageList(@QueryMap params: Map<String, String>): Call<NewResultResponse<GoodsImage>>

    @get:GET("buy/countAll")
    val buyCountAll: Call<NewResultResponse<Int>>

    @get:GET("goodsReview/countAll")
    val goodsReviewCountAll: Call<NewResultResponse<Int>>

    @GET("juso/list")
    fun getJusoList(@QueryMap params: Map<String, String>): Call<NewResultResponse<Juso>>

    @get:GET("juso/doList")
    val doList: Call<NewResultResponse<Province>>

    @get:GET("category/myFavorite")
    val myFavoriteCategoryList: Call<NewResultResponse<CategoryFavorite>>

    @POST("category/favorite/insert")
    fun insertCategoryFavorite(@Body params: CategoryFavorite): Call<NewResultResponse<CategoryFavorite>>

    @DELETE("category/favorite/delete")
    fun deleteCategoryFavorite(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @PUT("user/updateActiveArea1")
    fun updateActiveArea1(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @PUT("user/updateActiveArea2")
    fun updateActiveArea2(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @PUT("user/updateQrImage")
    fun updateQrImage(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @PUT("event/updateUseGift")
    fun updateUseGift(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("shippingSite/checkIslandsRegion")
    fun checkIslandsRegion(@QueryMap params: Map<String, String>): Call<NewResultResponse<Boolean>>

    @GET("shippingSite/getIsLandsRegion")
    fun getIsLandsRegion(@QueryMap params: Map<String, String>): Call<NewResultResponse<IslandsRegion>>

    @get:GET("category/first/list")
    val categoryFirstList: Call<NewResultResponse<CategoryFirst>>

    @GET("businessLicense/get")
    fun getBusinessLicense(@QueryMap params: Map<String, String>): Call<NewResultResponse<BusinessLicense>>

    @GET("goods/price/shipTypeByPageSeqNoOnlyNormal")
    fun getGoodsPriceListShipTypeByPageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<GoodsPrice>>>

    @GET("product/price/shipTypeIsLuckybol")
    fun getProductPriceShipType(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/shipTypeByPageSeqNoOnlyNormal")
    fun getProductPriceListShipTypeByPageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/storeTypeByPageSeqNoOnlyNormal")
    fun getProductPriceListStoreTypeByPageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/storeTypeByPageSeqNoAndDiscountOnlyNormal")
    fun getProductPriceListStoreTypeByPageSeqNoAndDiscountOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/isSubscriptionAndIsPrepaymentOnlyNormal")
    fun getProductPriceListByIsSubscriptionAndIsPrepaymentOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/shipTypeByManageSeqNoOnlyNormal")
    fun getProductPriceListShipTypeByManageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/ticketTypeByManageSeqNoOnlyNormal")
    fun getProductPriceListTicketTypeByManageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/shipTypeByPageAndDiscount")
    fun getProductPriceListShipTypeByPageAndDiscount(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/storeTypeByPageAndDiscountDistanceDesc")
    fun getProductPriceListStoreTypeByPageAndDiscountDistanceDesc(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/getPlusSubscriptionTypeOnlyNormalOrderByDistance")
    fun getPlusSubscriptionTypeOnlyNormalOrderByDistance(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/subscriptionTypeByPageSeqNoOnlyNormal")
    fun getSubscriptionTypeByPageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/moneyTypeByPageSeqNoOnlyNormal")
    fun getProductPriceListMoneyTypeByPageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price/getLastSubscriptionTypeByPageSeqNoOnlyNormal")
    fun getLastSubscriptionTypeByPageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductPrice>>

    @GET("product/price/getLastMoneyTypeByPageSeqNoOnlyNormal")
    fun getLastMoneyTypeByPageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductPrice>>

    @GET("product/price")
    fun getProductPrice(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductPrice>>

    @GET("product/price/main")
    fun getMainProductPrice(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductPrice>>

    @GET("product/review/countByProductPriceSeqNo")
    fun getCountProductReviewByProductPriceSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @get:GET("product/review/countByMemberSeqNo")
    val countProductReviewByyMemberSeqNo: Call<NewResultResponse<Int>>

    @GET("product/review/countByPageSeqNo")
    fun getCountProductReviewByPageSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("product/like/check")
    fun getProductLikeCheck(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("product/like/insert")
    fun insertProductLike(@Body params: ProductLike): Call<NewResultResponse<Any>>

    @HTTP(method = "DELETE", path = "product/like/delete", hasBody = true)
    fun deleteProductLike(@Body params: ProductLike): Call<NewResultResponse<Any>>

    @GET("product/review/productPriceSeqNo")
    fun getProductReviewByProductPriceSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductReview>>>

    @GET("product/review/pageSeqNo")
    fun getProductReviewByPageSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductReview>>>

    @GET("product/review/getLastProductReviewByPageSeqNo")
    fun getLastProductReviewByPageSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductReview>>

    @GET("product/review/memberSeqNo")
    fun getProductReviewByMemberSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductReview>>>

    @POST("product/review/insert")
    fun insertProductReview(@Body params: ProductReview): Call<NewResultResponse<Any>>

    @PUT("product/review/update")
    fun updateProductReview(@Body params: ProductReview): Call<NewResultResponse<Any>>

    @DELETE("product/review/delete")
    fun deleteProductReview(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("product/option")
    fun getProductOption(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductOptionTotal>>

    @get:GET("product/like/count")
    val countProductLike: Call<NewResultResponse<Int>>

    @GET("product/like/shippingList")
    fun getProductLikeShippingList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductLike>>>

    @GET("purchase/product/list")
    fun getPurchaseProductListByMemberSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<PurchaseProduct>>>

    @GET("purchase/product/ticketList")
    fun getPurchaseProductListTicketTypeByMemberSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<PurchaseProduct>>>

    @get:GET("purchase/product/count")
    val countPurchaseProductByMemberSeqNo: Call<NewResultResponse<Int>>

    @GET("purchase/product")
    fun getPurchaseProduct(@QueryMap params: Map<String, String>): Call<NewResultResponse<PurchaseProduct>>

    @PUT("purchase/product/complete")
    fun updatePurchaseProductComplete(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("purchase/product/cancel")
    fun cancelPurchase(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("product/review/countGroupByEval")
    fun getProductReviewCountGroupByEval(@QueryMap params: Map<String, String>): Call<NewResultResponse<ReviewCountEval>>

    @GET("product/review/countGroupByEvalByPageSeqNo")
    fun getProductReviewCountGroupByEvalByPageSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ReviewCountEval>>

    @GET("product/info")
    fun getProductInfoByProductSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductInfo>>

    @GET("product/auth")
    fun getProductAuthByProductSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductAuth>>

    @GET("product/notice/list")
    fun getProductNoticeListByProductSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductNotice>>

    @POST("purchase/orderId")
    fun postOrderId(): Call<NewResultResponse<String>>

    @POST("purchase/ship")
    fun postPurchaseShip(@Body params: Purchase): Call<NewResultResponse<Purchase>>

    @POST("purchase/ticket")
    fun postPurchaseTicket(@Body params: Purchase): Call<NewResultResponse<Purchase>>

    @POST("purchase/ftlink/pay")
    fun postPurchaseFTLinkPay(@Body params: FTLink): Call<NewResultResponse<FTLink>>

    @POST("purchase/bootpay/verify")
    fun postPurchaseBootPayVerify(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("giftshow/list")
    fun getGiftishowList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Giftishow>>>

//    @GET("giftshow/listByBrand")
    @GET("giftshow/listByBrandAndMinPrice")
    fun getGiftishowListByBrand(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Giftishow>>>

    @POST("giftshow/buyByBol")
    fun postGiftishowBuy(@Body params: GiftishowBuy): Call<NewResultResponse<Any>>

    @GET("giftshow/buy/list")
    fun getGiftishowBuyList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<GiftishowBuy>>>

    @get:GET("giftshow/buy/count")
    val giftishowBuyCount: Call<NewResultResponse<Int>>

    @POST("giftshow/check")
    fun checkGiftishowStatus(@QueryMap params: Map<String, String>): Call<NewResultResponse<String>>

    @FormUrlEncoded
    @POST("giftshow/resend")
    fun resendGiftishowStatus(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:GET("giftshow/getMobileCategoryList")
    val mobileCategoryList: Call<NewResultResponse<MobileCategory>>

    @GET("giftshow/getMobileBrandList")
    fun getMobileBrandList(@QueryMap params: Map<String, String>): Call<NewResultResponse<MobileBrand>>

    @POST("chat/insert")
    fun insertChat(@Body params: ChatData): Call<NewResultResponse<Any>>

    @get:GET("themeCategory/list")
    val themeCategoryList: Call<NewResultResponse<ThemeCategory>>

    @POST("member/attendance")
    fun attendance(): Call<NewResultResponse<MemberAttendance>>

    @GET("product/price/getCountByPageSeqNo")
    fun getCountByPageSeqNoOnlyNormal(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("news/getListByPageSeqNo")
    fun getNewsListByPageSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<News>>>

    @GET("news/getPlusNewsList")
    fun getPlusNewsList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<News>>>

    @GET("news/get")
    fun getNews(@QueryMap params: Map<String, String>): Call<NewResultResponse<News>>

    @GET("news/getNewsReviewList")
    fun getNewsReviewList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<NewsReview>>>

    @POST("news/review/insert")
    fun insertNewsReview(@Body params: NewsReview): Call<NewResultResponse<Any>>

    @POST("news/review/update")
    fun updateNewsReview(@Body params: NewsReview): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("news/review/delete")
    fun deleteNewsReview(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("news/getNewsCountByPageSeqNo")
    fun getNewsCountByPageSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @POST("member/address/save")
    fun saveMemberAddress(@Body params: MemberAddress): Call<NewResultResponse<MemberAddress>>

    @get:GET("member/address/get")
    val memberAddress: Call<NewResultResponse<MemberAddress>>

    @GET("event/policy/list")
    fun getEventPolicyList(@QueryMap params: Map<String, String>): Call<NewResultResponse<EventPolicy>>

    @GET("virtualNumber/getVirtualNumberManage")
    fun getVirtualNumberManage(@QueryMap params: Map<String, String>): Call<NewResultResponse<VirtualNumberManage>>

    @GET("virtualNumber/getNbookVirtualNumberManageList")
    fun getNbookVirtualNumberManageList(): Call<NewResultResponse<VirtualNumberManage>>

    @GET("event/getEventDetailList")
    fun getEventDetailList(@QueryMap params: Map<String, String>): Call<NewResultResponse<EventDetail>>

    @GET("event/getEventDetailImageList")
    fun getEventDetailImageList(@QueryMap params: Map<String, String>): Call<NewResultResponse<EventDetailImage>>

    @GET("event/getEventDetailItemList")
    fun getEventDetailItemList(@QueryMap params: Map<String, String>): Call<NewResultResponse<EventDetailItem>>

    @GET("page/getPageListWithProductPrice")
    fun getPageListWithProductPrice(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("page/getPageListWithSubscription")
    fun getPageListWithSubscription(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("page/getOrderPageList")
    fun getOrderPageList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @FormUrlEncoded
    @POST("common/makeQrCode")
    fun makeQrCode(@FieldMap params: Map<String, String>): Call<NewResultResponse<String>>

    @POST("visitorPoint/givePointFromUser")
    fun postVisitorGivePoint(@Body params: VisitorPointGiveHistory): Call<NewResultResponse<Any>>

    @POST("visitorPoint/givePointWithStamp")
    fun postVisitorGivePointWithStamp(@Body params: VisitorPointGiveHistory): Call<NewResultResponse<Any>>

    @GET("visitorPoint/getFirstBenefit")
    fun getFirstBenefit(@QueryMap params: Map<String, String>): Call<NewResultResponse<VisitorPointGiveHistory>>

    @get:GET("subscription/getSubscriptionDownloadCountByMemberSeqNoAndStatus")
    val subscriptionDownloadCountByMemberSeqNoAndStatus: Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("subscription/downloadFromUser")
    fun subscriptionDownload(@FieldMap params: Map<String, String>): Call<NewResultResponse<SubscriptionDownload>>

    @FormUrlEncoded
    @POST("subscription/downloadWithStamp")
    fun subscriptionDownloadWithStamp(@FieldMap params: Map<String, String>): Call<NewResultResponse<SubscriptionDownload>>

    @GET("subscription/getSubscriptionDownloadListByMemberSeqNo")
    fun getSubscriptionDownloadListByMemberSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<SubscriptionDownload>>>

    @GET("subscription/getSubscriptionDownloadBySeqNo")
    fun getSubscriptionDownloadBySeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubscriptionDownload>>

    @GET("subscription/getSubscriptionLogListBySubscriptionDownloadSeqNo")
    fun getSubscriptionLogListBySubscriptionDownloadSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubscriptionLog>>

    @FormUrlEncoded
    @POST("subscription/useFromUser")
    fun subscriptionUse(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("subscription/useWithStamp")
    fun subscriptionUseWithStamp(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("pageAttendance/saveAndGet")
    fun pageAttendanceSaveAndGet(@FieldMap params: Map<String, String>): Call<NewResultResponse<PageAttendance>>

    @FormUrlEncoded
    @POST("pageAttendance/attendanceWithStamp")
    fun pageAttendanceWithStamp(@FieldMap params: Map<String, String>): Call<NewResultResponse<PageAttendance>>

    @GET("pageAttendance/getPageAttendanceLogList")
    fun getPageAttendanceLogList(@QueryMap params: Map<String, String>): Call<NewResultResponse<PageAttendanceLog>>

    @GET("page/getDeliveryPageList")
    fun getDeliveryPageList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("page/getDeliveryPageListByKeyword")
    fun getDeliveryPageListByKeyword(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("page/getVisitPageList")
    fun getVisitPageList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("page/getVisitPageListByKeyword")
    fun getVisitPageListByKeyword(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("page/getVisitPageListByArea")
    fun getVisitPageListByArea(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("page/getServicePageList")
    fun getServicePageList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("menu/getOrderMenuGroupWithMenuList")
    fun getOrderMenuGroupWithMenuList(@QueryMap params: Map<String, String>): Call<NewResultResponse<OrderMenuGroup>>

    @GET("menu/getDelegateOrderMenuList")
    fun getDelegateOrderMenuList(@QueryMap params: Map<String, String>): Call<NewResultResponse<OrderMenu>>


    @GET("menu/getMenu")
    fun getMenu(@QueryMap params: Map<String, String>): Call<NewResultResponse<OrderMenu>>

    @GET("cart/getCartCount")
    fun getCartCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("cart/getCartList")
    fun getCartList(@QueryMap params: Map<String, String>): Call<NewResultResponse<Cart>>

    @GET("cart/checkCartPage")
    fun checkCartPage(@QueryMap params: Map<String, String>): Call<NewResultResponse<Boolean>>

    @POST("cart/saveCart")
    fun saveCart(@Body params: Cart): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("cart/updateAmount")
    fun updateAmount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("cart/deleteCart")
    fun deleteCart(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("orderPurchase/purchase")
    fun orderPurchase(@Body params: OrderPurchase): Call<NewResultResponse<OrderPurchase>>

    @POST("orderPurchase/ftlink/pay")
    fun orderPurchaseFTLinkPay(@Body params: FTLink): Call<NewResultResponse<FTLink>>

    @FormUrlEncoded
    @POST("orderPurchase/bootpay/verify")
    fun orderPurchaseVerifyBootPay(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("orderPurchase/getOrderPurchaseListByMemberSeqNo")
    fun getOrderPurchaseListByMemberSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<OrderPurchase>>>

    @GET("orderPurchase/getTicketPurchaseListByMemberSeqNo")
    fun getTicketPurchaseListByMemberSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<OrderPurchase>>>

    @GET("orderPurchase/getOrderPurchase")
    fun getOrderPurchase(@QueryMap params: Map<String, String>): Call<NewResultResponse<OrderPurchase>>

    @POST("menu/insertReview")
    fun insertReview(@Body params: OrderMenuReview): Call<NewResultResponse<Any>>

    @PUT("menu/updateReview")
    fun updateReview(@Body params: OrderMenuReview): Call<NewResultResponse<Any>>

    @DELETE("menu/deleteReview")
    fun deleteReview(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("menu/getReviewByMemberSeqNo")
    fun getReviewByMemberSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<OrderMenuReview>>>

    @GET("menu/getReviewByPageSeqNo")
    fun getReviewByPageSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<OrderMenuReview>>>

    @GET("menu/getReviewCountGroupByEvalByPageSeqNo")
    fun getReviewCountGroupByEvalByPageSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ReviewCountEval>>

    @FormUrlEncoded
    @POST("orderPurchase/cancelOrderPurchaseUser")
    fun cancelOrderPurchaseUser(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("notificationBox/getNotificationBoxList")
    fun getNotificationBoxList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<NotificationBox>>>

    @FormUrlEncoded
    @POST("notificationBox/delete")
    fun notificationBoxDelete(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("locationServiceLog/save")
    fun locationServiceLogSave(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("page/getPageListWithPrepayment")
    fun getPageListWithPrepayment(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("page/getPageListWithPrepaymentExistVisitLog")
    fun getPageListWithPrepaymentExistVisitLog(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("prepayment/getPrepaymentRetentionCount")
    fun getPrepaymentRetentionCount(): Call<NewResultResponse<Int>>

    @GET("prepayment/getPrepayment")
    fun getPrepayment(@QueryMap params: Map<String, String>): Call<NewResultResponse<Prepayment>>

    @FormUrlEncoded
    @POST("prepayment/publish")
    fun prepaymentPublish(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("prepayment/use")
    fun prepaymentUse(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("page/getPageListWithPageWithPrepaymentPublish")
    fun getPageListWithPageWithPrepaymentPublish(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Page2>>>

    @GET("prepayment/getPrepaymentPublish")
    fun getPrepaymentPublish(@QueryMap params: Map<String, String>): Call<NewResultResponse<PrepaymentPublish>>

    @GET("prepayment/getPrepaymentLogList")
    fun getPrepaymentLogList(@QueryMap params: Map<String, String>): Call<NewResultResponse<PrepaymentLog>>

    @GET("visitLog/get")
    fun getVisitLog(@QueryMap params: Map<String, String>): Call<NewResultResponse<VisitLog>>

    @FormUrlEncoded
    @POST("visitLog/receive")
    fun visitLogReceive(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("orderPurchase/useTicket")
    fun useTicket(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

}