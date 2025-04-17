package com.pplus.prnumberuser.core.network;

import com.pplus.prnumberuser.core.network.apis.IPRNumberBuilder;
import com.pplus.prnumberuser.core.network.model.dto.*;
import com.pplus.prnumberuser.core.network.model.request.params.ParamsContact;
import com.pplus.prnumberuser.core.network.model.request.params.ParamsGroupPriority;
import com.pplus.prnumberuser.core.network.model.request.params.ParamsIntroImage;
import com.pplus.prnumberuser.core.network.model.request.params.ParamsJoinEvent;
import com.pplus.prnumberuser.core.network.model.request.params.ParamsPlusGroup;
import com.pplus.prnumberuser.core.network.model.request.params.ParamsRegDevice;
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse;
import com.pplus.prnumberuser.core.network.prnumber.IPRNumberDefaultRequest;
import com.pplus.prnumberuser.core.network.prnumber.IPRNumberRequest;
import com.pplus.prnumberuser.core.network.prnumber.PRNumberRequest;

import org.jetbrains.annotations.NotNull;

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

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().existsUser(params));
    }

    @Override
    public IPRNumberRequest<User> join(User params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().join(params));
    }

    @Override
    public IPRNumberRequest<AppVersion> appVersion(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().appVersion(params));
    }

    @Override
    public IPRNumberRequest<User> login(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().login(params));
    }

    @Override
    public IPRNumberRequest<User> existsDevice(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().existsDevice(params));
    }

    @Override
    public IPRNumberRequest<User> registDevice(ParamsRegDevice params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().registDevice(params));
    }

    @Override
    public IPRNumberRequest<Terms> getActiveTermsAll(String appType) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getActiveTermsAll(appType));
    }

    @Override
    public IPRNumberRequest<Terms> getNotSignedActiveTermsAll(String appType) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNotSignedActiveTermsAll(appType));
    }

    @Override
    public IPRNumberRequest<User> activatePage(User params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestActivatePage(params));
    }

    @Override
    public IPRNumberRequest<User> getSession() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSession());
    }

    @Override
    public IPRNumberRequest<User> reloadSession() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestReloadSession());
    }

    @Override
    public IPRNumberRequest<User> getUserByVerification(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getUserByVerification(params));
    }

    @Override
    public IPRNumberRequest<Object> changePasswordByVerification(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestChangePasswordByVerification(params));
    }

    @Override
    public IPRNumberRequest<User> getUserByLoginIdAndMobile(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getUserByLoginIdAndMobile(params));
    }

    @Override
    public IPRNumberRequest<User> getMe() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMe());
    }

    @Override
    public IPRNumberRequest<Object> updateProfileImage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateProfileImage(params));
    }

    @Override
    public IPRNumberRequest<Integer> getBoardPostCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBoardPostCount(params));
    }

    @Override
    public IPRNumberRequest<Post> getBoardPostList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBoardPostList(params));
    }

    @Override
    public IPRNumberRequest<Category> getCategoryAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCategoryAll(params));
    }

    @Override
    public IPRNumberRequest<Category> getCategoryList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCategoryList(params));
    }

    @Override
    public IPRNumberRequest<Page> updatePage(Page params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePage(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePageProfileImage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePageProfileImage(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePageBackgroundImage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePageBackgroundImage(params));
    }

    @Override
    public IPRNumberRequest<Page> updateIntroImageList(ParamsIntroImage params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateIntroImageList(params));
    }

    @Override
    public IPRNumberRequest<Attachment> getIntroImageAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getIntroImageAll(params));
    }

    @Override
    public IPRNumberRequest<PageImage> getPageImageAll(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageImageAll(params));
    }

    @Override
    public IPRNumberRequest<Coord> getCoordByAddress(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCoordByAddress(params));
    }

    @Override
    public IPRNumberRequest<Integer> getNoticeCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNoticeCount(params));
    }

    @Override
    public IPRNumberRequest<Notice> getNoticeList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNoticeList(params));
    }

    @Override
    public IPRNumberRequest<Notice> getNotice(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNotice(params));
    }

    @Override
    public IPRNumberRequest<FaqGroup> getFaqGroupAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getFaqGroupAll(params));
    }

    @Override
    public IPRNumberRequest<Integer> getFaqCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getFaqCount(params));
    }

    @Override
    public IPRNumberRequest<Faq> getFaqList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getFaqList(params));
    }

    @Override
    public IPRNumberRequest<Faq> getFaq(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getFaq(params));
    }

    @Override
    public IPRNumberRequest<Post> getPost(long params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPost(params));
    }

    @Override
    public IPRNumberRequest<Post> insertPost(Post params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().insertPost(params));
    }

    @Override
    public IPRNumberRequest<Post> updatePost(Post params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePost(params));
    }

    @Override
    public IPRNumberRequest<Object> deletePost(long params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeletePost(params));
    }

    @Override
    public IPRNumberRequest<Comment> getCommentAll(long params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCommentAll(params));
    }

    @Override
    public IPRNumberRequest<Comment> insertComment(Comment params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertComment(params));
    }

    @Override
    public IPRNumberRequest<Comment> updateComment(Comment params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateComment(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteComment(long params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteComment(params));
    }

    @Override
    public IPRNumberRequest<Attachment> copyAttachment(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCopyAttachment(params));
    }

    @Override
    public IPRNumberRequest<Attachment> getDefaultImageList() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getDefaultImageList());
    }

    @Override
    public IPRNumberRequest<Sns> getSnsLinkAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSnsLinkAll(params));
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
    public IPRNumberRequest<Cash> cashHistoryList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCashHistoryList(params));
    }

    @Override
    public IPRNumberRequest<String> getCashHistoryTotalAmount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCashHistoryTotalAmount(params));
    }

    @Override
    public IPRNumberRequest<String> getPrefixNumber() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPrefixNumber());
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

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSavedMsgCount());
    }

    @Override
    public IPRNumberRequest<SavedMsg> getSavedMsgList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSavedMsgList(params));
    }

    @Override
    public IPRNumberRequest<Msg> getReservedMsgAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getReservedMsgAll(params));
    }

    @Override
    public IPRNumberRequest<Page> getPage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPage(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Page2> getPage2(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPage2(params));
    }

    @Override
    public IPRNumberRequest<Object> insertPlusGroup(Group params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertPlusGroup(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePlusGroupName(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePlusGroupName(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePlusGroupPriorityAll(ParamsGroupPriority params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePlusGroupPriorityAll(params));
    }

    @Override
    public IPRNumberRequest<Object> deletePlusGroup(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeletePlusGroup(params));
    }

    @Override
    public IPRNumberRequest<Group> getPlusGroupAll() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPlusGroupAll());
    }

    @Override
    public IPRNumberRequest<Object> addPlusListToGroup(ParamsPlusGroup params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestAddPlusListToGroup(params));
    }

    @Override
    public IPRNumberRequest<Object> removePlusListToGroup(ParamsPlusGroup params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestRemovePlusListFromGroup(params));
    }

    @Override
    public IPRNumberRequest<Plus> getPlusList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPlusList(params));
    }

    @Override
    public IPRNumberRequest<Plus> getOnlyPlus(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getOnlyPlus(params));
    }

    @Override
    public IPRNumberRequest<Integer> getPlusCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPlusCount(params));
    }

    @Override
    public IPRNumberRequest<Plus> getExcludePlusList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getExcludePlusList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getExcludePlusCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getExcludePlusCount(params));
    }

    @Override
    public IPRNumberRequest<Plus> insertPlus(Plus params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestInsertPlus(params));
    }

    @Override
    public IPRNumberRequest<Object> deletePlus(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeletePlus(params));
    }

    @Override
    public IPRNumberRequest<Object> deletePlusByPage(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeletePlusByPage(params));
    }

    @Override
    public IPRNumberRequest<Object> existPlusByPage(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestExistPlusByPage(params));
    }

    @Override
    public IPRNumberRequest<Integer> getPageCountByTheme(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageCountByTheme(params));
    }

    @Override
    public IPRNumberRequest<Page> getPageListByTheme(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListByTheme(params));
    }

    @Override
    public IPRNumberRequest<Integer> getPageCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageCount(params));
    }

    @Override
    public IPRNumberRequest<Page> getPageList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Integer> getPageCountByManageSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageCountByManageSeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Page> getPageListByManageSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListByManageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<CountryConfig> getCountryConfigAll() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCountryConfigAll());
    }

    @Override
    public IPRNumberRequest<Object> updateNickname(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateNickname(params));
    }

    @Override
    public IPRNumberRequest<Object> agreeTermsList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestAgreeTermsList(params));
    }

    @Override
    public IPRNumberRequest<Report> reporting(Report params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestReporting(params));
    }

    @Override
    public IPRNumberRequest<Object> updateContactList(ParamsContact params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateContactList(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteContactList(ParamsContact params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteContactList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getFriendCount() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getFriendCount());
    }

    @Override
    public IPRNumberRequest<Friend> getFriendList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getFriendList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getAllFriendCount() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getAllFriendCount());
    }

    @Override
    public IPRNumberRequest<Friend> getAllFriendList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getAllFriendList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getFriendPageCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getFriendPageCount(params));
    }

    @Override
    public IPRNumberRequest<Page> getFriendPageList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getFriendPageList(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePushConfig(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePushConfig(params));
    }

    @Override
    public IPRNumberRequest<Object> readComplete(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestReadComplete(params));
    }

    @Override
    public IPRNumberRequest<Integer> getMsgCountInBox() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMsgCountInBox());
    }

    @Override
    public IPRNumberRequest<Msg> getMsgListInBox(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMsgListInBox(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteMsgInBox(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteMsgInBox(params));
    }

    @Override
    public IPRNumberRequest<Page> getPageByNumber(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageByNumber(params));
    }

    @Override
    public IPRNumberRequest<Integer> getPageCountByArea(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageCountByArea(params));
    }

    @Override
    public IPRNumberRequest<Page> getPageListByArea(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListByArea(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Page> getPageListByAreaAndManageSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListByAreaAndManageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Integer> getPageCountByAreaByTheme(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageCountByAreaByTheme(params));
    }

    @Override
    public IPRNumberRequest<Page> getPageListByAreaByTheme(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListByAreaByTheme(params));
    }

    @Override
    public IPRNumberRequest<Bol> getBolHistoryList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBolHistoryList(params));
    }

    @Override
    public IPRNumberRequest<Object> bolExchange(Exchange params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().bolExchange(params));
    }

    @Override
    public IPRNumberRequest<Integer> getExistsNicknameFriendCount() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getExistsNicknameFriendCount());
    }

    @Override
    public IPRNumberRequest<Friend> getExistsNicknameFriendList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getExistsNicknameFriendList(params));
    }

    @Override
    public IPRNumberRequest<ImgUrl> getIntroMovieAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getIntroMovieAll(params));
    }

    @Override
    public IPRNumberRequest<Integer> getExistsNicknameUserCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getExistsNicknameUserCount(params));
    }

    @Override
    public IPRNumberRequest<User> getExistsNicknameUserList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getExistsNicknameUserList(params));
    }

    @Override
    public IPRNumberRequest<User> updateUser(User params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateUser(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<User> updateExternal(@NotNull User params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateExternal(params));
    }

    @Override
    public IPRNumberRequest<Object> updateMobileByVerification(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateMobileByVerification(params));
    }

    @Override
    public IPRNumberRequest<Bol> getBolHistoryListWithTargetList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBolHistoryWithTargetList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getBolHistoryCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBolHistoryCount(params));
    }

    @Override
    public IPRNumberRequest<User> getSameFriendAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSameFriendAll(params));
    }

    @Override
    public IPRNumberRequest<Integer> checkAuthCodeForUser(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCheckAuthCodeForUser(params));
    }

    @Override
    public IPRNumberRequest<Integer> getEventCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventCount(params));
    }

    @Override
    public IPRNumberRequest<Event> getEventList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getEventCountByPageSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventCountByPageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Event> getEventListByPageSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventListByPageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Event> getActiveEventByPageSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getActiveEventByPageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Event> getEventByNumber(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventByNumber(params));
    }

    @Override
    public IPRNumberRequest<EventResult> joinEvent(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().joinEvent(params));
    }

    @Override
    public IPRNumberRequest<EventResult> serializableJoinEvent(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().serializableJoinEvent(params));
    }

    @Override
    public IPRNumberRequest<Object> writeImpression(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().writeImpression(params));
    }

    @Override
    public IPRNumberRequest<Object> updateEventWinAddress(EventWin params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateEventWinAddress(params));
    }

    @Override
    public IPRNumberRequest<EventBanner> getBannerAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBannerAll(params));
    }

    @Override
    public IPRNumberRequest<EventGift> getGiftAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGiftAll(params));
    }

    @Override
    public IPRNumberRequest<Integer> getWinCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getWinCount(params));
    }

    @Override
    public IPRNumberRequest<EventWin> getWinList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getWinList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getEventWinCountByGiftSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventWinCountByGiftSeqNo(params));
    }

    @Override
    public IPRNumberRequest<EventWin> getEventWinListByGiftSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventWinListByGiftSeqNo(params));
    }

    @Override
    public IPRNumberRequest<EventWin> getWinBySeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getWinBySeqNo(params));
    }

    @Override
    public IPRNumberRequest<Integer> getUserWinCount() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getUserWinCount());
    }

    @Override
    public IPRNumberRequest<EventWin> getUserWinList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getUserWinList(params));
    }

    @Override
    public IPRNumberRequest<User> getInviteRanking(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getInviteRanking(params));
    }

    @Override
    public IPRNumberRequest<User> getRewardRanking(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getRewardRanking(params));
    }

    @Override
    public IPRNumberRequest<User> getMyInviteRanking() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMyInviteRanking());
    }

    @Override
    public IPRNumberRequest<User> getMyRewardRanking() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMyRewardRanking());
    }

    @Override
    public IPRNumberRequest<EventExist> existsEventResult(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestExistsEventResult(params));
    }

    @Override
    public IPRNumberRequest<EventWin> getWinAll(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getWinAll(params));
    }

    @Override
    public IPRNumberRequest<Event> getEvent(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEvent(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Event> getEventByCode(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventByCode(params));
    }

    @Override
    public IPRNumberRequest<EventResult> joinWithPropertiesEvent(ParamsJoinEvent params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestJoinWithPropertiesEvent(params));
    }

    @Override
    public IPRNumberRequest<EventGroup> getEventGroupAll() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventGroupAll());
    }

    @Override
    public IPRNumberRequest<Integer> getEventCountByGroup(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventCountByGroup(params));
    }

    @Override
    public IPRNumberRequest<Event> getEventListByGroup(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventListByGroup(params));
    }

    @Override
    public IPRNumberRequest<Integer> getWinAnnounceCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getWinAnnounceCount(params));
    }

    @Override
    public IPRNumberRequest<Event> getWinAnnounceList(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getWinAnnounceList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getUserCountByRecommendKey(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getUserCountByRecommendKey(params));
    }

    @Override
    public IPRNumberRequest<User> getUserListByRecommendKey(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getUserListByRecommendKey(params));
    }

    @Override
    public IPRNumberRequest<Bol> getBolHistoryWithTarget(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBolHistoryWithTarget(params));
    }

    @Override
    public IPRNumberRequest<Object> postGoodsReview(GoodsReview params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postGoodsReview(params));
    }

    @Override
    public IPRNumberRequest<Object> putGoodsReview(GoodsReview params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().putGoodsReview(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<GoodsReview>> getGoodsReview(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsReview(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteGoodsReview(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteGoodsReview(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getPageLisWithGoods(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageLisWithGoods(params));
    }

    @Override
    public IPRNumberRequest<PageEval> getPageEval(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageEval(params));
    }

    @Override
    public IPRNumberRequest<Buy> postBuy(Buy params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postBuy(params));
    }

    @Override
    public IPRNumberRequest<Buy> postBuyHot(Buy params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postBuyHot(params));
    }

    @Override
    public IPRNumberRequest<Buy> postBuyShop(Buy params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postBuyShop(params));
    }

    @Override
    public IPRNumberRequest<Buy> postBuyShip(Buy params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postBuyShip(params));
    }

    @Override
    public IPRNumberRequest<Buy> postBuyQr(Buy params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postBuyQr(params));
    }

    @Override
    public IPRNumberRequest<Lpng> postBuyLpng(Lpng params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postBuyLpng(params));
    }

    @Override
    public IPRNumberRequest<FTLink> postBuyFTLinkPay(FTLink params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postBuyFTLinkPay(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteBuy(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteBuy(params));
    }

    @Override
    public IPRNumberRequest<Buy> getOneBuy(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getOneBuy(params));
    }

    @Override
    public IPRNumberRequest<Buy> getOneBuyDetail(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getOneBuyDetail(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Buy>> getBuy(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBuy(params));
    }

    @Override
    public IPRNumberRequest<String> getBuyOrderId() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBuyOrderId());
    }

    @Override
    public IPRNumberRequest<String> getLpngOrderId() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLpngOrderId());
    }

    @Override
    public IPRNumberRequest<SubResultResponse<BuyGoods>> getBuyGoods(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBuyGoods(params));
    }

    @Override
    public IPRNumberRequest<BuyGoods> getOneBuyGoods(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getOneBuyGoods(params));
    }

    @Override
    public IPRNumberRequest<Count> getGoodsReviewCount(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsReviewCount(params));
    }

    @Override
    public IPRNumberRequest<Count> getBuyCountByGoodsSeqNo(Map<String, String> params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBuyCountByGoodsSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Count> getGoodsLikeCount(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsLikeCount(params));
    }

    @Override
    public IPRNumberRequest<GoodsLike> postGoodsLike(GoodsLike params) {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postGoodsLike(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteGoodsLike(GoodsLike params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestDeleteGoodsLike(params));
    }

    @Override
    public IPRNumberRequest<GoodsLike> getGoodsLikeOne(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsLikeOne(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<GoodsLike>> getGoodsLike(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsLike(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<GoodsLike>> getGoodsLikeBySalesType(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsLikeBySalesType(params));
    }

    @Override
    public IPRNumberRequest<Object> putSnsAccount(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().putSnsAccount(params));
    }

    @Override
    public IPRNumberRequest<Integer> getUserFriendCount() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getUserFriendCount());
    }

    @Override
    public IPRNumberRequest<Friend> getUserFriendList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getUserFriendList(params));
    }

    @Override
    public IPRNumberRequest<String> userCheck(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().userCheck(params));
    }

    @Override
    public IPRNumberRequest<PageGoodsCategory> getPageGoodsCategory(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageGoodsCategory(params));
    }

    @Override
    public IPRNumberRequest<PageManagement> getPageManagement(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageManagement(params));
    }

    @Override
    public IPRNumberRequest<Lotto> getLotto() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLotto());
    }

    @Override
    public IPRNumberRequest<LottoGift> getLottoPlayGiftList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoPlayGiftList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getLottoWinnerCount(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoWinnerCount(params));
    }

    @Override
    public IPRNumberRequest<EventWin> getLottoWinnerList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoWinnerList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getLottoCount(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoCount(params));
    }

    @Override
    public IPRNumberRequest<Event> getLottoList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getTicketHistoryCount(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getTicketHistoryCount(params));
    }

    @Override
    public IPRNumberRequest<Bol> getTicketHistory(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getTicketHistory(params));
    }

    @Override
    public IPRNumberRequest<Integer> getLottoTicketHistoryCount(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoTicketHistoryCount(params));
    }

    @Override
    public IPRNumberRequest<Bol> getLottoTicketHistory(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoTicketHistory(params));
    }

    @Override
    public IPRNumberRequest<EventWin> getLottoResult(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoResult(params));
    }

    @Override
    public IPRNumberRequest<Integer> getLottoJoinCont(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoJoinCount(params));
    }

    @Override
    public IPRNumberRequest<Event> getLottoJoinList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLottoJoinList(params));
    }

    @Override
    public IPRNumberRequest<String> getRandomPrnumber() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getRandomPrnumber());
    }

    @Override
    public IPRNumberRequest<HashTag> getHashTagList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getHashTagList());
    }

    @Override
    public IPRNumberRequest<Object> cpeReport(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCpeReport(params));
    }

    @Override
    public IPRNumberRequest<Object> cpaReport(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCpaReport(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePushKey(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePushKey(params));
    }

    @Override
    public IPRNumberRequest<Buy> checkBootPay(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().requestCheckBootPay(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Plus2>> getPlusLisWithPlusGoods(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPlusListWithPlusGoods(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Plus2>> getPlusListWithNews(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPlusListWithNews(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Goods>> getGoodsListByPageSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsListByPageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePayPassword(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePayPassword(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePayPasswordWithVerification(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePayPasswordWithVerification(params));
    }

    @Override
    public IPRNumberRequest<Object> checkPayPassword(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().checkPayPassword(params));
    }

    @Override
    public IPRNumberRequest<Card> getCardList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCardList());
    }

    @Override
    public IPRNumberRequest<Card> updateRepresentCard(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateRepresentCard(params));
    }

    @Override
    public IPRNumberRequest<Card> postCardRegister(CardReq params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postCardRegister(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteCard(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().deleteCard(params));
    }

    @Override
    public IPRNumberRequest<Object> updateBuyPlusTerms(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateBuyPlusTerms(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePlusPush(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePlusPush(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePushActivate(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePushActivate(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePlusGift(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePlusGift(params));
    }

    @Override
    public IPRNumberRequest<ShippingSite> getShippingSiteList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getShippingSiteList());
    }

    @Override
    public IPRNumberRequest<ShippingSite> insertShippingSite(ShippingSite params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().insertShippingSite(params));
    }

    @Override
    public IPRNumberRequest<ShippingSite> updateShippingSite(ShippingSite params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateShippingSite(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteShippingSite(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().deleteShippingSite(params));
    }

    @Override
    public IPRNumberRequest<CategoryMajor> getCategoryMajorOnlyList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCategoryMajorOnlyList());
    }

    @Override
    public IPRNumberRequest<CategoryMajor> getCategoryMajorOnly(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCategoryMajorOnly(params));
    }

    @Override
    public IPRNumberRequest<CategoryMajor> getCategoryMajor() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCategoryMajor());
    }

    @Override
    public IPRNumberRequest<CategoryMinor> getCategoryMinorList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCategoryMinorList(params));
    }

    @Override
    public IPRNumberRequest<GoodsImage> getGoodsImageList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsImageList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getBuyCountAll() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBuyCountAll());
    }

    @Override
    public IPRNumberRequest<Integer> getGoodsReviewCountAll() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsReviewCountAll());
    }

    @Override
    public IPRNumberRequest<Juso> getJusoList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getJusoList(params));
    }

    @Override
    public IPRNumberRequest<Province> getDoList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getDoList());
    }

    @Override
    public IPRNumberRequest<CategoryFavorite> getMyFavoriteCategoryList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMyFavoriteCategoryList());
    }

    @Override
    public IPRNumberRequest<CategoryFavorite> insertCategoryFavorite(CategoryFavorite params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().insertCategoryFavorite(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteCategoryFavorite(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().deleteCategoryFavorite(params));
    }

    @Override
    public IPRNumberRequest<Object> updateActiveArea1(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateActiveArea1(params));
    }

    @Override
    public IPRNumberRequest<Object> updateActiveArea2(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateActiveArea2(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> updateQrImage(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateQrImage(params));
    }

    @Override
    public IPRNumberRequest<Object> updateUseGift(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateUseGift(params));
    }

    @Override
    public IPRNumberRequest<Boolean> checkIslandsRegion(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().checkIslandsRegion(params));
    }

    @Override
    public IPRNumberRequest<IslandsRegion> getIsLandsRegion(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getIsLandsRegion(params));
    }

    @Override
    public IPRNumberRequest<CategoryFirst> getCategoryFirstList(){

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCategoryFirstList());
    }

    @Override
    public IPRNumberRequest<BusinessLicense> getBusinessLicense(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getBusinessLicense(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<GoodsPrice>> getGoodsPriceListShipTypeByPageSeqNoOnlyNormal(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGoodsPriceListShipTypeByPageSeqNoOnlyNormal(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceShipType(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceShipType(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceListShipTypeByPageSeqNoOnlyNormal(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceListShipTypeByPageSeqNoOnlyNormal(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceListStoreTypeByPageSeqNoOnlyNormal(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceListStoreTypeByPageSeqNoOnlyNormal(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceListStoreTypeByPageSeqNoAndDiscountOnlyNormal(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceListStoreTypeByPageSeqNoAndDiscountOnlyNormal(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceListByIsSubscriptionAndIsPrepaymentOnlyNormal(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceListByIsSubscriptionAndIsPrepaymentOnlyNormal(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceListShipTypeByManageSeqNoOnlyNormal(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceListShipTypeByManageSeqNoOnlyNormal(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceListTicketTypeByManageSeqNoOnlyNormal(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceListTicketTypeByManageSeqNoOnlyNormal(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceListShipTypeByPageAndDiscount(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceListShipTypeByPageAndDiscount(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceListStoreTypeByPageAndDiscountDistanceDesc(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceListStoreTypeByPageAndDiscountDistanceDesc(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getPlusSubscriptionTypeOnlyNormalOrderByDistance(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPlusSubscriptionTypeOnlyNormalOrderByDistance(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getSubscriptionTypeByPageSeqNoOnlyNormal(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSubscriptionTypeByPageSeqNoOnlyNormal(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<ProductPrice>> getProductPriceListMoneyTypeByPageSeqNoOnlyNormal(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPriceListMoneyTypeByPageSeqNoOnlyNormal(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<ProductPrice> getLastSubscriptionTypeByPageSeqNoOnlyNormal(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLastSubscriptionTypeByPageSeqNoOnlyNormal(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<ProductPrice> getLastMoneyTypeByPageSeqNoOnlyNormal(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLastMoneyTypeByPageSeqNoOnlyNormal(params));
    }

    @Override
    public IPRNumberRequest<ProductPrice> getProductPrice(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductPrice(params));
    }

    @Override
    public IPRNumberRequest<ProductPrice> getMainProductPrice(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMainProductPrice(params));
    }

    @Override
    public IPRNumberRequest<Integer> getCountProductReviewByProductPriceSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCountProductReviewByProductPriceSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Integer> getCountProductReviewByyMemberSeqNo() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCountProductReviewByyMemberSeqNo());
    }

    @Override
    public IPRNumberRequest<Integer> getCountProductReviewByPageSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCountProductReviewByPageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Object> getProductLikeCheck(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductLikeCheck(params));
    }

    @Override
    public IPRNumberRequest<Object> insertProductLike(ProductLike params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().insertProductLike(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteProductLike(ProductLike params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().deleteProductLike(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<ProductReview>> getProductReviewByProductPriceSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductReviewByProductPriceSeqNo(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<ProductReview>> getProductReviewByPageSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductReviewByPageSeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<ProductReview> getLastProductReviewByPageSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getLastProductReviewByPageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<ProductReview>> getProductReviewByMemberSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductReviewByMemberSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Object> insertProductReview(ProductReview params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().insertProductReview(params));
    }

    @Override
    public IPRNumberRequest<Object> updateProductReview(ProductReview params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateProductReview(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteProductReview(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().deleteProductReview(params));
    }

    @Override
    public IPRNumberRequest<ProductOptionTotal> getProductOption(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductOption(params));
    }

    @Override
    public IPRNumberRequest<Integer> getCountProductLike() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCountProductLike());
    }

    @Override
    public IPRNumberRequest<SubResultResponse<ProductLike>> getProductLikeShippingList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductLikeShippingList(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<PurchaseProduct>> getPurchaseProductListByMemberSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPurchaseProductListByMemberSeqNo(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<PurchaseProduct>> getPurchaseProductListTicketTypeByMemberSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPurchaseProductListTicketTypeByMemberSeqNo(params));
    }

    @Override
    public IPRNumberRequest<Integer> getCountPurchaseProductByMemberSeqNo() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCountPurchaseProductByMemberSeqNo());
    }

    @Override
    public IPRNumberRequest<PurchaseProduct> getPurchaseProduct(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPurchaseProduct(params));
    }

    @Override
    public IPRNumberRequest<Object> updatePurchaseProductComplete(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updatePurchaseProductComplete(params));
    }

    @Override
    public IPRNumberRequest<Object> cancelPurchase(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().cancelPurchase(params));
    }

    @Override
    public IPRNumberRequest<ReviewCountEval> getProductReviewCountGroupByEval(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductReviewCountGroupByEval(params));
    }

    @Override
    public IPRNumberRequest<ReviewCountEval> getProductReviewCountGroupByEvalByPageSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductReviewCountGroupByEvalByPageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<ProductInfo> getProductInfoByProductSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductInfoByProductSeqNo(params));
    }

    @Override
    public IPRNumberRequest<ProductAuth> getProductAuthByProductSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductAuthByProductSeqNo(params));
    }

    @Override
    public IPRNumberRequest<ProductNotice> getProductNoticeListByProductSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getProductNoticeListByProductSeqNo(params));
    }

    @Override
    public IPRNumberRequest<String> postOrderId() {

        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postOrderId());
    }

    @Override
    public IPRNumberRequest<Purchase> postPurchaseShip(Purchase params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postPurchaseShip(params));
    }

    @Override
    public IPRNumberRequest<Purchase> postPurchaseTicket(Purchase params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postPurchaseTicket(params));
    }

    @Override
    public IPRNumberRequest<FTLink> postPurchaseFTLinkPay(FTLink params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postPurchaseFTLinkPay(params));
    }

    @Override
    public IPRNumberRequest<Object> postPurchaseBootPayVerify(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postPurchaseBootPayVerify(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Giftishow>> getGiftishowList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGiftishowList(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<Giftishow>> getGiftishowListByBrand(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGiftishowListByBrand(params));
    }

    @Override
    public IPRNumberRequest<Object> postGiftishowBuy(GiftishowBuy params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postGiftishowBuy(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<GiftishowBuy>> getGiftishowBuyList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGiftishowBuyList(params));
    }

    @Override
    public IPRNumberRequest<Integer> getGiftishowBuyCount() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getGiftishowBuyCount());
    }

    @Override
    public IPRNumberRequest<String> checkGiftishowStatus(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().checkGiftishowStatus(params));
    }

    @Override
    public IPRNumberRequest<Object> resendGiftishowStatus(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().resendGiftishowStatus(params));
    }

    @Override
    public IPRNumberRequest<MobileCategory> getMobileCategoryList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMobileCategoryList());
    }

    @Override
    public IPRNumberRequest<MobileBrand> getMobileBrandList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMobileBrandList(params));
    }

    @Override
    public IPRNumberRequest<Object> insertChat(ChatData params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().insertChat(params));
    }

    @Override
    public IPRNumberRequest<ThemeCategory> getThemeCategoryList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getThemeCategoryList());
    }

    @Override
    public IPRNumberRequest<MemberAttendance> attendance() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().attendance());
    }

    @Override
    public IPRNumberRequest<Integer> getCountByPageSeqNoOnlyNormal(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCountByPageSeqNoOnlyNormal(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<News>> getNewsListByPageSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNewsListByPageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<News>> getPlusNewsList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPlusNewsList(params));
    }

    @Override
    public IPRNumberRequest<News> getNews(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNews(params));
    }

    @Override
    public IPRNumberRequest<SubResultResponse<NewsReview>> getNewsReviewList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNewsReviewList(params));
    }

    @Override
    public IPRNumberRequest<Object> insertNewsReview(NewsReview params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().insertNewsReview(params));
    }

    @Override
    public IPRNumberRequest<Object> updateNewsReview(NewsReview params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateNewsReview(params));
    }

    @Override
    public IPRNumberRequest<Object> deleteNewsReview(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().deleteNewsReview(params));
    }

    @Override
    public IPRNumberRequest<Integer> getNewsCountByPageSeqNo(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNewsCountByPageSeqNo(params));
    }

    @Override
    public IPRNumberRequest<MemberAddress> saveMemberAddress(MemberAddress params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().saveMemberAddress(params));
    }

    @Override
    public IPRNumberRequest<MemberAddress> getMemberAddress() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMemberAddress());
    }

    @Override
    public IPRNumberRequest<EventPolicy> getEventPolicyList(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventPolicyList(params));
    }

    @Override
    public IPRNumberRequest<VirtualNumberManage> getVirtualNumberManage(Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getVirtualNumberManage(params));
    }

    @Override
    public IPRNumberRequest<VirtualNumberManage> getNbookVirtualNumberManageList() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNbookVirtualNumberManageList());
    }

    @NotNull
    @Override
    public IPRNumberRequest<EventDetail> getEventDetailList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventDetailList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<EventDetailImage> getEventDetailImageList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventDetailImageList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<EventDetailItem> getEventDetailItemList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getEventDetailItemList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getPageListWithProductPrice(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListWithProductPrice(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getPageListWithSubscription(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListWithSubscription(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getOrderPageList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getOrderPageList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<String> makeQrCode(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().makeQrCode(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> postVisitorGivePoint(@NotNull VisitorPointGiveHistory params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postVisitorGivePoint(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> postVisitorGivePointWithStamp(@NotNull VisitorPointGiveHistory params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().postVisitorGivePointWithStamp(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<VisitorPointGiveHistory> getFirstBenefit(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getFirstBenefit(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Integer> getSubscriptionDownloadCountByMemberSeqNoAndStatus() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSubscriptionDownloadCountByMemberSeqNoAndStatus());
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubscriptionDownload> subscriptionDownload(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().subscriptionDownload(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubscriptionDownload> subscriptionDownloadWithStamp(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().subscriptionDownloadWithStamp(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<SubscriptionDownload>> getSubscriptionDownloadListByMemberSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSubscriptionDownloadListByMemberSeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubscriptionDownload> getSubscriptionDownloadBySeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSubscriptionDownloadBySeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubscriptionLog> getSubscriptionLogListBySubscriptionDownloadSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getSubscriptionLogListBySubscriptionDownloadSeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> subscriptionUse(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().subscriptionUse(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> subscriptionUseWithStamp(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().subscriptionUseWithStamp(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<PageAttendance> pageAttendanceSaveAndGet(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().pageAttendanceSaveAndGet(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<PageAttendance> pageAttendanceWithStamp(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().pageAttendanceWithStamp(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<PageAttendanceLog> getPageAttendanceLogList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageAttendanceLogList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getDeliveryPageList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getDeliveryPageList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getDeliveryPageListByKeyword(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getDeliveryPageListByKeyword(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getVisitPageList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getVisitPageList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getVisitPageListByKeyword(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getVisitPageListByKeyword(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getVisitPageListByArea(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getVisitPageListByArea(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getServicePageList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getServicePageList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<OrderMenuGroup> getOrderMenuGroupWithMenuList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getOrderMenuGroupWithMenuList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<OrderMenu> getDelegateOrderMenuList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getDelegateOrderMenuList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<OrderMenu> getMenu(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getMenu(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Integer> getCartCount(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCartCount(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Cart> getCartList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getCartList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Boolean> checkCartPage(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().checkCartPage(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> saveCart(@NotNull Cart params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().saveCart(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> updateAmount(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateAmount(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> deleteCart(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().deleteCart(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<OrderPurchase> orderPurchase(@NotNull OrderPurchase params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().orderPurchase(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<FTLink> orderPurchaseFTLinkPay(@NotNull FTLink params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().orderPurchaseFTLinkPay(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> orderPurchaseVerifyBootPay(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().orderPurchaseVerifyBootPay(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<OrderPurchase>> getOrderPurchaseListByMemberSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getOrderPurchaseListByMemberSeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<OrderPurchase>> getTicketPurchaseListByMemberSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getTicketPurchaseListByMemberSeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<OrderPurchase> getOrderPurchase(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getOrderPurchase(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> insertReview(@NotNull OrderMenuReview params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().insertReview(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> updateReview(@NotNull OrderMenuReview params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().updateReview(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> deleteReview(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().deleteReview(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<OrderMenuReview>> getReviewByMemberSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getReviewByMemberSeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<OrderMenuReview>> getReviewByPageSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getReviewByPageSeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<ReviewCountEval> getReviewCountGroupByEvalByPageSeqNo(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getReviewCountGroupByEvalByPageSeqNo(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> cancelOrderPurchaseUser(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().cancelOrderPurchaseUser(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<NotificationBox>> getNotificationBoxList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getNotificationBoxList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> notificationBoxDelete(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().notificationBoxDelete(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> locationServiceLogSave(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().locationServiceLogSave(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getPageListWithPrepayment(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListWithPrepayment(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getPageListWithPrepaymentExistVisitLog(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListWithPrepaymentExistVisitLog(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Integer> getPrepaymentRetentionCount() {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPrepaymentRetentionCount());
    }

    @NotNull
    @Override
    public IPRNumberRequest<Prepayment> getPrepayment(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPrepayment(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> prepaymentPublish(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().prepaymentPublish(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> prepaymentUse(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().prepaymentUse(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<SubResultResponse<Page2>> getPageListWithPageWithPrepaymentPublish(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPageListWithPageWithPrepaymentPublish(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<PrepaymentPublish> getPrepaymentPublish(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPrepaymentPublish(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<PrepaymentLog> getPrepaymentLogList(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getPrepaymentLogList(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<VisitLog> getVisitLog(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().getVisitLog(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> visitLogReceive(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().visitLogReceive(params));
    }

    @NotNull
    @Override
    public IPRNumberRequest<Object> useTicket(@NotNull Map<String, String> params) {
        return prnumberApi().setRequestCall(ApiController.getPRNumberApi().useTicket(params));
    }
}

