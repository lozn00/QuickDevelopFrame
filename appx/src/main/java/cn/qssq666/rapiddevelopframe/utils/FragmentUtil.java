package cn.qssq666.rapiddevelopframe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.activity.FragmentContainerActivity;


/**
 * Created by LUOZHENG on 2016/3/17.
 */
public class FragmentUtil {

    /**
     * 蓝天背景，没有头部
     *
     * @param context
     */
    public static void toNewActivityFragmentPage(Context context, String className, String title, Bundle bundle) {
        Intent intent = new Intent(context, FragmentContainerActivity.class);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        intent.putExtra(FragmentContainerActivity.INTENT_TITLE, title);
        intent.putExtra(FragmentContainerActivity.INTENT_FRAGMENT_NAME, className);
        if (!(context instanceof Activity)) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//清空任务栈栈顶也就是把那个清空掉 然后跳转到付款界面. 但是这里必须把栈顶清空了
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);//清空任务栈栈顶也就是把那个清空掉 然后跳转到付款界面.
        }
        AppUtils.skipActivity(context, intent);
    }

    public static void toNewActivityFragmentPage(Context context, String className) {
        toNewActivityFragmentPage(context, className, null, null);
    }


    public static void toNewActivityFragmentPage(Context context, String className, Bundle bundle) {
        toNewActivityFragmentPage(context, className, null, bundle);
    }

    private static final String TAG = "FragmentUtil";

    public static void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass, int containerId) {
        replaceFragment(activity, fragmentClass, containerId, true);
    }

    public static void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass, boolean addBackStack) {
        replaceFragment(null, activity, fragmentClass, null, CONTAINER_ID_DEFAULT, null, addBackStack);
    }

    public static void replaceFragment(FragmentActivity activity, Fragment fragment, boolean addBackStack) {
        replaceFragment(null, activity, fragment, null, CONTAINER_ID_DEFAULT, null, addBackStack, false);
    }

    public static void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass, boolean addBackStack, boolean needAnim) {
        replaceFragment(null, activity, fragmentClass, null, CONTAINER_ID_DEFAULT, null, addBackStack, needAnim);
    }

    public static void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass, int containerId, boolean addBackStack) {
        replaceFragment(null, activity, fragmentClass, null, containerId, null, addBackStack);
    }

    /**
     * 跳转子fragment
     *
     * @param childFragment
     * @param activity
     * @param fragmentClass
     * @param containerId
     * @param addBackStack
     */
    public static void replaceChildFragment(Fragment childFragment, FragmentActivity activity, Class<? extends Fragment> fragmentClass, int containerId, boolean addBackStack) {
        replaceFragment(childFragment, activity, fragmentClass, null, containerId, null, addBackStack);
    }

    /**
     * 跳转子fragment
     *
     * @param fragment
     * @param activity
     * @param fragmentClass
     * @param addBackStack
     */
    public static void replaceChildFragment(Fragment fragment, FragmentActivity activity, Class<? extends Fragment> fragmentClass, boolean addBackStack) {
        replaceFragment(fragment, activity, fragmentClass, null, CONTAINER_ID_DEFAULT, null, addBackStack);
    }

    public static void replaceChildFragment(Fragment fragment, FragmentActivity activity, Class<? extends Fragment> fragmentClass, Bundle bundle, boolean addBackStack) {
        replaceFragment(fragment, activity, fragmentClass, bundle, CONTAINER_ID_DEFAULT, null, addBackStack);
    }

    public static void replaceChildFragment(Fragment currentFragment, FragmentActivity activity, Fragment fragment, Bundle bundle, boolean addBackStack) {
        replaceFragment(currentFragment, activity, fragment, bundle, CONTAINER_ID_DEFAULT, null, addBackStack, false);
    }

    public static void replaceChildFragment(Fragment currentFragment, FragmentActivity activity, Fragment fragment, boolean addBackStack) {
        replaceFragment(currentFragment, activity, fragment, null, CONTAINER_ID_DEFAULT, null, addBackStack, false);
    }

    public static void replaceChildFragment(Fragment currentFragment, FragmentActivity activity, Fragment fragment, Bundle bundle, int container_id, boolean addBackStack) {
        replaceFragment(currentFragment, activity, fragment, bundle, container_id, null, addBackStack, false);
    }

    public static void replaceChildFragment(Fragment currentFragment, FragmentActivity activity, Fragment fragment, int container_id, boolean addBackStack) {
        replaceFragment(currentFragment, activity, fragment, null, container_id, null, addBackStack, false);
    }

    public static void replaceChildFragment(Fragment currentFragment, FragmentActivity activity, Fragment fragment) {
        replaceFragment(currentFragment, activity, fragment, null, CONTAINER_ID_DEFAULT, null, true, false);
    }

    public static <T> void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass, String tag) {
        replaceFragment(activity, fragmentClass, null, CONTAINER_ID_DEFAULT, tag);
    }

    public static <T> void replaceFragment(FragmentActivity activity, Class<? extends Fragment> clazz, final Bundle bundle, int containerId) {
        replaceFragment(activity, clazz, bundle, containerId, null);
    }

    /**
     * 默认添加到回退栈
     *
     * @param activity
     * @param fragmentClass
     * @param bundle
     * @param <T>
     */
    public static <T> void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass, final Bundle bundle) {
        replaceFragment(activity, fragmentClass, bundle, CONTAINER_ID_DEFAULT, null);
    }


    public static <T> void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass, final Bundle bundle, boolean addBackStack) {
        replaceFragment(null, activity, fragmentClass, bundle, CONTAINER_ID_DEFAULT, null, addBackStack);
//        replaceFragment(activity, fragmentClass, bundle, CONTAINER_ID_DEFAULT, null);
    }

    public static <T> void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass, final Bundle bundle, boolean addBackStack, boolean needAnim) {
        replaceFragment(null, activity, fragmentClass, bundle, CONTAINER_ID_DEFAULT, null, addBackStack, needAnim);
//        replaceFragment(activity, fragmentClass, bundle, CONTAINER_ID_DEFAULT, null);
    }

    /**
     * 默认添加到回退栈
     *
     * @param activity
     * @param fragmentClass
     * @param <T>
     */
    public static <T> void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass) {
        replaceFragment(activity, fragmentClass, null, CONTAINER_ID_DEFAULT, null);
    }

    /**
     * 默认添加到回退栈
     *
     * @param activity
     * @param fragmentClass
     * @param bundle
     * @param containerId
     * @param tag
     * @param <T>
     */
    public static <T> void replaceFragment(FragmentActivity activity, Class<? extends Fragment> fragmentClass, final Bundle bundle, final int containerId, final String tag) {
        replaceFragment(null, activity, fragmentClass, bundle, containerId, tag, true);
    }

    /**
     * 此种方法比fragmentFragment 好用 无明显闪,但是这个针对回退真更实用一些 但是真正要一步一步网上返回就不能这么用了。
     *
     * @param currentFragment
     */
    public static void removeFragment(final Fragment currentFragment) {
        FragmentManager supportFragmentManager = currentFragment.getFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.remove(currentFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static <T> void replaceFragment(final Fragment currentFragment, final FragmentActivity activity, final Class<? extends Fragment> fragmentClass, final Bundle bundle, final int containerId, final String tag, final boolean addToBackStack) {
        replaceFragment(currentFragment, activity, fragmentClass, bundle, containerId, tag, addToBackStack, false);
    }

    /**
     * Verifier rejected class com.mm999.xingyunda.fragment.detail.XuanShangeDetailPayFragment due to bad method java.
     *
     * @param currentFragment
     * @param activity
     * @param fragmentClass
     * @param bundle
     * @param containerId
     * @param tag
     * @param addToBackStack
     * @param needAnim
     * @param <T>
     */

    public static <T> void replaceFragment(final Fragment currentFragment, final FragmentActivity activity, final Class<? extends Fragment> fragmentClass, final Bundle bundle, final int containerId, final String tag, final boolean addToBackStack, final boolean needAnim) {
        try {
            final Fragment instance = fragmentClass.newInstance();
            replaceFragment(currentFragment, activity, instance, bundle, containerId, tag, addToBackStack, needAnim);
        } catch (InstantiationException e) {
            //出现错误的原因可能是反射 后者 是抽象的
            e.printStackTrace();
            throw new RuntimeException("InstantiationException错误的原因可能是反射 后者 是抽象的" + e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> void replaceFragment(final Fragment currentFragment, final FragmentActivity activity, final Fragment fragment, final Bundle bundle, final int containerId, final String tag, final boolean addToBackStack, final boolean needAnim) {
        replaceFragment(currentFragment, activity, fragment, bundle, containerId, tag, null, addToBackStack, needAnim);
    }

    public static <T> void replaceFragment(final FragmentActivity activity, final Fragment fragment, final Bundle bundle, String backStackName, final boolean addToBackStack, final boolean needAnim) {
        replaceFragment(null, activity, fragment, bundle, CONTAINER_ID_DEFAULT, null, backStackName, addToBackStack, needAnim);
    }

    public static <T> void replaceFragment(final FragmentActivity activity, final Fragment fragment, String backStackName, final boolean addToBackStack, final boolean needAnim) {
        replaceFragment(null, activity, fragment, null, CONTAINER_ID_DEFAULT, null, backStackName, addToBackStack, needAnim);
    }

    public static <T> void replaceFragment(final FragmentActivity activity, final Fragment fragment, String backStackName, final boolean addToBackStack) {
        replaceFragment(null, activity, fragment, null, CONTAINER_ID_DEFAULT, null, backStackName, addToBackStack, false);
    }

    public static <T> void replaceFragment(final FragmentActivity activity, final Fragment fragment, String backStackName) {
        replaceFragment(null, activity, fragment, null, CONTAINER_ID_DEFAULT, null, backStackName, true, false);
    }

    public static <T> void replaceFragment(final FragmentActivity activity, final Class<? extends Fragment> fragmentClass, Bundle bundle, final String backStackName, final boolean addToBackStack) {
        try {
            final Fragment instance = fragmentClass.newInstance();
            replaceFragment(null, activity, instance, bundle, CONTAINER_ID_DEFAULT, null, backStackName, addToBackStack, false);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> void replaceFragment(final FragmentActivity activity, final Class<? extends Fragment> fragmentClass, final String backStackName, final boolean addToBackStack) {
        try {
            final Fragment instance = fragmentClass.newInstance();
            replaceFragment(null, activity, instance, null, CONTAINER_ID_DEFAULT, null, backStackName, addToBackStack, false);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param currentFragment 没填写说明不是自容器
     * @param activity        需要 必须填写
     * @param fragment        跳转目标
     * @param bundle          传递的参数
     * @param containerId     容器  @see FragmentUtil.CONTAINER_ID_DEFAULT
     * @param tag
     * @param backStackName
     * @param addToBackStack  是否添加到回退栈
     * @param needAnim
     * @param <T>
     */
    public static <T> void replaceFragment(final Fragment currentFragment, final FragmentActivity activity, final Fragment fragment, final Bundle bundle, final int containerId, final String tag, final String backStackName, final boolean addToBackStack, final boolean needAnim) {
/*        AppContext.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activity == null) {
                    Log.w(TAG, "Activity is Null");
                    return;
                }*/
        Bundle tempBundle = bundle;
        if (tempBundle != null) {
            fragment.setArguments(tempBundle);
        }
        FragmentManager supportFragmentManager;
        if (currentFragment != null) {
            supportFragmentManager = currentFragment.getChildFragmentManager();
        } else {
            supportFragmentManager = activity.getSupportFragmentManager();
        }
        if (fragment != null && supportFragmentManager != null) {
            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            if (needAnim) {

                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity == null || activity.isDestroyed()) {
                    return;
                }
            }
//                        transaction.setCustomAnimations(R.animator.fragment_enter_left, R.animator.fragment_exit_left);
            Log.i(TAG, "containerId:" + (containerId == CONTAINER_ID_DEFAULT ? R.id.fragment_space : containerId) + "");
            transaction.replace(containerId == CONTAINER_ID_DEFAULT ? R.id.fragment_space : containerId, fragment, tag);
            if (addToBackStack) {
                transaction.addToBackStack(backStackName);
            }
            transaction.commitAllowingStateLoss();
                        /*
                            Can not perform this action after onSaveInstanceState
                      java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                          at android.support.v4.app.FragmentManagerImpl.checkStateLoss(FragmentManager.java:1493)
                          at android.support.v4.app.FragmentManagerImpl.enqueueAction(FragmentManager.java:1511)
                          at android.support.v4.app.BackStackRecord.commitInternal(BackStackRecord.java:634)
                          at android.support.v4.app.BackStackRecord.commit(BackStackRecord.java:613)
                         */
        }
//            }
//        }, 50);
    }

    /**
     * 删除没有添加到回退栈的fragmnet
     *
     * @param fragment
     */
    public static void removeAddedFragment(Fragment fragment) {
        FragmentTransaction transaction = fragment.getFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();//沒有添加到回退戰的東西只能這麼刪除了？
    }

    /**
     * @param activity 如果设置，和名称或ID
     *                 一个已提供的后堆栈项，然后所有匹配项将
     *                 被消耗，直到一个不匹配的发现或底部
     *                 堆栈已达到。否则，所有条目最多，但不包括该项
     *                 将被删除。
     *                 删除所有 设置为null
     */
    public static void finishAllAddBackStack(FragmentActivity activity, String tag) {
        activity.getSupportFragmentManager().popBackStack(tag,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static final int CONTAINER_ID_DEFAULT = 0;

    /*
        public static void TEST(Class<? extends Fragment> clazz) {
            try {
                Fragment fragment = clazz.newInstance();

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }*/
    public static void finishFragment(FragmentActivity fragmentActivity, String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (fragmentActivity == null || fragmentActivity.isDestroyed()) {
                return;
            }
        }
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
//            AppUtils.showToast(mActivity, "哇哦,没得返回啦!");//和直播页形成了死循环。
            fragmentActivity.finish();
//            getFragmentActivity().onBackPressed();
            return;
        }
//        ((FragmentManagerImpl) fragmentManager)
//        fragmentManager.enqueueAction();//    java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);//    java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
    }

    public static Object productObject(String className) {
        try {
            Class aClass = Class.forName(className);
            return aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
