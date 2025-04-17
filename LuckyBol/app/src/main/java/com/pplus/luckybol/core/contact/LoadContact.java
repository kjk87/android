package com.pplus.luckybol.core.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.pplus.luckybol.Const;
import com.pplus.luckybol.core.database.entity.ContactDao;
import com.pplus.utils.part.format.FormatUtil;
import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.utils.StringUtils;
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager;
import com.pplus.luckybol.core.database.entity.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class LoadContact{

    public boolean getPhoneNumbers(Context context, ContactDao contactDao){

        List<HashMap<String, String>> mContactList = new ArrayList<>();
        String strProjection[] = new String[]{Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE};

        final int SUMMARY_NAME_COLUMN_INDEX = 1;
        final int SUMMARY_NUMBER_COLUMN_INDEX = 2;
        final int SUMMARY_TYPE_COLUMN_INDEX = 3;

        String strSelect = Phone.DISPLAY_NAME + " NOTNULL AND " + Phone.DISPLAY_NAME + " != ? AND " + Phone.NUMBER + " NOTNULL AND " + Phone.NUMBER + " != ?";

        if(context == null){
            return false;
        }

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Phone.CONTENT_URI, strProjection, strSelect, new String[]{"", ""}, Phone.NUMBER + " ASC");

        Locale locale = context.getResources().getConfiguration().locale;
        String strCountry = locale.getCountry();

        String strPhoneTemp = "";
        if(cursor != null && cursor.moveToFirst()) {
            do {
                String strName = cursor.getString(SUMMARY_NAME_COLUMN_INDEX);
                String strPhoneNumber = cursor.getString(SUMMARY_NUMBER_COLUMN_INDEX).replaceAll("-", "").replaceAll(" ", "").replaceAll(";", "").replaceAll(",", "");

                if(strPhoneNumber.contains("+82")){
                    strPhoneNumber = strPhoneNumber.replace("+82", "0");
                }

                if(strPhoneNumber.contains("#")) continue;

                if(strPhoneNumber.contains("*")) continue;

                if(strPhoneNumber.equals(strPhoneTemp)) {
                    continue;
                } else {
                    strPhoneTemp = strPhoneNumber;
                }

                if(LoginInfoManager.getInstance().isMember() && StringUtils.isNotEmpty(LoginInfoManager.getInstance().getUser().getMobile()) && strPhoneNumber.equals(LoginInfoManager.getInstance().getUser().getMobile().replace(Const.APP_TYPE+"##", ""))) {
                    continue;
                }

                if(FormatUtil.isCellPhoneNumber(strPhoneNumber)) {
                    HashMap<String, String> item = new HashMap<>();
                    item.put("name", strName);
                    item.put("phone", strPhoneNumber);
                    mContactList.add(item);
                }

            } while (cursor.moveToNext());
        }

        if(cursor != null)
            cursor.close();

        if(mContactList.size() > 0) {
            List<Contact> contactList = contactDao.loadAll();
            if(contactList.size() == 0) {
                for(int i = 0; i < mContactList.size(); i++) {
                    HashMap<String, String> hmData = mContactList.get(i);
                    String strUserName = hmData.get("name");
                    String strUserPhone = hmData.get("phone");

                    Contact contact = new Contact();
                    contact.setMobileNumber(strUserPhone);
                    contact.setMemberName(strUserName);
                    contact.setDelete(false);
                    contact.setUpdate(true);
                    contactDao.insertOrReplace(contact);

                }
                return true;
            } else {

                for(Contact c : contactList) {
                    c.setDelete(true);
                    contactDao.update(c);
                }

                for(int i = 0; i < mContactList.size(); i++) {
                    HashMap<String, String> hmData = mContactList.get(i);
                    String strUserName = hmData.get("name");
                    String strUserPhone = hmData.get("phone");

                    Contact contact = new Contact();
                    contact.setMobileNumber(strUserPhone);
                    contact.setMemberName(strUserName);
                    contact.setDelete(false);
                    String key = contactDao.getKey(contact);
                    //LogUtil.e("loadContact", "{}, {}", key, strUserName);
                    if(contactDao.load(key) == null) {
                        contact.setUpdate(true);
                        LogUtil.e("CONTACT NEW", "{} : {}", strUserName, strUserPhone);
                        contactDao.insert(contact);
                    } else {
                        contact = contactDao.load(key);
                        contact.setMemberName(strUserName);
                        contact.setDelete(false);
                        contact.setUpdate(false);
                        contactDao.update(contact);
                        //LogUtil.e("loadContact", "update");
                    }
                }
                return false;
            }

        } else {
            if(contactDao.count() > 0) {
                List<Contact> contactList = contactDao.queryBuilder().list();
                if(contactList != null) {
                    for(Contact c : contactList) {
                        c.setDelete(true);
                        contactDao.update(c);
                    }
                }
            }
            return false;
        }

    }

}
