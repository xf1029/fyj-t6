package com.a51tgt.t4m.net;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.config.TugeMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import com.a51tgt.t4m.MZApplication;
public class TcpUtil {
	private static TcpUtil instance;
	public static String hostIP="192.168.43.1";
	public static  Socket socketTemp = null;

	private TcpUtil(){
	}

	public static synchronized  TcpUtil getInstance(){
		if(null == instance){
			instance = new TcpUtil();
//			socket = new Socket();
		}
		return instance;
	}


	public void sendMessage(final String msg, final Handler handler){
		Runnable sendTcp = new Runnable() {
			@Override
			public void run() {

				BufferedReader in = null;
				PrintWriter out = null;

				Socket socket = new Socket();
				socketTemp =  socket;
				JSONObject object = new JSONObject();
				try {
					socket.connect(new InetSocketAddress(hostIP, TcpConfig.port), 10 * 1000);
					if (!msg.isEmpty()) {
						if (!socket.isConnected() || socket.isClosed()) {

							Toast.makeText(MZApplication.getContext(),"未连接到设备",Toast.LENGTH_SHORT);
						}
						while (socket.isConnected() && !socket.isClosed()) {
							try {
								Message message = new Message();
								out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
								out.println(TcpConfig._HEAD_ + "/" + msg);
								Log.i("deviceinfo",msg);
								in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
								String line = null;
								while ((line = in.readLine()) != null) {//注意当读入的数据有多行时（含有\n换行符），会出问题
									object.put("send", msg);
									object.put("get", line);
									message.what = TugeMessage.GET_MESSAGE;
									message.obj = object;
									if (handler != null) {
										handler.sendMessage(message);
										Log.i("tetetetetetete",message.toString());

									}
									//Log.i(message.obj.toString());
								}
								break;
							} catch (Exception e) {
//								if (msg.equals("CMD_SET_WIFIANDPASSWORD"))
//									MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_SET_WIFIANDPASSWORD, "true", OnNoticeUI.KEY_TYPE_DEVICE_SETTING_FRAGMENT);
//								else if (msg.equals(TcpConfig.BT_REDUCTION))
//									MZApplication.getInstance().sendUiNotice(OnNoticeUI.NOTICE_TYPE_BT_DEVICE_REDUTION, "true", OnNoticeUI.KEY_TYPE_DEVICE_REDUCTION);




								Log.i("而肉肉肉肉肉",e.toString());
								Message message = new Message();
								message.what = TugeMessage.EXCEPTION_ERROR;
								if (e instanceof JSONException) {
									message.what = TugeMessage.EXCEPTION_JSON_ERROR;
								}
								try {
									object.put("send", msg);
									object.put("get", e.getMessage());
									message.obj = object;
								}catch (JSONException e1){
									e1.printStackTrace();
								}
								if (handler != null) {
									handler.sendMessage(message);
								}
							}
						}
					}
				} catch (IOException e) {
					Message message = new Message();
					message.what = TugeMessage.EXCEPTION_IO_ERROR;

					if (e.getMessage() != null && e.getMessage().contains(hostIP)) {
						message.what = TugeMessage.TIME_OUT;
					}

					try {
						object.put("send", msg);
						object.put("get", e.getMessage());
						message.obj = object;
					}catch (JSONException e1){
						e1.printStackTrace();
					}

					if (handler != null) {
						handler.sendMessage(message);
					}
				} finally {
					try {
						if (in != null)
							in.close();
						if (socket != null)
							socket.close();
						if (out != null)
							out.close();
					} catch (IOException e) {
					}
				}
			}
		};
		MZApplication.getInstance().runBackGround(sendTcp,0);

	}
public  void  closeSocket (){

		try {
			socketTemp.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
//	private void Log.i(Object o){
////		MZApplication.Log.i("TcpUtil:"+o);
////
// }
}
