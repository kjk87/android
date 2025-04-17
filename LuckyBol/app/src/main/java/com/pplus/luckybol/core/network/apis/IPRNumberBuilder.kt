package com.pplus.luckybol.core.network.apis

import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.request.params.ParamsContact
import com.pplus.luckybol.core.network.model.request.params.ParamsJoinEvent
import com.pplus.luckybol.core.network.model.request.params.ParamsRegDevice
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.network.prnumber.IPRNumberRequest

/**
 * Created by j2n on 2016. 9. 12..
 */
interface IPRNumberBuilder {
    //existsUser
    fun existsUser(params: Map<String, String>): IPRNumberRequest<Any>

    //join
    fun join(params: User): IPRNumberRequest<User>

    //appversion
    fun appVersion(params: Map<String, String>): IPRNumberRequest<AppVersion>

    //login
    fun login(params: Map<String, String>): IPRNumberRequest<User>

    //check device
    fun existsDevice(params: Map<String, String>): IPRNumberRequest<User>

    //reg device
    fun registDevice(params: ParamsRegDevice): IPRNumberRequest<User>

    //terms list
    fun getActiveTermsAll(params: Map<String, String>): IPRNumberRequest<Terms>

    //not signe terms list
    fun getNotSignedActiveTermsAll(appType: String): IPRNumberRequest<Terms>

    //get session
    val session: IPRNumberRequest<User>

    //reload session
    fun reloadSession(): IPRNumberRequest<User>

    //find id
    fun getUserByVerification(params: Map<String, String>): IPRNumberRequest<User>

    //change password
    fun changePasswordByVerification(params: Map<String, String>): IPRNumberRequest<Any>

    //find user
    fun getUserByLoginIdAndMobile(params: Map<String, String>): IPRNumberRequest<User>
    fun getNoticeCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getNoticeList(params: Map<String, String>): IPRNumberRequest<Notice>
    fun getNotice(params: Map<String, String>): IPRNumberRequest<Notice>
    fun getFaqGroupAll(params: Map<String, String>): IPRNumberRequest<FaqGroup>
    fun getFaqCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getFaqList(params: Map<String, String>): IPRNumberRequest<Faq>
    fun getFaq(params: Map<String, String>): IPRNumberRequest<Faq>


    //updateProfileImage
    fun updateProfileImage(params: Map<String, String>): IPRNumberRequest<Any>

    //insert post
    fun insertPost(params: Post): IPRNumberRequest<Post>

    //verification request
    fun verification(params: Map<String, String>): IPRNumberRequest<Verification>

    //verification request
    fun confirmVerification(params: Map<String, String>): IPRNumberRequest<Any>

    //leave
    fun leave(params: Map<String, String>): IPRNumberRequest<Any>

    //cancel leave
    fun cancelLeave(params: Map<String, String>): IPRNumberRequest<Any>

    fun getPage(params: Map<String, String>): IPRNumberRequest<Page>

    //fan list
    fun getPlusList(params: Map<String, String>): IPRNumberRequest<Plus>

    //fan count
    fun getPlusCount(params: Map<String, String>): IPRNumberRequest<Int>

    //exclude fan list
    fun getExcludePlusList(params: Map<String, String>): IPRNumberRequest<Plus>

    //exclude fan count
    fun getExcludePlusCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun insertPlus(params: Plus): IPRNumberRequest<Plus>
    fun deletePlus(params: Map<String, String>): IPRNumberRequest<Any>
    fun deletePlusByPage(params: Map<String, String>): IPRNumberRequest<Any>
    fun existPlusByPage(params: Map<String, String>): IPRNumberRequest<Any>
    val countryConfigAll: IPRNumberRequest<CountryConfig>
    fun updateNickname(params: Map<String, String>): IPRNumberRequest<Any>
    fun agreeTermsList(params: Map<String, String>): IPRNumberRequest<Any>
    fun reporting(params: Report): IPRNumberRequest<Report>
    fun updateContactList(params: ParamsContact): IPRNumberRequest<Any>
    fun deleteContactList(params: ParamsContact): IPRNumberRequest<Any>
    val allFriendCount: IPRNumberRequest<Int>
    fun getAllFriendList(params: Map<String, String>): IPRNumberRequest<Friend>
    val friendCount: IPRNumberRequest<Int>
    fun getFriendPageCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getFriendPageList(params: Map<String, String>): IPRNumberRequest<Page>
    val userFriendCount: IPRNumberRequest<Int>
    fun getUserFriendList(params: Map<String, String>): IPRNumberRequest<Friend>
    fun updatePushConfig(params: Map<String, String>): IPRNumberRequest<Any>
    fun readComplete(params: Map<String, String>): IPRNumberRequest<Any>
    fun getBolHistoryList(params: Map<String, String>): IPRNumberRequest<Bol>
    fun getBolHistoryTotalAmount(params: Map<String, String>): IPRNumberRequest<String>
    fun cashExchange(params: Exchange): IPRNumberRequest<Any>
    val existsNicknameFriendCount: IPRNumberRequest<Int>
    fun getExistsNicknameFriendList(params: Map<String, String>): IPRNumberRequest<Friend>
    fun getExistsNicknameUserCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getExistsNicknameUserList(params: Map<String, String>): IPRNumberRequest<User>
    fun getUserCountByRecommendKey(params: Map<String, String>): IPRNumberRequest<Int>
    fun getUserListByRecommendKey(params: Map<String, String>): IPRNumberRequest<User>
    fun updateExternal(params: User): IPRNumberRequest<User>
    fun updateMobileByVerification(params: Map<String, String>): IPRNumberRequest<Any>
    fun getBolHistoryListWithTargetList(params: Map<String, String>): IPRNumberRequest<Bol>
    fun getBolHistoryCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getSameFriendAll(params: Map<String, String>): IPRNumberRequest<User>
    fun checkAuthCodeForUser(params: Map<String, String>): IPRNumberRequest<Int>
    fun getEventCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getEventList(params: Map<String, String>): IPRNumberRequest<Event>
    fun getMainBannerLottoList(params: Map<String, String>): IPRNumberRequest<Event>
    val eventGroupAll: IPRNumberRequest<EventGroup>
    fun getEventCountByGroup(params: Map<String, String>): IPRNumberRequest<Int>
    fun getEventListByGroup(params: Map<String, String>): IPRNumberRequest<Event>
    fun getEventByNumber(params: Map<String, String>): IPRNumberRequest<Event>
    fun joinEvent(params: Map<String, String>): IPRNumberRequest<EventResult>
    fun lottoJoin(params: Map<String, String>): IPRNumberRequest<EventResult>
    fun serializableJoinEvent(params: Map<String, String>): IPRNumberRequest<EventResult>
    fun checkJoinEnable(params: Map<String, String>): IPRNumberRequest<EventResult>
    fun joinWithPropertiesEvent(params: ParamsJoinEvent): IPRNumberRequest<EventResult>
    fun writeImpression(params: Map<String, String>): IPRNumberRequest<Any>
    fun getBannerAll(params: Map<String, String>): IPRNumberRequest<EventBanner>
    fun getGiftAll(params: Map<String, String>): IPRNumberRequest<EventGift>
    fun getWinCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getWinList(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun getEventJoinCount(params: Map<String, String>): IPRNumberRequest<Int>
    val winCountOnlyPresentByMemberSeqNo: IPRNumberRequest<Int>
    val winCountOnlyPresentToday: IPRNumberRequest<Int>
    fun getWinCountOnlyPresent(params: Map<String, String>): IPRNumberRequest<Int>
    fun getWinListOnlyPresent(params: Map<String, String>): IPRNumberRequest<EventWin>
    val myWinCountOnlyPresent: IPRNumberRequest<Int>
    fun getMyWinListOnlyPresent(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun getWinBySeqNo(params: Map<String, String>): IPRNumberRequest<EventWin>
    val userWinCount: IPRNumberRequest<Int>
    fun getUserWinList(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun existsEventResult(params: Map<String, String>): IPRNumberRequest<EventExist>
    fun getWinAll(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun getEvent(params: Map<String, String>): IPRNumberRequest<Event>
    fun getMyJoinCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getMyJoinCountAndBuyType(params: Map<String, String>): IPRNumberRequest<Int>
    fun getWinAnnounceCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getWinAnnounceList(params: Map<String, String>): IPRNumberRequest<Event>
    fun getBolHistoryWithTarget(params: Map<String, String>): IPRNumberRequest<Bol>
    fun postGoodsReview(params: GoodsReview): IPRNumberRequest<Any>
    fun putGoodsReview(params: GoodsReview): IPRNumberRequest<Any>
    fun postPurchaseShip(params: Purchase): IPRNumberRequest<Purchase>
    fun postOrderId(): IPRNumberRequest<String>
    fun userCheck(params: Map<String, String>): IPRNumberRequest<String>
    fun getLottoJoinList(params: Map<String, String>): IPRNumberRequest<EventJoinJpa>
    fun getLottoWinNumberList(params: Map<String, String>): IPRNumberRequest<LottoWinNumber>
    fun getLottoWinnerCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getLottoWinnerList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventWinJpa>>
    fun getLottoHistoryCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getLottoHistoryList(params: Map<String, String>): IPRNumberRequest<Event>
    fun cpeReport(params: Map<String, String>): IPRNumberRequest<Any>
    fun cpaReport(params: Map<String, String>): IPRNumberRequest<Any>
    fun updatePushKey(params: Map<String, String>): IPRNumberRequest<Any>
    fun updatePayPassword(params: Map<String, String>): IPRNumberRequest<Any>
    fun updatePayPasswordWithVerification(params: Map<String, String>): IPRNumberRequest<Any>
    fun checkPayPassword(params: Map<String, String>): IPRNumberRequest<Any>
    val cardList: IPRNumberRequest<Card>
    fun updateRepresentCard(params: Map<String, String>): IPRNumberRequest<Card>
    fun postCardRegister(params: CardReq): IPRNumberRequest<Card>
    fun deleteCard(params: Map<String, String>): IPRNumberRequest<Any>
    val shippingSiteList: IPRNumberRequest<ShippingSite>
    fun insertShippingSite(params: ShippingSite): IPRNumberRequest<ShippingSite>
    fun updateShippingSite(params: ShippingSite): IPRNumberRequest<ShippingSite>
    fun deleteShippingSite(params: Map<String, String>): IPRNumberRequest<Any>
    val categoryMajorOnly: IPRNumberRequest<CategoryMajor>
    val categoryMajor: IPRNumberRequest<CategoryMajor>
    val categoryFirstList: IPRNumberRequest<CategoryFirst>
    fun getJusoList(params: Map<String, String>): IPRNumberRequest<Juso>
    val doList: IPRNumberRequest<Province>
    val myFavoriteCategoryList: IPRNumberRequest<CategoryFavorite>
    fun insertCategoryFavorite(params: CategoryFavorite): IPRNumberRequest<CategoryFavorite>
    fun deleteCategoryFavorite(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateActiveArea1(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateActiveArea2(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateUseGift(params: Map<String, String>): IPRNumberRequest<Any>
    fun checkIslandsRegion(params: Map<String, String>): IPRNumberRequest<Boolean>
    fun getIsLandsRegion(params: Map<String, String>): IPRNumberRequest<IslandsRegion>
    fun updateGender(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateBirthday(params: Map<String, String>): IPRNumberRequest<Any>
    fun eventPayPoint(params: Map<String, String>): IPRNumberRequest<Any>
    fun insertEventReview(params: EventReview): IPRNumberRequest<Any>
    fun updateEventReview(params: EventReview): IPRNumberRequest<Any>
    fun getEventReviewList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReview>>
    fun getMyEventReviewList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReview>>
    fun getEventReview(params: Map<String, String>): IPRNumberRequest<EventReview>
    fun getProductPriceShipType(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListShipTypeByShoppingGroup(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPrice(params: Map<String, String>): IPRNumberRequest<ProductPrice>
    fun getCountProductReviewByProductPriceSeqNo(params: Map<String, String>): IPRNumberRequest<Int>
    val countProductReviewByyMemberSeqNo: IPRNumberRequest<Int>
    fun getProductLikeCheck(params: Map<String, String>): IPRNumberRequest<Any>
    fun insertProductLike(params: ProductLike): IPRNumberRequest<Any>
    fun deleteProductLike(params: ProductLike): IPRNumberRequest<Any>
    fun getProductReviewByProductPriceSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductReview>>
    fun getProductReviewByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductReview>>
    fun insertProductReview(params: ProductReview): IPRNumberRequest<Any>
    fun updateProductReview(params: ProductReview): IPRNumberRequest<Any>
    fun deleteProductReview(params: Map<String, String>): IPRNumberRequest<Any>
    fun getProductOption(params: Map<String, String>): IPRNumberRequest<ProductOptionTotal>
    val countProductLike: IPRNumberRequest<Int>
    fun getProductLikeShippingList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductLike>>
    fun getPurchaseProductListByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<PurchaseProduct>>
    val countPurchaseProductByMemberSeqNo: IPRNumberRequest<Int>
    fun getPurchaseProduct(params: Map<String, String>): IPRNumberRequest<PurchaseProduct>
    fun updatePurchaseProductComplete(params: Map<String, String>): IPRNumberRequest<Any>
    fun cancelPurchase(params: Map<String, String>): IPRNumberRequest<Any>
    fun getProductReviewCountGroupByEval(params: Map<String, String>): IPRNumberRequest<ProductReviewCountEval>
    fun getProductInfoByProductSeqNo(params: Map<String, String>): IPRNumberRequest<ProductInfo>
    fun getProductAuthByProductSeqNo(params: Map<String, String>): IPRNumberRequest<ProductAuth>
    fun getProductNoticeListByProductSeqNo(params: Map<String, String>): IPRNumberRequest<ProductNotice>
    fun getBusinessLicense(params: Map<String, String>): IPRNumberRequest<BusinessLicense>
    fun insertPointBuy(params: PointBuy): IPRNumberRequest<Any>
    fun getPointHistoryList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<PointHistory>>
    fun getPointHistory(params: Map<String, String>): IPRNumberRequest<PointHistory>
    fun exchangePointByBol(params: Map<String, String>): IPRNumberRequest<Any>
    fun insertEventReply(params: EventReply): IPRNumberRequest<Any>
    fun updateEventReply(params: EventReply): IPRNumberRequest<Any>
    fun getEventReplyListByEventReviewSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReply>>
    fun getEventReplyListByEventSeqNoAndEventWinSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReply>>
    fun getEventReplyListByEventWinId(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReply>>
    fun deleteEventReply(params: Map<String, String>): IPRNumberRequest<Any>
    val giftishowCategoryList: IPRNumberRequest<GiftishowCategory>
    fun getGiftishowList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Giftishow>>
    fun getGiftishowListByBrand(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Giftishow>>
    fun postGiftishowBuy(params: GiftishowBuy): IPRNumberRequest<Any>
    fun getGiftishowBuyList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<GiftishowBuy>>
    val giftishowBuyCount: IPRNumberRequest<Int>
    fun checkGiftishowStatus(params: Map<String, String>): IPRNumberRequest<String>
    fun resendGiftishowStatus(params: Map<String, String>): IPRNumberRequest<Any>
    fun postPurchaseFTLinkPay(params: FTLink): IPRNumberRequest<FTLink>
    fun postPurchaseBootPayVerify(params: Map<String, String>): IPRNumberRequest<Any>
    fun eventBuy(params: EventBuy): IPRNumberRequest<EventWinJpa>
    fun eventBuyList(params: EventBuy): IPRNumberRequest<EventResultJpa>
    fun adReward(): IPRNumberRequest<Int>
    fun checkAdRewardPossible(): IPRNumberRequest<AdRewardPossible>
    fun attendance(): IPRNumberRequest<MemberAttendance>
    val mobileCategoryList: IPRNumberRequest<MobileCategory>
    fun getMobileBrandList(params: Map<String, String>): IPRNumberRequest<MobileBrand>
    fun saveMemberAddress(params: MemberAddress): IPRNumberRequest<MemberAddress>
    val memberAddress: IPRNumberRequest<MemberAddress>
    fun getNotificationBoxList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<NotificationBox>>
    fun notificationBoxDelete(params: Map<String, String>): IPRNumberRequest<Any>
    fun getBuffWithdrawNotification(): IPRNumberRequest<NotificationBox>
    fun notificationBoxRead(params: Map<String, String>): IPRNumberRequest<Any>
    fun getPopupList(params: Map<String, String>): IPRNumberRequest<PopupMange>
    fun getBannerList(params: Map<String, String>): IPRNumberRequest<Banner>
    fun getContactListWithMember(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Contact>>
    fun getFriendList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Contact>>
    fun getContactMemberCount(): IPRNumberRequest<Int>
    fun getBuff(params: Map<String, String>): IPRNumberRequest<Buff>
    fun getBuffMember(): IPRNumberRequest<BuffMember>
    fun getBuffMemberList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffMember>>
    fun getBuffLogList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffDividedBolLog>>
    fun getRequestList(): IPRNumberRequest<BuffRequest>
    fun getRequestCount(): IPRNumberRequest<Int>
    fun buffMake(params: Buff): IPRNumberRequest<Any>
    fun buffInvite(params: BuffParam): IPRNumberRequest<Any>
    fun changeBuffRequest(params: Map<String, String>): IPRNumberRequest<BuffRequestResult>
    fun changeBuffOwner(params: Map<String, String>): IPRNumberRequest<Any>
    fun exitBuff(params: Map<String, String>): IPRNumberRequest<Any>
    fun forcedExitBuff(params: BuffParam): IPRNumberRequest<Any>
    fun getBuffPostList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffPost>>
    fun getBuffPostLikeList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffPostLike>>
    fun getBuffPostReplyList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffPostReply>>
    fun buffPostLike(params: Map<String, String>): IPRNumberRequest<String>
    fun insertBuffPostReply(params: Map<String, String>): IPRNumberRequest<Any>
    fun modifyBuffPostReply(params: Map<String, String>): IPRNumberRequest<Any>
    fun deleteBuffPostReply(params: Map<String, String>): IPRNumberRequest<Any>
    fun insertBuffPost(params: BuffPost): IPRNumberRequest<Any>
    fun updateBuffPost(params: BuffPost): IPRNumberRequest<Any>
    fun updateBuffPostPublic(params: Map<String, String>): IPRNumberRequest<Any>
    fun walletSignUp(params: Map<String, String>): IPRNumberRequest<String>
    fun walletSync(params: Map<String, String>): IPRNumberRequest<String>
    fun walletBalance(): IPRNumberRequest<Map<String, Any>>
    fun walletDuplicateUser(): IPRNumberRequest<String>
    fun getShoppingGroup(params: Map<String, String>): IPRNumberRequest<ShoppingGroup>
}