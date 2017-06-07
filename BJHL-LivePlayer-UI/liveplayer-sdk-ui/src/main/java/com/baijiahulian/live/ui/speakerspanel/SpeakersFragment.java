package com.baijiahulian.live.ui.speakerspanel;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseFragment;
import com.baijiahulian.live.ui.utils.LinearLayoutWrapManager;

/**
 * Created by Shubo on 2017/6/5.
 */

public class SpeakersFragment extends BaseFragment implements SpeakersContract.View {

    private SpeakersContract.Presenter presenter;
    private SpeakersAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_speakers;
    }

    @Override
    public void setPresenter(SpeakersContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) $.id(R.id.fragment_speakers_recycler).view();
        recyclerView.setLayoutManager(new LinearLayoutWrapManager(getContext()));
        adapter = new SpeakersAdapter();
        recyclerView.setAdapter(adapter);
        // disable recycle
        recyclerView.getRecycledViewPool().setMaxRecycledViews(SpeakersAdapter.VIEW_TYPE_RECORD, 0);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(SpeakersAdapter.VIEW_TYPE_PPT, 0);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(SpeakersAdapter.VIEW_TYPE_VIDEO_PLAY, 0);
    }

    @Override
    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void notifyItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItemDeleted(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemMoved(int fromPosition, int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }


    private class SpeakersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_TYPE_APPLY = 0;
        private static final int VIEW_TYPE_RECORD = 1;
        private static final int VIEW_TYPE_VIDEO_PLAY = 2;
        private static final int VIEW_TYPE_PPT = 3;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

}
