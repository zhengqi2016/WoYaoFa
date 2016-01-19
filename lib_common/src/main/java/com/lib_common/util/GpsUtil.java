package com.lib_common.util;

import android.content.Context;
import android.location.LocationManager;

public class GpsUtil {

	// Gps是否可用  
    public static boolean isGpsEnable(Context context) {  
        LocationManager locationManager =   
                ((LocationManager) context.getSystemService(context.LOCATION_SERVICE));  
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
    }  
}
