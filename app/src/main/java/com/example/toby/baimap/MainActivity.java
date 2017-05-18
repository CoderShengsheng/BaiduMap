package com.example.toby.baimap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.example.toby.baimap.R.id.location;

public class MainActivity extends AppCompatActivity {
    MapView mMapView =null;
    BaiduMap mBaiduMap = null;
    //关于交互后台的
    //public static SelectActivity sel=
    private int num;
    public static Data count = null;
    public static String npark,str="001",str3;
    //定位
    private MyLocationListener mLocationListener;
    private LocationClient mLocationClient;
    private boolean isFirstIn=true;

    private Context context;
    MapStatusUpdate msu;
    //经纬度
    public  double mLatitude;
    public  double mLongtitude;
    //添加覆盖物
    private BitmapDescriptor mMarker;
    private RelativeLayout mMarkerLy;
    //调用外部导航
    private Button mButton,comment;
    //扫描


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //后台交互
        //find();
        //
        this.context=this;
        initMap();
        //definePoint();
        //定位
        initLocation();

        initMarker();
        //添加覆盖
        //find(str);
        addOverlays(Info.infos);
        //导航按钮
        mButton = (Button) findViewById(R.id.button_go);
        mButton.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                openBaiduMap(39.981567,116.431011,"我的位置","中日友好医院");
            }

        });
        comment= (Button) findViewById(R.id.selectCome);
        comment.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SelectComment(str);
                Intent intent =new Intent();
                intent.setClass(MainActivity.this,SelectActivity.class);
                intent.putExtra("str",str);
                MainActivity.this.startActivity(intent);
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                Bundle extraInfo = marker.getExtraInfo();
                Info info = (Info) extraInfo.getSerializable("info");
                ImageView iv = (ImageView) mMarkerLy
                        .findViewById(R.id.id_info_img);
                //TextView distance = (TextView) mMarkerLy
                  //      .findViewById(R.id.id_info_distance);
                TextView name = (TextView) mMarkerLy
                        .findViewById(R.id.id_info_name);
                TextView zan = (TextView) mMarkerLy
                        .findViewById(R.id.id_info_zan);
                iv.setImageResource(info.getImgId());
                //distance.setText(info.getDistance());
                name.setText(info.getName());
                //zan.setText(info.getZan());
                str=info.getUser();
                //count.setC("001");
                Log.e("parkid",str);
                find(str);
                //zan.setText(npark);
                //Toast.makeText(context,"剩余车位："+info.getUser(),
                        //location.getAddrStr(),
                        //Toast.LENGTH_LONG).show();

                mMarkerLy.setVisibility(View.VISIBLE);

                return true;
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener()
        {

            @Override
            public boolean onMapPoiClick(MapPoi arg0)
            {
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0)
            {
                mMarkerLy.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void initMarker() {
        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        mMarkerLy = (RelativeLayout) findViewById(R.id.id_maker_ly);
    }

    private void initLocation()
    {
       // mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mLocationClient=new LocationClient(this);
        mLocationListener  = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);


        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
//初始化图标
        //mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.arrow);
        //myOrientationListener = new MyOrientationListener(context);
/*
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener()
                {
                    @Override
                    public void onOrientationChanged(float x)
                    {
                        mCurrentX = x;
                    }
                });
                */
//设置定位点
        LatLng point2 = new LatLng(39.98181,116.426876);
        BitmapDescriptor bitloc=BitmapDescriptorFactory.fromResource(R.drawable.arrow);
        OverlayOptions option2 = new MarkerOptions().position(point2).icon(bitloc);
        mBaiduMap.addOverlay(option2);
//添加标记



    }




    private void initMap(){
        mMapView= (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //定义层级
        msu = MapStatusUpdateFactory.zoomTo(17.0f);
        mBaiduMap.setMapStatus(msu);
    }

    //我的位置的监听
    private class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData data = new MyLocationData.Builder()
                    //.direction(mCurrentX)//
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(data);

           //设置图标，经纬
           // MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode, true, mIconLocation );
            //mBaiduMap.setMyLocationConfiguration(config);

            mLatitude=39.98181;;//location.getLatitude();39.98181;
            mLongtitude=116.426876;//location.getLongitude();116.426876;
            if(isFirstIn) {
                LatLng latlng = new LatLng(mLatitude, mLongtitude);//经纬度
                msu = MapStatusUpdateFactory.newLatLng(latlng);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;
                Toast.makeText(context,"我的位置",
                        //location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }


    private void centerToMyLocation() {
        LatLng latLng = new LatLng(mLatitude,mLongtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }
    //添加覆盖物
    private void addOverlays(List<Info> infos)
    {
        //mBaiduMap.clear();
        LatLng latLng = null;
        Marker marker;
        OverlayOptions options;
        for (Info info : infos)
        {
            // æ≠Œ≥∂»
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            // Õº±Í
            options = new MarkerOptions().position(latLng).icon(mMarker)
                    .zIndex(5);
            marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle arg0 = new Bundle();
            arg0.putSerializable("info", info);
            marker.setExtraInfo(arg0);
        }

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);

    }

//百度地图调用
private void openBaiduMap(double lon, double lat, String title, String describle) {
    try {
        StringBuilder loc = new StringBuilder();
        loc.append("intent://map/direction?origin=latlng:");
        loc.append(lat);
        loc.append(",");
        loc.append(lon);
        loc.append("|name:");
        loc.append("我的位置");
        loc.append("&destination=latlng:");
        loc.append(lat);
        loc.append(",");
        loc.append(lon);
        loc.append("|name:");
        loc.append(describle);
        loc.append("&mode=driving");
        loc.append("&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
        Intent intent = Intent.getIntent(loc.toString());
        if (isInstallPackage("com.baidu.BaiduMap")) {
            startActivity(intent); //启动调用
            Log.e("GasStation", "百度地图客户端已经安装");
        } else {
            //Log.e("GasStation", "没有安装百度地图客户端");
            Toast.makeText(context,"没有安装百度地图客户端",
                    //location.getAddrStr(),
                    Toast.LENGTH_SHORT).show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private boolean isInstallPackage(String s) {
        return new File("/data/data/" + s).exists();
    }


    //生命周期管理及菜单管理
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        //方向传感器
        //myOrientationListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        //关闭方向传感器
        //myOrientationListener.stop();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id){
            case R.id.map_location:
                centerToMyLocation();
                break;
            case R.id.car_start:
                //扫描二维码
                //customScan();
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,ParkActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case  R.id.exit:
                jumpTo(LoginActivity.class);

        }

        return super.onOptionsItemSelected(item);
    }

    private void jumpTo(Class<LoginActivity> m) {
        Intent intent =new Intent();
        intent.setClass(MainActivity.this,m);
        MainActivity.this.startActivity(intent);
    }

    //链接后台，获取停车位
    private void find(final String parkid) {
        RequestQueue requestQueue1 = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "http://10.0.2.2/Project/ParkCount.php", listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                //map.put("username","2013014110");
                map.put("parkid", parkid);
                return map;
            }
        };
        requestQueue1.add(stringRequest1);
        //return npark;

    }
    Response.Listener<String> listener = new Response.Listener<String>(){
        @Override
        public void onResponse(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                num = jsonObject.getInt("success");
                 str3=jsonObject.getString("success2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String str = num+" ";
            Log.e("we",str);
            //npark=str;
            TextView text= (TextView) findViewById(R.id.id_info_zan);
            text.setText(str);
            String str2="停车单价："+str3+"/h";
            TextView text2= (TextView) findViewById(R.id.id_info_distance);
            text2.setText(str2);

            //Toast.makeText(context,"车位余量："+str,Toast.LENGTH_SHORT).show();
            //count = (Data) getApplication();
            //count.setD(str);


        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("error", volleyError.getMessage(), volleyError);
        }
    };

}

