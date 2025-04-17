package com.pplus.prnumberbiz.core.contact;

import android.content.Context;
import android.os.AsyncTask;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.core.database.DBManager;
import com.pplus.prnumberbiz.core.database.dao.ContactDao;
import com.pplus.prnumberbiz.core.database.entity.Contact;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Customer;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsContact;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsCustomerList;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.ArrayList;
import java.util.List;

import network.common.PplusCallback;
import retrofit2.Call;

public class LoadContactTask extends AsyncTask<Void, Void, Boolean>{

    private Context mContext;
    private ContactDao mContactDao;
    private OnResultListener listener;


    public interface OnResultListener{

        void onResult();
    }

    public LoadContactTask(Context context, OnResultListener listener){

        super();
        mContext = context;
        this.listener = listener;
        mContactDao = DBManager.getInstance(context).getSession().getContactDao();
    }

    @Override
    protected void onPreExecute(){

        super.onPreExecute();
        LogUtil.e("contact", "onPreExecute");

    }

    @Override
    protected Boolean doInBackground(Void... params){

        LogUtil.e("contact", "doInBackground");
        //주소록에 있는 전화번호 리스트 저장
        LoadContact loadContact = new LoadContact();
        return loadContact.getPhoneNumbers(mContext, mContactDao);
    }

    @Override
    protected void onPostExecute(Boolean result){

        super.onPostExecute(result);
        updateFriend(result);
        if(listener != null) {
            listener.onResult();
        }
//        if(LoginInfoManager.getInstance().getUser().getPage() != null && LoginInfoManager.getInstance().getUser().getPage().getCustomerCount() == 0) {
//            List<Contact> contactList = mContactDao.loadAll();
//
//            if(contactList.size() > 0) {
//
//                List<Customer> customerList = new ArrayList<>();
//                for(int i = 0; i < contactList.size(); i++) {
//                    Customer customer = new Customer();
//                    customer.setMobile(contactList.get(i).getMobileNumber());
//                    customer.setName(contactList.get(i).getMemberName());
//                    customer.setInputType("contact");
//                    customerList.add(customer);
//                }
//
//                ParamsCustomerList params = new ParamsCustomerList();
//                params.setPage(new No(LoginInfoManager.getInstance().getUser().getPage().getNo()));
//                params.setCustomerList(customerList);
//                ApiBuilder.create().insertCustomerList(params).setCallback(new PplusCallback<NewResultResponse<Customer>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Customer>> call, NewResultResponse<Customer> response){
//
//                        if(listener != null) {
//                            listener.onResult();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<Customer>> call, Throwable t, NewResultResponse<Customer> response){
//
//                        if(listener != null) {
//                            listener.onResult();
//                        }
//                    }
//                }).build().call();
//
//            } else {
//                if(listener != null) {
//                    listener.onResult();
//                }
//            }
//        }else{
//            if(listener != null) {
//                listener.onResult();
//            }
//        }

    }

    private void updateFriend(boolean result){
        List<Contact> contactList = null;
        if(result) {
            contactList = mContactDao.loadAll();
            if(contactList.size() > 0) {
                ParamsContact params = new ParamsContact();
                List<ParamsContact.Contact> numberList = new ArrayList<>();
                for(int i = 0; i < contactList.size(); i++) {
                    ParamsContact.Contact contact = new ParamsContact.Contact();
                    contact.setMobile(contactList.get(i).getMobileNumber());
                    numberList.add(contact);
                }
                params.setContactList(numberList);

                ApiBuilder.create().updateContactList(params).setCallback(null).build().call();
            }
        } else {
            contactList = mContactDao.queryBuilder().whereOr(ContactDao.Properties.Update.eq(true), ContactDao.Properties.Delete.eq(true)).list();
            if(contactList.size() > 0) {
                List<ParamsContact.Contact> updateList = new ArrayList<>();
                List<ParamsContact.Contact> deleteList = new ArrayList<>();
                ParamsContact updateParams = new ParamsContact();
                ParamsContact deleteParams = new ParamsContact();
                for(int i = 0; i < contactList.size(); i++) {

                    if(contactList.get(i).getUpdate()) {
                        ParamsContact.Contact contact = new ParamsContact.Contact();
                        contact.setMobile(contactList.get(i).getMobileNumber());
                        updateList.add(contact);
                    }

                    if(contactList.get(i).getDelete()) {
                        ParamsContact.Contact contact = new ParamsContact.Contact();
                        contact.setMobile(contactList.get(i).getMobileNumber());
                        deleteList.add(contact);
                        mContactDao.delete(new Contact(contactList.get(i).getMobileNumber()));
                    }
                }


                if(deleteList.size() > 0){
                    deleteParams.setContactList(deleteList);
                    ApiBuilder.create().deleteContactList(deleteParams).setCallback(null).build().call();
                }
                if(updateList.size() > 0){
                    updateParams.setContactList(updateList);
                    ApiBuilder.create().updateContactList(updateParams).setCallback(null).build().call();
                }
            }
        }
    }
}
