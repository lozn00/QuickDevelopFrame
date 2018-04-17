package cn.qssq666.rapiddevelopframe.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ScrollView;

import cn.qssq666.rapiddevelopframe.BuildConfig;
import cn.qssq666.rapiddevelopframe.base.refresh.RereshLogicI;

/**
 * Created by qssq on 2017/12/5 qssq666@foxmail.com
 */

public class TopRereshUtils {


    private static final String TAG = "TopRereshUtils";

    public static void scrollTop(Fragment fragment) {
        if (!fragment.isAdded()) {
            return;
        } else if (fragment instanceof NestedScrollViewHolder) {
            NestedScrollViewHolder scrollViewHolder = (NestedScrollViewHolder) fragment;
            scrollViewHolder.getScrollView().scrollTo(0, 0);
        } else if (fragment instanceof ScrollViewHolder) {
            ScrollViewHolder scrollViewHolder = (ScrollViewHolder) fragment;
            scrollViewHolder.getScrollView().scrollTo(0, 0);
        } else if (fragment instanceof RereshLogicI) {
            RecyclerView recyclerView = ((RereshLogicI) fragment).getRecyclerView();
            if (recyclerView != null && recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() > 0) {
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(0);
                } else {
                    recyclerView.scrollToPosition(0);

                }
            }
        } else {
            Prt.e(TAG, "传递的界面不支持到顶定位,fragment:" + fragment);
        }
    }

    public static void scrollTopAndrefreshRefreshFragment(final Fragment fragment) {
        scrollTop(fragment);
        refreshRefreshFragment(fragment);

    }

    public static void refreshRefreshFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            return;
        } else if (fragment instanceof EnableRereshHolder) {

            ((EnableRereshHolder) fragment).doRefresh();

        } else if (fragment instanceof RereshLogicI) {
            ((RereshLogicI) fragment).queryDataRereshAndSetProgressUi();
        }
       /* else if (fragment instanceof BaseJSONRefreshFragmentN) {
            ((BaseJSONRefreshFragmentN) fragment).queryDataRereshAndSetProgressUi();
        } else if (fragment instanceof DataBindBaseJSONLazyRefreshFragment) {
            ((DataBindBaseJSONLazyRefreshFragment) fragment).queryDataRereshAndSetProgressUi();

        } */
        else {
            Log.w(TAG, "抱歉，不支持双击TAB刷新 " + fragment);
        }
    }


    public interface GestureActionListener {

        public int getCurrentItem();

        public void setCurrentItem(int index);

        public Adapter getAdapter();
    }

    public static GestureDetectorCompat addTopReresh(final Context context, final int index, View view, final GestureActionListener listener) {
        final GestureDetectorCompat detector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            //            GestureDetector detector = new GestureDetectorCompat(AppContext.getInstance(), new GestureDetector.SimpleOnGestureListener() {
//@返回TRUE如果事件被消耗，否则假
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //Prt.d(TAG, "onSingleTapUp");

                if (listener.getCurrentItem() != index) {
                    listener.setCurrentItem(index);
                    return true;
                } else {


                    if (listener.getAdapter() instanceof FragmentPagerAdapter) {
                        Fragment fragment = ((FragmentPagerAdapter) listener.getAdapter()).getItem(index);
                        if (fragment instanceof TopRereshUtils.ITabAction) {
                            ((TopRereshUtils.ITabAction) fragment).scrollToTop();
                            return true;
                        } else {
                            if (BuildConfig.DEBUG) {
                                ToastUtils.showToast("当前空间不支持到顶");

                            }
                        }
                    }
                    return false;
                }
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //Prt.d(TAG, "onLongPress");
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //Prt.d(TAG, "onScroll");
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //Prt.d(TAG, "onFling");
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                //Prt.d(TAG, "onShowPress");
                super.onShowPress(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                //Prt.d(TAG, "onDown");
                return super.onDown(e);

            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {


                if (listener.getAdapter() instanceof FragmentPagerAdapter) {
                    Fragment fragment = ((FragmentPagerAdapter) listener.getAdapter()).getItem(index);
                    if (fragment instanceof TopRereshUtils.ITabAction) {
                        ((TopRereshUtils.ITabAction) fragment).refresh();
                        return true;
                    } else {
                        if (BuildConfig.DEBUG) {
                            ToastUtils.showToast("当前空间不支持到顶刷新");

                        }
                    }
                }

                return super.onDoubleTap(e);
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                //Prt.d(TAG, "onDoubleTapEvent");
                return super.onDoubleTapEvent(e);

            }

            /**
             * 这个方法不同于onSingleTapUp，他是在GestureDetector确信用户在第一次触摸屏幕后，没有紧跟着第二次触摸屏幕，也就是不是“双击”的时候触发
             * @param e
             * @return
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //Prt.d(TAG, "onSingleTapConfirmed");
                //有点慢。虽然确定是单击了
                return true;
            }

            @Override
            public boolean onContextClick(MotionEvent e) {
                //Prt.d(TAG, "onContextClick");
                return super.onContextClick(e);
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {


            @Override


            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;

//                return super.onTouch(v,event);
            }
        });
        return detector;
    }

    public interface ITabAction {
        void scrollToTop();

        void refresh();
    }

    public interface RecyclerViewHolder {
        RecyclerView getRecyclerView();

    }

    public interface ScrollViewHolder {
        ScrollView getScrollView();

    }

    public interface NestedScrollViewHolder {
        NestedScrollView getScrollView();

    }

    public interface EnableRereshHolder {
        void doRefresh();

    }
}
