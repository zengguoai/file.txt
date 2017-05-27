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
	// С������View��ʵ��
	private static FloatWindowSmallView smallWindow;
	// ��������View��ʵ��
	private static FloatWindowBigView bigWindow;
	// С������view�Ĳ���
	private static LayoutParams smallwindowParams;
	// ��������view�Ĳ���
	private static LayoutParams bigWindowParams;
	// ���ڿ�������Ļ����ӻ��Ƴ�������
	private static WindowManager mWindowManager;
	// ���ڻ�ȡ�ֻ������ڴ�
	private static ActivityManager mActivityManager;
	private static String TAG = "MyWindowManager";

	// �ж��Ƿ���������(����С�������ʹ�������)��ʾ����Ļ�ϡ�
	public static boolean isWindowShowing() {
		// TODO Auto-generated method stub
		return smallWindow != null || bigWindow != null;
	}

	// ����һ��С����������ʼλ��Ϊ��Ļ���Ҳ��м�λ�á�
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

	// ���WindowManager��δ�������򴴽�һ���µ�WindowManager���ء����򷵻ص�ǰ�Ѵ�����WindowManager��
	// ����ΪӦ�ó����Context.
	// @return WindowManager��ʵ�������ڿ�������Ļ����ӻ��Ƴ���������
	private static WindowManager getWindowManager(Context context) {
		// TODO Auto-generated method stub
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

	// ��С����������Ļ���Ƴ��� ����ΪӦ�ó����Context.
	public static void removeSamllwindow(Context context) {
		// TODO Auto-generated method stub
		if (smallWindow != null) {
			WindowManager windowmanager = getWindowManager(context);
			windowmanager.removeView(smallWindow);
			smallWindow = null;
			Log.e(TAG, "removeSamllwindow successfully");
		}
	}

	// ��������������Ļ���Ƴ��� ����ΪӦ�ó����Context.
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
		// �ڴ���Ϣ�ļ���CPU��Ϣ�ļ���/proc/cpuinfo��
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir); // ������ȡ�ַ���������
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
		return "������";
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
