package com.pplus.prnumberbiz.apps.common.mgmt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pple.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.prnumberbiz.PRNumberBizApplication;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.network.model.dto.Category;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * Created by 김종경 on 2016-10-06.
 */

public class CategoryInfoManager{

    private List<Category> categoryListStore;
    private List<Category> categoryListPerson;
    private List<Category> categoryListShop;

    public List<Category> getCategoryListStore(){

        return categoryListStore;
    }

    public void setCategoryListStore(List<Category> categoryListStore){

        this.categoryListStore = categoryListStore;
    }

    public List<Category> getCategoryListPerson(){

        return categoryListPerson;
    }

    public void setCategoryListPerson(List<Category> categoryListPerson){

        this.categoryListPerson = categoryListPerson;
    }

    public List<Category> getCategoryListShop() {
        return categoryListShop;
    }

    public void setCategoryListShop(List<Category> categoryListShop) {
        this.categoryListShop = categoryListShop;
    }

    // Preference
    public static final String SHARED_CATEGORY_INFO = "category_info";

    private static CategoryInfoManager mCategoryInfoManager = null;

    private CategoryCache categoryCache;

    public enum CATEGORY_TYPE{
        LEVEL1(1), LEVEL2(2);

        int value;

        CATEGORY_TYPE(int value){

            this.value = value;
        }
    }

    private CategoryInfoManager(){

    }

    public static CategoryInfoManager getInstance(){

        if(mCategoryInfoManager == null) {
            mCategoryInfoManager = new CategoryInfoManager();
            mCategoryInfoManager.load();
        }
        return mCategoryInfoManager;
    }

    public void load(){

        String data = PreferenceUtil.getDefaultPreference(PRNumberBizApplication.getContext()).getString(SHARED_CATEGORY_INFO);
        Type typeOfSrc = new TypeToken<CategoryInfoManager>(){

        }.getType();

        CategoryInfoManager categoryInfoManager = new Gson().fromJson(data, typeOfSrc);

        if(categoryInfoManager != null) {
            mCategoryInfoManager = new Gson().fromJson(data, typeOfSrc);
        }
    }

    public void save(){

        Type typeOfSrc = new TypeToken<CategoryInfoManager>(){

        }.getType();
        String data = new Gson().toJson(mCategoryInfoManager, typeOfSrc);
        PreferenceUtil.getDefaultPreference(PRNumberBizApplication.getContext()).put(SHARED_CATEGORY_INFO, data);
    }

    public void clear(){

        //PreferenceUtil.getPreference(mContext, SHARED_CATEGORY_INFO).clear();
        mCategoryInfoManager = new CategoryInfoManager();
        mCategoryInfoManager.setCategoryListStore(null);
        save();
    }

    public boolean isCalled(){

        if(mCategoryInfoManager.getCategoryListStore() != null) {
            return true;
        }

        return false;
    }

    public void categoryListCall(final CATEGORY_TYPE level, String type, final Long parentNo, final OnCategoryResultListener onCategoryResultListener){

        if(categoryCache != null && categoryCache.getCategoryType() == level && categoryCache.getNo() != null && categoryCache.getNo().equals(parentNo)) {
            onCategoryResultListener.onResult(level, categoryCache.getCategoryList());
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("depth", "" + level.value);
        if(parentNo != null && !level.equals(CATEGORY_TYPE.LEVEL1)) {
            params.put("parent.no", String.valueOf(parentNo));
        }
        ApiBuilder.create().getCategoryAll(params).setCallback(new PplusCallback<NewResultResponse<Category>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Category>> call, NewResultResponse<Category> response){

                List<Category> categoryList = response.getDatas();
                categoryCache = new CategoryCache(level, categoryList, parentNo);

                onCategoryResultListener.onResult(level, categoryCache.getCategoryList());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Category>> call, Throwable t, NewResultResponse<Category> response){

            }
        }).build().call();
    }

    public interface OnCategoryResultListener{

        void onResult(CATEGORY_TYPE Level, List<Category> categories);

        void onFailed(String message);
    }

    private class CategoryCache{

        private CATEGORY_TYPE categoryType;
        private List<Category> categoryList;
        private Long no;

        public CategoryCache(CATEGORY_TYPE categoryType, List<Category> categoryList, Long no){

            this.categoryType = categoryType;
            this.categoryList = categoryList;
            this.no = no;
        }

        public CATEGORY_TYPE getCategoryType(){

            return categoryType;
        }

        public List<Category> getCategoryList(){

            return categoryList;
        }

        public Long getNo(){

            return no;
        }
    }
}
