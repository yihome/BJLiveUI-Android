package com.baijiahulian.live.ui.pptmanage;

import com.baijiahulian.live.ui.activity.LiveRoomRouterListener;
import com.baijiahulian.livecore.models.LPDocumentModel;
import com.baijiahulian.livecore.models.LPUploadDocModel;
import com.baijiahulian.livecore.utils.LPBackPressureBufferedSubscriber;
import com.baijiahulian.livecore.utils.LPErrorPrintSubscriber;
import com.baijiahulian.livecore.utils.LPRxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 管理PPT文档
 * <p>
 * Created by Shubo on 2017/4/26.
 */

public class PPTManagePresenter implements PPTManageContract.Presenter {

    private LiveRoomRouterListener routerListener;
    private PPTManageContract.View view;
    private List<DocumentModel> addedDocuments;
    // 图片上传完成，从uploadingQueue中移除添加到waitDocAddQueue，为了保证添加顺序一个一个发docAdd并等待docAdd信令返回
    private LinkedBlockingQueue<DocumentUploadingModel> uploadingQueue;
    private Subscription subscriptionOfDocAdd;


    public PPTManagePresenter(PPTManageContract.View view) {
        this.view = view;
        uploadingQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void setRouter(LiveRoomRouterListener liveRoomRouterListener) {
        routerListener = liveRoomRouterListener;
    }

    @Override
    public void subscribe() {
        addedDocuments = new ArrayList<>();
        for (LPDocumentModel m : routerListener.getLiveRoom().getDocListVM().getDocumentList()) {
            addedDocuments.add(new DocumentModel(m));
        }

        if (addedDocuments.size() > 0) {
            view.showPPTNotEmpty();
        } else {
            view.showPPTEmpty();
        }

        subscriptionOfDocAdd = routerListener.getLiveRoom().getDocListVM().getObservableOfDocAdd()
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LPBackPressureBufferedSubscriber<LPDocumentModel>() {
                    @Override
                    public void call(LPDocumentModel lpDocumentModel) {
                        addedDocuments.add(new DocumentModel(lpDocumentModel));
                        DocumentUploadingModel model = uploadingQueue.peek();
                        // 如果是本地上传，等待DocAdd信令返回后再队列里移除
                        if (model != null && model.status == DocumentUploadingModel.WAIT_SIGNAL
                                && String.valueOf(model.uploadModel.fileId).equals(lpDocumentModel.number)) {
                            // lpDocumentModel.number 既文档服务器分配的fileId
                            uploadingQueue.poll();
                            continueQueue();
                        }
                    }
                });
    }

    @Override
    public void unSubscribe() {
        LPRxUtils.unSubscribe(subscriptionOfDocAdd);
    }

    @Override
    public void destroy() {
        view = null;
        routerListener = null;
    }

    @Override
    public IDocumentModel getItem(int position) {
        if (position < addedDocuments.size()) {
            return addedDocuments.get(position);
        } else {
            int count = position - addedDocuments.size();
            return (DocumentUploadingModel) uploadingQueue.toArray()[count];
        }
    }

    @Override
    public int getCount() {
        return addedDocuments.size() + uploadingQueue.size();
    }

    @Override
    public void uploadNewPics(List<String> picsPath) {
        for (String path : picsPath) {
            DocumentUploadingModel model = new DocumentUploadingModel(path);
            uploadingQueue.offer(model);
        }
        startQueue();
    }

    private void startQueue() {
        for (final DocumentUploadingModel model : uploadingQueue) {
            if (model.status == DocumentUploadingModel.INITIAL) {
                routerListener.getLiveRoom().getDocListVM().uploadImage(model.imgPath)
                        .subscribe(new LPErrorPrintSubscriber<LPUploadDocModel>() {
                            @Override
                            public void call(LPUploadDocModel lpUploadDocModel) {
                                model.uploadModel = lpUploadDocModel;
                                model.status = DocumentUploadingModel.UPLOADED;
                            }
                        });
                model.status = DocumentUploadingModel.UPLOADING;
            }
        }
    }

    private void continueQueue() {
        DocumentUploadingModel model = uploadingQueue.peek();
        if (model == null) return;
        if (model.status == DocumentUploadingModel.UPLOADED) {
            routerListener.getLiveRoom().getDocListVM().addDocument(String.valueOf(model.uploadModel.fileId)
                    , model.uploadModel.fext, model.uploadModel.name, model.uploadModel.width, model.uploadModel.height, model.uploadModel.url);
            model.status = DocumentUploadingModel.WAIT_SIGNAL;
        }
    }

    @Override
    public void selectItem(int position) {

    }

    @Override
    public void deselectItem(int position) {

    }

    @Override
    public void removeSelectedItems() {

    }

}
