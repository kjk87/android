package com.pplus.prnumberbiz.core.network;

import com.pplus.prnumberbiz.core.network.apis.ICdnApi;
import com.pplus.prnumberbiz.core.network.apis.IPRNumberApi;
import com.pplus.prnumberbiz.core.network.service.CdnService;
import com.pplus.prnumberbiz.core.network.service.PRNumberService;

import network.core.NetworkController;

/**
 * Created by J2N on 16. 6. 20..
 */
public class ApiController extends NetworkController{

    private static ApiController Instance;

    private ApiController(){

    }

    private synchronized static ApiController getInstance(){

        if(Instance == null) {
            Instance = new ApiController();
        }
        return Instance;
    }

    /**
     * PRNumber Service를 반환합니다.
     */
    public static PRNumberService getPRNumberService(){

        return getInstance().getService(PRNumberService.class);
    }


    public static CdnService getCdnService(){

        return getInstance().getService(CdnService.class);
    }

    /**
     * P+ Api를 반환합니다.
     */
    public static IPRNumberApi getPRNumberApi(){

        return getInstance().getPRNumberService().getApi();
    }

    public static ICdnApi getCdnApi(){

        return getInstance().getCdnService().getApi();
    }
}
