package com.tangchaoke.yiyoubangjiao.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by hg on 16/10/28.
 * Android实现获取验证码的倒计时功能
 */
public class CountDownTimerUtils extends CountDownTimer {


    private TextView mTextView;
    private EditText mEditText;

    public CountDownTimerUtils(long millisInFuture, long countDownInterval, TextView mTextView, EditText mEditText) {
        super(millisInFuture, countDownInterval);
        this.mTextView = mTextView;
        this.mEditText = mEditText;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false);
        mTextView.setText(Html.fromHtml("<u>" + millisUntilFinished / 1000 + " s" + "</u>"));//设置倒计时时间
        //获取按钮上面的字
        SpannableString spannableString = new SpannableString(mTextView.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(Color.WHITE);
        //将倒计时时间设置为红色
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

    }

    @Override
    public void onFinish() {
        mTextView.setText(Html.fromHtml("<u>" + "重新获取" + " " + "</u>"));
        mEditText.setText("");
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
        mTextView.setClickable(true);
    }
}
