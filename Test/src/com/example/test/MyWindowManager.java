package com.example.test;

import java.io.BufferedReader;
import java.io.FileReader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class MyWindowManager {
	// 小悬浮窗View的实例
	private static FloatWindowSmallView smallWindow;
	// 大悬浮窗View的实例
	private static FloatWindowBigView bigWindow;
	// 小悬浮窗view的参数
	private static LayoutParams smallwindowParams;
	// 大悬浮窗view的参数
	private static LayoutParams bigWindowParams;
	// 用于控制在屏幕上添加或移除悬浮窗
	private static WindowManager mWindowManager;
	// 用于获取手机可用内存
	private static ActivityManager mActivityManager;
	private static String TAG = "MyWindowManager";

	// 判断是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
	public static boolean isWindowShowing() {
		// TODO Auto-generated method stub
		return smallWindow != null || bigWindow != null;
	}

	// 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
	public static void createSmallWindow(Context context) {
		// TODO Auto-generated method stub
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeiht = windowManager.getDefaultDisplay().getHeight();
		if (smallWindow == null) {
			smallWindow = new FloatWindowSmallView(context);
			if (smallwindowParams == null) {
				Log.e(TAG, "smallwindowParams ==null");
				smallwindowParams = new LayoutParams();
				smallwindowParams.type = LayoutParams.TYPE_PHONE;
				smallwindowParams.format = PixelFormat.RGBA_8888;
				smallwindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				smallwindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallwindowParams.width = FloatWindowSmallView.viewWidth;
				smallwindowParams.height = FloatWindowSmallView.viewHeight;
				smallwindowParams.x = screenWidth;
				smallwindowParams.y = screenHeiht / 2;
				Log.e(TAG, "smallwindowParams.type= " + LayoutParams.TYPE_PHONE
						+ "\n" + "smallwindowParams.format= "
						+ PixelFormat.RGBA_8888 + "\n"
						+ "smallwindowParams.width= "
						+ FloatWindowSmallView.viewWidth + "\n"
						+ "smallwindowParams.height= "
						+ FloatWindowSmallView.viewHeight + "\n");
			}
			smallWindow.setParams(smallwindowParams);
			windowManager.addView(smallWindow, smallwindowParams);
			Log.e(TAG, "createSmallWindow successfully");
		}

	}

	public static void createBigWindow(Context context) {
		// TODO Auto-generated method stub
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeiht = windowManager.getDefaultDisplay().getHeight();
		if (bigWindow == null) {
			bigWindow = new FloatWindowBigView(context);
			if (bigWindowParams == null) {
				Log.e(TAG, "bigWindowParams ==null");
				bigWindowParams = new LayoutParams();
				bigWindowParams.x = screenWidth / 2
						- FloatWindowBigView.viewWidth / 2;
				bigWindowParams.y = screenHeiht / 2
						- FloatWindowBigView.viewHeight / 2;
				bigWindowParams.type = LayoutParams.TYPE_PHONE;
				bigWindowParams.format = PixelFormat.RGBA_8888;
				bigWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				bigWindowParams.width = FloatWindowBigView.viewWidth;
				bigWindowParams.height = FloatWindowBigView.viewHeight;
				Log.e(TAG, "bigWindowParams.type= " + LayoutParams.TYPE_PHONE
						+ "\n" + "bigWindowParams.format= "
						+ PixelFormat.RGBA_8888 + "\n"
						+ "bigWindowParams.width= "
						+ FloatWindowSmallView.viewWidth + "\n"
						+ "bigWindowParams.height= "
						+ FloatWindowSmallView.viewHeight + "\n");
			}
			windowManager.addView(bigWindow, bigWindowParams);
			Log.e(TAG, "createBigWindow successfully");
		}
	}

	// 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
	// 必须为应用程序的Context.
	// @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
	private static WindowManager getWindowManager(Context context) {
		// TODO Auto-generated method stub
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

	// 将小悬浮窗从屏幕上移除。 必须为应用程序的Context.
	public static void removeSamllwindow(Context context) {
		// TODO Auto-generated method stub
		if (smallWindow != null) {
			WindowManager windowmanager = getWindowManager(context);
			windowmanager.removeView(smallWindow);
			smallWindow = null;
			Log.e(TAG, "removeSamllwindow successfully");
		}
	}

	// 将大悬浮窗从屏幕上移除。 必须为应用程序的Context.
	public static void removeBigwindow(Context context) {
		// TODO Auto-generated method stub
		if (bigWindow != null) {
			WindowManager windowmanager = getWindowManager(context);
			windowmanager.removeView(bigWindow);
			bigWindow = null;
			Log.e(TAG, "removeBigwindow successfully");
		}
	}

	public static void updateUsedPercent(Context context) {
		// TODO Auto-generated method stub
		if (smallWindow != null) {
			TextView preTextView = (TextView) smallWindow
					.findViewById(R.id.percent);
			preTextView.setText(getUsedPercentValue(context));
			Log.e(TAG, "updateUsedPercent= " + getUsedPercentValue(context));
		}
	}

	public static String getUsedPercentValue(Context context) {
		// 内存信息文件（CPU信息文件：/proc/cpuinfo）
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir); // 创建读取字符流缓存区
			BufferedReader br = new BufferedReader(fr, 2048);
			String meminfoLine = br.readLine();
			String subMeminfo = meminfoLine.substring(meminfoLine
					.indexOf("MemTotal:"));
			Log.e(TAG, "subMeminfo=" + subMeminfo);
			br.close();
			long totalMenorysize = Integer.parseInt(subMeminfo.replaceAll(
					"\\D+", ""));
			long availablesize = getAvailableMemory(context) / 1024;
			int percent = (int) ((totalMenorysize - availablesize)
					/ (float) totalMenorysize * 100);
			Log.e(TAG, "totalMenorysize,availablesize,percent="
					+ totalMenorysize + "/" + availablesize + "/" + percent);
			return percent + "%";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "悬浮窗";
	}

	private static int getAvailableMemory(Context context) {
		// TODO Auto-generated method stub
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(mi);
		return (int) mi.availMem;
	}

	private static ActivityManager getActivityManager(Context context) {
		// TODO Auto-generated method stub
		if (mActivityManager == null) {

			mActivityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);

		}
		return mActivityManager;
	}

}
