//package com.pplus.prnumberuser.apps.common.mgmt;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.pplus.utils.part.pref.PreferenceUtil;
//import com.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberuser.PRNumberApplication;
//import com.pplus.prnumberuser.core.network.ApiController;
//import com.pplus.prnumberuser.core.network.model.dto.StepAddress;
//import com.pplus.prnumberuser.core.network.model.response.result.ResultStepAddress;
//
//import java.lang.reflect.Type;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by 김종경 on 2016-10-06.
// */
//
//public class BaseAddressManager{
//
//    private List<StepAddress> addressList;
//
//    public List<StepAddress> getAddressList(){
//
//        return addressList;
//    }
//
//    public void setAddressList(List<StepAddress> addressList){
//
//        this.addressList = addressList;
//    }
//
//    // Preference
//    public static final String SHARED_BASE_ADDRESS = "base_address";
//
//    private static BaseAddressManager mBaseAddressManager = null;
//
//    private Cache cache;
//
//    public enum LEVEL_TYPE{
//        LEVEL1(1), LEVEL2(2), LEVEL3(3);
//
//        int value;
//
//        LEVEL_TYPE(int value){
//
//            this.value = value;
//        }
//    }
//
//    private BaseAddressManager(){
//
//    }
//
//    public static BaseAddressManager getInstance(){
//
//        if(mBaseAddressManager == null) {
//            mBaseAddressManager = new BaseAddressManager();
//            mBaseAddressManager.load();
//        }
//        return mBaseAddressManager;
//    }
//
//    public void load(){
//
//        String data = PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).getString(SHARED_BASE_ADDRESS);
//        Type typeOfSrc = new TypeToken<BaseAddressManager>(){
//
//        }.getType();
//
//        BaseAddressManager categoryInfoManager = new Gson().fromJson(data, typeOfSrc);
//
//        if(categoryInfoManager != null) {
//            mBaseAddressManager = new Gson().fromJson(data, typeOfSrc);
//        }
//    }
//
//    public void save(){
//
//        Type typeOfSrc = new TypeToken<BaseAddressManager>(){
//
//        }.getType();
//        String data = new Gson().toJson(mBaseAddressManager, typeOfSrc);
//        PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).put(SHARED_BASE_ADDRESS, data);
//    }
//
//    public void clear(){
//
//        //PreferenceUtil.getPreference(mContext, SHARED_CATEGORY_INFO).clear();
//        mBaseAddressManager = new BaseAddressManager();
//        mBaseAddressManager.setAddressList(null);
//        save();
//    }
//
//    public boolean isCalled(){
//
//        if(mBaseAddressManager.getAddressList() != null) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public void listCall(final LEVEL_TYPE level, final String cd, final OnCategoryResultListener onCategoryResultListener){
//
//        if(cache != null && cache.getLevel_type() == level && StringUtils.isNotEmpty(cd) && cache.getCd().equals(cd)) {
//            onCategoryResultListener.onResult(level, cache.getAddressList());
//            return;
//        }
//
//        Map<String, String> params = new HashMap<>();
//        if(StringUtils.isNotEmpty(cd)){
//            params.put("cd", cd);
//        }
//
//        ApiController.getPRNumberApi().requestSearchStepAddress(params).enqueue(new Callback<ResultStepAddress>(){
//
//            @Override
//            public void onResponse(Call<ResultStepAddress> call, Response<ResultStepAddress> response){
//                if(response.body() != null){
//                    cache = new Cache(level, response.body().getResult(), cd);
//
//                    onCategoryResultListener.onResult(level, cache.getAddressList());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResultStepAddress> call, Throwable t){
//            }
//        });
//    }
//
//    public interface OnCategoryResultListener{
//
//        void onResult(LEVEL_TYPE Level, List<StepAddress> addressList);
//
//        void onFailed(String message);
//    }
//
//    private class Cache{
//
//        private LEVEL_TYPE level_type;
//        private List<StepAddress> addressList;
//        private String cd;
//
//        public Cache(LEVEL_TYPE level_type, List<StepAddress> addressList, String cd){
//
//            this.level_type = level_type;
//            this.addressList = addressList;
//            this.cd = cd;
//        }
//
//        public LEVEL_TYPE getLevel_type(){
//
//            return level_type;
//        }
//
//        public void setLevel_type(LEVEL_TYPE level_type){
//
//            this.level_type = level_type;
//        }
//
//        public List<StepAddress> getAddressList(){
//
//            return addressList;
//        }
//
//        public void setAddressList(List<StepAddress> addressList){
//
//            this.addressList = addressList;
//        }
//
//        public String getCd(){
//
//            return cd;
//        }
//
//        public void setCd(String cd){
//
//            this.cd = cd;
//        }
//    }
//}
