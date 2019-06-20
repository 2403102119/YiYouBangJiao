package com.tangchaoke.yiyoubangjiao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.tangchaoke.yiyoubangjiao.service.MyService;
import com.tangchaoke.yiyoubangjiao.utils.AppManager;

/**
 * KeepAlive_Activity
 *
 * @author MG
 * @time 2017/7/11 8:58
 * 系统保活activity
 */

public class Activity_KeepAlive extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //只有左上角的一个点，主要为了使用户无感知
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);

        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onPause() {
        Log.i("MG", "KeepAlive_Activity__onPause" + "开启监听服务");
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("MG", "KeepAlive_Activity: " + "保活activity被销毁");
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        super.onDestroy();
    }
}
