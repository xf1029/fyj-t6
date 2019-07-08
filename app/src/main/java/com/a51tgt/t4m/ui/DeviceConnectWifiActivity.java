package com.a51tgt.t4m.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.adapter.ScanWifiAdapter;
import com.a51tgt.t4m.bean.AcessPoint;
import com.a51tgt.t4m.bluetooth.BluetoothUtil;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.comm.TcpConfig;
import com.a51tgt.t4m.net.TcpUtil;
import com.a51tgt.t4m.ui.view.OtherWiFiActivity;
import com.a51tgt.t4m.utils.CommUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by liu_w on 2017/9/15.
 */

public class DeviceConnectWifiActivity extends BaseActivity implements OnNoticeUI, ScanWifiAdapter.OnItemClickListener,View.OnClickListener {

    private Context mContext;
    private RecyclerView wifiRecyclerView, rv_saved_wifi, rv_connected_wifi;
    private ScanWifiAdapter adapter;
    private TextView   otherWifi;
    public String ssisName;
    List<AcessPoint> myWifiList = new ArrayList<AcessPoint>();
    List<AcessPoint> connectedWifiList = new ArrayList<AcessPoint>();
    List<AcessPoint> savedWifiList = new ArrayList<AcessPoint>();
    List<AcessPoint> searchedWifiList = new ArrayList<AcessPoint>();
    private ToggleButton tb_open_wifi;
    private LinearLayout ll_saved_wifi, ll_connected_wifi;
    private AcessPoint currentAp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connect_wifi);

        //CommUtil.setStatusBarBackgroundColor(DeviceConnectWifiActivity.this);
        mContext = DeviceConnectWifiActivity.this;
        tb_open_wifi = (ToggleButton) findViewById(R.id.tb_open_wifi);
        tb_open_wifi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(tb_open_wifi.isChecked()){
                    otherWifi.setVisibility(View.VISIBLE);
                    BluetoothUtil.getInstance().sendMessage(TcpConfig.BT_OPEN_WIFI);
                }
                else{
                    otherWifi.setVisibility(View.GONE);

                    BluetoothUtil.getInstance().sendMessage(TcpConfig.BT_CLOSE_WIFI);
                    clearWifiList();
                }
            }

        });

        wifiRecyclerView = (RecyclerView) findViewById(R.id.rv_wifi);
        wifiRecyclerView.setHasFixedSize(true);
        wifiRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        rv_saved_wifi = (RecyclerView) findViewById(R.id.rv_saved_wifi);
        rv_saved_wifi.setHasFixedSize(true);
        rv_saved_wifi.setLayoutManager(new LinearLayoutManager(mContext));

        rv_connected_wifi = (RecyclerView) findViewById(R.id.rv_connected_wifi);
        rv_connected_wifi.setHasFixedSize(true);
        rv_connected_wifi.setLayoutManager(new LinearLayoutManager(mContext));

        ll_saved_wifi = (LinearLayout) findViewById(R.id.ll_saved_wifi);
        ll_connected_wifi = (LinearLayout) findViewById(R.id.ll_connected_wifi);
        otherWifi = (TextView) findViewById(R.id.other_wifi);
        otherWifi.setOnClickListener(this);
        ll_saved_wifi.setVisibility(View.GONE);
        ll_connected_wifi.setVisibility(View.GONE);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new ScanWifiAdapter(myWifiList);
        adapter.setOnItemClickListener(this);
        wifiRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume(){
        super.onResume();
        MZApplication.getInstance().setOnNoticeUiListener(this,OnNoticeUI.KEY_TYPE_WIFI_ACTIVITY);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        MZApplication.getInstance().setOnNoticeUiListener(null,OnNoticeUI.KEY_TYPE_WIFI_ACTIVITY);
    }

    @Override
    public void onNotice(int NOTICE_TYPE, Object object) {
        tb_open_wifi.setChecked(true);
        otherWifi.setVisibility(View.VISIBLE);
//        if(!tb_open_wifi.isChecked()) return;
        switch (NOTICE_TYPE){
            case OnNoticeUI.NOTICE_TYPE_BT_MSG_CURRENT_AP:
                if(object.getClass().getName().equals("java.util.ArrayList")){
                    List<AcessPoint> aps = (List<AcessPoint>) object;
                    for(AcessPoint ap : aps) {
                        if(!TextUtils.isEmpty(ap.getState()) && ap.getState().equals("已连接"))
                            AddAcessPoint(ap, NOTICE_TYPE);
                    }
                }
                else{
                    AcessPoint ap = (AcessPoint) object;

                    if (ap.getSsid().equals(ssisName)) return;
                    if (ap.getSsid().equals("<unknown ssid>")) {
                        if (currentAp != null) {
//                            currentAp = null;
//                            currentAp.setSsid("unkonw");
//                               currentAp.setState("");
                            currentAp.setState(getResources().getString(R.string.status_wifi_saved));
                            return;

                        }else {


                        }
                    }else {
                        Log.i("currentttetetete",ap.getSsid());

                        ssisName = ap.getSsid();
                        currentAp = ap;
                    }
                    AddAcessPoint(ap, NOTICE_TYPE);
//                    if(ap != null && !TextUtils.isEmpty(ap.getPwd()))
//                        AddAcessPoint((AcessPoint) object, NOTICE_TYPE);
                }
                break;
            case OnNoticeUI.NOTICE_TYPE_BT_MSG_SAVED_AP:
                if(object.getClass().getName().equals("java.util.ArrayList")){

                    List<AcessPoint> aps = (List<AcessPoint>) object;
//                    for (int i=0;i<aps.size();i++) {
//                        AcessPoint tmep =aps[i];
//                        if (tmep.getSsid())
//
//                    }
                    for(AcessPoint ap : aps) {
//                       if (ap.getState().equals("已连接")) return;
//                        if(!TextUtils.isEmpty(ap.getState()) && !ap.getState().equals(getResources().getString(R.string.tip_connected_to)))
                            Log.i("apapappapp",ap.getSsid()+NOTICE_TYPE);
                            AddAcessPoint(ap, NOTICE_TYPE);
                    }
                }
                break;
            case OnNoticeUI.NOTICE_TYPE_BT_MSG_SEARCH_AP:
                if(object.getClass().getName().equals("java.util.ArrayList")){


                    List<AcessPoint> aps = (List<AcessPoint>) object;
                    for(AcessPoint ap : aps) {

                           AddAcessPoint(ap, NOTICE_TYPE);
                    }
                }
                break;
            case NOTICE_TYPE_CONNECT_PROCEDURE:
                String s = (String)object;

                if(currentAp != null && !TextUtils.isEmpty(s)){
                    Log.i("current99999",currentAp.getSsid()+s);

                    currentAp.setState(getAcessPointState(s));
                    AddAcessPoint(currentAp,NOTICE_TYPE);
//                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }


    private void AddAcessPointForConnectWifiList(AcessPoint ap){
        if(ap == null || ap.getSsid().equals("<unknown ssid>")) return;

        for(int i=0; i < connectedWifiList.size(); i++){
            if(connectedWifiList.get(i).getSsid().equals(ap.getSsid())){
                return;
            }
        }
        connectedWifiList.add(ap);
        updateWifiList();
    }

    private void AddAcessPointForSeached(AcessPoint ap){
        List<AcessPoint> tempWifiList = new ArrayList<AcessPoint>();
        for(int i=0; i < myWifiList.size(); i++){
            if(!TextUtils.isEmpty(myWifiList.get(i).getState())){
                tempWifiList.add(myWifiList.get(i));
            }
        }
        if(ap != null){
            for(int i=0; i < myWifiList.size(); i++){
                if(myWifiList.get(i).getSsid().equals(ap.getSsid())){
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
        myWifiList.add(ap);
        adapter.notifyDataSetChanged();
    }

    private void modifyAcessPointSsid(List<AcessPoint> aps) {
        if(aps == null || aps.size() <= 0) return;
        for (AcessPoint ap : aps ) {
            ap.setSsid(ap.getSsid().replace("\"", ""));
        }
    }

    private void modifyAcessPointSsid(AcessPoint ap) {
        if(ap == null) return;
        ap.setSsid(ap.getSsid().replace("\"", ""));
    }

    private AcessPoint isInConnectedWifiList(AcessPoint ap){
        for (AcessPoint temp : connectedWifiList ) {
            if(temp.getSsid().equals(ap.getSsid())) {
                temp.setRssi(ap.getRssi());
                return ap;
            }
        }
        return null;
    }

    private AcessPoint isInSavedWifiList(AcessPoint ap){
        for (AcessPoint temp : savedWifiList ) {
            if(temp.getSsid().equals(ap.getSsid())) {
                temp.setRssi(ap.getRssi());
                return ap;
            }
        }
        return null;
    }

    private void removeEmptySsid(List<AcessPoint> aps){
        List<AcessPoint> tempWifiList = new ArrayList<AcessPoint>();
        for (AcessPoint ap : aps ) {
            if(TextUtils.isEmpty(ap.getSsid()))
                tempWifiList.add(ap);
        }
        aps.removeAll(tempWifiList);
    }

    private void clearWifiFromConnectedWifi(List<AcessPoint> aps){
        List<AcessPoint> tempWifiList = new ArrayList<AcessPoint>();
        for (AcessPoint ap : aps ) {
            for(AcessPoint connected : connectedWifiList){
                if(ap.getSsid().equals(connected.getSsid()))
                    tempWifiList.add(ap);
            }
        }
        aps.removeAll(tempWifiList);
    }

    private void clearWifiFromConnectedAndSavedWifi(List<AcessPoint> aps){
        List<AcessPoint> tempWifiList = new ArrayList<AcessPoint>();
        List<AcessPoint> mergedWifiList = new ArrayList<AcessPoint>();
        mergedWifiList.addAll(connectedWifiList);
        mergedWifiList.addAll(savedWifiList);
        for (AcessPoint ap : aps ) {
            for(AcessPoint connected : mergedWifiList){
                if(ap.getSsid().equals(connected.getSsid()))
                    tempWifiList.add(ap);
            }
        }
        aps.removeAll(tempWifiList);
    }

    private void updateAcessPointState(List<AcessPoint> aps, int status){
        String state = "";
        if(status == OnNoticeUI.NOTICE_TYPE_BT_MSG_CURRENT_AP){
            state = getResources().getString(R.string.status_wifi_connected);
        }
        else if(status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SAVED_AP){
            state = getResources().getString(R.string.status_wifi_saved);
        }

        if(aps != null && aps.size() > 0){
            for(int i=0; i < aps.size(); i++) {
                aps.get(i).setSsid(aps.get(i).getSsid().replace("\"", ""));
                aps.get(i).setState(state);
            }
        }
        removeEmptySsid(aps);
    }

    private void updateAcessPointState(AcessPoint ap, int status){
        String state = "";
        if(status == OnNoticeUI.NOTICE_TYPE_BT_MSG_CURRENT_AP){
            state = getResources().getString(R.string.status_wifi_connected);
        }
        else if(status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SAVED_AP){
            state = getResources().getString(R.string.status_wifi_saved);
        }
        if(ap != null){
            ap.setSsid(ap.getSsid().replace("\"", ""));
            ap.setState(state);
        }
    }

    private void updateWifiList(){
        myWifiList.clear();
        myWifiList.addAll(connectedWifiList);
        myWifiList.addAll(savedWifiList);
        myWifiList.addAll(searchedWifiList);
        adapter.notifyDataSetChanged();
    }

    private void AddAcessPoint(AcessPoint ap, int status) {
        if(ap == null || TextUtils.isEmpty(ap.getSsid()) || ap.getSsid().equals("<unknown ssid>")) return;
        if(!TextUtils.isEmpty(ap.getSsid()) && ap.getSsid().contains("\""))
            ap.setSsid(ap.getSsid().replace("\"", ""));
        if(status == OnNoticeUI.NOTICE_TYPE_BT_MSG_CURRENT_AP){
            if (!myWifiList.contains(ap)) {
//                return;
            }

            ap.setState(getResources().getString(R.string.status_wifi_connected));
//            ap.setState(getResources().getString(R.string.tip_scaning));
        }
        else if(status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SAVED_AP){

//            if (ap.getState().equals("connect"));
            if (!ap.getState().equals("已连接")) {
                ap.setState(getResources().getString(R.string.status_wifi_saved));
            }else{
                ap.setState(getResources().getString(R.string.status_wifi_connected));

            }
            if(TextUtils.isEmpty(ap.getPwd())){
//               ap.setState("");
            }
       }
//        else
//            ap.setState("");

        if(ap.getSsid().equals("mobstaz2")){
            int a = 0;
            a ++;
        }

        for(int i=0; i < myWifiList.size(); i++){
            AcessPoint temp = myWifiList.get(i);
//           / if (status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SAVED_AP){
//                if (ap.getSsid().equals(temp.getSsid())){
//                    myWifiList.remove(i);
//                }
//            }
            if(temp.getSsid().equals(ap.getSsid())){
                temp.setRssi(ap.getRssi());
                if(TextUtils.isEmpty(temp.getPwd()) && !TextUtils.isEmpty(ap.getPwd()))
                    temp.setPwd(ap.getPwd());
                //对于已连接的，传入已保存或已搜索的忽略
//                if(i == 0 && !TextUtils.isEmpty(temp.getState()) && temp.getState().equals(getResources().getString(R.string.status_wifi_connected)) && (status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SAVED_AP || status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SEARCH_AP)) {
                if(i == 0 && !TextUtils.isEmpty(temp.getState()) && temp.getState().equals(getResources().getString(R.string.status_wifi_connected)) && (status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SAVED_AP )&&ap.getState().equals("已连接")) {


                    Log.i("connect:",ap.getSsid()+temp.getSsid()+status+ap.getState());
                    adapter.notifyDataSetChanged();

                    return;
                }
                if(i == 0 && !TextUtils.isEmpty(temp.getState())  && (status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SEARCH_AP )&&temp.getState().equals(getResources().getString(R.string.status_wifi_connected)) ) {


                    adapter.notifyDataSetChanged();

                    return;
                }
                //对于已的保存的，传入已搜索的忽略
                if(!TextUtils.isEmpty(temp.getState()) && temp.getState().equals(getResources().getString(R.string.status_wifi_saved)) && status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SEARCH_AP&&ap.getState().equals("")) {

//                if(!TextUtils.isEmpty(temp.getState()) && temp.getState().equals(getResources().getString(R.string.status_wifi_saved)) && status == OnNoticeUI.NOTICE_TYPE_BT_MSG_SEARCH_AP) {
                    adapter.notifyDataSetChanged();
                    return;
                }

                temp.setState(ap.getState());
//               if(!TextUtils.isEmpty(temp.getState()) && temp.getState().equals(getResources().getString(R.string.tip_scaning))){

                if(!TextUtils.isEmpty(temp.getState()) && temp.getState().equals(getResources().getString(R.string.status_wifi_connected))){
                   myWifiList.remove(i);
                    myWifiList.add(0, temp);
                }
                adapter.notifyDataSetChanged();

                return;
            }
        }

        if(myWifiList.size() == 0) {
            myWifiList.add(ap);
        }
//        else if(ap.getState() != null && ap.getState().equals(getResources().getString(R.string.tip_scaning))||ap.getState().equals(getResources().getString(R.string.status_wifi_connected))){

        else if(ap.getState() != null && ap.getState().equals(getResources().getString(R.string.status_wifi_connected))){

                myWifiList.add(0, ap);

        }
        else if(ap.getState() != null && ap.getState().equals(getResources().getString(R.string.status_wifi_saved))){
            for(int i = 0; i < myWifiList.size(); i++){
                AcessPoint temp = myWifiList.get(i);
                if(temp.getState() != null && !temp.getState().equals(getResources().getString(R.string.status_wifi_connected))){

                    myWifiList.add(i, ap);
                    break;
                }
                if(i == myWifiList.size() - 1){
                    myWifiList.add(ap);
                }
            }
        }
        else{
            myWifiList.add(ap);
        }
        adapter.setData(myWifiList);
        adapter.notifyDataSetChanged();
    }

    public void clearWifiList(){
        myWifiList.clear();
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void clearSaveAndConnectedWifiList(){
        connectedWifiList.clear();
        savedWifiList.clear();
        updateWifiList();
    }

    @Override
    public void onItemClick(View view, final int position, final String ssid) {
        if(myWifiList == null || myWifiList.size() == 0) return;
        currentAp = null;
        final AcessPoint ap = myWifiList.get(position);
       //已连接的wifi
        if(ap != null && !TextUtils.isEmpty(ap.getState()) && ap.getState().equals(getResources().getString(R.string.status_wifi_connected))){
            new AlertDialog.Builder(mContext)
                    .setMessage(ap.getSsid())
                    .setNeutralButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .setPositiveButton(R.string.button_disconnect, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            disconnectFrom(ap.getSsid().replace("\"",""),ap.getPwd());
                            currentAp = ap;
                        }
                    })
                    .setNegativeButton(R.string.button_forget, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            forget(ap.getSsid().replace("\"",""),ap.getPwd());
                            myWifiList.remove(position);
                            adapter.notifyDataSetChanged();
                            ssisName = "";
                        }
                    }).show();
        }
        //已保存过的wifi
        else if(ap != null && !TextUtils.isEmpty(ap.getPwd()) && !TextUtils.isEmpty(ap.getState()) && ap.getState().equals(getResources().getString(R.string.status_wifi_saved))){
            new AlertDialog.Builder(mContext)
                    .setMessage(ap.getSsid())
                    .setNeutralButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(R.string.button_forget, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            forget(ap.getSsid().replace("\"",""),ap.getPwd());

//                            AcessPoint tmp =  myWifiList.get(position);
//                            tmp.setState("");
//                            myWifiList.remove(position);
//                            myWifiList.add(position,tmp);
                            myWifiList.remove(position);
                            adapter.notifyDataSetChanged();
                            ssisName = "";
//                           currentAp = ap;
                        }
                    })
                    .setPositiveButton(R.string.button_connect, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("currentap1111",ap.toString());
                            connectTo(ap.getSsid().replace("\"",""),ap.getPwd());
                            currentAp = ap;
                        }
                    }).show();
        }
        //未连接过的wifi
        else if(ap != null){
            final EditText et = new EditText(mContext);

            new AlertDialog.Builder(mContext)
                    .setMessage(getResources().getString(R.string.tip_input) + " " + ap.getSsid() + " " + getResources().getString(R.string.tip_password))
                    .setView(et)
                    .setPositiveButton(getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.button_connect), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String input = et.getText().toString();
                            if (TextUtils.isEmpty(input) || input.length() < 8) {
                                Toast.makeText(mContext, R.string.tip_password_at_least_8, Toast.LENGTH_LONG).show();
                            } else {
                                connectTo(ap.getSsid(),input);
                                currentAp = ap;
                            }
                        }
                    }).show();
        }
    }

    private void connectTo(String ssid, String pwd){
        if(TextUtils.isEmpty(ssid) || TextUtils.isEmpty(pwd)) return;
        BluetoothUtil.getInstance().sendMessage(buildCmdContent(ssid,pwd,TcpConfig.BT_CONNECT_TO_AP));
    }

    private void disconnectFrom(String ssid, String pwd){
        //if(TextUtils.isEmpty(ssid) || TextUtils.isEmpty(pwd)) return;
        if(TextUtils.isEmpty(pwd)) pwd = "";
        BluetoothUtil.getInstance().sendMessage(buildCmdContent(ssid,pwd,TcpConfig.BT_DISCONNECT_AP));
    }

    private void forget(String ssid, String pwd){
        //if(TextUtils.isEmpty(ssid) || TextUtils.isEmpty(pwd)) return;
        if(TextUtils.isEmpty(pwd)) pwd = "";
        BluetoothUtil.getInstance().sendMessage(buildCmdContent(ssid,pwd,TcpConfig.BT_FORGET_SAVED_AP));
    }

    private String buildCmdContent(String ssid, String pwd, String cmd){
        JSONObject temp = new JSONObject();
        JSONObject res = new JSONObject();
        try {
            temp.put("ssid",ssid);
            temp.put("pwd",pwd);
            res.put(TcpConfig.KEY, cmd);
            res.put(TcpConfig.VALUE, temp.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return res.toString();
    }

    private String getAcessPointState(String s){
        String res = getResources().getString(R.string.tip_connected_to);
        switch (s){
            case "SCANNING":
                res = getResources().getString(R.string.tip_scaning);
                break;
            case "DISCONNECTED":
                res = getResources().getString(R.string.tip_disconnected);
                break;
            case "ASSOCIATING":
                res = getResources().getString(R.string.tip_connecting);
                break;
            case "ASSOCIATED":
                res = getResources().getString(R.string.tip_get_ip_address);
                break;
            case "FOUR_WAY_HANDSHAKE":
                res = getResources().getString(R.string.tip_authenticating);
                break;
            case "GROUP_HANDSHAKE":
                res = getResources().getString(R.string.tip_authenticating);
                break;
            case "COMPLETED":
                res = getResources().getString(R.string.tip_connected_to);
                break;
        }
        return res;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, OtherWiFiActivity.class);
        startActivity(intent);

    }
}
