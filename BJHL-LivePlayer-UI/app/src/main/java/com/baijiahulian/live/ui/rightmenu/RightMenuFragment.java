package com.baijiahulian.live.ui.rightmenu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.utils.AliCloudImageUtil;
import com.squareup.picasso.Picasso;

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
        $.id(R.id.fragment_right_speak_apply).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.speakApply();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_right_menu;
    }

    @Override
    public void showSpeakApplyImage(String imgUrl) {
        $.id(R.id.fragment_right_speakers_img).visible();
        Picasso.with(getActivity()).load(AliCloudImageUtil.getRoundedAvatarUrl(imgUrl, 46))
                .into((ImageView) $.id(R.id.fragment_right_speakers_img).view());
    }

    @Override
    public void showSpeakApplyCount(int count) {
        if(count >0) {
            $.id(R.id.fragment_right_speakers_num).visible();
            $.id(R.id.fragment_right_speakers_num).text(String.valueOf(count));
        }else{
            $.id(R.id.fragment_right_speakers_num).gone();
        }
    }

    @Override
    public void showEmptySpeakers() {
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

    }

    @Override
    public void showSpeakApplyAgreed() {
        showToast(getString(R.string.live_media_speak_apply_agree));
    }

    @Override
    public void showSpeakApplyDisagreed() {
        showToast(getString(R.string.live_media_speak_apply_disagree));
    }

    @Override
    public void showTeacherRightMenu() {
        $.id(R.id.fragment_right_pen).visible();
        $.id(R.id.fragment_right_ppt).visible();
        $.id(R.id.fragment_right_speak_apply).gone();
    }

    @Override
    public void showStudentRightMenu() {
        $.id(R.id.fragment_right_pen).gone();
        $.id(R.id.fragment_right_ppt).gone();
        $.id(R.id.fragment_right_speak_apply).visible();
    }

    @Override
    public void setPresenter(RightMenuContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }
}
