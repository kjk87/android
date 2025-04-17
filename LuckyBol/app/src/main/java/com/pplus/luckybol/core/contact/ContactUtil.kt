package com.pplus.luckybol.core.contact

import android.content.Context
import com.pplus.luckybol.Const
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.Contact
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.ApiBuilder.Companion.create
import com.pplus.luckybol.core.network.model.request.params.ParamsContact
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.logs.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call

class ContactUtil {

    companion object {
        private var mContactUtil: ContactUtil? = null
        fun getInstance(): ContactUtil {
            if (mContactUtil == null) {
                mContactUtil = ContactUtil()
            }
            return mContactUtil!!
        }
    }

    private var mContactDao:ContactDao? = null

    fun load(context:Context){
        val job = Job()
        CoroutineScope(Dispatchers.IO + job).launch {
            mContactDao = DBManager.getInstance(context).session.contactDao //주소록에 있는 전화번호 리스트 저장
            val loadContact = LoadContact()
            val result = loadContact.getPhoneNumbers(context, mContactDao)
            updateFriend(result)
        }
    }

    private fun updateFriend(result: Boolean) {
        var contactList: List<Contact>? = null
        if (result) {
            contactList = mContactDao!!.loadAll()
            if (contactList.isNotEmpty()) {
                val params = ParamsContact()
                val numberList: MutableList<ParamsContact.Contact> = ArrayList()
                for (i in contactList.indices) {
                    val contact = ParamsContact.Contact()
                    contact.mobile = Const.APP_TYPE + "##" + contactList[i].mobileNumber
                    numberList.add(contact)
                }
                LogUtil.e("INSERT CONTACT", numberList.toString())
                params.contactList = numberList
                create().updateContactList(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                            response: NewResultResponse<Any>?) {

                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                           t: Throwable?,
                                           response: NewResultResponse<Any>?) {

                    }
                }).build().call()
            }
        } else {
            contactList = mContactDao!!.queryBuilder().whereOr(ContactDao.Properties.Update.eq(true), ContactDao.Properties.Delete.eq(true)).list()
            if (contactList.isNotEmpty()) {
                val updateList: MutableList<ParamsContact.Contact> = ArrayList()
                val deleteList: MutableList<ParamsContact.Contact> = ArrayList()
                for (i in contactList.indices) {
                    if (contactList[i].update) {
                        val contact = ParamsContact.Contact()
                        contact.mobile = Const.APP_TYPE + "##" + contactList[i].mobileNumber
                        updateList.add(contact)
                    }
                    if (contactList[i].delete) {
                        val contact = ParamsContact.Contact()
                        contact.mobile = Const.APP_TYPE + "##" + contactList[i].mobileNumber
                        deleteList.add(contact)
                        mContactDao!!.delete(Contact(contactList[i].mobileNumber, null, null, null))
                    }
                }
                if (deleteList.isNotEmpty()) {
                    val deleteParams = ParamsContact()
                    deleteParams.contactList = deleteList
                    deleteParams.appType = Const.APP_TYPE
                    create().deleteContactList(deleteParams).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                        override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                                response: NewResultResponse<Any>?) {

                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                               t: Throwable?,
                                               response: NewResultResponse<Any>?) {

                        }
                    }).build().call()
                }
                if (updateList.size > 0) {
                    LogUtil.e("CONTACT NEW", "new update : {}", updateList[0].mobile)
                    val updateParams = ParamsContact()
                    updateParams.contactList = updateList
                    updateParams.appType = Const.APP_TYPE
                    create().updateContactList(updateParams).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                        override fun onResponse(call: Call<NewResultResponse<Any>>?,
                                                response: NewResultResponse<Any>?) {

                        }

                        override fun onFailure(call: Call<NewResultResponse<Any>>?,
                                               t: Throwable?,
                                               response: NewResultResponse<Any>?) {

                        }
                    }).build().call()
                }
            }
        }
    }
}