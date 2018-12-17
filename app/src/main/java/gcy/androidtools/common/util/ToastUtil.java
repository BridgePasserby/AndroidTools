package gcy.androidtools.common.util;

/**
 * Copyright (c) 2018, 北京视达科科技有限责任公司 All rights reserved.
 * author：chongyang.gao
 * date：2018/12/17
 * description：
 */

import android.content.Context;
import android.widget.Toast;

import gcy.androidtools.common.BaseApp;

/**
 * ToastUtils 利用单例模式，解决重命名toast重复弹出的问题
 */
public class ToastUtil {
    private static ToastUtil mToastUtils;
    private static Toast mToast;

    private ToastUtil(Context context) {
        if (null == mToast) {
            synchronized (ToastUtil.class){
                if (mToast == null){
                    mToast = Toast.makeText(context.getApplicationContext(), null, Toast.LENGTH_LONG);
                }
            }
        }
    }

    public static ToastUtil getInstance() {
        if (mToastUtils == null) {
            mToastUtils = new ToastUtil(BaseApp.getApplication());
        }
        return mToastUtils;
    }

    public void showShortToast(String mString) {
        if (mToast == null) {
            return;
        }
        mToast.setText(mString);
        mToast.setDuration(Toast.LENGTH_SHORT);
        // mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void showShortToast(int resId) {
        if (mToast == null) {
            return;
        }
        mToast.setText(resId);
        mToast.setDuration(Toast.LENGTH_SHORT);
        // mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void showLongToast(String mString) {
        if (mToast == null) {
            return;
        }
        mToast.setText(mString);
        mToast.setDuration(Toast.LENGTH_LONG);
        // mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void showLongToast(int resId) {
        if (mToast == null) {
            return;
        }
        mToast.setText(resId);
        mToast.setDuration(Toast.LENGTH_SHORT);
        // mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }


}