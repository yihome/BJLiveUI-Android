package com.baijiahulian.live.ui.rightmenu;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Shubo on 2017/2/15.
 */

public class RightMenuFragment extends BaseFragment implements RightMenuContract.View {

    @BindView(R.id.fragment_right_speakers_img)
    ImageView fragmentRightSpeakersImg;
    @BindView(R.id.fragment_right_speakers_num)
    TextView fragmentRightSpeakersNum;
    @BindView(R.id.fragment_right_speakers_container)
    RelativeLayout fragmentRightSpeakersContainer;
    @BindView(R.id.fragment_right_pen)
    ImageView fragmentRightPen;
    @BindView(R.id.fragment_right_ppt)
    ImageView fragmentRightPpt;
    @BindView(R.id.fragment_right_speak_apply)
    ImageView fragmentRightSpeakApply;

    private RightMenuContract.Presenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_right_menu;
    }

    @Override
    public void showSpeakApplyImage(String imgUrl) {

    }

    @Override
    public void showSpeakApplyCount(int count) {

    }

    @Override
    public void showDrawingStatus(boolean isEnable) {

    }

    @Override
    public void showSpeakApplyCountDown(int countDownTime) {

    }

    @Override
    public void showSpeakApplyAgreed() {

    }

    @Override
    public void showSpeakApplyDisagreed() {

    }

    @Override
    public void showTeacherRightMenu() {
        fragmentRightSpeakApply.setVisibility(View.GONE);
    }

    @Override
    public void showStudentRightMenu() {
        fragmentRightPpt.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(RightMenuContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @OnClick({R.id.fragment_right_speakers_container, R.id.fragment_right_pen, R.id.fragment_right_ppt, R.id.fragment_right_speak_apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_right_speakers_container:
                break;
            case R.id.fragment_right_pen:
                presenter.drawing();
                break;
            case R.id.fragment_right_ppt:
                presenter.managePPT();
                break;
            case R.id.fragment_right_speak_apply:
                break;
        }
    }
}
