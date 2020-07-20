package com.lx.gaomap;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;


public class MapSelectedAddressActivity extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener, Inputtips.InputtipsListener {


    private MapView mapView;
    private AMap aMap;
    private ImageView ivCenter;

    private static final String TAG = "MapSelectedAddressActiv";

    private List<Tip> hints = new ArrayList<Tip>();
    private HintAddressAdapter hintAdapter;
    private String province, city, town;
    private List<PoiItem> poiItems = new ArrayList<PoiItem>();
    private PoiAdapter poiAdapter;
    private ListView lv;
    private ListView lvPoi;
    private Button btnSearch;
    private EditText etKeywords;
    AMapLocationClient mLocationClient = null;
    TranslateAnimation animation;
    AMapLocationClientOption mLocationOption = null;
    private String lng;
    GeocodeSearch geocoderSearch; //逆地理编码
    private String lat;

    @PermissionGrant(AppSP.PMS_LOCATION)
    public void pmsLocationSuccess() {
        //权限授权成功
        mLocationClient.startLocation();
    }

    @PermissionDenied(AppSP.PMS_LOCATION)
    public void pmsLocationError() {
        Toast.makeText(MapSelectedAddressActivity.this, "权限被拒绝，无法使用该功能！",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapselectedaddress_activity);




        mapView = findViewById(R.id.mapView);
        ivCenter = findViewById(R.id.iv_center);
        lv = findViewById(R.id.lv);
        lvPoi = findViewById(R.id.lvPoi);
        btnSearch = findViewById(R.id.btnSearch);
        etKeywords = findViewById(R.id.etKeywords);

        mapView.onCreate(savedInstanceState);// 此方法必须重写

        aMap = mapView.getMap();


        LatLng location = new LatLng(34.75, 113.70);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {


            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                ivCenter.startAnimation(animation);
                CameraPosition p = aMap.getCameraPosition();
                lat = p.target.latitude + "";
                lng = p.target.longitude + "";
                LatLonPoint latLonPoint = new LatLonPoint(p.target.latitude, p.target.longitude);
                // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                geocoderSearch.getFromLocationAsyn(query);

            }
        });


        mLocationClient = new AMapLocationClient(MapSelectedAddressActivity.this);
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MPermissions.requestPermissions(this, AppSP.PMS_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            );
        } else {
            pmsLocationSuccess();
        }


        animation = new TranslateAnimation(0, 0, 0, -10);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(200);
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);

        geocoderSearch = new GeocodeSearch(MapSelectedAddressActivity.this);
        geocoderSearch.setOnGeocodeSearchListener(this);


        etKeywords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                InputtipsQuery inputquery = new InputtipsQuery(editable.toString(), "");
                inputquery.setCityLimit(true);//限制在当前城市
                Inputtips inputTips = new Inputtips(MapSelectedAddressActivity.this, inputquery);
                inputTips.setInputtipsListener(MapSelectedAddressActivity.this);
                inputTips.requestInputtipsAsyn();
            }
        });

        hintAdapter = new HintAddressAdapter(MapSelectedAddressActivity.this, hints);
        lv.setAdapter(hintAdapter);

        poiAdapter = new PoiAdapter(MapSelectedAddressActivity.this, poiItems);
        lvPoi.setAdapter(poiAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (null != hints) {
                    if (!TextUtils.isEmpty(String.valueOf(hints.get(i).getPoint().getLatitude()))) {
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(hints.get(i).getPoint().getLatitude(), hints.get(i).getPoint().getLongitude())));
                        lv.setVisibility(View.GONE);
                        etKeywords.clearFocus();
                        etKeywords.setText("");
                        KeyboardUtil.hideKeyboard(etKeywords);
                    }
                } else {
                    Log.e(TAG, "onItemClick: http------------------");
                }


            }
        });

        lvPoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PoiItem item = poiItems.get(i);
                Intent intent = new Intent();
                intent.putExtra("lat", item.getLatLonPoint().getLatitude() + "");
                intent.putExtra("lng", item.getLatLonPoint().getLongitude() + "");
                intent.putExtra("address", item.getSnippet());
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("town", town);
                intent.putExtra("province_city_town", province + city + town);
                setResult(1, intent);
                //act.finishSelf();
                finish();
            }
        });
    }

    /**
     * 定位监听
     */
    //声明定位回调监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    city = amapLocation.getCity();
                    lat = amapLocation.getLatitude() + "";
                    lng = amapLocation.getLongitude() + "";
                    final LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
                } else {
                    if (amapLocation.getErrorCode() == 12 || amapLocation.getErrorCode() == 13) {
                        Toast.makeText(MapSelectedAddressActivity.this, "定位失败！",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000) {
            province = regeocodeResult.getRegeocodeAddress().getProvince();
            city = regeocodeResult.getRegeocodeAddress().getCity();
            town = regeocodeResult.getRegeocodeAddress().getDistrict();
            poiItems.clear();
            String s = String.valueOf(regeocodeResult.getRegeocodeQuery().getPoint().getLatitude());
            if (!TextUtils.isEmpty(s)) {
                poiItems.addAll(regeocodeResult.getRegeocodeAddress().getPois());
                poiAdapter.notifyDataSetChanged();
            }



        }
    }


    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    //hints
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {
            lv.setVisibility(View.VISIBLE);
            hints.clear();
            for (int i = 0; i < tipList.size(); i++) {
                if (tipList.get(i).getPoint() != null) {
                    hints.add(tipList.get(i));
                }
            }
            hintAdapter.notifyDataSetChanged();
        }
    }



}
