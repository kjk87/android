package com.pplus.prnumberuser.core.network;

import com.pplus.networks.core.NetworkController;
import com.pplus.prnumberuser.core.network.apis.GpaApi;
import com.pplus.prnumberuser.core.network.apis.ICSApi;
import com.pplus.prnumberuser.core.network.apis.ICdnApi;
import com.pplus.prnumberuser.core.network.apis.IMapApi;
import com.pplus.prnumberuser.core.network.apis.IPRNumberApi;
import com.pplus.prnumberuser.core.network.service.CSService;
import com.pplus.prnumberuser.core.network.service.CdnService;
import com.pplus.prnumberuser.core.network.service.GpaService;
import com.pplus.prnumberuser.core.network.service.MapService;
import com.pplus.prnumberuser.core.network.service.PRNumberService;

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

    /**
     * MapService를 반환합니다.
     */
    public static MapService getMapService(){

        return getInstance().getService(MapService.class);
    }


    /**
     * 통계용 Service를 호출
     *
     * @return
     */
    public static CdnService getCdnService(){

        return getInstance().getService(CdnService.class);
    }

    public static GpaService getGpaService(){

        return getInstance().getService(GpaService.class);
    }

    public static CSService getCSService(){

        return getInstance().getService(CSService.class);
    }

    /**
     * P+ Api를 반환합니다.
     */
    public static IPRNumberApi getPRNumberApi(){

        return getInstance().getPRNumberService().getApi();
    }

    /**
     * P+ 광고 서버 관련 API를 반환합니다.
     */
    public static IMapApi getMapApi(){

        return getInstance().getMapService().getApi();
    }

    public static ICdnApi getCdnApi(){

        return getInstance().getCdnService().getApi();
    }

    public static GpaApi getGpaApi(){

        return getInstance().getGpaService().getApi();
    }

    public static ICSApi getCSApi(){

        return getInstance().getCSService().getApi();
    }
}
