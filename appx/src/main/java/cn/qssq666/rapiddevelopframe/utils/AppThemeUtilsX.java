package cn.qssq666.rapiddevelopframe.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import cn.qssq666.rapiddevelopframe.R;
import cn.qssq666.rapiddevelopframe.global.SuperAppContext;
import cn.qssq666.rapiddevelopframe.interfaces.INotify;
import cn.qssq666.rapiddevelopframe.ui.itemdivider.ListDividerItemDecoration;
import cn.qssq666.rapiddevelopframe.view.EqualDivierDecoration;

/**
 * Created by qssq on 2017/8/30 qssq666@foxmail.com
 */

public class AppThemeUtilsX {

    private static final String TAG = "AppThemeUtilsX";


    /**
     * 不包括顶部
     *
     * @param recyclerView
     */
    public static RecyclerView.ItemDecoration setGridLayoutDividerItem(RecyclerView recyclerView) {
        int spacingInPixels = SuperAppContext.getInstance().getResources().getDimensionPixelOffset(R.dimen.theme_margin_distance);
        EqualDivierDecoration decor = new EqualDivierDecoration(recyclerView, spacingInPixels);
        recyclerView.addItemDecoration(decor);
        return decor;
    }

    public static RecyclerView.ItemDecoration setGridLayoutDividerItem(RecyclerView recyclerView, boolean isNeedLeft, boolean isNeedRight) {
        return setGridLayoutDividerItem(recyclerView, isNeedLeft, isNeedRight, true);
    }

    public static RecyclerView.ItemDecoration setGridLayoutDividerItem(RecyclerView recyclerView, boolean isNeedLeft, boolean isNeedRight, boolean isNeedTop) {
        int spacingInPixels = SuperAppContext.getInstance().getResources().getDimensionPixelOffset(R.dimen.theme_margin_distance);
        return setGridLayoutDividerItem(recyclerView, isNeedLeft, isNeedRight, isNeedTop, spacingInPixels);
    }

    public static RecyclerView.ItemDecoration setGridLayoutDividerItem(RecyclerView recyclerView, boolean isNeedLeft, boolean isNeedRight, boolean isNeedTop, int size) {

        EqualDivierDecoration decor = new EqualDivierDecoration(recyclerView, size, isNeedLeft, isNeedRight, isNeedTop);
        recyclerView.addItemDecoration(decor);
        return decor;
    }

    public static void setLineLayoutDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new ListDividerItemDecoration(SuperAppContext.getInstance(), R.drawable.shape_divider));
    }


    public static Dialog generateTestDialog(final Activity activity, final INotify<String> notify) {
        final Dialog dialog = new Dialog(activity, R.style.dialog_transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        View root = LayoutInflater.from(SuperAppContext.getInstance()).inflate(R.layout.view_head, null, false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.windowAnimations = R.style.BottompSelectAnimationShowQuickly;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(root);//这样可以解决布局没有填充问题
       /* binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
        return dialog;
    }


    public static RecyclerView.LayoutManager getListItemLayoutManager() {

        return getListItemLayoutManager(LinearLayoutManager.VERTICAL);
    }

    public static LinearLayoutManager getListItemLayoutManager(int orienration) {

        return new LinearLayoutManager(SuperAppContext.getInstance(), orienration, false);
    }

    public static RecyclerView.LayoutManager getVideoLiveLayoutManager() {
        return new GridLayoutManager(SuperAppContext.getInstance(), 2, LinearLayoutManager.VERTICAL, false);
    }
    public static void setSwiperRefreshLayoutStyle(android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(SuperAppContext.getInstance().getResources().getColor(R.color.colorWhite));
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(SuperAppContext.getInstance(), R.color.colorThemeColor));
    }

    public static void setSwiperRefreshLayoutStyle(SmartRefreshLayout refreshLayout) {
        refreshLayout.setEnableHeaderTranslationContent(false);//内容不偏移
//        refreshLayout.setShowBezierWave(false);//关闭背景 根据颜色来获取。
        RefreshHeader refreshHeader = refreshLayout.getRefreshHeader();
        if (refreshHeader instanceof ClassicsHeader) {
            ClassicsHeader classicsHeader = (ClassicsHeader) refreshHeader;
            classicsHeader.setEnableLastTime(true);

            Drawable progressDrawable = classicsHeader.getProgressView().getDrawable();
            if (Build.VERSION.SDK_INT >= 21) {
//                progressDrawable.setTint(AppContext.getInstance().getResources().getColor(R.color.transparent));
            } else if (progressDrawable instanceof VectorDrawableCompat) {
//                ((VectorDrawableCompat) progressDrawable).setTint(AppContext.getInstance().getResources().getColor(R.color.transparent));
            }

        } else if (refreshHeader instanceof MaterialHeader) {
            MaterialHeader materialHeader = (MaterialHeader) refreshHeader;
//            materialHeader.setBackgroundColor(AppContext.getInstance().getResources().getColor(android.R.color.white));
            materialHeader.setColorSchemeColors(SuperAppContext.getInstance().getResources().getColor(R.color.colorThemeColor));
        }
        RefreshFooter refreshFooter = refreshLayout.getRefreshFooter();
        refreshLayout.setPrimaryColorsId(R.color.colorThemeColor);
    }

    /**
     * by zheng fix l c
     *
     * @param recyclerviewLike
     */
    public static void fixScrollBug(RecyclerView recyclerviewLike) {
        RecyclerView.LayoutManager manager = recyclerviewLike.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;

            linearLayoutManager.setSmoothScrollbarEnabled(true);
        } else if (manager instanceof GridLayoutManager) {

            GridLayoutManager layoutManager = (GridLayoutManager) manager;

            layoutManager.setSmoothScrollbarEnabled(true);
        } else if (manager instanceof StaggeredGridLayoutManager) {//不支持
//            layoutManager.setSmoothScrollbarEnabled(true);
        }


        manager.setAutoMeasureEnabled(true);
        recyclerviewLike.setHasFixedSize(true);
        recyclerviewLike.setNestedScrollingEnabled(false);
    }


    public static void setAndSaveCurrentThemeColor(int currentThemeColor) {

    /*    AppThemeUtils.currentThemeColor = currentThemeColor;
        int index = 0;
        index = findIndexByColor();
        cn.qssq666.systool.utils.SPUtils.setValue(AppContext.getInstance(), Cns.THEME_INDEX, index);*/
    }


/*
    public static int getCurrentThemeColorIndexFromLocal() {

    }*/
}
