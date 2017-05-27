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
	// ��¼С�������Ŀ��
	static int viewWidth;
	// ��¼С�������ĸ߶�
	static int viewHeight;
	// ��¼ϵͳ״̬���ĸ߶�
	private static int statusBarHeight;
	// С�������Ĳ���
	private WindowManager.LayoutParams mParams;
	// ��¼��ǰ��ָλ������Ļ�ϵĺ�����ֵ
	private float xInScreen;
	// ��¼��ǰ��ָλ������Ļ�ϵ�������ֵ
	private float yInScreen;
	// ��¼��ָ����ʱ����Ļ�ϵĺ������ֵ
	private float xDownInScreen;
	// ��¼��ָ����ʱ����Ļ�ϵ��������ֵ
	private float yDownInScreen;
	// ��¼��ָ����ʱ��С��������View�ϵĺ������ֵ
	private float xInView;
	// ��¼��ָ����ʱ��С��������View�ϵ��������ֵ
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
		// ��ָ����ʱ��¼��Ҫ����,�������ֵ����Ҫ��ȥ״̬���߶�
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

	// ��С�������Ĳ������룬���ڸ���С��������λ�á�
	// С�������Ĳ���
	void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	// �򿪴���������ͬʱ�ر�С��������
	private void openBigWindow() {
		// TODO Auto-generated method stub
		MyWindowManager.createBigWindow(getContext());
		MyWindowManager.removeSamllwindow(getContext());
		Log.e(TAG, "openBigWindow successfully");
	}

	// ����С����������Ļ�е�λ�á�
	private void updateViewPosition() {
		// TODO Auto-generated method stub
		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
		Log.e(TAG, "updateViewPosition successfully");
	}

	// ���ڻ�ȡ״̬���ĸ߶ȡ�
	// @return ����״̬���߶ȵ�����ֵ��
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
		Log.e(TAG, "״̬��-����2:" + statusBarHeight);
		return statusBarHeight;
	}
}
