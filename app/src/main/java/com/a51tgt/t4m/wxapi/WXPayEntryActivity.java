package com.a51tgt.t4m.wxapi;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import com.a51tgt.t4m.R;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.ui.BaseActivity;
import com.a51tgt.t4m.utils.PayUtil;
import com.a51tgt.t4m.utils.TipUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	
	private IWXAPI api;
	Context mContext;
	private int res_code = BaseResp.ErrCode.ERR_OK;
	private boolean queryAgain = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wx_entry);
		mContext = this;

		api = WXAPIFactory.createWXAPI(this, APIConstants.WX_APP_ID, false);
		api.registerApp(APIConstants.WX_APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req)
	{
	}

	@Override
	public void onResp(BaseResp resp)
	{
		if (resp.getType() != ConstantsAPI.COMMAND_PAY_BY_WX)
			return;

		String msg = resp.errCode+"";
		res_code = resp.errCode;
		switch (resp.errCode)
		{
		case BaseResp.ErrCode.ERR_OK:
			msg = getString(R.string.tip_wxpay_success);
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			msg = getString(R.string.tip_wxpay_cancel);
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			msg = getString(R.string.tip_wxpay_denied);
			break;
		case BaseResp.ErrCode.ERR_SENT_FAILED:
			msg = getString(R.string.tip_wxpay_failed);
			break;
		case BaseResp.ErrCode.ERR_UNSUPPORT:
			msg = getString(R.string.tip_wxpay_unsupport);
			break;
		default:
			break;
		}

		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		if(resp.errCode == BaseResp.ErrCode.ERR_OK)
			SendQuery();
		queryAgain = false;
		finish();
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg == null || msg.obj == null) {
				return;
			}
			if(msg.what == -10){
				return;
			}

			HttpResponseData responseData = new HttpResponseData((String) msg.obj);
			if (responseData == null || responseData.status < 0 || responseData.data == null) {
				return;
			}

			switch (msg.what){
				case 1:
					if(responseData.data.containsKey("pay_status")){
						if (responseData.data.get("pay_status").toString().equals("true")){
							Intent intent = new Intent();
							intent.setAction(APIConstants.BR_ORDER_STATUS);
							intent.putExtra("order_status", true);
							sendBroadcast(intent);
						}
						else{
							Intent intent = new Intent();
							intent.setAction(APIConstants.BR_ORDER_STATUS);
							intent.putExtra("order_status", false);
							sendBroadcast(intent);
						}
					}
					break;
			}
			finish();
		}
	}

	private void SendQuery()
	{
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
		params[0] = new OkHttpClientManager.Param("out_order_no",  APIConstants.OUT_TRADE_NO);
		new SendRequest(APIConstants.Query_WxOrder, params, new MyHandler(), 1);
	}
}