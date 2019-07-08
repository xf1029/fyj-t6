package com.a51tgt.t4m.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
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
import android.widget.TextView;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.adapter.ScanWifiAdapter;
import com.a51tgt.t4m.bean.AcessPoint;
import com.a51tgt.t4m.net.WifiAdmin;

import java.util.List;


public class DeviceConnectWifiFragment extends Fragment {

    private static final String TAG = "SelectWifiFragment";
    private RecyclerView wifiRecyclerView;
    private ScanWifiAdapter adapter;
    List<AcessPoint> myWifiList;
    private WifiAdmin mWifiAdmin;

    public DeviceConnectWifiFragment() {
        // Required empty public constructor
    }

    public static DeviceConnectWifiFragment newInstance(List<AcessPoint> wifiList) {
        DeviceConnectWifiFragment fragment = new DeviceConnectWifiFragment();
        fragment.myWifiList = wifiList;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_connect_wifi, container, false);
        wifiRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_wifi);
        wifiRecyclerView.setHasFixedSize(true);
        wifiRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ScanWifiAdapter(myWifiList);
        wifiRecyclerView.setAdapter(adapter);

        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        adapter.setOnItemClickListener(new ScanWifiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String ssid) {
                AcessPoint scanResult = myWifiList.get(position);
                if(scanResult != null) {

                }
            }
        });

        wifiRecyclerView.setAdapter(adapter);

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




}
