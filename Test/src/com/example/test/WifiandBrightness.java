package com.example.test;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WifiandBrightness {
	
	private static WifiManager wifimanager;
	private static String TAG="WifiandBrightness";
	
	public  WifiandBrightness(Context context){
		if(wifimanager==null){
			wifimanager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}
	}
	
	public static void openWifi(){
		if(!wifimanager.isWifiEnabled()){
			wifimanager.setWifiEnabled(true);
			Log.e(TAG, "openWifi...");
		}
	}
	public static void closeWifi(){
		if(wifimanager.isWifiEnabled()){
			wifimanager.setWifiEnabled(false);
			Log.e(TAG, "closeWifi...");
		}
	}
}
