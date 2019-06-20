package com.tangchaoke.yiyoubangjiao.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tangchaoke.yiyoubangjiao.R;

/**
 * 自定义toase
 */

public class IToast {
    /**
     * 展示toast==LENGTH_SHORT
     * <p>
     * 默认时长 2秒
     *
     * @param msg
     */
    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 展示toast==LENGTH_LONG
     * <p>
     * 默认时长 3.5秒
     *
     * @param msg
     */
    public static void showLong(Context context, String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }


    private static void show(Context context, String massage, int show_length) {
        //使用布局加载器，将编写的toast_layout布局加载进来
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        //获取ImageView
        //ImageView image = (ImageView) view.findViewById(R.id.toast_iv);
        //设置图片
        //image.setImageResource(R.mipmap.cofe);
        //获取TextView
        TextView mTvToast = (TextView) view.findViewById(R.id.tv_toast);
        //设置显示的内容
        mTvToast.setText(massage);
        Toast toast = new Toast(context);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 100);
        //设置显示时间
        toast.setDuration(show_length);
        toast.setView(view);
        toast.show();
    }

}