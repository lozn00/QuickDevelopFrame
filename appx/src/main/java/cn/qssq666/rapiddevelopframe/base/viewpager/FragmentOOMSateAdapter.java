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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import cn.qssq666.rapiddevelopframe.utils.Prt;

/**
 * create by luozheng 2017年8月10日 10:38:15
 * oom解决
 */
public class FragmentOOMSateAdapter<T> extends FragmentPagerAdapter {
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

    public FragmentOOMSateAdapter(FragmentManager fm, OnNeedReCreateListener onNeedReCreateListener) {
        super(fm);
        mViewList = new ArrayList<>();
        this.onNeedReCreateListener = onNeedReCreateListener;
    }


    @Override
    public Fragment getItem(int position) {
        MPair<T, SoftReference<Fragment>> pair = mViewList.get(position);
        SoftReference<Fragment> reference = pair.second;
        Fragment fragment = reference.get();
        if (fragment != null) {

            return fragment;
        } else {
            Prt.w(TAG, "内存不足 原来position:" + position + ",tag" + pair.first + "已被销毁尝试重新创建");
            fragment = this.onNeedReCreateListener.onReCrete(position, pair.first);
            pair.second = new SoftReference<Fragment>(fragment);
            return fragment;
        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Prt.i(TAG, "instantiateItem containerId:" + container + ",id:" + container.getId());
        return super.instantiateItem(container, position);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Prt.i(TAG, "destoryItem:" + position + "," + object);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mViewList == null ? 0 : mViewList.size();
    }
}

