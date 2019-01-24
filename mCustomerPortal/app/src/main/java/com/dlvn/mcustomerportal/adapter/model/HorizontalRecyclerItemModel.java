package com.dlvn.mcustomerportal.adapter.model;

import com.dlvn.mcustomerportal.database.entity.DocumentEntity;

public class HorizontalRecyclerItemModel {

    public static final int STATE_ADD_IMAGE = 4949;

    int status;
    DocumentEntity docItem;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DocumentEntity getDocItem() {
        return docItem;
    }

    public void setDocItem(DocumentEntity docItem) {
        this.docItem = docItem;
    }

    public HorizontalRecyclerItemModel() {
        super();
        // TODO Auto-generated constructor stub
    }

    public HorizontalRecyclerItemModel(DocumentEntity docItem, int status) {
        super();
        this.status = status;
        this.docItem = docItem;
    }
}
