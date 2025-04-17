package com.root37.buflexz.core.network

import com.root37.buflexz.core.network.apis.IApiBuilder
import com.root37.buflexz.core.network.model.dto.App
import com.root37.buflexz.core.network.model.dto.Banner
import com.root37.buflexz.core.network.model.dto.CommunityApply
import com.root37.buflexz.core.network.model.dto.Device
import com.root37.buflexz.core.network.model.dto.EmsCountry
import com.root37.buflexz.core.network.model.dto.Faq
import com.root37.buflexz.core.network.model.dto.FaqCategory
import com.root37.buflexz.core.network.model.dto.GiftCard
import com.root37.buflexz.core.network.model.dto.GiftCardBrand
import com.root37.buflexz.core.network.model.dto.GiftCardPurchase
import com.root37.buflexz.core.network.model.dto.HistoryBall
import com.root37.buflexz.core.network.model.dto.HistoryCommission
import com.root37.buflexz.core.network.model.dto.HistoryPoint
import com.root37.buflexz.core.network.model.dto.Inquire
import com.root37.buflexz.core.network.model.dto.Lottery
import com.root37.buflexz.core.network.model.dto.LotteryJoin
import com.root37.buflexz.core.network.model.dto.LotteryWin
import com.root37.buflexz.core.network.model.dto.LotteryWinCondition
import com.root37.buflexz.core.network.model.dto.LuckyDraw
import com.root37.buflexz.core.network.model.dto.LuckyDrawGift
import com.root37.buflexz.core.network.model.dto.LuckyDrawNumber
import com.root37.buflexz.core.network.model.dto.LuckyDrawPurchase
import com.root37.buflexz.core.network.model.dto.LuckyDrawWin
import com.root37.buflexz.core.network.model.dto.LuckyDrawWinReply
import com.root37.buflexz.core.network.model.dto.Member
import com.root37.buflexz.core.network.model.dto.MemberDelivery
import com.root37.buflexz.core.network.model.dto.MemberProfileImage
import com.root37.buflexz.core.network.model.dto.Nation
import com.root37.buflexz.core.network.model.dto.Notice
import com.root37.buflexz.core.network.model.dto.NotificationBox
import com.root37.buflexz.core.network.model.dto.Partner
import com.root37.buflexz.core.network.model.dto.Popup
import com.root37.buflexz.core.network.model.dto.Product
import com.root37.buflexz.core.network.model.dto.ProductCategory
import com.root37.buflexz.core.network.model.dto.ProductPurchase
import com.root37.buflexz.core.network.model.dto.ProfitPartner
import com.root37.buflexz.core.network.model.dto.Terms
import com.root37.buflexz.core.network.model.dto.WalletRes
import com.root37.buflexz.core.network.model.response.ListResultResponse
import com.root37.buflexz.core.network.request.ApiRequest
import com.root37.buflexz.core.network.request.IApiRequest

/**
 * Created by j2n on 2016. 9. 12..
 */
class ApiBuilder private constructor() : IApiBuilder {
    private val TAG = ApiBuilder::class.java.simpleName

    override fun loginByPlatform(params: Map<String, String>): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.loginByPlatform(params))
    }

    override fun login(params: Map<String, String>): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.login(params))
    }

    override fun updateMember(params: Member): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.updateMember(params))
    }

    override fun updateProfile(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.updateProfile(params))
    }

    override fun join(params: Member): IApiRequest<Member> {
        return ApiRequest.Builder<Member>().setRequestCall(ApiController.api.join(params))
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

    override fun lotteryJoin(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.lotteryJoin(params))
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

    override fun withdrawalCancel(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.withdrawalCancel(params))
    }

    override fun authWalletEmail(params: Map<String, String>): IApiRequest<Boolean> {
        return ApiRequest.Builder<Boolean>().setRequestCall(ApiController.api.authWalletEmail(params))
    }

    override fun updateMarketingReceiving(params: Map<String, String>): IApiRequest<Any> {
        return ApiRequest.Builder<Any>().setRequestCall(ApiController.api.updateMarketingReceiving(params))
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

    override fun getProductCategoryList(): IApiRequest<ListResultResponse<ProductCategory>> {
        return ApiRequest.Builder<ListResultResponse<ProductCategory>>().setRequestCall(ApiController.api.getProductCategoryList())
    }

    override fun getProductList(params: Map<String, String>): IApiRequest<ListResultResponse<Product>> {
        return ApiRequest.Builder<ListResultResponse<Product>>().setRequestCall(ApiController.api.getProductList(params))
    }

    override fun getProduct(seqNo: Long): IApiRequest<Product> {
        return ApiRequest.Builder<Product>().setRequestCall(ApiController.api.getProduct(seqNo))
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

    override fun postProductPurchase(params: ProductPurchase): IApiRequest<ProductPurchase> {
        return ApiRequest.Builder<ProductPurchase>().setRequestCall(ApiController.api.postProductPurchase(params))
    }

    override fun getEmsCountryList(): IApiRequest<ListResultResponse<EmsCountry>> {
        return ApiRequest.Builder<ListResultResponse<EmsCountry>>().setRequestCall(ApiController.api.getEmsCountryList())
    }

    override fun getProductPurchaseList(params: Map<String, String>): IApiRequest<ListResultResponse<ProductPurchase>> {
        return ApiRequest.Builder<ListResultResponse<ProductPurchase>>().setRequestCall(ApiController.api.getProductPurchaseList(params))
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

    companion object {
        @JvmStatic
        fun create(): ApiBuilder {
            return ApiBuilder()
        }
    }
}