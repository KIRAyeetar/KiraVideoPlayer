package com.example.administrator.kiravideoplayer.videoList.video;

import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrator.kiravideoplayer.R;
import com.example.administrator.kiravideoplayer.databinding.ItemVideoBinding;
import com.example.administrator.kiravideoplayer.videoList.BindingViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/15.
 */

public class Video_RC_Adapter<T extends ViewDataBinding>
        extends RecyclerView.Adapter<BindingViewHolder> {
    private ItemVideoBinding binding;

    private ArrayList<Video> mList = new ArrayList();
    private LayoutInflater layoutInflater = null;
    private Context context;
    private Video video;

    public Video_RC_Adapter(Context context, ArrayList list){
        this.context = context;
        this.mList = list;
    }
    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_video,parent,false);
        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, final int position) {
        binding = (ItemVideoBinding) holder.getBinding();
        video = mList.get(position);
        binding.setVideo(video);
        System.out.println(video.getHate());
        binding.executePendingBindings();
        binding.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(video.getVideoUrl());
                Toast.makeText(context, "视频链接复制成功", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
