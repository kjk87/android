package com.pplus.prnumberuser.apps.common.mgmt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.prnumberuser.PRNumberApplication;
import com.pplus.prnumberuser.core.network.model.dto.CategoryMajor;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 김종경 on 2016-10-06.
 */

public class CategoryInfoManager{

    private List<CategoryMajor> categoryList;

    public List<CategoryMajor> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryMajor> categoryList) {
        this.categoryList = categoryList;
    }

    // Preference
    public static final String SHARED_CATEGORY_INFO = "category_info";

    private static CategoryInfoManager mCategoryInfoManager = null;

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

        String data = PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).getString(SHARED_CATEGORY_INFO);
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
        PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).put(SHARED_CATEGORY_INFO, data);
    }

    public void clear(){

        //PreferenceUtil.getPreference(mContext, SHARED_CATEGORY_INFO).clear();
        mCategoryInfoManager = new CategoryInfoManager();
        mCategoryInfoManager.setCategoryList(null);
        save();
    }

    public boolean isCalled(){

        if(mCategoryInfoManager.getCategoryList() != null) {
            return true;
        }

        return false;
    }
}
