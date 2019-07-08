package com.a51tgt.t4m.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;

public class CreditActivity extends AppCompatActivity {
    private WebView webView;
    public  String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideStatusNavigationBar();
        setContentView(R.layout.activity_credit_pay);
        Intent intent = getIntent();//获取传来的intent对象
        String data = intent.getStringExtra("weburl");//获取键值对的键名
        webView = (WebView) findViewById(R.id.webview1);
        Log.i("WEBURL:",data);
//        webView.loadUrl("file:///android_asset/test.html");//加载asset文件夹下html
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);

//        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                Log.i("allurl:",url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                if (url.contains("tgt://do=close")){
                    SendQuery();
//                    queryAgain = false;
                    finish();
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
//                Toast.makeText(CreditActivity.this, getResources().getText(R.string.system_webview_error), Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl(data);



//        WebSettings webSettings=webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);//允许使用js

    }


    private void SendQuery()
    {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[1];
        params[0] = new OkHttpClientManager.Param("trans_no",  APIConstants.OUT_TRADE_NO);
        new SendRequest(APIConstants.Query_CreditOrder, params, new CreditActivity.MyHandler(), 1);
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

}



