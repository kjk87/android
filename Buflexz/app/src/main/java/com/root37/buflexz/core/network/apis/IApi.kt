package com.root37.buflexz.core.network.apis

import com.root37.buflexz.core.network.model.dto.*
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.model.response.NewResultResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by 김종경 on 2016-10-06.
 */
interface IApi {

    @FormUrlEncoded
    @POST("member/loginByPlatform")
    fun loginByPlatform(@FieldMap params: Map<String, String>): Call<NewResultResponse<Member>>

    @FormUrlEncoded
    @POST("member/login")
    fun login(@FieldMap params: Map<String, String>): Call<NewResultResponse<Member>>

    //회원가입
    @POST("member/join")
    fun join(@Body params: Member): Call<NewResultResponse<Member>>

    @POST("member/update")
    fun updateMember(@Body params: Member): Call<NewResultResponse<Member>>

    @FormUrlEncoded
    @POST("member/updateProfile")
    fun updateProfile(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("member/checkNickname")
    fun checkNickname(@QueryMap params: Map<String, String>): Call<NewResultResponse<Boolean>>

    @POST("member/getSession")
    fun getSession(): Call<NewResultResponse<Member>>

    @POST("member/reloadSession")
    fun reloadSession(): Call<NewResultResponse<Member>>

    @FormUrlEncoded
    @POST("member/refreshToken")
    fun refreshToken(@FieldMap params: Map<String, String>): Call<NewResultResponse<String>>

    @FormUrlEncoded
    @POST("member/exchangePointToBall")
    fun exchangePointToBall(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("terms")
    fun getTermsList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Terms>>>

    @GET("terms/{seqNo}")
    fun getTerms(@Path("seqNo") seqNo: Long): Call<NewResultResponse<Terms>>

    @GET("terms/notSignedList")
    fun getNotSignedList(): Call<NewResultResponse<ListResultResponse<Terms>>>

    @FormUrlEncoded
    @POST("terms/agreeAddTerms")
    fun agreeAddTerms(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("device")
    fun postDevice(@Body device: Device): Call<NewResultResponse<Device>>

    @GET("device")
    fun getDevice(@QueryMap params: Map<String, String>): Call<NewResultResponse<Device>>

    @GET("app")
    fun getApp(@QueryMap params: Map<String, String>): Call<NewResultResponse<App>>

    @GET("lottery")
    fun getLottery(): Call<NewResultResponse<Lottery>>

    @GET("lottery/{lotteryRound}")
    fun getLotteryByLotteryRound(@Path(value = "lotteryRound") lotteryRound: Int): Call<NewResultResponse<Lottery>>

    @GET("/lottery/roundList")
    fun getLotteryRoundList(): Call<NewResultResponse<ListResultResponse<Lottery>>>

    @FormUrlEncoded
    @POST("lotteryJoin")
    fun lotteryJoin(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("lotteryJoin")
    fun getLotteryJoinList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LotteryJoin>>>

    @GET("lotteryJoin/count")
    fun getLotteryJoinCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("lotteryWin/{lotteryRound}")
    fun getMyLotteryWinList(@Path(value = "lotteryRound") lotteryRound: Int, @QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LotteryWin>>>

    @GET("lotteryWin/myCount/{lotteryRound}")
    fun getMyLotteryWinCount(@Path(value = "lotteryRound") lotteryRound: Int): Call<NewResultResponse<Int>>

    @GET("lotteryWin/all/{lotteryRound}")
    fun getTotalLotteryWinList(@Path(value = "lotteryRound") lotteryRound: Int, @QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LotteryWin>>>

    @GET("lotteryWin/count/{lotteryRound}")
    fun getLotteryWinCount(@Path(value = "lotteryRound") lotteryRound: Int): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("lotteryWin/receive/{seqNo}")
    fun receiveLotteryWin(@Path(value = "seqNo") seqNo: Long, @FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("lotteryWin/receiveAll")
    fun totalReceiveLotteryWin(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("lotteryWin/remainCount")
    fun getLotteryWinRemainCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("lotteryWinCondition/{lotteryRound}")
    fun getLotteryWinCondition(@Path(value = "lotteryRound") lotteryRound: Int): Call<NewResultResponse<LotteryWinCondition>>

    @GET("banner")
    fun getBannerList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Banner>>>

    @GET("popup")
    fun getPopupList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Popup>>>

    @GET("giftCardBrand/list")
    fun getGiftCardBrandList(): Call<NewResultResponse<ListResultResponse<GiftCardBrand>>>

    @GET("giftCard/list/{brandSeqNo}")
    fun getGiftCardList(@Path(value = "brandSeqNo") brandSeqNo: Long, @QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<GiftCard>>>

    @GET("giftCard/{seqNo}")
    fun getGiftCard(@Path(value = "seqNo") seqNo: Long): Call<NewResultResponse<GiftCard>>

    @POST("giftCardPurchase")
    fun giftCardPurchase(@Body params: GiftCardPurchase): Call<NewResultResponse<Any>>

    @GET("giftCardPurchase/{seqNo}")
    fun getGiftCardPurchase(@Path(value = "seqNo") seqNo: Long): Call<NewResultResponse<GiftCardPurchase>>

    @GET("giftCardPurchase/list")
    fun getGiftCardPurchaseList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<GiftCardPurchase>>>

    @GET("history/point/list")
    fun getHistoryPointList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<HistoryPoint>>>

    @GET("history/ball/list")
    fun getHistoryBallList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<HistoryBall>>>

    @POST("partner")
    fun postPartner(@Body params: Partner): Call<NewResultResponse<Any>>

    @POST("partner/resendEmail")
    fun partnerResendEmail(): Call<NewResultResponse<Any>>

    @GET("partner")
    fun getPartner(): Call<NewResultResponse<Partner>>

    @GET("partner/childCount")
    fun getChildPartnerCount(): Call<NewResultResponse<Int>>

    @GET("partner/childList")
    fun getChildPartnerList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Partner>>>

    @GET("profitPartner/getTotalProfit")
    fun getTotalProfit(): Call<NewResultResponse<Double>>

    @GET("profitPartner/getTotalBonusProfit")
    fun getTotalBonusProfit(): Call<NewResultResponse<Double>>

    @GET("history/getCommission")
    fun getCommission(@QueryMap params: Map<String, String>): Call<NewResultResponse<Double>>

    @GET("history/commission/list")
    fun getHistoryCommissionList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<HistoryCommission>>>

    @GET("profitPartner")
    fun getProfitPartner(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProfitPartner>>

    @GET("profitPartner/childList")
    fun getChildProfitPartnerList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<ProfitPartner>>>

    @GET("faq/category/list")
    fun getFaqCategoryList(): Call<NewResultResponse<ListResultResponse<FaqCategory>>>

    @GET("faq/list")
    fun getFaqList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Faq>>>

    @GET("faq/{seqNo}")
    fun getFaq(@Path("seqNo") seqNo: Long): Call<NewResultResponse<Faq>>

    @GET("notice/list")
    fun getNoticeList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Notice>>>

    @GET("notice/{seqNo}")
    fun getNotice(@Path("seqNo") seqNo: Long): Call<NewResultResponse<Notice>>

    @GET("/inquire/list")
    fun getInquireList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Inquire>>>

    @POST("inquire")
    fun postInquire(@Body params: Inquire): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/sendEmailForAuth")
    fun sendEmailForAuth(@FieldMap params: Map<String, String>): Call<NewResultResponse<String>>

    @FormUrlEncoded
    @POST("member/withdrawal")
    fun withdrawal(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/withdrawalCancel")
    fun withdrawalCancel(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/authWalletEmail")
    fun authWalletEmail(@FieldMap params: Map<String, String>): Call<NewResultResponse<Boolean>>

    @FormUrlEncoded
    @POST("member/updateMarketingReceiving")
    fun updateMarketingReceiving(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("/luckyDraw/list")
    fun getLuckyDrawList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyDraw>>>

    @GET("/luckyDraw/completeList")
    fun getLuckyDrawCompleteList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyDraw>>>

    @GET("luckyDraw/{seqNo}")
    fun getLuckyDraw(@Path("seqNo") seqNo: Long): Call<NewResultResponse<LuckyDraw>>

    @GET("/luckyDrawGift/list")
    fun getLuckyDrawGiftList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyDrawGift>>>

    @GET("luckyDrawNumber/remainCount")
    fun getLuckyDrawNumberRemainCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("luckyDrawNumber/selectNumber")
    fun luckyDrawSelectNumber(@FieldMap params: Map<String, String>): Call<NewResultResponse<LuckyDrawNumber>>

    @FormUrlEncoded
    @POST("luckyDrawNumber/deleteNumber")
    fun luckyDrawDeleteNumber(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("luckyDrawPurchase/getJoinCount")
    fun getLuckyDrawJoinCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @POST("luckyDrawPurchase")
    fun postLuckyDrawPurchase(@Body params: LuckyDrawPurchase): Call<NewResultResponse<LuckyDrawPurchase>>

    @GET("luckyDrawPurchase/list")
    fun getLuckyDrawPurchaseList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyDrawPurchase>>>

    @GET("luckyDrawWin/list")
    fun getLuckyDrawWinList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyDrawWin>>>

    @GET("luckyDrawWin/myWinList")
    fun getLuckyDrawMyWinList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyDrawWin>>>

    @GET("luckyDrawWin/myWinListAll")
    fun getLuckyDrawMyWinListAll(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyDrawWin>>>

    @GET("luckyDrawWinReply/list")
    fun getLuckyDrawWinReplyList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyDrawWinReply>>>

    @FormUrlEncoded
    @POST("luckyDrawWinReply")
    fun postLuckyDrawWinReply(@FieldMap params: Map<String, String>): Call<NewResultResponse<LuckyDrawWinReply>>

    @FormUrlEncoded
    @POST("luckyDrawWinReply/update")
    fun updateLuckyDrawWinReply(@FieldMap params: Map<String, String>): Call<NewResultResponse<LuckyDrawWinReply>>

    @FormUrlEncoded
    @POST("luckyDrawWinReply/delete")
    fun deleteLuckyDrawWinReply(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("luckyDrawWin/updateImpression")
    fun updateLuckyDrawWinImpression(@FieldMap params: Map<String, String>): Call<NewResultResponse<LuckyDrawWin>>

    @GET("productCategory/list")
    fun getProductCategoryList(): Call<NewResultResponse<ListResultResponse<ProductCategory>>>

    @GET("product/list")
    fun getProductList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Product>>>

    @GET("product/{seqNo}")
    fun getProduct(@Path("seqNo") seqNo: Long): Call<NewResultResponse<Product>>

    @GET("memberDelivery")
    fun getMemberDelivery(): Call<NewResultResponse<MemberDelivery>>

    @GET("nation")
    fun getNation(@QueryMap params: Map<String, String>): Call<NewResultResponse<Nation>>

    @GET("nation/list")
    fun getNationList(): Call<NewResultResponse<ListResultResponse<Nation>>>

    @POST("productPurchase")
    fun postProductPurchase(@Body params: ProductPurchase): Call<NewResultResponse<ProductPurchase>>

    @GET("emsCountry/list")
    fun getEmsCountryList(): Call<NewResultResponse<ListResultResponse<EmsCountry>>>

    @GET("productPurchase/list")
    fun getProductPurchaseList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<ProductPurchase>>>

    @GET("communityApply")
    fun getCommunityApply(): Call<NewResultResponse<CommunityApply>>

    @POST("communityApply")
    fun postCommunityApply(@Body params: CommunityApply): Call<NewResultResponse<CommunityApply>>

    @GET("memberProfileImage/list")
    fun getMemberProfileImageList(): Call<NewResultResponse<ListResultResponse<MemberProfileImage>>>

    @GET("notificationBox/list")
    fun getNotificationBoxList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<NotificationBox>>>

    @GET("notificationBox/getUnReadCount")
    fun getUnReadCount(): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("notificationBox/read")
    fun notificationBoxRead(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("notificationBox/delete")
    fun notificationBoxDelete(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("member/inviteList")
    fun getInviteList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Member>>>

    @FormUrlEncoded
    @POST("replyReport")
    fun replyReport(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("buffWallet/duplicateUser")
    fun walletDuplicateUser(@FieldMap params: Map<String, String>): Call<NewResultResponse<WalletRes>>

    @POST("buffWallet/signUp")
    fun walletSignUp(): Call<NewResultResponse<WalletRes>>

    @POST("buffWallet/coinBalance")
    fun getBuffCoinBalance(): Call<NewResultResponse<Map<String, Any>>>
}