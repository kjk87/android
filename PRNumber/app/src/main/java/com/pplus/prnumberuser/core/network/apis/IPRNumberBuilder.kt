package com.pplus.prnumberuser.core.network.apis

import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.request.params.*
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.network.prnumber.IPRNumberRequest

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
    fun getActiveTermsAll(appType: String): IPRNumberRequest<Terms>

    //not signe terms list
    fun getNotSignedActiveTermsAll(appType: String): IPRNumberRequest<Terms>

    //activate page
    fun activatePage(params: User): IPRNumberRequest<User>

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

    //get page
    fun updatePageProfileImage(params: Map<String, String>): IPRNumberRequest<Any>
    fun updatePageBackgroundImage(params: Map<String, String>): IPRNumberRequest<Any>

    //introduce image reg
    fun updateIntroImageList(params: ParamsIntroImage): IPRNumberRequest<Page>

    //introduce image list
    fun getIntroImageAll(params: Map<String, String>): IPRNumberRequest<Attachment>
    fun getPageImageAll(params: Map<String, String>): IPRNumberRequest<PageImage>
    fun getCoordByAddress(params: Map<String, String>): IPRNumberRequest<Coord>
    fun getNoticeCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getNoticeList(params: Map<String, String>): IPRNumberRequest<Notice>
    fun getNotice(params: Map<String, String>): IPRNumberRequest<Notice>
    fun getFaqGroupAll(params: Map<String, String>): IPRNumberRequest<FaqGroup>
    fun getFaqCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getFaqList(params: Map<String, String>): IPRNumberRequest<Faq>
    fun getFaq(params: Map<String, String>): IPRNumberRequest<Faq>

    //getMe
    val me: IPRNumberRequest<User>

    //updateProfileImage
    fun updateProfileImage(params: Map<String, String>): IPRNumberRequest<Any>

    //get post count
    fun getBoardPostCount(params: Map<String, String>): IPRNumberRequest<Int>

    //get post list
    fun getBoardPostList(params: Map<String, String>): IPRNumberRequest<Post>

    //get category list
    fun getCategoryAll(params: Map<String, String>): IPRNumberRequest<Category>
    fun getCategoryList(params: Map<String, String>): IPRNumberRequest<Category>

    //activate page
    fun updatePage(params: Page): IPRNumberRequest<Page>

    //get post
    fun getPost(params: Long): IPRNumberRequest<Post>

    //insert post
    fun insertPost(params: Post): IPRNumberRequest<Post>

    //update post
    fun updatePost(params: Post): IPRNumberRequest<Post>

    //delete post
    fun deletePost(params: Long): IPRNumberRequest<Any>

    //get comment list
    fun getCommentAll(params: Long): IPRNumberRequest<Comment>

    //insert comment
    fun insertComment(params: Comment): IPRNumberRequest<Comment>

    //update comment
    fun updateComment(params: Comment): IPRNumberRequest<Comment>

    //delete comment
    fun deleteComment(params: Long): IPRNumberRequest<Any>

    //copy attachment
    fun copyAttachment(params: Map<String, String>): IPRNumberRequest<Attachment>

    //get default iage
    val defaultImageList: IPRNumberRequest<Attachment>
    fun getSnsLinkAll(params: Map<String, String>): IPRNumberRequest<Sns>
    fun saveSnsLink(params: Sns): IPRNumberRequest<Sns>
    fun deleteSnsLinkByType(params: Map<String, String>): IPRNumberRequest<Any>

    //verification request
    fun verification(params: Map<String, String>): IPRNumberRequest<Verification>

    //verification request
    fun confirmVerification(params: Map<String, String>): IPRNumberRequest<Any>

    //leave
    fun leave(params: Map<String, String>): IPRNumberRequest<Any>

    //cancel leave
    fun cancelLeave(params: Map<String, String>): IPRNumberRequest<Any>

    //cash history
    fun cashHistoryList(params: Map<String, String>): IPRNumberRequest<Cash>
    fun getCashHistoryTotalAmount(params: Map<String, String>): IPRNumberRequest<String>

    //get prefix
    val prefixNumber: IPRNumberRequest<String>
    //insert sms
    fun insertSmsMsg(params: Msg): IPRNumberRequest<Msg>

    //insert sms
    fun insertPushMsg(params: Msg): IPRNumberRequest<Msg>

    //insert savedMsg
    fun insertSavedMsg(params: SavedMsg): IPRNumberRequest<SavedMsg>

    //delete savedMsg
    fun deleteSavedMsg(params: Map<String, String>): IPRNumberRequest<Any>

    //savedMsg count
    val savedMsgCount: IPRNumberRequest<Int>

    ///savedMsg list
    fun getSavedMsgList(params: Map<String, String>): IPRNumberRequest<SavedMsg>

    //getReservedMsgAll
    fun getReservedMsgAll(params: Map<String, String>): IPRNumberRequest<Msg>
    fun getPage(params: Map<String, String>): IPRNumberRequest<Page>
    fun getPage2(params: Map<String, String>): IPRNumberRequest<Page2>

    //insert group
    fun insertPlusGroup(params: Group): IPRNumberRequest<Any>

    //update group name
    fun updatePlusGroupName(params: Map<String, String>): IPRNumberRequest<Any>

    //update priority
    fun updatePlusGroupPriorityAll(params: ParamsGroupPriority): IPRNumberRequest<Any>

    //delete group
    fun deletePlusGroup(params: Map<String, String>): IPRNumberRequest<Any>

    //group list
    val plusGroupAll: IPRNumberRequest<Group>

    //add fan to group
    fun addPlusListToGroup(params: ParamsPlusGroup): IPRNumberRequest<Any>

    //remove fan to group
    fun removePlusListToGroup(params: ParamsPlusGroup): IPRNumberRequest<Any>

    //fan list
    fun getPlusList(params: Map<String, String>): IPRNumberRequest<Plus>
    fun getOnlyPlus(params: Map<String, String>): IPRNumberRequest<Plus>

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
    fun getPageCountByTheme(params: Map<String, String>): IPRNumberRequest<Int>
    fun getPageListByTheme(params: Map<String, String>): IPRNumberRequest<Page>
    fun getPageCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getPageList(params: Map<String, String>): IPRNumberRequest<Page>
    fun getPageCountByManageSeqNo(params: Map<String, String>): IPRNumberRequest<Int>
    fun getPageListByManageSeqNo(params: Map<String, String>): IPRNumberRequest<Page>
    val countryConfigAll: IPRNumberRequest<CountryConfig>
    fun updateNickname(params: Map<String, String>): IPRNumberRequest<Any>
    fun agreeTermsList(params: Map<String, String>): IPRNumberRequest<Any>
    fun reporting(params: Report): IPRNumberRequest<Report>
    fun updateContactList(params: ParamsContact): IPRNumberRequest<Any>
    fun deleteContactList(params: ParamsContact): IPRNumberRequest<Any>
    val allFriendCount: IPRNumberRequest<Int>
    fun getAllFriendList(params: Map<String, String>): IPRNumberRequest<Friend>
    val friendCount: IPRNumberRequest<Int>
    fun getFriendList(params: Map<String, String>): IPRNumberRequest<Friend>
    fun getFriendPageCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getFriendPageList(params: Map<String, String>): IPRNumberRequest<Page>
    val userFriendCount: IPRNumberRequest<Int>
    fun getUserFriendList(params: Map<String, String>): IPRNumberRequest<Friend>
    fun updatePushConfig(params: Map<String, String>): IPRNumberRequest<Any>
    fun readComplete(params: Map<String, String>): IPRNumberRequest<Any>
    val msgCountInBox: IPRNumberRequest<Int>
    fun getMsgListInBox(params: Map<String, String>): IPRNumberRequest<Msg>
    fun deleteMsgInBox(params: Map<String, String>): IPRNumberRequest<Any>
    fun getPageByNumber(params: Map<String, String>): IPRNumberRequest<Page>
    fun getPageCountByArea(params: Map<String, String>): IPRNumberRequest<Int>
    fun getPageListByArea(params: Map<String, String>): IPRNumberRequest<Page>
    fun getPageListByAreaAndManageSeqNo(params: Map<String, String>): IPRNumberRequest<Page>
    fun getPageCountByAreaByTheme(params: Map<String, String>): IPRNumberRequest<Int>
    fun getPageListByAreaByTheme(params: Map<String, String>): IPRNumberRequest<Page>
    fun getBolHistoryList(params: Map<String, String>): IPRNumberRequest<Bol>
    fun bolExchange(params: Exchange): IPRNumberRequest<Any>
    val existsNicknameFriendCount: IPRNumberRequest<Int>
    fun getExistsNicknameFriendList(params: Map<String, String>): IPRNumberRequest<Friend>
    fun getIntroMovieAll(params: Map<String, String>): IPRNumberRequest<ImgUrl>
    fun getExistsNicknameUserCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getExistsNicknameUserList(params: Map<String, String>): IPRNumberRequest<User>
    fun getUserCountByRecommendKey(params: Map<String, String>): IPRNumberRequest<Int>
    fun getUserListByRecommendKey(params: Map<String, String>): IPRNumberRequest<User>
    fun updateUser(params: User): IPRNumberRequest<User>
    fun updateExternal(params: User): IPRNumberRequest<User>
    fun updateMobileByVerification(params: Map<String, String>): IPRNumberRequest<Any>
    fun getBolHistoryListWithTargetList(params: Map<String, String>): IPRNumberRequest<Bol>
    fun getBolHistoryCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getSameFriendAll(params: Map<String, String>): IPRNumberRequest<User>
    fun checkAuthCodeForUser(params: Map<String, String>): IPRNumberRequest<Int>
    fun getEventCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getEventList(params: Map<String, String>): IPRNumberRequest<Event>
    fun getEventCountByPageSeqNo(params: Map<String, String>): IPRNumberRequest<Int>
    fun getEventListByPageSeqNo(params: Map<String, String>): IPRNumberRequest<Event>
    fun getActiveEventByPageSeqNo(params: Map<String, String>): IPRNumberRequest<Event>
    val eventGroupAll: IPRNumberRequest<EventGroup>
    fun getEventCountByGroup(params: Map<String, String>): IPRNumberRequest<Int>
    fun getEventListByGroup(params: Map<String, String>): IPRNumberRequest<Event>
    fun getEventByNumber(params: Map<String, String>): IPRNumberRequest<Event>
    fun joinEvent(params: Map<String, String>): IPRNumberRequest<EventResult>
    fun serializableJoinEvent(params: Map<String, String>): IPRNumberRequest<EventResult>
    fun joinWithPropertiesEvent(params: ParamsJoinEvent): IPRNumberRequest<EventResult>
    fun writeImpression(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateEventWinAddress(params: EventWin): IPRNumberRequest<Any>
    fun getBannerAll(params: Map<String, String>): IPRNumberRequest<EventBanner>
    fun getGiftAll(params: Map<String, String>): IPRNumberRequest<EventGift>
    fun getWinCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getWinList(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun getEventWinCountByGiftSeqNo(params: Map<String, String>): IPRNumberRequest<Int>
    fun getEventWinListByGiftSeqNo(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun getWinBySeqNo(params: Map<String, String>): IPRNumberRequest<EventWin>
    val userWinCount: IPRNumberRequest<Int>
    fun getUserWinList(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun getInviteRanking(params: Map<String, String>): IPRNumberRequest<User>
    fun getRewardRanking(params: Map<String, String>): IPRNumberRequest<User>
    val myInviteRanking: IPRNumberRequest<User>
    val myRewardRanking: IPRNumberRequest<User>
    fun existsEventResult(params: Map<String, String>): IPRNumberRequest<EventExist>
    fun getWinAll(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun getEvent(params: Map<String, String>): IPRNumberRequest<Event>
    fun getEventByCode(params: Map<String, String>): IPRNumberRequest<Event>
    fun getWinAnnounceCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getWinAnnounceList(params: Map<String, String>): IPRNumberRequest<Event>
    fun getBolHistoryWithTarget(params: Map<String, String>): IPRNumberRequest<Bol>
    fun postGoodsReview(params: GoodsReview): IPRNumberRequest<Any>
    fun putGoodsReview(params: GoodsReview): IPRNumberRequest<Any>
    fun getGoodsReview(params: Map<String, String>): IPRNumberRequest<SubResultResponse<GoodsReview>>
    fun deleteGoodsReview(params: Map<String, String>): IPRNumberRequest<Any>
    fun getPageLisWithGoods(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getPageEval(params: Map<String, String>): IPRNumberRequest<PageEval>
    fun postBuy(params: Buy): IPRNumberRequest<Buy>
    fun postBuyHot(params: Buy): IPRNumberRequest<Buy>
    fun postBuyShop(params: Buy): IPRNumberRequest<Buy>
    fun postBuyShip(params: Buy): IPRNumberRequest<Buy>
    fun postBuyQr(params: Buy): IPRNumberRequest<Buy>
    fun postBuyLpng(params: Lpng): IPRNumberRequest<Lpng>
    fun postBuyFTLinkPay(params: FTLink): IPRNumberRequest<FTLink>
    fun deleteBuy(params: Map<String, String>): IPRNumberRequest<Any>
    fun getOneBuy(params: Map<String, String>): IPRNumberRequest<Buy>
    fun getOneBuyDetail(params: Map<String, String>): IPRNumberRequest<Buy>
    fun getBuy(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Buy>>
    val buyOrderId: IPRNumberRequest<String>
    val lpngOrderId: IPRNumberRequest<String>
    fun getBuyGoods(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuyGoods>>
    fun getOneBuyGoods(params: Map<String, String>): IPRNumberRequest<BuyGoods>
    fun getGoodsReviewCount(params: Map<String, String>): IPRNumberRequest<Count>
    fun getBuyCountByGoodsSeqNo(params: Map<String, String>): IPRNumberRequest<Count>
    fun getGoodsLikeCount(params: Map<String, String>): IPRNumberRequest<Count>
    fun postGoodsLike(params: GoodsLike): IPRNumberRequest<GoodsLike>
    fun deleteGoodsLike(params: GoodsLike): IPRNumberRequest<Any>
    fun getGoodsLikeOne(params: Map<String, String>): IPRNumberRequest<GoodsLike>
    fun getGoodsLike(params: Map<String, String>): IPRNumberRequest<SubResultResponse<GoodsLike>>
    fun getGoodsLikeBySalesType(params: Map<String, String>): IPRNumberRequest<SubResultResponse<GoodsLike>>
    fun putSnsAccount(params: Map<String, String>): IPRNumberRequest<Any>
    fun userCheck(params: Map<String, String>): IPRNumberRequest<String>
    fun getPageGoodsCategory(params: Map<String, String>): IPRNumberRequest<PageGoodsCategory>
    fun getPageManagement(params: Map<String, String>): IPRNumberRequest<PageManagement>
    val lotto: IPRNumberRequest<Lotto>
    fun getLottoPlayGiftList(params: Map<String, String>): IPRNumberRequest<LottoGift>
    fun getLottoWinnerCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getLottoWinnerList(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun getLottoCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getLottoList(params: Map<String, String>): IPRNumberRequest<Event>
    fun getTicketHistoryCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getTicketHistory(params: Map<String, String>): IPRNumberRequest<Bol>
    fun getLottoTicketHistoryCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getLottoTicketHistory(params: Map<String, String>): IPRNumberRequest<Bol>
    fun getLottoResult(params: Map<String, String>): IPRNumberRequest<EventWin>
    fun getLottoJoinCont(params: Map<String, String>): IPRNumberRequest<Int>
    fun getLottoJoinList(params: Map<String, String>): IPRNumberRequest<Event>
    val randomPrnumber: IPRNumberRequest<String>
    val hashTagList: IPRNumberRequest<HashTag>
    fun cpeReport(params: Map<String, String>): IPRNumberRequest<Any>
    fun cpaReport(params: Map<String, String>): IPRNumberRequest<Any>
    fun updatePushKey(params: Map<String, String>): IPRNumberRequest<Any>
    fun checkBootPay(params: Map<String, String>): IPRNumberRequest<Buy>
    fun getPlusLisWithPlusGoods(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Plus2>>
    fun getPlusListWithNews(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Plus2>>
    fun getGoodsListByPageSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Goods>>
    fun updatePayPassword(params: Map<String, String>): IPRNumberRequest<Any>
    fun updatePayPasswordWithVerification(params: Map<String, String>): IPRNumberRequest<Any>
    fun checkPayPassword(params: Map<String, String>): IPRNumberRequest<Any>
    val cardList: IPRNumberRequest<Card>
    fun updateRepresentCard(params: Map<String, String>): IPRNumberRequest<Card>
    fun postCardRegister(params: CardReq): IPRNumberRequest<Card>
    fun deleteCard(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateBuyPlusTerms(params: Map<String, String>): IPRNumberRequest<Any>
    fun updatePlusPush(params: Map<String, String>): IPRNumberRequest<Any>
    fun updatePushActivate(params: Map<String, String>): IPRNumberRequest<Any>
    fun updatePlusGift(params: Map<String, String>): IPRNumberRequest<Any>
    val shippingSiteList: IPRNumberRequest<ShippingSite>
    fun insertShippingSite(params: ShippingSite): IPRNumberRequest<ShippingSite>
    fun updateShippingSite(params: ShippingSite): IPRNumberRequest<ShippingSite>
    fun deleteShippingSite(params: Map<String, String>): IPRNumberRequest<Any>
    val categoryMajorOnlyList: IPRNumberRequest<CategoryMajor>
    fun getCategoryMajorOnly(params: Map<String, String>): IPRNumberRequest<CategoryMajor>
    val categoryMajor: IPRNumberRequest<CategoryMajor>
    fun getCategoryMinorList(params: Map<String, String>): IPRNumberRequest<CategoryMinor>
    fun getGoodsImageList(params: Map<String, String>): IPRNumberRequest<GoodsImage>
    val buyCountAll: IPRNumberRequest<Int>
    val goodsReviewCountAll: IPRNumberRequest<Int>
    fun getJusoList(params: Map<String, String>): IPRNumberRequest<Juso>
    val doList: IPRNumberRequest<Province>
    val myFavoriteCategoryList: IPRNumberRequest<CategoryFavorite>
    fun insertCategoryFavorite(params: CategoryFavorite): IPRNumberRequest<CategoryFavorite>
    fun deleteCategoryFavorite(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateActiveArea1(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateActiveArea2(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateQrImage(params: Map<String, String>): IPRNumberRequest<Any>
    fun updateUseGift(params: Map<String, String>): IPRNumberRequest<Any>
    fun checkIslandsRegion(params: Map<String, String>): IPRNumberRequest<Boolean>
    fun getIsLandsRegion(params: Map<String, String>): IPRNumberRequest<IslandsRegion>
    val categoryFirstList: IPRNumberRequest<CategoryFirst>
    fun getBusinessLicense(params: Map<String, String>): IPRNumberRequest<BusinessLicense>
    fun getGoodsPriceListShipTypeByPageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<SubResultResponse<GoodsPrice>>
    fun getProductPriceShipType(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListShipTypeByPageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListStoreTypeByPageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListStoreTypeByPageSeqNoAndDiscountOnlyNormal(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListByIsSubscriptionAndIsPrepaymentOnlyNormal(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListShipTypeByManageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListTicketTypeByManageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListShipTypeByPageAndDiscount(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListStoreTypeByPageAndDiscountDistanceDesc(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getPlusSubscriptionTypeOnlyNormalOrderByDistance(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getSubscriptionTypeByPageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getProductPriceListMoneyTypeByPageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>>
    fun getLastSubscriptionTypeByPageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<ProductPrice>
    fun getLastMoneyTypeByPageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<ProductPrice>
    fun getProductPrice(params: Map<String, String>): IPRNumberRequest<ProductPrice>
    fun getMainProductPrice(params: Map<String, String>): IPRNumberRequest<ProductPrice>
    fun getCountProductReviewByProductPriceSeqNo(params: Map<String, String>): IPRNumberRequest<Int>
    val countProductReviewByyMemberSeqNo: IPRNumberRequest<Int>
    fun getCountProductReviewByPageSeqNo(params: Map<String, String>): IPRNumberRequest<Int>
    fun getProductLikeCheck(params: Map<String, String>): IPRNumberRequest<Any>
    fun insertProductLike(params: ProductLike): IPRNumberRequest<Any>
    fun deleteProductLike(params: ProductLike): IPRNumberRequest<Any>
    fun getProductReviewByProductPriceSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductReview>>
    fun getProductReviewByPageSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductReview>>
    fun getLastProductReviewByPageSeqNo(params: Map<String, String>): IPRNumberRequest<ProductReview>
    fun getProductReviewByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductReview>>
    fun insertProductReview(params: ProductReview): IPRNumberRequest<Any>
    fun updateProductReview(params: ProductReview): IPRNumberRequest<Any>
    fun deleteProductReview(params: Map<String, String>): IPRNumberRequest<Any>
    fun getProductOption(params: Map<String, String>): IPRNumberRequest<ProductOptionTotal>
    val countProductLike: IPRNumberRequest<Int>
    fun getProductLikeShippingList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductLike>>
    fun getPurchaseProductListByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<PurchaseProduct>>
    fun getPurchaseProductListTicketTypeByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<PurchaseProduct>>
    val countPurchaseProductByMemberSeqNo: IPRNumberRequest<Int>
    fun getPurchaseProduct(params: Map<String, String>): IPRNumberRequest<PurchaseProduct>
    fun updatePurchaseProductComplete(params: Map<String, String>): IPRNumberRequest<Any>
    fun cancelPurchase(params: Map<String, String>): IPRNumberRequest<Any>
    fun getProductReviewCountGroupByEval(params: Map<String, String>): IPRNumberRequest<ReviewCountEval>
    fun getProductReviewCountGroupByEvalByPageSeqNo(params: Map<String, String>): IPRNumberRequest<ReviewCountEval>
    fun getProductInfoByProductSeqNo(params: Map<String, String>): IPRNumberRequest<ProductInfo>
    fun getProductAuthByProductSeqNo(params: Map<String, String>): IPRNumberRequest<ProductAuth>
    fun getProductNoticeListByProductSeqNo(params: Map<String, String>): IPRNumberRequest<ProductNotice>
    fun postOrderId(): IPRNumberRequest<String>
    fun postPurchaseShip(params: Purchase): IPRNumberRequest<Purchase>
    fun postPurchaseTicket(params: Purchase): IPRNumberRequest<Purchase>
    fun postPurchaseFTLinkPay(params: FTLink): IPRNumberRequest<FTLink>
    fun postPurchaseBootPayVerify(params: Map<String, String>): IPRNumberRequest<Any>
    fun getGiftishowList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Giftishow>>
    fun getGiftishowListByBrand(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Giftishow>>
    fun postGiftishowBuy(params: GiftishowBuy): IPRNumberRequest<Any>
    fun getGiftishowBuyList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<GiftishowBuy>>
    val giftishowBuyCount: IPRNumberRequest<Int>
    fun checkGiftishowStatus(params: Map<String, String>): IPRNumberRequest<String>
    fun resendGiftishowStatus(params: Map<String, String>): IPRNumberRequest<Any>
    val mobileCategoryList: IPRNumberRequest<MobileCategory>
    fun getMobileBrandList(params: Map<String, String>): IPRNumberRequest<MobileBrand>
    fun insertChat(params: ChatData): IPRNumberRequest<Any>
    val themeCategoryList: IPRNumberRequest<ThemeCategory>
    fun attendance(): IPRNumberRequest<MemberAttendance>
    fun getCountByPageSeqNoOnlyNormal(params: Map<String, String>): IPRNumberRequest<Int>
    fun getNewsListByPageSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<News>>
    fun getPlusNewsList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<News>>
    fun getNews(params: Map<String, String>): IPRNumberRequest<News>
    fun getNewsReviewList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<NewsReview>>
    fun insertNewsReview(params: NewsReview): IPRNumberRequest<Any>
    fun updateNewsReview(params: NewsReview): IPRNumberRequest<Any>
    fun deleteNewsReview(params: Map<String, String>): IPRNumberRequest<Any>
    fun getNewsCountByPageSeqNo(params: Map<String, String>): IPRNumberRequest<Int>
    fun saveMemberAddress(params: MemberAddress): IPRNumberRequest<MemberAddress>
    val memberAddress: IPRNumberRequest<MemberAddress>
    fun getEventPolicyList(params: Map<String, String>): IPRNumberRequest<EventPolicy>
    fun getVirtualNumberManage(params: Map<String, String>): IPRNumberRequest<VirtualNumberManage>
    fun getNbookVirtualNumberManageList(): IPRNumberRequest<VirtualNumberManage>
    fun getEventDetailList(params: Map<String, String>): IPRNumberRequest<EventDetail>
    fun getEventDetailImageList(params: Map<String, String>): IPRNumberRequest<EventDetailImage>
    fun getEventDetailItemList(params: Map<String, String>): IPRNumberRequest<EventDetailItem>
    fun getPageListWithProductPrice(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getPageListWithSubscription(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getOrderPageList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun makeQrCode(params: Map<String, String>): IPRNumberRequest<String>
    fun postVisitorGivePoint(params: VisitorPointGiveHistory): IPRNumberRequest<Any>
    fun postVisitorGivePointWithStamp(params: VisitorPointGiveHistory): IPRNumberRequest<Any>
    fun getFirstBenefit(params: Map<String, String>): IPRNumberRequest<VisitorPointGiveHistory>
    fun getSubscriptionDownloadCountByMemberSeqNoAndStatus(): IPRNumberRequest<Int>
    fun subscriptionDownload(params: Map<String, String>): IPRNumberRequest<SubscriptionDownload>
    fun subscriptionDownloadWithStamp(params: Map<String, String>): IPRNumberRequest<SubscriptionDownload>
    fun getSubscriptionDownloadListByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<SubscriptionDownload>>
    fun getSubscriptionDownloadBySeqNo(params: Map<String, String>): IPRNumberRequest<SubscriptionDownload>
    fun getSubscriptionLogListBySubscriptionDownloadSeqNo(params: Map<String, String>): IPRNumberRequest<SubscriptionLog>
    fun subscriptionUse(params: Map<String, String>): IPRNumberRequest<Any>
    fun subscriptionUseWithStamp(params: Map<String, String>): IPRNumberRequest<Any>
    fun pageAttendanceSaveAndGet(params: Map<String, String>): IPRNumberRequest<PageAttendance>
    fun pageAttendanceWithStamp(params: Map<String, String>): IPRNumberRequest<PageAttendance>
    fun getPageAttendanceLogList(params: Map<String, String>): IPRNumberRequest<PageAttendanceLog>
    fun getDeliveryPageList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getDeliveryPageListByKeyword(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getVisitPageList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getVisitPageListByKeyword(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getVisitPageListByArea(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getServicePageList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getOrderMenuGroupWithMenuList(params: Map<String, String>): IPRNumberRequest<OrderMenuGroup>
    fun getDelegateOrderMenuList(params: Map<String, String>): IPRNumberRequest<OrderMenu>
    fun getMenu(params: Map<String, String>): IPRNumberRequest<OrderMenu>
    fun getCartCount(params: Map<String, String>): IPRNumberRequest<Int>
    fun getCartList(params: Map<String, String>): IPRNumberRequest<Cart>
    fun checkCartPage(params: Map<String, String>): IPRNumberRequest<Boolean>
    fun saveCart(params: Cart): IPRNumberRequest<Any>
    fun updateAmount(params: Map<String, String>): IPRNumberRequest<Any>
    fun deleteCart(params: Map<String, String>): IPRNumberRequest<Any>
    fun orderPurchase(params: OrderPurchase): IPRNumberRequest<OrderPurchase>
    fun orderPurchaseFTLinkPay(params: FTLink): IPRNumberRequest<FTLink>
    fun orderPurchaseVerifyBootPay(params: Map<String, String>): IPRNumberRequest<Any>
    fun getOrderPurchaseListByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<OrderPurchase>>
    fun getTicketPurchaseListByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<OrderPurchase>>
    fun getOrderPurchase(params: Map<String, String>): IPRNumberRequest<OrderPurchase>
    fun insertReview(params: OrderMenuReview): IPRNumberRequest<Any>
    fun updateReview(params: OrderMenuReview): IPRNumberRequest<Any>
    fun deleteReview(params: Map<String, String>): IPRNumberRequest<Any>
    fun getReviewByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<OrderMenuReview>>
    fun getReviewByPageSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<OrderMenuReview>>
    fun getReviewCountGroupByEvalByPageSeqNo(params: Map<String, String>): IPRNumberRequest<ReviewCountEval>
    fun cancelOrderPurchaseUser(params: Map<String, String>): IPRNumberRequest<Any>
    fun getNotificationBoxList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<NotificationBox>>
    fun notificationBoxDelete(params: Map<String, String>): IPRNumberRequest<Any>
    fun locationServiceLogSave(params: Map<String, String>): IPRNumberRequest<Any>
    fun getPageListWithPrepayment(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getPageListWithPrepaymentExistVisitLog(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getPrepaymentRetentionCount(): IPRNumberRequest<Int>
    fun getPrepayment(params: Map<String, String>): IPRNumberRequest<Prepayment>
    fun prepaymentPublish(params: Map<String, String>): IPRNumberRequest<Any>
    fun prepaymentUse(params: Map<String, String>): IPRNumberRequest<Any>
    fun getPageListWithPageWithPrepaymentPublish(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Page2>>
    fun getPrepaymentPublish(params: Map<String, String>): IPRNumberRequest<PrepaymentPublish>
    fun getPrepaymentLogList(params: Map<String, String>): IPRNumberRequest<PrepaymentLog>
    fun getVisitLog(params: Map<String, String>): IPRNumberRequest<VisitLog>
    fun visitLogReceive(params: Map<String, String>): IPRNumberRequest<Any>
    fun useTicket(params: Map<String, String>): IPRNumberRequest<Any>
}