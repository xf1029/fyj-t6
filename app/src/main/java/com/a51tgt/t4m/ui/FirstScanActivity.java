package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.service.MainService;
import com.a51tgt.t4m.bean.DeviceInfo;
import com.a51tgt.t4m.bean.DeviceInfoForUserData;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.bean.UserDataUtils;
import com.a51tgt.t4m.bluetooth.BluetoothUtil;
import com.a51tgt.t4m.bluetooth.ClsUtils;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.service.MainService;
import com.a51tgt.t4m.ui.view.DeviceInfoView;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.NetWorkUtils;
import com.a51tgt.t4m.utils.TipUtil;

import com.google.gson.internal.LinkedTreeMap;
import com.znq.zbarcode.CaptureActivity;
import java.util.ArrayList;
import java.util.Map;


public class FirstScanActivity extends com.a51tgt.t4m.ui.BaseActivity {

	private String deviceMacAddress = "",alertInfo="";
	private String deviceSSID = "";
	private Context mContext;
	private ScrollView sv_device, sv_introduce;
	private LinearLayout ll_device;
	private TextView tv_introduce, tv_menu;
	private boolean isEdit = false;
	private WifiManager wifiManager;

	private boolean quit = false;
	private com.a51tgt.t4m.ui.DeviceSetDialog setDialog = new com.a51tgt.t4m.ui.DeviceSetDialog();

	//接收广播的处理
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{


		FirstScanActivity.this.recreate();

		}
	};

	@Override
	protected void onDestroy() {

		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@SuppressLint("InflateParams") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
//
		PackageManager packageManager =  getPackageManager();
		PackageInfo packInfo = null;
		try {
			String str =   this.getPackageName();

			packInfo = packageManager.getPackageInfo(str,0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		params[0] = new OkHttpClientManager.Param("ver","fyj_"+version);
		Log.i("xiazaidizhi","====="+"fyj_"+version);

//		new SendRequest(APIConstants.Get_FYJApp_Info, params, new MyHandler(), 1);

		//注册接受的广播
		IntentFilter filter = new IntentFilter(APIConstants.BR_LAN_STATUS);
		registerReceiver(broadcastReceiver, filter);







		setContentView(R.layout.activity_first_scan);

//		CommUtil.setStatusBarBackgroundColor(FirstScanActivity.this);
		mContext = FirstScanActivity.this;
		sv_device = (ScrollView) findViewById(R.id.sv_device);
		sv_introduce = (ScrollView) findViewById(R.id.sv_introduce);
		ll_device = (LinearLayout) findViewById(R.id.ll_device);
//		tv_introduce = (TextView) findViewById(R.id.tv_introduce);
		tv_menu = (TextView) findViewById(R.id.tv_menu);

		findViewById(R.id.connect_Scan).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int net_type = NetWorkUtils.getAPNType(mContext);
				if(net_type == 0 || net_type == 2){
					Toast.makeText(mContext, R.string.error_network_is_not_available, Toast.LENGTH_LONG).show();
				}
				else{

					Intent intent = new Intent(mContext, CaptureActivity.class);
					startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);
				}
			}
		});
		findViewById(R.id.connect_Text).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int net_type = NetWorkUtils.getAPNType(mContext);
				if(net_type == 0 || net_type == 2){
					Toast.makeText(mContext, R.string.error_network_is_not_available, Toast.LENGTH_LONG).show();
				}
				else{

					final EditText inputServer = new EditText(FirstScanActivity.this);
					AlertDialog.Builder builder = new AlertDialog.Builder(FirstScanActivity.this);
					builder.setTitle(getResources().getString(R.string.tip_input_device_num)).setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
							.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					builder.setPositiveButton(R.string.button_commit, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							String text = inputServer.getText().toString();
							parameterValidation(text);


						}

						});
                builder.show();


				//zheli

				}


			}
		});
		tv_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(isEdit == false){
					isEdit = true;
					tv_menu.setText(R.string.tip_device_list_menu2);
					for(int i = 0; i < ll_device.getChildCount(); i++){
						DeviceInfoView deviceInfoView = (DeviceInfoView)ll_device.getChildAt(i);
						deviceInfoView.openMenu();
					}
				}
				else {
					isEdit = false;
					tv_menu.setText(R.string.tip_device_list_menu);
					for(int i = 0; i < ll_device.getChildCount(); i++){
						DeviceInfoView deviceInfoView = (DeviceInfoView)ll_device.getChildAt(i);
						deviceInfoView.closeMenu();
					}
				}
			}
		});

		getDeviceList();

		MZApplication.getInstance().addActivity(this);
	}
	private void parameterValidation(String ssid) {

		if (!ssid.contains("TGT")&&!ssid.contains("GTWiFi")) {
			Toast.makeText(FirstScanActivity.this, getResources().getString(R.string.tip_input_corret_device_num), Toast.LENGTH_LONG).show();

			return;
		}

		deviceSSID = ssid;
		String temp;
		if (deviceSSID.contains("GTWiFi")){

			temp = deviceSSID.replace("GTWiFi","TGT");
//			deviceSSID = deviceSSID

		}else{

			temp = deviceSSID;
			deviceSSID = deviceSSID.replace("TGT","GTWiFi");
		}





		APIConstants.deviceInfo = new DeviceInfo(deviceSSID);
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
		params[0] = new OkHttpClientManager.Param("ssid", temp);
		alertInfo = getResources().getString(R.string.tip_can_not_find_the_device);
		new SendRequest(APIConstants.Get_Device_Status, params, new MyHandler(), 1);
//
//				Log.i("dessvicessid:",deviceSSID);
//							UserDataUtils.getInstance(mContext).setDeviceList(deviceSSID, deviceMacAddress);
////
//							getDeviceList();
//		alertInfo ="Cannot detect device info, please try another device";
//
//		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
//		params[0] = new OkHttpClientManager.Param("ssid", deviceSSID);
//		new SendRequest(APIConstants.Get_Device_Status, params, new MyHandler(), 1);

	}
	private void operation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("shoudong")
				.setPositiveButton(R.string.tip_connect2, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//集合里的删除点击的条目
						Intent intent = new Intent(mContext, CaptureActivity.class);
						startActivityForResult(intent, APIConstants.SCANNIN_GREQUEST_CODE);

					}
				})
				.setNegativeButton(R.string.tip_device_info_menu, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {


						final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
						isExit.setTitle(R.string.tip_title);
						isExit.setMessage(getResources().getString(R.string.tip_unbind_the_device));


					}}).show();


	}
	private void startMainservice(){
		Intent intent = new Intent(this, MainService.class);
		startService(intent);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				MainService.getInstance().startHeartBeat();


				try {
//					MainService.getInstance().startHeartBeat();

				}
				catch (Exception e){

					Log.i("exception",e.toString());
				}

			}
		},500);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void showNotFindDialog(String string)
	{
		final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
		isExit.setTitle(R.string.tip_title);
		isExit.setMessage(string);
		isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.
				commit_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				isExit.dismiss();
			}
		});
		isExit.show();
	}

	@Override
	public void onBackPressed() {
		if (!quit) {
			quit = true;
			Toast.makeText(this, R.string.toast_back_exit, Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					quit = false;
				}
			}, 2000);
		} else {
			MZApplication.getInstance().exit();
		}
	}

	private void getDeviceList(){
		ArrayList<DeviceInfoForUserData> devices = UserDataUtils.getInstance(mContext).getDeviceList();
		if(devices != null && devices.size() > 0){
			sv_introduce.setVisibility(View.GONE);
			sv_device.setVisibility(View.VISIBLE);
			ll_device.removeAllViews();
			for(int i = 0; i < devices.size(); i++){
				final DeviceInfoForUserData device = devices.get(i);
				final DeviceInfoView deviceInfoView = new DeviceInfoView(mContext, device.Ssid, device.MacAddress);
				ll_device.addView(deviceInfoView);
				deviceInfoView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						duihukuan(device,deviceInfoView);


						/*wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
						if(!wifiManager.isWifiEnabled())
							wifiManager.setWifiEnabled(true);
						if(isEdit == true){
							final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
							isExit.setTitle(R.string.tip_title);
							isExit.setMessage(getResources().getString(R.string.tip_unbind_the_device));
							isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									isExit.dismiss();
								}
							});
							isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.commit_button), new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
//									BluetoothDevice device = BluetoothUtil.getInstance().getBTDevice(deviceInfoView.getMacAddress());
//									try {
//										boolean res = ClsUtils.removeBond(device.getClass(), device);
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
									UserDataUtils.getInstance(mContext).removeFromDeviceList(deviceInfoView.getSsid(), deviceInfoView.getMacAddress());
									isExit.dismiss();
									getDeviceList();

								}
							});

							isExit.show();
						}
						else{
							APIConstants.deviceInfo = new DeviceInfo(device.Ssid);
							Intent intent = new Intent(mContext, com.a51tgt.t4m.ui.MainActivity.class);
							startActivity(intent);
						}*/
					}
				});
			}
			isEdit = true;
//			tv_menu.setVisibility(View.VISIBLE);
			tv_menu.performClick();
		}
		else{
//			tv_menu.setVisibility(View.GONE);
			sv_introduce.setVisibility(View.VISIBLE);
			sv_device.setVisibility(View.GONE);
		}
	}
	private void duihukuan(final DeviceInfoForUserData device, final DeviceInfoView deviceInfoView) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.tip_device_operation_title)
				.setPositiveButton(R.string.tip_connect2, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//集合里的删除点击的条目
						APIConstants.deviceInfo = new DeviceInfo(device.Ssid);
						Intent intent = new Intent(mContext, MainActivity.class);
						startActivity(intent);

					}
				})
				.setNegativeButton(R.string.tip_device_info_menu, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {


						final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
						isExit.setTitle(R.string.tip_title);
						isExit.setMessage(getResources().getString(R.string.tip_unbind_the_device));
						isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								isExit.dismiss();
							}
						});
						isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.button_commit), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								UserDataUtils.getInstance(mContext).removeFromDeviceList(deviceInfoView.getSsid(), deviceInfoView.getMacAddress());
								isExit.dismiss();
								getDeviceList();

							}
						});

						isExit.show();
					}
				}).show();


	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case APIConstants.SCANNIN_GREQUEST_CODE:
				if (resultCode == RESULT_OK) {
					String result = data.getStringExtra(CaptureActivity.EXTRA_STRING);
					Log.i("wufaeegegeg",result);

					if (!TextUtils.isEmpty(result)) {
						//http://aifi.51tgt.com:9988/downloadapp.html?sn=TGT24170833157&wmac=A4D8CA7B7F88&bmac=4045DA963CB8&imei1=864772030440548&imei2=864772030440549
						//http://aifi.51tgt.com:9988/downloadapp.html?sn=TGT24170833232&wmac=A4D8CA7B7F88&bmac=4045DA7CAEE5&imei1=864772030440548&imei2=864772030440549
						Map<String, String> maps = CommUtil.URLRequest(result);
						if (result != null  && result.contains("sn")) {
							String temp = maps.get("sn");
//							if (temp.length() == 12 || temp.length() == 17) {
//								if (temp.length() == 12) {
//
//									deviceMacAddress = temp.substring(0, 2) + ":" + temp.substring(2, 4) + ":" + temp.substring(4, 6) + ":" + temp.substring(6, 8) + ":" + temp.substring(8, 10) + ":" + temp.substring(10, 12);
//
//								} else {
//									deviceMacAddress = temp;
//
//
//								}
//							}

//							}
							deviceSSID = temp;
//							System.out.println("ooooo" + deviceMacAddress + deviceSSID);
						}else if(result!=null&&result.contains("TGT")) {

							deviceSSID = result;
						}
						else
						{
							deviceSSID = "";
						}
					}
					if (TextUtils.isEmpty(deviceSSID)) {
						showNotFindDialog(getResources().getString(R.string.tip_can_not_find_the_device) );
						return;
					} else {
//						UserDataUtils.getInstance(mContext).setDeviceList(deviceSSID, deviceMacAddress);
//						getDeviceList();
						String temp;
						Log.i("temp", "onActivityResult: "+deviceSSID);

						if (deviceSSID.toString().contains("TGT")){
							temp = deviceSSID;

							deviceSSID = deviceSSID.replace("TGT","GTWiFi");


						}else{
							temp = deviceSSID.replace("GTWiFi","TGT");
//							deviceSSID = deviceSSID;
						}



						APIConstants.deviceInfo = new DeviceInfo(deviceSSID);
						OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
						params[0] = new OkHttpClientManager.Param("ssid", temp);
						Log.i("temp", "onActivityResult: "+temp);
						alertInfo = getResources().getString(R.string.tip_can_not_find_the_device);
						new SendRequest(APIConstants.Get_Device_Status, params, new MyHandler(), 1);
					}
				} else {
					//showNotFindDialog();
				}
				break;
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg == null || msg.obj == null) {
				return;
			}
			if (msg.what == -10) {
				return;
			}
			HttpResponseData responseData = new HttpResponseData((String) msg.obj);
			if (responseData == null || responseData.status < 0 || responseData.data == null) {
				return;
			}
			Log.i("response",msg.toString());

			switch (msg.what) {
				case 1:

					boolean is_active = false;
					if(responseData.data == null || responseData.data.size() == 0){
						showNotFindDialog(alertInfo);
						return;
//						UserDataUtils.getInstance(mContext).setDeviceList(deviceSSID, deviceMacAddress);
//
//						getDeviceList();
//
//						return;
					}
					Log.i("response",msg.toString());
					if (responseData.data.containsKey("new_version")) {
//                    APIConstants.deviceVersion = "T4_V2.1.6";//responseData.data.get("new_version").toString();
						if (responseData.data.containsKey("new_version") && !TextUtils.isEmpty(responseData.data.get("new_version").toString())) {
							APIConstants.deviceVersion = responseData.data.get("new_version").toString();
							setDialog.showVersionInfo(mContext);
							return;
						}
						return;
					}
					if (responseData.data.containsKey("device")) {
						LinkedTreeMap<String, Object> data = (LinkedTreeMap<String, Object>) responseData.data.get("device");
						if (data.containsKey("is_active") && data.get("is_active").toString().equals("true")) {
							is_active = true;
						}
						if (data.containsKey("is_active") && data.get("is_active").toString().equals("true")){

//							UserDataUtils.getInstance(mContext).setMacAddress(APIConstants.deviceInfo.getBluetoothMac());
//							UserDataUtils.getInstance(mContext).setSsid(APIConstants.deviceInfo.getSsid());
							UserDataUtils.getInstance(mContext).setDeviceList(deviceSSID, deviceMacAddress);
							getDeviceList();
						}
					}
					if(is_active == false){
						final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
						isExit.setTitle(R.string.tip_title);
						isExit.setMessage(getResources().getString(R.string.tip_active_header)+"("+deviceSSID+")"+getResources().getString(R.string.tip_for_active_device));
						isExit.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.tip_active_not), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								isExit.dismiss();
							}
						});
						isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.tip_active_now), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								isExit.dismiss();
								OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
//								if (deviceSSID.contains("GTWiFi")) {
									params[0] = new OkHttpClientManager.Param("ssid", deviceSSID.replace("GTWiFi","TGT"));

//								}

								new SendRequest(APIConstants.Device_Active, params, new MyHandler(), 2);
							}
						});

						isExit.show();
					}
					break;
				case 2:
//					if (responseData.data.containsKey("active") ) {

					if (responseData.data.containsKey("active") && responseData.data.get("active").toString().equals("true")) {
//						UserDataUtils.getInstance(mContext).setMacAddress(APIConstants.deviceInfo.getBluetoothMac());
//						UserDataUtils.getInstance(mContext).setSsid(APIConstants.deviceInfo.getSsid());
						final AlertDialog isExit = new AlertDialog.Builder(mContext).create();
						isExit.setTitle(R.string.tip_active_success);
						isExit.setMessage(getResources().getString(R.string.tip_active_success_info));

						isExit.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.tip_start_now), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								isExit.dismiss();
								UserDataUtils.getInstance(mContext).setDeviceList(deviceSSID, deviceMacAddress);
								getDeviceList();

							}
						});

						isExit.show();



					}
					else {
						TipUtil.showAlert(mContext,
								mContext.getResources().getText(R.string.tip_title).toString(),
								mContext.getResources().getText(R.string.tip_for_active_device_error).toString(),
								mContext.getResources().getText(R.string.commit_button).toString(),
								new TipUtil.OnAlertSelected() {
									@Override
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.dismiss();
									}
								});
					}
					break;
				default:
					break;
			}
		}
	}
}

