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

package cn.qssq666.rapiddevelopframe.base.viewpager;

import android.support.v4.app.Fragment;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import cn.qssq666.rapiddevelopframe.utils.Prt;

/**
 * create by luozheng 2017年8月10日 10:38:15
 * oom解决
 */
public class FragmentOOMListManager<T> {
    //public class BaseFragmentSateAdapter extends FragmentPagerAdapter { 那么view的缓存机制用不上了
    private static final String TAG = "FragmentOOMSateAdapter";
    private final OnNeedReCreateListener onNeedReCreateListener;

    public List<MPair<T, SoftReference<Fragment>>> getViewList() {
        return mViewList;
    }

    public void clear() {
        mViewList.clear();
    }

    public void put(T tag, Fragment fragment) {
        mViewList.add(MPair.create(tag, new SoftReference<Fragment>(fragment)));
    }

    List<MPair<T, SoftReference<Fragment>>> mViewList;

    public interface OnNeedReCreateListener<T> {
        public Fragment onReCrete(int position, T tag);
    }

    public FragmentOOMListManager(OnNeedReCreateListener onNeedReCreateListener) {
        mViewList = new ArrayList<>();
        this.onNeedReCreateListener = onNeedReCreateListener;
    }


    public Fragment getItem(int position) {
        MPair<T, SoftReference<Fragment>> pair = mViewList.get(position);
        SoftReference<Fragment> reference = pair.second;
        Fragment fragment = reference.get();
        if (fragment != null) {

            return fragment;
        } else {
            Prt.i(TAG, "内存不足 已销毁了一个:" + position + "," + pair.first);
            fragment = this.onNeedReCreateListener.onReCrete(position, pair.first);
            pair.second = new SoftReference<Fragment>(fragment);
            return fragment;
        }

    }


    public int getCount() {
        return mViewList == null ? 0 : mViewList.size();
    }
}

