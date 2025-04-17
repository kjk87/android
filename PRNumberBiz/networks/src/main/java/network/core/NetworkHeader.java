package network.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by J2N on 16. 6. 21..
 */
public class NetworkHeader {

    /**
     * 기본 사용으로 처리함
     */
    public boolean headerEnable;

    /**
     * 헤더 Map
     */
    private Map<String, String> headers = null;

    public NetworkHeader() {
        headers = new HashMap<>();
        headerEnable = true;
    }

    public Map<String, String> getHeaderMap() {

        return headers;
    }

    /**
     * 헤더를 모두 삭제합니다.
     */
    public void cleanHeaders() {
        headers.clear();
    }

    /**
     * 헤더를 추가합니다.
     *
     * @param key   String header key
     * @param value String header value
     */
    public void addHeader(String key, String value) {

        if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
            headers.put(key, value);
        }
    }

    public void setHeaders(Map<String, String> headers){
        this.headers = headers;
    }

    /**
     * 헤더를 삭제합니다.
     *
     * @param key
     */
    public void removeHeaderforKey(String key) {
        if (headers.containsKey(key)) {
            headers.remove(key);
        }
    }

    /**
     * 헤더 사용 여부
     */
    public boolean isHeader() {
        if (!headerEnable || headers.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 헤더 사용 여부 기본은 사용하도록 됨
     * */
    public void setHeaderEnable(boolean enable) {
        headerEnable = enable;
    }

}
