package com.pplus.prnumberbiz.core.network.apis;

import com.pplus.prnumberbiz.core.network.model.dto.Advertise;
import com.pplus.prnumberbiz.core.network.model.dto.Agent;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
import com.pplus.prnumberbiz.core.network.model.dto.Bol;
import com.pplus.prnumberbiz.core.network.model.dto.BolGift;
import com.pplus.prnumberbiz.core.network.model.dto.Buy;
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods;
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoodsTypePrice;
import com.pplus.prnumberbiz.core.network.model.dto.Cash;
import com.pplus.prnumberbiz.core.network.model.dto.Category;
import com.pplus.prnumberbiz.core.network.model.dto.Comment;
import com.pplus.prnumberbiz.core.network.model.dto.Coord;
import com.pplus.prnumberbiz.core.network.model.dto.Count;
import com.pplus.prnumberbiz.core.network.model.dto.CountryConfig;
import com.pplus.prnumberbiz.core.network.model.dto.Coupon;
import com.pplus.prnumberbiz.core.network.model.dto.CouponTemplate;
import com.pplus.prnumberbiz.core.network.model.dto.Customer;
import com.pplus.prnumberbiz.core.network.model.dto.Delivery;
import com.pplus.prnumberbiz.core.network.model.dto.DeliveryTotalPrice;
import com.pplus.prnumberbiz.core.network.model.dto.Fan;
import com.pplus.prnumberbiz.core.network.model.dto.Faq;
import com.pplus.prnumberbiz.core.network.model.dto.FaqGroup;
import com.pplus.prnumberbiz.core.network.model.dto.Franchise;
import com.pplus.prnumberbiz.core.network.model.dto.FranchiseGroup;
import com.pplus.prnumberbiz.core.network.model.dto.Friend;
import com.pplus.prnumberbiz.core.network.model.dto.Goods;
import com.pplus.prnumberbiz.core.network.model.dto.GoodsNoticeInfo;
import com.pplus.prnumberbiz.core.network.model.dto.GoodsReview;
import com.pplus.prnumberbiz.core.network.model.dto.Group;
import com.pplus.prnumberbiz.core.network.model.dto.HashTag;
import com.pplus.prnumberbiz.core.network.model.dto.ImgUrl;
import com.pplus.prnumberbiz.core.network.model.dto.Lpng;
import com.pplus.prnumberbiz.core.network.model.dto.LpngRes;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGift;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftCategory;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftHistory;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftPurchase;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.dto.NoteReceive;
import com.pplus.prnumberbiz.core.network.model.dto.NoteSend;
import com.pplus.prnumberbiz.core.network.model.dto.Notice;
import com.pplus.prnumberbiz.core.network.model.dto.NoticeInfo;
import com.pplus.prnumberbiz.core.network.model.dto.PageManagement;
import com.pplus.prnumberbiz.core.network.model.dto.OrderCount;
import com.pplus.prnumberbiz.core.network.model.dto.OrderTypeCount;
import com.pplus.prnumberbiz.core.network.model.dto.OutgoingNumber;
import com.pplus.prnumberbiz.core.network.model.dto.Page;
import com.pplus.prnumberbiz.core.network.model.dto.PageEval;
import com.pplus.prnumberbiz.core.network.model.dto.PageGoodsCategory;
import com.pplus.prnumberbiz.core.network.model.dto.PageSeller;
import com.pplus.prnumberbiz.core.network.model.dto.Payment;
import com.pplus.prnumberbiz.core.network.model.dto.Post;
import com.pplus.prnumberbiz.core.network.model.dto.Price;
import com.pplus.prnumberbiz.core.network.model.dto.Report;
import com.pplus.prnumberbiz.core.network.model.dto.SavedMsg;
import com.pplus.prnumberbiz.core.network.model.dto.Sns;
import com.pplus.prnumberbiz.core.network.model.dto.Target;
import com.pplus.prnumberbiz.core.network.model.dto.UseCount;
import com.pplus.prnumberbiz.core.network.model.dto.User;
import com.pplus.prnumberbiz.core.network.model.dto.Verification;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsContact;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsCustomerGroup;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsCustomerList;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsFanGroup;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsGroupPriority;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroImage;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroMovie;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsNoteSend;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsRegDevice;
import com.pplus.prnumberbiz.core.network.model.dto.AppVersion;
import com.pplus.prnumberbiz.core.network.model.dto.Terms;
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse;
import com.pplus.prnumberbiz.core.network.prnumber.IPRNumberRequest;

import java.util.Map;

/**
 * Created by j2n on 2016. 9. 12..
 */
public interface IPRNumberBuilder {

    //existsUser
    IPRNumberRequest<Object> existsUser(Map<String, String> params);

    //join
    IPRNumberRequest<User> join(User params);

    IPRNumberRequest<User> levelup(User params);

    //appversion
    IPRNumberRequest<AppVersion> appVersion(Map<String, String> params);

    //login
    IPRNumberRequest<User> login(Map<String, String> params);

    //check device
    IPRNumberRequest<User> existsDevice(Map<String, String> params);

    //reg device
    IPRNumberRequest<User> registDevice(ParamsRegDevice params);

    //terms list
    IPRNumberRequest<Terms> getActiveTermsAll(String appKey);

    //not signe terms list
    IPRNumberRequest<Terms> getNotSignedActiveTermsAll(String appKey);

    //activate page
    IPRNumberRequest<User> activatePage(User params);

    IPRNumberRequest<User> startPage();

    //get session
    IPRNumberRequest<User> getSession();

    //reload session
    IPRNumberRequest<User> reloadSession();

    //find id
    IPRNumberRequest<User> getUserByVerification(Map<String, String> params);

    //change password
    IPRNumberRequest<Object> changePasswordByVerification(Map<String, String> params);

    IPRNumberRequest<Object> updateMobileByVerification(Map<String, String> params);

    //find user
    IPRNumberRequest<User> getUserByLoginIdAndMobile(Map<String, String> params);


    //get page
    IPRNumberRequest<Page> getPage(Map<String, String> params);

    IPRNumberRequest<Object> updatePageProfileImage(Map<String, String> params);

    IPRNumberRequest<Object> updatePageBackgroundImage(Map<String, String> params);

    //introduce image reg
    IPRNumberRequest<Page> updateIntroImageList(ParamsIntroImage params);

    //introduce image list
    IPRNumberRequest<Attachment> getIntroImageAll(Map<String, String> params);

    IPRNumberRequest<Coord> getCoordByAddress(Map<String, String> params);

    IPRNumberRequest<Integer> getNoticeCount(Map<String, String> params);

    IPRNumberRequest<Notice> getNoticeList(Map<String, String> params);

    IPRNumberRequest<Notice> getNotice(Map<String, String> params);

    IPRNumberRequest<FaqGroup> getFaqGroupAll(Map<String, String> params);

    IPRNumberRequest<Integer> getFaqCount(Map<String, String> params);

    IPRNumberRequest<Faq> getFaqList(Map<String, String> params);

    IPRNumberRequest<Faq> getFaq(Map<String, String> params);

    //getMe
    IPRNumberRequest<User> getMe();

    //updateProfileImage
    IPRNumberRequest<Object> updateProfileImage(Map<String, String> params);

    //get post count
    IPRNumberRequest<Integer> getBoardPostCount(Map<String, String> params);

    //get post list
    IPRNumberRequest<Post> getBoardPostList(Map<String, String> params);

    //get category list
    IPRNumberRequest<Category> getCategoryAll(Map<String, String> params);

    //activate page
    IPRNumberRequest<Page> updatePage(Page params);

    //activate page
    IPRNumberRequest<Page> updatePageProperties(Page params);

    IPRNumberRequest<Page> updatePagePropertiesAll(Page params);

    //get post
    IPRNumberRequest<Post> getPost(Long params);

    //insert post
    IPRNumberRequest<Post> insertPost(Post params);

    //update post
    IPRNumberRequest<Post> updatePost(Post params);

    //delete post
    IPRNumberRequest<Object> deletePost(Long params);

    //get comment list
    IPRNumberRequest<Comment> getCommentAll(Long params);

    //insert comment
    IPRNumberRequest<Comment> insertComment(Comment params);

    //update comment
    IPRNumberRequest<Comment> updateComment(Comment params);

    //delete comment
    IPRNumberRequest<Object> deleteComment(Long params);

    //delete attachment
    IPRNumberRequest<Object> deleteAttachment(Long params);

    //copy attachment
    IPRNumberRequest<Attachment> copyAttachment(Map<String, String> params);

    //get default iage
    IPRNumberRequest<Attachment> getDefaultImageList(Map<String, String> params);

    IPRNumberRequest<Sns> getSnsLinkAll(Map<String, String> params);

    IPRNumberRequest<Sns> saveSnsLink(Sns params);

    IPRNumberRequest<Object> deleteSnsLinkByType(Map<String, String> params);

    //verification request
    IPRNumberRequest<Verification> verification(Map<String, String> params);

    //verification request
    IPRNumberRequest<Object> confirmVerification(Map<String, String> params);

    //leave
    IPRNumberRequest<Object> leave(Map<String, String> params);

    //cancel leave
    IPRNumberRequest<Object> cancelLeave(Map<String, String> params);

    //cash charge
    IPRNumberRequest<Cash> cashCharge(Cash params);

    //cash give
    IPRNumberRequest<Cash> cashGive(Map<String, String> params);

    //cash history
    IPRNumberRequest<Integer> getCashHistoryCount(Map<String, String> params);

    //cash history
    IPRNumberRequest<Cash> getCashHistoryList(Map<String, String> params);

    IPRNumberRequest<String> getCashHistoryTotalAmount(Map<String, String> params);

    //get prefix
    IPRNumberRequest<String> getPrefixNumber();

    //insert group
    IPRNumberRequest<Object> insertGroup(Group params);

    //update group name
    IPRNumberRequest<Object> updateGroupName(Map<String, String> params);

    //update priority
    IPRNumberRequest<Object> updateGroupPriorityAll(ParamsGroupPriority params);

    //delete group
    IPRNumberRequest<Object> deleteGroup(Map<String, String> params);

    //group list
    IPRNumberRequest<Group> getGroupAll(Map<String, String> params);

    //getCustomer
    IPRNumberRequest<Customer> getCustomerByMobile(Map<String, String> params);

    //insert customer list
    IPRNumberRequest<Customer> insertCustomerList(ParamsCustomerList params);

    //insert customer
    IPRNumberRequest<Customer> insertCustomer(Customer params);

    //insert customer
    IPRNumberRequest<Customer> updateCustomer(Customer params);

    //customer list
    IPRNumberRequest<Customer> getCustomerList(Map<String, String> params);

    //customer count
    IPRNumberRequest<Integer> getCustomerCount(Map<String, String> params);

    //customer list
    IPRNumberRequest<Customer> getUserCustomerList(Map<String, String> params);

    //customer count
    IPRNumberRequest<Integer> getUserCustomerCount(Map<String, String> params);

    //exclude customer list
    IPRNumberRequest<Customer> getExcludeCustomerList(Map<String, String> params);

    //exclude customer count
    IPRNumberRequest<Integer> getExcludeCustomerCount(Map<String, String> params);

    //add customer to group
    IPRNumberRequest<Object> addCustomerListToGroup(ParamsCustomerGroup params);

    //remove customer to group
    IPRNumberRequest<Object> removeCustomerListToGroup(ParamsCustomerGroup params);


    //getPendingApprovalAll
    IPRNumberRequest<Payment> getApprovalByOrderKey(String params);

    //insert group
    IPRNumberRequest<Object> insertFanGroup(Group params);

    //update group name
    IPRNumberRequest<Object> updateFanGroupName(Map<String, String> params);

    //update priority
    IPRNumberRequest<Object> updateFanGroupPriorityAll(ParamsGroupPriority params);

    //delete group
    IPRNumberRequest<Object> deleteFanGroup(Map<String, String> params);

    //group list
    IPRNumberRequest<Group> getFanGroupAll(Map<String, String> params);

    //add fan to group
    IPRNumberRequest<Object> addFanListToGroup(ParamsFanGroup params);

    //remove fan to group
    IPRNumberRequest<Object> removeFanListToGroup(ParamsFanGroup params);

    //fan list
    IPRNumberRequest<Fan> getFanList(Map<String, String> params);

    //fan count
    IPRNumberRequest<Integer> getFanCount(Map<String, String> params);

    //exclude fan list
    IPRNumberRequest<Fan> getExcludeFanList(Map<String, String> params);

    //exclude fan count
    IPRNumberRequest<Integer> getExcludeFanCount(Map<String, String> params);

    //insert sms
    IPRNumberRequest<Msg> insertSmsMsg(Msg params);

    //insert sms
    IPRNumberRequest<Msg> insertPushMsg(Msg params);

    //insert savedMsg
    IPRNumberRequest<SavedMsg> insertSavedMsg(SavedMsg params);

    //delete savedMsg
    IPRNumberRequest<Object> deleteSavedMsg(Map<String, String> params);

    //savedMsg count
    IPRNumberRequest<Integer> getSavedMsgCount();

    ///savedMsg list
    IPRNumberRequest<SavedMsg> getSavedMsgList(Map<String, String> params);

    //getReservedMsgAll
    IPRNumberRequest<Msg> getReservedMsgAll(Map<String, String> params);

    //delete customer
    IPRNumberRequest<Object> deleteCustomer(Map<String, String> params);

    IPRNumberRequest<Integer> getMsgCount(Map<String, String> params);

    IPRNumberRequest<Msg> getMsgList(Map<String, String> params);

    IPRNumberRequest<CountryConfig> getCountryConfigAll();

    IPRNumberRequest<Report> reporting(Report params);

    IPRNumberRequest<Object> updateContactList(ParamsContact params);

    IPRNumberRequest<Object> deleteContactList(ParamsContact params);

    IPRNumberRequest<Integer> getFriendCount();

    IPRNumberRequest<Friend> getFriendList(Map<String, String> params);

    IPRNumberRequest<Integer> getFriendPageCount(Map<String, String> params);

    IPRNumberRequest<Page> getFriendPageList(Map<String, String> params);

    IPRNumberRequest<Object> updatePushConfig(Map<String, String> params);

    IPRNumberRequest<Object> readComplete(Map<String, String> params);

    IPRNumberRequest<Integer> getMsgCountInBox();

    IPRNumberRequest<Msg> getMsgListInBox(Map<String, String> params);

    IPRNumberRequest<Object> deleteMsgInBox(Map<String, String> params);

    IPRNumberRequest<Object> agreeTermsList(Map<String, String> params);

    IPRNumberRequest<FranchiseGroup> getFranchiseGroupAll();

    IPRNumberRequest<Integer> getFranchiseCount(Map<String, String> params);

    IPRNumberRequest<Franchise> getFranchiseList(Map<String, String> params);

    IPRNumberRequest<ImgUrl> getIntroMovieAll(Map<String, String> params);

    IPRNumberRequest<Object> allocateVirtualNumberToPage(Map<String, String> params);

    IPRNumberRequest<Integer> getPushTargetCount(Map<String, String> params);

    IPRNumberRequest<Target> getPushTargetList(Map<String, String> params);

    IPRNumberRequest<Integer> getSmsTargetCount(Map<String, String> params);

    IPRNumberRequest<Target> getSmsTargetList(Map<String, String> params);

    IPRNumberRequest<Bol> getBolHistoryList(Map<String, String> params);

    IPRNumberRequest<String> getBolHistoryTotalAmount(Map<String, String> params);

    IPRNumberRequest<Object> insertAuthedNumber(Map<String, String> params);

    IPRNumberRequest<OutgoingNumber> getAuthedNumberAll();

    IPRNumberRequest<Object> deleteAuthedNumber(Map<String, String> params);

    IPRNumberRequest<Object> updateMainMovie(Map<String, String> params);

    IPRNumberRequest<Page> updateIntroMovieList(ParamsIntroMovie params);

    IPRNumberRequest<Object> sendNow(Map<String, String> params);

    IPRNumberRequest<Object> chargeBol(Cash params);

    IPRNumberRequest<Object> giftBols(Bol params);

    IPRNumberRequest<Integer> getGiftCount();

    IPRNumberRequest<BolGift> getGiftList(Map<String, String> params);

    IPRNumberRequest<Object> receiveGift(Map<String, String> params);

    IPRNumberRequest<Integer> getExistsNicknameUserCount(Map<String, String> params);

    IPRNumberRequest<User> getExistsNicknameUserList(Map<String, String> params);

    IPRNumberRequest<Bol> getBolHistoryListWithTargetList(Map<String, String> params);

    IPRNumberRequest<Integer> getBolHistoryCount(Map<String, String> params);

    IPRNumberRequest<Object> reviewReward(Bol params);

    IPRNumberRequest<Object> commentReward(Bol params);

    IPRNumberRequest<Object> cancelSend(Map<String, String> params);

    IPRNumberRequest<MobileGiftCategory> getMobileGiftCategoryAll();

    IPRNumberRequest<Integer> getMobileGiftCount(Map<String, String> params);

    IPRNumberRequest<MobileGift> getMobileGiftList(Map<String, String> params);

    IPRNumberRequest<No> prepareOrder(MobileGiftPurchase params);

    IPRNumberRequest<Object> completeOrder(Map<String, String> params);

    IPRNumberRequest<Object> cancelPrepareOrder(Map<String, String> params);

    IPRNumberRequest<Integer> getMobileGiftPurchaseCount();

    IPRNumberRequest<MobileGiftHistory> getMobileGiftPurchaseList(Map<String, String> params);

    IPRNumberRequest<Integer> getPurchaseGiftCount(Map<String, String> params);

    IPRNumberRequest<MobileGiftHistory> getMobileGiftPurchaseWithTargetAll(Map<String, String> params);

    IPRNumberRequest<MobileGiftHistory> getMobileGiftStatus(Map<String, String> params);

    IPRNumberRequest<Object> mobileGiftResend(Map<String, String> params);


    IPRNumberRequest<Object> updateAuthCodeByVerification(Map<String, String> params);



    IPRNumberRequest<Agent> getAgent(Map<String, String> params);

    IPRNumberRequest<Object> putMainGoods(Map<String, String> params);

    IPRNumberRequest<Object> postGoods(Goods params);

    IPRNumberRequest<Object> putGoods(Goods params);

    IPRNumberRequest<SubResultResponse<Goods>> getGoods(Map<String, String> params);

    IPRNumberRequest<Goods> getOneGoods(Map<String, String> params);

    IPRNumberRequest<Object> deleteGoods(Map<String, String> params);

    IPRNumberRequest<Object> putGoodsStatus(Map<String, String> params);

    IPRNumberRequest<SubResultResponse<GoodsReview>> getGoodsReview(Map<String, String> params);

    IPRNumberRequest<PageEval> getPageEval(Map<String, String> params);

    IPRNumberRequest<SubResultResponse<BuyGoods>> getBuyGoods(Map<String, String> params);

    IPRNumberRequest<Count> getBuyGoodsCount(Map<String, String> params);

    IPRNumberRequest<Price> getBuyGoodsPrice(Map<String, String> params);

    IPRNumberRequest<BuyGoods> getOneBuyGoods(Map<String, String> params);

    IPRNumberRequest<Object> postPageSeller(PageSeller params);

    IPRNumberRequest<String> getBuyOrderId(Map<String, String> params);

    IPRNumberRequest<Buy> postBuyCash(Buy params);

    IPRNumberRequest<Buy> getOneBuy(Map<String, String> params);

    IPRNumberRequest<PageGoodsCategory> getPageGoodsCategory(Map<String, String> params);

    IPRNumberRequest<Object> deletePageGoodsCategory(Map<String, String> params);

    IPRNumberRequest<PageGoodsCategory> postPageGoodsCategory(PageGoodsCategory params);

    IPRNumberRequest<PageGoodsCategory> putPageGoodsCategory(PageGoodsCategory params);

    IPRNumberRequest<Buy> getOneBuyDetail(Map<String, String> params);

    IPRNumberRequest<SubResultResponse<Buy>> getBuy(Map<String, String> params);

    IPRNumberRequest<OrderCount> getCountOrderProcess(Map<String, String> params);

    IPRNumberRequest<BuyGoodsTypePrice> getPriceGoodsType(Map<String, String> params);

    IPRNumberRequest<DeliveryTotalPrice> getDeliveryTotalPrice(Map<String, String> params);

    IPRNumberRequest<DeliveryTotalPrice> getDeliveryCompanyTotalPrice(Map<String, String> params);

    IPRNumberRequest<Float> getBuyPrice(Map<String, String> params);

    IPRNumberRequest<OrderTypeCount> getBuyCountOrderType(Map<String, String> params);

    IPRNumberRequest<Object> updateOrderProcess(Map<String, String> params);

    IPRNumberRequest<SubResultResponse<Delivery>> getDelivery(Map<String, String> params);

    IPRNumberRequest<PageManagement> getPageManagement(Map<String, String> params);

    IPRNumberRequest<Object> postPageManagement(PageManagement params);

    IPRNumberRequest<Count> getBuyCount(Map<String, String> params);

    IPRNumberRequest<Object> goodsNews(Map<String, String> params);

    IPRNumberRequest<HashTag>  getHashTagList();

    IPRNumberRequest<String>  getHashTagSearch(Map<String, String> params);

    IPRNumberRequest<GoodsNoticeInfo>  getPageGoodsInfo(Map<String, String> params);

    IPRNumberRequest<Object>  postPageGoodsInfo(GoodsNoticeInfo params);

    IPRNumberRequest<Object>  putPageGoodsInfo(GoodsNoticeInfo params);

    IPRNumberRequest<String>  getGoodsItemList();

    IPRNumberRequest<NoticeInfo>  getNoticeInfoListByItem(Map<String, String> params);

    IPRNumberRequest<String> putUserStatus(User params);

    IPRNumberRequest<String>  userCheck(Map<String, String> params);

    IPRNumberRequest<String> getLpngOrderId();

    IPRNumberRequest<Buy>  postBuyBiz(Buy params);

    IPRNumberRequest<LpngRes> postBuyLpngTag(Lpng params);

    IPRNumberRequest<Object> deleteCancelLpngTag(Map<String, String> params);

    IPRNumberRequest<LpngRes> postBuyLpngCheck(Lpng params);

    IPRNumberRequest<Object> buyGoodsListCancel(Map<String, String> params);

    IPRNumberRequest<GoodsReview> putGoodsReviewReply(GoodsReview params);

    IPRNumberRequest<Integer> checkAuthCode(Map<String, String> params);

    IPRNumberRequest<Integer> checkAndUpdateAuthCode(Map<String, String> params);

    IPRNumberRequest<Count> getGoodsLikeCount(Map<String, String> params);

}
