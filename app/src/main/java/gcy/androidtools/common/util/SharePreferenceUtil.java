package gcy.androidtools.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/8/24 0024.
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
