package com.lejel.wowbox.core.network

import com.lejel.wowbox.core.network.apis.IApiBuilder
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
import com.lejel.wowbox.core.network.request.ApiRequest
import com.lejel.wowbox.core.network.request.IApiRequest

/**
 * Created by j2n on 2016. 9. 12..
 */
class ApiBuilder private constructor() : IApiBuilder {
    private val TAG = ApiBuilder::class.java.simpleName

    override fun loginByPlatform(params: Map<String, String>): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.loginByPlatform(params))
    }

    override fun loginByMobile(params: Map<String, String>): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.loginByMobile(params))
    }

    override fun login(params: Map<String, String>): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.login(params))
    }

    override fun updateMember(params: Member): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.updateMember(params))
    }

    override fun updateMobileNumber(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.updateMobileNumber(params))
    }

    override fun updateVerifiedMobileNumber(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.updateVerifiedMobileNumber(params))
    }

    override fun updateProfile(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.updateProfile(params))
    }

    override fun join(params: Member): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.join(params))
    }

    override fun wowMallJoin(): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.wowMallJoin())
    }

    override fun checkNickname(params: Map<String, String>): IApiRequest<Boolean> {
        return ApiRequest.Builder<Boolean>().setRequestCall(ApiController.api.checkNickname(params))
    }

    override fun getSession(): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.getSession())
    }

    override fun reloadSession(): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.reloadSession())
    }

    override fun refreshToken(params: Map<String, String>): IApiRequest<String> {
        return ApiRequest.Builder<String>().setRequestCall(ApiController.api.refreshToken(params))
    }

    override fun exchangePointToBall(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.exchangePointToBall(params))
    }

    override fun getTermsList(params: Map<String, String>): IApiRequest<ListResultResponse<Terms>> {
        return ApiRequest.Builder<ListResultResponse<Terms>>().setRequestCall(ApiController.api.getTermsList(params))
    }

    override fun getTerms(seqNo: Long): IApiRequest<Terms> {
        return ApiRequest.Builder<Terms>().setRequestCall(ApiController.api.getTerms(seqNo))
    }

    override fun getNotSignedList(): IApiRequest<ListResultResponse<Terms>> {
        return ApiRequest.Builder<ListResultResponse<Terms>>().setRequestCall(ApiController.api.getNotSignedList())
    }

    override fun agreeAddTerms(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.agreeAddTerms(params))
    }

    override fun postDevice(params: Device): IApiRequest<Device> {
        return ApiRequest.Builder<Device>().setRequestCall(ApiController.api.postDevice(params))
    }

    override fun getDevice(params: Map<String, String>): IApiRequest<Device> {
        return ApiRequest.Builder<Device>().setRequestCall(ApiController.api.getDevice(params))
    }

    override fun getApp(params: Map<String, String>): IApiRequest<App> {
        return ApiRequest.Builder<App>().setRequestCall(ApiController.api.getApp(params))
    }

    override fun getLottery(): IApiRequest<Lottery> {
        return ApiRequest.Builder<Lottery>().setRequestCall(ApiController.api.getLottery())
    }

    override fun getLotteryByLotteryRound(lotteryRound: Int): IApiRequest<Lottery> {
        return ApiRequest.Builder<Lottery>().setRequestCall(ApiController.api.getLotteryByLotteryRound(lotteryRound))
    }

    override fun getLotteryRoundList(): IApiRequest<ListResultResponse<Lottery>> {
        return ApiRequest.Builder<ListResultResponse<Lottery>>().setRequestCall(ApiController.api.getLotteryRoundList())
    }

    override fun lotteryJoin(params: Map<String, String>): IApiRequest<LotteryJoin> {
        return ApiRequest.Builder<LotteryJoin>().setRequestCall(ApiController.api.lotteryJoin(params))
    }

    override fun getLotteryJoinList(params: Map<String, String>): IApiRequest<ListResultResponse<LotteryJoin>> {
        return ApiRequest.Builder<ListResultResponse<LotteryJoin>>().setRequestCall(ApiController.api.getLotteryJoinList(params))
    }

    override fun getLotteryJoinCount(params: Map<String, String>): IApiRequest<Int> {
        return ApiRequest.Builder<Int>().setRequestCall(ApiController.api.getLotteryJoinCount(params))
    }

    override fun getMyLotteryWinList(lotteryRound: Int, params: Map<String, String>): IApiRequest<ListResultResponse<LotteryWin>> {
        return ApiRequest.Builder<ListResultResponse<LotteryWin>>().setRequestCall(ApiController.api.getMyLotteryWinList(lotteryRound, params))
    }

    override fun getMyLotteryWinCount(lotteryRound: Int): IApiRequest<Int> {
        return ApiRequest.Builder<Int>().setRequestCall(ApiController.api.getMyLotteryWinCount(lotteryRound))
    }

    override fun getTotalLotteryWinList(lotteryRound: Int, params: Map<String, String>): IApiRequest<ListResultResponse<LotteryWin>> {
        return ApiRequest.Builder<ListResultResponse<LotteryWin>>().setRequestCall(ApiController.api.getTotalLotteryWinList(lotteryRound, params))
    }

    override fun getLotteryWinCount(lotteryRound: Int): IApiRequest<Int> {
        return ApiRequest.Builder<Int>().setRequestCall(ApiController.api.getLotteryWinCount(lotteryRound))
    }

    override fun receiveLotteryWin(seqNo: Long, params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.receiveLotteryWin(seqNo, params))
    }

    override fun totalReceiveLotteryWin(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.totalReceiveLotteryWin(params))
    }

    override fun getLotteryWinRemainCount(params: Map<String, String>): IApiRequest<Int> {
        return ApiRequest.Builder<Int>().setRequestCall(ApiController.api.getLotteryWinRemainCount(params))
    }

    override fun getLotteryWinCondition(lotteryRound: Int): IApiRequest<LotteryWinCondition> {
        return ApiRequest.Builder<LotteryWinCondition>().setRequestCall(ApiController.api.getLotteryWinCondition(lotteryRound))
    }

    override fun getBannerList(params: Map<String, String>): IApiRequest<ListResultResponse<Banner>> {
        return ApiRequest.Builder<ListResultResponse<Banner>>().setRequestCall(ApiController.api.getBannerList(params))
    }

    override fun getPopupList(params: Map<String, String>): IApiRequest<ListResultResponse<Popup>> {
        return ApiRequest.Builder<ListResultResponse<Popup>>().setRequestCall(ApiController.api.getPopupList(params))
    }

    override fun getGiftCardBrandList(): IApiRequest<ListResultResponse<GiftCardBrand>> {
        return ApiRequest.Builder<ListResultResponse<GiftCardBrand>>().setRequestCall(ApiController.api.getGiftCardBrandList())
    }

    override fun getGiftCardList(brandSeqNo: Long, params: Map<String, String>): IApiRequest<ListResultResponse<GiftCard>> {
        return ApiRequest.Builder<ListResultResponse<GiftCard>>().setRequestCall(ApiController.api.getGiftCardList(brandSeqNo, params))
    }

    override fun getGiftCard(seqNo: Long): IApiRequest<GiftCard> {
        return ApiRequest.Builder<GiftCard>().setRequestCall(ApiController.api.getGiftCard(seqNo))
    }

    override fun giftCardPurchase(params: GiftCardPurchase): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.giftCardPurchase(params))
    }

    override fun getGiftCardPurchase(seqNo: Long): IApiRequest<GiftCardPurchase> {
        return ApiRequest.Builder<GiftCardPurchase>().setRequestCall(ApiController.api.getGiftCardPurchase(seqNo))
    }

    override fun getGiftCardPurchaseList(params: Map<String, String>): IApiRequest<ListResultResponse<GiftCardPurchase>> {
        return ApiRequest.Builder<ListResultResponse<GiftCardPurchase>>().setRequestCall(ApiController.api.getGiftCardPurchaseList(params))
    }

    override fun getHistoryPointList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryPoint>> {
        return ApiRequest.Builder<ListResultResponse<HistoryPoint>>().setRequestCall(ApiController.api.getHistoryPointList(params))
    }

    override fun getHistoryCashList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryCash>> {
        return ApiRequest.Builder<ListResultResponse<HistoryCash>>().setRequestCall(ApiController.api.getHistoryCashList(params))
    }

    override fun getHistoryBallList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryBall>> {
        return ApiRequest.Builder<ListResultResponse<HistoryBall>>().setRequestCall(ApiController.api.getHistoryBallList(params))
    }

    override fun postPartner(params: Partner): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.postPartner(params))
    }

    override fun partnerResendEmail(): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.partnerResendEmail())
    }

    override fun getPartner(): IApiRequest<Partner> {
        return ApiRequest.Builder<Partner>().setRequestCall(ApiController.api.getPartner())
    }

    override fun getChildPartnerCount(): IApiRequest<Int> {
        return ApiRequest.Builder<Int>().setRequestCall(ApiController.api.getChildPartnerCount())
    }

    override fun getChildPartnerList(params: Map<String, String>): IApiRequest<ListResultResponse<Partner>> {
        return ApiRequest.Builder<ListResultResponse<Partner>>().setRequestCall(ApiController.api.getChildPartnerList(params))
    }

    override fun getTotalProfit(): IApiRequest<Double> {
        return ApiRequest.Builder<Double>().setRequestCall(ApiController.api.getTotalProfit())
    }

    override fun getTotalBonusProfit(): IApiRequest<Double> {
        return ApiRequest.Builder<Double>().setRequestCall(ApiController.api.getTotalBonusProfit())
    }

    override fun getCommission(params: Map<String, String>): IApiRequest<Double> {
        return ApiRequest.Builder<Double>().setRequestCall(ApiController.api.getCommission(params))
    }

    override fun getHistoryCommissionList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryCommission>> {
        return ApiRequest.Builder<ListResultResponse<HistoryCommission>>().setRequestCall(ApiController.api.getHistoryCommissionList(params))
    }

    override fun getProfitPartner(params: Map<String, String>): IApiRequest<ProfitPartner> {
        return ApiRequest.Builder<ProfitPartner>().setRequestCall(ApiController.api.getProfitPartner(params))
    }

    override fun getChildProfitPartnerList(params: Map<String, String>): IApiRequest<ListResultResponse<ProfitPartner>> {
        return ApiRequest.Builder<ListResultResponse<ProfitPartner>>().setRequestCall(ApiController.api.getChildProfitPartnerList(params))
    }

    override fun getFaqCategoryList(): IApiRequest<ListResultResponse<FaqCategory>> {
        return ApiRequest.Builder<ListResultResponse<FaqCategory>>().setRequestCall(ApiController.api.getFaqCategoryList())
    }

    override fun getFaqList(params: Map<String, String>): IApiRequest<ListResultResponse<Faq>> {
        return ApiRequest.Builder<ListResultResponse<Faq>>().setRequestCall(ApiController.api.getFaqList(params))
    }

    override fun getFaq(seqNo: Long): IApiRequest<Faq> {
        return ApiRequest.Builder<Faq>().setRequestCall(ApiController.api.getFaq(seqNo))
    }

    override fun getNoticeList(params: Map<String, String>): IApiRequest<ListResultResponse<Notice>> {
        return ApiRequest.Builder<ListResultResponse<Notice>>().setRequestCall(ApiController.api.getNoticeList(params))
    }

    override fun getNotice(seqNo: Long): IApiRequest<Notice> {
        return ApiRequest.Builder<Notice>().setRequestCall(ApiController.api.getNotice(seqNo))
    }

    override fun getInquireList(params: Map<String, String>): IApiRequest<ListResultResponse<Inquire>> {
        return ApiRequest.Builder<ListResultResponse<Inquire>>().setRequestCall(ApiController.api.getInquireList(params))
    }

    override fun postInquire(params: Inquire): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.postInquire(params))
    }

    override fun sendEmailForAuth(params: Map<String, String>): IApiRequest<String> {
        return ApiRequest.Builder<String>().setRequestCall(ApiController.api.sendEmailForAuth(params))
    }

    override fun withdrawal(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.withdrawal(params))
    }

    override fun withdrawalCancelWithVerify(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.withdrawalCancelWithVerify(params))
    }

    override fun withdrawalCancel(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.withdrawalCancel(params))
    }

    override fun authWalletEmail(params: Map<String, String>): IApiRequest<Boolean> {
        return ApiRequest.Builder<Boolean>().setRequestCall(ApiController.api.authWalletEmail(params))
    }

    override fun updateMarketingReceiving(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.updateMarketingReceiving(params))
    }

    override fun getLuckyDrawThemeList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawTheme>> {
        return ApiRequest.Builder<ListResultResponse<LuckyDrawTheme>>().setRequestCall(ApiController.api.getLuckyDrawThemeList(params))
    }

    override fun getLuckyDrawList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDraw>> {
        return ApiRequest.Builder<ListResultResponse<LuckyDraw>>().setRequestCall(ApiController.api.getLuckyDrawList(params))
    }

    override fun getLuckyDrawCompleteList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDraw>> {
        return ApiRequest.Builder<ListResultResponse<LuckyDraw>>().setRequestCall(ApiController.api.getLuckyDrawCompleteList(params))
    }

    override fun getLuckyDraw(seqNo: Long): IApiRequest<LuckyDraw> {
        return ApiRequest.Builder<LuckyDraw>().setRequestCall(ApiController.api.getLuckyDraw(seqNo))
    }

    override fun getLuckyDrawGiftList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawGift>> {
        return ApiRequest.Builder<ListResultResponse<LuckyDrawGift>>().setRequestCall(ApiController.api.getLuckyDrawGiftList(params))
    }

    override fun getLuckyDrawNumberRemainCount(params: Map<String, String>): IApiRequest<Int> {
        return ApiRequest.Builder<Int>().setRequestCall(ApiController.api.getLuckyDrawNumberRemainCount(params))
    }

    override fun luckyDrawSelectNumber(params: Map<String, String>): IApiRequest<LuckyDrawNumber> {
        return ApiRequest.Builder<LuckyDrawNumber>().setRequestCall(ApiController.api.luckyDrawSelectNumber(params))
    }

    override fun luckyDrawDeleteNumber(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.luckyDrawDeleteNumber(params))
    }

    override fun getLuckyDrawJoinCount(params: Map<String, String>): IApiRequest<Int> {
        return ApiRequest.Builder<Int>().setRequestCall(ApiController.api.getLuckyDrawJoinCount(params))
    }

    override fun postLuckyDrawPurchase(params: LuckyDrawPurchase): IApiRequest<LuckyDrawPurchase> {
        return ApiRequest.Builder<LuckyDrawPurchase>().setRequestCall(ApiController.api.postLuckyDrawPurchase(params))
    }

    override fun getLuckyDrawPurchaseList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawPurchase>> {
        return ApiRequest.Builder<ListResultResponse<LuckyDrawPurchase>>().setRequestCall(ApiController.api.getLuckyDrawPurchaseList(params))
    }

    override fun getLuckyDrawWinList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawWin>> {
        return ApiRequest.Builder<ListResultResponse<LuckyDrawWin>>().setRequestCall(ApiController.api.getLuckyDrawWinList(params))
    }

    override fun getLuckyDrawMyWinList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawWin>> {
        return ApiRequest.Builder<ListResultResponse<LuckyDrawWin>>().setRequestCall(ApiController.api.getLuckyDrawMyWinList(params))
    }

    override fun getLuckyDrawMyWinListAll(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawWin>> {
        return ApiRequest.Builder<ListResultResponse<LuckyDrawWin>>().setRequestCall(ApiController.api.getLuckyDrawMyWinListAll(params))
    }

    override fun getLuckyDrawWinReplyList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyDrawWinReply>> {
        return ApiRequest.Builder<ListResultResponse<LuckyDrawWinReply>>().setRequestCall(ApiController.api.getLuckyDrawWinReplyList(params))
    }

    override fun postLuckyDrawWinReply(params: Map<String, String>): IApiRequest<LuckyDrawWinReply> {
        return ApiRequest.Builder<LuckyDrawWinReply>().setRequestCall(ApiController.api.postLuckyDrawWinReply(params))
    }

    override fun updateLuckyDrawWinReply(params: Map<String, String>): IApiRequest<LuckyDrawWinReply> {
        return ApiRequest.Builder<LuckyDrawWinReply>().setRequestCall(ApiController.api.updateLuckyDrawWinReply(params))
    }

    override fun deleteLuckyDrawWinReply(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.deleteLuckyDrawWinReply(params))
    }

    override fun updateLuckyDrawWinImpression(params: Map<String, String>): IApiRequest<LuckyDrawWin> {
        return ApiRequest.Builder<LuckyDrawWin>().setRequestCall(ApiController.api.updateLuckyDrawWinImpression(params))
    }

    override fun getMemberDelivery(): IApiRequest<MemberDelivery> {
        return ApiRequest.Builder<MemberDelivery>().setRequestCall(ApiController.api.getMemberDelivery())
    }

    override fun getNation(params: Map<String, String>): IApiRequest<Nation> {
        return ApiRequest.Builder<Nation>().setRequestCall(ApiController.api.getNation(params))
    }

    override fun getNationList(): IApiRequest<ListResultResponse<Nation>> {
        return ApiRequest.Builder<ListResultResponse<Nation>>().setRequestCall(ApiController.api.getNationList())
    }

    override fun getEmsCountryList(): IApiRequest<ListResultResponse<EmsCountry>> {
        return ApiRequest.Builder<ListResultResponse<EmsCountry>>().setRequestCall(ApiController.api.getEmsCountryList())
    }

    override fun getCommunityApply(): IApiRequest<CommunityApply> {
        return ApiRequest.Builder<CommunityApply>().setRequestCall(ApiController.api.getCommunityApply())
    }

    override fun postCommunityApply(params: CommunityApply): IApiRequest<CommunityApply> {
        return ApiRequest.Builder<CommunityApply>().setRequestCall(ApiController.api.postCommunityApply(params))
    }

    override fun getMemberProfileImageList(): IApiRequest<ListResultResponse<MemberProfileImage>> {
        return ApiRequest.Builder<ListResultResponse<MemberProfileImage>>().setRequestCall(ApiController.api.getMemberProfileImageList())
    }

    override fun getNotificationBoxList(params: Map<String, String>): IApiRequest<ListResultResponse<NotificationBox>> {
        return ApiRequest.Builder<ListResultResponse<NotificationBox>>().setRequestCall(ApiController.api.getNotificationBoxList(params))
    }

    override fun getUnReadCount(): IApiRequest<Int> {
        return ApiRequest.Builder<Int>().setRequestCall(ApiController.api.getUnReadCount())
    }

    override fun notificationBoxRead(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.notificationBoxRead(params))
    }

    override fun notificationBoxDelete(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.notificationBoxDelete(params))
    }

    override fun getInviteList(params: Map<String, String>): IApiRequest<ListResultResponse<Member>> {
        return ApiRequest.Builder<ListResultResponse<Member>>().setRequestCall(ApiController.api.getInviteList(params))
    }

    override fun replyReport(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.replyReport(params))
    }

    override fun walletDuplicateUser(params: Map<String, String>): IApiRequest<WalletRes> {
        return ApiRequest.Builder<WalletRes>().setRequestCall(ApiController.api.walletDuplicateUser(params))
    }

    override fun walletSignUp(): IApiRequest<WalletRes> {
        return ApiRequest.Builder<WalletRes>().setRequestCall(ApiController.api.walletSignUp())
    }

    override fun getBuffCoinBalance(): IApiRequest<Map<String, Any>> {
        return ApiRequest.Builder<Map<String, Any>>().setRequestCall(ApiController.api.getBuffCoinBalance())
    }

    override fun makeInviteUrl(): IApiRequest<String> {
        return ApiRequest.Builder<String>().setRequestCall(ApiController.api.makeInviteUrl())
    }

    override fun attendance(): IApiRequest<Float> {
        return ApiRequest.Builder<Float>().setRequestCall(ApiController.api.attendance())
    }

    override fun getBenefit(): IApiRequest<Benefit> {
        return ApiRequest.Builder<Benefit>().setRequestCall(ApiController.api.getBenefit())
    }

    override fun getBenefitCalculateList(params: Map<String, String>): IApiRequest<ListResultResponse<BenefitCalculate>> {
        return ApiRequest.Builder<ListResultResponse<BenefitCalculate>>().setRequestCall(ApiController.api.getBenefitCalculateList(params))
    }

    override fun getHistoryBenefitList(params: Map<String, String>): IApiRequest<ListResultResponse<HistoryBenefit>> {
        return ApiRequest.Builder<ListResultResponse<HistoryBenefit>>().setRequestCall(ApiController.api.getHistoryBenefitList(params))
    }

    override fun getTotalBenefit(params: Map<String, String>): IApiRequest<Double> {
        return ApiRequest.Builder<Double>().setRequestCall(ApiController.api.getTotalBenefit(params))
    }

    override fun checkLuckyDrawPrivate(seqNo: Long, params: Map<String, String>): IApiRequest<Boolean> {
        return ApiRequest.Builder<Boolean>().setRequestCall(ApiController.api.checkLuckyDrawPrivate(seqNo, params))
    }

    override fun getConfig(code: String): IApiRequest<Config> {
        return ApiRequest.Builder<Config>().setRequestCall(ApiController.api.getConfig(code))
    }

    override fun getBuffInviteMiningList(params: Map<String, String>): IApiRequest<ListResultResponse<BuffInviteMining>> {
        return ApiRequest.Builder<ListResultResponse<BuffInviteMining>>().setRequestCall(ApiController.api.getBuffInviteMiningList(params))
    }

    override fun getBuffInviteMiningCount(): IApiRequest<Int> {
        return ApiRequest.Builder<Int>().setRequestCall(ApiController.api.getBuffInviteMiningCount())
    }

    override fun getBuffInviteMiningTotalCoin(): IApiRequest<Double> {
        return ApiRequest.Builder<Double>().setRequestCall(ApiController.api.getBuffInviteMiningTotalCoin())
    }

    override fun getAirDrop(): IApiRequest<AirDrop> {
        return ApiRequest.Builder<AirDrop>().setRequestCall(ApiController.api.getAirDrop())
    }

    override fun airDropApply(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.airDropApply(params))
    }

    override fun reward(params: Map<String, String>): IApiRequest<Float> {
        return ApiRequest.Builder<Float>().setRequestCall(ApiController.api.reward(params))
    }

    override fun getLuckyBoxList(): IApiRequest<ListResultResponse<LuckyBox>> {
        return ApiRequest.Builder<ListResultResponse<LuckyBox>>().setRequestCall(ApiController.api.getLuckyBoxList())
    }

    override fun getLuckyBox(seqNo: Long): IApiRequest<LuckyBox> {
        return ApiRequest.Builder<LuckyBox>().setRequestCall(ApiController.api.getLuckyBox(seqNo))
    }

    override fun getLuckyBoxProductList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxProductGroupItem>> {
        return ApiRequest.Builder<ListResultResponse<LuckyBoxProductGroupItem>>().setRequestCall(ApiController.api.getLuckyBoxProductList(params))
    }

    override fun getCountNotOpenLuckyBoxPurchaseItem(): IApiRequest<Count> {
        return ApiRequest.Builder<Count>().setRequestCall(ApiController.api.getCountNotOpenLuckyBoxPurchaseItem())
    }

    override fun saveLuckyBoxPurchase(params: LuckyBoxPurchase): IApiRequest<LuckyBoxPurchase> {
        return ApiRequest.Builder<LuckyBoxPurchase>().setRequestCall(ApiController.api.saveLuckyBoxPurchase(params))
    }

    override fun getNotOpenLuckyBoxPurchaseList(): IApiRequest<ListResultResponse<LuckyBoxPurchase>> {
        return ApiRequest.Builder<ListResultResponse<LuckyBoxPurchase>>().setRequestCall(ApiController.api.getNotOpenLuckyBoxPurchaseList())
    }

    override fun getNotOpenLuckyBoxPurchaseItemListByLuckyBoxPurchaseSeqNo(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxPurchaseItem>> {
        return ApiRequest.Builder<ListResultResponse<LuckyBoxPurchaseItem>>().setRequestCall(ApiController.api.getNotOpenLuckyBoxPurchaseItemListByLuckyBoxPurchaseSeqNo(params))
    }

    override fun openLuckyBoxPurchaseItem(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.openLuckyBoxPurchaseItem(params))
    }

    override fun cancelLuckyBox(seqNo: Long): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.cancelLuckyBox(seqNo))
    }

    override fun confirmLuckyBoxPurchaseItem(seqNo: Long): IApiRequest<LuckyBoxPurchaseItem> {
        return ApiRequest.Builder<LuckyBoxPurchaseItem>().setRequestCall(ApiController.api.confirmLuckyBoxPurchaseItem(seqNo))
    }

    override fun getTotalLuckyBoxPurchaseItemList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxPurchaseItem>> {
        return ApiRequest.Builder<ListResultResponse<LuckyBoxPurchaseItem>>().setRequestCall(ApiController.api.getTotalLuckyBoxPurchaseItemList(params))
    }

    override fun getLuckyBoxPurchaseItem(seqNo: Long): IApiRequest<LuckyBoxPurchaseItem> {
        return ApiRequest.Builder<LuckyBoxPurchaseItem>().setRequestCall(ApiController.api.getLuckyBoxPurchaseItem(seqNo))
    }

    override fun updateLuckyBoxImpression(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.updateLuckyBoxImpression(params))
    }

    override fun insertLuckyBoxReply(params: LuckyBoxReply): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.insertLuckyBoxReply(params))
    }

    override fun updateLuckyBoxReply(seqNo: Long, params: LuckyBoxReply): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.updateLuckyBoxReply(seqNo, params))
    }

    override fun deleteLuckyBoxReply(seqNo: Long): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.deleteLuckyBoxReply(seqNo))
    }

    override fun getLuckyBoxReplyList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxReply>> {
        return ApiRequest.Builder<ListResultResponse<LuckyBoxReply>>().setRequestCall(ApiController.api.getLuckyBoxReplyList(params))
    }

    override fun getOpenLuckyBoxPurchaseItemList(params: Map<String, String>): IApiRequest<ListResultResponse<LuckyBoxPurchaseItem>> {
        return ApiRequest.Builder<ListResultResponse<LuckyBoxPurchaseItem>>().setRequestCall(ApiController.api.getOpenLuckyBoxPurchaseItemList(params))
    }

    override fun cashBackLuckyBoxPurchaseItem(seqNo: Long, params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.cashBackLuckyBoxPurchaseItem(seqNo, params))
    }

    override fun saveLuckyBoxDeliveryPurchase(params: LuckyBoxDeliveryPurchase): IApiRequest<LuckyBoxDeliveryPurchase> {
        return ApiRequest.Builder<LuckyBoxDeliveryPurchase>().setRequestCall(ApiController.api.saveLuckyBoxDeliveryPurchase(params))
    }

    override fun getProduct(seqNo: Long): IApiRequest<Product> {
        return ApiRequest.Builder<Product>().setRequestCall(ApiController.api.getProduct(seqNo))
    }

    override fun getProductOption(params: Map<String, String>): IApiRequest<ProductOptionTotal> {
        return ApiRequest.Builder<ProductOptionTotal>().setRequestCall(ApiController.api.getProductOption(params))
    }

    override fun withdraw(params: Withdraw): IApiRequest<Withdraw> {
        return ApiRequest.Builder<Withdraw>().setRequestCall(ApiController.api.withdraw(params))
    }

    override fun getWithdrawList(params: Map<String, String>): IApiRequest<ListResultResponse<Withdraw>> {
        return ApiRequest.Builder<ListResultResponse<Withdraw>>().setRequestCall(ApiController.api.getWithdrawList(params))
    }

    override fun getPointMallCategoryList(): IApiRequest<ListResultResponse<PointMallCategory>> {
        return ApiRequest.Builder<ListResultResponse<PointMallCategory>>().setRequestCall(ApiController.api.getPointMallCategoryList())
    }

    override fun pointMallLogin(): IApiRequest<String> {
        return ApiRequest.Builder<String>().setRequestCall(ApiController.api.pointMallLogin())
    }

    override fun getProvinsiList(): IApiRequest<ListResultResponse<Provinsi>> {
        return ApiRequest.Builder<ListResultResponse<Provinsi>>().setRequestCall(ApiController.api.getProvinsiList())
    }

    override fun getKabkotaList(params: Map<String, String>): IApiRequest<ListResultResponse<Kabkota>> {
        return ApiRequest.Builder<ListResultResponse<Kabkota>>().setRequestCall(ApiController.api.getKabkotaList(params))
    }

    override fun getKecamatanList(params: Map<String, String>): IApiRequest<ListResultResponse<Kecamatan>> {
        return ApiRequest.Builder<ListResultResponse<Kecamatan>>().setRequestCall(ApiController.api.getKecamatanList(params))
    }

    override fun getKodePosList(params: Map<String, String>): IApiRequest<ListResultResponse<KodePos>> {
        return ApiRequest.Builder<ListResultResponse<KodePos>>().setRequestCall(ApiController.api.getKodePosList(params))
    }

    override fun getBankList(): IApiRequest<ListResultResponse<Bank>> {
        return ApiRequest.Builder<ListResultResponse<Bank>>().setRequestCall(ApiController.api.getBankList())
    }

    override fun pointMallExchange(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.pointMallExchange(params))
    }

    override fun getEventList(params: Map<String, String>): IApiRequest<ListResultResponse<Event>> {
        return ApiRequest.Builder<ListResultResponse<Event>>().setRequestCall(ApiController.api.getEventList(params))
    }

    override fun getEvent(seqNo: Long): IApiRequest<Event> {
        return ApiRequest.Builder<Event>().setRequestCall(ApiController.api.getEvent(seqNo))
    }

    override fun getEventGiftAll(params: Map<String, String>): IApiRequest<ListResultResponse<EventGift>> {
        return ApiRequest.Builder<ListResultResponse<EventGift>>().setRequestCall(ApiController.api.getEventGiftAll(params))
    }

    override fun writeImpression(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.writeImpression(params))
    }

    override fun existsEventResult(params: Map<String, String>): IApiRequest<EventExist> {
        return ApiRequest.Builder<EventExist>().setRequestCall(ApiController.api.existsEventResult(params))
    }

    override fun getWinAll(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>> {
        return ApiRequest.Builder<ListResultResponse<EventWin>>().setRequestCall(ApiController.api.getWinAll(params))
    }

    override fun getEventWinList(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>> {
        return ApiRequest.Builder<ListResultResponse<EventWin>>().setRequestCall(ApiController.api.getEventWinList(params))
    }

    override fun joinEvent(params: EventJoinParam): IApiRequest<EventJoinResult> {
        return ApiRequest.Builder<EventJoinResult>().setRequestCall(ApiController.api.joinEvent(params))
    }

    override fun getWinListOnlyPresent(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>> {
        return ApiRequest.Builder<ListResultResponse<EventWin>>().setRequestCall(ApiController.api.getWinListOnlyPresent(params))
    }

    override fun getMyWinListOnlyPresent(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>> {
        return ApiRequest.Builder<ListResultResponse<EventWin>>().setRequestCall(ApiController.api.getMyWinListOnlyPresent(params))
    }

    override fun getWinBySeqNo(params: Map<String, String>): IApiRequest<EventWin> {
        return ApiRequest.Builder<EventWin>().setRequestCall(ApiController.api.getWinBySeqNo(params))
    }

    override fun getEventReplyList(params: Map<String, String>): IApiRequest<ListResultResponse<EventReply>> {
        return ApiRequest.Builder<ListResultResponse<EventReply>>().setRequestCall(ApiController.api.getEventReplyList(params))
    }

    override fun insertEventReply(params: EventReply): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.insertEventReply(params))
    }

    override fun updateEventReply(seqNo: Long, params: EventReply): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.updateEventReply(seqNo, params))
    }

    override fun deleteEventReply(seqNo: Long): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.deleteEventReply(seqNo))
    }

    override fun getUserWinList(params: Map<String, String>): IApiRequest<ListResultResponse<EventWin>> {
        return ApiRequest.Builder<ListResultResponse<EventWin>>().setRequestCall(ApiController.api.getUserWinList(params))
    }

    override fun getNewWinCountByUser(): IApiRequest<Count> {
        return ApiRequest.Builder<Count>().setRequestCall(ApiController.api.getNewWinCountByUser())
    }

    override fun getWinAnnounceList(params: Map<String, String>): IApiRequest<ListResultResponse<Event>> {
        return ApiRequest.Builder<ListResultResponse<Event>>().setRequestCall(ApiController.api.getWinAnnounceList(params))
    }

    override fun checkJoinedMobile(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.checkJoinedMobile(params))
    }

    override fun checkJoinedMobileNumber(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.checkJoinedMobileNumber(params))
    }

    override fun sendWhatsapp(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.sendWhatsapp(params))
    }

    override fun verifyWhatsapp(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.verifyWhatsapp(params))
    }

    override fun sendSms(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.sendSms(params))
    }

    override fun verifySms(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.verifySms(params))
    }

    override fun inputRecommender(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.inputRecommender(params))
    }

    companion object {
        @JvmStatic
        fun create(): ApiBuilder {
            return ApiBuilder()
        }
    }
}