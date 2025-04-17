package com.pplus.prnumberbiz.core.network.apis;

import com.pplus.prnumberbiz.core.network.model.dto.Advertise;
import com.pplus.prnumberbiz.core.network.model.dto.Agent;
import com.pplus.prnumberbiz.core.network.model.dto.AppVersion;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
import com.pplus.prnumberbiz.core.network.model.dto.Bol;
import com.pplus.prnumberbiz.core.network.model.dto.BolGift;
import com.pplus.prnumberbiz.core.network.model.dto.Buy;
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods;
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoodsTypePrice;
import com.pplus.prnumberbiz.core.network.model.dto.Cash;
import com.pplus.prnumberbiz.core.network.model.dto.Category;
import com.pplus.prnumberbiz.core.network.model.dto.Comment;
import com.pplus.prnumberbiz.core.network.model.dto.Coord;
import com.pplus.prnumberbiz.core.network.model.dto.Count;
import com.pplus.prnumberbiz.core.network.model.dto.CountryConfig;
import com.pplus.prnumberbiz.core.network.model.dto.Coupon;
import com.pplus.prnumberbiz.core.network.model.dto.CouponTemplate;
import com.pplus.prnumberbiz.core.network.model.dto.Customer;
import com.pplus.prnumberbiz.core.network.model.dto.Delivery;
import com.pplus.prnumberbiz.core.network.model.dto.DeliveryTotalPrice;
import com.pplus.prnumberbiz.core.network.model.dto.Fan;
import com.pplus.prnumberbiz.core.network.model.dto.Faq;
import com.pplus.prnumberbiz.core.network.model.dto.FaqGroup;
import com.pplus.prnumberbiz.core.network.model.dto.Franchise;
import com.pplus.prnumberbiz.core.network.model.dto.FranchiseGroup;
import com.pplus.prnumberbiz.core.network.model.dto.Friend;
import com.pplus.prnumberbiz.core.network.model.dto.Goods;
import com.pplus.prnumberbiz.core.network.model.dto.GoodsNoticeInfo;
import com.pplus.prnumberbiz.core.network.model.dto.GoodsReview;
import com.pplus.prnumberbiz.core.network.model.dto.Group;
import com.pplus.prnumberbiz.core.network.model.dto.HashTag;
import com.pplus.prnumberbiz.core.network.model.dto.ImgUrl;
import com.pplus.prnumberbiz.core.network.model.dto.Lpng;
import com.pplus.prnumberbiz.core.network.model.dto.LpngRes;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGift;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftCategory;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftHistory;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftPurchase;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.dto.NoteReceive;
import com.pplus.prnumberbiz.core.network.model.dto.NoteSend;
import com.pplus.prnumberbiz.core.network.model.dto.Notice;
import com.pplus.prnumberbiz.core.network.model.dto.NoticeInfo;
import com.pplus.prnumberbiz.core.network.model.dto.OrderCount;
import com.pplus.prnumberbiz.core.network.model.dto.OrderTypeCount;
import com.pplus.prnumberbiz.core.network.model.dto.OutgoingNumber;
import com.pplus.prnumberbiz.core.network.model.dto.Page;
import com.pplus.prnumberbiz.core.network.model.dto.PageEval;
import com.pplus.prnumberbiz.core.network.model.dto.PageGoodsCategory;
import com.pplus.prnumberbiz.core.network.model.dto.PageManagement;
import com.pplus.prnumberbiz.core.network.model.dto.PageSeller;
import com.pplus.prnumberbiz.core.network.model.dto.Payment;
import com.pplus.prnumberbiz.core.network.model.dto.Post;
import com.pplus.prnumberbiz.core.network.model.dto.Price;
import com.pplus.prnumberbiz.core.network.model.dto.Report;
import com.pplus.prnumberbiz.core.network.model.dto.ResultAddress;
import com.pplus.prnumberbiz.core.network.model.dto.SavedMsg;
import com.pplus.prnumberbiz.core.network.model.dto.Sns;
import com.pplus.prnumberbiz.core.network.model.dto.Target;
import com.pplus.prnumberbiz.core.network.model.dto.Terms;
import com.pplus.prnumberbiz.core.network.model.dto.UseCount;
import com.pplus.prnumberbiz.core.network.model.dto.User;
import com.pplus.prnumberbiz.core.network.model.dto.Verification;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsContact;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsCustomerGroup;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsCustomerList;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsFanGroup;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsGroupPriority;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroImage;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroMovie;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsNoteSend;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsRegDevice;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

/**
 * Created by 김종경 on 2016-10-06.
 */

public interface IPRNumberApi {

    //중복 확인
    @FormUrlEncoded
    @POST("auth/existsUser")
    Call<NewResultResponse<Object>> requestExistsUser(@FieldMap Map<String, String> params);

    //회원가입
    @POST("auth/join")
    Call<NewResultResponse<User>> requestJoin(@Body User params);

    //회원가입
    @POST("auth/levelup")
    Call<NewResultResponse<User>> requestLevelup(@Body User params);

    //앱 버전 가져오기
    @FormUrlEncoded
    @POST("auth/getAppVersion")
    Call<NewResultResponse<AppVersion>> requestAppVersion(@FieldMap Map<String, String> params);

    //앱 로그인
    @FormUrlEncoded
    @POST("auth/login")
    Call<NewResultResponse<User>> requestLogin(@FieldMap Map<String, String> params);

    //디바이스 존재여부 체크
    @FormUrlEncoded
    @POST("auth/existsDevice")
    Call<NewResultResponse<User>> requestExistsDevice(@FieldMap Map<String, String> params);

    @POST("auth/registDevice")
    Call<NewResultResponse<User>> requestRegistDevice(@Body ParamsRegDevice params);

    //약관 리스트
    @FormUrlEncoded
    @POST("auth/getActiveTermsAll")
    Call<NewResultResponse<Terms>> requestGetActiveTermsAll(@Field("appKey") String appKey);

    //미동의 약관 리스트
    @FormUrlEncoded
    @POST("auth/getNotSignedActiveTermsAll")
    Call<NewResultResponse<Terms>> requestGetNotSignedActiveTermsAll(@Field("appKey") String appKey);

    @POST("auth/activatePage")
    Call<NewResultResponse<User>> requestActivatePage(@Body User params);

    @POST("auth/startPage")
    Call<NewResultResponse<User>> requestStartPage();

    @POST("auth/getSession")
    Call<NewResultResponse<User>> requestGetSession();

    @POST("auth/reloadSession")
    Call<NewResultResponse<User>> requestReloadSession();

    @FormUrlEncoded
    @POST("auth/getUserByVerification")
    Call<NewResultResponse<User>> requestGetUserByVerification(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("auth/getUserByLoginIdAndMobile")
    Call<NewResultResponse<User>> requestGetUserByLoginIdAndMobile(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("auth/changePasswordByVerification")
    Call<NewResultResponse<Object>> requestChangePasswordByVerification(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("auth/updateMobileByVerification")
    Call<NewResultResponse<Object>> requestUpdateMobileByVerification(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("auth/leave")
    Call<NewResultResponse<Object>> requestLeave(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("verification/cancelLeave")
    Call<NewResultResponse<Object>> requestCancelLeave(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("auth/agreeTermsList")
    Call<NewResultResponse<Object>> requestAgreeTermsList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("auth/updateAuthCodeByVerification")
    Call<NewResultResponse<Object>> requestUpdateAuthCodeByVerification(@FieldMap Map<String, String> params);

    @GET("auth/getAgent")
    Call<NewResultResponse<Agent>> requestGetAgent(@QueryMap Map<String, String> params);

    //page 가져오기
    @FormUrlEncoded
    @POST("page/getPage")
    Call<NewResultResponse<Page>> requestPage(@FieldMap Map<String, String> params);

    //앱 버전 가져오기
    @POST("my/getMe")
    Call<NewResultResponse<User>> requestGetMe();

    //프로필이미지 세팅
    @FormUrlEncoded
    @POST("my/updateProfileImage")
    Call<NewResultResponse<Object>> requestUpdateProfileImage(@FieldMap Map<String, String> params);

    //push 세팅
    @FormUrlEncoded
    @POST("my/updatePushConfig")
    Call<NewResultResponse<Object>> requestUpdatePushConfig(@FieldMap Map<String, String> params);

    //인증번호 insert
    @FormUrlEncoded
    @POST("my/insertAuthedNumber")
    Call<NewResultResponse<Object>> requestInsertAuthedNumber(@FieldMap Map<String, String> params);

    //인증번호 리스트
    @POST("my/getAuthedNumberAll")
    Call<NewResultResponse<OutgoingNumber>> requestGetAuthedNumberAll();

    //인증번호 delete
    @FormUrlEncoded
    @POST("my/deleteAuthedNumber")
    Call<NewResultResponse<Object>> requestDeleteAuthedNumber(@FieldMap Map<String, String> params);

    //카테고리 리스트
    @FormUrlEncoded
    @POST("page/getCategoryAll")
    Call<NewResultResponse<Category>> requestGetCategoryAll(@FieldMap Map<String, String> params);

    @POST("page/updatePage")
    Call<NewResultResponse<Page>> requestUpdatePage(@Body Page page);

    @POST("page/updateProperties")
    Call<NewResultResponse<Page>> requestUpdatePageProperties(@Body Page page);

    @POST("page/updatePropertiesAll")
    Call<NewResultResponse<Page>> requestUpdatePagePropertiesAll(@Body Page page);

    //프로필이미지 세팅
    @FormUrlEncoded
    @POST("page/updateProfileImage")
    Call<NewResultResponse<Object>> requestUpdatePageProfileImage(@FieldMap Map<String, String> params);

    //백그라운 세팅
    @FormUrlEncoded
    @POST("page/updateBackgroundImage")
    Call<NewResultResponse<Object>> requestUpdatePageBackgroundImage(@FieldMap Map<String, String> params);

    //소개이미지 등록
    @POST("page/updateIntroImageList")
    Call<NewResultResponse<Page>> requestUpdateIntroImageList(@Body ParamsIntroImage params);

    //소개이미지 리스트
    @FormUrlEncoded
    @POST("page/getIntroImageAll")
    Call<NewResultResponse<Attachment>> requestGetIntroImageAll(@FieldMap Map<String, String> params);

    //sns list
    @FormUrlEncoded
    @POST("page/getSnsLinkAll")
    Call<NewResultResponse<Sns>> requestGetSnsLinkAll(@FieldMap Map<String, String> params);

    @POST("page/saveSnsLink")
    Call<NewResultResponse<Sns>> requestSaveSnsLink(@Body Sns params);

    @FormUrlEncoded
    @POST("page/deleteSnsLinkByType")
    Call<NewResultResponse<Object>> requestDeleteSnsLinkByType(@FieldMap Map<String, String> params);

    //포스트 카운트
    @FormUrlEncoded
    @POST("post/getBoardPostCount")
    Call<NewResultResponse<Integer>> requestGetBoardPostCount(@FieldMap Map<String, String> params);

    //포스트 리스트
    @FormUrlEncoded
    @POST("post/getBoardPostList")
    Call<NewResultResponse<Post>> requestGetBoardPostList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("post/getPostWithAttachment")
    Call<NewResultResponse<Post>> requestGetPost(@Field("postNo") Long postNo);

    //포스트 등록
    @POST("post/insertPost")
    Call<NewResultResponse<Post>> requestInsertPost(@Body Post params);

    //포스트 수정
    @POST("post/updatePost")
    Call<NewResultResponse<Post>> requestUpdatePost(@Body Post params);

    //포스트 삭제
    @FormUrlEncoded
    @POST("post/deletePost")
    Call<NewResultResponse<Object>> requestDeletePost(@Field("postNo") Long postNo);

    //댓글 리스트
    @FormUrlEncoded
    @POST("post/getCommentAll")
    Call<NewResultResponse<Comment>> requestGetCommentAll(@Field("postNo") Long postNo);

    //댓글입력
    @POST("post/insertComment")
    Call<NewResultResponse<Comment>> requestInsertComment(@Body Comment params);

    //댓글수정
    @POST("post/updateComment")
    Call<NewResultResponse<Comment>> requestUpdateComment(@Body Comment params);

    //댓글삭제
    @FormUrlEncoded
    @POST("post/deleteComment")
    Call<NewResultResponse<Object>> requestDeleteComment(@Field("commentNo") Long commentNo);

    //첨부파일 삭제
    @FormUrlEncoded
    @POST("common/deleteAttachment")
    Call<NewResultResponse<Object>> requestDeleteAttachment(@Field("no") Long no);

    @FormUrlEncoded
    @POST("common/copyAttachment")
    Call<NewResultResponse<Attachment>> requestCopyAttachment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/searchAddress")
    Call<ResultAddress> requestSearchAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/getDefaultImageList")
    Call<NewResultResponse<Attachment>> requestGetDefaultImageList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/getCoordByAddress")
    Call<NewResultResponse<Coord>> requestGetCoordByAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/getNoticeCount")
    Call<NewResultResponse<Integer>> requestGetNoticeCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/getNoticeList")
    Call<NewResultResponse<Notice>> requestGetNoticeList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/getNotice")
    Call<NewResultResponse<Notice>> requestGetNotice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/getFaqGroupAll")
    Call<NewResultResponse<FaqGroup>> requestGetFaqGroupAll(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/getFaqCount")
    Call<NewResultResponse<Integer>> requestGetFaqCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/getFaqList")
    Call<NewResultResponse<Faq>> requestGetFaqList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("common/getFaq")
    Call<NewResultResponse<Faq>> requestGetFaq(@FieldMap Map<String, String> params);

    @POST("common/getCountryConfigAll")
    Call<NewResultResponse<CountryConfig>> requestGetCountryConfigAll();

    @POST("common/reporting")
    Call<NewResultResponse<Report>> requestReporting(@Body Report report);

    //인증 요청
    @FormUrlEncoded
    @POST("verification/request")
    Call<NewResultResponse<Verification>> requestVerification(@FieldMap Map<String, String> params);

    //인증 요청
    @FormUrlEncoded
    @POST("verification/confirm")
    Call<NewResultResponse<Object>> confirmVerification(@FieldMap Map<String, String> params);

    //캐시 충전(pg않붙음)
    @POST("cash/aos/chargeCash")
    Call<NewResultResponse<Cash>> requestCashCharge(@Body Cash params);

    //캐시 선물
    @FormUrlEncoded
    @POST("cash/give")
    Call<NewResultResponse<Cash>> requestCashGive(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cash/getHistoryCount")
    Call<NewResultResponse<Integer>> requestGetCashHistoryCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cash/getHistoryList")
    Call<NewResultResponse<Cash>> requestGetCashHistoryList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cash/getHistoryTotalAmount")
    Call<NewResultResponse<String>> requestGetCashHistoryTotalAmount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bol/getHistoryList")
    Call<NewResultResponse<Bol>> requestGetBolHistoryList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bol/getHistoryTotalAmount")
    Call<NewResultResponse<String>> requestGetBolHistoryTotalAmount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bol/getHistoryCount")
    Call<NewResultResponse<Integer>> requestGetBolHistoryCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bol/getHistoryWithTargetList")
    Call<NewResultResponse<Bol>> requestGetBolHistoryWithTargetList(@FieldMap Map<String, String> params);

    @POST("bol/aos/chargeBol")
    Call<NewResultResponse<Object>> requestChargeBol(@Body Cash params);

    @POST("bol/use/giftBols")
    Call<NewResultResponse<Object>> requestGiftBols(@Body Bol params);

    @POST("bol/getGiftCount")
    Call<NewResultResponse<Integer>> requestGetGiftCount();

    @FormUrlEncoded
    @POST("bol/getGiftList")
    Call<NewResultResponse<BolGift>> requestGetGiftList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("bol/receiveGift")
    Call<NewResultResponse<Object>> requestReceiveGift(@FieldMap Map<String, String> params);

    @POST("bol/use/reviewReward")
    Call<NewResultResponse<Object>> requestReviewReward(@Body Bol params);

    @POST("bol/use/commentReward")
    Call<NewResultResponse<Object>> requestCommentReward(@Body Bol params);

    @POST("number/getPrefixNumber")
    Call<NewResultResponse<String>> requestGetPrefixNumber();

    @FormUrlEncoded
    @POST("number/allocateActiveVirtualNumberToPage")
    Call<NewResultResponse<Object>> requestAllocateVirtualNumberToPage(@FieldMap Map<String, String> params);

    //그룹 추가
    @POST("customer/insertGroup")
    Call<NewResultResponse<Object>> requestInsertGroup(@Body Group params);

    //그룹명 수정
    @FormUrlEncoded
    @POST("customer/updateGroupName")
    Call<NewResultResponse<Object>> requestUpdateGroupName(@FieldMap Map<String, String> params);

    //그룹 정렬
    @POST("customer/updateGroupPriorityAll")
    Call<NewResultResponse<Object>> requestUpdateGroupPriorityAll(@Body ParamsGroupPriority params);

    //그룹명 삭제
    @FormUrlEncoded
    @POST("customer/deleteGroup")
    Call<NewResultResponse<Object>> requestDeleteGroup(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("customer/getGroupAll")
    Call<NewResultResponse<Group>> requestGetGroupAll(@FieldMap Map<String, String> params);

    //고객 정보 조회
    @FormUrlEncoded
    @POST("customer/getCustomerByMobile")
    Call<NewResultResponse<Customer>> requestGetCustomerByMobile(@FieldMap Map<String, String> params);

    //고객리스트 추가
    @POST("customer/insertCustomerList")
    Call<NewResultResponse<Customer>> requestInsertCustomerList(@Body ParamsCustomerList params);

    //고객 추가
    @POST("customer/insertCustomer")
    Call<NewResultResponse<Customer>> requestInsertCustomer(@Body Customer params);

    //고객 수정
    @POST("customer/updateCustomer")
    Call<NewResultResponse<Customer>> requestUpdateCustomer(@Body Customer params);

    @FormUrlEncoded
    @POST("customer/getCustomerList")
    Call<NewResultResponse<Customer>> requestGetCustomerList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("customer/getCustomerCount")
    Call<NewResultResponse<Integer>> requestGetCustomerCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("customer/getUserCustomerList")
    Call<NewResultResponse<Customer>> requestGetUserCustomerList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("customer/getUserCustomerCount")
    Call<NewResultResponse<Integer>> requestGetUserCustomerCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("customer/getExcludeCustomerList")
    Call<NewResultResponse<Customer>> requestGetExcludeCustomerList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("customer/getExcludeCustomerCount")
    Call<NewResultResponse<Integer>> requestGetExcludeCustomerCount(@FieldMap Map<String, String> params);

    //그룸원 리스트 추가
    @POST("customer/addCustomerListToGroup")
    Call<NewResultResponse<Object>> requestAddCustomerListToGroup(@Body ParamsCustomerGroup params);

    //그룸원 리스트 삭제
    @POST("customer/removeCustomerListFromGroup")
    Call<NewResultResponse<Object>> requestRemoveCustomerListFromGroup(@Body ParamsCustomerGroup params);

    //그룹명 삭제
    @FormUrlEncoded
    @POST("customer/deleteCustomer")
    Call<NewResultResponse<Object>> requestDeleteCustomer(@FieldMap Map<String, String> params);

    //트랜젝션id로
    @FormUrlEncoded
    @POST("payment/getApprovalByOrderKey")
    Call<NewResultResponse<Payment>> requestGetApprovalByOrderKey(@Field("orderKey") String orderKey);

    //그룹 추가
    @POST("fan/insertGroup")
    Call<NewResultResponse<Object>> requestInsertFanGroup(@Body Group params);

    //그룹명 수정
    @FormUrlEncoded
    @POST("fan/updateGroupName")
    Call<NewResultResponse<Object>> requestUpdateFanGroupName(@FieldMap Map<String, String> params);

    //그룹 정렬
    @POST("fan/updateGroupPriorityAll")
    Call<NewResultResponse<Object>> requestUpdateFanGroupPriorityAll(@Body ParamsGroupPriority params);

    //그룹명 삭제
    @FormUrlEncoded
    @POST("fan/deleteGroup")
    Call<NewResultResponse<Object>> requestDeleteFanGroup(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("fan/getGroupAll")
    Call<NewResultResponse<Group>> requestGetFanGroupAll(@FieldMap Map<String, String> params);

    //그룸원 리스트 추가
    @POST("fan/addListToGroup")
    Call<NewResultResponse<Object>> requestAddFanListToGroup(@Body ParamsFanGroup params);

    //그룸원 리스트 삭제
    @POST("fan/removeListFromGroup")
    Call<NewResultResponse<Object>> requestRemoveFanListFromGroup(@Body ParamsFanGroup params);

    @FormUrlEncoded
    @POST("fan/getList")
    Call<NewResultResponse<Fan>> requestGetFanList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("fan/getCount")
    Call<NewResultResponse<Integer>> requestGetFanCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("fan/getExcludeCount")
    Call<NewResultResponse<Integer>> requestGetExcludeFanCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("fan/getExcludeList")
    Call<NewResultResponse<Fan>> requestGetExcludeFanList(@FieldMap Map<String, String> params);

    //문자 보내기
    @POST("msg/insertSmsMsg")
    Call<NewResultResponse<Msg>> requestInsertSmsMsg(@Body Msg params);

    //문자 보내기
    @POST("msg/insertPushMsg")
    Call<NewResultResponse<Msg>> requestInsertPushMsg(@Body Msg params);

    //문자 보내기
    @POST("msg/insertSavedMsg")
    Call<NewResultResponse<SavedMsg>> requestInsertSavedMsg(@Body SavedMsg params);

    @FormUrlEncoded
    @POST("msg/deleteSavedMsg")
    Call<NewResultResponse<Object>> requestDeleteSavedMsg(@FieldMap Map<String, String> params);

    @POST("msg/getSavedMsgCount")
    Call<NewResultResponse<Integer>> requestGetSavedMsgCount();

    //문자 보내기
    @FormUrlEncoded
    @POST("msg/getSavedMsgList")
    Call<NewResultResponse<SavedMsg>> requestGetSavedMsgList(@FieldMap Map<String, String> params);

    //예약 메세지 리스
    @FormUrlEncoded
    @POST("msg/getReservedMsgAll")
    Call<NewResultResponse<Msg>> requestgGetReservedMsgAll(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/getMsgCount")
    Call<NewResultResponse<Integer>> requestGetMsgCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/getMsgList")
    Call<NewResultResponse<Msg>> requestGetMsgList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/readComplete")
    Call<NewResultResponse<Object>> requestReadComplete(@FieldMap Map<String, String> params);

    @POST("msg/getMsgCountInBox")
    Call<NewResultResponse<Integer>> requestGetMsgCountInBox();

    @FormUrlEncoded
    @POST("msg/getMsgListInBox")
    Call<NewResultResponse<Msg>> requestGetMsgListInBox(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/deleteMsgInBox")
    Call<NewResultResponse<Object>> requestDeleteMsgInBox(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/getPushTargetCount")
    Call<NewResultResponse<Integer>> requestGetPushTargetCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/getPushTargetList")
    Call<NewResultResponse<Target>> requestGetPushTargetList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/getSmsTargetCount")
    Call<NewResultResponse<Integer>> requestGetSmsTargetCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/getSmsTargetList")
    Call<NewResultResponse<Target>> requestGetSmsTargetList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/sendNow")
    Call<NewResultResponse<Object>> requestSendNow(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("msg/cancelSend")
    Call<NewResultResponse<Object>> requestCancelSend(@FieldMap Map<String, String> params);

    //연락처 업데이트
    @POST("contact/updateList")
    Call<NewResultResponse<Object>> requestUpdateContactList(@Body ParamsContact params);

    //연락처 삭제
    @POST("contact/deleteList")
    Call<NewResultResponse<Object>> requestDeleteContactList(@Body ParamsContact params);

    //친구 리스트 카운트
    @POST("contact/getFriendCount")
    Call<NewResultResponse<Integer>> requestGetFriendCount();

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getFriendList")
    Call<NewResultResponse<Friend>> requestGetFriendList(@FieldMap Map<String, String> params);

    //친구 리스트 카운트
    @FormUrlEncoded
    @POST("contact/getFriendPageCount")
    Call<NewResultResponse<Integer>> requestGetFriendPageCount(@FieldMap Map<String, String> params);

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getFriendPageList")
    Call<NewResultResponse<Page>> requestGetFriendPageList(@FieldMap Map<String, String> params);

    @POST("cooperation/getGroupAll")
    Call<NewResultResponse<FranchiseGroup>> requestGetFranchiseGroupAll();

    @FormUrlEncoded
    @POST("cooperation/getCount")
    Call<NewResultResponse<Integer>> requestGetFranchiseCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cooperation/getList")
    Call<NewResultResponse<Franchise>> requestGetFranchiseList(@FieldMap Map<String, String> params);

    //추가동영상 리스트
    @FormUrlEncoded
    @POST("page/getIntroMovieAll")
    Call<NewResultResponse<ImgUrl>> requestGetIntroMovieAll(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("page/updateMainMovie")
    Call<NewResultResponse<Object>> requestUpdateMainMovie(@FieldMap Map<String, String> params);

    //추가동영상 등록
    @POST("page/updateIntroMovieList")
    Call<NewResultResponse<Page>> requestUpdateIntroMovieList(@Body ParamsIntroMovie params);

    @FormUrlEncoded
    @POST("user/getExistsNicknameUserCount")
    Call<NewResultResponse<Integer>> requestGetExistsNicknameUserCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/getExistsNicknameUserList")
    Call<NewResultResponse<User>> requestGetExistsNicknameUserList(@FieldMap Map<String, String> params);

    @POST("mobilegift/getCategoryAll")
    Call<NewResultResponse<MobileGiftCategory>> requestGetMobileGiftCategoryAll();

    @FormUrlEncoded
    @POST("mobilegift/getCount")
    Call<NewResultResponse<Integer>> requestGetMobileGiftCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("mobilegift/getList")
    Call<NewResultResponse<MobileGift>> requestGetMobileGiftList(@FieldMap Map<String, String> params);

    @POST("mobilegift/prepareOrder")
    Call<NewResultResponse<No>> requestPrepareOrder(@Body MobileGiftPurchase params);

    @FormUrlEncoded
    @POST("mobilegift/completeOrder")
    Call<NewResultResponse<Object>> requestCompleteOrder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("mobilegift/cancelPrepareOrder")
    Call<NewResultResponse<Object>> requestCancelPrepareOrder(@FieldMap Map<String, String> params);

    @POST("mobilegift/getPurchaseCount")
    Call<NewResultResponse<Integer>> requestGetMobileGiftPurchaseCount();

    @FormUrlEncoded
    @POST("mobilegift/getPurchaseList")
    Call<NewResultResponse<MobileGiftHistory>> requestGetMobileGiftPurchaseList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("mobilegift/getPurchaseGiftCount")
    Call<NewResultResponse<Integer>> requestGetPurchaseGiftCount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("mobilegift/getPurchaseWithTargetAll")
    Call<NewResultResponse<MobileGiftHistory>> requestGetMobileGiftPurchaseWithTargetAll(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("mobilegift/getStatus")
    Call<NewResultResponse<MobileGiftHistory>> requestGetMobileGiftStatus(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("mobilegift/resend")
    Call<NewResultResponse<Object>> requestMobileGiftResend(@FieldMap Map<String, String> params);

    //상품api
    @PUT("page/mainGoods")
    Call<NewResultResponse<Object>> requestPutMainGoods(@QueryMap Map<String, String> params);

    @POST("goods")
    Call<NewResultResponse<Object>> requestPostGoods(@Body Goods params);

    @PUT("goods")
    Call<NewResultResponse<Object>> requestPutGoods(@Body Goods params);

    @GET("goods/detail")
    Call<NewResultResponse<SubResultResponse<Goods>>> requestGetGoods(@QueryMap Map<String, String> params);

    @GET("goods/detail")
    Call<NewResultResponse<Goods>> requestGetOneGoods(@QueryMap Map<String, String> params);

    @DELETE("goods")
    Call<NewResultResponse<Object>> requestDeleteGoods(@QueryMap Map<String, String> params);

    @PUT("goods/status")
    Call<NewResultResponse<Object>> requestPutGoodsStatus(@QueryMap Map<String, String> params);

    @GET("goodsReview/detail")
    Call<NewResultResponse<SubResultResponse<GoodsReview>>> requestGetGoodsReview(@QueryMap Map<String, String> params);

    @GET("page/eval")
    Call<NewResultResponse<PageEval>> requestGetPageEval(@QueryMap Map<String, String> params);

    @GET("buyGoods/detail")
    Call<NewResultResponse<SubResultResponse<BuyGoods>>> requestGetBuyGoods(@QueryMap Map<String, String> params);

    @GET("buyGoods/count")
    Call<NewResultResponse<Count>> requestGetBuyGoodsCount(@QueryMap Map<String, String> params);

    @GET("buyGoods/price")
    Call<NewResultResponse<Price>> requestGetBuyGoodsPrice(@QueryMap Map<String, String> params);

    @GET("buyGoods/detail")
    Call<NewResultResponse<BuyGoods>> requestGetOneBuyGoods(@QueryMap Map<String, String> params);

    @POST("pageSeller")
    Call<NewResultResponse<Object>> requestPostPageSeller(@Body PageSeller params);

    @GET("buy/orderId")
    Call<NewResultResponse<String>> requestGetBuyOrderId(@QueryMap Map<String, String> params);

    @GET("buy/detail")
    Call<NewResultResponse<Buy>> requestGetOneBuyDetail(@QueryMap Map<String, String> params);

    @GET("buy/detail")
    Call<NewResultResponse<SubResultResponse<Buy>>> requestGetBuy(@QueryMap Map<String, String> params);

    //구매 pai
    @POST("buy/cash")
    Call<NewResultResponse<Buy>> requestPostBuyCash(@Body Buy goods);

    @GET("buy")
    Call<NewResultResponse<Buy>> requestGetOneBuy(@QueryMap Map<String, String> params);

    @GET("pageGoodsCategory")
    Call<NewResultResponse<PageGoodsCategory>> requestGetPageGoodsCategory(@QueryMap Map<String, String> params);

    @DELETE("pageGoodsCategory")
    Call<NewResultResponse<PageGoodsCategory>> requestDeletePageGoodsCategory(@QueryMap Map<String, String> params);

    @POST("pageGoodsCategory")
    Call<NewResultResponse<PageGoodsCategory>> requestPostPageGoodsCategory(@Body PageGoodsCategory params);

    @PUT("pageGoodsCategory")
    Call<NewResultResponse<PageGoodsCategory>> requestPutPageGoodsCategory(@Body PageGoodsCategory params);

    @GET("buy/count/orderProcess")
    Call<NewResultResponse<OrderCount>> requestGetCountOrderProcess(@QueryMap Map<String, String> params);

    @GET("buy/price/goodsType")
    Call<NewResultResponse<BuyGoodsTypePrice>> requestGetPriceGoodsType(@QueryMap Map<String, String> params);

    @GET("delivery/total")
    Call<NewResultResponse<DeliveryTotalPrice>> requestGetDeliveryTotalPrice(@QueryMap Map<String, String> params);

    @GET("delivery/company/total")
    Call<NewResultResponse<DeliveryTotalPrice>> requestGetDeliveryCompanyTotalPrice(@QueryMap Map<String, String> params);

    @GET("buy/price")
    Call<NewResultResponse<Float>> requestGetBuyPrice(@QueryMap Map<String, String> params);

    @GET("buy/count/orderType")
    Call<NewResultResponse<OrderTypeCount>> requestGetBuyCountOrderType(@QueryMap Map<String, String> params);

    @GET("buy/totalCount")
    Call<NewResultResponse<Count>> requestGetBuyCount(@QueryMap Map<String, String> params);

    @PUT("buy/orderProcess")
    Call<NewResultResponse<Object>> requestUpdateOrderProcess(@QueryMap Map<String, String> params);

    @GET("delivery/detail")
    Call<NewResultResponse<SubResultResponse<Delivery>>> requestGetDelivery(@QueryMap Map<String, String> params);

    @GET("page/management")
    Call<NewResultResponse<PageManagement>> requestGetPageManagement(@QueryMap Map<String, String> params);

    @POST("page/management")
    Call<NewResultResponse<PageManagement>> requestPostPageManagement(@Body PageManagement params);

    @PUT("goods/news")
    Call<NewResultResponse<Object>> requestGoodsNews(@QueryMap Map<String, String> params);

    @GET("page/hashtag/list")
    Call<NewResultResponse<HashTag>> requestGetHashTagList();

    @GET("page/hashtag/search")
    Call<NewResultResponse<String>> requestGetHashTagSearch(@QueryMap Map<String, String> params);

    @GET("page/goodsInfo")
    Call<NewResultResponse<GoodsNoticeInfo>> requestGetPageGoodsInfo(@QueryMap Map<String, String> params);

    @POST("page/goodsInfo")
    Call<NewResultResponse<Object>> requestPostPageGoodsInfo(@Body GoodsNoticeInfo params);

    @PUT("page/goodsInfo")
    Call<NewResultResponse<Object>> requestPutPageGoodsInfo(@Body GoodsNoticeInfo params);

    @GET("page/goodsInfo/category")
    Call<NewResultResponse<String>> requestGetGoodsItemList();

    @GET("page/goodsInfo/category")
    Call<NewResultResponse<NoticeInfo>> requestGetNoticeInfoListByItem(@QueryMap Map<String, String> params);

    @PUT("user/useStatusWithAgreeTerms")
    Call<NewResultResponse<String>> requestPutUserStatus(@Body User params);

    @GET("user/check")
    Call<NewResultResponse<String>> requestUserCheck(@QueryMap Map<String, String> params);

    @GET("buy/orderId/lpng")
    Call<NewResultResponse<String>> requestGetLpngOrderId();

    @POST("buybiz")
    Call<NewResultResponse<Buy>> requestPostBuyBiz(@Body Buy goods);

    @POST("buy/lpng/lpngtag")
    Call<NewResultResponse<LpngRes>> requestPostBuyLpngTag(@Body Lpng params);

    @DELETE("buy/lpng/canceltag")
    Call<NewResultResponse<Object>> requestDeleteCancelLpngTag(@QueryMap Map<String, String> params);

    @POST("buy/lpng/check")
    Call<NewResultResponse<LpngRes>> requestPostBuyLpngCheck(@Body Lpng params);

    @PUT("buyGoods/cancel/list")
    Call<NewResultResponse<Object>> requestBuyGoodsListCancel(@QueryMap Map<String, String> params);

    @PUT("goodsReview/reply")
    Call<NewResultResponse<GoodsReview>> requestPutGoodsReviewReply(@Body GoodsReview params);

    @FormUrlEncoded
    @POST("page/checkAuthCode")
    Call<NewResultResponse<Integer>> requestCheckAuthCode(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("page/checkAndUpdateAuthCode")
    Call<NewResultResponse<Integer>> requestCheckAndUpdateAuthCode(@FieldMap Map<String, String> params);

    @GET("goodsLike/count")
    Call<NewResultResponse<Count>> requestGetGoodsLikeCount(@QueryMap Map<String, String> params);
}
