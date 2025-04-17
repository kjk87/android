package com.pplus.utils.part.pref;

import com.pplus.utils.Config;

import java.util.LinkedHashMap;

/**
 * Created by 안명훈 on 16. 6. 28..
 */
public class PreferenceMapHelper extends LinkedHashMap<String, PreferenceUtil> {

    @Override
    protected boolean removeEldestEntry(Entry eldest) {
        return size() > Config.PREFERENCE_MAX_LIMIT;
    }
}
