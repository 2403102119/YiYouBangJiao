package com.tangchaoke.yiyoubangjiao.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.api.Api;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.base.BaseApplication;
import com.tangchaoke.yiyoubangjiao.hg.HGTool;
import com.tangchaoke.yiyoubangjiao.model.HeadPathModel;
import com.tangchaoke.yiyoubangjiao.model.MineDataModel;
import com.tangchaoke.yiyoubangjiao.type.RequestType;
import com.tangchaoke.yiyoubangjiao.utils.BitmapUtils;
import com.tangchaoke.yiyoubangjiao.utils.CheckLoginExceptionUtil;
import com.tangchaoke.yiyoubangjiao.utils.ImageDispose;
import com.tangchaoke.yiyoubangjiao.utils.PictureUtils;
import com.tangchaoke.yiyoubangjiao.view.ContainsEmojiEditText;
import com.tangchaoke.yiyoubangjiao.view.IToast;
import com.tangchaoke.yiyoubangjiao.view.ProgressHUD;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static com.tangchaoke.yiyoubangjiao.hg.HGTool.FormetFileSize;

/*
* @author hg
* create at 2019/1/2
* description: 我的资料
*/
public class Activity_MineData extends BaseActivity {

    @BindView(R.id.ll_mine_data)
    RelativeLayout mLlMineData;

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.img_avatar)
    CircleImageView mImgAvatar;

    public static final int OPEN_CAMERA = 1;
    public static final int OPEN_PICTURE = 2;
    private String mAvatarPath = ""; // 头像路径
    private String mAvatarUploadPath = "";
    private Uri mImageUri;

    @BindView(R.id.edit_nick_name)
    ContainsEmojiEditText mEditNickName;

    private String mNickName = "";

    SharedPreferences.Editor mEditor;

    @OnClick({R.id.ll_back, R.id.img_avatar, R.id.but_determine})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

            case R.id.img_avatar:
                AvatarPop();
                break;

            case R.id.but_determine:
                mNickName = mEditNickName.getText().toString().trim();
                initDetermine(mNickName);
                break;

        }

    }

    /**
     * 修改昵称
     *
     * @param mNickName
     */
    private void initDetermine(final String mNickName) {

        if (HGTool.isEmpty(mNickName)) {
            IToast.show(Activity_MineData.this, "请输入要修改的昵称");
            return;
        }

        if (mNickName.length() > 6) {
            IToast.show(Activity_MineData.this, "昵称不能多于6个字");
            return;
        }

        final ProgressHUD mProgressHUD = ProgressHUD.show(this, "请稍候", true, false, null);
        OkHttpUtils
                .post()
                .url(Api.UPDATE_USER_INFO)
                .addParams("token", BaseApplication.getApplication().getToken())
                .addParams("model.nickName", mNickName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_MineData.this, "服务器开小差！请稍后重试");
                        Log.e("====修改昵称：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("====修改昵称：：：", response);
                        Log.e("====修改昵称：：：", Api.UPDATE_USER_INFO);
                        Log.e("====修改昵称：：：", BaseApplication.getApplication().getToken());
                        Log.e("====修改昵称：：：", mNickName);
                        MineDataModel mMineDataModel = JSONObject.parseObject(response, MineDataModel.class);
                        if (RequestType.SUCCESS.equals(mMineDataModel.getStatus())) {
                            initSaveUserName(mMineDataModel);
                            mProgressHUD.dismiss();
                            IToast.show(Activity_MineData.this, mMineDataModel.getMessage());
                            finish();
                        } else {
                            if (mMineDataModel.getStatus().equals("9") || mMineDataModel.getStatus().equals("8")) {
                                mProgressHUD.dismiss();
                                IToast.show(Activity_MineData.this, "登录失效");
                            } else if (mMineDataModel.getStatus().equals("0")) {
                                mProgressHUD.dismiss();
                                IToast.show(Activity_MineData.this, mMineDataModel.getMessage());
                            }
                        }
                    }
                });

    }

    private void initSaveUserName(MineDataModel mMineDataModel) {
        mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("nickName", mMineDataModel.getModel().getNickName());
        mEditor.commit();
    }

    View mPopView;
    PopupWindow mPopupWindow;
    TextView mTvAvatarCancel;
    TextView mTvPhotoAlbum;
    TextView mTvTakingPictures;

    /**
     * 发布弹窗
     * <p>
     * 弹出底部对话框，达到背景背景透明效果
     * <p>
     * 实现原理：弹出一个全屏popupWind+ow,将Gravity属性设置bottom,根背景设置为一个半透明的颜色，
     * 弹出时popupWindow的半透明效果背景覆盖了在Activity之上 给人感觉上是popupWindow弹出后，背景变成半透明
     */
    private void AvatarPop() {
        mPopView = LayoutInflater.from(Activity_MineData.this).inflate(R.layout.popup_window_avatar, null);
        mTvAvatarCancel = mPopView.findViewById(R.id.tv_avatar_cancel);
        mTvAvatarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        mTvPhotoAlbum = mPopView.findViewById(R.id.tv_photo_album);
        mTvPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPhotoAlbum();
            }
        });
        mTvTakingPictures = mPopView.findViewById(R.id.tv_taking_pictures);
        mTvTakingPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTakingPictures();
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
        mPopupWindow.showAtLocation(mLlMineData, Gravity.BOTTOM | Gravity.LEFT, 0, 0);
    }

    /**
     * 打开相机
     */
    private void initTakingPictures() {
        mPopupWindow.dismiss();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkStoragePermission();
            checkCameraPermission(OPEN_CAMERA);
        } else {
            // 拍照
            startCamera(OPEN_CAMERA);
        }
    }

    /**
     * 打开相册
     */
    private void initPhotoAlbum() {
        // 打开系统图库
        Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picture, OPEN_PICTURE);
        mPopupWindow.dismiss();
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

    @Override
    public int getLayoutResId() {
        return R.layout.activity_mine_data;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setVisibility(View.VISIBLE);
        mTvTopTitle.setText("我的资料");
        mTvTopTitle.setTextColor(Activity_MineData.this.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void initData() {
        if (!HGTool.isEmpty(BaseApplication.getApplication().getHead())) {
            Glide.with(Activity_MineData.this).load(BaseApplication.getApplication().getHead()).into(mImgAvatar);
        }
        if (!HGTool.isEmpty(BaseApplication.getApplication().getNickName())) {
            mEditNickName.setText(BaseApplication.getApplication().getNickName());
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
        if (requestCode == OPEN_CAMERA) {
            mAvatarPath = file.getAbsolutePath();
            mAvatarUploadPath = imgPathFile + "/upload_" + file.getName();
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
                if (requestCode == OPEN_CAMERA) {
                    mImageUri = FileProvider.getUriForFile(Activity_MineData.this, "com.tangchaoke.yiyoubangjiao.fileprovider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将拍取的照片保存到指定URI
                    startActivityForResult(intent, OPEN_CAMERA);
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
        if (requestCode == OPEN_CAMERA && resultCode == RESULT_OK) {
            // 拍照用户头像
            Bitmap bitmap = BitmapUtils.createImageThumbnail(mAvatarPath);
            byte[] mByte = ImageDispose.Bitmap2Bytes(bitmap);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 0;
            /**
             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
             */
            bitmap = ImageDispose.getPicFromBytes(mByte, bitmapOptions);
            saveSmallBitmap(bitmap, mAvatarPath, mAvatarUploadPath);
            Glide.with(Activity_MineData.this).load(mAvatarPath).into(mImgAvatar);
            uploadAvatar();
        } else if (requestCode == OPEN_PICTURE && resultCode == RESULT_OK && data != null) {
            // 相册用户头像
            String pathResult = getPicPath(Activity_MineData.this, data);
            mAvatarPath = pathResult;
            File file1 = new File(mAvatarPath);
            mAvatarUploadPath = imgPath + "upload_" + file1.getName();
            Bitmap bitmap = getPicBitmap(Activity_MineData.this, data);
            byte[] mByte = ImageDispose.Bitmap2Bytes(bitmap);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 0;
            /**
             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
             */
            bitmap = ImageDispose.getPicFromBytes(mByte, bitmapOptions);
            saveSmallBitmap(bitmap, mAvatarPath, mAvatarUploadPath);
            mImgAvatar.setImageBitmap(bitmap);
            uploadAvatar();
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

    /**
     * 上传头像
     */
    private void uploadAvatar() {
        final File vehicleImg = new File(mAvatarPath);/* 头像 */
        if (!vehicleImg.exists()) {
            IToast.show(Activity_MineData.this, "文件不存在，请修改文件路径");
            return;
        }
        final ProgressHUD mProgressHUD = ProgressHUD.show(this, "正在提交",
                true, false, null);
        OkHttpUtils
                .post()
                .url(Api.UPDATE_HEAD
                        + "?token=" + BaseApplication.getApplication().getToken())
                .addFile("vehicle_pic", "vehicle.png", vehicleImg)
                .build()
                .connTimeOut(100000)      // 设置当前请求的连接超时时间
                .readTimeOut(100000)      // 设置当前请求的读取超时时间
                .writeTimeOut(100000)     // 设置当前请求的写入超时时间
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        IToast.show(Activity_MineData.this, "服务器开小差！请稍后重试");
                        Log.e("==上传头像：：：", e.getMessage());
                        mProgressHUD.dismiss();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("==上传头像：：：", response);
                        Log.e("==上传头像：：：", Api.UPDATE_HEAD);
                        Log.e("==上传头像：：：", BaseApplication.getApplication().getToken());
                        Log.e("==上传头像：：：", mAvatarUploadPath);
                        mProgressHUD.dismiss();
                        HeadPathModel mHeadPathModel = JSONObject.parseObject(response, HeadPathModel.class);
                        if (RequestType.SUCCESS.equals(mHeadPathModel.getStatus())) {
                            initSaveUserAvatar(mHeadPathModel);
                            mProgressHUD.dismiss();
                            IToast.show(Activity_MineData.this, mHeadPathModel.getMessage());
                            finish();
                        } else {
                            if (mHeadPathModel.getStatus().equals("9") || mHeadPathModel.getStatus().equals("8")) {
                                mProgressHUD.dismiss();
                                boolean isAccountException = CheckLoginExceptionUtil.checkLoginState(mHeadPathModel.getStatus(), Activity_MineData.this, true);
                                if (isAccountException == false) {
                                    IToast.show(Activity_MineData.this, "登录失效");
                                }
                            } else if (mHeadPathModel.getStatus().equals("0")) {
                                mProgressHUD.dismiss();
                                IToast.show(Activity_MineData.this, mHeadPathModel.getMessage());
                            }
                        }
                    }
                });

    }

    private void initSaveUserAvatar(HeadPathModel mHeadPathModel) {
        mEditor = BaseApplication.getApplication().getEditor();
        mEditor.putString("head", Api.PATH + mHeadPathModel.getPath());
        mEditor.commit();
    }

}
