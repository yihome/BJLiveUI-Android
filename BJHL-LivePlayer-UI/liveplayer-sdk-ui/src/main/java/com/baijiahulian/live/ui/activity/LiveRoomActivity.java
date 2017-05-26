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
import android.view.SurfaceView;
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
    private FrameLayout flError;
    private DrawerLayout dlChat;
    private LinearLayout llVideoContainer;
    private FrameLayout flTopRight;
    private FrameLayout flPPTLeft;
    private FrameLayout flRightBottom;
    private FrameLayout flRight;

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
    private PPTLeftFragment pptLeftFragment;

    private OrientationEventListener orientationEventListener; //处理屏幕旋转时本地录制视频的方向
    private int oldRotation;

    private Subscription subscriptionOfLoginConflict, subscriptionOfSwitch;
    private IMediaModel currentRemoteMediaUser;
    private boolean isClearScreen;//是否已经清屏，作用于视频采集和远程视频ui的调整
    private String code, name;
    private boolean isSwitchable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);

        initViews();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onConfigurationChanged(getResources().getConfiguration());
        }

        code = getIntent().getStringExtra("code");
        name = getIntent().getStringExtra("name");

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
//        CrashHandler.getInstance().init(LiveRoomActivity.this);
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
        flError = (FrameLayout) findViewById(R.id.activity_live_room_error);
        flRightBottom = (FrameLayout) findViewById(R.id.activity_live_room_bottom_right);
        flRight = (FrameLayout) findViewById(R.id.activity_live_room_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationEventListener.enable();

        if (roomLifeCycleListener != null) {
            roomLifeCycleListener.onResume(this, new LiveSDKWithUI.LPRoomChangeRoomListener() {
                @Override
                public void changeRoom(String code, String nickName) {
                    //重新进入教室
                    removeFragment(lppptFragment);
                    removeFragment(topBarFragment);
                    removeFragment(rightTopMenuFragment);
                    removeFragment(leftMenuFragment);
                    removeFragment(pptLeftFragment);
                    removeFragment(rightMenuFragment);
                    removeFragment(rightBottomMenuFragment);
                    removeFragment(chatFragment);

                    if (playerFragment != null && playerFragment.isAdded())
                        removeFragment(playerFragment);
                    if (recorderFragment != null && recorderFragment.isAdded())
                        removeFragment(recorderFragment);
                    if (errorFragment != null && errorFragment.isAdded())
                        removeFragment(errorFragment);

                    loadingFragment = new LoadingFragment();
                    LoadingPresenter loadingPresenter = new LoadingPresenter(loadingFragment, code, nickName, false);
                    bindVP(loadingFragment, loadingPresenter);
                    addFragment(R.id.activity_live_room_loading, loadingFragment);
                }
            });
        }
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
            if (isClearScreen)
                unClearScreen();
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
                flRightBottom.setVisibility(View.INVISIBLE);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            flLeft.setVisibility(View.VISIBLE);
            dlChat.setVisibility(View.VISIBLE);
            flRightBottom.setVisibility(View.VISIBLE);
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
            public void onError(final LPError error) {
                switch ((int) error.getCode()) {
                    case LPError.CODE_ERROR_ROOMSERVER_LOSE_CONNECTION:
                    case LPError.CODE_ERROR_NETWORK_FAILURE:
                    case LPError.CODE_ERROR_CHATSERVER_LOSE_CONNECTION:
                        Observable.just(1).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new LPErrorPrintSubscriber<Integer>() {
                                    @Override
                                    public void call(Integer integer) {
                                        showNetError(error);
                                    }
                                });
                        break;
                    case LPError.CODE_ERROR_LOGIN_CONFLICT:
                        break;
                    case LPError.CODE_ERROR_OPEN_AUDIO_RECORD_FAILED:
                        if (!TextUtils.isEmpty(error.getMessage()))
                            showMessage(error.getMessage());
                        break;
                    case LPError.CODE_ERROR_OPEN_AUDIO_CAMERA_FAILED:
                        if (!TextUtils.isEmpty(error.getMessage()))
                            showMessage(error.getMessage());
                        detachLocalVideo();
                        break;
//                    case LPError.CODE_ERROR_CHATSERVER_LOSE_CONNECTION:
//
//                        break;
                    default:
                        if (!TextUtils.isEmpty(error.getMessage()))
                            showMessage(error.getMessage());
                        break;
                }
            }
        });
    }

    private Toast toast;

    @Override
    public void showMessage(final String message) {
        if (TextUtils.isEmpty(message)) return;
        Observable.just(1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object obj) {
                toast = Toast.makeText(LiveRoomActivity.this, message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    @Override
    public void saveTeacherMediaStatus(IMediaModel model) {
        globalPresenter.setTeacherMedia(model);
    }

    @Override
    public void showError(LPError error) {
        if (errorFragment != null && errorFragment.isAdded()) return;
        if (flError.getChildCount() >= 2) return;
        if (loadingFragment != null && loadingFragment.isAdded()) {
            removeFragment(loadingFragment);
        }
        errorFragment = ErrorFragment.newInstance(getString(R.string.live_override_error), error.getMessage(), ErrorFragment.ERROR_HANDLE_REENTER);
        errorFragment.setRouterListener(this);
        flError.setVisibility(View.VISIBLE);
        addFragment(R.id.activity_live_room_error, errorFragment);
        if (Build.VERSION.SDK_INT < 24) {
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public boolean canStudentDraw() {
        return isTeacherOrAssistant() || lppptFragment.isCurrentMaxPage();
    }

    @Override
    public boolean isCurrentUserTeacher() {
        return liveRoom.getCurrentUser().getType() == LPConstants.LPUserType.Teacher;
    }

    @Override
    public boolean isVideoManipulated() {
        return globalPresenter.isVideoManipulated();
    }

    @Override
    public void setVideoManipulated(boolean b) {
        globalPresenter.setVideoManipulated(b);
    }

    @Override
    public int getSpeakApplyStatus() {
        return rightMenuPresenter.getSpeakApplyStatus();
    }

    @Override
    public boolean switchable() {
        if (!isSwitchable) {
            showMessage(getString(R.string.live_frequent_error));
        }
        return isSwitchable;
    }

    @Override
    public void setSwitching() {
        isSwitchable = false;
        subscriptionOfSwitch = Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<Long>() {
                    @Override
                    public void call(Long aLong) {
                        isSwitchable = true;
                    }
                });
    }

    @Override
    public void showMessageClassEnd() {
        showMessage(getString(R.string.live_message_le, getString(R.string.lp_override_class_end)));
    }

    @Override
    public void showMessageClassStart() {
        showMessage(getString(R.string.live_message_le, getString(R.string.lp_override_class_start)));
    }

    @Override
    public void showMessageForbidAllChat(boolean isOn) {
        if (isTeacherOrAssistant()) {
            showMessage((isOn ? "打开" : "关闭") + "全体禁言成功");
        } else {
            showMessage(getString(R.string.lp_override_role_teacher) + (isOn ? "打开了" : "关闭了") + "全体禁言");
        }
    }

    @Override
    public void showMessageTeacherOpenAudio() {
        showMessage(getString(R.string.lp_override_role_teacher) + "打开了音频");
    }

    @Override
    public void showMessageTeacherOpenVideo() {
        showMessage(getString(R.string.lp_override_role_teacher) + "打开了视频");
    }

    @Override
    public void showMessageTeacherOpenAV() {
        showMessage(getString(R.string.lp_override_role_teacher) + "打开了音视频");
    }

    @Override
    public void showMessageTeacherCloseAV() {
        showMessage(getString(R.string.lp_override_role_teacher) + "关闭了音视频");
    }

    @Override
    public void showMessageTeacherCloseAudio() {
        showMessage(getString(R.string.lp_override_role_teacher) + "关闭了音频");
    }

    @Override
    public void showMessageTeacherCloseVideo() {
        showMessage(getString(R.string.lp_override_role_teacher) + "关闭了视频");
    }

    @Override
    public void showMessageTeacherEnterRoom() {
        showMessage(getString(R.string.lp_override_role_teacher) + "进入了" + getString(R.string.lp_override_classroom));
    }

    @Override
    public void showMessageTeacherExitRoom() {
        showMessage(getString(R.string.lp_override_role_teacher) + "离开了" + getString(R.string.lp_override_classroom));
    }

    private void showNetError(LPError error) {
        if (errorFragment != null && errorFragment.isAdded()) return;
        if (flError.getChildCount() >= 2) return;
        if (loadingFragment != null && loadingFragment.isAdded()) {
            removeFragment(loadingFragment);
        }
        errorFragment = ErrorFragment.newInstance("好像断网了", error.getMessage(), ErrorFragment.ERROR_HANDLE_RECONNECT);
        errorFragment.setRouterListener(this);
        flError.setVisibility(View.VISIBLE);
        addFragment(R.id.activity_live_room_error, errorFragment);
        if (Build.VERSION.SDK_INT < 24) {
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public void doReEnterRoom() {
        if (errorFragment != null && errorFragment.isAdded()) {
            removeFragment(errorFragment);
        }
        removeFragment(lppptFragment);
        removeFragment(topBarFragment);
        removeFragment(rightTopMenuFragment);
        removeFragment(leftMenuFragment);
        removeFragment(pptLeftFragment);
        removeFragment(rightMenuFragment);
        removeFragment(rightBottomMenuFragment);
        removeFragment(chatFragment);

        currentRemoteMediaUser = null;
        if (loadingFragment != null && loadingFragment.isAdded())
            removeFragment(loadingFragment);
        if (playerFragment != null && playerFragment.isAdded()) {
            removeFragment(playerFragment);
            playerFragment = null;
            playerPresenter = null;
        }
        if (recorderFragment != null && recorderFragment.isAdded()) {
            removeFragment(recorderFragment);
            recorderFragment = null;
        }

        flBackground.removeAllViews();
        flForegroundLeft.removeAllViews();
        flForegroundRight.removeAllViews();

        getSupportFragmentManager().executePendingTransactions();

        flForegroundLeft.setVisibility(View.GONE);
        flForegroundRight.setVisibility(View.GONE);

        liveRoom.quitRoom();

        flLoading.setVisibility(View.VISIBLE);
        loadingFragment = new LoadingFragment();
        LoadingPresenter loadingPresenter = new LoadingPresenter(loadingFragment, code, name, false);
        bindVP(loadingFragment, loadingPresenter);
        addFragment(R.id.activity_live_room_loading, loadingFragment);
    }

    @Override
    public void doReconnectServer() {
//        if (errorFragment != null && errorFragment.isAdded())
//            removeFragment(errorFragment);
//
//        loadingFragment = new LoadingFragment();
//        LoadingPresenter loadingPresenter = new LoadingPresenter(loadingFragment, "", "", true);
//        bindVP(loadingFragment, loadingPresenter);
//        addFragment(R.id.activity_live_room_loading, loadingFragment);
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

        pptLeftFragment = new PPTLeftFragment();
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

        RxUtils.unSubscribe(subscriptionOfLoginConflict);
        subscriptionOfLoginConflict = getLiveRoom().getObservableOfLoginConflict().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPErrorPrintSubscriber<ILoginConflictModel>() {
                    @Override
                    public void call(ILoginConflictModel iLoginConflictModel) {
                        if (enterRoomConflictListener != null) {
                            enterRoomConflictListener.onConflict(LiveRoomActivity.this, iLoginConflictModel.getConflictEndType()
                                    , new LiveSDKWithUI.LPRoomExitCallback() {
                                        @Override
                                        public void exit() {
                                            LiveRoomActivity.super.finish();
                                        }

                                        @Override
                                        public void cancel() {
                                            LiveRoomActivity.super.finish();
                                        }
                                    });
                        } else {
                            LiveRoomActivity.super.finish();
                        }
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
                        flError.setVisibility(View.GONE);
                    }
                });

        if (liveRoom.getCurrentUser().getType() == LPConstants.LPUserType.Teacher) {
            liveRoom.getRecorder().publish();
            liveRoom.getRecorder().attachAudio();
            attachLocalVideo();
        }
    }

    @Override
    public void clearScreen() {
        chatFragment.clearScreen();
        isClearScreen = true;
//        dlChat.setVisibility(View.GONE);
        rightBottomMenuFragment.clearScreen();
        hideFragment(topBarFragment);
        hideFragment(rightMenuFragment);
        flLeft.setVisibility(View.INVISIBLE);
        flRight.setVisibility(View.INVISIBLE);
        flRightBottom.setVisibility(View.INVISIBLE);
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
        flLeft.setVisibility(View.VISIBLE);
        flRight.setVisibility(View.VISIBLE);
        flRightBottom.setVisibility(View.VISIBLE);
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
                flRightBottom.setVisibility(View.INVISIBLE);
            }
            flPPTLeft.setVisibility(View.VISIBLE);
        } else {
            flTop.setVisibility(View.VISIBLE);
            flLeft.setVisibility(View.VISIBLE);
            dlChat.setVisibility(View.VISIBLE);
            flRightBottom.setVisibility(View.VISIBLE);
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
            detachLocalVideo();
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
        switchView(recorderFragment.getView(), max);
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
        switchView(playerFragment.getView(), max);
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
        switchView(lppptFragment.getView(), max);
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
    public void attachLocalVideo() {
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
            showMessage(getString(R.string.live_camera_on));
        }
    }

    @Override
    public void detachLocalVideo() {
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
        showMessage(getString(R.string.live_camera_off));
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

    public boolean isPPTMax() {
        return flBackground.getChildAt(0) == lppptFragment.getView();
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

    @Override
    public void doHandleErrorNothing() {
        removeFragment(errorFragment);
    }

    private void switchView(View view1, View view2) {
        FrameLayout fl1 = (FrameLayout) view1.getParent();
        FrameLayout fl2 = (FrameLayout) view2.getParent();
        if (view1 == lppptFragment.getView() || view2 == lppptFragment.getView()) {
            lppptFragment.onPause();
        }
        fl1.removeView(view1);
        fl2.removeView(view2);

        fl1.addView(view2);
        fl2.addView(view1);

        if (view1 == lppptFragment.getView() || view2 == lppptFragment.getView()) {
            lppptFragment.onResume();
        }
        setZOrderMediaOverlayTrue(view2);
        setZOrderMediaOverlayFalse(view1);
    }

    private void setZOrderMediaOverlayTrue(View view) {
        if (view instanceof SurfaceView) {
            ((SurfaceView) view).setZOrderMediaOverlay(true);
        } else if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setZOrderMediaOverlayTrue(((ViewGroup) view).getChildAt(i));
            }
        }
    }

    private void setZOrderMediaOverlayFalse(View view) {
        if (view instanceof SurfaceView) {
            ((SurfaceView) view).setZOrderMediaOverlay(false);
        } else if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setZOrderMediaOverlayFalse(((ViewGroup) view).getChildAt(i));
            }
        }
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
        if (globalPresenter != null) {
            globalPresenter.destroy();
            globalPresenter = null;
        }

        RxUtils.unSubscribe(subscriptionOfLoginConflict);
        RxUtils.unSubscribe(subscriptionOfSwitch);

        orientationEventListener = null;

        getLiveRoom().quitRoom();
        clearStaticCallback();
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

    @Override
    public void finish() {
        if (exitListener != null) {
            exitListener.onRoomExit(this, new LiveSDKWithUI.LPRoomExitCallback() {
                @Override
                public void exit() {
                    tryToCloseCloudRecord();
                    LiveRoomActivity.super.finish();
                }

                @Override
                public void cancel() {
                }
            });
        } else {
            tryToCloseCloudRecord();
            super.finish();
        }
    }

    private void tryToCloseCloudRecord() {
        // 如果是被踢下线 core里面会调quitRoom
        if (getLiveRoom().isQuit()) return;

        if (getLiveRoom().getCurrentUser().getType() == LPConstants.LPUserType.Teacher) {
            if (getLiveRoom().getCloudRecordStatus()) {
                liveRoom.requestCloudRecord(false);
            }
            liveRoom.requestClassEnd();
        }
    }

    /* 房间外部回调 */
    private static LiveSDKWithUI.LPShareListener shareListener;
    private static LiveSDKWithUI.LPRoomExitListener exitListener;
    private static LiveSDKWithUI.RoomEnterConflictListener enterRoomConflictListener;
    private static LiveSDKWithUI.LPRoomResumeListener roomLifeCycleListener;

    private void clearStaticCallback() {
        shareListener = null;
        exitListener = null;
        enterRoomConflictListener = null;
        roomLifeCycleListener = null;
    }

    public static void setRoomLifeCycleListener(LiveSDKWithUI.LPRoomResumeListener roomLifeCycleListener) {
        LiveRoomActivity.roomLifeCycleListener = roomLifeCycleListener;
    }

    public static void setShareListener(LiveSDKWithUI.LPShareListener listener) {
        LiveRoomActivity.shareListener = listener;
    }

    public static void setRoomExitListener(LiveSDKWithUI.LPRoomExitListener roomExitListener) {
        LiveRoomActivity.exitListener = roomExitListener;
    }

    public static void setEnterRoomConflictListener(LiveSDKWithUI.RoomEnterConflictListener enterRoomConflictListener) {
        LiveRoomActivity.enterRoomConflictListener = enterRoomConflictListener;
    }

}
