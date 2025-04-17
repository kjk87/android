package com.pplus.luckybol.core.network

import com.pplus.luckybol.core.network.apis.IPRNumberBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.request.params.ParamsContact
import com.pplus.luckybol.core.network.model.request.params.ParamsJoinEvent
import com.pplus.luckybol.core.network.model.request.params.ParamsRegDevice
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.network.prnumber.IPRNumberRequest
import com.pplus.luckybol.core.network.prnumber.PRNumberRequest

/**
 * Created by j2n on 2016. 9. 12..
 */
class ApiBuilder private constructor() : IPRNumberBuilder {
    private val TAG = ApiBuilder::class.java.simpleName

    //prnumber api
    override fun existsUser(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.existsUser(params))
    }

    override fun join(params: User): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.join(params))
    }

    override fun appVersion(params: Map<String, String>): IPRNumberRequest<AppVersion> {
        return PRNumberRequest.Builder<AppVersion>().setRequestCall(ApiController.pRNumberApi.appVersion(params))
    }

    override fun login(params: Map<String, String>): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.login(params))
    }

    override fun existsDevice(params: Map<String, String>): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.existsDevice(params))
    }

    override fun registDevice(params: ParamsRegDevice): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.registDevice(params))
    }

    override fun getActiveTermsAll(params: Map<String, String>): IPRNumberRequest<Terms> {
        return PRNumberRequest.Builder<Terms>().setRequestCall(ApiController.pRNumberApi.getActiveTermsAll(params))
    }

    override fun getNotSignedActiveTermsAll(appType: String): IPRNumberRequest<Terms> {
        return PRNumberRequest.Builder<Terms>().setRequestCall(ApiController.pRNumberApi.getNotSignedActiveTermsAll(appType))
    }

    override val session: IPRNumberRequest<User>
        get() = PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.session)

    override fun reloadSession(): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.requestReloadSession())
    }

    override fun getUserByVerification(params: Map<String, String>): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.getUserByVerification(params))
    }

    override fun changePasswordByVerification(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.changePasswordByVerification(params))
    }

    override fun getUserByLoginIdAndMobile(params: Map<String, String>): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.getUserByLoginIdAndMobile(params))
    }

    override fun updateProfileImage(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateProfileImage(params))
    }

    override fun getNoticeCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getNoticeCount(params))
    }

    override fun getNoticeList(params: Map<String, String>): IPRNumberRequest<Notice> {
        return PRNumberRequest.Builder<Notice>().setRequestCall(ApiController.pRNumberApi.getNoticeList(params))
    }

    override fun getNotice(params: Map<String, String>): IPRNumberRequest<Notice> {
        return PRNumberRequest.Builder<Notice>().setRequestCall(ApiController.pRNumberApi.getNotice(params))
    }

    override fun getFaqGroupAll(params: Map<String, String>): IPRNumberRequest<FaqGroup> {
        return PRNumberRequest.Builder<FaqGroup>().setRequestCall(ApiController.pRNumberApi.getFaqGroupAll(params))
    }

    override fun getFaqCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getFaqCount(params))
    }

    override fun getFaqList(params: Map<String, String>): IPRNumberRequest<Faq> {
        return PRNumberRequest.Builder<Faq>().setRequestCall(ApiController.pRNumberApi.getFaqList(params))
    }

    override fun getFaq(params: Map<String, String>): IPRNumberRequest<Faq> {
        return PRNumberRequest.Builder<Faq>().setRequestCall(ApiController.pRNumberApi.getFaq(params))
    }

    override fun insertPost(params: Post): IPRNumberRequest<Post> {
        return PRNumberRequest.Builder<Post>().setRequestCall(ApiController.pRNumberApi.insertPost(params))
    }

    override fun verification(params: Map<String, String>): IPRNumberRequest<Verification> {
        return PRNumberRequest.Builder<Verification>().setRequestCall(ApiController.pRNumberApi.requestVerification(params))
    }

    override fun confirmVerification(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.confirmVerification(params))
    }

    override fun leave(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestLeave(params))
    }

    override fun cancelLeave(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestCancelLeave(params))
    }

    override fun getPage(params: Map<String, String>): IPRNumberRequest<Page> {
        return PRNumberRequest.Builder<Page>().setRequestCall(ApiController.pRNumberApi.getPage(params))
    }

    override fun getPlusList(params: Map<String, String>): IPRNumberRequest<Plus> {
        return PRNumberRequest.Builder<Plus>().setRequestCall(ApiController.pRNumberApi.getPlusList(params))
    }

    override fun getPlusCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getPlusCount(params))
    }

    override fun getExcludePlusList(params: Map<String, String>): IPRNumberRequest<Plus> {
        return PRNumberRequest.Builder<Plus>().setRequestCall(ApiController.pRNumberApi.getExcludePlusList(params))
    }

    override fun getExcludePlusCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getExcludePlusCount(params))
    }

    override fun insertPlus(params: Plus): IPRNumberRequest<Plus> {
        return PRNumberRequest.Builder<Plus>().setRequestCall(ApiController.pRNumberApi.requestInsertPlus(params))
    }

    override fun deletePlus(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestDeletePlus(params))
    }

    override fun deletePlusByPage(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestDeletePlusByPage(params))
    }

    override fun existPlusByPage(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestExistPlusByPage(params))
    }

    override val countryConfigAll: IPRNumberRequest<CountryConfig>
        get() = PRNumberRequest.Builder<CountryConfig>().setRequestCall(ApiController.pRNumberApi.countryConfigAll)

    override fun updateNickname(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateNickname(params))
    }

    override fun agreeTermsList(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.agreeTermsList(params))
    }

    override fun reporting(params: Report): IPRNumberRequest<Report> {
        return PRNumberRequest.Builder<Report>().setRequestCall(ApiController.pRNumberApi.requestReporting(params))
    }

    override fun updateContactList(params: ParamsContact): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateContactList(params))
    }

    override fun deleteContactList(params: ParamsContact): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestDeleteContactList(params))
    }

    override val friendCount: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.friendCount)

    override val allFriendCount: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.allFriendCount)

    override fun getAllFriendList(params: Map<String, String>): IPRNumberRequest<Friend> {
        return PRNumberRequest.Builder<Friend>().setRequestCall(ApiController.pRNumberApi.getAllFriendList(params))
    }

    override fun getFriendPageCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getFriendPageCount(params))
    }

    override fun getFriendPageList(params: Map<String, String>): IPRNumberRequest<Page> {
        return PRNumberRequest.Builder<Page>().setRequestCall(ApiController.pRNumberApi.getFriendPageList(params))
    }

    override fun updatePushConfig(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updatePushConfig(params))
    }

    override fun readComplete(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestReadComplete(params))
    }

    override fun getBolHistoryList(params: Map<String, String>): IPRNumberRequest<Bol> {
        return PRNumberRequest.Builder<Bol>().setRequestCall(ApiController.pRNumberApi.getBolHistoryList(params))
    }

    override fun getBolHistoryTotalAmount(params: Map<String, String>): IPRNumberRequest<String> {
        return PRNumberRequest.Builder<String>().setRequestCall(ApiController.pRNumberApi.getBolHistoryTotalAmount(params))
    }

    override fun cashExchange(params: Exchange): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.cashExchange(params))
    }

    override val existsNicknameFriendCount: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.existsNicknameFriendCount)

    override fun getExistsNicknameFriendList(params: Map<String, String>): IPRNumberRequest<Friend> {
        return PRNumberRequest.Builder<Friend>().setRequestCall(ApiController.pRNumberApi.getExistsNicknameFriendList(params))
    }

    override fun getExistsNicknameUserCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getExistsNicknameUserCount(params))
    }

    override fun getExistsNicknameUserList(params: Map<String, String>): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.getExistsNicknameUserList(params))
    }

    override fun updateExternal(params: User): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.updateExternal(params))
    }

    override fun updateMobileByVerification(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateMobileByVerification(params))
    }

    override fun getBolHistoryListWithTargetList(params: Map<String, String>): IPRNumberRequest<Bol> {
        return PRNumberRequest.Builder<Bol>().setRequestCall(ApiController.pRNumberApi.getBolHistoryWithTargetList(params))
    }

    override fun getBolHistoryCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getBolHistoryCount(params))
    }

    override fun getSameFriendAll(params: Map<String, String>): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.getSameFriendAll(params))
    }

    override fun checkAuthCodeForUser(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.requestCheckAuthCodeForUser(params))
    }

    override fun getEventCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getEventCount(params))
    }

    override fun getEventList(params: Map<String, String>): IPRNumberRequest<Event> {
        return PRNumberRequest.Builder<Event>().setRequestCall(ApiController.pRNumberApi.getEventList(params))
    }

    override fun getMainBannerLottoList(params: Map<String, String>): IPRNumberRequest<Event> {
        return PRNumberRequest.Builder<Event>().setRequestCall(ApiController.pRNumberApi.getMainBannerLottoList(params))
    }

    override fun getEventByNumber(params: Map<String, String>): IPRNumberRequest<Event> {
        return PRNumberRequest.Builder<Event>().setRequestCall(ApiController.pRNumberApi.getEventByNumber(params))
    }

    override fun joinEvent(params: Map<String, String>): IPRNumberRequest<EventResult> {
        return PRNumberRequest.Builder<EventResult>().setRequestCall(ApiController.pRNumberApi.joinEvent(params))
    }

    override fun lottoJoin(params: Map<String, String>): IPRNumberRequest<EventResult> {
        return PRNumberRequest.Builder<EventResult>().setRequestCall(ApiController.pRNumberApi.lottoJoin(params))
    }

    override fun serializableJoinEvent(params: Map<String, String>): IPRNumberRequest<EventResult> {
        return PRNumberRequest.Builder<EventResult>().setRequestCall(ApiController.pRNumberApi.serializableJoinEvent(params))
    }

    override fun checkJoinEnable(params: Map<String, String>): IPRNumberRequest<EventResult> {
        return PRNumberRequest.Builder<EventResult>().setRequestCall(ApiController.pRNumberApi.checkJoinEnable(params))
    }

    override fun writeImpression(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestWriteImpression(params))
    }

    override fun getBannerAll(params: Map<String, String>): IPRNumberRequest<EventBanner> {
        return PRNumberRequest.Builder<EventBanner>().setRequestCall(ApiController.pRNumberApi.getBannerAll(params))
    }

    override fun getGiftAll(params: Map<String, String>): IPRNumberRequest<EventGift> {
        return PRNumberRequest.Builder<EventGift>().setRequestCall(ApiController.pRNumberApi.getGiftAll(params))
    }

    override fun getWinCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getWinCount(params))
    }

    override fun getWinList(params: Map<String, String>): IPRNumberRequest<EventWin> {
        return PRNumberRequest.Builder<EventWin>().setRequestCall(ApiController.pRNumberApi.getWinList(params))
    }

    override fun getEventJoinCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getEventJoinCount(params))
    }

    override val winCountOnlyPresentByMemberSeqNo: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.winCountOnlyPresentByMemberSeqNo)
    override val winCountOnlyPresentToday: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.winCountOnlyPresentToday)

    override fun getWinCountOnlyPresent(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getWinCountOnlyPresent(params))
    }

    override fun getWinListOnlyPresent(params: Map<String, String>): IPRNumberRequest<EventWin> {
        return PRNumberRequest.Builder<EventWin>().setRequestCall(ApiController.pRNumberApi.getWinListOnlyPresent(params))
    }

    override val myWinCountOnlyPresent: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.myWinCountOnlyPresent)

    override fun getMyWinListOnlyPresent(params: Map<String, String>): IPRNumberRequest<EventWin> {
        return PRNumberRequest.Builder<EventWin>().setRequestCall(ApiController.pRNumberApi.getMyWinListOnlyPresent(params))
    }

    override fun getWinBySeqNo(params: Map<String, String>): IPRNumberRequest<EventWin> {
        return PRNumberRequest.Builder<EventWin>().setRequestCall(ApiController.pRNumberApi.getWinBySeqNo(params))
    }

    override val userWinCount: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.userWinCount)

    override fun getUserWinList(params: Map<String, String>): IPRNumberRequest<EventWin> {
        return PRNumberRequest.Builder<EventWin>().setRequestCall(ApiController.pRNumberApi.getUserWinList(params))
    }

    override fun existsEventResult(params: Map<String, String>): IPRNumberRequest<EventExist> {
        return PRNumberRequest.Builder<EventExist>().setRequestCall(ApiController.pRNumberApi.requestExistsEventResult(params))
    }

    override fun getWinAll(params: Map<String, String>): IPRNumberRequest<EventWin> {
        return PRNumberRequest.Builder<EventWin>().setRequestCall(ApiController.pRNumberApi.getWinAll(params))
    }

    override fun getEvent(params: Map<String, String>): IPRNumberRequest<Event> {
        return PRNumberRequest.Builder<Event>().setRequestCall(ApiController.pRNumberApi.getEvent(params))
    }

    override fun getMyJoinCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getMyJoinCount(params))
    }

    override fun getMyJoinCountAndBuyType(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getMyJoinCountAndBuyType(params))
    }

    override fun joinWithPropertiesEvent(params: ParamsJoinEvent): IPRNumberRequest<EventResult> {
        return PRNumberRequest.Builder<EventResult>().setRequestCall(ApiController.pRNumberApi.requestJoinWithPropertiesEvent(params))
    }

    override val eventGroupAll: IPRNumberRequest<EventGroup>
        get() = PRNumberRequest.Builder<EventGroup>().setRequestCall(ApiController.pRNumberApi.eventGroupAll)

    override fun getEventCountByGroup(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getEventCountByGroup(params))
    }

    override fun getEventListByGroup(params: Map<String, String>): IPRNumberRequest<Event> {
        return PRNumberRequest.Builder<Event>().setRequestCall(ApiController.pRNumberApi.getEventListByGroup(params))
    }

    override fun getWinAnnounceCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getWinAnnounceCount(params))
    }

    override fun getWinAnnounceList(params: Map<String, String>): IPRNumberRequest<Event> {
        return PRNumberRequest.Builder<Event>().setRequestCall(ApiController.pRNumberApi.getWinAnnounceList(params))
    }

    override fun getUserCountByRecommendKey(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getUserCountByRecommendKey(params))
    }

    override fun getUserListByRecommendKey(params: Map<String, String>): IPRNumberRequest<User> {
        return PRNumberRequest.Builder<User>().setRequestCall(ApiController.pRNumberApi.getUserListByRecommendKey(params))
    }

    override fun getBolHistoryWithTarget(params: Map<String, String>): IPRNumberRequest<Bol> {
        return PRNumberRequest.Builder<Bol>().setRequestCall(ApiController.pRNumberApi.getBolHistoryWithTarget(params))
    }

    override fun postGoodsReview(params: GoodsReview): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.postGoodsReview(params))
    }

    override fun putGoodsReview(params: GoodsReview): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.putGoodsReview(params))
    }

    override fun postPurchaseShip(params: Purchase): IPRNumberRequest<Purchase> {
        return PRNumberRequest.Builder<Purchase>().setRequestCall(ApiController.pRNumberApi.postPurchaseShip(params))
    }

    override fun postOrderId(): IPRNumberRequest<String> {
        return PRNumberRequest.Builder<String>().setRequestCall(ApiController.pRNumberApi.postOrderId())
    }

    override val userFriendCount: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.userFriendCount)

    override fun getUserFriendList(params: Map<String, String>): IPRNumberRequest<Friend> {
        return PRNumberRequest.Builder<Friend>().setRequestCall(ApiController.pRNumberApi.getUserFriendList(params))
    }

    override fun userCheck(params: Map<String, String>): IPRNumberRequest<String> {
        return PRNumberRequest.Builder<String>().setRequestCall(ApiController.pRNumberApi.userCheck(params))
    }

    override fun getLottoJoinList(params: Map<String, String>): IPRNumberRequest<EventJoinJpa> {
        return PRNumberRequest.Builder<EventJoinJpa>().setRequestCall(ApiController.pRNumberApi.getLottoJoinList(params))
    }

    override fun getLottoWinNumberList(params: Map<String, String>): IPRNumberRequest<LottoWinNumber> {
        return PRNumberRequest.Builder<LottoWinNumber>().setRequestCall(ApiController.pRNumberApi.getLottoWinNumberList(params))
    }

    override fun getLottoWinnerCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getLottoWinnerCount(params))
    }

    override fun getLottoWinnerList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventWinJpa>> {
        return PRNumberRequest.Builder<SubResultResponse<EventWinJpa>>().setRequestCall(ApiController.pRNumberApi.getLottoWinnerList(params))
    }

    override fun getLottoHistoryCount(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getLottoHistoryCount(params))
    }

    override fun getLottoHistoryList(params: Map<String, String>): IPRNumberRequest<Event> {
        return PRNumberRequest.Builder<Event>().setRequestCall(ApiController.pRNumberApi.getLottoHistoryList(params))
    }

    override fun cpeReport(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestCpeReport(params))
    }

    override fun cpaReport(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.requestCpaReport(params))
    }

    override fun updatePushKey(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updatePushKey(params))
    }

    override fun updatePayPassword(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updatePayPassword(params))
    }

    override fun updatePayPasswordWithVerification(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updatePayPasswordWithVerification(params))
    }

    override fun checkPayPassword(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.checkPayPassword(params))
    }

    override val cardList: IPRNumberRequest<Card>
        get() = PRNumberRequest.Builder<Card>().setRequestCall(ApiController.pRNumberApi.cardList)

    override fun updateRepresentCard(params: Map<String, String>): IPRNumberRequest<Card> {
        return PRNumberRequest.Builder<Card>().setRequestCall(ApiController.pRNumberApi.updateRepresentCard(params))
    }

    override fun postCardRegister(params: CardReq): IPRNumberRequest<Card> {
        return PRNumberRequest.Builder<Card>().setRequestCall(ApiController.pRNumberApi.postCardRegister(params))
    }

    override fun deleteCard(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.deleteCard(params))
    }

    override val shippingSiteList: IPRNumberRequest<ShippingSite>
        get() = PRNumberRequest.Builder<ShippingSite>().setRequestCall(ApiController.pRNumberApi.shippingSiteList)

    override fun insertShippingSite(params: ShippingSite): IPRNumberRequest<ShippingSite> {
        return PRNumberRequest.Builder<ShippingSite>().setRequestCall(ApiController.pRNumberApi.insertShippingSite(params))
    }

    override fun updateShippingSite(params: ShippingSite): IPRNumberRequest<ShippingSite> {
        return PRNumberRequest.Builder<ShippingSite>().setRequestCall(ApiController.pRNumberApi.updateShippingSite(params))
    }

    override fun deleteShippingSite(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.deleteShippingSite(params))
    }

    override val categoryMajorOnly: IPRNumberRequest<CategoryMajor>
        get() = PRNumberRequest.Builder<CategoryMajor>().setRequestCall(ApiController.pRNumberApi.categoryMajorOnly)
    override val categoryMajor: IPRNumberRequest<CategoryMajor>
        get() = PRNumberRequest.Builder<CategoryMajor>().setRequestCall(ApiController.pRNumberApi.categoryMajor)
    override val categoryFirstList: IPRNumberRequest<CategoryFirst>
        get() = PRNumberRequest.Builder<CategoryFirst>().setRequestCall(ApiController.pRNumberApi.categoryFirstList)

    override fun getJusoList(params: Map<String, String>): IPRNumberRequest<Juso> {
        return PRNumberRequest.Builder<Juso>().setRequestCall(ApiController.pRNumberApi.getJusoList(params))
    }

    override val doList: IPRNumberRequest<Province>
        get() = PRNumberRequest.Builder<Province>().setRequestCall(ApiController.pRNumberApi.doList)
    override val myFavoriteCategoryList: IPRNumberRequest<CategoryFavorite>
        get() = PRNumberRequest.Builder<CategoryFavorite>().setRequestCall(ApiController.pRNumberApi.myFavoriteCategoryList)

    override fun insertCategoryFavorite(params: CategoryFavorite): IPRNumberRequest<CategoryFavorite> {
        return PRNumberRequest.Builder<CategoryFavorite>().setRequestCall(ApiController.pRNumberApi.insertCategoryFavorite(params))
    }

    override fun deleteCategoryFavorite(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.deleteCategoryFavorite(params))
    }

    override fun updateActiveArea1(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateActiveArea1(params))
    }

    override fun updateActiveArea2(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateActiveArea2(params))
    }

    override fun updateUseGift(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateUseGift(params))
    }

    override fun checkIslandsRegion(params: Map<String, String>): IPRNumberRequest<Boolean> {
        return PRNumberRequest.Builder<Boolean>().setRequestCall(ApiController.pRNumberApi.checkIslandsRegion(params))
    }

    override fun getIsLandsRegion(params: Map<String, String>): IPRNumberRequest<IslandsRegion> {
        return PRNumberRequest.Builder<IslandsRegion>().setRequestCall(ApiController.pRNumberApi.getIsLandsRegion(params))
    }

    override fun updateGender(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateGender(params))
    }

    override fun updateBirthday(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateBirthday(params))
    }

    override fun eventPayPoint(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.eventPayPoint(params))
    }

    override fun insertEventReview(params: EventReview): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.insertEventReview(params))
    }

    override fun updateEventReview(params: EventReview): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateEventReview(params))
    }

    override fun getEventReviewList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReview>> {
        return PRNumberRequest.Builder<SubResultResponse<EventReview>>().setRequestCall(ApiController.pRNumberApi.getEventReviewList(params))
    }

    override fun getMyEventReviewList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReview>> {
        return PRNumberRequest.Builder<SubResultResponse<EventReview>>().setRequestCall(ApiController.pRNumberApi.getMyEventReviewList(params))
    }

    override fun getEventReview(params: Map<String, String>): IPRNumberRequest<EventReview> {
        return PRNumberRequest.Builder<EventReview>().setRequestCall(ApiController.pRNumberApi.getEventReview(params))
    }

    override fun getProductPriceShipType(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>> {
        return PRNumberRequest.Builder<SubResultResponse<ProductPrice>>().setRequestCall(ApiController.pRNumberApi.getProductPriceShipType(params))
    }

    override fun getProductPriceListShipTypeByShoppingGroup(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductPrice>> {
        return PRNumberRequest.Builder<SubResultResponse<ProductPrice>>().setRequestCall(ApiController.pRNumberApi.getProductPriceListShipTypeByShoppingGroup(params))
    }

    override fun getProductPrice(params: Map<String, String>): IPRNumberRequest<ProductPrice> {
        return PRNumberRequest.Builder<ProductPrice>().setRequestCall(ApiController.pRNumberApi.getProductPrice(params))
    }

    override fun getCountProductReviewByProductPriceSeqNo(params: Map<String, String>): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getCountProductReviewByProductPriceSeqNo(params))
    }

    override val countProductReviewByyMemberSeqNo: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.countProductReviewByyMemberSeqNo)

    override fun getProductLikeCheck(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.getProductLikeCheck(params))
    }

    override fun insertProductLike(params: ProductLike): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.insertProductLike(params))
    }

    override fun deleteProductLike(params: ProductLike): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.deleteProductLike(params))
    }

    override fun getProductReviewByProductPriceSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductReview>> {
        return PRNumberRequest.Builder<SubResultResponse<ProductReview>>().setRequestCall(ApiController.pRNumberApi.getProductReviewByProductPriceSeqNo(params))
    }

    override fun getProductReviewByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductReview>> {
        return PRNumberRequest.Builder<SubResultResponse<ProductReview>>().setRequestCall(ApiController.pRNumberApi.getProductReviewByMemberSeqNo(params))
    }

    override fun insertProductReview(params: ProductReview): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.insertProductReview(params))
    }

    override fun updateProductReview(params: ProductReview): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateProductReview(params))
    }

    override fun deleteProductReview(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.deleteProductReview(params))
    }

    override fun getProductOption(params: Map<String, String>): IPRNumberRequest<ProductOptionTotal> {
        return PRNumberRequest.Builder<ProductOptionTotal>().setRequestCall(ApiController.pRNumberApi.getProductOption(params))
    }

    override val countProductLike: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.countProductLike)

    override fun getProductLikeShippingList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<ProductLike>> {
        return PRNumberRequest.Builder<SubResultResponse<ProductLike>>().setRequestCall(ApiController.pRNumberApi.getProductLikeShippingList(params))
    }

    override fun getPurchaseProductListByMemberSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<PurchaseProduct>> {
        return PRNumberRequest.Builder<SubResultResponse<PurchaseProduct>>().setRequestCall(ApiController.pRNumberApi.getPurchaseProductListByMemberSeqNo(params))
    }

    override val countPurchaseProductByMemberSeqNo: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.countPurchaseProductByMemberSeqNo)

    override fun getPurchaseProduct(params: Map<String, String>): IPRNumberRequest<PurchaseProduct> {
        return PRNumberRequest.Builder<PurchaseProduct>().setRequestCall(ApiController.pRNumberApi.getPurchaseProduct(params))
    }

    override fun updatePurchaseProductComplete(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updatePurchaseProductComplete(params))
    }

    override fun cancelPurchase(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.cancelPurchase(params))
    }

    override fun getProductReviewCountGroupByEval(params: Map<String, String>): IPRNumberRequest<ProductReviewCountEval> {
        return PRNumberRequest.Builder<ProductReviewCountEval>().setRequestCall(ApiController.pRNumberApi.getProductReviewCountGroupByEval(params))
    }

    override fun getProductInfoByProductSeqNo(params: Map<String, String>): IPRNumberRequest<ProductInfo> {
        return PRNumberRequest.Builder<ProductInfo>().setRequestCall(ApiController.pRNumberApi.getProductInfoByProductSeqNo(params))
    }

    override fun getProductAuthByProductSeqNo(params: Map<String, String>): IPRNumberRequest<ProductAuth> {
        return PRNumberRequest.Builder<ProductAuth>().setRequestCall(ApiController.pRNumberApi.getProductAuthByProductSeqNo(params))
    }

    override fun getProductNoticeListByProductSeqNo(params: Map<String, String>): IPRNumberRequest<ProductNotice> {
        return PRNumberRequest.Builder<ProductNotice>().setRequestCall(ApiController.pRNumberApi.getProductNoticeListByProductSeqNo(params))
    }

    override fun getBusinessLicense(params: Map<String, String>): IPRNumberRequest<BusinessLicense> {
        return PRNumberRequest.Builder<BusinessLicense>().setRequestCall(ApiController.pRNumberApi.getBusinessLicense(params))
    }

    override fun insertPointBuy(params: PointBuy): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.insertPointBuy(params))
    }

    override fun getPointHistoryList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<PointHistory>> {
        return PRNumberRequest.Builder<SubResultResponse<PointHistory>>().setRequestCall(ApiController.pRNumberApi.getPointHistoryList(params))
    }

    override fun getPointHistory(params: Map<String, String>): IPRNumberRequest<PointHistory> {
        return PRNumberRequest.Builder<PointHistory>().setRequestCall(ApiController.pRNumberApi.getPointHistory(params))
    }

    override fun exchangePointByBol(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.exchangePointByBol(params))
    }

    override fun insertEventReply(params: EventReply): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.insertEventReply(params))
    }

    override fun updateEventReply(params: EventReply): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateEventReply(params))
    }

    override fun getEventReplyListByEventReviewSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReply>> {
        return PRNumberRequest.Builder<SubResultResponse<EventReply>>().setRequestCall(ApiController.pRNumberApi.getEventReplyListByEventReviewSeqNo(params))
    }

    override fun getEventReplyListByEventSeqNoAndEventWinSeqNo(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReply>> {
        return PRNumberRequest.Builder<SubResultResponse<EventReply>>().setRequestCall(ApiController.pRNumberApi.getEventReplyListByEventSeqNoAndEventWinSeqNo(params))
    }

    override fun getEventReplyListByEventWinId(params: Map<String, String>): IPRNumberRequest<SubResultResponse<EventReply>> {
        return PRNumberRequest.Builder<SubResultResponse<EventReply>>().setRequestCall(ApiController.pRNumberApi.getEventReplyListByEventWinId(params))
    }

    override fun deleteEventReply(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.deleteEventReply(params))
    }

    override val giftishowCategoryList: IPRNumberRequest<GiftishowCategory>
        get() = PRNumberRequest.Builder<GiftishowCategory>().setRequestCall(ApiController.pRNumberApi.giftishowCategoryList)

    override fun getGiftishowList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Giftishow>> {
        return PRNumberRequest.Builder<SubResultResponse<Giftishow>>().setRequestCall(ApiController.pRNumberApi.getGiftishowList(params))
    }

    override fun getGiftishowListByBrand(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Giftishow>> {
        return PRNumberRequest.Builder<SubResultResponse<Giftishow>>().setRequestCall(ApiController.pRNumberApi.getGiftishowListByBrand(params))
    }

    override fun postGiftishowBuy(params: GiftishowBuy): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.postGiftishowBuy(params))
    }

    override fun getGiftishowBuyList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<GiftishowBuy>> {
        return PRNumberRequest.Builder<SubResultResponse<GiftishowBuy>>().setRequestCall(ApiController.pRNumberApi.getGiftishowBuyList(params))
    }

    override val giftishowBuyCount: IPRNumberRequest<Int>
        get() = PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.giftishowBuyCount)

    override fun checkGiftishowStatus(params: Map<String, String>): IPRNumberRequest<String> {
        return PRNumberRequest.Builder<String>().setRequestCall(ApiController.pRNumberApi.checkGiftishowStatus(params))
    }

    override fun resendGiftishowStatus(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.resendGiftishowStatus(params))
    }

    override fun postPurchaseFTLinkPay(params: FTLink): IPRNumberRequest<FTLink> {
        return PRNumberRequest.Builder<FTLink>().setRequestCall(ApiController.pRNumberApi.postPurchaseFTLinkPay(params))
    }

    override fun postPurchaseBootPayVerify(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.postPurchaseBootPayVerify(params))
    }

    override fun eventBuy(params: EventBuy): IPRNumberRequest<EventWinJpa> {
        return PRNumberRequest.Builder<EventWinJpa>().setRequestCall(ApiController.pRNumberApi.eventBuy(params))
    }

    override fun eventBuyList(params: EventBuy): IPRNumberRequest<EventResultJpa> {
        return PRNumberRequest.Builder<EventResultJpa>().setRequestCall(ApiController.pRNumberApi.eventBuyList(params))
    }

    override fun adReward(): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.adReward())
    }

    override fun checkAdRewardPossible(): IPRNumberRequest<AdRewardPossible> {
        return PRNumberRequest.Builder<AdRewardPossible>().setRequestCall(ApiController.pRNumberApi.checkAdRewardPossible())
    }

    override fun attendance(): IPRNumberRequest<MemberAttendance> {
        return PRNumberRequest.Builder<MemberAttendance>().setRequestCall(ApiController.pRNumberApi.attendance())
    }

    override val mobileCategoryList: IPRNumberRequest<MobileCategory>
        get() = PRNumberRequest.Builder<MobileCategory>().setRequestCall(ApiController.pRNumberApi.mobileCategoryList)

    override fun getMobileBrandList(params: Map<String, String>): IPRNumberRequest<MobileBrand> {
        return PRNumberRequest.Builder<MobileBrand>().setRequestCall(ApiController.pRNumberApi.getMobileBrandList(params))
    }

    override fun saveMemberAddress(params: MemberAddress): IPRNumberRequest<MemberAddress> {
        return PRNumberRequest.Builder<MemberAddress>().setRequestCall(ApiController.pRNumberApi.saveMemberAddress(params))
    }

    override val memberAddress: IPRNumberRequest<MemberAddress>
        get() = PRNumberRequest.Builder<MemberAddress>().setRequestCall(ApiController.pRNumberApi.memberAddress)

    override fun getNotificationBoxList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<NotificationBox>> {
        return PRNumberRequest.Builder<SubResultResponse<NotificationBox>>().setRequestCall(ApiController.pRNumberApi.getNotificationBoxList(params))
    }

    override fun notificationBoxDelete(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.notificationBoxDelete(params))
    }

    override fun getBuffWithdrawNotification(): IPRNumberRequest<NotificationBox> {
        return PRNumberRequest.Builder<NotificationBox>().setRequestCall(ApiController.pRNumberApi.getBuffWithdrawNotification())
    }

    override fun notificationBoxRead(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.notificationBoxRead(params))
    }

    override fun getPopupList(params: Map<String, String>): IPRNumberRequest<PopupMange> {
        return PRNumberRequest.Builder<PopupMange>().setRequestCall(ApiController.pRNumberApi.getPopupList(params))
    }

    override fun getBannerList(params: Map<String, String>): IPRNumberRequest<Banner> {
        return PRNumberRequest.Builder<Banner>().setRequestCall(ApiController.pRNumberApi.getBannerList(params))
    }

    override fun getContactListWithMember(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Contact>> {
        return PRNumberRequest.Builder<SubResultResponse<Contact>>().setRequestCall(ApiController.pRNumberApi.getContactListWithMember(params))
    }

    override fun getFriendList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<Contact>> {
        return PRNumberRequest.Builder<SubResultResponse<Contact>>().setRequestCall(ApiController.pRNumberApi.getFriendList(params))
    }

    override fun getContactMemberCount(): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getContactMemberCount())
    }

    override fun getBuff(params: Map<String, String>): IPRNumberRequest<Buff> {
        return PRNumberRequest.Builder<Buff>().setRequestCall(ApiController.pRNumberApi.getBuff(params))
    }

    override fun getBuffMember(): IPRNumberRequest<BuffMember> {
        return PRNumberRequest.Builder<BuffMember>().setRequestCall(ApiController.pRNumberApi.getBuffMember())
    }

    override fun getBuffMemberList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffMember>> {
        return PRNumberRequest.Builder<SubResultResponse<BuffMember>>().setRequestCall(ApiController.pRNumberApi.getBuffMemberList(params))
    }

    override fun getBuffLogList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffDividedBolLog>> {
        return PRNumberRequest.Builder<SubResultResponse<BuffDividedBolLog>>().setRequestCall(ApiController.pRNumberApi.getBuffLogList(params))
    }

    override fun getRequestList(): IPRNumberRequest<BuffRequest> {
        return PRNumberRequest.Builder<BuffRequest>().setRequestCall(ApiController.pRNumberApi.getRequestList())
    }

    override fun getRequestCount(): IPRNumberRequest<Int> {
        return PRNumberRequest.Builder<Int>().setRequestCall(ApiController.pRNumberApi.getRequestCount())
    }

    override fun buffMake(params: Buff): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.buffMake(params))
    }

    override fun buffInvite(params: BuffParam): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.buffInvite(params))
    }

    override fun changeBuffRequest(params: Map<String, String>): IPRNumberRequest<BuffRequestResult> {
        return PRNumberRequest.Builder<BuffRequestResult>().setRequestCall(ApiController.pRNumberApi.changeBuffRequest(params))
    }

    override fun changeBuffOwner(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.changeBuffOwner(params))
    }

    override fun exitBuff(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.exitBuff(params))
    }

    override fun forcedExitBuff(params: BuffParam): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.forcedExitBuff(params))
    }

    override fun getBuffPostList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffPost>> {
        return PRNumberRequest.Builder<SubResultResponse<BuffPost>>().setRequestCall(ApiController.pRNumberApi.getBuffPostList(params))
    }

    override fun getBuffPostLikeList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffPostLike>> {
        return PRNumberRequest.Builder<SubResultResponse<BuffPostLike>>().setRequestCall(ApiController.pRNumberApi.getBuffPostLikeList(params))
    }

    override fun getBuffPostReplyList(params: Map<String, String>): IPRNumberRequest<SubResultResponse<BuffPostReply>> {
        return PRNumberRequest.Builder<SubResultResponse<BuffPostReply>>().setRequestCall(ApiController.pRNumberApi.getBuffPostReplyList(params))
    }

    override fun buffPostLike(params: Map<String, String>): IPRNumberRequest<String> {
        return PRNumberRequest.Builder<String>().setRequestCall(ApiController.pRNumberApi.buffPostLike(params))
    }

    override fun insertBuffPostReply(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.insertBuffPostReply(params))
    }

    override fun modifyBuffPostReply(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.modifyBuffPostReply(params))
    }

    override fun deleteBuffPostReply(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.deleteBuffPostReply(params))
    }

    override fun insertBuffPost(params: BuffPost): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.insertBuffPost(params))
    }

    override fun updateBuffPost(params: BuffPost): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateBuffPost(params))
    }

    override fun updateBuffPostPublic(params: Map<String, String>): IPRNumberRequest<Any> {
        return PRNumberRequest.Builder<Any>().setRequestCall(ApiController.pRNumberApi.updateBuffPostPublic(params))
    }

    override fun walletSignUp(params: Map<String, String>): IPRNumberRequest<String> {
        return PRNumberRequest.Builder<String>().setRequestCall(ApiController.pRNumberApi.walletSignUp(params))
    }

    override fun walletSync(params: Map<String, String>): IPRNumberRequest<String> {
        return PRNumberRequest.Builder<String>().setRequestCall(ApiController.pRNumberApi.walletSync(params))
    }

    override fun walletBalance(): IPRNumberRequest<Map<String, Any>> {
        return PRNumberRequest.Builder<Map<String, Any>>().setRequestCall(ApiController.pRNumberApi.walletBalance())
    }

    override fun walletDuplicateUser(): IPRNumberRequest<String> {
        return PRNumberRequest.Builder<String>().setRequestCall(ApiController.pRNumberApi.walletDuplicateUser())
    }

    override fun getShoppingGroup(params: Map<String, String>): IPRNumberRequest<ShoppingGroup> {
        return PRNumberRequest.Builder<ShoppingGroup>().setRequestCall(ApiController.pRNumberApi.getShoppingGroup(params))
    }

    companion object {
        @JvmStatic
        fun create(): ApiBuilder {
            return ApiBuilder()
        }
    }
}