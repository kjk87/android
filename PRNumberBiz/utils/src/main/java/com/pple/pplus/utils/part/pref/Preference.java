package com.pple.pplus.utils.part.pref;

import android.os.Bundle;

import java.util.Set;

/**
 * Created by 안명훈 on 16. 6. 28..
 */
public interface Preference {

    /**
     * @param key   저장할 key
     * @param value 저장할 값
     */
    void put(String key, String value);

    /**
     * @param key   저장할 key
     * @param value 저장할 값
     */
    void put(String key, boolean value);

    /**
     * @param key   저장할 key
     * @param value 저장할 값
     */
    void put(String key, float value);

    /**
     * @param key   저장할 key
     * @param value 저장할 값
     */
    void put(String key, int value);

    /**
     * @param key   저장할 key
     * @param value 저장할 값
     */
    void put(String key, long value);

    /**
     * @param key   저장할 key
     * @param value 저장할 값
     */
    void put(String key, Set<String> value);

    /**
     * @param bundle 저장할 key - value
     */
    void put(Bundle bundle);

    /**
     * @param key          저장한 key
     * @param defaultValue 기본으로 되돌려 받을 값
     * @return String
     */
    String get(String key, String defaultValue);

    /**
     * @param key          저장한 key
     * @param defaultValue 기본으로 되돌려 받을 값
     * @return boolean
     */
    boolean get(String key, boolean defaultValue);

    /**
     * @param key          저장한 key
     * @param defaultValue 기본으로 되돌려 받을 값
     * @return float
     */
    float get(String key, float defaultValue);

    /**
     * @param key          저장한 key
     * @param defaultValue 기본으로 되돌려 받을 값
     * @return int
     */
    int get(String key, int defaultValue);

    /**
     * @param key          저장한 key
     * @param defaultValue 기본으로 되돌려 받을 값
     * @return long
     */
    long get(String key, long defaultValue);

    /**
     * @param key          저장한 key
     * @param defaultValue 기본으로 되돌려 받을 값
     * @return Set<String>
     */
    Set<String> get(String key, Set<String> defaultValue);

    /**
     * @param defaultValue 기본으로 되돌려 받을 값
     * @return Bundle
     */
    Bundle get(Bundle defaultValue);

    /**
     * @param key 저장한 key
     * @return String
     */
    String getString(String key);

    /**
     * @param key 저장한 key
     * @return boolean
     */
    boolean getBoolean(String key);

    /**
     * @param key 저장한 key
     * @return float
     */
    float getFloat(String key);

    /**
     * @param key 저장한 key
     * @return int
     */
    int getInt(String key);

    /**
     * @param key 저장한 key
     * @return long
     */
    long getLong(String key);

    /**
     * @param key 저장한 key
     * @return Set<String>
     */
    Set<String> getStringSet(String key);

    /**
     * @return Bundle
     */
    Bundle getBundle();

    void remove(String var1);

    void clear();

}
