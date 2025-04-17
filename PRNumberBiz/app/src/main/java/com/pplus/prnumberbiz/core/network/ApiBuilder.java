package com.pplus.prnumberbiz.core.network;

import com.pplus.prnumberbiz.core.network.apis.IPRNumberBuilder;
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
import com.pplus.prnumberbiz.core.network.model.dto.CouponTemplate;
import com.pplus.prnumberbiz.core.network.model.dto.Coupon;
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
import com.pplus.prnumberbiz.core.network.model.dto.Terms;
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
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse;
import com.pplus.prnumberbiz.core.network.prnumber.IPRNumberDefaultRequest;
import com.pplus.prnumberbiz.core.network.prnumber.IPRNumberRequest;
import com.pplus.prnumberbiz.core.network.prnumber.PRNumberRequest;

import java.util.Map;

/**
 * Created by j2n on 2016. 9. 12..
 */
public class ApiBuilder implements IPRNumberBuilder {

    private final String TAG = ApiBuilder.class.getSimpleName();

    private ApiBuilder() {

    }

    public static ApiBuilder create() {

        return new ApiBuilder();
    }

    public IPRNumberDefaultRequest prnumberApi() {

        return PRNumberRequest.Builder.create();
    }

    //prnumber api

    @Override
    public IPRNumberRequest<Object> existsUser(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestExistsUser(params));
    }

    @Override
    public IPRNumberRequest<User> join(User params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestJoin(params));
    }

    @Override
    public IPRNumberRequest<User> levelup(User params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestLevelup(params));
    }

    @Override
    public IPRNumberRequest<AppVersion> appVersion(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestAppVersion(params));
    }

    @Override
    public IPRNumberRequest<User> login(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestLogin(params));
    }

    @Override
    public IPRNumberRequest<User> existsDevice(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestExistsDevice(params));
    }

    @Override
    public IPRNumberRequest<User> registDevice(ParamsRegDevice params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestRegistDevice(params));
    }

    @Override
    public IPRNumberRequest<Terms> getActiveTermsAll(String appKey) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetActiveTermsAll(appKey));
    }

    @Override
    public IPRNumberRequest<Terms> getNotSignedActiveTermsAll(String appKey) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetNotSignedActiveTermsAll(appKey));
    }

    @Override
    public IPRNumberRequest<User> activatePage(User params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestActivatePage(params));
    }

    @Override
    public IPRNumberRequest<User> startPage() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestStartPage());
    }

    @Override
    public IPRNumberRequest<User> getSession() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetSession());
    }

    @Override
    public IPRNumberRequest<User> reloadSession() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestReloadSession());
    }

    @Override
    public IPRNumberRequest<User> getUserByVerification(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetUserByVerification(params));
    }

    @Override
    public IPRNumberRequest<Object> changePasswordByVerification(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestChangePasswordByVerification(params));
    }

    @Override
    public IPRNumberRequest<Object> updateMobileByVerification(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateMobileByVerification(params));
    }

    @Override
    public IPRNumberRequest<User> getUserByLoginIdAndMobile(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetUserByLoginIdAndMobile(params));
    }

    @Override
    public IPRNumberRequest<Page> getPage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPage(params));
    }

    @Override
    public IPRNumberRequest<User> getMe() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMe());
    }

    @Override
    public IPRNumberRequest<Object> updateProfileImage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateProfileImage(params));
    }

    @Override
    public IPRNumberRequest<Integer> getBoardPostCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBoardPostCount(params));
    }

    @Override
    public IPRNumberRequest<Post> getBoardPostList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBoardPostList(params));
    }

    @Override
    public IPRNumberRequest<Category> getCategoryAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCategoryAll(params));
    }

    @Override
    public IPRNumberRequest<Page> updatePage(Page params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdatePage(params));
    }

    @Override
    public IPRNumberRequest<Page> updatePageProperties(Page params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdatePageProperties(params));
    }

    @Override
    public IPRNumberRequest<Page> updatePagePropertiesAll(Page params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdatePagePropertiesAll(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePageProfileImage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdatePageProfileImage(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePageBackgroundImage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdatePageBackgroundImage(params));
    }

    @Override
    public IPRNumberRequest<Page> updateIntroImageList(ParamsIntroImage params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateIntroImageList(params));
    }

    @Override
    public IPRNumberRequest<Attachment> getIntroImageAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetIntroImageAll(params));
    }

    @Override
    public IPRNumberRequest<Coord> getCoordByAddress(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCoordByAddress(params));
    }

    @Override
    public IPRNumberRequest<Integer> getNoticeCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetNoticeCount(params));
    }

    @Override
    public IPRNumberRequest<Notice> getNoticeList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetNoticeList(params));
    }

    @Override
    public IPRNumberRequest<Notice> getNotice(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetNotice(params));
    }

    @Override
    public IPRNumberRequest<FaqGroup> getFaqGroupAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFaqGroupAll(params));
    }

    @Override
    public IPRNumberRequest<Integer> getFaqCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFaqCount(params));
    }

    @Override
    public IPRNumberRequest<Faq> getFaqList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFaqList(params));
    }

    @Override
    public IPRNumberRequest<Faq> getFaq(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFaq(params));
    }

    @Override
    public IPRNumberRequest<Post> getPost(Long params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPost(params));
    }

    @Override
    public IPRNumberRequest<Post> insertPost(Post params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertPost(params));
    }

    @Override
    public IPRNumberRequest<Post> updatePost(Post params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdatePost(params));
    }

    @Override
    public IPRNumberRequest<Object> deletePost(Long params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeletePost(params));
    }

    @Override
    public IPRNumberRequest<Comment> getCommentAll(Long params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCommentAll(params));
    }

    @Override
    public IPRNumberRequest<Comment> insertComment(Comment params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertComment(params));
    }

    @Override
    public IPRNumberRequest<Comment> updateComment(Comment params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateComment(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteComment(Long params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteComment(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteAttachment(Long params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteAttachment(params));
    }

    @Override
    public IPRNumberRequest<Attachment> copyAttachment(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCopyAttachment(params));
    }

    @Override
    public IPRNumberRequest<Attachment> getDefaultImageList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetDefaultImageList(params));
    }

    @Override
    public IPRNumberRequest<Sns> getSnsLinkAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetSnsLinkAll(params));
    }

    @Override
    public IPRNumberRequest<Sns> saveSnsLink(Sns params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestSaveSnsLink(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteSnsLinkByType(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteSnsLinkByType(params));
    }

    @Override
    public IPRNumberRequest<Verification> verification(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestVerification(params));
    }

    @Override
    public IPRNumberRequest<Object> confirmVerification(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().confirmVerification(params));
    }

    @Override
    public IPRNumberRequest<Object> leave(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestLeave(params));
    }

    @Override
    public IPRNumberRequest<Object> cancelLeave(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCancelLeave(params));
    }

    @Override
    public IPRNumberRequest<Cash> cashCharge(Cash params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCashCharge(params));
    }

    @Override
    public IPRNumberRequest<Cash> cashGive(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCashGive(params));
    }

    @Override
    public IPRNumberRequest<Integer> getCashHistoryCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCashHistoryCount(params));
    }

    @Override
    public IPRNumberRequest<Cash> getCashHistoryList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCashHistoryList(params));
    }

    @Override
    public IPRNumberRequest<String> getCashHistoryTotalAmount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCashHistoryTotalAmount(params));
    }

    @Override
    public IPRNumberRequest<String> getPrefixNumber() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPrefixNumber());
    }

    @Override
    public IPRNumberRequest<Object> insertGroup(Group params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertGroup(params));
    }

    @Override
    public IPRNumberRequest<Object> updateGroupName(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateGroupName(params));
    }

    @Override
    public IPRNumberRequest<Object> updateGroupPriorityAll(ParamsGroupPriority params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateGroupPriorityAll(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteGroup(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteGroup(params));
    }

    @Override
    public IPRNumberRequest<Group> getGroupAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetGroupAll(params));
    }

    @Override
    public IPRNumberRequest<Customer> getCustomerByMobile(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCustomerByMobile(params));
    }

    @Override
    public IPRNumberRequest<Customer> insertCustomerList(ParamsCustomerList params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertCustomerList(params));
    }

    @Override
    public IPRNumberRequest<Customer> insertCustomer(Customer params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertCustomer(params));
    }

    @Override
    public IPRNumberRequest<Customer> updateCustomer(Customer params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateCustomer(params));
    }

    @Override
    public IPRNumberRequest<Customer> getCustomerList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCustomerList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getCustomerCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCustomerCount(params));
    }


    @Override
    public IPRNumberRequest<Customer> getExcludeCustomerList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetExcludeCustomerList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getExcludeCustomerCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetExcludeCustomerCount(params));
    }

    @Override
    public IPRNumberRequest<Object> addCustomerListToGroup(ParamsCustomerGroup params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestAddCustomerListToGroup(params));
    }

    @Override
    public IPRNumberRequest<Object> removeCustomerListToGroup(ParamsCustomerGroup params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestRemoveCustomerListFromGroup(params));
    }

    @Override
    public IPRNumberRequest<Payment> getApprovalByOrderKey(String params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetApprovalByOrderKey(params));
    }

    @Override
    public IPRNumberRequest<Object> insertFanGroup(Group params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertFanGroup(params));
    }

    @Override
    public IPRNumberRequest<Object> updateFanGroupName(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateFanGroupName(params));
    }

    @Override
    public IPRNumberRequest<Object> updateFanGroupPriorityAll(ParamsGroupPriority params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateFanGroupPriorityAll(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteFanGroup(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteFanGroup(params));
    }

    @Override
    public IPRNumberRequest<Group> getFanGroupAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFanGroupAll(params));
    }

    @Override
    public IPRNumberRequest<Object> addFanListToGroup(ParamsFanGroup params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestAddFanListToGroup(params));
    }

    @Override
    public IPRNumberRequest<Object> removeFanListToGroup(ParamsFanGroup params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestRemoveFanListFromGroup(params));
    }

    @Override
    public IPRNumberRequest<Fan> getFanList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFanList(params));
    }

    @Override
    public IPRNumberRequest<Fan> getExcludeFanList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetExcludeFanList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getExcludeFanCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetExcludeFanCount(params));
    }

    @Override
    public IPRNumberRequest<Integer> getFanCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFanCount(params));
    }

    @Override
    public IPRNumberRequest<Msg> insertSmsMsg(Msg params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertSmsMsg(params));
    }

    @Override
    public IPRNumberRequest<Msg> insertPushMsg(Msg params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertPushMsg(params));
    }

    @Override
    public IPRNumberRequest<SavedMsg> insertSavedMsg(SavedMsg params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertSavedMsg(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteSavedMsg(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteSavedMsg(params));
    }

    @Override
    public IPRNumberRequest<Integer> getSavedMsgCount() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetSavedMsgCount());
    }

    @Override
    public IPRNumberRequest<SavedMsg> getSavedMsgList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetSavedMsgList(params));
    }

    @Override
    public IPRNumberRequest<Msg> getReservedMsgAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestgGetReservedMsgAll(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteCustomer(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteCustomer(params));
    }

    @Override
    public IPRNumberRequest<Integer> getMsgCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMsgCount(params));
    }

    @Override
    public IPRNumberRequest<Msg> getMsgList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMsgList(params));
    }

    @Override
    public IPRNumberRequest<CountryConfig> getCountryConfigAll() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCountryConfigAll());
    }

    @Override
    public IPRNumberRequest<Report> reporting(Report params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestReporting(params));
    }

    @Override
    public IPRNumberRequest<Object> updateContactList(ParamsContact params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateContactList(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteContactList(ParamsContact params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteContactList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getFriendCount() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFriendCount());
    }

    @Override
    public IPRNumberRequest<Friend> getFriendList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFriendList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getFriendPageCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFriendPageCount(params));
    }

    @Override
    public IPRNumberRequest<Page> getFriendPageList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFriendPageList(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePushConfig(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdatePushConfig(params));
    }

    @Override
    public IPRNumberRequest<Object> readComplete(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestReadComplete(params));
    }

    @Override
    public IPRNumberRequest<Integer> getMsgCountInBox() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMsgCountInBox());
    }

    @Override
    public IPRNumberRequest<Msg> getMsgListInBox(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMsgListInBox(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteMsgInBox(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteMsgInBox(params));
    }

    @Override
    public IPRNumberRequest<Object> agreeTermsList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestAgreeTermsList(params));
    }

    @Override
    public IPRNumberRequest<FranchiseGroup> getFranchiseGroupAll() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFranchiseGroupAll());
    }

    @Override
    public IPRNumberRequest<Integer> getFranchiseCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFranchiseCount(params));
    }

    @Override
    public IPRNumberRequest<Franchise> getFranchiseList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetFranchiseList(params));
    }

    @Override
    public IPRNumberRequest<ImgUrl> getIntroMovieAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetIntroMovieAll(params));
    }

    @Override
    public IPRNumberRequest<Object> allocateVirtualNumberToPage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestAllocateVirtualNumberToPage(params));
    }

    @Override
    public IPRNumberRequest<Integer> getPushTargetCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPushTargetCount(params));
    }

    @Override
    public IPRNumberRequest<Target> getPushTargetList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPushTargetList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getSmsTargetCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetSmsTargetCount(params));
    }

    @Override
    public IPRNumberRequest<Target> getSmsTargetList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetSmsTargetList(params));
    }

    @Override
    public IPRNumberRequest<Bol> getBolHistoryList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBolHistoryList(params));
    }

    @Override
    public IPRNumberRequest<String> getBolHistoryTotalAmount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBolHistoryTotalAmount(params));
    }

    @Override
    public IPRNumberRequest<Object> insertAuthedNumber(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertAuthedNumber(params));
    }

    @Override
    public IPRNumberRequest<OutgoingNumber> getAuthedNumberAll() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetAuthedNumberAll());
    }

    @Override
    public IPRNumberRequest<Object> deleteAuthedNumber(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteAuthedNumber(params));
    }

    @Override
    public IPRNumberRequest<Object> updateMainMovie(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateMainMovie(params));
    }

    @Override
    public IPRNumberRequest<Page> updateIntroMovieList(ParamsIntroMovie params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateIntroMovieList(params));
    }

    @Override
    public IPRNumberRequest<Object> sendNow(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestSendNow(params));
    }

    @Override
    public IPRNumberRequest<Object> chargeBol(Cash params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestChargeBol(params));
    }

    @Override
    public IPRNumberRequest<Object> giftBols(Bol params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGiftBols(params));
    }

    @Override
    public IPRNumberRequest<Customer> getUserCustomerList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetUserCustomerList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getUserCustomerCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetUserCustomerCount(params));
    }

    @Override
    public IPRNumberRequest<Integer> getGiftCount() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetGiftCount());
    }

    @Override
    public IPRNumberRequest<BolGift> getGiftList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetGiftList(params));
    }

    @Override
    public IPRNumberRequest<Object> receiveGift(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestReceiveGift(params));
    }

    @Override
    public IPRNumberRequest<Integer> getExistsNicknameUserCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetExistsNicknameUserCount(params));
    }

    @Override
    public IPRNumberRequest<User> getExistsNicknameUserList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetExistsNicknameUserList(params));
    }

    @Override
    public IPRNumberRequest<Bol> getBolHistoryListWithTargetList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBolHistoryWithTargetList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getBolHistoryCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBolHistoryCount(params));
    }

    @Override
    public IPRNumberRequest<Object> reviewReward(Bol params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestReviewReward(params));
    }

    @Override
    public IPRNumberRequest<Object> commentReward(Bol params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCommentReward(params));
    }

    @Override
    public IPRNumberRequest<Object> cancelSend(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCancelSend(params));
    }

    @Override
    public IPRNumberRequest<MobileGiftCategory> getMobileGiftCategoryAll() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMobileGiftCategoryAll());
    }

    @Override
    public IPRNumberRequest<Integer> getMobileGiftCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMobileGiftCount(params));
    }

    @Override
    public IPRNumberRequest<MobileGift> getMobileGiftList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMobileGiftList(params));
    }

    @Override
    public IPRNumberRequest<No> prepareOrder(MobileGiftPurchase params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPrepareOrder(params));
    }

    @Override
    public IPRNumberRequest<Object> completeOrder(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCompleteOrder(params));
    }

    @Override
    public IPRNumberRequest<Object> cancelPrepareOrder(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCancelPrepareOrder(params));
    }

    @Override
    public IPRNumberRequest<Integer> getMobileGiftPurchaseCount() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMobileGiftPurchaseCount());
    }

    @Override
    public IPRNumberRequest<MobileGiftHistory> getMobileGiftPurchaseList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMobileGiftPurchaseList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getPurchaseGiftCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPurchaseGiftCount(params));
    }

    @Override
    public IPRNumberRequest<MobileGiftHistory> getMobileGiftPurchaseWithTargetAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMobileGiftPurchaseWithTargetAll(params));
    }

    @Override
    public IPRNumberRequest<MobileGiftHistory> getMobileGiftStatus(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetMobileGiftStatus(params));
    }

    @Override
    public IPRNumberRequest<Object> mobileGiftResend(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestMobileGiftResend(params));
    }

    @Override
    public IPRNumberRequest<Object> updateAuthCodeByVerification(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateAuthCodeByVerification(params));
    }


    @Override
    public IPRNumberRequest<Agent> getAgent(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetAgent(params));
    }

    @Override
    public IPRNumberRequest<Object> putMainGoods(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPutMainGoods(params));
    }

    @Override
    public IPRNumberRequest<Object> postGoods(Goods params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPostGoods(params));
    }

    @Override
    public IPRNumberRequest<Object> putGoods(Goods params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPutGoods(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Goods>> getGoods(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetGoods(params));
    }

    @Override
    public IPRNumberRequest<Goods> getOneGoods(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetOneGoods(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteGoods(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteGoods(params));
    }

    @Override
    public IPRNumberRequest<Object> putGoodsStatus(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPutGoodsStatus(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<GoodsReview>> getGoodsReview(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetGoodsReview(params));
    }

    @Override
    public IPRNumberRequest<PageEval> getPageEval(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPageEval(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<BuyGoods>> getBuyGoods(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBuyGoods(params));
    }

    @Override
    public IPRNumberRequest<Count> getBuyGoodsCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBuyGoodsCount(params));
    }

    @Override
    public IPRNumberRequest<Price> getBuyGoodsPrice(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBuyGoodsPrice(params));
    }

    @Override
    public IPRNumberRequest<BuyGoods> getOneBuyGoods(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetOneBuyGoods(params));
    }

    @Override
    public IPRNumberRequest<Object> postPageSeller(PageSeller params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPostPageSeller(params));
    }

    @Override
    public IPRNumberRequest<String> getBuyOrderId(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBuyOrderId(params));
    }

    @Override
    public IPRNumberRequest<Buy> postBuyCash(Buy params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPostBuyCash(params));
    }

    @Override
    public IPRNumberRequest<Buy> getOneBuy(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetOneBuy(params));
    }

    @Override
    public IPRNumberRequest<PageGoodsCategory> getPageGoodsCategory(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPageGoodsCategory(params));
    }

    @Override
    public IPRNumberRequest<Object> deletePageGoodsCategory(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeletePageGoodsCategory(params));
    }

    @Override
    public IPRNumberRequest<PageGoodsCategory> postPageGoodsCategory(PageGoodsCategory params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPostPageGoodsCategory(params));
    }

    @Override
    public IPRNumberRequest<PageGoodsCategory> putPageGoodsCategory(PageGoodsCategory params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPutPageGoodsCategory(params));
    }

    @Override
    public IPRNumberRequest<Buy> getOneBuyDetail(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetOneBuyDetail(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Buy>> getBuy(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBuy(params));
    }

    @Override
    public IPRNumberRequest<OrderCount> getCountOrderProcess(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetCountOrderProcess(params));
    }

    @Override
    public IPRNumberRequest<BuyGoodsTypePrice> getPriceGoodsType(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPriceGoodsType(params));
    }

    @Override
    public IPRNumberRequest<DeliveryTotalPrice> getDeliveryTotalPrice(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetDeliveryTotalPrice(params));
    }

    @Override
    public IPRNumberRequest<DeliveryTotalPrice> getDeliveryCompanyTotalPrice(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetDeliveryCompanyTotalPrice(params));
    }

    @Override
    public IPRNumberRequest<Float> getBuyPrice(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBuyPrice(params));
    }

    @Override
    public IPRNumberRequest<OrderTypeCount> getBuyCountOrderType(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBuyCountOrderType(params));
    }

    @Override
    public IPRNumberRequest<Object> updateOrderProcess(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUpdateOrderProcess(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Delivery>> getDelivery(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetDelivery(params));
    }

    @Override
    public IPRNumberRequest<PageManagement> getPageManagement(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPageManagement(params));
    }

    @Override
    public IPRNumberRequest<Object> postPageManagement(PageManagement params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPostPageManagement(params));
    }

    @Override
    public IPRNumberRequest<Count> getBuyCount(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetBuyCount(params));
    }

    @Override
    public IPRNumberRequest<Object> goodsNews(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGoodsNews(params));
    }

    @Override
    public IPRNumberRequest<HashTag> getHashTagList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetHashTagList());
    }

    @Override
    public IPRNumberRequest<String> getHashTagSearch(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetHashTagSearch(params));
    }

    @Override
    public IPRNumberRequest<GoodsNoticeInfo> getPageGoodsInfo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetPageGoodsInfo(params));
    }

    @Override
    public IPRNumberRequest<Object> postPageGoodsInfo(GoodsNoticeInfo params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPostPageGoodsInfo(params));
    }

    @Override
    public IPRNumberRequest<Object> putPageGoodsInfo(GoodsNoticeInfo params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPutPageGoodsInfo(params));
    }

    @Override
    public IPRNumberRequest<String> getGoodsItemList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetGoodsItemList());
    }

    @Override
    public IPRNumberRequest<NoticeInfo> getNoticeInfoListByItem(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetNoticeInfoListByItem(params));
    }

    @Override
    public IPRNumberRequest<String> putUserStatus(User params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPutUserStatus(params));
    }

    @Override
    public IPRNumberRequest<String> userCheck(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestUserCheck(params));
    }

    @Override
    public IPRNumberRequest<String> getLpngOrderId() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetLpngOrderId());
    }

    @Override
    public IPRNumberRequest<Buy> postBuyBiz(Buy params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPostBuyBiz(params));
    }

    @Override
    public IPRNumberRequest<LpngRes> postBuyLpngTag(Lpng params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPostBuyLpngTag(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteCancelLpngTag(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteCancelLpngTag(params));
    }

    @Override
    public IPRNumberRequest<LpngRes> postBuyLpngCheck(Lpng params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPostBuyLpngCheck(params));
    }

    @Override
    public IPRNumberRequest<Object> buyGoodsListCancel(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestBuyGoodsListCancel(params));
    }

    @Override
    public IPRNumberRequest<GoodsReview> putGoodsReviewReply(GoodsReview params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestPutGoodsReviewReply(params));
    }

    @Override
    public IPRNumberRequest<Integer> checkAuthCode(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCheckAuthCode(params));
    }

    @Override
    public IPRNumberRequest<Integer> checkAndUpdateAuthCode(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCheckAndUpdateAuthCode(params));
    }

    @Override
    public IPRNumberRequest<Count> getGoodsLikeCount(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestGetGoodsLikeCount(params));
    }
}

