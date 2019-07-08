package com.a51tgt.t4m.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.bean.AcessPoint;
import com.a51tgt.t4m.utils.CommUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ScanWifiAdapter extends RecyclerView.Adapter {

    private List<AcessPoint> data;
    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position, String ssid);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public ScanWifiAdapter(List<AcessPoint> data) {
        if (data == null) {
            this.data = new ArrayList<AcessPoint>();
        } else {
            this.data = data;
        }
    }

    public void setData(List<AcessPoint> data) {
        if (data == null) {
            this.data = new ArrayList<AcessPoint>();
        } else {
            this.data = data;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_wifi_list, null);
        final ItemViewHolder viewHolder = new ItemViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取position
                    mOnItemClickListener.onItemClick(v, (int) v.getTag(), viewHolder.GetTitle());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AcessPoint item = data.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.tvTitle.setText(item.getSsid());
        itemViewHolder.tvStatus.setText(item.getState());
        itemViewHolder.ivSignal.setImageResource(CommUtil.getSignalImage(item.getRssi()));
        itemViewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        else
            return data.size();
    }



    class ItemViewHolder extends RecyclerView.ViewHolder {

        public View convertView;
        public TextView tvTitle;
        public TextView tvStatus;
        public ImageView ivSignal;

        public ItemViewHolder(View convertView) {
            super(convertView);
            this.convertView = convertView;
            tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            ivSignal = (ImageView) convertView.findViewById(R.id.iv_signal);
        }

        public String GetTitle() {
            return tvTitle.getText().toString();
        }
    }

}
