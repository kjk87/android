package com.root37.buflexz.core.network.apis

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
import com.root37.buflexz.core.network.request.IApiRequest

/**
 * Created by j2n on 2016. 9. 12..
 */
interface IApiBuilder {
    fun loginByPlatform(params: Map<String, String>): IApiRequest<Member>
    fun login(params: Map<String, String>): IApiRequest<Member>
    fun join(params: Member): IApiRequest<Member>
    fun updateMember(params: Member): IApiRequest<Member>
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
    fun lotteryJoin(params: Map<String, String>): IApiRequest<Any>
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
    fun withdrawalCancel(params: Map<String, String>): IApiRequest<Any>
    fun authWalletEmail(params: Map<String, String>): IApiRequest<Boolean>
    fun updateMarketingReceiving(params: Map<String, String>): IApiRequest<Any>
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
    fun getProductCategoryList(): IApiRequest<ListResultResponse<ProductCategory>>
    fun getProductList(params: Map<String, String>): IApiRequest<ListResultResponse<Product>>
    fun getProduct(seqNo: Long): IApiRequest<Product>
    fun getMemberDelivery(): IApiRequest<MemberDelivery>
    fun getNation(params: Map<String, String>): IApiRequest<Nation>
    fun getNationList(): IApiRequest<ListResultResponse<Nation>>
    fun postProductPurchase(params: ProductPurchase): IApiRequest<ProductPurchase>
    fun getEmsCountryList(): IApiRequest<ListResultResponse<EmsCountry>>
    fun getProductPurchaseList(params: Map<String, String>): IApiRequest<ListResultResponse<ProductPurchase>>
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
}