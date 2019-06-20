package com.tangchaoke.yiyoubangjiao.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tangchaoke.yiyoubangjiao.R;

/**
 * PlayService 推送声音播放服务
 * 2017/7/4.
 *
 * @modify MG
 * @time 2017/7/13 16:12
 */

public class PlayService extends Service implements MediaPlayer.OnCompletionListener {

    MediaPlayer player;

    PowerManager.WakeLock mWakeLock = null;

    /**
     * PowerManager.PARTIAL_WAKE_LOCK:保持CPU运转，屏幕和键盘灯可能是关闭的
     * PowerManager.SCREEN_DIM_WAKE_LOCK:保持CPU运转,运行屏幕显示但是屏幕有可能是灰的，允许关闭键盘灯
     * PowerManager.SCREEN_BRIGHT_WAKE_LOCK：保持CPU运转，屏幕高亮显示，允许关闭键盘灯
     * PowerManager.FULL_WAKE_LOCK：保持CPU运转，屏幕高亮显示，键盘灯高亮显示
     * PowerManager.ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
     * PowerManager.ACQUIRE_CAUSES_WAKEUP：强制屏幕亮起
     */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopSelf();
    }

    //在这里我们需要实例化MediaPlayer对象
    public void onCreate() {
        super.onCreate();
        //我们从raw文件夹中获取一个应用自带的mp3文件
//        player = MediaPlayer.create(this, R.raw.new_order);
//        player.setOnCompletionListener(this);
    }

    /**
     * 该方法在SDK2.0才开始有的，替代原来的onStart方法
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!player.isPlaying()) {
            player.start();
            acquireWakeLock();
            Log.e("==PlayService", "play");
        }
        return START_STICKY;
    }

    public void onDestroy() {
        //super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();

        if (mWakeLock != null) {
            Log.e("==PlayService", "声音播放服务结束,释放唤醒锁");
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    /**
     * 获取电源锁，当消息通知来临的时候点亮屏幕
     */
    private void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) this.getSystemService(getApplicationContext().POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "SoundService");/*强制点亮屏幕*/
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }
}
