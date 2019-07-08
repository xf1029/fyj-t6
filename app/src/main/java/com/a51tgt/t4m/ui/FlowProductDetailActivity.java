package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a51tgt.t4m.BuildConfig;
import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.adapter.FlowMallAdapter;
import com.a51tgt.t4m.bean.FlowProductInfo;
import com.a51tgt.t4m.bean.HttpResponseData;
import com.a51tgt.t4m.bean.PackageInfo;
import com.a51tgt.t4m.bean.UserDataUtils;
import com.a51tgt.t4m.bean.WxHttpResponseData;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.net.OkHttpClientManager;
import com.a51tgt.t4m.net.SendRequest;
import com.a51tgt.t4m.ui.view.PackageInfoView;
import com.a51tgt.t4m.utils.CommUtil;
import com.a51tgt.t4m.utils.PayUtil;
import com.a51tgt.t4m.utils.TipUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liu_w on 2017/9/15.
 */

public class FlowProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private ProgressBar pb_progress;
    private LinearLayout payPalLayout;

    private ImageView iv_back, iv_close, iv_pay_type, iv_result;
    private WebView webView;
    private TextView pay_title,tv_title, tv_price, tv_price_type, tv_package_name, tv_pay_price, tv_ssid, tv_money_type, tv_start_date;
    private Button bt_commit, bt_buy_now, bt_close;
    private Context mContext;
    private String url, title, price, price_type,money_type,payType = "FC";
    private int product_id;
    private CheckBox wechatCheck,paypalCheck;

    private LinearLayout ll_pay, ll_pay_info, ll_order_status;

    // 配置各种支付类型，一般就沙盒测试的和正式的
    private static final String CONFIG_ENVIRONMENT = /*BuildConfig.DEBUG ? PayPalConfiguration.ENVIRONMENT_SANDBOX :*/ PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    // note that these credentialswill differ between live & sandbox environments.
// 这是在第一步时候注册创建得来的Client ID
    private static final String CONFIG_CLIENT_ID = /*BuildConfig.DEBUG ? "AbpeJgzsjhKYOEBsClpMmd9koLInA9edZijfbjnaWGsoYpysI4IsOGl0WLPCKr2qF_Fa8Q8Ye90b5XO2" :*/ "AYnWniLiu_BrY9QgKLOiXXHgHSQ8Ndn7sXTrCcy9JAlmUWz0HWOCacKjMCn800wzHLc9VJYIWpeNBcVK";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private HttpResponseData wxPayResponseData;
    private IWXAPI iWXApi;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);
    // The following are only used inPayPalFuturePaymentActivity.
// 下面的这些都是要用到授权支付才用到的，不用就注释掉可以了
//        .merchantName("Example Merchant")
//        .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
//            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));


    //接收广播的处理
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            boolean order_status = intent.getBooleanExtra("order_status", false);
            if(order_status == true){
//                ll_pay_info.setVisibility(View.GONE);
//                ll_order_status.setVisibility(View.VISIBLE);
//                AnimationDrawable frameAnimation = (AnimationDrawable) iv_result.getBackground();  //获得动画图片列表animation-list
//                frameAnimation.start();
            }
            else{
//                ll_pay_info.setVisibility(View.GONE);
                Toast.makeText(mContext, R.string.tip_wxpay_failed, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_product_detail);
//    recreate();

        mContext = FlowProductDetailActivity.this;
        iWXApi = WXAPIFactory.createWXAPI(this, APIConstants.WX_APP_ID);

        webView = (WebView) findViewById(R.id.webview);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_pay_type = (ImageView) findViewById(R.id.iv_pay_type);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_price = (TextView) findViewById(R.id.tv_price);
        bt_commit = (Button) findViewById(R.id.bt_commit);
        pay_title = (TextView)findViewById(R.id.tv_pay_title);
        payPalLayout = (LinearLayout) findViewById(R.id.paypal_layout);
        wechatCheck = (CheckBox) findViewById(R.id.wechat_check);
        paypalCheck = (CheckBox) findViewById(R.id.paypal_check);

        bt_buy_now = (Button) findViewById(R.id.bt_buy_now);
        tv_price_type = (TextView) findViewById(R.id.tv_price_type);
        tv_package_name = (TextView) findViewById(R.id.tv_package_name);
        tv_pay_price = (TextView) findViewById(R.id.tv_pay_price);
        tv_ssid = (TextView) findViewById(R.id.tv_ssid);
        tv_money_type = (TextView) findViewById(R.id.tv_money_type);
        iv_result = (ImageView) findViewById(R.id.iv_result);
        bt_close = (Button) findViewById(R.id.bt_close);
        tv_start_date = (TextView) findViewById(R.id.tv_start_date);

        ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
        ll_pay.setVisibility(View.GONE);
        ll_pay_info = (LinearLayout) findViewById(R.id.ll_pay_info);
        ll_order_status = (LinearLayout) findViewById(R.id.ll_order_status);

        product_id = getIntent().getIntExtra("product_id", 0);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        price_type = getIntent().getStringExtra("price_type");
        money_type = getIntent().getStringExtra("money_type");

        if (TextUtils.isEmpty(price_type))
            price_type = "USD";

        tv_title.setText(title);
        tv_price.setText(price);
        tv_price_type.setText(money_type);
        tv_package_name.setText(title);
        tv_pay_price.setText(price);
        tv_ssid.setText(getResources().getText(R.string.tip_device_ssid) + APIConstants.deviceInfo.getSsid());
        tv_money_type.setText(money_type);
        if (price_type.equals("RMB")) {
            iv_pay_type.setImageResource(R.mipmap.ic_credit);
            pay_title.setText("国际信用卡支付");
            payPalLayout.setVisibility(View.VISIBLE);


        } else {
//            iv_pay_type.setImageResource(R.mipmap.ic_wx_pay);
//            pay_title.setText("微信支付");
        }
        iv_back.setOnClickListener(this);
        bt_commit.setOnClickListener(this);
        bt_buy_now.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        bt_close.setOnClickListener(this);
        wechatCheck.setOnClickListener(this);
        paypalCheck.setOnClickListener(this);
        tv_start_date.setOnClickListener(this);

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

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(mContext, getResources().getText(R.string.system_webview_error), Toast.LENGTH_SHORT).show();
            }
        });

        webView.loadUrl(url);

        //注册接受的广播
        IntentFilter filter = new IntentFilter(APIConstants.BR_ORDER_STATUS);
        registerReceiver(broadcastReceiver, filter);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_commit:
                ll_pay.setVisibility(View.VISIBLE);
                ll_pay_info.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_buy_now:
                String start_date = tv_start_date.getText().toString().replace(getString(R.string.tip_start_date), "").replace(getString(R.string.tip_select_start_date), "");
                if(TextUtils.isEmpty(start_date)){
                    Toast.makeText(mContext, R.string.warning_input_start_date, Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(FlowProductDetailActivity.this, CardActivity.class);
                intent.putExtra("start_date", start_date);
                intent.putExtra("product_id", product_id);
                intent.putExtra("title",title);
                intent.putExtra("price",price);



                startActivity(intent);

//                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
             /*  if(TextUtils.isEmpty(start_date)){
                    Toast.makeText(mContext, R.string.warning_input_start_date, Toast.LENGTH_LONG).show();
                    return;
                }

                if (!price_type.equals("RMB")) {

                    OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[3];
                    params[0] = new OkHttpClientManager.Param("ssid",  APIConstants.deviceInfo.getSsid());
                    params[1] = new OkHttpClientManager.Param("product_id", product_id+"");
                    params[2] = new OkHttpClientManager.Param("start_date", start_date);

                    new SendRequest(APIConstants.Purchase_Product, params, new MyHandler(), 1);
//                    Intent intent = new Intent(this, CreditActivity.class);
//
//                    startActivity(intent);
//                    PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//                    Intent intent = new Intent(FlowProductDetailActivity.this, PaymentActivity.class);
//                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//                    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                }else {

                    OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[4];
                    params[0] = new OkHttpClientManager.Param("ssid",  APIConstants.deviceInfo.getSsid());
                    params[1] = new OkHttpClientManager.Param("product_id", product_id+"");
                    params[2] = new OkHttpClientManager.Param("start_date", start_date);
                    params[3] = new OkHttpClientManager.Param("payment_schema", payType);

                    new SendRequest(APIConstants.Purchase_Product_credit, params, new MyHandler(), 2);
                }*/
                break;
            case R.id.tv_start_date:
                String start = CommUtil.getDateNow();
                TimeSelector timeSelector = new TimeSelector(mContext, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        String[] temps = time.split(" ");
                        tv_start_date.setText(getString(R.string.tip_start_date) + temps[0]);
//                        Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
                    }
                }, start, "2025-12-31 23:59");
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
                break;
            case R.id.wechat_check:
                payType = "FC";
                wechatCheck.setChecked(true);
                paypalCheck.setChecked(false);

                break;
            case R.id.paypal_check:
                payType = "PAYPAL";
                wechatCheck.setChecked(false);
                paypalCheck.setChecked(true);

                break;
            case R.id.iv_close:
            case R.id.bt_close:
                ll_pay.setVisibility(View.GONE);
                ll_pay_info.setVisibility(View.VISIBLE);
                ll_order_status.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirm != null){
                    ll_pay_info.setVisibility(View.GONE);
                    iv_result.setVisibility(View.VISIBLE);
                    PayPalPayment payPalPayment = confirm.getPayment();
                    AnimationDrawable frameAnimation = (AnimationDrawable) iv_result.getBackground();  //获得动画图片列表animation-list
                    frameAnimation.start();

                    try {
                        String mPaymentId = confirm.toJSONObject().getJSONObject("response").getString("id");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                pb_progress.setVisibility(View.GONE);
            } else {
                if (pb_progress.getVisibility() == View.GONE)
                    pb_progress.setVisibility(View.VISIBLE);
                pb_progress.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    /*
 * This method shows use of optionalpayment details and item list.
 */
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("0.01"), "USD", title, paymentIntent);
    }

    private PayPalPayment getStuffToBuy(String paymentIntent) {
        //--- include an item list, payment amountdetails
        PayPalItem[] items =
        {
                new PayPalItem(title, 1, new BigDecimal(price), "USD", "tfms_paypal_"+product_id)
        };
        BigDecimal subtotal = PayPalItem.getItemTotal(items);
        BigDecimal shipping = new BigDecimal("0.0");
        BigDecimal tax = new BigDecimal("0.0");
        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(shipping, subtotal, tax);
        BigDecimal amount = subtotal.add(shipping).add(tax);
        PayPalPayment payment = new PayPalPayment(amount, "USD", title, paymentIntent);
        payment.items(items).paymentDetails(paymentDetails);

        //--- set other optional fields likeinvoice_number, custom field, and soft_descriptor
//        payment.custom("This is text that will be associatedwith the payment that the app can use.");

        return payment;
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
                    if(responseData.data.containsKey("out_order_no")
                            && responseData.data.containsKey("prepayId")
                            && responseData.data.containsKey("partnerId")
                            && responseData.data.containsKey("package")
                            && responseData.data.containsKey("nonceStr")
                            && responseData.data.containsKey("timeStamp")
                            && responseData.data.containsKey("sign")
                            ){
                        wxPayResponseData = responseData;
                        APIConstants.OUT_TRADE_NO = responseData.data.get("out_order_no").toString();
                        PayUtil.WXPayForOrder(FlowProductDetailActivity.this,iWXApi, APIConstants.WX_APP_ID,
                                responseData.data.get("prepayId").toString(),
                                responseData.data.get("partnerId").toString(),
                                responseData.data.get("package").toString(),
                                responseData.data.get("nonceStr").toString(),
                                responseData.data.get("timeStamp").toString(),
                                responseData.data.get("sign").toString());
                    }

                break;
                case 2:
                    if (responseData.code==1) {
                        APIConstants.OUT_TRADE_NO = responseData.data.get("trans_no").toString();
                        Intent intent = new Intent(mContext, CreditActivity.class);

                        intent.putExtra("weburl",responseData.data.get("pay_url").toString());




                        startActivity(intent);
                    }else {



                        Toast.makeText(mContext,"订单异常",Toast.LENGTH_SHORT);

                    }




                    break;
                case 3:
                break;
                default:
                    break;
            }
        }
    }

}
