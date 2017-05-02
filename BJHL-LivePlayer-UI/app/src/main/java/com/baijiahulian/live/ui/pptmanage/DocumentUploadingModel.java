package com.baijiahulian.live.ui.pptmanage;

import com.baijiahulian.livecore.models.LPUploadDocModel;

/**
 * Created by Shubo on 2017/5/2.
 */

class DocumentUploadingModel implements IDocumentModel {
    static final int INITIAL = 0;
    static final int UPLOADING = 1;
    static final int UPLOADED = 2;
    static final int WAIT_SIGNAL = 3;

    DocumentUploadingModel(String imgPath) {
        this.imgPath = imgPath;
        status = INITIAL;
    }

    String imgPath;

    String fileName;

    String fileExt;

    LPUploadDocModel uploadModel;

    int status;

    int uploadPercentage;

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileExt() {
        return fileExt;
    }

    @Override
    public int getUploadPercent() {
        return 0;
    }

    @Override
    public int getStatus() {
        return status;
    }
}
