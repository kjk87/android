package com.pplus.prnumberbiz.apps.signin.data;

/**
 * Created by Windows7-00 on 2016-10-23.
 */

public class SingInOutData {

    private boolean isLogin;

    public SingInOutData(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isLogin() {

        return isLogin;
    }

    @Override
    public String toString() {
        return "SingInOutData{" +
                "isLogin=" + isLogin +
                '}';
    }

}
