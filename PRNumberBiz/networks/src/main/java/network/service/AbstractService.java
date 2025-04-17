package network.service;

import java.lang.reflect.ParameterizedType;

import network.core.NetworkCore;
import network.core.NetworkHeader;
import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Created by J2N on 16. 6. 20.. <br> T = Api interface를 정의해주셔야합니다.
 */
public abstract class AbstractService<T>{

    public String LOG_TAG = getClass().getSimpleName();
    /**
     * 서비스 코어
     */
    private NetworkCore networkCore;

    private NetworkHeader networkHeader;
    /**
     * Api 클래스 정의
     */
    private T api;
    private Class<T> apiClass;

    /**
     * Base Url..
     */
    public abstract String getEndPoint();

    /**
     * 별도로 사용할 http 클라이언트가 존재시 없으면 null
     */
    public abstract OkHttpClient getCustomHttpClient();

    /**
     * 기본적으로 json을 사용하며 별도의 convert가 필요시 반환함.
     */
    public abstract Converter.Factory getCustomConverterFactory();

    /**
     * 헤더정보 초기화
     */
    public abstract void initializeHeader(NetworkHeader header);

    /**
     * api 서비스를 반환합니다.
     */
    public T getApi(){

        return api;
    }

    public AbstractService(){

        initialize();
    }

    /**
     * api를 생성하여 초기화 합니다.
     */
    private void initialize(){

        networkCore = new NetworkCore(this);

        this.apiClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        api = networkCore.getRetrofit().create(apiClass);

        networkHeader = new NetworkHeader();
    }

    /**
     * 헤더정보를 수정할수있는 객체
     */
    public NetworkHeader getHeaders(){

        if(networkHeader == null) {
            networkHeader = new NetworkHeader();
        }
        return networkHeader;
    }

    /**
     * @return 헤더정보를 사용하는지 여부 반환
     */
    public boolean isHeader(){

        return getHeaders().isHeader();
    }

    public NetworkCore getNetworkCore(){

        return networkCore;
    }

    public void updateHeaders(){

        networkCore.updateHeaders();
    }
}
