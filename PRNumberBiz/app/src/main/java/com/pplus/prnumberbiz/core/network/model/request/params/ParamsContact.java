package com.pplus.prnumberbiz.core.network.model.request.params;

import java.util.List;

/**
 * Created by imac on 2017. 8. 31..
 */

public class ParamsContact{
    private boolean deleteAll;
    private List<Contact> contactList;

    public boolean isDeleteAll(){

        return deleteAll;
    }

    public void setDeleteAll(boolean deleteAll){

        this.deleteAll = deleteAll;
    }

    public List<Contact> getContactList(){

        return contactList;
    }

    public void setContactList(List<Contact> contactList){

        this.contactList = contactList;
    }

    @Override
    public String toString(){

        return "ParamsContact{" + "deleteAll=" + deleteAll + ", contactList=" + contactList + '}';
    }

    public static class Contact{
        private String mobile;
        private String regDate;

        public String getMobile(){

            return mobile;
        }

        public void setMobile(String mobile){

            this.mobile = mobile;
        }

        public String getRegDate(){

            return regDate;
        }

        public void setRegDate(String regDate){

            this.regDate = regDate;
        }

        @Override
        public String toString(){

            return "Contact{" + "mobile='" + mobile + '\'' + ", regDate='" + regDate + '\'' + '}';
        }
    }
}
