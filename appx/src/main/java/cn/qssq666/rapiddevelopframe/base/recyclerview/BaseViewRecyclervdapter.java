package cn.qssq666.rapiddevelopframe.base.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import cn.qssq666.rapiddevelopframe.interfaces.IGet;
import cn.qssq666.rapiddevelopframe.interfaces.OnItemClickListener;
import cn.qssq666.rapiddevelopframe.interfaces.OnItemLongClickListener;

/**
 * recyclerview viewholder的数据绑定基类    只包含点击事件
 * 2016-3-30
 * by luozheng
 */
public abstract class BaseViewRecyclervdapter< VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final String TAG = "BaseRecyclervdapter";




    @Override
    public final VH onCreateViewHolder(final ViewGroup parent, int viewType) {
        final VH holder = onCreateViewHolderFix(parent, viewType);
        View clickView = holder.itemView;

        final View finalClickView = clickView;

        if (onItemClickListener != null) {

            if (getClickChildIds() != null) {
                Log.i(TAG, "position:" + holder.getAdapterPosition() + "," + holder.getPosition() + "," + holder.getPosition() + "," + holder.getLayoutPosition());
                setOnClickListeners(getClickChildIds(), holder.itemView, new IGet<Integer>() {
                    @Override
                    public Integer onGet() {
                        return holder.getAdapterPosition();
                    }
                }, onItemClickListener);
            } else {

                clickView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(parent, finalClickView, holder.getAdapterPosition());
                    }

                });
            }
        }

        if (onItemLongClickListener != null) {
            if (getClickChildIds() != null) {
                setOnLongClickListeners(getClickChildIds(), holder.itemView, new IGet<Integer>() {
                    @Override
                    public Integer onGet() {
                        //点击时候所发生的位置,只能用回调实现静态和非静态的取值了吗？final 的position永远是-1
                        Log.i(TAG, "position:" + holder.getAdapterPosition() + "," + holder.getPosition() + "," + holder.getPosition() + "," + holder.getLayoutPosition());
                        return holder.getAdapterPosition();
                    }
                }, onItemLongClickListener);
            } else {
                clickView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return onItemLongClickListener.onItemLongClick(parent, finalClickView, holder.getAdapterPosition());
                    }
                });
            }
        }
        return holder;
    }

    public static void setOnClickListeners(int[] id, final View viewParent, final IGet<Integer> iGet, final OnItemClickListener onItemClickListener) {
        for (int i : id) {
            final View viewById = viewParent.findViewById(i);
            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick((ViewGroup) viewParent, viewById, iGet.onGet());
                }
            });
        }
    }

    public static void setOnLongClickListeners(int[] id, final View viewParent, final IGet<Integer> iGet, final OnItemLongClickListener onItemLongClickListener) {
        for (int i : id) {
            final View viewById = viewParent.findViewById(i);
            viewById.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onItemLongClick((ViewGroup) viewParent, viewById, iGet.onGet());
                }
            });
        }
    }

    abstract public VH onCreateViewHolderFix(ViewGroup parent, int viewType);

    public int[] getClickChildIds() {
        return null;
    }

/*    */

    /**
     * 如果 getChildId也有 父亲是否需要呢需要也会默认设置点击事件
     *
     * @return
     *//*
    public boolean needChildParentClick() {
        return false;
    }*/
    @Override
    abstract public void onBindViewHolder(VH holder, int position);


    /**
     * 如果不复写 getClickIndex那么默认从 item 的根item设置点击事件,
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    OnItemLongClickListener onItemLongClickListener;
}
