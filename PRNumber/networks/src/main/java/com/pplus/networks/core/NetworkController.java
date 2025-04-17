package com.pplus.networks.core;

import com.pplus.networks.service.AbstractService;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by J2N on 16. 6. 21..
 */
public class NetworkController {

    private Map<String , AbstractService> abstractServiceMap;

    public NetworkController() {
        this.abstractServiceMap = new HashMap<>();
    }

    public <T extends AbstractService> T getService(Class<T> service){

        try {
            if(!this.abstractServiceMap.containsKey(service.getSimpleName())){
                this.abstractServiceMap.put(service.getSimpleName() , service.newInstance());
            }
            return (T) this.abstractServiceMap.get(service.getSimpleName());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
