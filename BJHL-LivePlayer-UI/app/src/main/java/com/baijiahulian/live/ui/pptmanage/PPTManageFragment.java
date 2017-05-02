package com.baijiahulian.live.ui.pptmanage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseDialogFragment;
import com.baijiahulian.live.ui.utils.QueryPlus;
import com.squareup.picasso.Picasso;

/**
 * Created by Shubo on 2017/4/26.
 */

public class PPTManageFragment extends BaseDialogFragment implements PPTManageContract.View {

    private PPTManageContract.Presenter presenter;
    private QueryPlus $;
    private DocumentAdapter adapter;

    public static PPTManageFragment newInstance() {

        Bundle args = new Bundle();

        PPTManageFragment fragment = new PPTManageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_ppt_manage;
    }

    @Override
    protected void init(Bundle savedInstanceState, Bundle arguments) {
        super.title(getString(R.string.live_ppt)).editable(true);
        $ = QueryPlus.with(contentView);
        RecyclerView recyclerView = (RecyclerView) $.id(R.id.dialog_ppt_manage_rv).view();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DocumentAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void enableEdit() {
    }

    @Override
    protected void disableEdit() {
    }

    @Override
    public void setPresenter(PPTManageContract.Presenter presenter) {
        super.setBasePresenter(presenter);
        this.presenter = presenter;
    }

    @Override
    public void showPPTEmpty() {
        $.id(R.id.dialog_ppt_manage_empty_container).visible();
        $.id(R.id.dialog_ppt_manage_rv).gone();
    }

    @Override
    public void showPPTNotEmpty() {
        $.id(R.id.dialog_ppt_manage_empty_container).gone();
        $.id(R.id.dialog_ppt_manage_rv).visible();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
        $ = null;
    }

    private static class DocViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        ImageView ivIcon;
        TextView tvTitle;

        DocViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.item_ppt_manage_normal_check_box);
            ivIcon = (ImageView) itemView.findViewById(R.id.item_ppt_manage_normal_icon);
            tvTitle = (TextView) itemView.findViewById(R.id.item_ppt_manage_normal_title);
        }
    }

    private static class UploadingViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        TextView tvTitle;
        View progress;
        TextView tvStatus;

        UploadingViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.item_ppt_manage_uploading_icon);
            tvTitle = (TextView) itemView.findViewById(R.id.item_ppt_manage_uploading_title);
            tvStatus = (TextView) itemView.findViewById(R.id.item_ppt_manage_uploading_status);
            progress = itemView.findViewById(R.id.item_ppt_manage_uploading_progress);
        }
    }

    private class DocumentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int ITEM_TYPE_NORMAL = 0;
        private static final int ITEM_TYPE_UPLOADING = 1;

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_NORMAL) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_ppt_manage_normal, parent, false);
                return new DocViewHolder(view);
            } else if (viewType == ITEM_TYPE_UPLOADING) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_ppt_manage_uploading, parent, false);
                return new UploadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof DocViewHolder) {
                DocViewHolder docViewHolder = (DocViewHolder) holder;
                docViewHolder.tvTitle.setText(presenter.getItem(position).getFileName());
                Picasso.with(getContext()).load(getDrawableResByFileExt(presenter.getItem(position).getFileExt())).into(docViewHolder.ivIcon);
            } else if (holder instanceof UploadingViewHolder) {
                UploadingViewHolder viewHolder = (UploadingViewHolder) holder;
                viewHolder.tvTitle.setText(presenter.getItem(position).getFileName());
                Picasso.with(getContext()).load(getDrawableResByFileExt(presenter.getItem(position).getFileExt())).into(viewHolder.ivIcon);
                if (presenter.getItem(position).getStatus() == DocumentUploadingModel.UPLOADING) {
                    viewHolder.tvStatus.setText(getString(R.string.live_uploading));
                } else if (presenter.getItem(position).getStatus() == DocumentUploadingModel.UPLOADED) {
                    viewHolder.tvStatus.setText(getString(R.string.live_queueing));
                } else {
                    viewHolder.tvStatus.setText("");
                }
//                viewHolder.progress
            }
        }

        @Override
        public int getItemCount() {
            return presenter.getCount();
        }

        private int getDrawableResByFileExt(String ext) {
            switch (ext) {
                case ".doc":
                case ".docx":
                    return R.drawable.live_ic_file_pdf;
                case ".ppt":
                case ".pptx":
                    return R.drawable.live_ic_file_ppt;
                case ".pdf":
                    return R.drawable.live_ic_file_pdf;
                default:
                    return R.drawable.live_ic_file_jpg;
            }
        }
    }
}
