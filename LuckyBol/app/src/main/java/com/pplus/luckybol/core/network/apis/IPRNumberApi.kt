package com.pplus.luckybol.core.network.apis

import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.request.params.ParamsContact
import com.pplus.luckybol.core.network.model.request.params.ParamsJoinEvent
import com.pplus.luckybol.core.network.model.request.params.ParamsRegDevice
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.network.model.response.result.ResultStepAddress
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by 김종경 on 2016-10-06.
 */
interface IPRNumberApi {

    //중복 확인
    @FormUrlEncoded
    @POST("auth/existsUser")
    fun existsUser(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //회원가입
    @POST("auth/join")
    fun join(@Body params: User): Call<NewResultResponse<User>>

    //앱 버전 가져오기
    @FormUrlEncoded
    @POST("auth/getAppVersion")
    fun appVersion(@FieldMap params: Map<String, String>): Call<NewResultResponse<AppVersion>>

    //앱 로그인
    @FormUrlEncoded
    @POST("auth/login")
    fun login(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    //디바이스 존재여부 체크
    @FormUrlEncoded
    @POST("auth/existsDevice")
    fun existsDevice(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @POST("auth/registDevice")
    fun registDevice(@Body params: ParamsRegDevice): Call<NewResultResponse<User>>

    //약관 리스트
    @FormUrlEncoded
    @POST("auth/getActiveTermsAllByAppType")
    fun getActiveTermsAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<Terms>>

    //미동의 약관 리스트
    @FormUrlEncoded
    @POST("auth/getNotSignedActiveTermsAllByAppType")
    fun getNotSignedActiveTermsAll(@Field("appType") appType: String): Call<NewResultResponse<Terms>>


    @get:POST("auth/getSession")
    val session: Call<NewResultResponse<User>>

    @POST("auth/reloadSession")
    fun requestReloadSession(): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("auth/getUserByVerification")
    fun getUserByVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("auth/getUserByLoginIdAndMobile")
    fun getUserByLoginIdAndMobile(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("auth/agreeTermsList")
    fun agreeTermsList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("auth/changePasswordByVerification")
    fun changePasswordByVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("auth/updateMobileByVerification")
    fun updateMobileByVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //프로필이미지 세팅
    @FormUrlEncoded
    @POST("my/updateProfileImage")
    fun updateProfileImage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("my/updateNickname")
    fun updateNickname(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //push 세팅
    @FormUrlEncoded
    @POST("my/updatePushConfig")
    fun updatePushConfig(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("my/updateExternal")
    fun updateExternal(@Body params: User): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("page/getPage")
    fun getPage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    //포스트 등록
    @POST("post/insertPost")
    fun insertPost(@Body params: Post): Call<NewResultResponse<Post>>

    @FormUrlEncoded
    @POST("common/searchAddress")
    fun requestSearchAddress(@FieldMap params: Map<String, String>): Call<ResultAddress>

    @FormUrlEncoded
    @POST("common/searchStepAddress")
    fun requestSearchStepAddress(@FieldMap params: Map<String, String>): Call<ResultStepAddress>

    @FormUrlEncoded
    @POST("common/getNoticeCount")
    fun getNoticeCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("common/getNoticeList")
    fun getNoticeList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Notice>>

    @FormUrlEncoded
    @POST("common/getNotice")
    fun getNotice(@FieldMap params: Map<String, String>): Call<NewResultResponse<Notice>>

    @FormUrlEncoded
    @POST("common/getFaqGroupAll")
    fun getFaqGroupAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<FaqGroup>>

    @FormUrlEncoded
    @POST("common/getFaqCountByAppType")
    fun getFaqCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("common/getFaqListByAppType")
    fun getFaqList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Faq>>

    @FormUrlEncoded
    @POST("common/getFaq")
    fun getFaq(@FieldMap params: Map<String, String>): Call<NewResultResponse<Faq>>

    @get:POST("common/getCountryConfigAll")
    val countryConfigAll: Call<NewResultResponse<CountryConfig>>

    @POST("common/reporting")
    fun requestReporting(@Body report: Report): Call<NewResultResponse<Report>>

    //인증 요청
    @FormUrlEncoded
    @POST("verification/request")
    fun requestVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<Verification>>

    //인증 요청
    @FormUrlEncoded
    @POST("verification/confirm")
    fun confirmVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //탈퇴 요청
    @FormUrlEncoded
    @POST("verification/leave")
    fun requestLeave(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("verification/cancelLeave")
    fun requestCancelLeave(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("plus/getList")
    fun getPlusList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Plus>>

    @FormUrlEncoded
    @POST("plus/getCount")
    fun getPlusCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("plus/getExcludeCount")
    fun getExcludePlusCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("plus/getExcludeList")
    fun getExcludePlusList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Plus>>

    @POST("plus/insert")
    fun requestInsertPlus(@Body params: Plus): Call<NewResultResponse<Plus>>

    @FormUrlEncoded
    @POST("plus/delete")
    fun requestDeletePlus(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("plus/deleteByPage")
    fun requestDeletePlusByPage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("plus/getExistFan")
    fun requestExistPlusByPage(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("msg/readComplete")
    fun requestReadComplete(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    //연락처 업데이트
    @POST("contact/updateList")
    fun updateContactList(@Body params: ParamsContact): Call<NewResultResponse<Any>>

    //연락처 삭제
    @POST("contact/deleteList")
    fun requestDeleteContactList(@Body params: ParamsContact): Call<NewResultResponse<Any>>

    //친구 리스트 카운트
    @get:POST("contact/getAllFriendCount")
    val allFriendCount: Call<NewResultResponse<Int>>

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getAllFriendList")
    fun getAllFriendList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Friend>>

    //친구 리스트 카운트
    @get:POST("contact/getFriendCount")
    val friendCount: Call<NewResultResponse<Int>>

    //같은 친구 리스트
    @FormUrlEncoded
    @POST("contact/getSameFriendAll")
    fun getSameFriendAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    //친구 리스트 카운트
    @FormUrlEncoded
    @POST("contact/getFriendPageCount")
    fun getFriendPageCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getFriendPageList")
    fun getFriendPageList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Page>>

    //친구 리스트 카운트
    @get:POST("contact/getUserFriendCount")
    val userFriendCount: Call<NewResultResponse<Int>>

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getUserFriendList")
    fun getUserFriendList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Friend>>

    //친구 리스트 카운트
    @get:POST("contact/getExistsNicknameFriendCount")
    val existsNicknameFriendCount: Call<NewResultResponse<Int>>

    //친구 리스트
    @FormUrlEncoded
    @POST("contact/getExistsNicknameFriendList")
    fun getExistsNicknameFriendList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Friend>>

    @FormUrlEncoded
    @POST("bol/getHistoryList")
    fun getBolHistoryList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Bol>>

    @FormUrlEncoded
    @POST("bol/getHistoryTotalAmount")
    fun getBolHistoryTotalAmount(@FieldMap params: Map<String, String>): Call<NewResultResponse<String>>

    @POST("point/cashExchange")
    fun cashExchange(@Body params: Exchange): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("bol/getHistoryCount")
    fun getBolHistoryCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("bol/getHistoryWithTargetList")
    fun getBolHistoryWithTargetList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Bol>>

    @FormUrlEncoded
    @POST("bol/getHistoryWithTarget")
    fun getBolHistoryWithTarget(@FieldMap params: Map<String, String>): Call<NewResultResponse<Bol>>

    @FormUrlEncoded
    @POST("user/getExistsNicknameUserCount")
    fun getExistsNicknameUserCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("user/getExistsNicknameUserList")
    fun getExistsNicknameUserList(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("user/getUserCountByRecommendKey")
    fun getUserCountByRecommendKey(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("user/getUserListByRecommendKey")
    fun getUserListByRecommendKey(@FieldMap params: Map<String, String>): Call<NewResultResponse<User>>

    @FormUrlEncoded
    @POST("page/checkAuthCodeForUser")
    fun requestCheckAuthCodeForUser(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getCount")
    fun getEventCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getEventList")
    fun getEventList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("event/getMainBannerLottoList")
    fun getMainBannerLottoList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @get:POST("event/getGroupAll")
    val eventGroupAll: Call<NewResultResponse<EventGroup>>

    @FormUrlEncoded
    @POST("event/getCountByGroup")
    fun getEventCountByGroup(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getEventListByGroup")
    fun getEventListByGroup(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("event/getByNumber")
    fun getEventByNumber(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @FormUrlEncoded
    @POST("event/join")
    fun joinEvent(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventResult>>

    @FormUrlEncoded
    @POST("event/lottoJoin")
    fun lottoJoin(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventResult>>

    @FormUrlEncoded
    @POST("event/serializableJoin")
    fun serializableJoinEvent(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventResult>>

    @FormUrlEncoded
    @POST("event/checkJoinEnable")
    fun checkJoinEnable(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventResult>>

    @POST("event/joinWithProperties")
    fun requestJoinWithPropertiesEvent(@Body params: ParamsJoinEvent): Call<NewResultResponse<EventResult>>

    @FormUrlEncoded
    @POST("event/writeImpression")
    fun requestWriteImpression(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("event/getBannerAll")
    fun getBannerAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventBanner>>

    @FormUrlEncoded
    @POST("event/getGiftAll")
    fun getGiftAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventGift>>

    @FormUrlEncoded
    @POST("event/getWinCount")
    fun getWinCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getWinList")
    fun getWinList(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @GET("event/getEventJoinCount")
    fun getEventJoinCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @get:POST("event/getWinCountOnlyPresentByMemberSeqNo")
    val winCountOnlyPresentByMemberSeqNo: Call<NewResultResponse<Int>>

    @get:POST("event/getWinCountOnlyPresentToday")
    val winCountOnlyPresentToday: Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getWinCountOnlyPresent")
    fun getWinCountOnlyPresent(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getWinListOnlyPresent")
    fun getWinListOnlyPresent(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @get:POST("event/getMyWinCountOnlyPresent")
    val myWinCountOnlyPresent: Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getMyWinListOnlyPresent")
    fun getMyWinListOnlyPresent(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @FormUrlEncoded
    @POST("event/getWinBySeqNo")
    fun getWinBySeqNo(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @get:POST("event/getUserWinCount")
    val userWinCount: Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getUserWinList")
    fun getUserWinList(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @FormUrlEncoded
    @POST("event/existsResult")
    fun requestExistsEventResult(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventExist>>

    @FormUrlEncoded
    @POST("event/getWinAll")
    fun getWinAll(@FieldMap params: Map<String, String>): Call<NewResultResponse<EventWin>>

    @FormUrlEncoded
    @POST("event/get")
    fun getEvent(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @GET("event/getMyBuyJoinCount")
    fun getMyJoinCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("event/getMyBuyJoinCountAndBuyType")
    fun getMyJoinCountAndBuyType(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getWinAnnounceCount")
    fun getWinAnnounceCount(@FieldMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("event/getWinAnnounceList")
    fun getWinAnnounceList(@FieldMap params: Map<String, String>): Call<NewResultResponse<Event>>

    @GET("event/getLottoJoinList")
    fun getLottoJoinList(@QueryMap params: Map<String, String>): Call<NewResultResponse<EventJoinJpa>>

    @GET("event/getLottoWinNumberList")
    fun getLottoWinNumberList(@QueryMap params: Map<String, String>): Call<NewResultResponse<LottoWinNumber>>

    @GET("event/getLottoWinnerCount")
    fun getLottoWinnerCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("event/getLottoWinnerList")
    fun getLottoWinnerList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<EventWinJpa>>>

    @GET("event/getLottoHistoryCount")
    fun getLottoHistoryCount(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @GET("event/getLottoHistoryList")
    fun getLottoHistoryList(@QueryMap params: Map<String, String>): Call<NewResultResponse<Event>>


    //상품 리뷰 api
    @POST("goodsReview")
    fun postGoodsReview(@Body goods: GoodsReview): Call<NewResultResponse<Any>>

    @PUT("goodsReview")
    fun putGoodsReview(@Body goods: GoodsReview): Call<NewResultResponse<Any>>

    @POST("purchase/orderId")
    fun postOrderId(): Call<NewResultResponse<String>>

    @GET("user/check")
    fun userCheck(@QueryMap params: Map<String, String>): Call<NewResultResponse<String>>

    @FormUrlEncoded
    @POST("common/cpeReport")
    fun requestCpeReport(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("common/cpaReport")
    fun requestCpaReport(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("auth/updatePushKey")
    fun updatePushKey(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("user/updatePayPassword")
    fun updatePayPassword(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("user/updatePayPasswordWithVerification")
    fun updatePayPasswordWithVerification(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("user/checkPayPassword")
    fun checkPayPassword(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:GET("card/list")
    val cardList: Call<NewResultResponse<Card>>

    @GET("card/updateRepresent")
    fun updateRepresentCard(@QueryMap params: Map<String, String>): Call<NewResultResponse<Card>>

    @POST("card/register")
    fun postCardRegister(@Body params: CardReq): Call<NewResultResponse<Card>>

    @DELETE("card/delete")
    fun deleteCard(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:GET("shippingSite/list")
    val shippingSiteList: Call<NewResultResponse<ShippingSite>>

    @POST("shippingSite/insert")
    fun insertShippingSite(@Body params: ShippingSite): Call<NewResultResponse<ShippingSite>>

    @PUT("shippingSite/update")
    fun updateShippingSite(@Body params: ShippingSite): Call<NewResultResponse<ShippingSite>>

    @DELETE("shippingSite/delete")
    fun deleteShippingSite(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:GET("category/majorOnly/list")
    val categoryMajorOnly: Call<NewResultResponse<CategoryMajor>>

    @get:GET("category/major/list")
    val categoryMajor: Call<NewResultResponse<CategoryMajor>>

    @get:GET("category/first/list")
    val categoryFirstList: Call<NewResultResponse<CategoryFirst>>

    @GET("juso/list")
    fun getJusoList(@QueryMap params: Map<String, String>): Call<NewResultResponse<Juso>>

    @get:GET("juso/doList")
    val doList: Call<NewResultResponse<Province>>

    @get:GET("category/myFavorite")
    val myFavoriteCategoryList: Call<NewResultResponse<CategoryFavorite>>

    @POST("category/favorite/insert")
    fun insertCategoryFavorite(@Body params: CategoryFavorite): Call<NewResultResponse<CategoryFavorite>>

    @DELETE("category/favorite/delete")
    fun deleteCategoryFavorite(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @PUT("user/updateActiveArea1")
    fun updateActiveArea1(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @PUT("user/updateActiveArea2")
    fun updateActiveArea2(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @PUT("event/updateUseGift")
    fun updateUseGift(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("shippingSite/checkIslandsRegion")
    fun checkIslandsRegion(@QueryMap params: Map<String, String>): Call<NewResultResponse<Boolean>>

    @GET("shippingSite/getIsLandsRegion")
    fun getIsLandsRegion(@QueryMap params: Map<String, String>): Call<NewResultResponse<IslandsRegion>>

    @FormUrlEncoded
    @POST("my/updateGender")
    fun updateGender(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("my/updateBirthday")
    fun updateBirthday(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("event/payPoint")
    fun eventPayPoint(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("event/insertEventReview")
    fun insertEventReview(@Body params: EventReview): Call<NewResultResponse<Any>>

    @PUT("event/updateEventReview")
    fun updateEventReview(@Body params: EventReview): Call<NewResultResponse<Any>>

    @GET("event/getEventReviewList")
    fun getEventReviewList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<EventReview>>>

    @GET("event/getMyEventReviewList")
    fun getMyEventReviewList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<EventReview>>>

    @GET("event/getEventReview")
    fun getEventReview(@QueryMap params: Map<String, String>): Call<NewResultResponse<EventReview>>

    @GET("product/price/shipTypeIsLuckybol")
    fun getProductPriceShipType(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/getProductPriceListShipTypeByShoppingGroup")
    fun getProductPriceListShipTypeByShoppingGroup(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductPrice>>>

    @GET("product/price")
    fun getProductPrice(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductPrice>>

    @GET("product/review/countByProductPriceSeqNo")
    fun getCountProductReviewByProductPriceSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<Int>>

    @get:GET("product/review/countByMemberSeqNo")
    val countProductReviewByyMemberSeqNo: Call<NewResultResponse<Int>>

    @GET("product/like/check")
    fun getProductLikeCheck(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("product/like/insert")
    fun insertProductLike(@Body params: ProductLike): Call<NewResultResponse<Any>>

    @HTTP(method = "DELETE", path = "product/like/delete", hasBody = true)
    fun deleteProductLike(@Body params: ProductLike): Call<NewResultResponse<Any>>

    @GET("product/review/productPriceSeqNo")
    fun getProductReviewByProductPriceSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductReview>>>

    @GET("product/review/memberSeqNo")
    fun getProductReviewByMemberSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductReview>>>

    @POST("product/review/insert")
    fun insertProductReview(@Body params: ProductReview): Call<NewResultResponse<Any>>

    @PUT("product/review/update")
    fun updateProductReview(@Body params: ProductReview): Call<NewResultResponse<Any>>

    @DELETE("product/review/delete")
    fun deleteProductReview(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("product/option")
    fun getProductOption(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductOptionTotal>>

    @get:GET("product/like/count")
    val countProductLike: Call<NewResultResponse<Int>>

    @GET("product/like/shippingList")
    fun getProductLikeShippingList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<ProductLike>>>

    @POST("purchase/ship")
    fun postPurchaseShip(@Body params: Purchase): Call<NewResultResponse<Purchase>>

    @GET("purchase/product/list")
    fun getPurchaseProductListByMemberSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<PurchaseProduct>>>

    @get:GET("purchase/product/count")
    val countPurchaseProductByMemberSeqNo: Call<NewResultResponse<Int>>

    @GET("purchase/product")
    fun getPurchaseProduct(@QueryMap params: Map<String, String>): Call<NewResultResponse<PurchaseProduct>>

    @PUT("purchase/product/complete")
    fun updatePurchaseProductComplete(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("purchase/product/cancel")
    fun cancelPurchase(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("purchase/ftlink/pay")
    fun postPurchaseFTLinkPay(@Body params: FTLink): Call<NewResultResponse<FTLink>>

    @FormUrlEncoded
    @POST("purchase/bootpay/verify")
    fun postPurchaseBootPayVerify(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("product/review/countGroupByEval")
    fun getProductReviewCountGroupByEval(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductReviewCountEval>>

    @GET("product/info")
    fun getProductInfoByProductSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductInfo>>

    @GET("product/auth")
    fun getProductAuthByProductSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductAuth>>

    @GET("product/notice/list")
    fun getProductNoticeListByProductSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<ProductNotice>>

    @GET("businessLicense/get")
    fun getBusinessLicense(@QueryMap params: Map<String, String>): Call<NewResultResponse<BusinessLicense>>

    @POST("point/insert")
    fun insertPointBuy(@Body params: PointBuy): Call<NewResultResponse<Any>>

    @GET("point/history/list")
    fun getPointHistoryList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<PointHistory>>>

    @GET("point/history/get")
    fun getPointHistory(@QueryMap params: Map<String, String>): Call<NewResultResponse<PointHistory>>

    @FormUrlEncoded
    @POST("point/exchange")
    fun exchangePointByBol(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("event/reply/insert")
    fun insertEventReply(@Body params: EventReply): Call<NewResultResponse<Any>>

    @PUT("event/reply/update")
    fun updateEventReply(@Body params: EventReply): Call<NewResultResponse<Any>>

    @GET("event/getEventReplyListByEventReviewSeqNo")
    fun getEventReplyListByEventReviewSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<EventReply>>>

    @GET("event/getEventReplyListByEventSeqNoAndEventWinSeqNo")
    fun getEventReplyListByEventSeqNoAndEventWinSeqNo(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<EventReply>>>

    @GET("event/getEventReplyListByEventWinId")
    fun getEventReplyListByEventWinId(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<EventReply>>>

    @DELETE("event/reply/delete")
    fun deleteEventReply(@QueryMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @get:GET("giftshow/category/list")
    val giftishowCategoryList: Call<NewResultResponse<GiftishowCategory>>

    @GET("giftshow/list")
    fun getGiftishowList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Giftishow>>>

    @GET("giftshow/listByBrand")
    fun getGiftishowListByBrand(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Giftishow>>>

    @POST("giftshow/buy")
    fun postGiftishowBuy(@Body params: GiftishowBuy): Call<NewResultResponse<Any>>

    @GET("giftshow/buy/list")
    fun getGiftishowBuyList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<GiftishowBuy>>>

    @get:GET("giftshow/buy/count")
    val giftishowBuyCount: Call<NewResultResponse<Int>>

    @FormUrlEncoded
    @POST("giftshow/check")
    fun checkGiftishowStatus(@FieldMap params: Map<String, String>): Call<NewResultResponse<String>>

    @FormUrlEncoded
    @POST("giftshow/resend")
    fun resendGiftishowStatus(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("event/buy")
    fun eventBuy(@Body params: EventBuy): Call<NewResultResponse<EventWinJpa>>

    @POST("event/buy/list")
    fun eventBuyList(@Body params: EventBuy): Call<NewResultResponse<EventResultJpa>>

    @POST("bol/adReward")
    fun adReward(): Call<NewResultResponse<Int>>

    @POST("event/checkAdRewardPossible")
    fun checkAdRewardPossible(): Call<NewResultResponse<AdRewardPossible>>

    @POST("member/attendance")
    fun attendance(): Call<NewResultResponse<MemberAttendance>>

    @get:GET("giftshow/getMobileCategoryList")
    val mobileCategoryList: Call<NewResultResponse<MobileCategory>>

    @GET("giftshow/getMobileBrandList")
    fun getMobileBrandList(@QueryMap params: Map<String, String>): Call<NewResultResponse<MobileBrand>>

    @POST("member/address/save")
    fun saveMemberAddress(@Body params: MemberAddress): Call<NewResultResponse<MemberAddress>>

    @get:GET("member/address/get")
    val memberAddress: Call<NewResultResponse<MemberAddress>>

    @GET("notificationBox/getNotificationBoxList")
    fun getNotificationBoxList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<NotificationBox>>>

    @FormUrlEncoded
    @POST("notificationBox/delete")
    fun notificationBoxDelete(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("notificationBox/getBuffWithdrawNotification")
    fun getBuffWithdrawNotification(): Call<NewResultResponse<NotificationBox>>

    @FormUrlEncoded
    @POST("notificationBox/read")
    fun notificationBoxRead(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @GET("popupMange/getPopupList")
    fun getPopupList(@QueryMap params: Map<String, String>): Call<NewResultResponse<PopupMange>>

    @GET("banner/getBannerList")
    fun getBannerList(@QueryMap params: Map<String, String>): Call<NewResultResponse<Banner>>

    @GET("contact/getContactListWithMember")
    fun getContactListWithMember(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Contact>>>

    @GET("contact/getFriendList")
    fun getFriendList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<Contact>>>

    @GET("contact/getContactMemberCount")
    fun getContactMemberCount(): Call<NewResultResponse<Int>>

    @GET("buff/getBuff")
    fun getBuff(@QueryMap params: Map<String, String>): Call<NewResultResponse<Buff>>

    @GET("buff/getBuffMember")
    fun getBuffMember(): Call<NewResultResponse<BuffMember>>

    @GET("buff/getBuffMemberList")
    fun getBuffMemberList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<BuffMember>>>

    @GET("buff/getBuffLogList")
    fun getBuffLogList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<BuffDividedBolLog>>>

    @GET("buff/getRequestList")
    fun getRequestList(): Call<NewResultResponse<BuffRequest>>

    @GET("buff/getRequestCount")
    fun getRequestCount(): Call<NewResultResponse<Int>>

    @POST("buff/buffMake")
    fun buffMake(@Body params: Buff): Call<NewResultResponse<Any>>

    @POST("buff/buffInvite")
    fun buffInvite(@Body params: BuffParam): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("buff/changeBuffRequest")
    fun changeBuffRequest(@FieldMap params: Map<String, String>): Call<NewResultResponse<BuffRequestResult>>

    @FormUrlEncoded
    @POST("buff/changeBuffOwner")
    fun changeBuffOwner(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("buff/exitBuff")
    fun exitBuff(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("buff/forcedExitBuff")
    fun forcedExitBuff(@Body params: BuffParam): Call<NewResultResponse<Any>>

    @GET("buff/getBuffPostList")
    fun getBuffPostList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<BuffPost>>>

    @GET("buff/getBuffPostLikeList")
    fun getBuffPostLikeList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<BuffPostLike>>>

    @GET("buff/getBuffPostReplyList")
    fun getBuffPostReplyList(@QueryMap params: Map<String, String>): Call<NewResultResponse<SubResultResponse<BuffPostReply>>>

    @FormUrlEncoded
    @POST("buff/buffPostLike")
    fun buffPostLike(@FieldMap params: Map<String, String>): Call<NewResultResponse<String>>

    @FormUrlEncoded
    @POST("buff/insertBuffPostReply")
    fun insertBuffPostReply(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("buff/modifyBuffPostReply")
    fun modifyBuffPostReply(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("buff/deleteBuffPostReply")
    fun deleteBuffPostReply(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @POST("buff/insertBuffPost")
    fun insertBuffPost(@Body params: BuffPost): Call<NewResultResponse<Any>>

    @POST("buff/updateBuffPost")
    fun updateBuffPost(@Body params: BuffPost): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("member/updateBuffPostPublic")
    fun updateBuffPostPublic(@FieldMap params: Map<String, String>): Call<NewResultResponse<Any>>

    @FormUrlEncoded
    @POST("buffWallet/walletSignUp")
    fun walletSignUp(@FieldMap params: Map<String, String>): Call<NewResultResponse<String>>

    @FormUrlEncoded
    @POST("buffWallet/walletSync")
    fun walletSync(@FieldMap params: Map<String, String>): Call<NewResultResponse<String>>

    @POST("buffWallet/walletBalance")
    fun walletBalance(): Call<NewResultResponse<Map<String, Any>>>

    @POST("buffWallet/duplicateUser")
    fun walletDuplicateUser(): Call<NewResultResponse<String>>

    @GET("shoppingGroup/getShoppingGroup")
    fun getShoppingGroup(@QueryMap params: Map<String, String>): Call<NewResultResponse<ShoppingGroup>>


}