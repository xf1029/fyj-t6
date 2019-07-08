package com.a51tgt.t4m.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a51tgt.t4m.R;
import com.a51tgt.t4m.bean.FlowProductInfo;
import com.a51tgt.t4m.comm.APIConstants;
import com.a51tgt.t4m.utils.ImageUtils;
import com.a51tgt.t4m.utils.ImageViewUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FlowMallAdapter extends RecyclerView.Adapter {

  private List<FlowProductInfo> data;
  private OnItemClickListener mOnItemClickListener = null;
  private Context mContext;

  //define interface
  public static interface OnItemClickListener {
    void onItemClick(View view, int position, FlowProductInfo flowProductInfo);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  public FlowMallAdapter(Context context, List<FlowProductInfo> data) {
    mContext = context;
    this.data = data;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View itemView = inflater.inflate(R.layout.item_flow_product, null);
    final ItemViewHolder viewHolder = new ItemViewHolder(itemView);

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    final FlowProductInfo item = data.get(position);
    final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
    itemViewHolder.tv_title.setText(item.title);
    //itemViewHolder.tv_subtitle.setText(item.subTitle);
  Log.i("tatatatt",item.price.getClass().toString()) ;
//    double   d   =   Double.parseDouble(item.price);
//    int i = (int) d ;

//    int price=0;
//         price = Integer.parseInt(item.price);
//    String[] price = item.price.split(".");
    itemViewHolder.tv_price.setText(item.price+"("+item.priceType+")");
    itemViewHolder.tv_price_type.setText(item.moneyType);
    Picasso mPicasso = Picasso.with(mContext);
    mPicasso.setIndicatorsEnabled(false);
    mPicasso.load(APIConstants.server_host + item.coverImage)
            .into(itemViewHolder.iv_cover, new Callback() {
              @Override
              public void onSuccess() {
                Bitmap bitmap = ImageUtils.drawableToBitmap(itemViewHolder.iv_cover.getDrawable());
//                android.view.ViewGroup.LayoutParams para;
//                para = itemViewHolder.iv_cover.getLayoutParams();
//                float height = (float) para.width * ((float)bitmap.getHeight() / (float) bitmap.getWidth());
//                para.height = (int)height;
//                itemViewHolder.iv_cover.setLayoutParams(para);
                //ImageViewUtil.matchAll(mContext, itemViewHolder.iv_cover);


              }

              @Override
              public void onError() {

              }
            });

    itemViewHolder.itemView.setTag(position);

    itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnItemClickListener != null) {
          //注意这里使用getTag方法获取position
          mOnItemClickListener.onItemClick(v,(int)v.getTag(), data.get(position));
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    if(data == null)
      return 0;
    else
      return data.size();

  }

  class ItemViewHolder extends RecyclerView.ViewHolder {

    public View convertView;
    public TextView tv_title, tv_subtitle, tv_price, tv_price_type;
    public ImageView iv_cover;

    public ItemViewHolder(View convertView) {
      super(convertView);
      this.convertView = convertView;
      tv_title = (TextView) convertView.findViewById(R.id.tv_title);
      //tv_subtitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
      tv_price = (TextView) convertView.findViewById(R.id.tv_price);
      tv_price_type = (TextView) convertView.findViewById(R.id.tv_price_type);
      iv_cover = (ImageView) convertView.findViewById(R.id.iv_cover);
    }
  }

}
