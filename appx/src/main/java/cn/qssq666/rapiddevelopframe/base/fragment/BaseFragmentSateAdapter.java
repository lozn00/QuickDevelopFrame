package cn.qssq666.rapiddevelopframe.base.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by luozheng on 15/12/30.FragmentStatePagerAdapter
 * <p>
 * 这种 适配器如果纯fragmet的子fragment那么无法销毁 会反复创建 除非 activity销毁 。需要特殊手段 @see {@link .MyAnswerFragment}的 back()
 */

public class BaseFragmentSateAdapter extends FragmentStatePagerAdapter {
    //public class BaseFragmentSateAdapter extends FragmentPagerAdapter { 那么view的缓存机制用不上了
    private static final String TAG = "BaseFragmentSateAdapter";
    ArrayList<Fragment> mViewList;

    public BaseFragmentSateAdapter(FragmentManager fm) {
        super(fm);
        mViewList = new ArrayList<>();
    }

    public BaseFragmentSateAdapter(FragmentManager fm, ArrayList<Fragment> viewList) {
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

