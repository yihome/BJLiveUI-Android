package com.baijiahulian.live.ui.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseDialogFragment;
import com.baijiahulian.live.ui.utils.Query;

/**
 * Created by Shubo on 2017/3/4.
 */

public class MessageSentFragment extends BaseDialogFragment implements MessageSendContract.View {

    private MessageSendContract.Presenter presenter;
    private Query $;
    private EditText etContent;
    private MessageTextWatcher textWatcher;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_message_send;
    }

    @Override
    protected void init(Bundle savedInstanceState, Bundle arguments) {
        $ = Query.with(contentView);
        textWatcher = new MessageTextWatcher();
        etContent = ((EditText) $.id(R.id.dialog_message_send_et).view());
        etContent.addTextChangedListener(textWatcher);
        $.id(R.id.dialog_message_send_btn).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(etContent.getEditableText().toString());
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
        etContent.removeTextChangedListener(textWatcher);
        $ = null;
        presenter = null;
    }

    @Override
    public void showMessageSuccess() {
        etContent.setText("");
    }

    private class MessageTextWatcher implements android.text.TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(s)){
                $.id(R.id.dialog_message_send_btn).enable(false);
            }else{
                $.id(R.id.dialog_message_send_btn).enable(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
