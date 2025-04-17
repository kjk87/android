package com.pplus.utils;

import com.pplus.utils.part.logs.LogUtil;
import com.squareup.otto.Bus;

/**
 * Created by 안명훈 on 16. 7. 5..
 */
public final class BusProvider{

    private static Bus Instance;

    public synchronized static Bus getInstance(){

        if(Instance == null) {
            Instance = new Bus(){

                @Override
                public void unregister(Object object){

                    try {
                        super.unregister(object);
                    } catch (NullPointerException e) {
                        LogUtil.e("BUS", "Object to unregister must not be null.");
                    }

                }
            };
        }
        return Instance;
    }

    private BusProvider(){
        // not instances.
    }

}
