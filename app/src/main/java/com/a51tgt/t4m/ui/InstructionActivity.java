package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.net.ViewPagerFixed;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;


public class InstructionActivity extends BaseActivity implements DownloadFile.Listener {
	private RelativeLayout pdf_root;
	private ProgressBar pb_bar;
	private ViewPagerFixed remotePDFViewPager;
	private String mUrl = "http://mall.51tgt.com/download/introduce-jp.pdf";
	private String mUrl2="https://www.tutorialspoint.com/ios/ios_tutorial.pdf";

	private PDFPagerAdapter adapter;
	private ImageView iv_back;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instruction);
		initView();
		setDownloadListener();
	}


	protected void initView() {
		pdf_root = (RelativeLayout) findViewById(R.id.remote_pdf_root);
		pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/*设置监听*/
	protected void setDownloadListener() {
		final DownloadFile.Listener listener = this;
	String item = getIntent().getStringExtra("item");
		remotePDFViewPager = new ViewPagerFixed(this, "http://mall.51tgt.com/download/instructions/"+item+".pdf", listener);
		remotePDFViewPager.setId(R.id.pdfViewPager);
	}

	/*加载成功调用*/
	@Override
	public void onSuccess(String url, String destinationPath) {
		pb_bar.setVisibility(View.GONE);
		adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
		remotePDFViewPager.setAdapter(adapter); updateLayout();
	}

	/*更新视图*/
	private void updateLayout() {
		pdf_root.removeAllViewsInLayout();
		pdf_root.addView(remotePDFViewPager, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}
	/*加载失败调用*/
	@Override
	public void onFailure(Exception e) {
		pb_bar.setVisibility(View.GONE);
		Toast.makeText(InstructionActivity.this,"数据加载错误",Toast.LENGTH_SHORT).show();
//		ToastUitl.show(this, "数据加载错误");
	}
	@Override
	public void onProgressUpdate(int progress, int total) {

	}
}