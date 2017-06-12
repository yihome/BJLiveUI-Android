package com.baijiahulian.live.ui.speakerspanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.utils.DisplayUtils;
import com.baijiahulian.live.ui.utils.QueryPlus;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.models.imodels.IUserModel;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;

import java.util.ArrayList;
import java.util.List;

import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_APPLY;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_PPT;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_RECORD;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_SPEAKER;
import static com.baijiahulian.live.ui.speakerspanel.SpeakersContract.VIEW_TYPE_VIDEO_PLAY;

/**
 * Created by Shubo on 2017/6/5.
 */

public class SpeakersFragment extends BaseFragment implements SpeakersContract.View {

    private SpeakersContract.Presenter presenter;
    private LinearLayout container;
    private RecorderView recorderView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_speakers;
    }

    @Override
    public void setPresenter(SpeakersContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        container = (LinearLayout) $.id(R.id.fragment_speakers_container).view();
    }

    @Override
    public void notifyItemChanged(int position) {
        if (presenter.getItemViewType(position) == VIEW_TYPE_SPEAKER) {
            container.removeViewAt(position);
            container.addView(getSpeakView(presenter.getSpeakModel(position)), position);
        }
    }

    @Override
    public void notifyItemInserted(int position) {
        switch (presenter.getItemViewType(position)) {
            case VIEW_TYPE_PPT:
                container.addView(presenter.getPPTView(), position);
                break;
            case VIEW_TYPE_RECORD:
                if (recorderView == null) {
                    recorderView = new RecorderView(getActivity());
                    presenter.getRecorder().setPreview(recorderView);
                }
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DisplayUtils.dip2px(getContext(), 100), DisplayUtils.dip2px(getContext(), 76));
                container.addView(recorderView, position, lp);
                // TODO: 2017/6/11 reconsider the lifecycle
                if (!presenter.getRecorder().isPublishing())
                    presenter.getRecorder().publish();
                if (!presenter.getRecorder().isVideoAttached())
                    presenter.getRecorder().attachVideo();
                break;
            case VIEW_TYPE_VIDEO_PLAY:
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                VideoView videoView = new VideoView(getActivity());
                container.addView(videoView, position, lp1);
                final IMediaModel model = presenter.getSpeakModel(position);
                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCloseVideoDialog(model);
                    }
                });
                presenter.getPlayer().playAVClose(presenter.getItem(position));
                presenter.getPlayer().playVideo(presenter.getItem(position), videoView.getSurfaceView());
                break;
            case VIEW_TYPE_SPEAKER:
                View view = getSpeakView(presenter.getSpeakModel(position));
                container.addView(view, position);
                break;
            case VIEW_TYPE_APPLY:
                View applyView = getApplyView(presenter.getApplyModel(position));
                container.addView(applyView, position);
                break;
            default:
                break;
        }
    }

    @Override
    public void notifyItemDeleted(int position) {
        container.removeViewAt(position);
        if(presenter.getItemViewType(position) == VIEW_TYPE_RECORD){
            presenter.getRecorder().detachVideo();
        }
    }

    private View getApplyView(final IUserModel model) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_speak_apply, container, false);
        QueryPlus q = QueryPlus.with(view);
        q.id(R.id.item_speak_apply_avatar).image(getActivity(), model.getAvatar());
        q.id(R.id.item_speak_apply_name).text(model.getName() + getContext().getString(R.string.live_media_speak_applying));
        q.id(R.id.item_speak_apply_agree).clicked().subscribe(new LPErrorPrintSubscriber<Void>() {
            @Override
            public void call(Void aVoid) {
                presenter.agreeSpeakApply(model.getUserId());
            }
        });
        q.id(R.id.item_speak_apply_disagree).clicked().subscribe(new LPErrorPrintSubscriber<Void>() {
            @Override
            public void call(Void aVoid) {
                presenter.disagreeSpeakApply(model.getUserId());
            }
        });
        return view;
    }

    private View getSpeakView(final IMediaModel model) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_speak_speaker, container, false);
        QueryPlus q = QueryPlus.with(view);
        q.id(R.id.item_speak_speaker_avatar).image(getActivity(), model.getUser().getAvatar());
        q.id(R.id.item_speak_speaker_name).text(model.getUser().getName());
        q.id(R.id.item_speak_speaker_video_label).visibility(model.isVideoOn() ? View.VISIBLE : View.GONE);
        q.contentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOpenVideoDialog(model);
            }
        });
        return view;
    }

    private void showOpenVideoDialog(final IMediaModel model) {
        List<String> options = new ArrayList<>();
        if (model.isVideoOn()) {
            options.add(getString(R.string.live_open_video));
        }
        if (presenter.isTeacherOrAssistant()) {
            options.add(getString(R.string.live_close_speaking));
        }
        if (options.size() <= 0) return;

        new MaterialDialog.Builder(getActivity())
                .items(options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        if (getString(R.string.live_open_video).equals(charSequence.toString())) {
                            presenter.playVideo(model.getUser().getUserId());
                        } else if (getString(R.string.live_close_speaking).equals(charSequence.toString())) {
                            presenter.closeSpeaking(model.getUser().getUserId());
                        }
                        materialDialog.dismiss();
                    }
                })
                .show();
    }

    private void showCloseVideoDialog(final IMediaModel model) {
        List<String> options = new ArrayList<>();
        if (model.isVideoOn()) {
            options.add(getString(R.string.live_close_video));
        }
        if (presenter.isTeacherOrAssistant()) {
            options.add(getString(R.string.live_close_speaking));
        }
        if (options.size() <= 0) return;

        new MaterialDialog.Builder(getActivity())
                .items(options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        if (getString(R.string.live_close_video).equals(charSequence.toString())) {
                            presenter.closeVideo(model.getUser().getUserId());
                        } else if (getString(R.string.live_close_speaking).equals(charSequence.toString())) {
                            presenter.closeSpeaking(model.getUser().getUserId());
                        }
                        materialDialog.dismiss();
                    }
                })
                .show();
    }

}
