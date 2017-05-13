package com.baijiahulian.live.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baijiahulian.live.ui.LiveSDKWithUI;
import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.announcement.AnnouncementFragment;
import com.baijiahulian.live.ui.announcement.AnnouncementPresenter;
import com.baijiahulian.live.ui.base.BasePresenter;
import com.baijiahulian.live.ui.base.BaseView;
import com.baijiahulian.live.ui.chat.ChatFragment;
import com.baijiahulian.live.ui.chat.ChatPresenter;
import com.baijiahulian.live.ui.chat.MessageSendPresenter;
import com.baijiahulian.live.ui.chat.MessageSentFragment;
import com.baijiahulian.live.ui.chat.preview.ChatPictureViewFragment;
import com.baijiahulian.live.ui.chat.preview.ChatPictureViewPresenter;
import com.baijiahulian.live.ui.chat.preview.ChatSavePicDialogFragment;
import com.baijiahulian.live.ui.chat.preview.ChatSavePicDialogPresenter;
import com.baijiahulian.live.ui.error.ErrorFragment;
import com.baijiahulian.live.ui.leftmenu.LeftMenuFragment;
import com.baijiahulian.live.ui.leftmenu.LeftMenuPresenter;
import com.baijiahulian.live.ui.loading.LoadingFragment;
import com.baijiahulian.live.ui.loading.LoadingPresenter;
import com.baijiahulian.live.ui.more.MoreMenuDialogFragment;
import com.baijiahulian.live.ui.more.MoreMenuPresenter;
import com.baijiahulian.live.ui.ppt.MyPPTFragment;
import com.baijiahulian.live.ui.ppt.PPTPresenter;
import com.baijiahulian.live.ui.pptdialog.PPTDialogFragment;
import com.baijiahulian.live.ui.pptdialog.PPTDialogPresenter;
import com.baijiahulian.live.ui.pptleftmenu.PPTLeftFragment;
import com.baijiahulian.live.ui.pptleftmenu.PPTLeftPresenter;
import com.baijiahulian.live.ui.pptmanage.PPTManageFragment;
import com.baijiahulian.live.ui.pptmanage.PPTManagePresenter;
import com.baijiahulian.live.ui.recorderdialog.RecorderDialogFragment;
import com.baijiahulian.live.ui.recorderdialog.RecorderDialogPresenter;
import com.baijiahulian.live.ui.remotevideodialog.RemoteVideoDialogFragment;
import com.baijiahulian.live.ui.remotevideodialog.RemoteVideoDialogPresenter;
import com.baijiahulian.live.ui.rightbotmenu.RightBottomMenuFragment;
import com.baijiahulian.live.ui.rightbotmenu.RightBottomMenuPresenter;
import com.baijiahulian.live.ui.rightmenu.RightMenuFragment;
import com.baijiahulian.live.ui.rightmenu.RightMenuPresenter;
import com.baijiahulian.live.ui.righttopmenu.RightTopMenuFragment;
import com.baijiahulian.live.ui.righttopmenu.RightTopMenuPresenter;
import com.baijiahulian.live.ui.setting.SettingDialogFragment;
import com.baijiahulian.live.ui.setting.SettingPresenter;
import com.baijiahulian.live.ui.share.LPShareDialog;
import com.baijiahulian.live.ui.speakqueue.SpeakQueueDialogFragment;
import com.baijiahulian.live.ui.speakqueue.SpeakQueuePresenter;
import com.baijiahulian.live.ui.topbar.TopBarFragment;
import com.baijiahulian.live.ui.topbar.TopBarPresenter;
import com.baijiahulian.live.ui.users.OnlineUserDialogFragment;
import com.baijiahulian.live.ui.users.OnlineUserPresenter;
import com.baijiahulian.live.ui.utils.RxUtils;
import com.baijiahulian.live.ui.videoplayer.VideoPlayerFragment;
import com.baijiahulian.live.ui.videoplayer.VideoPlayerPresenter;
import com.baijiahulian.live.ui.videorecorder.VideoRecorderFragment;
import com.baijiahulian.live.ui.videorecorder.VideoRecorderPresenter;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.context.LPError;
import com.baijiahulian.livecore.context.LiveRoom;
import com.baijiahulian.livecore.context.OnLiveRoomListener;
import com.baijiahulian.livecore.models.imodels.ILoginConflictModel;
import com.baijiahulian.livecore.models.imodels.IMediaModel;
import com.baijiahulian.livecore.utils.CrashHandler;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;
import com.baijiahulian.livecore.wrapper.exception.NotInitializedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.R.attr.path;
import static com.baijiahulian.live.ui.utils.Precondition.checkNotNull;

public class LiveRoomActivity extends LiveRoomBaseActivity implements LiveRoomRouterListener {

    private FrameLayout flBackground;
    private FrameLayout flForegroundLeft;
    private FrameLayout flForegroundRight;
    private FrameLayout flTop;
    private FrameLayout flLeft;
    private FrameLayout flLoading;
    private DrawerLayout dlChat;
    private LinearLayout llVideoContainer;
    private FrameLayout flTopRight;
    private FrameLayout flPPTLeft;

    private LiveRoom liveRoom;

    private LoadingFragment loadingFragment;
    private TopBarFragment topBarFragment;
    private RightTopMenuFragment rightTopMenuFragment;

    private MyPPTFragment lppptFragment;
    private VideoRecorderFragment recorderFragment;
    private ChatFragment chatFragment;
    private ChatPresenter chatPresenter;
    private RightBottomMenuFragment rightBottomMenuFragment;
    private LeftMenuFragment leftMenuFragment;
    private RightMenuFragment rightMenuFragment;
    private WindowManager windowManager;

    private VideoPlayerFragment playerFragment;
    private VideoPlayerPresenter playerPresenter;
    private RightMenuPresenter rightMenuPresenter;
    private PPTManageFragment pptManageFragment;
    private PPTManagePresenter pptManagePresenter;
    private ErrorFragment errorFragment;
    private GlobalPresenter globalPresenter;

    private OrientationEventListener orientationEventListener; //处理屏幕旋转时本地录制视频的方向
    private int oldRotation;

    private Subscription subscriptionOfLoginConflict;
    private IMediaModel currentRemoteMediaUser;
    private boolean isClearScreen;//是否已经清屏，作用于视频采集和远程视频ui的调整

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);

        initViews();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onConfigurationChanged(getResources().getConfiguration());
        }

        String code = getIntent().getStringExtra("code");
        String name = getIntent().getStringExtra("name");

        loadingFragment = new LoadingFragment();
        LoadingPresenter loadingPresenter = new LoadingPresenter(loadingFragment, code, name, false);
        bindVP(loadingFragment, loadingPresenter);
        addFragment(R.id.activity_live_room_loading, loadingFragment);

        windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        oldRotation = windowManager.getDefaultDisplay().getRotation();

        orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_FASTEST) {
            @Override
            public void onOrientationChanged(int orientation) {
                int newRotation = windowManager.getDefaultDisplay().getRotation();
                if (newRotation != oldRotation) {
                    oldRotation = newRotation;
                    try {
                        if (liveRoom.getRecorder().isVideoAttached())
                            liveRoom.getRecorder().invalidVideo();
                    } catch (NotInitializedException ignored) {
                    }

                }
            }
        };
        dlChat.openDrawer(Gravity.START);
        checkScreenOrientationInit();
        CrashHandler.getInstance().init(LiveRoomActivity.this);
    }

    private void initViews() {
        flBackground = (FrameLayout) findViewById(R.id.activity_live_room_background_container);
        flForegroundLeft = (FrameLayout) findViewById(R.id.activity_live_room_foreground_left_container);
        flForegroundRight = (FrameLayout) findViewById(R.id.activity_live_room_foreground_right_container);
        flTop = (FrameLayout) findViewById(R.id.activity_live_room_top);
        flLeft = (FrameLayout) findViewById(R.id.activity_live_room_bottom_left);
        flLoading = (FrameLayout) findViewById(R.id.activity_live_room_loading);
        dlChat = (DrawerLayout) findViewById(R.id.activity_live_room_chat_drawer);
        llVideoContainer = (LinearLayout) findViewById(R.id.activity_live_room_video_recorder_container);
        flTopRight = (FrameLayout) findViewById(R.id.activity_live_room_top_right);
        flPPTLeft = (FrameLayout) findViewById(R.id.activity_live_room_ppt_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationEventListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationEventListener.disable();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dlChat.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dlChat.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
        onBackgroundContainerConfigurationChanged(newConfig);
        onForegroundContainerConfigurationChanged(newConfig);
        onPPTLeftMenuConfigurationChanged(newConfig);
    }

    private void onForegroundContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) llVideoContainer.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (isClearScreen) {
                lp.addRule(RelativeLayout.BELOW, 0);
            } else {
                lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_background_container);
        }
        llVideoContainer.setLayoutParams(lp);
    }

    private void onBackgroundContainerConfigurationChanged(Configuration newConfig) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flBackground.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lp.addRule(RelativeLayout.ABOVE, 0); // lp.removeRule()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.addRule(RelativeLayout.ABOVE, R.id.activity_live_room_center_anchor);
        }
        flBackground.setLayoutParams(lp);
    }

    private void onPPTLeftMenuConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (lppptFragment != null && lppptFragment.isEditable()) {
                flLeft.setVisibility(View.GONE);
                dlChat.setVisibility(View.GONE);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            flLeft.setVisibility(View.VISIBLE);
            dlChat.setVisibility(View.VISIBLE);
        }
    }

    //录课中ui清屏调整
    private void onRecordFullScreenConfigurationChanged(boolean isClear) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) flTopRight.getLayoutParams();
        if (isClear) {
            lp.addRule(RelativeLayout.BELOW, 0);
        } else {
            lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
        }
        flTopRight.setLayoutParams(lp);
    }

    //视频采集和远程视频ui清屏调整,仅横屏
    private void onVideoFullScreenConfigurationChanged(boolean isClear) {
        if (getCurrentScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) llVideoContainer.getLayoutParams();
            if (isClear) {
                lp.addRule(RelativeLayout.BELOW, 0);
            } else {
                lp.addRule(RelativeLayout.BELOW, R.id.activity_live_room_top);
            }
            llVideoContainer.setLayoutParams(lp);
        }
    }

    @Override
    public LiveRoom getLiveRoom() {
        checkNotNull(liveRoom);
        return liveRoom;
    }

    @Override
    public void setLiveRoom(LiveRoom liveRoom) {
        this.liveRoom = liveRoom;
        liveRoom.setOnLiveRoomListener(new OnLiveRoomListener() {
            @Override
            public void onError(LPError error) {
                switch ((int) error.getCode()) {
                    case LPError.CODE_ERROR_ROOMSERVER_LOSE_CONNECTION:
                    case LPError.CODE_ERROR_NETWORK_FAILURE:
                        showNetError(error);
                        break;
                    case LPError.CODE_ERROR_LOGIN_CONFLICT:
                        showMessage("登录冲突");
                        break;
                    default:
                        if (!TextUtils.isEmpty(error.getMessage()))
                            showMessage(error.getMessage());
                        break;
                }
            }
        });
    }

    @Override
    public void showMessage(final String message) {
        Observable.empty().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object obj) {
                Toast toast = Toast.makeText(LiveRoomActivity.this, message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    @Override
    public void saveTeacherMediaStatus(IMediaModel model) {
        globalPresenter.setTeacherMedia(model);
    }

    private void showNetError(LPError error) {
        if (errorFragment != null) return;
        errorFragment = ErrorFragment.newInstance("好像断网了", error.getMessage());
        errorFragment.setRouterListener(this);
        flLoading.setVisibility(View.VISIBLE);
        addFragment(R.id.activity_live_room_loading, errorFragment);
    }

    @Override
    public void doReconnectServer() {
        removeFragment(errorFragment);
        errorFragment = null;

        loadingFragment = new LoadingFragment();
        LoadingPresenter loadingPresenter = new LoadingPresenter(loadingFragment, "", "", true);
        bindVP(loadingFragment, loadingPresenter);
        addFragment(R.id.activity_live_room_loading, loadingFragment);
    }

    @Override
    public void navigateToMain() {

        globalPresenter = new GlobalPresenter();
        globalPresenter.setRouter(this);
        globalPresenter.subscribe();

        lppptFragment = new MyPPTFragment();
        lppptFragment.setLiveRoom(getLiveRoom());
        bindVP(lppptFragment, new PPTPresenter(lppptFragment));
        addFragment(R.id.activity_live_room_background_container, lppptFragment);

        topBarFragment = new TopBarFragment();
        bindVP(topBarFragment, new TopBarPresenter(topBarFragment));
        addFragment(R.id.activity_live_room_top, topBarFragment);

        rightTopMenuFragment = new RightTopMenuFragment();
        bindVP(rightTopMenuFragment, new RightTopMenuPresenter());
        addFragment(R.id.activity_live_room_top_right, rightTopMenuFragment);

        leftMenuFragment = new LeftMenuFragment();
        bindVP(leftMenuFragment, new LeftMenuPresenter(leftMenuFragment));
        addFragment(R.id.activity_live_room_bottom_left, leftMenuFragment);

        PPTLeftFragment pptLeftFragment = new PPTLeftFragment();
        bindVP(pptLeftFragment, new PPTLeftPresenter(pptLeftFragment));
        addFragment(R.id.activity_live_room_ppt_left, pptLeftFragment);

        rightMenuFragment = new RightMenuFragment();
        rightMenuPresenter = new RightMenuPresenter(rightMenuFragment);
        bindVP(rightMenuFragment, rightMenuPresenter);
        addFragment(R.id.activity_live_room_right, rightMenuFragment);

        rightBottomMenuFragment = new RightBottomMenuFragment();
        bindVP(rightBottomMenuFragment, new RightBottomMenuPresenter(rightBottomMenuFragment));
        addFragment(R.id.activity_live_room_bottom_right, rightBottomMenuFragment);

        chatFragment = new ChatFragment();
        chatPresenter = new ChatPresenter(chatFragment);
        bindVP(chatFragment, chatPresenter);
        addFragment(R.id.activity_live_room_chat, chatFragment);

        subscriptionOfLoginConflict = getLiveRoom().getObservableOfLoginConflict().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<ILoginConflictModel>() {
                    @Override
                    public void call(ILoginConflictModel iLoginConflictModel) {
                        LiveRoomActivity.super.finish();
                    }
                });

        if (getLiveRoom().getCurrentUser().getType() == LPConstants.LPUserType.Teacher) {
            liveRoom.requestClassStart();
        }

        if (shareListener != null) {
            shareListener.getShareData(this, liveRoom.getRoomId());
        }

        // might delay 500ms to process
        Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Long>() {
                    @Override
                    public void call(Long aLong) {
                        removeFragment(loadingFragment);
                        flLoading.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void clearScreen() {
        chatFragment.clearScreen();
        isClearScreen = true;
//        dlChat.setVisibility(View.GONE);
        rightBottomMenuFragment.clearScreen();
        hideFragment(topBarFragment);
        hideFragment(rightMenuFragment);
        onRecordFullScreenConfigurationChanged(true);
        onVideoFullScreenConfigurationChanged(true);
    }

    @Override
    public void unClearScreen() {
        chatFragment.unClearScreen();
        isClearScreen = false;
        rightBottomMenuFragment.unClearScreen();
        showFragment(topBarFragment);
        showFragment(rightMenuFragment);
        onRecordFullScreenConfigurationChanged(false);
        onVideoFullScreenConfigurationChanged(false);
        dlChat.openDrawer(Gravity.START);
    }

    @Override
    public void navigateToMessageInput() {
        MessageSentFragment fragment = MessageSentFragment.newInstance();
        MessageSendPresenter presenter = new MessageSendPresenter(fragment);
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    @Override
    public void navigateToPPTDrawing() {
        checkNotNull(lppptFragment);
        lppptFragment.changePPTCanvasMode();

        int currentOrientation = getResources().getConfiguration().orientation;
        if (lppptFragment.isEditable()) {
            flTop.setVisibility(View.GONE);
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                checkNotNull(leftMenuFragment);
                flLeft.setVisibility(View.GONE);
                dlChat.setVisibility(View.GONE);
            }
            flPPTLeft.setVisibility(View.VISIBLE);
        } else {
            flTop.setVisibility(View.VISIBLE);
            flLeft.setVisibility(View.VISIBLE);
            dlChat.setVisibility(View.VISIBLE);
            dlChat.openDrawer(Gravity.START);
            flPPTLeft.setVisibility(View.GONE);
        }
    }

    @Override
    public LPConstants.LPPPTShowWay getPPTShowType() {
        return lppptFragment.getPPTShowWay();
    }

    @Override
    public void setPPTShowType(LPConstants.LPPPTShowWay type) {
        lppptFragment.setPPTShowWay(type);
    }

    @Override
    public void navigateToSpeakers() {
        SpeakQueueDialogFragment fragment = SpeakQueueDialogFragment.newInstance();
        SpeakQueuePresenter presenter = new SpeakQueuePresenter(fragment);
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    @Override
    public void navigateToUserList() {
        OnlineUserDialogFragment userListFragment = OnlineUserDialogFragment.newInstance();
        OnlineUserPresenter userPresenter = new OnlineUserPresenter(userListFragment);
        bindVP(userListFragment, userPresenter);
        showDialogFragment(userListFragment);
    }

    @Override
    public void navigateToPPTWareHouse() {
        pptManageFragment = PPTManageFragment.newInstance();
        if (pptManagePresenter == null) {
            pptManagePresenter = new PPTManagePresenter();
            pptManagePresenter.setRouter(this);
            pptManagePresenter.subscribe();
        }
        pptManageFragment.setPresenter(pptManagePresenter);
        showDialogFragment(pptManageFragment);
    }

    @Override
    public void disableSpeakerMode() {
        rightBottomMenuFragment.disableSpeakerMode();
        if (recorderFragment != null) {
            detachVideo();
        }
        if (getLiveRoom().getRecorder().isPublishing())
            getLiveRoom().getRecorder().stopPublishing();
    }

    @Override
    public void enableSpeakerMode() {
        rightBottomMenuFragment.enableSpeakerMode();
    }

    @Override
    public void maximiseRecorderView() {
        View max = flBackground.getChildAt(0);
        if (recorderFragment.getView() == max) return;
        boolean isPPT = max == lppptFragment.getView();
        if (isPPT)
            lppptFragment.onPause();
        switchView(recorderFragment.getView(), max);
        if (isPPT)
            lppptFragment.onResume();
        liveRoom.getRecorder().invalidVideo();

        if (lppptFragment != null && lppptFragment.isEditable()) {
            rightMenuPresenter.changeDrawing();
        }
        rightMenuPresenter.changePPTDrawBtnStatus(false);
    }

    @Override
    public void maximisePlayerView() {
        View max = flBackground.getChildAt(0);
        if (playerFragment.getView() == max) return;
        boolean isPPT = max == lppptFragment.getView();
        if (isPPT)
            lppptFragment.onPause();
        switchView(playerFragment.getView(), max);
        if (isPPT)
            lppptFragment.onResume();
        liveRoom.getRecorder().invalidVideo();

        if (lppptFragment != null && lppptFragment.isEditable()) {
            rightMenuPresenter.changeDrawing();
        }
        rightMenuPresenter.changePPTDrawBtnStatus(false);
    }

    @Override
    public void maximisePPTView() {
        View max = flBackground.getChildAt(0);
        if (lppptFragment.getView() == max) return;
        lppptFragment.onPause();
        switchView(lppptFragment.getView(), max);
        lppptFragment.onResume();
        liveRoom.getRecorder().invalidVideo();
        rightMenuPresenter.changePPTDrawBtnStatus(true);
    }

    @Override
    public void showMorePanel(int anchorX, int anchorY) {
        MoreMenuDialogFragment fragment = MoreMenuDialogFragment.newInstance(anchorX, anchorY);
        MoreMenuPresenter presenter = new MoreMenuPresenter(fragment);
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    @Override
    public void navigateToShare() {
        LPShareDialog shareDialog = LPShareDialog.newInstance(shareListener.setShareList());
        shareDialog.setListener(new LPShareDialog.LPShareClickListener() {
            @Override
            public void onShareClick(int type) {
                shareListener.onShareClicked(LiveRoomActivity.this, type);
            }
        });
        showDialogFragment(shareDialog);
    }

    @Override
    public void navigateToAnnouncement() {
        AnnouncementFragment fragment = AnnouncementFragment.newInstance();
        AnnouncementPresenter presenter = new AnnouncementPresenter(fragment);
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    /**
     * @param recordStatus 当前的状态 1:正在录制 0:未录制
     */
    @Override
    public void navigateToCloudRecord(boolean recordStatus) {
        if (recordStatus) {
            flTopRight.setVisibility(View.VISIBLE);
            showFragment(rightTopMenuFragment);
        } else {
            flTopRight.setVisibility(View.GONE);
            hideFragment(rightTopMenuFragment);
        }
    }

    @Override
    public void navigateToHelp() {

    }

    @Override
    public void navigateToSetting() {
        SettingDialogFragment settingFragment = SettingDialogFragment.newInstance();
        SettingPresenter settingPresenter = new SettingPresenter(settingFragment);
        bindVP(settingFragment, settingPresenter);
        showDialogFragment(settingFragment);
    }

    @Override
    public boolean isTeacherOrAssistant() {
        return getLiveRoom().getCurrentUser().getType() == LPConstants.LPUserType.Teacher ||
                getLiveRoom().getCurrentUser().getType() == LPConstants.LPUserType.Assistant;
    }

    @Override
    public String getCurrentVideoPlayingUserId() {
        if (currentRemoteMediaUser == null || currentRemoteMediaUser.getUser() == null) return null;
        return currentRemoteMediaUser.getUser().getUserId();
    }

    @Override
    public void playVideo(String userId) {
        if (playerPresenter == null) {
            playerFragment = new VideoPlayerFragment();
            playerPresenter = new VideoPlayerPresenter(playerFragment);
            bindVP(playerFragment, playerPresenter);
            if (flForegroundLeft.getVisibility() == View.GONE) {
                //左侧闲置
                addFragment(R.id.activity_live_room_foreground_left_container, playerFragment);
                flForegroundLeft.setVisibility(View.VISIBLE);
            } else if (flForegroundRight.getVisibility() == View.GONE) {
                //右侧闲置
                addFragment(R.id.activity_live_room_foreground_right_container, playerFragment);
                flForegroundRight.setVisibility(View.VISIBLE);
            }
        }
        playerPresenter.playVideo(userId);
    }

    @Override
    public void playVideoClose(String userId) {
        checkNotNull(playerPresenter);
        checkNotNull(playerFragment.getView());
        checkNotNull(lppptFragment.getView());
        int playerParentId = ((ViewGroup) playerFragment.getView().getParent()).getId();
        int pptParentId = ((ViewGroup) lppptFragment.getView().getParent()).getId();
        if (playerParentId == R.id.activity_live_room_foreground_left_container) {
            //视频在左边
            flForegroundLeft.setVisibility(View.GONE);
        } else if (playerParentId == R.id.activity_live_room_foreground_right_container) {
            //视频在右边
            flForegroundRight.setVisibility(View.GONE);
        } else if (playerParentId == R.id.activity_live_room_background_container) {
            //视频已经最大化
            if (pptParentId == R.id.activity_live_room_foreground_left_container) {
                //ppt在左边
                flForegroundLeft.setVisibility(View.GONE);
            } else if (pptParentId == R.id.activity_live_room_foreground_right_container) {
                //ppt在右边
                flForegroundRight.setVisibility(View.GONE);
            }
            maximisePPTView();
        }
        setCurrentVideoUser(null);
        playerPresenter.playAVClose(userId);
        removeFragment(playerFragment);
        playerFragment = null;
        playerPresenter = null;
    }

    @Override
    public void attachVideo() {
        if (recorderFragment == null) {
            recorderFragment = new VideoRecorderFragment();
            bindVP(recorderFragment, new VideoRecorderPresenter(recorderFragment));
            if (flForegroundLeft.getVisibility() == View.GONE) {
                //左侧闲置
                addFragment(R.id.activity_live_room_foreground_left_container, recorderFragment);
                flForegroundLeft.setVisibility(View.VISIBLE);
            } else if (flForegroundRight.getVisibility() == View.GONE) {
                //右侧闲置
                addFragment(R.id.activity_live_room_foreground_right_container, recorderFragment);
                flForegroundRight.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void detachVideo() {
        checkNotNull(recorderFragment);
        checkNotNull(recorderFragment.getView());
        checkNotNull(lppptFragment.getView());
        int recordParentId = ((ViewGroup) recorderFragment.getView().getParent()).getId();
        int pptParentId = ((ViewGroup) lppptFragment.getView().getParent()).getId();
        if (recordParentId == R.id.activity_live_room_foreground_left_container) {
            //视频采集在左边
            flForegroundLeft.setVisibility(View.GONE);
        } else if (recordParentId == R.id.activity_live_room_foreground_right_container) {
            //视频采集在右边
            flForegroundRight.setVisibility(View.GONE);
        } else if (recordParentId == R.id.activity_live_room_background_container) {
            //视频采集已经最大化
            if (pptParentId == R.id.activity_live_room_foreground_left_container) {
                //ppt在左边
                flForegroundLeft.setVisibility(View.GONE);
            } else if (pptParentId == R.id.activity_live_room_foreground_right_container) {
                //ppt在右边
                flForegroundRight.setVisibility(View.GONE);
            }
            maximisePPTView();
        }
        removeFragment(recorderFragment);
        if (Build.VERSION.SDK_INT < 24) {
            getSupportFragmentManager().executePendingTransactions();
        }
        recorderFragment = null;
    }

    @Override
    public void showRecorderDialogFragment() {
        View max = flBackground.getChildAt(0);
        if (max == recorderFragment.getView()) return;
        RecorderDialogFragment recorderFragment = new RecorderDialogFragment();
        RecorderDialogPresenter recorderPresenter = new RecorderDialogPresenter(recorderFragment);
        bindVP(recorderFragment, recorderPresenter);
        showDialogFragment(recorderFragment);
    }

    @Override
    public void showPPTDialogFragment() {
        View max = flBackground.getChildAt(0);
        if (max == lppptFragment.getView()) return;
        PPTDialogFragment pptFragment = new PPTDialogFragment();
        PPTDialogPresenter pptPresenter = new PPTDialogPresenter(pptFragment);
        bindVP(pptFragment, pptPresenter);
        showDialogFragment(pptFragment);
    }

    @Override
    public void showRemoteVideoPlayer() {
        View max = flBackground.getChildAt(0);
        if (max == playerFragment.getView()) return;
        RemoteVideoDialogFragment remoteVideoFragment = new RemoteVideoDialogFragment();
        RemoteVideoDialogPresenter remoteVideoPresenter = new RemoteVideoDialogPresenter(remoteVideoFragment);
        bindVP(remoteVideoFragment, remoteVideoPresenter);
        showDialogFragment(remoteVideoFragment);
    }

    /**
     * 当前正在互动的用户多媒体对象
     *
     * @param mediaModel
     */
    @Override
    public void setCurrentVideoUser(IMediaModel mediaModel) {
        this.currentRemoteMediaUser = mediaModel;
    }

    @Override
    public IMediaModel getCurrentVideoUser() {
        return currentRemoteMediaUser;
    }

    @Override
    public void clearPPTAllShapes() {
        checkNotNull(lppptFragment);
        lppptFragment.eraseAllShape();
    }

    @Override
    public void changeScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public int getCurrentScreenOrientation() {
        return getResources().getConfiguration().orientation;
    }

    @Override
    public int getSysRotationSetting() {
        int status = 0;
        try {
            status = Settings.System.getInt(getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public void letScreenRotateItself() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public void forbidScreenRotateItself() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
    }

    @Override
    public void showBigChatPic(String url) {
        ChatPictureViewFragment fragment = ChatPictureViewFragment.newInstance(url);
        ChatPictureViewPresenter presenter = new ChatPictureViewPresenter();
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    @Override
    public void sendImageMessage(String path) {
        chatPresenter.sendImageMessage(path);
    }

    @Override
    public void showReconnectSuccess() {
        // might delay 500ms to process
        Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Long>() {
                    @Override
                    public void call(Long aLong) {
                        removeFragment(loadingFragment);
                        flLoading.setVisibility(View.GONE);
                    }
                });
    }

    public void showSavePicDialog(byte[] bmpArray) {
        ChatSavePicDialogFragment fragment = new ChatSavePicDialogFragment();
        ChatSavePicDialogPresenter presenter = new ChatSavePicDialogPresenter(bmpArray);
        bindVP(fragment, presenter);
        showDialogFragment(fragment);
    }

    @Override
    public void realSaveBmpToFile(byte[] bmpArray) {
        saveImageToGallery(bmpArray);
    }

    private void switchView(View view1, View view2) {
        FrameLayout fl1 = (FrameLayout) view1.getParent();
        FrameLayout fl2 = (FrameLayout) view2.getParent();
        fl1.removeView(view1);
        fl2.removeView(view2);
        fl1.addView(view2);
        fl2.addView(view1);
    }

    private <V extends BaseView, P extends BasePresenter> void bindVP(V view, P presenter) {
        presenter.setRouter(this);
        view.setPresenter(presenter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pptManagePresenter != null) {
            pptManagePresenter.destroy();
            pptManagePresenter = null;
        }
        if (globalPresenter != null)
            globalPresenter.destroy();
        RxUtils.unSubscribe(subscriptionOfLoginConflict);

        orientationEventListener = null;
        if (getLiveRoom().getCurrentUser().getType() == LPConstants.LPUserType.Teacher) {
            if (getLiveRoom().getCloudRecordStatus()) {
                liveRoom.requestCloudRecord(false);
            }
            liveRoom.requestClassEnd();
        }
        getLiveRoom().quitRoom();
    }

    //初始化时检测屏幕方向，以此设置聊天页面是否可隐藏
    private void checkScreenOrientationInit() {
        Configuration configuration = this.getResources().getConfiguration();
        int orientation = configuration.orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dlChat.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            dlChat.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
    }

    private static LiveSDKWithUI.LPShareListener shareListener;

    public static void setShareListener(LiveSDKWithUI.LPShareListener listener) {
        shareListener = listener;
    }

    /**
     * 保存图片
     */
    private void saveImageToGallery(final byte[] bmpArray) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 首先保存图片
                File appDir = new File(Environment.getExternalStorageDirectory(), "bjhl_lp_image");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                File file = new File(appDir, fileName);
                final String picPath = file.getAbsolutePath();
                try {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bmpArray, 0, bmpArray.length);
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(getContentResolver(),
                            file.getAbsolutePath(), fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LiveRoomActivity.this, "图片保存在" + picPath, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }
}
