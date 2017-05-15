package com.baijiahulian.live.ui.rightmenu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.utils.AliCloudImageUtil;
import com.baijiahulian.live.ui.viewsupport.CountdownCircleView;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Shubo on 2017/2/15.
 */

public class RightMenuFragment extends BaseFragment implements RightMenuContract.View {

    private RightMenuContract.Presenter presenter;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        $.id(R.id.fragment_right_pen).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeDrawing();
            }
        });
        $.id(R.id.fragment_right_ppt).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.managePPT();
            }
        });
        $.id(R.id.fragment_right_speakers_container).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.visitSpeakers();
            }
        });
        $.id(R.id.fragment_right_speak_apply).clicked().throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        presenter.speakApply();
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_right_menu;
    }

    @Override
    public void showSpeakQueueImage(String imgUrl) {
        $.id(R.id.fragment_right_speakers_img).visible();
        Picasso.with(getActivity()).load(AliCloudImageUtil.getRoundedAvatarUrl(imgUrl, 46))
                .into((ImageView) $.id(R.id.fragment_right_speakers_img).view());
    }

    @Override
    public void showSpeakQueueCount(int count) {
        if (count > 0) {
            $.id(R.id.fragment_right_speakers_num).visible();
            $.id(R.id.fragment_right_speakers_num).text(String.valueOf(count));
        } else {
            $.id(R.id.fragment_right_speakers_num).gone();
        }
    }

    @Override
    public void showEmptyQueue() {
        $.id(R.id.fragment_right_speakers_num).gone();
        $.id(R.id.fragment_right_speakers_img).gone();
    }

    @Override
    public void showDrawingStatus(boolean isEnable) {
        if (isEnable)
            $.id(R.id.fragment_right_pen).image(R.drawable.live_ic_lightpen_on);
        else
            $.id(R.id.fragment_right_pen).image(R.drawable.live_ic_lightpen);
    }

    @Override
    public void showSpeakApplyCountDown(int countDownTime) {
        $.id(R.id.fragment_right_hand_countdown).visible();
        ((CountdownCircleView) $.id(R.id.fragment_right_hand_countdown).view()).setRatio(countDownTime / 30000.0f);
        $.id(R.id.fragment_right_hand_countdown).view().invalidate();
    }

    @Override
    public void showSpeakApplyAgreed() {
        showToast(getString(R.string.live_media_speak_apply_agree));
        $.id(R.id.fragment_right_speak_apply).image(R.drawable.live_ic_handup_on);
        $.id(R.id.fragment_right_pen).visible();
        $.id(R.id.fragment_right_hand_countdown).invisible();
    }

    @Override
    public void showSpeakClosedByTeacher() {
        showToast(getString(R.string.live_media_speak_closed_by_teacher));
        $.id(R.id.fragment_right_speak_apply).image(R.drawable.live_ic_handup);
        $.id(R.id.fragment_right_pen).gone();
        $.id(R.id.fragment_right_hand_countdown).invisible();
    }

    @Override
    public void showSpeakApplyDisagreed() {
        $.id(R.id.fragment_right_speak_apply).enable(true);
        showToast(getString(R.string.live_media_speak_apply_disagree));
        $.id(R.id.fragment_right_speak_apply).image(R.drawable.live_ic_handup);
        $.id(R.id.fragment_right_hand_countdown).invisible();
    }

    @Override
    public void showSpeakApplyCanceled() {
        $.id(R.id.fragment_right_speak_apply).image(R.drawable.live_ic_handup);
        $.id(R.id.fragment_right_pen).gone();
        $.id(R.id.fragment_right_hand_countdown).invisible();
    }

    @Override
    public void showTeacherRightMenu() {
        $.id(R.id.fragment_right_pen).visible();
        $.id(R.id.fragment_right_ppt).visible();
        $.id(R.id.fragment_right_speak_wrapper).gone();
    }

    @Override
    public void showStudentRightMenu() {
        $.id(R.id.fragment_right_pen).gone();
        $.id(R.id.fragment_right_ppt).gone();
        $.id(R.id.fragment_right_speak_wrapper).visible();
    }

    @Override
    public void showForbiddenHand() {
        $.id(R.id.fragment_right_speak_apply).image(R.drawable.live_ic_handup_forbid);
        $.id(R.id.fragment_right_speak_apply).enable(false);
        $.id(R.id.fragment_right_hand_countdown).invisible();
    }

    @Override
    public void showNotForbiddenHand() {
        $.id(R.id.fragment_right_speak_apply).image(R.drawable.live_ic_handup);
        $.id(R.id.fragment_right_speak_apply).enable(true);
    }

    @Override
    public void hidePPTDrawBtn() {
        $.id(R.id.fragment_right_pen).gone();
    }

    @Override
    public void showPPTDrawBtn() {
        $.id(R.id.fragment_right_pen).visible();
    }

    @Override
    public void showHandUpError() {
        showToast(getString(R.string.live_hand_up_error));
    }

    @Override
    public void setPresenter(RightMenuContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }
}
