package com.baijiahulian.live.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;
import com.baijiahulian.live.ui.chat.ChatFragment;
import com.baijiahulian.live.ui.chat.ChatPresenter;
import com.baijiahulian.live.ui.leftmenu.LeftMenuFragment;
import com.baijiahulian.live.ui.leftmenu.LeftMenuPresenter;
import com.baijiahulian.live.ui.loading.LoadingFragment;
import com.baijiahulian.live.ui.loading.LoadingPresenter;
import com.baijiahulian.live.ui.ppt.MyPPTFragment;
import com.baijiahulian.live.ui.ppt.PPTPresenter;
import com.baijiahulian.live.ui.rightbotmenu.RightBottomMenuFragment;
import com.baijiahulian.live.ui.rightbotmenu.RightBottomMenuPresenter;
import com.baijiahulian.live.ui.rightmenu.RightMenuFragment;
import com.baijiahulian.live.ui.rightmenu.RightMenuPresenter;
import com.baijiahulian.live.ui.topbar.TopBarFragment;
import com.baijiahulian.live.ui.topbar.TopBarPresenter;
import com.baijiahulian.live.ui.videorecorder.RecorderFragment;
import com.baijiahulian.live.ui.videorecorder.RecorderPresenter;
import com.baijiahulian.livecore.LiveSDK;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LiveRoom;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

public class LiveRoomActivity extends LiveRoomBaseActivity implements LiveRoomRouterListener {

    @BindView(R.id.activity_live_room_background_container)
    FrameLayout flBackground;
    @BindView(R.id.activity_live_room_foreground_left_container)
    FrameLayout flForegroundLeft;
    @BindView(R.id.activity_live_room_foreground_right_container)
    FrameLayout flForegroundRight;
    @BindView(R.id.activity_live_room_chat)
    FrameLayout flChat;
    @BindView(R.id.activity_live_room_top)
    FrameLayout flTop;
    @BindView(R.id.activity_live_room_bottom_left)
    FrameLayout flLeft;
    @BindView(R.id.activity_live_room_right)
    FrameLayout flRight;
    @BindView(R.id.activity_live_room_bottom_right)
    FrameLayout flBottomRight;
    @BindView(R.id.activity_live_room_loading)
    FrameLayout flLoading;

    private LiveRoom liveRoom;

    private LoadingFragment loadingFragment;
    private TopBarFragment topBarFragment;

    private MyPPTFragment lppptFragment;
    private RecorderFragment recorderFragment;
    private ChatFragment chatFragment;
    private RightBottomMenuFragment rightBottomMenuFragment;
    private LeftMenuFragment leftMenuFragment;
    private RightMenuFragment rightMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);
        ButterKnife.bind(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onConfigurationChanged(getResources().getConfiguration());
        }

        String code = getIntent().getStringExtra("code");
        String name = getIntent().getStringExtra("name");

        LiveSDK.init(LPConstants.LPDeployType.Test);

        code = "6tlfmz";
        name = "Shubo";

        loadingFragment = new LoadingFragment();
        LoadingPresenter loadingPresenter = new LoadingPresenter(loadingFragment, code, name);
        loadingPresenter.setRouter(this);
        loadingFragment.setPresenter(loadingPresenter);
        addFragment(R.id.activity_live_room_loading, loadingFragment);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onBackgroundContainerConfigurationChanged(newConfig);
        onForegroundLeftContainerConfigurationChanged(newConfig);
        onForegroundRightContainerConfigurationChanged(newConfig);
    }

    private void onForegroundRightContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flForegroundRight.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_background_container);
        }
        flForegroundRight.setLayoutParams(lp);
    }

    private void onForegroundLeftContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flForegroundLeft.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.removeRule(RelativeLayout.BELOW);
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_background_container);
        }
        flForegroundLeft.setLayoutParams(lp);
    }

    private void onBackgroundContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flBackground.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.removeRule(RelativeLayout.ABOVE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.addRule(RelativeLayout.ABOVE, R.id.activity_live_room_center_anchor);
        }
        flBackground.setLayoutParams(lp);
    }

    @Override
    public LiveRoom getLiveRoom() {
        checkNotNull(liveRoom);
        return liveRoom;
    }

    @Override
    public void setLiveRoom(LiveRoom liveRoom) {
        this.liveRoom = liveRoom;
    }

    @Override
    public void navigateToMain() {

        lppptFragment = new MyPPTFragment();
        lppptFragment.setLiveRoom(getLiveRoom());
        bindVP(lppptFragment, new PPTPresenter(lppptFragment));
        addFragment(R.id.activity_live_room_background_container, lppptFragment);

        topBarFragment = new TopBarFragment();
        bindVP(topBarFragment, new TopBarPresenter(topBarFragment));
        addFragment(R.id.activity_live_room_top, topBarFragment);

        leftMenuFragment = new LeftMenuFragment();
        bindVP(leftMenuFragment, new LeftMenuPresenter(leftMenuFragment));
        addFragment(R.id.activity_live_room_bottom_left, leftMenuFragment);

        rightMenuFragment = new RightMenuFragment();
        bindVP(rightMenuFragment, new RightMenuPresenter(rightMenuFragment));
        addFragment(R.id.activity_live_room_right, rightMenuFragment);

        rightBottomMenuFragment = new RightBottomMenuFragment();
        bindVP(rightBottomMenuFragment, new RightBottomMenuPresenter(rightBottomMenuFragment));
        addFragment(R.id.activity_live_room_bottom_right, rightBottomMenuFragment);

        recorderFragment = new RecorderFragment();
        bindVP(recorderFragment, new RecorderPresenter(recorderFragment));
        addFragment(R.id.activity_live_room_foreground_left_container, recorderFragment);

        chatFragment = new ChatFragment();
        bindVP(chatFragment, new ChatPresenter(chatFragment));
        addFragment(R.id.activity_live_room_chat, chatFragment);

        // might delay 500ms to process
        removeFragment(loadingFragment);
        flLoading.setVisibility(View.GONE);
    }

    /*
    private void switchContainer(final ViewGroup toZoomOut, final ViewGroup toZoomIn) {
        if (isSwitchAnimationRunning) {
            return;
        }
        isSwitchAnimationRunning = true;
        Rect zoomOutRect = new Rect();
        toZoomOut.getGlobalVisibleRect(zoomOutRect);

        Rect zoomInRect = new Rect();
        toZoomIn.getGlobalVisibleRect(zoomInRect);

        RelativeLayout.LayoutParams lp = ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams());
        lp.removeRule(RelativeLayout.BELOW);
        lp.setMargins(lp.leftMargin, zoomOutRect.top - zoomInRect.top, lp.rightMargin, lp.bottomMargin);

        RelativeLayout.LayoutParams lp1 = ((RelativeLayout.LayoutParams) toZoomIn.getLayoutParams());
        lp1.removeRule(RelativeLayout.ABOVE);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(toZoomOut, "scaleX", 1f, (zoomInRect.right - zoomInRect.left) / (float) (zoomOutRect.right - zoomOutRect.left));
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(toZoomOut, "scaleY", 1f, (zoomInRect.bottom - zoomInRect.top) / (float) (zoomOutRect.bottom - zoomOutRect.top));
        ObjectAnimator translationX = ObjectAnimator.ofFloat(toZoomOut, "translationX", (zoomInRect.right - zoomInRect.left) / 2 - (zoomOutRect.right - zoomOutRect.left) / 2 - ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams()).leftMargin);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(toZoomOut, "translationY", (zoomInRect.bottom - zoomInRect.top) / 2 - (zoomOutRect.bottom - zoomOutRect.top) / 2 - ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams()).topMargin);

        ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(toZoomIn, "scaleX", 1f, (zoomOutRect.right - zoomOutRect.left) / (float) (zoomInRect.right - zoomInRect.left));
        ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(toZoomIn, "scaleY", 1f, (zoomOutRect.bottom - zoomOutRect.top) / (float) (zoomInRect.bottom - zoomInRect.top));
        ObjectAnimator translationX1 = ObjectAnimator.ofFloat(toZoomIn, "translationX", (zoomOutRect.right - zoomOutRect.left) / 2 - (zoomInRect.right - zoomInRect.left) / 2 + ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams()).leftMargin);
        ObjectAnimator translationY1 = ObjectAnimator.ofFloat(toZoomIn, "translationY", (zoomOutRect.bottom - zoomOutRect.top) / 2 - (zoomInRect.bottom - zoomInRect.top) / 2 + ((RelativeLayout.LayoutParams) toZoomOut.getLayoutParams()).topMargin);

        animatorSet.setDuration(2000);
        animatorSet.play(scaleX).with(scaleY).with(translationX).with(translationY)
                .with(scaleX1).with(scaleY1).with(translationX1).with(translationY1);
//        animatorSet.play(scaleX1).with(scaleY1).with(translationX1).with(translationY1);

        animatorSet.setInterpolator(new DecelerateInterpolator());

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isSwitchAnimationRunning = false;
                flBackground.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST);
                flBackground.forceLayout();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();

    }
    */

    @Override
    public void clearScreen() {
        chatFragment.clearScreen();
        rightBottomMenuFragment.clearScreen();
        hideFragment(topBarFragment);
        hideFragment(rightMenuFragment);
    }

    @Override
    public void unClearScreen() {
        chatFragment.unClearScreen();
        rightBottomMenuFragment.unClearScreen();
        showFragment(topBarFragment);
        showFragment(rightMenuFragment);
    }

    @Override
    public void navigateToMessageInput() {

    }

    @Override
    public void navigateToPPTDrawing() {
        checkNotNull(lppptFragment);
        lppptFragment.changePPTCanvasMode();
    }

    @Override
    public void navigateToSpeakers() {

    }

    @Override
    public void navigateToPPTWareHouse() {

    }

    @Override
    public void disableSpeakerMode() {

    }

    @Override
    public void enableSpeakerMode() {

    }

    @Override
    public void maximiseRecorderView() {
        if (recorderFragment.isMaximised()) return;
//        switchContainer(flForegroundLeft, getCurrentMaximisedView());
        lppptFragment.setIsDisplayMaximised(false);
        recorderFragment.setIsDisplayMaximised(true);
    }

    @Override
    public void maximisePlayerView() {
//        switchContainer(flForegroundRight, getCurrentMaximisedView());
    }

    public static void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    @Override
    public void maximisePPTView() {
        if (lppptFragment.isMaximised()) return;
//        sendViewToBack(flBackground);
//        switchContainer(flBackground, getCurrentMaximisedView());
        lppptFragment.setIsDisplayMaximised(true);
        recorderFragment.setIsDisplayMaximised(false);
//        flBackground.bringToFront();

    }

    private FrameLayout getCurrentMaximisedView() {
        if (lppptFragment.isMaximised()) {
            return flBackground;
        }
        if (recorderFragment != null && recorderFragment.isMaximised()) {
            return flForegroundLeft;
        }
//        flForegroundRight
//        if(recorderFragment!=null && recorderFragment.isMaximised()){
//            return flForegroundRight;
//        }
        return null;
    }

    private <V extends BaseView, P extends BasePresenter> void bindVP(V view, P presenter) {
        presenter.setRouter(this);
        view.setPresenter(presenter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLiveRoom().quitRoom();
    }
}
