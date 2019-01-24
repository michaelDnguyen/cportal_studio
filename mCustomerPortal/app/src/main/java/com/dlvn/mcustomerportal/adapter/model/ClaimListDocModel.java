package com.dlvn.mcustomerportal.adapter.model;

import com.dlvn.mcustomerportal.adapter.ClaimListDocAdapter;
import com.dlvn.mcustomerportal.database.entity.ClaimDocEntity;
import com.dlvn.mcustomerportal.database.entity.DocumentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nn.tai
 * modify 01-12-2018
 * Using description for a row of Document in screen mobile, have a Doctype {@Link ClaimDocEntity}, every row have list {@Link HorizontalRecyclerItemModel}
 */
public class ClaimListDocModel {

    //status position of item: normal , footer or header
    int statusItem;

    //doctype of document
    ClaimDocEntity claimDoc;
    //list document/image
    List<HorizontalRecyclerItemModel> lstDoc;

    boolean isEdit = true;
    boolean isImportant = false;
    boolean isComment = false;

    public int getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(int statusItem) {
        this.statusItem = statusItem;
    }

    public ClaimDocEntity getClaimDocItem() {
        return claimDoc;
    }

    public void setClaimDocItem(ClaimDocEntity docItem) {
        this.claimDoc = docItem;
    }

    public List<HorizontalRecyclerItemModel> getLstDoc() {
        return lstDoc;
    }

    public void setLstDoc(List<HorizontalRecyclerItemModel> lstDoc) {
        this.lstDoc = lstDoc;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }

    public boolean isComment() {
        return isComment;
    }

    public void setComment(boolean comment) {
        isComment = comment;
    }

    /**
     * init array not null
     */
    public ClaimListDocModel() {
        super();
        claimDoc = new ClaimDocEntity();
        lstDoc = new ArrayList<>();
    }

    public ClaimListDocModel(int statusItem, ClaimDocEntity claimDocEntity, List<HorizontalRecyclerItemModel> lstDoc, boolean isEdit,
                             boolean isImportant, boolean isComment) {
        super();
        this.statusItem = statusItem;
        this.claimDoc = claimDocEntity;
        this.lstDoc = lstDoc;
        this.isEdit = isEdit;
        this.isImportant = isImportant;
        this.isComment = isComment;
    }
}
