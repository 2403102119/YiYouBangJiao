package com.tangchaoke.yiyoubangjiao.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 检查是否连接网络
 */
public class NetWorkUsefulUtils {
    /**
     * 判断网络是否可用：
     *
     * @param context
     * @return false:不可用 true:可用
     */
    public static boolean getActiveNetwork(Context context) {
        if (context == null)
            return false;
        ConnectivityManager mConnMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnMgr == null)
            return false;
        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo();

        return !(aActiveInfo == null);
    }
}
