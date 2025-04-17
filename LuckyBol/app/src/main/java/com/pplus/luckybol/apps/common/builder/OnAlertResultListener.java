package com.pplus.luckybol.apps.common.builder;

/**
 * Created by j2n on 2016. 8. 22..
 */
public interface OnAlertResultListener{

    void onCancel();

    void onResult(AlertBuilder.EVENT_ALERT event_alert);
}
