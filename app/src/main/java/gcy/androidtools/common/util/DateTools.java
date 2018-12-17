package gcy.androidtools.common.util;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Copyright (c) 2018, 北京视达科科技有限责任公司 All rights reserved.
 * author：chongyang.gao
 * date：2018/10/16
 * description：
 */
public class DateTools {

    private static final String TAG = DateTools.class.getSimpleName();
    public static final String FMT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";

    public static final String FMT_YYYY_MM_DD = "yyyy/MM/dd";

    public static final String FMT_YYYYMMDD_T_HHMMSS_Z = "yyyyMMdd'T'hhmmss'Z'";

    public static final long getTime(String format, String timeStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = simpleDateFormat.parse(timeStr);
            long time = date.getTime();
            return time / 1000;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return 0l;
    }

    public static final String formatTime(String format, long second) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            String result = simpleDateFormat.format(new Date(second * 1000));
            return result;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    public static final String getUtcTime(long millis) {
        return formatTime(FMT_YYYYMMDD_T_HHMMSS_Z, millis / 1000);
    }
}
