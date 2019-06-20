package com.tangchaoke.yiyoubangjiao.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * MyReceiver 用于监听用户操作点击物理按键（home键、菜单键）广播监听
 *
 * @modify MG
 * @time 2017/7/13 16:33
 */

public class MyMonitorPhysicalKeyReceiver extends BroadcastReceiver {

    private final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private Context context1;

    public MyMonitorPhysicalKeyReceiver() {

    }

    public MyMonitorPhysicalKeyReceiver(Context context) {
        context1 = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);

            if (reason == null)
                return;

            // Home键
            if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
//                ((Activity) context1).moveTaskToBack(false);
                Toast.makeText(context, "按了Home键", Toast.LENGTH_SHORT).show();
            }

            // 最近任务列表键
            if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
//                ((Activity) context1).moveTaskToBack(false);
                Toast.makeText(context1, "按了最近任务列表", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
