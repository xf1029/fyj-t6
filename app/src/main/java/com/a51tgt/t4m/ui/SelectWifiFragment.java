package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.adapter.ScanWifiAdapter;
import com.a51tgt.t4m.bean.AcessPoint;
import com.a51tgt.t4m.net.WifiAdmin;

import java.util.HashMap;
import java.util.List;


public class SelectWifiFragment extends Fragment {

    private OnWifiConnectListener onWifiConnectListener;
    private static final String TAG = "SelectWifiFragment";
    private RecyclerView wifiRecyclerView;
    private ScanWifiAdapter adapter;
    List<AcessPoint> myWifiList;
    private WifiAdmin mWifiAdmin;

    public SelectWifiFragment() {
        // Required empty public constructor
    }

    public static SelectWifiFragment newInstance(List<AcessPoint> wifiList) {
        SelectWifiFragment fragment = new SelectWifiFragment();
        fragment.myWifiList = wifiList;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_wifi, container, false);
        wifiRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_wifi);
        wifiRecyclerView.setHasFixedSize(true);
        wifiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ScanWifiAdapter(myWifiList);
        wifiRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ScanWifiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String ssid) {
                AcessPoint scanResult = myWifiList.get(position);
                if(scanResult != null) {
//                    showInputWifiPassword(getActivity(), scanResult);
                    //connectWifi(scanResult, "");
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void connectWifi(final ScanResult scanResult, String password){
        mWifiAdmin = new WifiAdmin(getActivity()) {

            @Override
            public void myUnregisterReceiver(BroadcastReceiver receiver) {
                // TODO Auto-generated method stub
                getActivity().unregisterReceiver(receiver);
            }

            @Override
            public Intent myRegisterReceiver(BroadcastReceiver receiver,
                                             IntentFilter filter) {
                // TODO Auto-generated method stub
                getActivity().registerReceiver(receiver, filter);
                return null;
            }

            @Override
            public void onNotifyWifiConnected() {
                Log.d(TAG, "wifi connect success");
                if(onWifiConnectListener != null)
                    onWifiConnectListener.onWifiConnect(true);
            }

            @Override
            public void onNotifyWifiConnectFailed() {
                // TODO Auto-generated method stub
//                Log.d(TAG, "wifi connect failed");
//                showInputWifiPassword(getActivity(), scanResult);
//                if(onWifiConnectListener != null)
//                    onWifiConnectListener.onWifiConnect(false);
            }
        };

        mWifiAdmin.openWifi();
        mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo(scanResult, password));
    }

    @SuppressLint("InflateParams")
    private void showInputWifiPassword(Context context, final ScanResult scanResult) {
        final AlertDialog myDialog = new AlertDialog.Builder(context).create();
        myDialog.setView(LayoutInflater.from(context).inflate(R.layout.view_input_wifi_password, null));
        myDialog.show();
        myDialog.getWindow().setContentView(R.layout.view_input_wifi_password);
        final EditText et_password = (EditText) myDialog.getWindow().findViewById(R.id.et_password);
        TextView tv_password = (TextView) myDialog.getWindow().findViewById(R.id.tv_ssid);
        tv_password.setText(scanResult.SSID);

        myDialog.getWindow().findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        if(!TextUtils.isEmpty(et_password.getText()))
                            connectWifi(scanResult, et_password.getText().toString());
                    }
                });
        myDialog.getWindow().findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

    }

    public void setOnItemClickListener(OnWifiConnectListener listener) {
        this.onWifiConnectListener = listener;
    }

    //define interface
    public static interface OnWifiConnectListener {
        void onWifiConnect(boolean res);
    }

}
