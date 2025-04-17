package com.lejel.wowbox.core.network.apis

import com.lejel.wowbox.core.network.model.dto.AirDrop
import com.lejel.wowbox.core.network.model.dto.App
import com.lejel.wowbox.core.network.model.dto.Bank
import com.lejel.wowbox.core.network.model.dto.Banner
import com.lejel.wowbox.core.network.model.dto.Benefit
import com.lejel.wowbox.core.network.model.dto.BenefitCalculate
import com.lejel.wowbox.core.network.model.dto.BuffInviteMining
import com.lejel.wowbox.core.network.model.dto.CommunityApply
import com.lejel.wowbox.core.network.model.dto.Config
import com.lejel.wowbox.core.network.model.dto.Count
import com.lejel.wowbox.core.network.model.dto.Device
import com.lejel.wowbox.core.network.model.dto.EmsCountry
import com.lejel.wowbox.core.network.model.dto.Event
import com.lejel.wowbox.core.network.model.dto.EventGift
import com.lejel.wowbox.core.network.model.dto.EventExist
import com.lejel.wowbox.core.network.model.dto.EventJoinParam
import com.lejel.wowbox.core.network.model.dto.EventJoinResult
import com.lejel.wowbox.core.network.model.dto.EventReply
import com.lejel.wowbox.core.network.model.dto.EventWin
import com.lejel.wowbox.core.network.model.dto.Faq
import com.lejel.wowbox.core.network.model.dto.FaqCategory
import com.lejel.wowbox.core.network.model.dto.GiftCard
import com.lejel.wowbox.core.network.model.dto.GiftCardBrand
import com.lejel.wowbox.core.network.model.dto.GiftCardPurchase
import com.lejel.wowbox.core.network.model.dto.HistoryBall
import com.lejel.wowbox.core.network.model.dto.HistoryBenefit
import com.lejel.wowbox.core.network.model.dto.HistoryCash
import com.lejel.wowbox.core.network.model.dto.HistoryCommission
import com.lejel.wowbox.core.network.model.dto.HistoryPoint
import com.lejel.wowbox.core.network.model.dto.Inquire
import com.lejel.wowbox.core.network.model.dto.Kabkota
import com.lejel.wowbox.core.network.model.dto.Kecamatan
import com.lejel.wowbox.core.network.model.dto.KodePos
import com.lejel.wowbox.core.network.model.dto.Kyc
import com.lejel.wowbox.core.network.model.dto.Lottery
import com.lejel.wowbox.core.network.model.dto.LotteryJoin
import com.lejel.wowbox.core.network.model.dto.LotteryWin
import com.lejel.wowbox.core.network.model.dto.LotteryWinCondition
import com.lejel.wowbox.core.network.model.dto.LuckyBox
import com.lejel.wowbox.core.network.model.dto.LuckyBoxDeliveryPurchase
import com.lejel.wowbox.core.network.model.dto.LuckyBoxProductGroupItem
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchase
import com.lejel.wowbox.core.network.model.dto.LuckyBoxPurchaseItem
import com.lejel.wowbox.core.network.model.dto.LuckyBoxReply
import com.lejel.wowbox.core.network.model.dto.LuckyDraw
import com.lejel.wowbox.core.network.model.dto.LuckyDrawGift
import com.lejel.wowbox.core.network.model.dto.LuckyDrawNumber
import com.lejel.wowbox.core.network.model.dto.LuckyDrawPurchase
import com.lejel.wowbox.core.network.model.dto.LuckyDrawTheme
import com.lejel.wowbox.core.network.model.dto.LuckyDrawWin
import com.lejel.wowbox.core.network.model.dto.LuckyDrawWinReply
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.dto.MemberDelivery
import com.lejel.wowbox.core.network.model.dto.MemberProfileImage
import com.lejel.wowbox.core.network.model.dto.Nation
import com.lejel.wowbox.core.network.model.dto.Notice
import com.lejel.wowbox.core.network.model.dto.NotificationBox
import com.lejel.wowbox.core.network.model.dto.Partner
import com.lejel.wowbox.core.network.model.dto.PointMallCategory
import com.lejel.wowbox.core.network.model.dto.Popup
import com.lejel.wowbox.core.network.model.dto.Product
import com.lejel.wowbox.core.network.model.dto.ProductOptionTotal
import com.lejel.wowbox.core.network.model.dto.ProfitPartner
import com.lejel.wowbox.core.network.model.dto.Provinsi
import com.lejel.wowbox.core.network.model.dto.Terms
import com.lejel.wowbox.core.network.model.dto.WalletRes
import com.lejel.wowbox.core.network.model.dto.Withdraw
import com.lejel.wowbox.core.network.model.response.ListResultResponse
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by 김종경 on 2016-10-06.
 */
interface IApi {

    @FormUrlEncoded
    @POST("member/loginByPlatform")
    fun loginByPlatform(@FieldMap params: Map<String, String>): Call<NewResultResponse<Member>>

    @FormUrlEncoded
    @POST("member/loginByMobile")
    fun loginByMobile(@FieldMap params: Map<String, String>): Call<NewResultResponse<Member>>

    @FormUrlEncoded
    @POST("member/login")
    fun login(@FieldMap params: Map<String, String>): Call<NewResultResponse<Member>>

    //회원가입
    @POST("member/join")
    fun join(@Body params: Member): Call<NewResultResponse<Member>>

    @POST("member/wowMallJoin")
    fun wowMallJoin(): Call<NewResultResponse<Any>>

    @POST("member/update")
    fun updateMember(@Body params: Member): Call<NewResultResponse<Member>>

    @FormUrlEncoded
    @POST("member/updateMobileNumber")
    fun updateMobileNumber(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/updateVerifiedMobileNumber")
    fun updateVerifiedMobileNumber(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

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
    fun lotteryJoin(@FieldMap params: Map<String, String>): Call<NewResultResponse<LotteryJoin>>

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

    @GET("history/cash/list")
    fun getHistoryCashList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<HistoryCash>>>

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
    @POST("member/withdrawalWithVerify")
    fun withdrawal(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/withdrawalCancelWithVerify")
    fun withdrawalCancelWithVerify(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/withdrawalCancel")
    fun withdrawalCancel(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/authWalletEmail")
    fun authWalletEmail(@FieldMap params: Map<String, String>): Call<NewResultResponse<Boolean>>

    @FormUrlEncoded
    @POST("member/updateMarketingReceiving")
    fun updateMarketingReceiving(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("/luckyDrawTheme/list")
    fun getLuckyDrawThemeList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyDrawTheme>>>

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

    @GET("memberDelivery")
    fun getMemberDelivery(): Call<NewResultResponse<MemberDelivery>>

    @GET("nation")
    fun getNation(@QueryMap params: Map<String, String>): Call<NewResultResponse<Nation>>

    @GET("nation/list")
    fun getNationList(): Call<NewResultResponse<ListResultResponse<Nation>>>

    @GET("emsCountry/list")
    fun getEmsCountryList(): Call<NewResultResponse<ListResultResponse<EmsCountry>>>

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

    @POST("member/makeInviteUrl")
    fun makeInviteUrl(): Call<NewResultResponse<String>>

    @POST("member/attendance")
    fun attendance(): Call<NewResultResponse<Float>>

    @GET("benefit")
    fun getBenefit(): Call<NewResultResponse<Benefit>>

    @GET("benefitCalculate/list")
    fun getBenefitCalculateList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<BenefitCalculate>>>

    @GET("history/benefit/list")
    fun getHistoryBenefitList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<HistoryBenefit>>>

    @GET("history/getTotalBenefit")
    fun getTotalBenefit(@QueryMap params: Map<String, String>): Call<NewResultResponse<Double>>

    @GET("luckyDraw/checkPrivate/{seqNo}")
    fun checkLuckyDrawPrivate(@Path("seqNo") seqNo: Long, @QueryMap params: Map<String, String>): Call<NewResultResponse<Boolean>>

    @GET("config/{code}")
    fun getConfig(@Path("code") code: String): Call<NewResultResponse<Config>>

    @GET("buffInviteMining/list")
    fun getBuffInviteMiningList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<BuffInviteMining>>>

    @GET("buffInviteMining/getCount")
    fun getBuffInviteMiningCount(): Call<NewResultResponse<Int>>

    @GET("buffInviteMining/getSumCoin")
    fun getBuffInviteMiningTotalCoin(): Call<NewResultResponse<Double>>

    @GET("airDrop")
    fun getAirDrop(): Call<NewResultResponse<AirDrop>>

    @FormUrlEncoded
    @POST("airDrop/apply")
    fun airDropApply(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("reward")
    fun reward(@FieldMap params: Map<String, String>): Call<NewResultResponse<Float>>

    @GET("luckybox")
    fun getLuckyBoxList(): Call<NewResultResponse<ListResultResponse<LuckyBox>>>

    @GET("luckybox/{seqNo}")
    fun getLuckyBox(@Path("seqNo") seqNo: Long): Call<NewResultResponse<LuckyBox>>

    @GET("luckyboxProductGroupItem")
    fun getLuckyBoxProductList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyBoxProductGroupItem>>>

    @GET("luckyboxPurchaseItem/getCountNotOpen")
    fun getCountNotOpenLuckyBoxPurchaseItem(): Call<NewResultResponse<Count>>

    @POST("luckyboxPurchase")
    fun saveLuckyBoxPurchase(@Body params: LuckyBoxPurchase): Call<NewResultResponse<LuckyBoxPurchase>>

    @GET("luckyboxPurchase")
    fun getNotOpenLuckyBoxPurchaseList(): Call<NewResultResponse<ListResultResponse<LuckyBoxPurchase>>>

    @GET("luckyboxPurchaseItem/notOpenList")
    fun getNotOpenLuckyBoxPurchaseItemListByLuckyBoxPurchaseSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>

    @FormUrlEncoded
    @POST("luckyboxPurchaseItem")
    fun openLuckyBoxPurchaseItem(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @DELETE("luckyboxPurchase/{seqNo}")
    fun cancelLuckyBox(@Path("seqNo") seqNo: Long): Call<NewResultResponse<Any>>

    @GET("luckyboxPurchaseItem/confirm/{seqNo}")
    fun confirmLuckyBoxPurchaseItem(@Path("seqNo") seqNo: Long): Call<NewResultResponse<LuckyBoxPurchaseItem>>

    @GET("luckyboxPurchaseItem/total")
    fun getTotalLuckyBoxPurchaseItemList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>

    @GET("luckyboxPurchaseItem/{seqNo}")
    fun getLuckyBoxPurchaseItem(@Path("seqNo") seqNo: Long): Call<NewResultResponse<LuckyBoxPurchaseItem>>

    @FormUrlEncoded
    @POST("luckyboxPurchaseItem/updateImpression")
    fun updateLuckyBoxImpression(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("luckyboxReply")
    fun insertLuckyBoxReply(@Body params: LuckyBoxReply): Call<NewResultResponse<Any>>

    @PUT("luckyboxReply/{seqNo}")
    fun updateLuckyBoxReply(@Path("seqNo") seqNo: Long, @Body params: LuckyBoxReply): Call<NewResultResponse<Any>>

    @DELETE("luckyboxReply/{seqNo}")
    fun deleteLuckyBoxReply(@Path("seqNo") seqNo: Long): Call<NewResultResponse<Any>>

    @GET("luckyboxReply")
    fun getLuckyBoxReplyList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyBoxReply>>>

    @GET("luckyboxPurchaseItem/my")
    fun getOpenLuckyBoxPurchaseItemList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<LuckyBoxPurchaseItem>>>

    @FormUrlEncoded
    @POST("luckyboxPurchaseItem/cashBack/{seqNo}")
    fun cashBackLuckyBoxPurchaseItem(@Path("seqNo") seqNo: Long, @FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("luckyboxDeliveryPurchase")
    fun saveLuckyBoxDeliveryPurchase(@Body params: LuckyBoxDeliveryPurchase): Call<NewResultResponse<LuckyBoxDeliveryPurchase>>

    @GET("product/{seqNo}")
    fun getProduct(@Path("seqNo") seqNo: Long): Call<NewResultResponse<Product>>

    @GET("productOption")
    fun getProductOption(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductOptionTotal>>

    @POST("withdraw")
    fun withdraw(@Body params: Withdraw): Call<NewResultResponse<Withdraw>>

    @GET("withdraw/list")
    fun getWithdrawList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Withdraw>>>

    @GET("pointMallCategory/list")
    fun getPointMallCategoryList(): Call<NewResultResponse<ListResultResponse<PointMallCategory>>>

    @POST("pointMallCategory/login")
    fun pointMallLogin(): Call<NewResultResponse<String>>

    @GET("provinsi/list")
    fun getProvinsiList(): Call<NewResultResponse<ListResultResponse<Provinsi>>>

    @GET("kabkota/list")
    fun getKabkotaList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Kabkota>>>

    @GET("kecamatan/list")
    fun getKecamatanList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Kecamatan>>>

    @GET("kodePos/list")
    fun getKodePosList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<KodePos>>>

    @GET("bank/list")
    fun getBankList(): Call<NewResultResponse<ListResultResponse<Bank>>>

    @FormUrlEncoded
    @POST("pointMall/exchange")
    fun pointMallExchange(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("event")
    fun getEventList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Event>>>

    @GET("event/{seqNo}")
    fun getEvent(@Path("seqNo") seqNo: Long): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("eventGift/all")
    fun getEventGiftAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<EventGift>>>

    @FormUrlEncoded
    @POST("event/writeImpression")
    fun writeImpression(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("eventwin/existsResult")
    fun existsEventResult(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventExist>>

    @FormUrlEncoded
    @POST("eventwin/getWinAll")
    fun getWinAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<EventWin>>>

    @GET("eventwin")
    fun getEventWinList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<EventWin>>>

    @POST("event/join")
    fun joinEvent(@Body params: EventJoinParam): Call<NewResultResponse<EventJoinResult>>

    @FormUrlEncoded
    @POST("eventwin/getWinListOnlyPresent")
    fun getWinListOnlyPresent(@FieldMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<EventWin>>>

    @FormUrlEncoded
    @POST("eventwin/getMyWinListOnlyPresent")
    fun getMyWinListOnlyPresent(@FieldMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<EventWin>>>

    @FormUrlEncoded
    @POST("eventwin/getWinBySeqNo")
    fun getWinBySeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @GET("eventReply")
    fun getEventReplyList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<EventReply>>>

    @POST("eventReply")
    fun insertEventReply(@Body params: EventReply): Call<NewResultResponse<Any>>

    @PUT("eventReply/{seqNo}")
    fun updateEventReply(@Path("seqNo") seqNo: Long, @Body params: EventReply): Call<NewResultResponse<Any>>

    @DELETE("eventReply/{seqNo}")
    fun deleteEventReply(@Path("seqNo") seqNo: Long): Call<NewResultResponse<Any>>

    @GET("eventwin/getUserWinList")
    fun getUserWinList(@QueryMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<EventWin>>>

    @POST("eventwin/getNewWinCountByUser")
    fun getNewWinCountByUser(): Call<NewResultResponse<Count>>

    @FormUrlEncoded
    @POST("event/getWinList")
    fun getWinAnnounceList(@FieldMap params: Map<String, String>): Call<NewResultResponse<ListResultResponse<Event>>>

    @FormUrlEncoded
    @POST("member/checkJoinedMobile")
    fun checkJoinedMobile(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/checkJoinedMobileNumber")
    fun checkJoinedMobileNumber(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/sendWhatsapp")
    fun sendWhatsapp(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/verifyWhatsapp")
    fun verifyWhatsapp(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/sendSms")
    fun sendSms(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/verifySms")
    fun verifySms(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/inputRecommender")
    fun inputRecommender(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>
}
