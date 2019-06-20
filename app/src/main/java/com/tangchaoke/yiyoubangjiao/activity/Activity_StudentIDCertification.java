package com.tangchaoke.yiyoubangjiao.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.AuditFailurePromptModel;
import com.tangchaoke.yiyoubangjiao.model.SuccessModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.type.SubjectType;
import com.tangchaoke.yiyoubangjiao.utils.BitmapUtils;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.ImageDispose;
import com.tangchaoke.yiyoubangjiao.utils.PictureUtils;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.tangchaoke.yiyoubangjiao.view.PromptEchoDialogView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/*
* @author hg
* create at 2019/1/2
* description: 在校学生证认证
*/
public class Activity_StudentIDCertification extends BaseActivity {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    /**
     * 根布局
     */
    @BindView(R.id.ll_academic_certificate)
    RelativeLayout mLlAcademicCertificate;

    /**
     * 学生证照片
     */
    @BindView(R.id.img_diploma)
    ImageView mImgDiploma;

    public static final int OPEN_DIPLOMA_CAMERA = 1;
    public static final int OPEN_DIPLOMA_PICTURE = 2;
    private String mDiplomaPath = ""; // 学生证照片路径
    private String mDiplomaUploadPath = "";
    private Uri mImageDiplomaUri;

    /**
     * 学校名称
     */
    @BindView(R.id.edit_school_name)
    EditText mEditSchoolName;

    private String mSchoolName = "";

    /**
     * 专业
     */
    @BindView(R.id.edit_profession)
    EditText mEditProfession;

    private String mProfession = "";

    /**
     * 入学时间
     */
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;

    private String mStartTime = "";

    /**
     * 发证时间
     */
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;

    private String mEndTime = "";

    /**
     * 系别
     */
    @BindView(R.id.tv_education)
    EditText mTvEducation;

    String mEducation = "";

    /**
     * 学生姓名
     */
    @BindView(R.id.edit_name)
    EditText mEditName;

    private String mName = "";

    /**
     * 学号
     */
    @BindView(R.id.edit_scholar)
    EditText mEditScholar;

    private String mScholar = "";

    /**
     * 性别
     */
    @BindView(R.id.tv_sex)
    TextView mTvSex;

    private String mSex = "男";

    /**
     * 身份证号
     */
    @BindView(R.id.edit_id_number)
    EditText mEditIDNumber;

    private String mIDNumber = "";

    /**
     * @param view
     */
    @OnClick({R.id.ll_back, R.id.ll_diploma, R.id.fl_sex, R.id.but_upload, R.id.fl_start_time, R.id.fl_end_time, R.id.fl_education})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            /**
             * 毕业证照片
             */
            case R.id.ll_diploma:
                initDiplomaPop();
                HGTool.hintKbTwo(Activity_StudentIDCertification.this);
                break;

            /**
             * 性别
             */
            case R.id.fl_sex:
                initSexPop();
                HGTool.hintKbTwo(Activity_StudentIDCertification.this);
                break;

            /**
             * 上传
             */
            case R.id.but_upload:
                mSchoolName = mEditSchoolName.getText().toString().trim();
                mProfession = mEditProfession.getText().toString().trim();
                mName = mEditName.getText().toString().trim();
                mScholar = mEditScholar.getText().toString().trim();
                mIDNumber = mEditIDNumber.getText().toString().trim();
                mEducation = mTvEducation.getText().toString().trim();
                initUpload(mSchoolName, mEducation, mProfession, mName, mScholar, mIDNumber, mStartTime, mEndTime, mSex);
                break;

            /**
             * 入学时间
             */
            case R.id.fl_start_time:
                initStartTime();
                HGTool.hintKbTwo(Activity_StudentIDCertification.this);
                break;

            /**
             * 发证时间
             */
            case R.id.fl_end_time:
                initEndTime();
                HGTool.hintKbTwo(Activity_StudentIDCertification.this);
                break;

            /**
             * 系别
             */
            case R.id.fl_education:
//                initEducation();
//                HGTool.hintKbTwo(Activity_StudentIDCertification.this);
                break;

        }

    }

    View mPopSexView;
    PopupWindow mSexPopupWindow;
    TextView mTvMale;
    TextView mTvFemale;
    TextView mTvSexCancel;

    /**
     * 选择 性别
     */
    private void initSexPop() {
        mPopSexView = LayoutInflater.from(Activity_StudentIDCertification.this).inflate(R.layout.popup_window_sex, null);
        mTvMale = mPopSexView.findViewById(R.id.tv_male);
        mTvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSex = "男";
                mTvSex.setText(mSex);
                mSexPopupWindow.dismiss();
            }
        });
        mTvFemale = mPopSexView.findViewById(R.id.tv_female);
        mTvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSex = "女";
                mTvSex.setText(mSex);
                mSexPopupWindow.dismiss();
            }
        });
        mTvSexCancel = mPopSexView.findViewById(R.id.tv_sex_cancel);
        mTvSexCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSexPopupWindow.dismiss();
            }
        });
        mSexPopupWindow = new PopupWindow(mPopSexView,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mSexPopupWindow.setAnimationStyle(R.style.take_photo_anim);
        mSexPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mSexPopupWindow.setFocusable(true);
        // 显示在根布局的底部
        mSexPopupWindow.showAtLocation(mLlAcademicCertificate, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    /**
     * 系别选择器
     */
    OptionsPickerView mEducationPvOptions;

    /**
     * 发证时间选择器
     */
    TimePickerView pvEndTime;

    /**
     * 入学时间
     */
    Date mStartData;

    /**
     * 发证时间
     */
    Date mEndData;

    /**
     * 发证时间
     */
    private void initEndTime() {
        pvEndTime = new TimePickerView.Builder(
                Activity_StudentIDCertification.this,
                new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date2, View v) {//选中事件回调
                        if (mStartData != null) {
                            if (mStartData.getTime() > date2.getTime()) {
                                IToast.show(Activity_StudentIDCertification.this, "入学时间大于发证时间");
                                return;
                            } else {
                                mEndTime = getTime(date2);
                                mTvEndTime.setText(mEndTime);
                                mEndData = date2;
                            }
                        } else {
                            mEndTime = getTime(date2);
                            mTvEndTime.setText(mEndTime);
                            mEndData = date2;
                        }
                    }
                })
                .setType(TimePickerView.Type.YEAR_MONTH)//默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleText("请选择发证时间")//标题文字
                .setContentSize(20)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Activity_StudentIDCertification.this.getResources().getColor(R.color.color999999))//确定按钮文字颜色
                .setCancelColor(Activity_StudentIDCertification.this.getResources().getColor(R.color.color999999))//取消按钮文字颜色
                .setLabel("年", "月", "日", "时", "分", "秒")
                //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），
                // 此项可以在弹出选择器的时候重新设置当前时间，
                // 避免在初始化之后由于时间已经设定，
                // 导致选中时间与当前时间不匹配的问题。
                .setDate(Calendar.getInstance())
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        pvEndTime.show();
    }

    /**
     * 入学时间选择器
     */
    TimePickerView pvStartTime;

    /**
     * 入学时间
     */
    private void initStartTime() {
        pvStartTime = new TimePickerView.Builder(
                Activity_StudentIDCertification.this,
                new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date2, View v) {//选中事件回调
                        if (mEndData != null) {
                            if (mEndData.getTime() < date2.getTime()) {
                                IToast.show(Activity_StudentIDCertification.this, "入学时间大于发证时间");
                                return;
                            } else {
                                mStartData = date2;
                                mStartTime = getTime(date2);
                                mTvStartTime.setText(mStartTime);
                            }
                        } else {
                            mStartData = date2;
                            mStartTime = getTime(date2);
                            mTvStartTime.setText(mStartTime);
                        }
                    }
                })
                .setType(TimePickerView.Type.YEAR_MONTH)//默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleText("请选择入学时间")//标题文字
                .setContentSize(20)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Activity_StudentIDCertification.this.getResources().getColor(R.color.color999999))//确定按钮文字颜色
                .setCancelColor(Activity_StudentIDCertification.this.getResources().getColor(R.color.color999999))//取消按钮文字颜色
                .setLabel("年", "月", "日", "时", "分", "秒")
                //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），
                // 此项可以在弹出选择器的时候重新设置当前时间，
                // 避免在初始化之后由于时间已经设定，
                // 导致选中时间与当前时间不匹配的问题。
                .setDate(Calendar.getInstance())
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        pvStartTime.show();
    }

    /**
     * 转换需要类型
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    View mPopView;
    PopupWindow mPopupWindow;
    TextView mTvAvatarCancel;
    TextView mTvPhotoAlbum;
    TextView mTvTakingPictures;

    /**
     * 毕业证照片
     */
    private void initDiplomaPop() {
        mPopView = LayoutInflater.from(Activity_StudentIDCertification.this).inflate(R.layout.popup_window_avatar, null);
        mTvAvatarCancel = mPopView.findViewById(R.id.tv_avatar_cancel);
        /**
         * 取消
         */
        mTvAvatarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mTvPhotoAlbum = mPopView.findViewById(R.id.tv_photo_album);
        /**
         * 相册
         */
        mTvPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDiplomaPhotoAlbum();
            }
        });
        mTvTakingPictures = mPopView.findViewById(R.id.tv_taking_pictures);
        /**
         * 拍照
         */
        mTvTakingPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDiplomaTakingPictures();
            }
        });
        mPopupWindow = new PopupWindow(mPopView,
                GridLayoutManager.LayoutParams.MATCH_PARENT,
                GridLayoutManager.LayoutParams.MATCH_PARENT);
        // 设置弹出动画
        mPopupWindow.setAnimationStyle(R.style.take_photo_anim);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        // 显示在根布局的底部
        mPopupWindow.showAtLocation(mLlAcademicCertificate, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    /**
     * 毕业证照片  拍照
     */
    private void initDiplomaTakingPictures() {
        mPopupWindow.dismiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkStoragePermission();
            checkCameraPermission(OPEN_DIPLOMA_CAMERA);
        } else {
            // 拍照
            startCamera(OPEN_DIPLOMA_CAMERA);
        }
    }

    /**
     * 毕业证照片  相册
     */
    private void initDiplomaPhotoAlbum() {
        // 打开系统图库
        Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, OPEN_DIPLOMA_PICTURE);
        mPopupWindow.dismiss();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_student_id_certification;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("在校学生认证");
    }

    private String mType = "";

    @Override
    protected void initData() {
        mType = getIntent().getStringExtra("type");
        if (!HGTool.isEmpty(mType)) {
            if (mType.equals("1")) {
                initAuditFailurePrompt();
            }
        }
    }

    /**
     * 审核失败提示
     */
    private void initAuditFailurePrompt() {
        OkHttpUtils
                .post()
                .url(Api.RETURN_MESSAGE)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("type", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_StudentIDCertification.this, "服务器开小差！请稍后重试");
                        Log.e("==返回认证信息：：：", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==返回认证信息：：：", response);
                        Log.e("==返回认证信息：：：", Api.RETURN_MESSAGE);
                        Log.e("==返回认证信息：：：", BaseApplication.getApplication().getToken());
                        Log.e("==返回认证信息：：：", "2");
                        AuditFailurePromptModel mAuditFailurePromptModel = JSONObject.parseObject(response, AuditFailurePromptModel.class);
                        if (RequestType.SUCCESS.equals(mAuditFailurePromptModel.getStatus())) {
                            initAuditFailurePromptDisplay(mAuditFailurePromptModel.getModel());
                        } else {
                            if (mAuditFailurePromptModel.getStatus().equals("9") || mAuditFailurePromptModel.getStatus().equals("8")) {
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mAuditFailurePromptModel.getStatus(), Activity_StudentIDCertification.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_StudentIDCertification.this, "登录失效");
                                }
                            } else if (mAuditFailurePromptModel.getStatus().equals("0")) {
                                IToast.show(Activity_StudentIDCertification.this, mAuditFailurePromptModel.getMessage());
                            }
                        }
                    }
                });
    }

    private String mImage = "";

    /**
     * 显示数据
     *
     * @param mAuditFailurePromptModel
     */
    private void initAuditFailurePromptDisplay(AuditFailurePromptModel.AuditFailurePromptModelModel mAuditFailurePromptModel) {
        if (!HGTool.isEmpty(mAuditFailurePromptModel.getReason())) {
            isLoginDialog(mAuditFailurePromptModel.getReason());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getName())) {
            mEditName.setText(mAuditFailurePromptModel.getName());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getIdNumber())) {
            mEditIDNumber.setText(mAuditFailurePromptModel.getIdNumber());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getSchoolName())) {
            mEditSchoolName.setText(mAuditFailurePromptModel.getSchoolName());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getStartTime())) {
            mStartTime = mAuditFailurePromptModel.getStartTime();
            mTvStartTime.setText(mAuditFailurePromptModel.getStartTime());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getIssuingTime())) {
            mEndTime = mAuditFailurePromptModel.getIssuingTime();
            mTvEndTime.setText(mAuditFailurePromptModel.getIssuingTime());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getDepartment())) {
            mEducation = mAuditFailurePromptModel.getDepartment();
            mTvEducation.setText(mAuditFailurePromptModel.getDepartment());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getUserMajor())) {
            mProfession = mAuditFailurePromptModel.getUserMajor();
            mEditProfession.setText(mAuditFailurePromptModel.getUserMajor());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getName())) {
            mEditName.setText(mAuditFailurePromptModel.getName());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getStudentNo())) {
            mEditScholar.setText(mAuditFailurePromptModel.getStudentNo());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getSex())) {
            mSex = mAuditFailurePromptModel.getSex();
            mTvSex.setText(mAuditFailurePromptModel.getSex());
        }

        if (!HGTool.isEmpty(mAuditFailurePromptModel.getPhoto())) {
            mImage = mAuditFailurePromptModel.getPhoto();
            Glide.with(Activity_StudentIDCertification.this).load(Api.PATH + mAuditFailurePromptModel.getPhoto()).centerCrop().into(mImgDiploma);
        }


    }

    /**
     * 是否失败 0 失败  1 提交审核成功
     */
    private int isfailure = 0;

    private void isLoginDialog(String mReason) {
        try {
            final PromptEchoDialogView mCleanUpCacheDialogView = new PromptEchoDialogView(Activity_StudentIDCertification.this);
            if (isfailure == 0) {
                mCleanUpCacheDialogView.setTitle("温馨提示");
            } else if (isfailure == 1) {
                mCleanUpCacheDialogView.setTitle("提交成功");
            }
            mCleanUpCacheDialogView.setContent(mReason);
            mCleanUpCacheDialogView.setCancelable(false);
            mCleanUpCacheDialogView.setCustomOnClickListener(new PromptEchoDialogView.OnCustomDialogListener() {
                @Override
                public void setYesOnclick() {
                    if (isfailure == 0) {
                        mCleanUpCacheDialogView.dismiss();
                    } else if (isfailure == 1) {
                        finish();
                        mCleanUpCacheDialogView.dismiss();
                    }
                }
            });
            mCleanUpCacheDialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查相机权限
     */
    public void checkCameraPermission(int requestCode) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            startCamera(requestCode);
        }
    }

    /**
     * 打开相机
     *
     * @param requestCode
     */
    private void startCamera(int requestCode) {
        File imgPathFile = new File(Environment.getExternalStorageDirectory(), "mydownloads");
        if (!imgPathFile.exists()) {
            imgPathFile.mkdirs();
        }
        File file = new File(imgPathFile, System.currentTimeMillis() + ".jpg");
        if (requestCode == OPEN_DIPLOMA_CAMERA) {
            mDiplomaPath = file.getAbsolutePath();
            mDiplomaUploadPath = imgPathFile + "/upload_" + file.getName();
            mImage = "";
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //当前系统为Android 6.0及以下版本
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(camera, requestCode);
        } else {
            // 当前系统为7.0及以上版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                //通过FileProvider创建一个content类型的Uri
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                if (requestCode == OPEN_DIPLOMA_CAMERA) {
                    mImageDiplomaUri = FileProvider.getUriForFile(Activity_StudentIDCertification.this, "com.tangchaoke.yiyoubangjiao.fileprovider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageDiplomaUri);//将拍取的照片保存到指定URI
                    startActivityForResult(intent, OPEN_DIPLOMA_CAMERA);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imgPath = Environment.getExternalStorageDirectory() + "/mydownloads/";
        File file = new File(imgPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (requestCode == OPEN_DIPLOMA_CAMERA && resultCode == RESULT_OK) {
            // 拍照用户头像
            Bitmap bitmap = BitmapUtils.createImageThumbnail(mDiplomaPath);
            byte[] mByte = ImageDispose.Bitmap2Bytes(bitmap);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 0;
            /**
             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
             */
            bitmap = ImageDispose.getPicFromBytes(mByte, bitmapOptions);
            saveSmallBitmap(bitmap, mDiplomaPath, mDiplomaUploadPath);
            Glide.with(Activity_StudentIDCertification.this).load(mDiplomaPath).centerCrop().into(mImgDiploma);
//            mImgDiploma.setImageBitmap(bitmap);
        } else if (requestCode == OPEN_DIPLOMA_PICTURE && resultCode == RESULT_OK && data != null) {
            // 相册用户头像
            String pathResult = getPicPath(Activity_StudentIDCertification.this, data);
            mDiplomaPath = pathResult;
            File file1 = new File(mDiplomaPath);
            mDiplomaUploadPath = imgPath + "upload_" + file1.getName();
            mImage = "";
            Bitmap bitmap = getPicBitmap(Activity_StudentIDCertification.this, data);
            byte[] mByte = ImageDispose.Bitmap2Bytes(bitmap);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 0;
            /**
             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
             */
            bitmap = ImageDispose.getPicFromBytes(mByte, bitmapOptions);
            saveSmallBitmap(bitmap, mDiplomaPath, mDiplomaUploadPath);
            Glide.with(Activity_StudentIDCertification.this).load(mDiplomaPath).centerCrop().into(mImgDiploma);
//            mImgDiploma.setImageBitmap(bitmap);
        }
    }

    /**
     * 将拍照或选择所得的图片压缩后保存在指定路径下
     *
     * @param bitmap        拍照或选择所得的图片对应的Bitmap
     * @param imgPath       拍照或选择所得的图片路径
     * @param uploadImgPath 压缩后的图片路径（上传用的图片）
     */
    private void saveSmallBitmap(Bitmap bitmap, String imgPath, String uploadImgPath) {
        Bitmap smallBitmap;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            smallBitmap = PictureUtils.getSmallBitmap(imgPath, 1120, 630);
        } else {
            smallBitmap = PictureUtils.getSmallBitmap(imgPath, 630, 1120);
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(uploadImgPath);
            smallBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把数据写入文件
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            smallBitmap.recycle();
        }
    }

    File mDiplomaFile;
    private String mAvatar = "";

    /**
     * 上传实在校学生证认证信息
     *
     * @param mSchoolName 学校姓名
     * @param mEducation  系别
     * @param mProfession 专业
     * @param mName       姓名
     * @param mScholar    学号
     * @param mIDNumber   身份证号
     * @param mStartTime  入学时间
     * @param mEndTime    发证时间
     * @param mSex        性别
     */
    private void initUpload(
            final String mSchoolName,
            final String mEducation,
            final String mProfession,
            final String mName,
            final String mScholar,
            final String mIDNumber,
            final String mStartTime,
            final String mEndTime,
            final String mSex) {
        if (HGTool.isEmpty(mSchoolName)) {
            IToast.show(Activity_StudentIDCertification.this, "请输入学校名称");
            return;
        }

        if (HGTool.isEmpty(mName)) {
            IToast.show(Activity_StudentIDCertification.this, "请输入学生姓名");
            return;
        }

        if (HGTool.isEmpty(mSex)) {
            IToast.show(Activity_StudentIDCertification.this, "请选择性别");
            return;
        }

        if (!HGTool.isEmpty(mIDNumber)) {
            if (!HGTool.isLegalId(mIDNumber)) {
                IToast.show(Activity_StudentIDCertification.this, "请输入真实身份证号");
                return;
            }
        }

        PostFormBuilder builder = OkHttpUtils.post();
        builder.url(Api.UPDATE_STUDENT_ID_CARD
                + "?token=" + BaseApplication.getApplication().getToken()
                + "&model.schoolName=" + mSchoolName
                + "&model.startTime=" + mStartTime
                + "&model.issuingTime=" + mEndTime
                + "&model.department=" + mEducation
                + "&model.userMajor=" + mProfession
                + "&model.name=" + mName
                + "&model.studentNo=" + mScholar
                + "&model.idNumber=" + mIDNumber
                + "&model.sex=" + mSex
                + "&url=" + mImage);

        if (!HGTool.isEmpty(mDiplomaPath)) {
            mAvatar = "student_card.png";
            mDiplomaFile = new File(mDiplomaPath);/* 学生证 */
        } else {
            mAvatar = "";
            mDiplomaFile = new File("");/* 学生证 */
        }
        if (mDiplomaFile.exists()) {
            builder.addFile("student_card_pic", mAvatar, mDiplomaFile);
        }

        Map<String, File> mMap = new HashMap();
        mMap.put("===", mDiplomaFile);

        File[] mByte = new File[]{mDiplomaFile};
        List<File> mByteList = Arrays.asList(mByte);
        Log.e("====mByte::::::", mByteList.get(0).getPath() + "===mByteList");

        final ProgressHUD mProgressHUD = ProgressHUD.show(this, "正在提交", true, false, null);
        builder
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_StudentIDCertification.this, "服务器开小差！请稍后重试");
                        Log.e("==上传在校学生证认证：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==上传在校学生证认证：：：", response);
                        Log.e("==上传在校学生证认证：：：", Api.UPDATE_STUDENT_ID_CARD);
                        Log.e("==上传在校学生证认证：：：", BaseApplication.getApplication().getToken());
                        Log.e("==上传在校学生证认证：：：", "schoolName=" + mSchoolName);
                        Log.e("==上传在校学生证认证：：：", "startTime=" + mStartTime);
                        Log.e("==上传在校学生证认证：：：", "issuingTime=" + mEndTime);
                        Log.e("==上传在校学生证认证：：：", "department=" + mEducation);
                        Log.e("==上传在校学生证认证：：：", "userMajor=" + mProfession);
                        Log.e("==上传在校学生证认证：：：", "name=" + mName);
                        Log.e("==上传在校学生证认证：：：", "studentNo=" + mScholar);
                        Log.e("==上传在校学生证认证：：：", "idNumber=" + mIDNumber);
                        Log.e("==上传在校学生证认证：：：", "sex=" + mSex);
                        Log.e("==上传在校学生证认证：：：", "student_card_pic=" + mDiplomaUploadPath);
                        mProgressHUD.dismiss();
                        SuccessModel mSuccessModel = JSONObject.parseObject(response, SuccessModel.class);
                        if (RequestType.SUCCESS.equals(mSuccessModel.getStatus())) {
                            isfailure = 1;
                            isLoginDialog("您的认证资料已经提交成功，我们将尽快为你审核");
                            mProgressHUD.dismiss();
                        } else {
                            if (mSuccessModel.getStatus().equals("9") || mSuccessModel.getStatus().equals("8")) {
                                mProgressHUD.dismiss();
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mSuccessModel.getStatus(), Activity_StudentIDCertification.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_StudentIDCertification.this, "登录失效");
                                }
                            } else if (mSuccessModel.getStatus().equals("0")) {
                                mProgressHUD.dismiss();
                                IToast.show(Activity_StudentIDCertification.this, mSuccessModel.getMessage());
                            }
                        }
                    }
                });
    }

}
