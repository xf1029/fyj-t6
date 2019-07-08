package com.a51tgt.t4m.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.a51tgt.t4m.R;

public class BottomPopupWindow extends PopupWindow implements View.OnClickListener{

  private static final int CANCEL_VIEW_ID = R.id.tv_cancel;
  private static final int ROOT_VIEW_ID = R.id.root;
  private static final int BASE_VIEW_ID_FOR_OPTIONS_VIEW = ROOT_VIEW_ID + CANCEL_VIEW_ID;

  private Context mContext;
  private LayoutInflater mInflater;
  private LinearLayout optionsContent;
  private ScrollView optionsWrap;
  private OnOptionsSelectedListener listener;

  public BottomPopupWindow(Context context) {
    this(context, null);
  }

  public BottomPopupWindow(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BottomPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    init(context);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case ROOT_VIEW_ID:
      case CANCEL_VIEW_ID:
        dismiss();
        break;
      default:
        int selectedIndex = v.getId() - BASE_VIEW_ID_FOR_OPTIONS_VIEW;
        if(selectedIndex < 0) return;
        select(selectedIndex);
        break;
    }
  }

  private void init(Context context) {
    mInflater = LayoutInflater.from(context);
    View rootView = mInflater.inflate(R.layout.view_bottom_popup_window, null);
    optionsContent = (LinearLayout) rootView.findViewById(R.id.ll_options);
    optionsWrap = (ScrollView) rootView.findViewById(R.id.sc_wrap);
    rootView.setOnClickListener(this);
    rootView.findViewById(R.id.tv_cancel).setOnClickListener(this);
    setContentView(rootView);
    setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
    setHeight(FrameLayout.LayoutParams.MATCH_PARENT);
    setFocusable(true);
  }

  private void select(int index) {
    dismiss();
    if(listener == null) return;
    listener.onOptionSelected(index);
  }

  public void showOptions(View parentView, String[] options, OnOptionsSelectedListener optionsSelectedListener) {
    if(options == null || options.length == 0) return;
    this.listener = optionsSelectedListener;
    int optionHeight = mContext.getResources().getDimensionPixelSize(R.dimen.bottom_popup_window_option_height);
    int bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.separator_line_height);
    if(options.length > 5) {
      RelativeLayout.LayoutParams oldLayoutParams = (RelativeLayout.LayoutParams) optionsWrap.getLayoutParams();
      oldLayoutParams.height = (optionHeight + bottomMargin) * 6;
      optionsWrap.setLayoutParams(oldLayoutParams);
    }
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, optionHeight);
    params.bottomMargin = bottomMargin;
    for(int i=0; i<options.length; i++) {
      TextView textView = (TextView) mInflater.inflate(R.layout.item_popup_option, null);
      textView.setText(options[i]);
      textView.setId(BASE_VIEW_ID_FOR_OPTIONS_VIEW + i);
      textView.setOnClickListener(this);
      if(i == options.length) params.bottomMargin = 0;
      optionsContent.addView(textView, params);
    }
    showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
  }

  public interface OnOptionsSelectedListener {
    void onOptionSelected(int index);
  }

}
