//package com.pplus.luckybol.core.contact;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import com.pplus.luckybol.Const;
//import com.pplus.luckybol.core.database.entity.ContactDao;
//import com.pplus.utils.part.logs.LogUtil;
//import com.pplus.luckybol.core.database.DBManager;
//import com.pplus.luckybol.core.database.entity.Contact;
//import com.pplus.luckybol.core.network.ApiBuilder;
//import com.pplus.luckybol.core.network.model.request.params.ParamsContact;
//import com.pplus.luckybol.core.network.model.response.NewResultResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class LoadContactTask extends AsyncTask<Void, Void, Boolean> {
//
//    private Context mContext;
//    private ContactDao mContactDao;
//    private OnResultListener listener;
//
//
//    public interface OnResultListener {
//
//        void onResult();
//    }
//
//    public LoadContactTask(Context context, OnResultListener listener) {
//
//        super();
//        mContext = context;
//        this.listener = listener;
//        mContactDao = DBManager.getInstance(context).getSession().getContactDao();
//    }
//
//    @Override
//    protected void onPreExecute() {
//
//        super.onPreExecute();
//        LogUtil.e("contact", "onPreExecute");
//
//    }
//
//    @Override
//    protected Boolean doInBackground(Void... params) {
//
//        LogUtil.e("contact", "doInBackground");
//        //주소록에 있는 전화번호 리스트 저장
//        LoadContact loadContact = new LoadContact();
//        return loadContact.getPhoneNumbers(mContext, mContactDao);
//    }
//
//    @Override
//    protected void onPostExecute(Boolean result) {
//
//        LogUtil.e("contact", "onPostExecute");
////        if(!LoginInfoManager.getInstance().getUser().getLoginId().equals("bayaba1977")){
////            updateFriend(result);
////        }else {
////            if (listener != null) {
////                listener.onResult();
////            }
////        }
//        updateFriend(result);
//    }
//
//    private void updateFriend(Boolean result) {
//        List<Contact> contactList = null;
//        if (result) {
//            contactList = mContactDao.loadAll();
//            if (contactList.size() > 0) {
//                ParamsContact params = new ParamsContact();
//                List<ParamsContact.Contact> numberList = new ArrayList<>();
//                for (int i = 0; i < contactList.size(); i++) {
//                    ParamsContact.Contact contact = new ParamsContact.Contact();
//                    contact.setMobile(Const.APP_TYPE + "##"+contactList.get(i).getMobileNumber());
//                    numberList.add(contact);
//                }
//                LogUtil.e("INSERT CONTACT", numberList.toString());
//                params.setContactList(numberList);
//
//                ApiBuilder.create().updateContactList(params).setCallback(new PplusCallback<NewResultResponse<Object>>() {
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response) {
//                        if (listener != null) {
//                            listener.onResult();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response) {
//
//                    }
//                }).build().call();
//            }
//        } else {
//            contactList = mContactDao.queryBuilder().whereOr(ContactDao.Properties.Update.eq(true), ContactDao.Properties.Delete.eq(true)).list();
//            if (contactList.size() > 0) {
//                List<ParamsContact.Contact> updateList = new ArrayList<>();
//                List<ParamsContact.Contact> deleteList = new ArrayList<>();
//
//                for (int i = 0; i < contactList.size(); i++) {
//
//                    if (contactList.get(i).getUpdate()) {
//                        ParamsContact.Contact contact = new ParamsContact.Contact();
//                        contact.setMobile(Const.APP_TYPE+"##"+contactList.get(i).getMobileNumber());
//                        updateList.add(contact);
//                    }
//
//                    if (contactList.get(i).getDelete()) {
//                        ParamsContact.Contact contact = new ParamsContact.Contact();
//                        contact.setMobile(Const.APP_TYPE+"##"+contactList.get(i).getMobileNumber());
//                        deleteList.add(contact);
//                        mContactDao.delete(new Contact(contactList.get(i).getMobileNumber(), null, null, null));
//                    }
//                }
//
//                if (deleteList.size() > 0) {
//                    ParamsContact deleteParams = new ParamsContact();
//                    deleteParams.setContactList(deleteList);
//                    deleteParams.setAppType(Const.APP_TYPE);
//                    ApiBuilder.create().deleteContactList(deleteParams).setCallback(new PplusCallback<NewResultResponse<Object>>() {
//                        @Override
//                        public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response) {
//
//                        }
//                    }).build().call();
//                }
//
//
//                if (updateList.size() > 0) {
//                    LogUtil.e("CONTACT NEW", "new update : {}", updateList.get(0).getMobile());
//                    ParamsContact updateParams = new ParamsContact();
//                    updateParams.setContactList(updateList);
//                    updateParams.setAppType(Const.APP_TYPE);
//                    ApiBuilder.create().updateContactList(updateParams).setCallback(new PplusCallback<NewResultResponse<Object>>() {
//                        @Override
//                        public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response) {
//
//                        }
//                    }).build().call();
//                }
//            }
//            if (listener != null) {
//                listener.onResult();
//            }
//        }
//    }
//}
