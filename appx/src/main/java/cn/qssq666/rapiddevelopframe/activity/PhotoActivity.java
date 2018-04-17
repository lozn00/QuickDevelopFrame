/*
 *
 *                     .::::.
 *                   .::::::::.
 *                  :::::::::::  by qssq666@foxmail.com
 *              ..:::::::::::'
 *            '::::::::::::'
 *              .::::::::::
 *         '::::::::::::::..
 *              ..::::::::::::.
 *            ``::::::::::::::::
 *             ::::``:::::::::'        .:::.
 *            ::::'   ':::::'       .::::::::.
 *          .::::'      ::::     .:::::::'::::.
 *         .:::'       :::::  .:::::::::' ':::::.
 *        .::'        :::::.:::::::::'      ':::::.
 *       .::'         ::::::::::::::'         ``::::.
 *   ...:::           ::::::::::::'              ``::.
 *  ```` ':.          ':::::::::'                  ::::..
 *                     '.:::::'                    ':'````..
 *
 */

package cn.qssq666.rapiddevelopframe.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bm.library.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.base.activity.BaseActionBarActivity;
import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.utils.ImageUtil;


/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/3/16.
 */

public class PhotoActivity extends BaseActionBarActivity {

    private String[] strs;
    private boolean showPoint;
    private int selectPosition;
    private PagerAdapter adapter;
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    public static String INTENT_IMAGES = "INTENT_IMAGES";
    public static String INTENT_POSITION = "INTENT_POSITION";

    @Override
    protected void init(Bundle savedInstanceState) {
        strs = getIntent().getStringArrayExtra(PhotoActivity.INTENT_IMAGES);
        showPoint = strs == null || strs.length < 2 ? false : true;
        selectPosition = getIntent().getIntExtra(PhotoActivity.INTENT_POSITION, 0);
//        String url = getArguments().getString("url");
//        Prt.i(TAG, "url:" + url);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        adapter = new PagerAdapter() {


            @Override
            public int getCount() {
                return strs == null ? 0 : strs.length;
            }

            @Override
            public View instantiateItem(ViewGroup container, int position) {
                PhotoView photoView = new PhotoView(container.getContext());

                photoView.setBackgroundColor(container.getContext().getResources().getColor(android.R.color.black));
                photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                photoView.enable();
//                photoView.disableRotate();


                    ImageLoader.getInstance().displayImage(strs[position], photoView, ImageUtil.getOptions());

                container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                return photoView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
//        adapter.setItems(viewList);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                               @Override
                                               public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                               }

                                               @Override
                                               public void onPageSelected(int position) {
                                                   if (showPoint) {//
                                                       RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(position);
                                                       radioButton.setChecked(true);
                                                   }
                                               }

                                               @Override
                                               public void onPageScrollStateChanged(int state) {

                                               }
                                           }

        );
        if (showPoint) {
            mRadioGroup.setVisibility(View.VISIBLE);
            for (int i = 0; i < strs.length; i++) {
                View inflate = LayoutInflater.from(SuperAppContext.getInstance()).inflate(R.layout.view_radiobutton, mRadioGroup, false);
                mRadioGroup.addView(inflate);
            }
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int i = group.indexOfChild(group.findViewById(checkedId));
                    mViewPager.setCurrentItem(i);
                }
            });
            ((RadioButton) mRadioGroup.getChildAt(selectPosition)).setChecked(true);
        } else {
            mRadioGroup.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
//        ((PhotoView) ((ViewGroup) mViewPager.getChildAt(0)).getChildAt(0)).setBackgroundColor(0);
        super.onBackPressed();

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_photo_view;
    }

   /* @Override
    protected int getStatusBarColor() {
        return R.color.colorThemeBlack;
    }*/

    @Override
    protected String getHeadTitle() {
        return null;
    }
}
