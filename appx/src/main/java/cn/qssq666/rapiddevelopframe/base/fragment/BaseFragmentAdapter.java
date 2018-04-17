package cn.qssq666.rapiddevelopframe.base.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 2016-9-26 14:30:39
 * by luozheng 这种适用 与 fragment +fragment+viewpager  FragmentagerAdapter
 * <p>
 * BaseFragmentAdapter 就是 activity不销毁 那么 都缓存着 下次可以不需要创建fragment 直接 适用 这个。即时你new了一个fragment d但是fragment里面的view不能缓存
 * <p>
 * 但是 又发现 毛病 在首页replace child 的产生的fragment 那么 它销毁了 里面veiwpager 的FragmentPagerAdapter 维护的不会出现空白
 * 如果是添加到回退栈的(这里测试 是 子fragment回退栈) 然后通过pop弹出的包含viewpager的页面 那么在此进入包含这个viepagerd的页面
 * 里面的fragmetn则会出现解决方法就是不 缓存 已经创建的view.
 * 所以这个FragmentPagerAdapter用在首页 最合适了。首页的情形是 包含viewpgaer容器的fragment销毁了 viewpager的适配器维护的 可以再次复用 不会被销毁 。 也可以缓存view,因为 没有添加到回退栈？？？
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private static final String TAG = "BaseFragmentSateAdapter";
    ArrayList<Fragment> mViewList;

    public BaseFragmentAdapter(FragmentManager fm) {
        super(fm);
        mViewList = new ArrayList<>();
    }

    public BaseFragmentAdapter(FragmentManager fm, ArrayList<Fragment> viewList) {
        super(fm);
        setViewList(viewList);
    }


    public void setViewList(ArrayList<Fragment> viewList) {
        mViewList = viewList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mViewList.get(position);

        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i(TAG, "instantiateItem containerId:" + container + ",id:" + container.getId());
        return super.instantiateItem(container, position);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i(TAG, "destoryItem:" + position + "," + object);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mViewList == null ? 0 : mViewList.size();
    }
}

