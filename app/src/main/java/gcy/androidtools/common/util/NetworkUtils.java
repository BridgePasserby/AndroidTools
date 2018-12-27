package gcy.androidtools.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 网络相关工具类
 */
public class NetworkUtils {

    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_WIFI = 1;
    public static final int NETWORK_TYPE_2G = 2;
    public static final int NETWORK_TYPE_3G = 3;
    public static final int NETWORK_TYPE_4G = 4;

    /**
     * 判断当前是否有网络连接
     *
     * @param context
     * @return true：数据网络或者wifi；false：没有开启任何网络
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable() && networkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * Wifi网络当前是否连接
     *
     * @param context
     * @return 只开启wifi时，返回true；同时开启wifi和数据网络时，返回true；
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return networkInfo.isAvailable() && networkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 数据网络当前是否连接
     *
     * @param context
     * @return 只开启数据网络时，返回true；同时开启wifi和数据网络时会返回false；
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return networkInfo.isAvailable() && networkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 通过反射获取数据网络是否连接
     * 可以在wifi和数据网络同时开启时使用
     * @param context
     * @return
     */
    public static boolean isMobileDataEnabled(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method getMobileDataEnabled = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
            getMobileDataEnabled.setAccessible(true);
            boolean dataEnabled = (boolean) getMobileDataEnabled.invoke(connectivityManager);
            return dataEnabled;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当前连接的网络的类型
     * 原生
     * Reports the type of network to which the
     * info in this {@code NetworkInfo} pertains.
     *
     * @return one of {@link ConnectivityManager#TYPE_MOBILE},
     * {@link ConnectivityManager#TYPE_WIFI},
     * {@link ConnectivityManager#TYPE_WIMAX}, {@link ConnectivityManager#TYPE_ETHERNET},  {@link
     * ConnectivityManager#TYPE_BLUETOOTH}, or other types defined by {@link ConnectivityManager}
     */
    public static int getConcreteNetworkType(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                return networkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     * @param context
     * @return
     */
    public static int getSimpleNetworkType(Context context) {
        //结果返回值
        int netType = NETWORK_TYPE_NONE;
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = NETWORK_TYPE_WIFI;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE  && !telephonyManager.isNetworkRoaming()) {
                netType = NETWORK_TYPE_4G;
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NETWORK_TYPE_3G;
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NETWORK_TYPE_2G;
            } else {
                netType = NETWORK_TYPE_2G;
            }
        }
        return netType;
    }


    /**
     * 获取当前的网络状态
     * @param context 全局context
     * @return
     * 没有网络-NO
     * WIFI网络-WIFI
     * 4G网络-4G
     * 3G网络-3G
     * 2G网络-2G
     * 未知-Unknown
     */
    public static String getAPNType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {      //无网络
            return "NO";
        }

        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {       //wifi
            return "WIFI";
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                return "4G";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSUPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_B
                    || nSubType == TelephonyManager.NETWORK_TYPE_EHRPD
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSPAP
                    && !telephonyManager.isNetworkRoaming()) {
                return "3G";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    || nSubType == TelephonyManager.NETWORK_TYPE_1xRTT
                    || nSubType == TelephonyManager.NETWORK_TYPE_IDEN
                    && !telephonyManager.isNetworkRoaming()) {
                return "2G";
            } else {
                return "Unknown";
            }
        }
        return "Unknown";
    }

    /*
    监听网络状态变化:
    可能用到WifiManager类

    静态注册广播
    <receiver android:name=".NetWorkStateReceiver">
        <intent-filter>
            <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
            <action android:name="android.Net.wifi.WIFI_STATE_CHANGED" />
            <action android:name="android.net.wifi.STATE_CHANGE" />
        </intent-filter>
    </receiver>
    如果只使用CONNECTIVITY_CHANGE的话，在7.0以上会收不到广播。

    动态注册广播
    IntentFilter filter = new IntentFilter();
    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
    filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    registerReceiver(netWorkStateReceiver, filter);
*/

}
