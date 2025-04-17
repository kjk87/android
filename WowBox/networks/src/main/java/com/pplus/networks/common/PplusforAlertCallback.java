package com.pplus.networks.common;

/**
 * Created by j2n on 2016. 10. 25..
 */

public interface PplusforAlertCallback<T> extends PplusCallback<T>{

    void onErrorAlert(T response);

}
