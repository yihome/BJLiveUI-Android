package com.baijia.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.baijiahulian.live.ui.LiveSDKWithUI;
import com.baijiahulian.live.ui.utils.LPShareModel;
import com.baijiahulian.livecore.context.LPConstants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String code;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("live_temp", Context.MODE_PRIVATE);
        code = sp.getString("code", "hwiv75");
        ((EditText) findViewById(R.id.activity_entry_join_code)).setText(code);

        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = ((EditText) findViewById(R.id.activity_entry_join_code)).getText().toString();
                String name = ((EditText) findViewById(R.id.activity_entry_name)).getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("code", code);
                editor.apply();

                LiveSDKWithUI.enterRoom(MainActivity.this, code, name, new LiveSDKWithUI.LiveSDKEnterRoomListener() {
                    @Override
                    public void onError(String s) {

                    }
                });
            }
        });
    }

    private class ShareModel extends LPShareModel {

        @Override
        public String getShareIconText() {
            return null;
        }

        @Override
        public int getShareIconRes() {
            return 0;
        }

        @Override
        public boolean hasCorner() {
            return false;
        }

        @Override
        public String getCornerText() {
            return null;
        }

        @Override
        public int getShareType() {
            return 0;
        }

        public String getTitle() {
            return null;
        }

        public String getContent() {
            return null;
        }

        public String getUrl() {
            return null;
        }

        public String getImageUrl() {
            return null;
        }
    }

    private static void setupRoomListeners(Context context, LPConstants.LPUserType type) {
        ArrayList<ShareModel> shareModels;

        LiveSDKWithUI.setShareListener(new LiveSDKWithUI.LPShareListener() {

            @Override
            public void onShareClicked(Context context, int i) {
                // i 为LPShareModel里getShareType的值
            }

            @Override
            public ArrayList<? extends LPShareModel> setShareList() {
                // shareModel请继承LPShareModel
                // getShareIconRes() 返回分享图标 resource
                // getShareIconText() 返回分享图标文字 如：微信、朋友圈
                // hasCorner 默认false
                return null;
            }

            @Override
            public void getShareData(Context context, long roomId) {
                // 触发集成方异步获取分享信息
            }
        });
        LiveSDKWithUI.setEnterRoomConflictListener(new LiveSDKWithUI.RoomEnterConflictListener() {
            @Override
            public void onConflict(Context context, LPConstants.LPEndType endType, final LiveSDKWithUI.LPRoomExitCallback callback) {
                // 单点登录冲突 endType为冲突方终端类型
                // 回调callback.exit(); 退出直播间
                /*
                new MaterialDialog.Builder(context)
                        .content("您的账号已在" + endType.name() + "端登录")
                        .positiveText(R.string.confirm)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                callback.exit();
                            }
                        })
                        .show();
                */
            }
        });

        LiveSDKWithUI.setRoomExitListener(new LiveSDKWithUI.LPRoomExitListener() {
            @Override
            public void onRoomExit(Context context, final LiveSDKWithUI.LPRoomExitCallback lpRoomExitCallback) {
                //退出直播间二次确认回调 无二次确认无需设置
                lpRoomExitCallback.exit();
                /*
                new MaterialDialog.Builder(context)
                        .content("您确定要离开房间吗?")
                        .positiveText("离开")
                        .positiveColor(color)
                        .negativeColor(color)
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                lpRoomExitCallback.exit();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                lpRoomExitCallback.cancel();
                            }
                        }).show();
                */
            }
        });
    }
}
