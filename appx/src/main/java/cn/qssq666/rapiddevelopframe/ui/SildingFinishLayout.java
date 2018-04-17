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

package cn.qssq666.rapiddevelopframe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by luozheng on 2017/5/23.  qssq.space
 */

public class SildingFinishLayout extends RelativeLayout implements View.OnTouchListener {
    /**
     * SildingFinishLayout布局的父布局
     */
    private ViewGroup mParentView;
    /**
     * 处理滑动逻辑的View
     */
    private View touchView;
    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 按下点的X坐标
     */
    private int downX;
    /**
     * 按下点的Y坐标
     */
    private int downY;
    /**
     * 临时存储X坐标
     */
    private int tempX;
    /**
     * 滑动类
     */
    private Scroller mScroller;
    /**
     * SildingFinishLayout的宽度
     */
    private int viewWidth;
    /**
     * 记录是否正在滑动
     */
    private boolean isSilding;

    private OnSildingFinishListener onSildingFinishListener;
    private boolean isFinish;


    public SildingFinishLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SildingFinishLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 获取SildingFinishLayout所在布局的父布局
            mParentView = (ViewGroup) this.getParent();
            viewWidth = this.getWidth();
        }
    }

    /**
     * 设置OnSildingFinishListener, 在onSildingFinish()方法中finish Activity
     *
     * @param onSildingFinishListener
     */
    public void setOnSildingFinishListener(
            OnSildingFinishListener onSildingFinishListener) {
        this.onSildingFinishListener = onSildingFinishListener;
    }

    /**
     * 设置Touch的View
     *
     * @param touchView
     */
    public void setTouchView(View touchView) {
        this.touchView = touchView;
        touchView.setOnTouchListener(this);
    }

    public View getTouchView() {
        return touchView;
    }

    /**
     * 滚动出界面
     */
    private void scrollRight() {
        final int delta = (viewWidth + mParentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta + 1, 0,
                Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int delta = mParentView.getScrollX();
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));
        postInvalidate();
    }

    /**
     * touch的View是否是AbsListView， 例如ListView, GridView等其子类
     *
     * @return
     */
    private boolean isTouchOnAbsListView() {
        return touchView instanceof AbsListView ? true : false;
    }

    /**
     * touch的view是否是ScrollView或者其子类
     *
     * @return
     */
    private boolean isTouchOnScrollView() {
        return touchView instanceof ScrollView ? true : false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int deltaX = tempX - moveX;
                tempX = moveX;
                if (Math.abs(moveX - downX) > mTouchSlop
                        && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                    isSilding = true;

                    // 若touchView是AbsListView，
                    // 则当手指滑动，取消item的点击事件，不然我们滑动也伴随着item点击事件的发生
                    if (isTouchOnAbsListView()) {
                        MotionEvent cancelEvent = MotionEvent.obtain(event);
                        cancelEvent
                                .setAction(MotionEvent.ACTION_CANCEL
                                        | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                        v.onTouchEvent(cancelEvent);
                    }

                }

                if (moveX - downX >= 0 && isSilding) {
                    mParentView.scrollBy(deltaX, 0);

                    // 屏蔽在滑动过程中ListView ScrollView等自己的滑动事件
                    if (isTouchOnScrollView() || isTouchOnAbsListView()) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isSilding = false;
                if (mParentView.getScrollX() <= -viewWidth / 2) {
                    isFinish = true;
                    scrollRight();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }

        // 假如touch的view是AbsListView或者ScrollView 我们处理完上面自己的逻辑之后
        // 再交给AbsListView, ScrollView自己处理其自己的逻辑
        if (isTouchOnScrollView() || isTouchOnAbsListView()) {
            return v.onTouchEvent(event);
        }

        // 其他的情况直接返回true
        return true;
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished()) {

                if (onSildingFinishListener != null && isFinish) {
                    onSildingFinishListener.onSildingFinish();
                }
            }
        }
    }


    public interface OnSildingFinishListener {
         void onSildingFinish();
    }

}
/*
    我们在onLayout()方法中利用getParent()方法获取该布局的父布局和获取其控件的宽度，主要是为之后的实现做准备工作。

        我们的滑动逻辑主要是利用View的scrollBy() 方法, scrollTo()方法和Scroller类来实现的，当手指拖动视图的时候，我们监听手指在屏幕上滑动的距离利用View的scrollBy() 方法使得View随着手指的滑动而滑动，而当手指离开屏幕，我们在根据逻辑使用Scroller类startScroll()方法设置滑动的参数，然后再根据View的scrollTo进行滚动。

        对于View的滑动，存在一些Touch事件消费的处理等问题，因此我们需要对View的整个Touch事件很熟悉 ,最主要的就是Activity里面有一些ListView、 GridView、ScrollView等控件了， 假如我们Activity里面存在ListView、GridView等控件的话，我们对Activity的最外层布局进行滚动根本就无效果，因为Touch事件被ListView、GridView等控件消费了，所以Activity的最外层布局根本得不到Touch事件，也就实现不了Touch逻辑了，所以为了解决此Touch事件问题我提供了setTouchView(View touchView) 方法，这个方法是将Touch事件动态的设置到到View上面，所以针对上面的问题，我们将OnTouchListener直接设置到ListView、GridView上面，这样子就避免了Activity的最外层接受不到Touch事件的问题了




        接下来看onTouch()方法

        首先我们在ACTION_DOWN记录按下点的X,Y坐标

        然后在ACTION_MOVE中判断，如果我们在水平方向滑动的距离大于mTouchSlop并且在竖直方向滑动的距离小于mTouchSlop，表示Activity处于滑动状态，我们判断如果touchView是ListView、GridView或者其子类的时候，因为我们手指在ListView、GridView上面，伴随着item的点击事件的发生，所以我们对touchView设置ACTION_CANCEL来取消item的点击事件，然后对该布局的父布局调用scrollBy()进行滚动，并且如果TouchView是AbsListView或者ScrollView直接返回true,来取消AbsListView或者ScrollView本身的ACTION_MOVE事件，最直观的感受就是我们在滑动Activity的时候，禁止AbsListView或者ScrollView的上下滑动

        最后在ACTION_UP中判断如果手指滑动的距离大于控件长度的二分之一，表示将Activity滑出界面，否则滑动到起始位置，我们利用Scroller类的startScroll()方法设置好开始位置，滑动距离和时间，然后调用postInvalidate()刷新界面，之后就到computeScroll()方法中，我们利用scrollTo()方法对该布局的父布局进行滚动，滚动结束之后，我们判断界面是否滑出界面，如果是就调用OnSildingFinishListener接口的onSildingFinish（）方法，所以只要在onSildingFinish()方法中finish界面就行了

        整个滑动布局的代码就是这个样子，接下来我们就来使用了，主界面Activity只有三个按钮，分别跳转到普通布局的Activity,有ListView的Activity和有ScrollView的Activity中


<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:orientation="vertical" >

<Button
        android:id="@+id/normal_activity"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="普通的Activity" />

<Button
        android:id="@+id/absListview_activity"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="有AbsListView的Activity" />

<Button
        android:id="@+id/scrollview_activity"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="有ScrollView的Activity" />

</LinearLayout>
        然后就是MainActivity的代码，根据ID实例化Button，然后为Button设置OnClickListener事件，不同的按钮跳转到不同的Activity，然后设置从右向左滑动的动画，重写onBackPressed()方法，当我们按下手机物理键盘的返回键，添加从左向右滑出的动画


        package com.example.slidingfinish;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.widget.Button;

        import com.example.slidingfinish.R;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButtonNormal = (Button) findViewById(R.id.normal_activity);
        mButtonNormal.setOnClickListener(this);

        Button mButtonAbs = (Button) findViewById(R.id.absListview_activity);
        mButtonAbs.setOnClickListener(this);

        Button mButtonScroll = (Button) findViewById(R.id.scrollview_activity);
        mButtonScroll.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent mIntent = null;
        switch (v.getId()) {
            case R.id.normal_activity:
                mIntent = new Intent(MainActivity.this, NormalActivity.class);
                break;
            case R.id.absListview_activity:
                mIntent = new Intent(MainActivity.this, AbsActivity.class);
                break;
            case R.id.scrollview_activity:
                mIntent = new Intent(MainActivity.this, ScrollActivity.class);
                break;
        }

        startActivity(mIntent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    //Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

}*/
/*
在这里我之贴出含有ListView的Activity的代码，先看布局，我们自定义滑动布局SildingFinishLayout应该放在XML的最顶层


<?xml version="1.0" encoding="UTF-8"?>
<com.example.view.SildingFinishLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/sildingFinishLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#556677" >

<ListView
        android:id="@+id/listView"
                android:cacheColorHint="@android:color/transparent"
                android:layout_width="match_parent"
                android:layout_height="match_parent" >
</ListView>


</com.example.view.SildingFinishLayout>



        package com.example.slidingfinish;

        import java.util.ArrayList;
        import java.util.List;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.view.Window;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import com.example.slidingfinish.R;
        import com.example.view.SildingFinishLayout;
        import com.example.view.SildingFinishLayout.OnSildingFinishListener;

public class AbsActivity extends Activity {
    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abslistview);

        for (int i = 0; i <= 30; i++) {
            list.add("测试数据" + i);
        }

        ListView mListView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                AbsActivity.this, android.R.layout.simple_list_item_1, list);
        mListView.setAdapter(adapter);

        SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
        mSildingFinishLayout
                .setOnSildingFinishListener(new OnSildingFinishListener() {

                    @Override
                    public void onSildingFinish() {
                        AbsActivity.this.finish();
                    }
                });

        // touchView要设置到ListView上面
        mSildingFinishLayout.setTouchView(mListView);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                startActivity(new Intent(AbsActivity.this, NormalActivity.class));
                overridePendingTransition(R.anim.base_slide_right_in,
                        R.anim.base_slide_remain);
            }
        });
    }

    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
*/

