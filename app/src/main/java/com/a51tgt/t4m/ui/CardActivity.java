package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Observable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.utils.PayUtil;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Source;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.model.SourceCardData;
import com.stripe.android.model.SourceParams;
import com.stripe.android.view.CardMultilineWidget;


import java.util.concurrent.Callable;


public class CardActivity extends BaseActivity{
	CardMultilineWidget mCardMultilineWidget;
	private Dialog mWeiboDialog;
	String tilte, price,cardNum,cardBrand;

	@SuppressLint("InflateParams") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_input_widget1);
		//字符串截取

		tilte = getIntent().getStringExtra("title");
		price = getIntent().getStringExtra("price");

		Log.i("prcieeee", "onCreate: "+price+tilte);

		mCardMultilineWidget = findViewById(R.id.card_multiline_widget);
		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

//		Intent intent = new Intent(CardActivity.this, PaySuccessActivity.class);
//		intent.putExtra("trans_no","eeeeeeeeee");
//		intent.putExtra("title","12");
//		intent.putExtra("price","5");
//		intent.putExtra("cardNum","4242424242424242");
//		intent.putExtra("cardBrand","ee");
//
//startActivity(intent);
		findViewById(R.id.save_payment).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				saveCard();
			}
		});
//		mCompositeSubscription.add(
//				RxView.clicks(findViewById(R.id.save_payment)).subscribe(new Action1<Void>() {
//					@Override
//					public void call(Void aVoid) {
//						saveCard();
//					}
//				}));
//

	}
	private void pay(String token) {


		String ssid;
		if (APIConstants.deviceInfo.getSsid().contains("GTWiFi")) {

			ssid = APIConstants.deviceInfo.getSsid().replace("GTWiFi","TGT");

		}else{


			ssid = APIConstants.deviceInfo.getSsid();
		}



		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[4];
		params[0] = new OkHttpClientManager.Param("ssid", ssid);
		params[1] = new OkHttpClientManager.Param("product_id", getIntent().getIntExtra("product_id", 0)+"");
		params[2] = new OkHttpClientManager.Param("start_date", getIntent().getStringExtra("start_date"));
		params[3] = new OkHttpClientManager.Param("token", token);
		new SendRequest(APIConstants.Purchase_Product_StripePay, params, new MyHandler(), 1);

	}
	private void saveCard() {
		Card card = mCardMultilineWidget.getCard();

		if (card == null) {
			return;
		}
		cardNum = card.getNumber();
		cardBrand = card.getBrand()+" card";
		mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CardActivity.this,getResources().getString(R.string.loading));
//		mHandler.sendEmptyMessageDelayed(1, 2000);
//		pk_live_sMAkefrTEHc8BWoApoD5PD5L
		Stripe stripe = new Stripe(this, "pk_live_sMAkefrTEHc8BWoApoD5PD5L");
		stripe.createToken(
				card,
				new TokenCallback() {
					public void onSuccess(Token token) {


						Log.i("tokentntnntnt:",token.getId());
						pay(token.getId());


						// Send token to your server
					}
					public void onError(Exception error) {

						Log.i("tokenerrrr:",error.getMessage());
//						finish();
//						pay("999");

						WeiboDialogUtils.closeDialog(mWeiboDialog);

//
//						Intent intent = new Intent(CardActivity.this, PaySuccessActivity.class);
//						intent.putExtra("trans_no","6666");
//						intent.putExtra("title",tilte);
//						intent.putExtra("price",price);
//						intent.putExtra("cardNum",cardNum);
//						intent.putExtra("cardBrand",cardBrand);
//
//
//
//						startActivity(intent);
//						Intent intent1 = new Intent();
//						intent1.setAction(APIConstants.BR_ORDER_STATUS);
//						intent1.putExtra("order_status", true);
//						sendBroadcast(intent1);

//						Intent intent = new Intent();
//						intent.setAction(APIConstants.BR_ORDER_STATUS);
//						intent.putExtra("order_status", false);
//						sendBroadcast(intent);
		Toast.makeText(CardActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

						// Show localized error message
//						Toast.makeText(getContext(),
//								error.getLocalizedString(getContext()),
//								Toast.LENGTH_LONG
//						).show();
					}






				}
		);




















	}
	@SuppressLint("HandlerLeak")
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
//                    wxPayResponseData = null;
//                    if(wxPayResponseData != null){
//                        PayUtil.WXPayForOrder(iWXApi, APIConstants.WX_APP_ID,
//                                wxPayResponseData.data.get("prepayId").toString(),
//                                wxPayResponseData.data.get("partnerId").toString(),
//                                wxPayResponseData.data.get("package").toString(),
//                                wxPayResponseData.data.get("nonceStr").toString(),
//                                wxPayResponseData.data.get("timeStamp").toString(),
//                                wxPayResponseData.data.get("sign").toString());
//                    }
//                    else{
//
//                        }
					Log.i("uuuuuuuuu",responseData.toString());
					if(responseData.data.containsKey("pay_status")){
						if (responseData.data.get("pay_status").toString().equals("true")){

							Intent intent = new Intent(CardActivity.this, PaySuccessActivity.class);
							intent.putExtra("trans_no",responseData.data.get("trans_no").toString());
							intent.putExtra("title",tilte);
							intent.putExtra("price",price);
							intent.putExtra("cardNum",cardNum);
							intent.putExtra("cardBrand",cardBrand);



							startActivity(intent);
							Intent intent1 = new Intent();
							intent1.setAction(APIConstants.BR_ORDER_STATUS);
							intent1.putExtra("order_status", true);
							sendBroadcast(intent1);
						}
						else{

//							Intent intent = new Intent();
//							intent.setAction(APIConstants.BR_ORDER_STATUS);
//							intent.putExtra("order_status", false);
//							sendBroadcast(intent);
							Toast.makeText(CardActivity.this,responseData.msg.toString(),Toast.LENGTH_SHORT).show();

						}
						WeiboDialogUtils.closeDialog(mWeiboDialog);

//						finish();

					}else {

						WeiboDialogUtils.closeDialog(mWeiboDialog);


						Toast.makeText(CardActivity.this,responseData.msg.toString(),Toast.LENGTH_SHORT).show();




					}
					break;



				default:
					break;
			}
		}
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
}

