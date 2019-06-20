package com.tangchaoke.yiyoubangjiao.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.activity.Activity_Settings;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.AskForVersionInfoModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.UpDatedDialogView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/25.
 */
public class UpdateApp {
    public Activity mContext;
    public ProgressDialog progressDialog;

    public UpdateApp(Activity mContext, ProgressDialog progressDialog) {
        this.mContext = mContext;
        this.progressDialog = progressDialog;
    }

    /**
     * 检查是否有新版本，如果有就升级
     */
    public void checkUpdate() {
        askForVersionInfo();
    }

    /**
     * 检测版本信息
     */
    public void askForVersionInfo() {
        OkHttpUtils
                .get()
                .url(Api.GET_SYSTEM_VERSION)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(mContext,"服务器开小差！请稍后重试");
                        Log.e("==更新：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==更新：：：", response);
                        AskForVersionInfoModel mAskForVersionInfoModel = JSONObject.parseObject(response, AskForVersionInfoModel.class);
                        if (RequestType.SUCCESS.equals(mAskForVersionInfoModel.getStatus())) {
                            if (mAskForVersionInfoModel.getModel() != null) {
                                if (ApkUtils.getVersionCode(mContext) < mAskForVersionInfoModel.getModel().getVersionCode()) {
                                    showUpdateDialog(mAskForVersionInfoModel.getModel().getApkurl(), mAskForVersionInfoModel.getModel().getDescription());
                                } else {
                                    IToast.show(mContext, "当前已是最新版本，无需更新");
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 弹出升级对话框
     */
    public void showUpdateDialog(final String apkurl, String description) {
        final UpDatedDialogView mUpDatedDialogView = new UpDatedDialogView(mContext);
        mUpDatedDialogView.setContent(description);
        mUpDatedDialogView.setCustomOnClickListener(new UpDatedDialogView.OnCustomDialogListener() {
            @Override
            public void setYesOnclick() {
                mUpDatedDialogView.dismiss();
                // 下载APK，并且替换安装
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // sdcard存在
                    createProgressDialog();
                    progressDialog.show();
                    OkHttpUtils
                            .get()
                            .url(Api.PATH + apkurl)
                            .build()
                            .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath()
                                    + "/mydownloads" + "/gandaji.apk", "gson-2.2.1.jar") {
                                @Override
                                public void inProgress(final float progress, long total) {
                                    if (progress < 1) {
                                        progressDialog.setProgress((int) (100 * progress));
                                    }
                                    mUpDatedDialogView.dismiss();
                                }

                                @Override
                                public File parseNetworkResponse(okhttp3.Response response) throws Exception {
                                    mUpDatedDialogView.dismiss();
                                    return super.parseNetworkResponse(response);
                                }

                                @Override
                                public File saveFile(okhttp3.Response response) throws IOException {
                                    mUpDatedDialogView.dismiss();
                                    return super.saveFile(response);
                                }

                                @Override
                                public void onError(Call call, Exception e) {
                                    IToast.show(mContext, "下载失败");
                                    mUpDatedDialogView.dismiss();
                                }


                                @Override
                                public void onResponse(File file) {
                                    installAPK(file);
                                    progressDialog.dismiss();
                                    mUpDatedDialogView.dismiss();
                                }
                            });

                } else {
                    IToast.show(mContext, "没有sdcard，请安装上再试");
                    mUpDatedDialogView.dismiss();
                    return;
                }
            }

            @Override
            public void setNoOnclick() {
                mUpDatedDialogView.dismiss();
            }
        });
        mUpDatedDialogView.setCancelable(false);
        mUpDatedDialogView.show();

    }

    /**
     * 安装APK
     *
     * @param t
     */
    private void installAPK(File t) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 判断版本是否在7.0以上
            // 在AndroidManifest.xml中声明的的android:authorities值
            Uri apkUri = FileProvider.getUriForFile(mContext,
                    "com.tangchaoke.yiyoubangjiao.fileprovider", t);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri,
                    "application/vnd.android.package-archive");
            mContext.startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(t),
                    "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(install);
        }
    }

    //progress dialog
    private void createProgressDialog() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        progressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        progressDialog.setIcon(R.drawable.ic_launcher);// 设置提示的title的图标，默认是没有的
//        progressDialog.setTitle("提示信息");// 设置提示的title的标题，默认是没有的
        progressDialog.setMax(100);
        progressDialog.setMessage("下载进度");
    }

}
