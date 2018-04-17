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
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/3/13.
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (preventScroll) {
            return false;
        }
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {//这个崩溃 我也没办法
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (preventScroll) {
            return false;
        }
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public void setPreventScroll(boolean preventScroll) {
        this.preventScroll = preventScroll;
    }

    boolean preventScroll;
    /*
    Build version: 2.0
Build date: 1979-11-30 00:00:00
Current date: 2017-03-29 15:35:51
Device: HUAWEI FRD-AL10

Stack trace:
java.lang.IllegalArgumentException: pointerIndex out of range pointerIndex=-1 pointerCount=1
	at android.view.MotionEvent.nativeGetAxisValue(Native Method)
	at android.view.MotionEvent.getX(MotionEvent.java:2122)
	at android.support.v4.view.ViewPager.onInterceptTouchEvent(ViewPager.java:2092)
	at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2212)
	at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2671)
	at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2358)
	at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2671)
	at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2358)
	at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2671)
	at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2358)
	at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2671)
	at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2358)
	at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2671)
	at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2358)
	at android.view.ViewGroup.dispatchTransformedTouchEvent(ViewGroup.java:2671)
	at android.view.ViewGroup.dispatchTouchEvent(ViewGroup.java:2358)
	at com.android.internal.policy.DecorView.superDispatchTouchEvent(DecorView.java:447)
	at com.android.internal.policy.PhoneWindow.superDispatchTouchEvent(PhoneWindow.java:1871)
	at android.app.Activity.dispatchTouchEvent(Activity.java:3213)
	at android.support.v7.view.WindowCallbackWrapper.dispatchTouchEvent(WindowCallbackWrapper.java:71)
	at com.android.internal.policy.DecorView.dispatchTouchEvent(DecorView.java:409)
	at android.view.View.dispatchPointerEvent(View.java:10231)
	at android.view.ViewRootImpl$ViewPostImeInputStage.processPointerEvent(ViewRootImpl.java:4851)
	at android.view.ViewRootImpl$ViewPostImeInputStage.onProcess(ViewRootImpl.java:4711)
	at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:4244)
	at android.view.ViewRootImpl$InputStage.onDeliverToNext(ViewRootImpl.java:4297)
	at android.view.ViewRootImpl$InputStage.forward(ViewRootImpl.java:4263)
	at android.view.ViewRootImpl$AsyncInputStage.forward(ViewRootImpl.java:4390)
	at android.view.ViewRootImpl$InputStage.apply(ViewRootImpl.java:4271)
	at android.view.ViewRootImpl$AsyncInputStage.apply(ViewRootImpl.java:4447)
	at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:4244)
	at android.view.ViewRootImpl$InputStage.onDeliverToNext(ViewRootImpl.java:4297)
	at android.view.ViewRootImpl$InputStage.forward(ViewRootImpl.java:4263)
	at android.view.ViewRootImpl$InputStage.apply(ViewRootImpl.java:4271)
	at android.view.ViewRootImpl$InputStage.deliver(ViewRootImpl.java:4244)
	at android.view.ViewRootImpl.deliverInputEvent(ViewRootImpl.java:6671)
	at android.view.ViewRootImpl.doProcessInputEvents(ViewRootImpl.java:6645)
	at android.view.ViewRootImpl.enqueueInputEvent(ViewRootImpl.java:6606)
	at android.view.ViewRootImpl$WindowInputEventReceiver.onInputEvent(ViewRootImpl.java:6798)
	at android.view.InputEventReceiver.dispatchInputEvent(InputEventReceiver.java:192)
	at android.os.MessageQueue.nativePollOnce(Native Method)
	at android.os.MessageQueue.next(MessageQueue.java:330)
	at android.os.Looper.loop(Looper.java:138)
	at android.app.ActivityThread.main(ActivityThread.java:6524)
	at java.lang.reflect.Method.invoke(Native Method)
	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:941)
	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:831)

     */
}