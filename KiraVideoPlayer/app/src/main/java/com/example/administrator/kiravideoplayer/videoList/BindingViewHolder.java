package com.example.administrator.kiravideoplayer.videoList;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/5/15.
 */

public class BindingViewHolder <T extends ViewDataBinding>
        extends RecyclerView.ViewHolder{

    private T mBinding;

    public BindingViewHolder(T binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public T getBinding(){
        return  mBinding;
    }
}
