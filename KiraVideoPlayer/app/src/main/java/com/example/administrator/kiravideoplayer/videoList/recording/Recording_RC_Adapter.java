package com.example.administrator.kiravideoplayer.videoList.recording;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.kiravideoplayer.R;
import com.example.administrator.kiravideoplayer.databinding.ItemRecordingBinding;
import com.example.administrator.kiravideoplayer.databinding.ItemVideoBinding;
import com.example.administrator.kiravideoplayer.videoList.BindingViewHolder;
import com.example.administrator.kiravideoplayer.videoList.ListSetter;
import com.example.administrator.kiravideoplayer.videoList.ListTools;
import com.example.administrator.kiravideoplayer.videoList.video.Video;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/21.
 */

public class Recording_RC_Adapter <T extends ViewDataBinding>
        extends RecyclerView.Adapter<BindingViewHolder> {
    private ItemRecordingBinding binding;

    private ArrayList<Recording> mList = new ArrayList();
    private LayoutInflater layoutInflater = null;
    private Context context;
    private Recording recording;

    public Recording_RC_Adapter(Context context, ArrayList list){
        this.context = context;
        this.mList = list;
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_recording,parent,false);
        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        binding = (ItemRecordingBinding) holder.getBinding();
        recording = mList.get(position);
        binding.setRecording(recording);
        binding.recordingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListTools.startPlay(recording.getVideoUrl(), recording.getPosition());
            }
        });
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
