package gcy.androidtools.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Copyright (c) 2018, 北京视达科科技有限责任公司 All rights reserved.
 * author：chongyang.gao
 * date：2018/10/16
 * description：
 */

public class SharePreferenceUtil {

    public static void put(Context context, String key, String value) {
        assert key == null;
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String get(Context context, String key) {
        return get(context, key, "");
    }

    public static String get(Context context, String key, String defVal) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.getString(key, defVal);
    }

    public static boolean remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return sp.edit().remove(key).commit();
    }
}
