package com.baijiahulian.live.ui.chat;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.models.imodels.IMessageModel;

import butterknife.BindView;

/**
 * Created by Shubo on 2017/2/23.
 */

public class ChatFragment extends BaseFragment implements ChatContract.View {

    @BindView(R.id.fragment_chat_recycler)
    RecyclerView recyclerView;
    private MessageAdapter adapter;
    private ChatContract.Presenter presenter;
    LinearLayoutManager mLayoutManager;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        adapter = new MessageAdapter();
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void notifyDataChanged() {
        adapter.notifyItemInserted(adapter.getItemCount());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void clearScreen() {
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void unClearScreen() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        this.presenter = presenter;
        super.setBasePresenter(presenter);
    }

    class MessageAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            IMessageModel message = presenter.getMessage(position);
            int color;
            if (message.getFrom().getType() == LPConstants.LPUserType.Teacher) {
                color = ContextCompat.getColor(getContext(), R.color.live_blue);
            } else {
                color = ContextCompat.getColor(getContext(), R.color.live_yellow);
            }
            String name = message.getFrom().getName() + "：";
            SpannableString spanText = new SpannableString(name);
            spanText.setSpan(new ForegroundColorSpan(color), 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.textView.setText(spanText);
            holder.textView.append(message.getContent());
        }

        @Override
        public int getItemCount() {
            return presenter.getCount();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_chat_text);
        }
    }
}
