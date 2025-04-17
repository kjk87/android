package network.common;

/**
 * Created by j2n on 2016. 9. 7..
 */
public interface NetworkInfo{

    NetworkConfig.SWITCH isDebug();

    NetworkConfig.DEVELOP_MODE getServiceMode();

    String BaseRealServerUrl();

    String BaseDevServerUrl();

}
