package com.pplus.prnumberuser.core.network.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by J2N on 16. 6. 21..<br> <h><t>제일 기본이되는 res 객체</h></t> <br> 'T' 부분을 정의하지않으면 Map<String ,
 * Object> 형태로 반환한다. <br>
 */
public class NewResultResponse<T extends Object>{

    /**
     * 상태 코드
     */
    private int resultCode;

    /**
     * 서버로 부터 받은 결과물 Object
     */
    @SerializedName("row")
    private T data;

    /**
     * 서버로 부터 받은 결과물 Object List
     */
    @SerializedName("rows")
    private List<T> datas;

    /**
     * 서버로 부터 받은 결과물 error값
     */
    @SerializedName("errorExtra")
    private Map<String, String> errorExtra;

    public int getResultCode(){

        return resultCode;
    }

    public void setResultCode(int resultCode){

        this.resultCode = resultCode;
    }

    public T getData(){

        return data;
    }

    public void setData(T data){

        this.data = data;
    }

    public List<T> getDatas(){

        return datas;
    }

    public void setDatas(List<T> datas){

        this.datas = datas;
    }

    public Map<String, String> getErrorExtra(){

        return errorExtra;
    }

    public void setErrorExtra(Map<String, String> errorExtra){

        this.errorExtra = errorExtra;
    }

    @Override
    public String toString(){

        return "NewResultResponse{" + "resultCode=" + resultCode + ", data=" + data + ", datas=" + datas + ", errorExtra=" + errorExtra + '}';
    }
}
