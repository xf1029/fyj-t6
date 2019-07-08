package com.a51tgt.t4m.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.a51tgt.t4m.MZApplication;
import com.a51tgt.t4m.R;
import com.a51tgt.t4m.abstract_face.OnNoticeUI;
import com.a51tgt.t4m.bean.UserDataUtils;
import com.google.gson.Gson;

public class WelcomeActivity extends BaseActivity {

   private ViewPager vp_pager;
    private ImageView iv_page0, iv_page1;
    private ImageView[] mPages = new ImageView[3];
    private ImageView[] mViews = new ImageView[3];
    private Button bt_enter;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mContext = WelcomeActivity.this;

        boolean isFirstEnter = UserDataUtils.getInstance(mContext).getFirstEnter();
        if(isFirstEnter == false){
            Intent intent = new Intent(mContext, SplashActivity.class);
            startActivity(intent);
            finish();
            return;
        }



        vp_pager = (ViewPager) findViewById(R.id.vp_pager);
        mPages[0] = (ImageView) findViewById(R.id.iv_page0);
        mPages[1] = (ImageView) findViewById(R.id.iv_page1);
        mPages[2] = (ImageView) findViewById(R.id.iv_page2);

        bt_enter = (Button) findViewById(R.id.bt_enter);

        mViews[0] = new ImageView(mContext);
        mViews[0].setImageResource(R.mipmap.welcome_1);
        mViews[1] = new ImageView(mContext);
        mViews[1].setImageResource(R.mipmap.welcome_2);
        mViews[2] = new ImageView(mContext);
        mViews[2].setImageResource(R.mipmap.welcome_3);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);//两个400分别为添加图片的大小
        mViews[0].setLayoutParams(params);
        mViews[1].setLayoutParams(params);
        mViews[2].setLayoutParams(params);

        mViews[0].setScaleType(ImageView.ScaleType.FIT_XY);
        mViews[1].setScaleType(ImageView.ScaleType.FIT_XY);
        mViews[2].setScaleType(ImageView.ScaleType.FIT_XY);

        ImagePagerAdapter mPagerAdapter = new ImagePagerAdapter();
        vp_pager.setAdapter(mPagerAdapter);

        vp_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mPages[0].setImageDrawable(getResources().getDrawable(R.mipmap.ic_viewpager_now));
                        mPages[1].setImageDrawable(getResources().getDrawable(R.mipmap.ic_viewpager));
                        mPages[2].setImageDrawable(getResources().getDrawable(R.mipmap.ic_viewpager));

                        bt_enter.setVisibility(View.GONE);
                        break;
                    case 1:
                        mPages[0].setImageDrawable(getResources().getDrawable(R.mipmap.ic_viewpager));
                        mPages[1].setImageDrawable(getResources().getDrawable(R.mipmap.ic_viewpager_now));
                        mPages[2].setImageDrawable(getResources().getDrawable(R.mipmap.ic_viewpager));

                        bt_enter.setVisibility(View.GONE);
                        break;
                    case 2:
                        mPages[0].setImageDrawable(getResources().getDrawable(R.mipmap.ic_viewpager));
                        mPages[1].setImageDrawable(getResources().getDrawable(R.mipmap.ic_viewpager));
                        mPages[2].setImageDrawable(getResources().getDrawable(R.mipmap.ic_viewpager_now));

                        bt_enter.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDataUtils.getInstance(mContext).setFirstEnter(false);
                Intent intent = new Intent(mContext, FirstScanActivity.class);
                startActivity(intent);
                finish();
            }
        });

}

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class ImagePagerAdapter extends PagerAdapter {

        ImagePagerAdapter() {
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            return mViews.length;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mViews[position]);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mViews[position]);
            return mViews[position];
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }
}
