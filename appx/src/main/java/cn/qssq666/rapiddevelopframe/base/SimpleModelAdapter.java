package cn.qssq666.rapiddevelopframe.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.qssq666.rapiddevelopframe.base.recyclerview.BaseRecyclervdapter;
import cn.qssq666.rapiddevelopframe.viewholder.GenericDataBindViewHolder;


/**
 * Created by qssq on 2017/10/31 qssq666@foxmail.com
 */

public abstract class SimpleModelAdapter<ML, VH extends ViewDataBinding> extends BaseRecyclervdapter<ML, GenericDataBindViewHolder<VH>> {
    @Override
    public GenericDataBindViewHolder onCreateViewHolderByExtend(ViewGroup parent, int viewType) {
        ViewDataBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutID(viewType), parent, false);
        return new GenericDataBindViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(GenericDataBindViewHolder holder, int position) {
        holder.getBinding().setVariable(getModelKey(), getData().get(position));
    }

    protected abstract  int getModelKey();
    public abstract int getLayoutID(int viewType);


}
