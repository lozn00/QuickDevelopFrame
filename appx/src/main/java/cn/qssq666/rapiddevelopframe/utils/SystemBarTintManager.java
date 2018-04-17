package cn.qssq666.rapiddevelopframe.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 2016-11-4 15:10:38
 * 支持 修改状态栏文字颜色
 */
public class SystemBarTintManager {
    /**
     * The default system bar tint color value.
     */
    public static final int DEFAULT_TINT_COLOR = 0x99000000;

    private final SystemBarConfig mConfig;
    private boolean mStatusBarAvailable;
    private boolean mNavBarAvailable;
    private boolean mStatusBarTintEnabled;
    private boolean mNavBarTintEnabled;
    private View mStatusBarTintView;
    private View mNavBarTintView;
    private static boolean sIsMiuiV6;
    private static String sNavBarOverride = null;


    static {
        Method methodGetter = null;
        try {
            Class<?> sysClass = Class.forName("android.os.SystemProperties");
            methodGetter = sysClass.getDeclaredMethod("get", String.class);
            sIsMiuiV6 = "V6".equals((String) methodGetter.invoke(sysClass, "ro.miui.ui.version.name"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (methodGetter != null) {
                try {
                    sNavBarOverride = (String) methodGetter.invoke(null, "qemu.hw.mainkeys");
                } catch (Exception e) {
                    e.printStackTrace();
                    sNavBarOverride = null;
                }
            }
        }
    }

    /**
     * @param activity The host activity. Error:Error: Expected resource of type styleable [ResourceType]  解决方法是:uppressWarnings("ResourceType")
     */
    @TargetApi(19)
    @SuppressWarnings("ResourceType")
    public SystemBarTintManager(Activity activity) {


        Window win = activity.getWindow();
        ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // check theme attrs
            int[] attrs = {android.R.attr.windowTranslucentStatus, android.R.attr.windowTranslucentNavigation};
            TypedArray a = activity.obtainStyledAttributes(attrs);
            try {
                mStatusBarAvailable = a.getBoolean(0, false);
                mNavBarAvailable = a.getBoolean(1, false);
            } finally {
                a.recycle();
            }

            // check window flags
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((winParams.flags & bits) != 0) {
                mStatusBarAvailable = true;
            }
            bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if ((winParams.flags & bits) != 0) {
                mNavBarAvailable = true;
            }
        }

        mConfig = new SystemBarConfig(activity, mStatusBarAvailable, mNavBarAvailable);
        // device might not have virtual navigation keys
        if (!mConfig.hasNavigtionBar()) {
            mNavBarAvailable = false;
        }

        if (mStatusBarAvailable) {
            setupStatusBarView(activity, decorViewGroup);
        }
        if (mNavBarAvailable) {
            setupNavBarView(activity, decorViewGroup);
        }

    }

    /**
     * Enable tinting of the system status bar.
     *
     * @param enabled True to enable tinting, false to disable it (default).
     */
    public void setStatusBarTintEnabled(boolean enabled) {
        mStatusBarTintEnabled = enabled;
        if (mStatusBarAvailable) {
            mStatusBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * set status bar darkmode
     *
     * @param darkmode
     * @param activity
     */
    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        if (sIsMiuiV6) {
            Class<? extends Window> clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Enable tinting of the system navigation bar.
     *
     * @param enabled True to enable tinting, false to disable it (default).
     */
    public void setNavigationBarTintEnabled(boolean enabled) {
        mNavBarTintEnabled = enabled;
        if (mNavBarAvailable) {
            mNavBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Apply the specified color tint to all system UI bars.
     *
     * @param color The color of the image tint.
     */
    public void setTintColor(int color) {
        setStatusBarTintColor(color);
        setNavigationBarTintColor(color);
    }

    /**
     * Apply the specified drawable or color resource to all system UI bars.
     *
     * @param res The identifier of the resource.
     */
    public void setTintResource(int res) {
        setStatusBarTintResource(res);
        setNavigationBarTintResource(res);
    }

    /**
     * Apply the specified drawable to all system UI bars.
     *
     * @param drawable The drawable to use as the image, or null to remove it.
     */
    public void setTintDrawable(Drawable drawable) {
        setStatusBarTintDrawable(drawable);
        setNavigationBarTintDrawable(drawable);
    }

    /**
     * Apply the specified alpha to all system UI bars.
     *
     * @param alpha The alpha to use
     */
    public void setTintAlpha(float alpha) {
        setStatusBarAlpha(alpha);
        setNavigationBarAlpha(alpha);
    }

    /**
     * Apply the specified color tint to the system status bar.
     *
     * @param color The color of the image tint.
     */
    public void setStatusBarTintColor(int color) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundColor(color);
        }
    }

    /**
     * Apply the specified drawable or color resource to the system status bar.
     *
     * @param res The identifier of the resource.
     */
    public void setStatusBarTintResource(int res) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundResource(res);
        }
    }

    /**
     * Apply the specified drawable to the system status bar.
     *
     * @param drawable The drawable to use as the image, or null to remove it.
     */
    @SuppressWarnings("deprecation")
    public void setStatusBarTintDrawable(Drawable drawable) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Apply the specified alpha to the system status bar.
     *
     * @param alpha The alpha to use
     */
    @TargetApi(11)
    public void setStatusBarAlpha(float alpha) {
        if (mStatusBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mStatusBarTintView.setAlpha(alpha);
        }
    }

    /**
     * Apply the specified color tint to the system navigation bar.
     *
     * @param color The color of the image tint.
     */
    public void setNavigationBarTintColor(int color) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundColor(color);
        }
    }

    /**
     * Apply the specified drawable or color resource to the system navigation bar.
     *
     * @param res The identifier of the resource.
     */
    public void setNavigationBarTintResource(int res) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundResource(res);
        }
    }

    /**
     * Apply the specified drawable to the system navigation bar.
     *
     * @param drawable The drawable to use as the image, or null to remove it.
     */
    @SuppressWarnings("deprecation")
    public void setNavigationBarTintDrawable(Drawable drawable) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Apply the specified alpha to the system navigation bar.
     *
     * @param alpha The alpha to use
     */
    @TargetApi(11)
    public void setNavigationBarAlpha(float alpha) {
        if (mNavBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mNavBarTintView.setAlpha(alpha);
        }
    }

    /**
     * Get the system bar configuration.
     *
     * @return The system bar configuration for the current device configuration.
     */
    public SystemBarConfig getConfig() {
        return mConfig;
    }

    /**
     * Is tinting enabled for the system status bar?
     *
     * @return True if enabled, False otherwise.
     */
    public boolean isStatusBarTintEnabled() {
        return mStatusBarTintEnabled;
    }

    /**
     * Is tinting enabled for the system navigation bar?
     *
     * @return True if enabled, False otherwise.
     */
    public boolean isNavBarTintEnabled() {
        return mNavBarTintEnabled;
    }

    private void setupStatusBarView(Context context, ViewGroup decorViewGroup) {
        mStatusBarTintView = new View(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mConfig.getStatusBarHeight());
        params.gravity = Gravity.TOP;
        if (mNavBarAvailable && !mConfig.isNavigationAtBottom()) {
            params.rightMargin = mConfig.getNavigationBarWidth();
        }
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mStatusBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mStatusBarTintView);
    }

    private void setupNavBarView(Context context, ViewGroup decorViewGroup) {
        mNavBarTintView = new View(context);
        FrameLayout.LayoutParams params;
        if (mConfig.isNavigationAtBottom()) {
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mConfig.getNavigationBarHeight());
            params.gravity = Gravity.BOTTOM;
        } else {
            params = new FrameLayout.LayoutParams(mConfig.getNavigationBarWidth(), FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;
        }
        mNavBarTintView.setLayoutParams(params);
        mNavBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mNavBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mNavBarTintView);
    }

    public static int getStatusBarHeight(Context instance) {
        int statusHeight = SystemBarTintManager.SystemBarConfig.getInternalDimensionSize(instance.getResources(), SystemBarTintManager.SystemBarConfig.STATUS_BAR_HEIGHT_RES_NAME);
        if (statusHeight == 0) {
            statusHeight = DensityUtil.dip2px(instance, 24);//1920 =72 由于setPadding无法使用智能用这个方法了
        }

        return statusHeight;
    }

    /**
     * Class which describes system bar sizing and other characteristics for the current
     * device configuration.
     */
    public static class SystemBarConfig {

        public static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
        public static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
        public static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
        public static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
        public static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";

        private final boolean mTranslucentStatusBar;
        private final boolean mTranslucentNavBar;
        private final int mStatusBarHeight;
        private final int mActionBarHeight;
        private final boolean mHasNavigationBar;
        private final int mNavigationBarHeight;
        private final int mNavigationBarWidth;
        private final boolean mInPortrait;
        private final float mSmallestWidthDp;

        private SystemBarConfig(Activity activity, boolean translucentStatusBar, boolean traslucentNavBar) {
            Resources res = activity.getResources();
            mInPortrait = (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
            mSmallestWidthDp = getSmallestWidthDp(activity);
            mStatusBarHeight = getInternalDimensionSize(res, STATUS_BAR_HEIGHT_RES_NAME);
            mActionBarHeight = getActionBarHeight(activity);
            mNavigationBarHeight = getNavigationBarHeight(activity);
            mNavigationBarWidth = getNavigationBarWidth(activity);
            mHasNavigationBar = (mNavigationBarHeight > 0);
            mTranslucentStatusBar = translucentStatusBar;
            mTranslucentNavBar = traslucentNavBar;
        }

        @TargetApi(14)
        private int getActionBarHeight(Context context) {
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                TypedValue tv = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
                result = context.getResources().getDimensionPixelSize(tv.resourceId);
            }
            return result;
        }

        @TargetApi(14)
        private int getNavigationBarHeight(Context context) {
            Resources res = context.getResources();
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (!hasNavBar(context)) {
                    String key;
                    if (mInPortrait) {
                        key = NAV_BAR_HEIGHT_RES_NAME;
                    } else {
                        key = NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
                    }
                    return getInternalDimensionSize(res, key);
                }
            }
            return result;
        }

        @TargetApi(14)
        private int getNavigationBarWidth(Context context) {
            Resources res = context.getResources();
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (!hasNavBar(context)) {
                    return getInternalDimensionSize(res, NAV_BAR_WIDTH_RES_NAME);
                }
            }
            return result;
        }

        public static int getInternalDimensionSize(Resources res, String key) {
            int result = 0;
            int resourceId = res.getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
            return result;
        }

        @SuppressLint("NewApi")
        private float getSmallestWidthDp(Activity activity) {
            DisplayMetrics metrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            } else {

                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            }
            float widthDp = metrics.widthPixels / metrics.density;
            float heightDp = metrics.heightPixels / metrics.density;
            return Math.min(widthDp, heightDp);
        }

        /**
         * Should a navigation bar appear at the bottom of the screen in the current
         * device configuration? A navigation bar may appear on the right side of
         * the screen in certain configurations.
         *
         * @return True if navigation should appear at the bottom of the screen, False otherwise.
         */
        public boolean isNavigationAtBottom() {
            return (mSmallestWidthDp >= 600 || mInPortrait);
        }

        /**
         * Get the height of the system status bar.
         *
         * @return The height of the status bar (in pixels).
         */
        public int getStatusBarHeight() {
            return mStatusBarHeight;
        }

        /**
         * Get the height of the action bar.
         *
         * @return The height of the action bar (in pixels).
         */
        public int getActionBarHeight() {
            return mActionBarHeight;
        }

        /**
         * Does this device have a system navigation bar?
         *
         * @return True if this device uses soft key navigation, False otherwise.
         */
        public boolean hasNavigtionBar() {
            return mHasNavigationBar;
        }

        /**
         * Get the height of the system navigation bar.
         *
         * @return The height of the navigation bar (in pixels). If the device does not have
         * soft navigation keys, this will always return 0.
         */
        public int getNavigationBarHeight() {
            return mNavigationBarHeight;
        }

        /**
         * Get the width of the system navigation bar when it is placed vertically on the screen.
         *
         * @return The width of the navigation bar (in pixels). If the device does not have
         * soft navigation keys, this will always return 0.
         */
        public int getNavigationBarWidth() {
            return mNavigationBarWidth;
        }

        /**
         * Get the layout inset for any system UI that appears at the top of the screen.
         *
         * @param withActionBar True to include the height of the action bar, False otherwise.
         * @return The layout inset (in pixels).
         */
        public int getPixelInsetTop(boolean withActionBar) {
            return (mTranslucentStatusBar ? mStatusBarHeight : 0) + (withActionBar ? mActionBarHeight : 0);
        }

        /**
         * Get the layout inset for any system UI that appears at the bottom of the screen.
         *
         * @return The layout inset (in pixels).
         */
        public int getPixelInsetBottom() {
            if (mTranslucentNavBar && isNavigationAtBottom()) {
                return mNavigationBarHeight;
            } else {
                return 0;
            }
        }

        /**
         * Get the layout inset for any system UI that appears at the right of the screen.
         *
         * @return The layout inset (in pixels).
         */
        public int getPixelInsetRight() {
            if (mTranslucentNavBar && !isNavigationAtBottom()) {
                return mNavigationBarWidth;
            } else {
                return 0;
            }
        }

        private boolean hasNavBar(Context context) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
            if (resourceId != 0) {
                boolean hasNav = res.getBoolean(resourceId);
                // check override flag (see static block)
                if ("1".equals(sNavBarOverride)) {
                    hasNav = false;
                } else if ("0".equals(sNavBarOverride)) {
                    hasNav = true;
                }
                return hasNav;
            } else { // fallback
                return !hasPermanentMenuKey(context);
            }
        }

        private boolean hasPermanentMenuKey(Context cxt) {
            try {
                WindowManager wm = (WindowManager) cxt.getSystemService(Context.WINDOW_SERVICE);

                ViewConfiguration config = ViewConfiguration.get(cxt);
                Field menuKeyField = ViewConfiguration.class
                        .getDeclaredField("sHasPermanentMenuKey");
                if (menuKeyField != null) {
                    menuKeyField.setAccessible(true);
                    return menuKeyField.getBoolean(config);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }


    }


    /**
     * 设置状态栏黑色字体图标， 调用我之后 需要       android:clipToPadding="true"
     * android:fitsSystemWindows="true" 或者在style里面设置也可以。
     * <p>
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int statusBarLightModeFontBlack(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    public static void statusBarLightModeFontBlack(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    public static void statusBarDarkMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
   /*     if (Build.VERSION.SDK_INT >= 23) {
            return false;
        }*/
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }
}