package com.example.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class FloatWindowService extends Service {
	private Handler handler = new Handler();// �������߳��д������Ƴ���������
	private Timer timer; // ��ʱ������ʱ���м�⵱ǰӦ�ô��������Ƴ���������
	private String TAG="FloatWindowService";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// ������ʱ����ÿ��0.5��ˢ��һ��
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
			Log.e(TAG, "start timer task...");
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// Service����ֹ��ͬʱҲֹͣ��ʱ����������
		timer.cancel();
		timer = null;
		Log.e(TAG, "stop timer task...");
		super.onDestroy();
	}

	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			// ��ǰ���������棬��û����������ʾ���򴴽���������
			if (isHome() && !MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.e(TAG, "createSmallWindow...");
						MyWindowManager
								.createSmallWindow(getApplicationContext());
					}
				});
			}// ��ǰ���治�����棬������������ʾ�����Ƴ���������
			else if (!isHome() && MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.e(TAG, "removeSamllwindow && removeBigwindow...");
						MyWindowManager
								.removeSamllwindow(getApplicationContext());
						MyWindowManager
								.removeBigwindow(getApplicationContext());
					}
				});
			}// ��ǰ���������棬������������ʾ��������ڴ����ݡ�
			else if (isHome() && MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.e(TAG, "updateUsedPercent...");
						MyWindowManager
								.updateUsedPercent(getApplicationContext());
					}
				});
			}
		}

		private boolean isHome() {
			// TODO Auto-generated method stub
			ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
			List<String> strs = getHomes();
			if (strs != null && strs.size() > 0) {
				return strs.contains(rti.get(0).topActivity.getPackageName());
			} else {
				return false;
			}

		}

		private List<String> getHomes() {
			// TODO Auto-generated method stub
			List<String> names = new ArrayList<String>();
			PackageManager packageManager = FloatWindowService.this.getPackageManager();
			Intent intent= new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			List<ResolveInfo> resolveinfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			for(ResolveInfo ri : resolveinfo){
				names.add(ri.activityInfo.packageName);
			}
			return names;
		}
	}
}
