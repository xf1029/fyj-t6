package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.a51tgt.t4m.R;


public class ProductActivity extends BaseActivity{

	TextView tv_version;
	static Point size;
	static float density;
	@SuppressLint("InflateParams") @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		Display display = getWindowManager().getDefaultDisplay();
		size=new Point();
		DisplayMetrics dm=new DisplayMetrics();
		display.getMetrics(dm);
		density=dm.density;
		display.getSize(size);
//		CommUtil.setStatusBarBackgroundColor(AboutUsActivity.this);
		ProductActivity context = ProductActivity.this;
//		WeDroidAlignTextView tv1=(WeDroidAlignTextView)findViewById(R.id.tv_introduce);

////		TextView tv=(TextView) findViewById(R.id.tv_introduce);
//		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
//		tv.setTypeface( font );
////		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
////		tv.setTypeface(typeface);
//		tv.setLineSpacing(0f, 1.2f);
//		tv.setTextSize(4*AboutUsActivity.density);

		//some random long text
		String myText=getResources().getString(R.string.device_introduce);

//		tv.setText(myText);
//		TextJustification.justify(tv,size.x);


		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			String version = packInfo.versionName;
//			tv_version.setText(version);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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

