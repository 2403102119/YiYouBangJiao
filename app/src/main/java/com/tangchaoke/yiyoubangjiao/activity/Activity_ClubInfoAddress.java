package com.tangchaoke.yiyoubangjiao.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.tangchaoke.yiyoubangjiao.R;
import com.tangchaoke.yiyoubangjiao.base.BaseActivity;
import com.tangchaoke.yiyoubangjiao.map.ALocationClientFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Activity_ClubInfoAddress extends BaseActivity implements AMapLocationListener,
        AMap.OnCameraChangeListener, LocationSource, AMap.OnMapTouchListener, AMap.OnMarkerClickListener {

    @BindView(R.id.tv_top_title)
    TextView mTvTopTitle;

    @BindView(R.id.club_info_address)
    MapView mClubInfoAddress;

    private AMap aMap;

    private AMapLocationClient mAMapLocationClient;

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private List<LatLng> mLatLngs;

    double mLat = 0;

    double mLng = 0;

    BitmapDescriptor fromResource01 = null;

    private boolean canMove = true;//变量控制拖动不返回中心点

    LocationSource.OnLocationChangedListener mListeners;

    MyLocationStyle myLocationStyle;;

    @OnClick({R.id.ll_back})
    void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_back:
                finish();
                break;

        }

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_club_info_address;
    }

    @Override
    public void initTitleBar() {
        mTvTopTitle.setText("棋类机构地址");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClubInfoAddress.onCreate(savedInstanceState);
        initViews();
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //定位初始化
        mAMapLocationClient = ALocationClientFactory.createLocationClient(Activity_ClubInfoAddress.this,
                ALocationClientFactory.createDefaultOption(), this);
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mAMapLocationClient.setLocationOption(mLocationOption);
        //开启定位
        mAMapLocationClient.startLocation();
    }

    private void initViews() {
        if (aMap == null) {
            aMap = mClubInfoAddress.getMap();//地图控制器对象
        }
        aMap.setLocationSource(this);
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(null);
        //不显示Marker外的圆圈
        myLocationStyle.strokeColor(Color.argb(00, 255, 255, 255));
        myLocationStyle.radiusFillColor(Color.argb(00, 255, 255, 255));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMapTouchListener(this);
        UiSettings uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);  //隐藏缩放按钮
        aMap.setOnCameraChangeListener(this); // 添加移动地图事件监听器
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnMarkerClickListener(this);
    }

    LatLng mLatLng;

    private void initDatas() {
        mLat = getIntent().getDoubleExtra("latitude", 0);
        mLng = getIntent().getDoubleExtra("longitude", 0);
        mLatLngs = new ArrayList<>();
        mLatLngs.add(new LatLng(mLat, mLng));
        LatLngBounds.Builder mLatLngBounds = LatLngBounds.builder();
        for (int i = 0; i < mLatLngs.size(); i++) {
            mLatLngBounds.include(mLatLngs.get(i));
            mLat = getIntent().getDoubleExtra("latitude", 0);
            mLng = getIntent().getDoubleExtra("longitude", 0);
            fromResource01 = new BitmapDescriptorFactory().fromResource(R.drawable.ic_position_logo);
            MarkerOptions mark = new MarkerOptions();
            mLatLng = new LatLng(mLat, mLng);
            mark.position(mLatLng); //mark的坐标位置
            ArrayList list_icons = new ArrayList();
            list_icons.add(fromResource01);
            mark.icons(list_icons);//添加mark显示的图片
            aMap.addMarker(mark);
        }
        LatLngBounds bounds = mLatLngBounds.build();
        aMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(bounds, 100, 100, 100, 100));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.d("activateactivate", "onLocationChanged");
        //Log.d("activateactivate","aMapLocation.getErrorCode()="+aMapLocation.getErrorCode() );
        if (mListeners != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                if (canMove) {
                    initDatas();
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("activateactivate", errText);
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    /**
     * 重新绘制加载地图
     */
    @Override
    public void onResume() {
        super.onResume();
        mClubInfoAddress.onResume();
    }

    /**
     * 暂停地图的绘制
     */
    @Override
    public void onPause() {
        super.onPause();
        mClubInfoAddress.onPause();
    }

    /**
     * 保存地图当前的状态方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mClubInfoAddress.onSaveInstanceState(outState);
    }

    /**
     * 销毁地图
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mClubInfoAddress.onDestroy();
        if (mAMapLocationClient != null) {
            mAMapLocationClient.stopLocation();
            mAMapLocationClient.onDestroy();
        }
        mAMapLocationClient = null;
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        canMove = true;
        mListeners = onLocationChangedListener;
        Log.d("activateactivate", "activateactivate");
        if (mAMapLocationClient == null) {
            mAMapLocationClient = new AMapLocationClient(getApplicationContext());
            mLocationOption = new AMapLocationClientOption();
            mAMapLocationClient.setLocationListener(this);
            //  mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mAMapLocationClient.setLocationOption(mLocationOption);
            mAMapLocationClient.stopLocation();
            mAMapLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        Log.d("activateactivate", "deactivatedeactivate");
        mListeners = null;
        if (mAMapLocationClient != null) {
            mAMapLocationClient.stopLocation();
            mAMapLocationClient.onDestroy();
        }
        mAMapLocationClient = null;
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (canMove) {
            //用户拖动地图后，不再跟随移动，直到用户点击定位按钮
            canMove = false;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
