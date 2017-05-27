package com.example.test;

import java.lang.reflect.Field;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatWindowBigView extends LinearLayout {
	// ��¼���������Ŀ��
	static int viewWidth;
	// ��¼���������ĸ߶�
	static int viewHeight;
	private String TAG = "FloatWindowBigView";
	private static WifiManager wifiManager;

	public FloatWindowBigView(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
		View view = findViewById(R.id.big_window_layout);
		viewHeight = view.getLayoutParams().height;
		viewWidth = view.getLayoutParams().width;
		Log.e(TAG, "big view###viewWidth= " + viewWidth + "\n" + "viewHeight= "
				+ viewHeight);

		final Button meminfobtn = (Button) findViewById(R.id.big_percent);
		meminfobtn.setText("ռ���ڴ棺"
				+ MyWindowManager.getUsedPercentValue(context));
		meminfobtn.getBackground().setAlpha(0);
		Button clean = (Button) findViewById(R.id.clean);
		Button more = (Button) findViewById(R.id.more);
		clean.getBackground().setAlpha(0);
		more.getBackground().setAlpha(0);
		clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActivityManager activityManager = (ActivityManager) context
						.getSystemService(Context.ACTIVITY_SERVICE);
				List<RunningAppProcessInfo> infolist = activityManager
						.getRunningAppProcesses();
				List<ActivityManager.RunningServiceInfo> serviceInfos = activityManager
						.getRunningServices(100);
				long beforMem = (long) getAvailMemory(context);
				int count = 0;
				if (infolist != null) {
					for (int i = 0; i < infolist.size(); ++i) {
						RunningAppProcessInfo appProcessInfo = infolist.get(i);
						Log.d(TAG, "process name : "
								+ appProcessInfo.processName);
						// importance �ý��̵���Ҫ�̶� ��Ϊ����������ֵԽ�;�Խ��Ҫ��
						Log.d(TAG, "importance : " + appProcessInfo.importance);
						Log.d(TAG, "IMPORTANCE_VISIBLE : "
								+ appProcessInfo.IMPORTANCE_VISIBLE);
						if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {

							String[] pkgList = appProcessInfo.pkgList;
							for (int j = 0; j < pkgList.length; ++j) {
								if (!pkgList[j].contains("com.example.test")) {
									Log.d(TAG,
											"It will be killed, package name : "
													+ pkgList[j]);
									activityManager
											.killBackgroundProcesses(pkgList[j]);
									count++;
									Log.e(TAG, "count= " + count);

								} else {
									Log.e(TAG, "process name is yourself ");
								}
							}
						}
					}
				}
				long afterMem = (long) getAvailMemory(context);
				Log.e(TAG, "beforMem= : " + beforMem + "\n" + "afterMem =: "
						+ afterMem);
				if (afterMem > beforMem) {
					Toast.makeText(
							getContext(),
							"���ϣ�������" + (afterMem - beforMem)
									+ "M�ڴ棬�ռ䶼��������!!!", Toast.LENGTH_SHORT)
							.show();
					meminfobtn.setText("ռ���ڴ棺"
							+ MyWindowManager.getUsedPercentValue(context));
				} else {
					Toast.makeText(getContext(), "���ϣ�������0M�ڴ棬�ռ䶼��������!!!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub	
				TextView tx=new TextView(getContext());
				tx.setText("��ѡ��ȷ��");
				tx.setTextSize(20);
				tx.setGravity(Gravity.CENTER);
				tx.setPadding(10, 10, 10, 10);
				tx.setTextColor(Color.parseColor("#0000CD"));
				AlertDialog.Builder singleBuilder = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
				singleBuilder.setCustomTitle(tx);
				singleBuilder.setItems(R.array.settings, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String[] itemArray=getResources().getStringArray(R.array.settings);
						String selectItem = itemArray[which].toString();
						Toast.makeText(getContext(), "��ѡ����'"+selectItem+"'", Toast.LENGTH_SHORT).show();
						switch (which) {
						case 0://����WiFi����
							wifiManager=(WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);//���ϵͳ���񣬲���AndroidManifest���Ȩ��
							if(!wifiManager.isWifiEnabled()){
								wifiManager.setWifiEnabled(true); //��WiFi
								Log.e(TAG, "you open wifi...");
							}else{
								wifiManager.setWifiEnabled(false); //�ر�WiFi
								Log.e(TAG, "you close wifi...");
							}
							Log.e(TAG, "you choose0 "+selectItem);
							break;
						case 1://��������
							Log.e(TAG, "you choose1 "+selectItem);
							setHideDialog(dialog, false);
							break;
						case 2://��С������
							MyWindowManager.removeBigwindow(getContext());
							MyWindowManager.createSmallWindow(getContext());
							Log.e(TAG, "you choose2 "+selectItem);
							break;
						case 3://�رշ�������������
							MyWindowManager.removeBigwindow(getContext());
							MyWindowManager.removeSamllwindow(getContext());
							Intent intent=new Intent(getContext(), FloatWindowService.class);
							getContext().stopService(intent);
							Log.e(TAG, "you choose3 "+selectItem);
							break;
						default:
							break;
						}
					}
				});
				AlertDialog dialog =singleBuilder.create();
				dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				dialog.show();
				WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
				params.width=500;
				params.height=350;
				dialog.getWindow().setAttributes(params);
			}
		});

	}
	//����dialogĬ���ǻ�dismiss���ģ������ڸ�ѡ��˵��ֻ�ô��û�ɸѡ����ѡ����ȷ�Ϻ�ſ���ʧ���������÷���ǿ�����䲻��ʧ��
	private static void setHideDialog(DialogInterface dialog,Boolean swit){
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			try {
				field.set(dialog, swit);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	/*
	 * MyWindowManager.removeBigwindow(context);
	 * MyWindowManager.removeSamllwindow(context); Intent intent = new
	 * Intent(getContext(),FloatWindowService.class);
	 * context.stopService(intent); }
	 */
	protected float getAvailMemory(Context context) {
		// TODO Auto-generated method stub
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		activityManager.getMemoryInfo(mi);
		Log.d(TAG, "�����ڴ�---->>>" + mi.availMem / (1024 * 1024));
		return (float) mi.availMem / (1024 * 1024);
	}

}
