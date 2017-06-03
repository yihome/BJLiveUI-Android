package com.baijiahulian.live.ui.quiz;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseDialogFragment;
import com.baijiahulian.live.ui.utils.QueryPlus;
import com.baijiahulian.livecore.LiveSDK;
import com.baijiahulian.livecore.models.LPJsonModel;
import com.baijiahulian.livecore.models.imodels.IUserModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkangfei on 17/5/31.
 */

public class QuizDialogFragment extends BaseDialogFragment implements QuizDialogContract.View {
    public static final String KEY_BTN_STATUS = "top_right_btn_status";

    private QueryPlus $;
    private static final String windowName = "bjlapp";
    private QuizDialogContract.Presenter presenter;
    private IUserModel currentUserInfo;
    private String quizId;
    private String roomId;
    private String roomToken;
    private List<LPJsonModel> signalList;
    private boolean shouldShowClose;
    private boolean isUrlLoaded;
    private static final String[] baseUrl = {
            "http://test-api.baijiacloud.com/m/quiz/student",
            "http://beta-api.baijiacloud.com/m/quiz/student",
            "http://api.baijiacloud.com/m/quiz/student"
    };

    public QuizDialogFragment() {
        if (signalList == null) {
            signalList = new ArrayList<>();
        } else {
            signalList.clear();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_quiz;
    }

    @Override
    protected void init(Bundle savedInstanceState, Bundle arguments) {
        $ = QueryPlus.with(contentView);
        title(getString(R.string.live_quiz_title));
        Bundle args = getArguments();
        shouldShowClose = args.getBoolean(KEY_BTN_STATUS);
        initWebClient();
        loadUrl();
    }

    //是否显示关闭按钮
    public void setCloseBtnStatus(boolean shouldShowClose) {
        if (shouldShowClose) {
            editText(getString(R.string.live_quiz_close));
            editClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCloseDlg();
                }
            });
        } else {
            editable(false);
        }
    }

    private void showCloseDlg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setMessage(R.string.live_quiz_dialog_tip)
                .setPositiveButton(R.string.live_quiz_dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.dismissDlg();
                    }
                }).setNegativeButton(R.string.live_quiz_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.live_blue));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.live_blue));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebClient() {
        ((WebView) $.id(R.id.wv_quiz_main).view()).getSettings().setJavaScriptEnabled(true);
        ((WebView) $.id(R.id.wv_quiz_main).view()).addJavascriptInterface(this, windowName);
        ((WebView) $.id(R.id.wv_quiz_main).view()).setVerticalScrollBarEnabled(false);

        //chrome client
        ((WebView) $.id(R.id.wv_quiz_main).view()).setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                ((ProgressBar) $.id(R.id.pb_web_view_quiz).view()).setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });

        //webView client
        ((WebView) $.id(R.id.wv_quiz_main).view()).setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                $.id(R.id.pb_web_view_quiz).visible();
                setCloseBtnStatus(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isUrlLoaded = true;
                $.id(R.id.pb_web_view_quiz).gone();
                setCloseBtnStatus(shouldShowClose);
                callJsInQueue();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                $.id(R.id.pb_web_view_quiz).gone();
                setCloseBtnStatus(true);
            }
        });

    }

    private void loadUrl() {
        try {
            roomToken = URLEncoder.encode(roomToken, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String params = "?userNumber=" + currentUserInfo.getNumber() + "&userName=" + currentUserInfo.getName() + "&quizId=" + quizId
                + "&roomId=" + roomId + "&token=" + roomToken;
        String url = baseUrl[LiveSDK.getDeployType().getType()] + params;
        System.out.println("hola url --> " + url);
        ((WebView) $.id(R.id.wv_quiz_main).view()).loadUrl(url);
    }


    @Override
    protected void setWindowParams(WindowManager.LayoutParams windowParams) {
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.gravity = Gravity.CENTER;
        windowParams.x = 0;
        windowParams.y = 0;

        windowParams.windowAnimations = R.style.LiveBaseSendMsgDialogAnim;
    }

    @Override
    public void setPresenter(QuizDialogContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
        presenter.getCurrentUser();
        roomToken = presenter.getRoomToken();
    }

    @Override
    public void onStartArrived(LPJsonModel jsonModel) {
        String key = jsonModel.data.get("message_type").getAsString();
        if (!"quiz_start".equals(key)) {
            return;
        }
        quizId = jsonModel.data.get("quiz_id").getAsString();
        roomId = jsonModel.data.get("class_id").getAsString();
        signalList.add(jsonModel);
    }

    @Override
    public void onEndArrived(LPJsonModel jsonModel) {
        String key = jsonModel.data.get("message_type").getAsString();
        if (!"quiz_end".equals(key)) {
            return;
        }
        signalList.add(jsonModel);
        /*只有load完之后才去转发h5，否则加入队列，load完成后依次转发*/
        if (isUrlLoaded) {
            callJs(jsonModel.data.toString());
        }
    }

    @Override
    public void onSolutionArrived(LPJsonModel jsonModel) {
        String key = jsonModel.data.get("message_type").getAsString();
        if (!"quiz_solution".equals(key)) {
            return;
        }
        quizId = jsonModel.data.get("quiz_id").getAsString();
        roomId = jsonModel.data.get("class_id").getAsString();
        signalList.add(jsonModel);
    }

    @Override
    public void onQuizResArrived(LPJsonModel jsonModel) {
        //TODO:半截加入
        String key = jsonModel.data.get("message_type").getAsString();
        if (!"quiz_res".equals(key)) {
            return;
        }
        quizId = jsonModel.data.get("quiz_id").getAsString();
        roomId = jsonModel.data.get("class_id").getAsString();
        signalList.add(jsonModel);
    }

    @Override
    public void onGetCurrentUser(IUserModel userModel) {
        this.currentUserInfo = userModel;
    }

    @Override
    public void dismissDlg() {
        presenter.dismissDlg();
    }

    /**
     * 逐个给js转发信令
     */
    private void callJsInQueue() {
        if (signalList != null && signalList.size() > 0) {
            for (LPJsonModel jsonModel : signalList) {
                callJs(jsonModel.data.toString());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (signalList != null) {
            signalList.clear();
        }
    }

    /******************
     * js call android
     ******************/
    @JavascriptInterface
    public void close() {
        presenter.dismissDlg();
    }

    @JavascriptInterface
    public void sendMessage(String json) {
        presenter.sendCommonRequest(json);
    }

    /*************
     * android call js
     ************/
    private void callJs(String json) {
        ((WebView) $.id(R.id.wv_quiz_main).view()).loadUrl("javascript:receivedMessage('" + json + "')");
    }
}
