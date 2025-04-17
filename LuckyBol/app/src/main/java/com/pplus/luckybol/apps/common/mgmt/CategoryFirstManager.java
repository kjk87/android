package com.pplus.luckybol.apps.common.mgmt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pplus.luckybol.LuckyBolApplication;
import com.pplus.luckybol.core.network.model.dto.CategoryFirst;
import com.pplus.utils.part.pref.PreferenceUtil;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 김종경 on 2016-10-06.
 */

public class CategoryFirstManager {

    private List<CategoryFirst> categoryFirstList;

    public List<CategoryFirst> getCategoryFirstList() {
        return categoryFirstList;
    }

    public void setCategoryFirstList(List<CategoryFirst> categoryFirstList) {
        this.categoryFirstList = categoryFirstList;
    }

    // Preference
    public static final String SHARED_CATEGORY_FIRST= "category_first";

    private static CategoryFirstManager mCategoryFirstManager = null;

    private CategoryFirstManager(){

    }

    public static CategoryFirstManager getInstance(){

        if(mCategoryFirstManager == null) {
            mCategoryFirstManager = new CategoryFirstManager();
            mCategoryFirstManager.load();
        }
        return mCategoryFirstManager;
    }

    public void load(){

        String data = PreferenceUtil.getDefaultPreference(LuckyBolApplication.getContext()).getString(SHARED_CATEGORY_FIRST);
        Type typeOfSrc = new TypeToken<CategoryFirstManager>(){

        }.getType();

        CategoryFirstManager categoryInfoManager = new Gson().fromJson(data, typeOfSrc);

        if(categoryInfoManager != null) {
            mCategoryFirstManager = new Gson().fromJson(data, typeOfSrc);
        }
    }

    public void save(){

        Type typeOfSrc = new TypeToken<CategoryFirstManager>(){

        }.getType();
        String data = new Gson().toJson(mCategoryFirstManager, typeOfSrc);
        PreferenceUtil.getDefaultPreference(LuckyBolApplication.getContext()).put(SHARED_CATEGORY_FIRST, data);
    }

    public void clear(){

        //PreferenceUtil.getPreference(mContext, SHARED_CATEGORY_INFO).clear();
        mCategoryFirstManager = new CategoryFirstManager();
        mCategoryFirstManager.setCategoryFirstList(null);
        save();
    }

    public boolean isCalled(){

        if(mCategoryFirstManager.getCategoryFirstList() != null) {
            return true;
        }

        return false;
    }
}
