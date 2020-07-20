package com.lx.gaomap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lx.gaomap.utils.AppUtils;
import com.lx.gaomap.utils.ShareUtils;

public class MainActivity extends AppCompatActivity implements AMapLocationListener {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("https://www.baidu.com");

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, MapSelectedAddressActivity.class), 101);
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareUtils(MainActivity.this).share("https://www.baidu.com", AppUtils.getAppName(MainActivity.this), "欢迎使用", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595249183915&di=a3fdc0d55231e6c4223058f248735e3d&imgtype=0&src=http%3A%2F%2Ft8.baidu.com%2Fit%2Fu%3D1484500186%2C1503043093%26fm%3D79%26app%3D86%26f%3DJPEG%3Fw%3D1280%26h%3D853");
            }
        });

    }


    //TODO---------------------


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocation();
    }

    private void stopLocation() {
        // 停止定位
        mlocationClient.stopLocation();
    }

    private AMapLocationClient mlocationClient;
    // 要申请的权限
    private String[] permissions = {
            Manifest.permission.LOCATION_HARDWARE,//位置
            Manifest.permission.ACCESS_FINE_LOCATION,//位置
            Manifest.permission.ACCESS_COARSE_LOCATION,//位置
    };
    //权限数组下标
    //权限申请返回码
    private int requestCodePre = 321;
    //系统设置权限申请返回码
    private int requestCodeSer = 123;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    //TODO---------------------


    /*----------高德定位---------------*/

    @Override
    protected void onStart() {
        super.onStart();
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            mlocationClient.startLocation();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200://刚才的识别码
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//用户同意权限,执行我们的操作
                    mlocationClient.startLocation();
                } else {//用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                    Toast.makeText(MainActivity.this, "未开启定位权限,请手动到设置去开启权限", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表

                //hahahhahhah河南省郑州市金水区宏昌街29号靠近居然之家(商都店)
                Log.i(TAG, "onLocationChanged: hahahhahhah" + amapLocation.getAddress());

                String city = amapLocation.getCity();
                double latitude = amapLocation.getLatitude();
                double longitude = amapLocation.getLongitude();
                String address = amapLocation.getAddress();

                SPTool.addSessionMap(AppSP.sCity, amapLocation.getCity());
                SPTool.addSessionMap(AppSP.sStringJ, Double.toString(amapLocation.getLongitude()));
                SPTool.addSessionMap(AppSP.sStringW, Double.toString(amapLocation.getLatitude()));

                //郑州市----34.749566---113.734133---河南省郑州市金水区宏昌街29号靠近居然之家(商都店)
                Log.i(TAG, "onLocationChanged: " + city + "----" + latitude + "---" + longitude + "---" + address);


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:" + amapLocation.getErrorInfo());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 1) {
            if (null != data) {
                //省
                String provinceMe = data.getStringExtra("province");
                //市
                String cityMe = data.getStringExtra("city");
                //区
                String townMe = data.getStringExtra("town");
                //省市区
                String province_city_townMe = data.getStringExtra("province_city_town");


                //维度
                String latMe = data.getStringExtra("lat");
                //经度
                String lonMe = data.getStringExtra("lng");
                //街道
                String addressMe = data.getStringExtra("address");
                //tv1.setText(cityMe + addressMe);
                tv1.setText(provinceMe + "/" + cityMe + "/" + townMe);
                tv2.setText(addressMe);

                tv3.setText(provinceMe + "--" + cityMe + "--" + townMe + "--" + province_city_townMe + "--" + latMe + "--" + lonMe + addressMe);
                Log.i(TAG, "onActivityResult: " + provinceMe + "--" + cityMe + "--" + townMe + "--" + province_city_townMe + "--" + latMe + "--" + lonMe + addressMe);
                //河南省--郑州市--金水区--河南省郑州市金水区--34.762949--113.784294七里河南路与圃田西路交叉路口西南角
            }
        }

    }
}
