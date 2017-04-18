package com.baijiahulian.live.ui.speakqueue;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseDialogFragment;
import com.baijiahulian.live.ui.utils.AliCloudImageUtil;
import com.baijiahulian.live.ui.utils.QueryPlus;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.models.imodels.IUserModel;
import com.squareup.picasso.Picasso;

/**
 * Created by Shubo on 2017/4/11.
 */

public class SpeakQueueDialogFragment extends BaseDialogFragment implements SpeakQueueContract.View {

    private SpeakQueueContract.Presenter presenter;
    private RecyclerView recyclerView;
    private SpeakQueueAdapter adapter;

    public static SpeakQueueDialogFragment newInstance() {
        Bundle args = new Bundle();

        SpeakQueueDialogFragment fragment = new SpeakQueueDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_speaker;
    }

    @Override
    protected void init(Bundle savedInstanceState, Bundle arguments) {
        QueryPlus $ = QueryPlus.with(contentView);
        recyclerView = (RecyclerView) $.id(R.id.dialog_speaker_rv).view();
        super.title(getString(R.string.live_speaker_dialog)).editable(false);
        adapter = new SpeakQueueAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(SpeakQueueContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void notifyItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItemDeleted(int position) {
        adapter.notifyItemRemoved(position);
    }

    private static class ApplyViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name;
        Button agree;
        Button disagree;

        ApplyViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.item_speaker_applying_avatar);
            name = (TextView) itemView.findViewById(R.id.item_speaker_applying_name);
            agree = (Button) itemView.findViewById(R.id.item_speaker_applying_agree);
            disagree = (Button) itemView.findViewById(R.id.item_speaker_applying_disagree);
        }
    }

    private static class SpeakViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar, cameraLabel;
        TextView name, videoLabel, openVideo, closeSpeak;

        SpeakViewHolder(View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.item_speaker_speaking_avatar);
            name = (TextView) itemView.findViewById(R.id.item_speaker_speaking_name);
            cameraLabel = (ImageView) itemView.findViewById(R.id.item_speaker_speaking_video_ic);
            videoLabel = (TextView) itemView.findViewById(R.id.item_speaker_speaking_video_label);
            openVideo = (TextView) itemView.findViewById(R.id.item_speaker_speaking_open_video);
            closeSpeak = (TextView) itemView.findViewById(R.id.item_speaker_speaking_close_speak);
        }
    }

    private class SpeakQueueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_TYPE_APPLY = 0;
        private static final int VIEW_TYPE_SPEAK = 1;

        @Override
        public int getItemViewType(int position) {
            return presenter.getItem(position) instanceof IUserModel ? VIEW_TYPE_APPLY : VIEW_TYPE_SPEAK;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_APPLY) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_speaker_applying, parent, false);
                return new ApplyViewHolder(view);
            } else if (viewType == VIEW_TYPE_SPEAK) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_speaker_speaking, parent, false);
                return new SpeakViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ApplyViewHolder) {
                IUserModel userModel = (IUserModel) presenter.getItem(position);
                ApplyViewHolder viewHolder = (ApplyViewHolder) holder;
                viewHolder.name.setText(userModel.getName());
                Picasso.with(getContext()).load(AliCloudImageUtil.getRoundedAvatarUrl(userModel.getAvatar(), 32)).into(viewHolder.avatar);
                viewHolder.agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.agreeSpeakApply(holder.getAdapterPosition());
                    }
                });
                viewHolder.disagree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.disagreeSpeakApply(holder.getAdapterPosition());
                    }
                });
            } else if (holder instanceof SpeakViewHolder) {
                IMediaModel mediaModel = (IMediaModel) presenter.getItem(position);
                final SpeakViewHolder viewHolder = (SpeakViewHolder) holder;
                viewHolder.name.setText(mediaModel.getUser().getName());
                Picasso.with(getContext()).load(AliCloudImageUtil.getRoundedAvatarUrl(mediaModel.getUser().getAvatar(), 32))
                        .into(viewHolder.avatar);
                viewHolder.cameraLabel.setVisibility(mediaModel.isVideoOn() ? View.VISIBLE : View.INVISIBLE);
                viewHolder.videoLabel.setVisibility(presenter.isCurrentVideoPlayingUser(position) ?
                        View.VISIBLE : View.INVISIBLE);
                viewHolder.closeSpeak.setVisibility(presenter.isTeacherOrAssistant() ? View.VISIBLE : View.GONE);
                viewHolder.closeSpeak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.closeSpeaking(holder.getAdapterPosition());
                    }
                });
                viewHolder.openVideo.setVisibility(mediaModel.isVideoOn() ? View.VISIBLE : View.INVISIBLE);
                viewHolder.openVideo.setText(presenter.isCurrentVideoPlayingUser(position) ?
                        getString(R.string.live_close_video) : getString(R.string.live_open_video));
                viewHolder.openVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (presenter.isCurrentVideoPlayingUser(holder.getAdapterPosition())) {
                            presenter.closeVideo(viewHolder.getAdapterPosition());
                        } else {
                            presenter.openVideo(viewHolder.getAdapterPosition());
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return presenter.getCount();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
