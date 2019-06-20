package com.tangchaoke.yiyoubangjiao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tangchaoke.yiyoubangjiao.activity.Activity_KeepAlive;
import com.tangchaoke.yiyoubangjiao.utils.AppManager;

/**
 * KeepAliveReceiver  监听手机屏幕状态广播
 *
 * @author Administrator
 * @modify MG
 * @time 2017/7/11 9:18
 */
public class KeepAliveReceiver extends BroadcastReceiver {

    private Context mContext;

    public KeepAliveReceiver() {
    }

    public KeepAliveReceiver(Context context) {
        this.mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            //锁屏、黑屏
            //启动保活Activity
            Log.e("==KeepAliveReceiver", "onReceive: " + mContext.toString());
            Intent aIntent = new Intent(mContext, Activity_KeepAlive.class);
            mContext.startActivity(aIntent);
            Log.e("==KeepAliveReceiver", "KeepAliveReceiver: " + "用户锁屏或熄屏");
            Log.e("==KeepAliveReceiver", "KeepAliveReceiver: " + "保活机制启动");
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
            //亮屏
            Log.e("==KeepAliveReceiver", "KeepAliveReceiver: " + "用户点亮屏幕");
            Log.e("==KeepAliveReceiver", "KeepAliveReceiver: " + "保活机制取消");
            AppManager.getInstance().killActivity(Activity_KeepAlive.class);
        }
    }


}
