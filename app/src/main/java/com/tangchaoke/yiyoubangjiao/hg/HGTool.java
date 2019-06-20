package com.tangchaoke.yiyoubangjiao.hg;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Author: Motee
 * Date : 2017/8/5
 */

public class HGTool {

    /**
     * 控制软键盘
     */
    public static void hintKbTwo(Activity mActivity) {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static EditText enter(EditText mEditText) {
        //回车不换行
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        return mEditText;
    }

    static Date mCurDate;
    private static SimpleDateFormat sf = null;

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getTimeData() {
        mCurDate = new Date(System.currentTimeMillis());
        return mCurDate;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static String getTime() {
        //获取当前时间戳
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }


    /**
     * 获取当前年
     *
     * @return
     */
    public static String getYearData() {
        String mYear = getYear(getTimeData());
        return mYear;
    }

    /**
     * 可根据需要自行截取数据显示   只显示年
     *
     * @param date 当前时间
     * @return
     */
    private static String getYear(Date date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy");
        return mFormat.format(date);
    }

    /**
     * 月份
     *
     * @return
     */
    public static String getMonthData() {
        String mMonth = getMonth(getTimeData());
        return mMonth;
    }

    /**
     * 可根据需要自行截取数据显示   只显示月
     *
     * @param date
     * @return
     */
    public static String getMonth(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return mFormat.format(date);
    }

    /**
     * 日期
     *
     * @return
     */
    public static String getDayData() {
        String mDay = getDay(getTimeData());
        return mDay;
    }

    /**
     * 可根据需要自行截取数据显示   只显示日期
     *
     * @param date
     * @return
     */
    private static String getDay(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat mFormat = new SimpleDateFormat("dd");
        return mFormat.format(date);
    }

    /**
     * 判断一个字符串是否是null或空字符串""
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        boolean result = s == null || "".equals(s) || "null".equals(s);
        return result;
    }

    /**
     * 判断是否是正确的手机号
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String mRegExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        Pattern mP = Pattern.compile(mRegExp);
        Matcher mM = mP.matcher(str);
        return mM.matches();
    }

    /**
     * 验证输入的身份证号是否合法
     */
    public static boolean isLegalId(String id) {
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断有没有.com
     *
     * @return
     */
    public static int isEmailCom(String mEmail) {
        int mCom = -1;
        if (mEmail.indexOf(".com") != -1) {
            mCom = mEmail.indexOf(".com");
        }
        if (mEmail.indexOf(".co") != -1) {
            mCom = mEmail.indexOf(".co");
        }
        if (mEmail.indexOf(".c") != -1) {
            mCom = mEmail.indexOf(".c");
        }
        if (mEmail.indexOf(".") != -1) {
            mCom = mEmail.indexOf(".");
        }
        return mCom;
    }

    /**
     * 判断邮箱最后是不是.com
     *
     * @return
     */
    public static boolean isEmailComEnd(String mEmail) {
        boolean mComEnd = true;
        return mComEnd;
    }

    /**
     * 判断有没有@
     *
     * @return
     */
    public static int isEmailAiT(String mEmail) {
        int mAit = mEmail.indexOf("@");
        return mAit;
    }


    /**
     * 规则2：至少包含大小写字母及数字中的两种
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9-.!@#$%^&*()+?><]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(String time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
        long lcc_time = Long.valueOf(time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    //将list转换为带有 ， 的字符串
    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }


    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 使用
     * <p>
     * try {
     * long mVehicleSize = getFileSize(vehicleImg);
     * Log.e("ssssss人车合影", FormetFileSize(mVehicleSize) + "-----");
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     */

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * MD5 加密
     *
     * @param info
     * @return
     */
    public String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * 手机号中间四位隐藏
     *
     * @param mPNumber
     * @return
     */
    public static String isConnected(String mPNumber) {
        if (!isEmpty(mPNumber) && mPNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mPNumber.length(); i++) {
                char c = mPNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            mPNumber = sb.toString();
        }
        return mPNumber;
    }

    /**
     * String转换成Bitmap
     *
     * @param string
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * Bitmap转换成String
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        //将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

}
