package com.tangchaoke.yiyoubangjiao.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tangchaoke.yiyoubangjiao.receiver.MyReceiver;

/**
 * MyService 保证极光推送的Receiver不被系统杀死的service
 *
 * @author Administrator
 * @modify MG
 * @time 2017/7/7 11:50
 */

public class MyService extends Service {

    private final static int GRAY_SERVICE_ID = 1001;
    private boolean bb = false;
    private int type = 0;
    private MyReceiver mMyReceiver;
    PowerManager.WakeLock mWakeLock = null;

    /**
     * PowerManager.PARTIAL_WAKE_LOCK:保持CPU运转，屏幕和键盘灯可能  是关闭的
     * PowerManager.SCREEN_DIM_WAKE_LOCK:保持CPU运转,运行屏幕显示但是屏幕有可能是灰的，允许关闭键盘灯
     * PowerManager.SCREEN_BRIGHT_WAKE_LOCK：保持CPU运转，屏幕高亮显示，允许关闭键盘灯
     * PowerManager.FULL_WAKE_LOCK：保持CPU运转，屏幕高亮显示，键盘灯高亮显示
     * PowerManager.ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
     * PowerManager.ACQUIRE_CAUSES_WAKEUP：强制屏幕亮起
     */

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquireWakeLock();
        new Thread(new MyThread()).start();

        return START_REDELIVER_INTENT;
    }

    /**
     * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
     */
    private void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(getApplicationContext().POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "SoundService");
// /*保持屏幕不熄灭，以保证服务不会被杀死*/
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SoundService");
            /*保持屏幕熄灭时仍保持CPU运行，如果程序关闭点亮手机开启服务。以保证服务不会被杀死*/
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    /**
     * 获取电源锁，强制点亮屏幕
     */
    private void shakerScreenWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(getApplicationContext().POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_DIM_WAKE_LOCK, "SoundService");/*强制点亮屏幕*/
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    /**
     * 监听服务被杀状态，如果被杀死了，再次启动它
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        bb = true;
        Log.e("==MyService", "MyService_onDestroy！！！！！");
        shakerScreenWakeLock();//当服务被杀死的时候点亮屏幕，然后重启服务
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        Log.e("==MyService", "MyService_onDestroy ---- restart！");
    }

    private class MyThread implements Runnable {

        @Override
        public void run() {
            while (!bb) {
                type++;
                if (mMyReceiver == null)
                    mMyReceiver = new MyReceiver();
                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                registerReceiver(mMyReceiver, intentFilter);
                Log.e("==MyService", "MyService_type =========" + type);
                try {
                    Thread.sleep(10000);// 线程暂停10秒，单位毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
