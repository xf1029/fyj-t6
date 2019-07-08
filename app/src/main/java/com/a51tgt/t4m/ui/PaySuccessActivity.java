package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Calendar;
import java.util.TimeZone;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.utils.AppLanguageUtils;


public class PaySuccessActivity extends BaseActivity{

	TextView pay_time,pay_tille,pay_price,pay_total_price,pay_summary;
	@SuppressLint("InflateParams") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_success);
		String tilte = getIntent().getStringExtra("title");
		String price = getIntent().getStringExtra("price");
		String cardNum = getIntent().getStringExtra("cardNum");
		String cardBrand = getIntent().getStringExtra("cardBrand");
		String trans_no = getIntent().getStringExtra("trans_no");

//		CommUtil.setStatusBarBackgroundColor(AboutUsActivity.this);
		PaySuccessActivity context = PaySuccessActivity.this;
		pay_time = (TextView)findViewById(R.id.pay_time);
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

	String	year = String.valueOf(cal.get(Calendar.YEAR));
	String	month = String.valueOf(cal.get(Calendar.MONTH))+1;
	String	day = String.valueOf(cal.get(Calendar.DATE));
		String	hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		String	minute = String.valueOf(cal.get(Calendar.MINUTE));
		String	secode = String.valueOf(cal.get(Calendar.SECOND));


		pay_time.setText("Purchase time: "+year + "-" + month + "-" + day+""+hour+":"+minute+":"+secode);

		pay_tille = findViewById(R.id.pay_title);
		pay_price = (TextView)findViewById(R.id.pay_price);
		TextView inf = (TextView) findViewById(R.id.info);
		inf.setText("You can find the expired date of this data package in \"Main page\".");
		pay_tille.setText(tilte);
		//字符串截取
		String cc = cardNum.substring(0,2)+"**********"+cardNum.substring(12,16);



		pay_price.setText("$"+price+"(USD)");
		pay_total_price = (TextView)findViewById(R.id.pay_total_price);
		pay_total_price.setText("$"+price+"(USD)");
		pay_summary = (TextView)findViewById(R.id.pay_summary);
 pay_summary.setText("Card Payment Summary: "+"\nCard Type: "+cardBrand+"\nCard: "+cc+"\nTransaction Type: SALE"+"\nTransaction ID: "+trans_no);
		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(PaySuccessActivity.this,MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				AppLanguageUtils.changeAppLanguage(MZApplication.getContext(), APIConstants.currentLan);

				finish();


			}
		});

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

