package com.example.test;

import java.lang.reflect.Field;

import android.R.bool;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatWindowSmallView extends LinearLayout {

	private WindowManager windowManager;
	// 记录小悬浮窗的宽度
	static int viewWidth;
	// 记录小悬浮窗的高度
	static int viewHeight;
	// 记录系统状态栏的高度
	private static int statusBarHeight;
	// 小悬浮窗的参数
	private WindowManager.LayoutParams mParams;
	// 记录当前手指位置在屏幕上的横坐标值
	private float xInScreen;
	// 记录当前手指位置在屏幕上的纵坐标值
	private float yInScreen;
	// 记录手指按下时在屏幕上的横坐标的值
	private float xDownInScreen;
	// 记录手指按下时在屏幕上的纵坐标的值
	private float yDownInScreen;
	// 记录手指按下时在小悬浮窗的View上的横坐标的值
	private float xInView;
	// 记录手指按下时在小悬浮窗的View上的纵坐标的值
	private float yInView;
	private String TAG = "FloatWindowSmallView";


	public FloatWindowSmallView(Context context) {
		super(context);
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		View view = findViewById(R.id.small_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		Log.e(TAG, "Small view###viewWidth= " + viewWidth + "\n"
				+ "viewHight= " + viewHeight);
		TextView percentView = (TextView) findViewById(R.id.percent);
		percentView.getBackground().setAlpha(200);
		percentView.setText(MyWindowManager.getUsedPercentValue(context));
		Log.e(TAG,
				"percentView= " + MyWindowManager.getUsedPercentValue(context));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
		case MotionEvent.ACTION_DOWN:
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			Log.e(TAG, "xInView,yInView=(" + xInView + "," + yInView + ")"
					+ "\n" + "xDownInScreen,yDownInScreen=(" + xDownInScreen
					+ "," + yDownInScreen + ")" + "\n"
					+ "xInScreen,yInScreen=(" + xInScreen + "," + yInScreen
					+ ")");
			// postCheckForLongClick();
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			updateViewPosition();
			Log.e(TAG, "updateViewPosition");
			break;
		case MotionEvent.ACTION_UP:
			if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
				openBigWindow();
				Log.e(TAG, "openBigWindow");
			}

			break;
		default:
			break;
		}
		return true;
	}

	// 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	// 小悬浮窗的参数
	void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	// 打开大悬浮窗，同时关闭小悬浮窗。
	private void openBigWindow() {
		// TODO Auto-generated method stub
		MyWindowManager.createBigWindow(getContext());
		MyWindowManager.removeSamllwindow(getContext());
		Log.e(TAG, "openBigWindow successfully");
	}

	// 更新小悬浮窗在屏幕中的位置。
	private void updateViewPosition() {
		// TODO Auto-generated method stub
		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
		Log.e(TAG, "updateViewPosition successfully");
	}

	// 用于获取状态栏的高度。
	// @return 返回状态栏高度的像素值。
	private float getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		Log.e(TAG, "状态栏-方法2:" + statusBarHeight);
		return statusBarHeight;
	}
}
