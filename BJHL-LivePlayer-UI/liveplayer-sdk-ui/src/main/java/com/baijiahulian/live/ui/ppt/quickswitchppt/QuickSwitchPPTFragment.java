package com.baijiahulian.live.ui.ppt.quickswitchppt;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baijiahulian.live.ui.R;
import com.baijiahulian.live.ui.base.BaseDialogFragment;
import com.baijiahulian.live.ui.utils.QueryPlus;
import com.baijiahulian.livecore.context.LPConstants;
import com.baijiahulian.livecore.viewmodels.impl.LPDocListViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szw on 17/7/4.
 */

public class QuickSwitchPPTFragment extends BaseDialogFragment implements SwitchPPTContract.View {
    private SwitchPPTContract.Presenter presenter;
    private QueryPlus $;
    private List<LPDocListViewModel.DocModel> quickDocList = new ArrayList<>();
    private List<LPDocListViewModel.DocModel> docModelList = new ArrayList<>();
    private QuickSwitchPPTAdapter adapter;
    private boolean isStudent = false;
    private int maxIndex;//学生可以快速滑动ppt的最大页数
    private int currentIndex;

    public static QuickSwitchPPTFragment newInstance() {
        Bundle args = new Bundle();
        QuickSwitchPPTFragment quickSwitchPPTFragment = new QuickSwitchPPTFragment();
        quickSwitchPPTFragment.setArguments(args);
        return quickSwitchPPTFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_ppt_switch;
    }

    @Override
    protected void init(Bundle savedInstanceState, Bundle arguments) {
        super.hideTitleBar();
        $ = QueryPlus.with(contentView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new QuickSwitchPPTAdapter();
        ((RecyclerView) $.id(R.id.dialog_switch_ppt_rv).view()).setLayoutManager(manager);
        ((RecyclerView) $.id(R.id.dialog_switch_ppt_rv).view()).setAdapter(adapter);
        this.maxIndex = getArguments().getInt("maxIndex");
        this.currentIndex = getArguments().getInt("currentIndex");
        initData();
    }

    public void initData() {
        docModelList = presenter.getDocList();
        if(isStudent){
            quickDocList = docModelList.subList(0, maxIndex + 1);
        }else{
            quickDocList.addAll(docModelList);
        }
    }

    @Override
    protected void setWindowParams(WindowManager.LayoutParams windowParams) {
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.gravity = Gravity.BOTTOM | GravityCompat.END;
        windowParams.windowAnimations = R.style.LiveBaseSendMsgDialogAnim;
    }

    @Override
    public void setPresenter(SwitchPPTContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setIndex() {
        ((RecyclerView) $.id(R.id.dialog_switch_ppt_rv).view()).smoothScrollToPosition(currentIndex);
    }

    @Override
    public void setMaxIndex(int updateMaxIndex) {
        this.maxIndex = updateMaxIndex;
        if(isStudent){
                quickDocList = docModelList.subList(0, maxIndex + 1);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setType(boolean isStudent) {
        this.isStudent = isStudent;
    }

    @Override
    public void docListChanged(List<LPDocListViewModel.DocModel> docModelList) {
        this.docModelList.clear();
        this.docModelList.addAll(docModelList);
        if(isStudent){
            quickDocList = docModelList.subList(0, maxIndex + 1);
        }else{
            quickDocList.addAll(docModelList);
        }
        adapter.notifyDataSetChanged();
    }

    class QuickSwitchPPTAdapter extends RecyclerView.Adapter<SwitchHolder> {

        @Override
        public SwitchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SwitchHolder holder = new SwitchHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_switch_ppt, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(SwitchHolder holder, final int position) {
            Picasso.with(getActivity()).load(quickDocList.get(position + 1).url).into(holder.PPTView);
            holder.PPTOrder.setText(String.valueOf(position + 1));
            holder.PPTRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.setSwitchPosition(position + 1);
                }
            });
        }

        @Override
        public int getItemCount() {
            return quickDocList.size() - 1;
        }
    }

    class SwitchHolder extends RecyclerView.ViewHolder {
        ImageView PPTView;
        TextView PPTOrder;
        RelativeLayout PPTRL;

        public SwitchHolder(View itemView) {
            super(itemView);
            this.PPTView = (ImageView) itemView.findViewById(R.id.item_ppt_view);
            this.PPTOrder = (TextView) itemView.findViewById(R.id.item_ppt_order);
            this.PPTRL = (RelativeLayout) itemView.findViewById(R.id.item_ppt_rl);
        }
    }
}
