package com.woyaofa.util;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.lib_common.util.ToastUtil;

/**
 * Created by LoaR on 15/11/5.
 */
public class LocationUtil {

    private static LocationUtil locationUtil;
    private static AMapLocationClient mlocationClient;
    private static AMapLocationClientOption mLocationOption;

    public static LocationUtil getInstance() {
        if (locationUtil == null) {
            locationUtil = new LocationUtil();
        }
        return locationUtil;
    }

    //通过地址获取经纬度
    public void getLatLngGeo(Context context, String address, String city, GeocodeSearch.OnGeocodeSearchListener geoSearchListener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(geoSearchListener);
        GeocodeQuery query = new GeocodeQuery(address, city);
//        GeocodeQuery query = new GeocodeQuery("东城区大兴胡同18号","北京");
        geocoderSearch.getFromLocationNameAsyn(query);
    }    //通过地址获取经纬度

    public void getLatLngGeo(String address, String city, OnGetGeoCoderResultListener listener) {
        GeoCoder mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(listener);
        mSearch.geocode(new GeoCodeOption().city(city).address(address));
    }

    /**
     * 通过经纬度获取地址
     *
     * @param context
     * @param lat
     * @param lng
     * @param geoSearchListener
     */
    public void getAddressGeo(Context context, double lat, double lng, GeocodeSearch.OnGeocodeSearchListener geoSearchListener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(geoSearchListener);
        //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat, lng), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * 定位工具
     *
     * @param context
     * @param locationListener 定位回调
     * @return
     */
    public AMapLocationClient getLocationClient(Context context, AMapLocationListener locationListener) {
        if (mLocationOption == null) {
            //声明mLocationOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            mLocationOption.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //设置定位间隔,单位毫秒,默认为5000ms
            mLocationOption.setInterval(1000);
        }
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(context.getApplicationContext());
            //设置定位回调监听
            mlocationClient.setLocationListener(locationListener);
        }
        //给定位客户端对象设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        return mlocationClient;
    }

    public void startLocation(Context context, AMapLocationListener locationListener) {
        if (getLocationClient(context, locationListener) != null) {
            mlocationClient.startLocation();
        }
    }

    public void stopLocation() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient = null;
        }
        if (mLocationOption != null) {
            mLocationOption = null;
        }
    }

    //百度坐标转火星坐标
    public LatLng bdToGcj(LatLng sourceLatLng){
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.BAIDU);
        converter.coord(sourceLatLng);
        return converter.convert();
    }

}
