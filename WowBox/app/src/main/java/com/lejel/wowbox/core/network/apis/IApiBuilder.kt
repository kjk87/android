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
import com.lejel.wowbox.core.network.model.dto.EventExist
import com.lejel.wowbox.core.network.model.dto.EventGift
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
import com.lejel.wowbox.core.network.request.IApiRequest

/**
 * Created by j2n on 2016. 9. 12..
 */
interface IApiBuilder {
    fun loginByPlatform(params: Map<String, String>): IApiRequest<Member>
    fun loginByMobile(params: Map<String, String>): IApiRequest<Member>
    fun login(params: Map<String, String>): IApiRequest<Member>
    fun join(params: Member): IApiRequest<Member>
    fun wowMallJoin(): IApiRequest<Any>
    fun updateMember(params: Member): IApiRequest<Member>
    fun updateMobileNumber(params: Map<String, String>): IApiRequest<Any>
    fun updateVerifiedMobileNumber(params: Map<String, String>): IApiRequest<Any>
    fun updateProfile(params: Map<String, String>): IApiRequest<Any>
    fun checkNickname(params: Map<String, String>): IApiRequest<Boolean>
    fun getSession(): IApiRequest<Member>
    fun reloadSession(): IApiRequest<Member>
    fun refreshToken(params: Map<String, String>): IApiRequest<String>
    fun exchangePointToBall(params: Map<String, String>): IApiRequest<Any>
    fun getTermsList(params: Map<String, String>): IApiRequest<ListResultResponse<Terms>>
    fun getTerms(seqNo: Long): IApiRequest<Terms>
    fun getNotSignedList(): IApiRequest<ListResultResponse<Terms>>
    fun agreeAddTerms(params: Map<String, String>): IApiRequest<Any>
    fun postDevice(params: Device): IApiRequest<Device>
    fun getDevice(params: Map<String, String>): IApiRequest<Device>
    fun getApp(params: Map<String, String>): IApiRequest<App>
    fun getLottery(): IApiRequest<Lottery>
    fun getLotteryByLotteryRound(lotteryRound: Int): IApiRequest<Lottery>
    fun getLotteryRoundList(): IApiRequest<ListResultResponse<Lottery>>
    fun lotteryJoin(params: Map<String, String>): IApiRequest<LotteryJoin>
    fun getLotteryJoinList(params: Map<String, String>): IApiRequest<ListResultResponse<LotteryJoin>>
    fun getLotteryJoinCount(params: Map<String, String>): IApiRequest<Int>
    fun getMyLotteryWinList(lotteryRound: Int, params: Map<String, String>): IApiRequest<ListResultResponse<LotteryWin>>
    fun getMyLotteryWinCount(lotteryRound: Int): IApiRequest<Int>
    fun getTotalLotteryWinList(lotteryRound: Int, params: Map<String, String>): IApiRequest<ListResultResponse<LotteryWin>>
    fun getLotteryWinCount(lotteryRound: Int): IApiRequest<Int>
    fun receiveLotteryWin(seqNo: Long, params: Map<String, String>): IApiRequest<Any>
    fun totalReceiveLotteryWin(params: Map<String, String>): IApiRequest<Any>
    fun getLotteryWinRemainCount(params: Map<String, String>): IApiRequest<Int>
    fun getLotteryWinCondition(lotteryRound: Int): IApiRequest<LotteryWinCondition>
    fun getBannerList(params: Map<String, String>): IApiRequest<ListResultResponse<Banner>>
    fun getPopupList(params: Map<String, String>): IApiRequest<ListResultResponse<Popup>>
    fun getGiftCardBrandList(): IApiRequest<ListResultResponse<GiftCardBrand>>

    fun getGiftCardList(brandSeqNo: Long, params: Map<String, String>): IApiRequest<ListResultResponse<GiftCard>>
    fun getGiftCard(seqNo: Long): IApiRequest<GiftCard>
    fun giftCardPurchase(params: GiftCardPurchase): IApiRequest<Any>
    fun getGiftCardPurchase(seqNo: Long): IApiRequest<GiftCardPurchase>
    fun getGiftCardPurchaseList(params: Map<String, String>): IApiRequest<ListResultResponse<GiftCardPurchase>>
    fun getHistoryPointList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryPoint>>
    fun getHistoryCashList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryCash>>
    fun getHistoryBallList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryBall>>
    fun postPartner(params: Partner): IApiRequest<Any>
    fun partnerResendEmail(): IApiRequest<Any>
    fun getPartner(): IApiRequest<Partner>
    fun getChildPartnerCount(): IApiRequest<Int>
    fun getChildPartnerList(params: Map<String, String>): IApiRequest<ListResultResponse<Partner>>
    fun getTotalProfit(): IApiRequest<Double>
    fun getTotalBonusProfit(): IApiRequest<Double>
    fun getCommission(params: Map<String, String>): IApiRequest<Double>
    fun getHistoryCommissionList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryCommission>>
    fun getProfitPartner(params: Map<String, String>): IApiRequest<ProfitPartner>
    fun getChildProfitPartnerList(params: Map<String, String>): IApiRequest<ListResultResponse<ProfitPartner>>
    fun getFaqCategoryList(): IApiRequest<ListResultResponse<FaqCategory>>
    fun getFaqList(params: Map<String, String>): IApiRequest<ListResultResponse<Faq>>
    fun getFaq(seqNo: Long): IApiRequest<Faq>
    fun getNoticeList(params: Map<String, String>): IApiRequest<ListResultResponse<Notice>>
    fun getNotice(seqNo: Long): IApiRequest<Notice>
    fun getInquireList(params: Map<String, String>): IApiRequest<ListResultResponse<Inquire>>
    fun postInquire(params: Inquire): IApiRequest<Any>
    fun sendEmailForAuth(params: Map<String, String>): IApiRequest<String>
    fun withdrawal(params: Map<String, String>): IApiRequest<Any>
    fun withdrawalCancelWithVerify(params: Map<String, String>): IApiRequest<Any>
    fun withdrawalCancel(params: Map<String, String>): IApiRequest<Any>
    fun authWalletEmail(params: Map<String, String>): IApiRequest<Boolean>
    fun updateMarketingReceiving(params: Map<String, String>): IApiRequest<Any>
    fun getLuckyDrawThemeList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawTheme>>
    fun getLuckyDrawList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDraw>>
    fun getLuckyDrawCompleteList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDraw>>
    fun getLuckyDraw(seqNo: Long): IApiRequest<LuckyDraw>
    fun getLuckyDrawGiftList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawGift>>
    fun getLuckyDrawNumberRemainCount(params: Map<String, String>): IApiRequest<Int>
    fun luckyDrawSelectNumber(params: Map<String, String>): IApiRequest<LuckyDrawNumber>
    fun luckyDrawDeleteNumber(params: Map<String, String>): IApiRequest<Any>
    fun getLuckyDrawJoinCount(params: Map<String, String>): IApiRequest<Int>
    fun postLuckyDrawPurchase(params: LuckyDrawPurchase): IApiRequest<LuckyDrawPurchase>
    fun getLuckyDrawPurchaseList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawPurchase>>
    fun getLuckyDrawWinList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawWin>>
    fun getLuckyDrawMyWinList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawWin>>
    fun getLuckyDrawMyWinListAll(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawWin>>
    fun getLuckyDrawWinReplyList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawWinReply>>
    fun postLuckyDrawWinReply(params: Map<String, String>): IApiRequest<LuckyDrawWinReply>
    fun updateLuckyDrawWinReply(params: Map<String, String>): IApiRequest<LuckyDrawWinReply>
    fun deleteLuckyDrawWinReply(params: Map<String, String>): IApiRequest<Any>
    fun updateLuckyDrawWinImpression(params: Map<String, String>): IApiRequest<LuckyDrawWin>
    fun getMemberDelivery(): IApiRequest<MemberDelivery>
    fun getNation(params: Map<String, String>): IApiRequest<Nation>
    fun getNationList(): IApiRequest<ListResultResponse<Nation>>
    fun getEmsCountryList(): IApiRequest<ListResultResponse<EmsCountry>>
    fun getCommunityApply(): IApiRequest<CommunityApply>
    fun postCommunityApply(params: CommunityApply): IApiRequest<CommunityApply>
    fun getMemberProfileImageList(): IApiRequest<ListResultResponse<MemberProfileImage>>
    fun getNotificationBoxList(params: Map<String, String>): IApiRequest<ListResultResponse<NotificationBox>>
    fun getUnReadCount(): IApiRequest<Int>
    fun notificationBoxRead(params: Map<String, String>): IApiRequest<Any>
    fun notificationBoxDelete(params: Map<String, String>): IApiRequest<Any>
    fun getInviteList(params: Map<String, String>): IApiRequest<ListResultResponse<Member>>
    fun replyReport(params: Map<String, String>): IApiRequest<Any>
    fun walletDuplicateUser(params: Map<String, String>): IApiRequest<WalletRes>
    fun walletSignUp(): IApiRequest<WalletRes>
    fun getBuffCoinBalance(): IApiRequest<Map<String, Any>>
    fun makeInviteUrl(): IApiRequest<String>
    fun attendance(): IApiRequest<Float>
    fun getBenefit(): IApiRequest<Benefit>
    fun getBenefitCalculateList(params: Map<String, String>): IApiRequest<ListResultResponse<BenefitCalculate>>
    fun getHistoryBenefitList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryBenefit>>
    fun getTotalBenefit(params: Map<String, String>): IApiRequest<Double>
    fun checkLuckyDrawPrivate(seqNo: Long, params: Map<String, String>): IApiRequest<Boolean>
    fun getConfig(code:String): IApiRequest<Config>
    fun getBuffInviteMiningList(params: Map<String, String>): IApiRequest<ListResultResponse<BuffInviteMining>>
    fun getBuffInviteMiningCount(): IApiRequest<Int>
    fun getBuffInviteMiningTotalCoin(): IApiRequest<Double>
    fun getAirDrop(): IApiRequest<AirDrop>
    fun airDropApply(params: Map<String, String>): IApiRequest<Any>
    fun reward(params: Map<String, String>): IApiRequest<Float>
    fun getLuckyBoxList(): IApiRequest<ListResultResponse<LuckyBox>>
    fun getLuckyBox(seqNo: Long): IApiRequest<LuckyBox>
    fun getLuckyBoxProductList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxProductGroupItem>>
    fun getCountNotOpenLuckyBoxPurchaseItem(): IApiRequest<Count>
    fun saveLuckyBoxPurchase(params: LuckyBoxPurchase): IApiRequest<LuckyBoxPurchase>
    fun getNotOpenLuckyBoxPurchaseList(): IApiRequest<ListResultResponse<LuckyBoxPurchase>>
    fun getNotOpenLuckyBoxPurchaseItemListByLuckyBoxPurchaseSeqNo(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxPurchaseItem>>
    fun openLuckyBoxPurchaseItem(params: Map<String, String>): IApiRequest<Any>
    fun cancelLuckyBox(seqNo: Long): IApiRequest<Any>
    fun confirmLuckyBoxPurchaseItem(seqNo: Long): IApiRequest<LuckyBoxPurchaseItem>
    fun getTotalLuckyBoxPurchaseItemList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxPurchaseItem>>
    fun getLuckyBoxPurchaseItem(seqNo: Long): IApiRequest<LuckyBoxPurchaseItem>
    fun updateLuckyBoxImpression(params: Map<String, String>): IApiRequest<Any>
    fun insertLuckyBoxReply(params: LuckyBoxReply): IApiRequest<Any>
    fun updateLuckyBoxReply(seqNo: Long, params: LuckyBoxReply): IApiRequest<Any>
    fun deleteLuckyBoxReply(seqNo: Long): IApiRequest<Any>
    fun getLuckyBoxReplyList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxReply>>
    fun getOpenLuckyBoxPurchaseItemList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxPurchaseItem>>
    fun cashBackLuckyBoxPurchaseItem(seqNo: Long, params: Map<String, String>): IApiRequest<Any>
    fun saveLuckyBoxDeliveryPurchase(params: LuckyBoxDeliveryPurchase): IApiRequest<LuckyBoxDeliveryPurchase>
    fun getProduct(seqNo: Long): IApiRequest<Product>
    fun getProductOption(params: Map<String, String>): IApiRequest<ProductOptionTotal>
    fun withdraw(params: Withdraw): IApiRequest<Withdraw>
    fun getWithdrawList(params: Map<String, String>): IApiRequest<ListResultResponse<Withdraw>>
    fun getPointMallCategoryList(): IApiRequest<ListResultResponse<PointMallCategory>>
    fun pointMallLogin(): IApiRequest<String>
    fun getProvinsiList(): IApiRequest<ListResultResponse<Provinsi>>
    fun getKabkotaList(params: Map<String, String>): IApiRequest<ListResultResponse<Kabkota>>
    fun getKecamatanList(params: Map<String, String>): IApiRequest<ListResultResponse<Kecamatan>>
    fun getKodePosList(params: Map<String, String>): IApiRequest<ListResultResponse<KodePos>>
    fun getBankList(): IApiRequest<ListResultResponse<Bank>>
    fun pointMallExchange(params: Map<String, String>): IApiRequest<Any>
    fun getEventList(params: Map<String, String>): IApiRequest<ListResultResponse<Event>>
    fun getEvent(seqNo: Long): IApiRequest<Event>
    fun getEventGiftAll(params: Map<String, String>): IApiRequest<ListResultResponse<EventGift>>
    fun writeImpression(params: Map<String, String>): IApiRequest<Any>
    fun existsEventResult(params: Map<String, String>): IApiRequest<EventExist>
    fun getWinAll(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>>
    fun getEventWinList(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>>
    fun joinEvent(params: EventJoinParam): IApiRequest<EventJoinResult>
    fun getWinListOnlyPresent(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>>
    fun getMyWinListOnlyPresent(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>>
    fun getWinBySeqNo(params: Map<String, String>): IApiRequest<EventWin>
    fun getEventReplyList(params: Map<String, String>): IApiRequest<ListResultResponse<EventReply>>
    fun insertEventReply(params: EventReply): IApiRequest<Any>
    fun updateEventReply(seqNo: Long, params: EventReply): IApiRequest<Any>
    fun deleteEventReply(seqNo: Long): IApiRequest<Any>
    fun getUserWinList(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>>
    fun getNewWinCountByUser(): IApiRequest<Count>
    fun getWinAnnounceList(params: Map<String, String>): IApiRequest<ListResultResponse<Event>>
    fun checkJoinedMobile(params: Map<String, String>): IApiRequest<Any>
    fun checkJoinedMobileNumber(params: Map<String, String>): IApiRequest<Any>
    fun sendWhatsapp(params: Map<String, String>): IApiRequest<Any>
    fun verifyWhatsapp(params: Map<String, String>): IApiRequest<Any>
    fun sendSms(params: Map<String, String>): IApiRequest<Any>
    fun verifySms(params: Map<String, String>): IApiRequest<Any>
    fun inputRecommender(params: Map<String, String>): IApiRequest<Any>

}