package com.baijiahulian.live.ui.chat;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseDialogFragment;
import com.baijiahulian.live.ui.utils.QueryPlus;

/**
 * Created by Shubo on 2017/3/4.
 */

public class MessageSentFragment extends BaseDialogFragment implements MessageSendContract.View {

    private MessageSendContract.Presenter presenter;
    private QueryPlus $;
    private MessageTextWatcher textWatcher;

    public static MessageSentFragment newInstance() {
        Bundle args = new Bundle();

        MessageSentFragment fragment = new MessageSentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_message_send;
    }

    @Override
    protected void setWindowParams(WindowManager.LayoutParams windowParams) {
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.gravity = Gravity.BOTTOM | GravityCompat.END;
    }

    @Override
    protected void init(Bundle savedInstanceState, Bundle arguments) {
        super.hideTitleBar();
        $ = QueryPlus.with(contentView);
        textWatcher = new MessageTextWatcher();
        ((EditText) $.id(R.id.dialog_message_send_et).view()).addTextChangedListener(textWatcher);
        $.id(R.id.dialog_message_send_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(((EditText) $.id(R.id.dialog_message_send_et).view())
                        .getEditableText().toString());
            }
        });
        $.id(R.id.dialog_message_send_pic).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.choosePhoto();
            }
        });
    }

    @Override
    public void setPresenter(MessageSendContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((EditText) $.id(R.id.dialog_message_send_et).view()).removeTextChangedListener(textWatcher);
        $ = null;
        presenter = null;
    }

    @Override
    public void showMessageSuccess() {
        $.id(R.id.dialog_message_send_et).text("");
    }

    private class MessageTextWatcher implements android.text.TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)) {
                $.id(R.id.dialog_message_send_btn).enable(false);
            } else {
                $.id(R.id.dialog_message_send_btn).enable(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
