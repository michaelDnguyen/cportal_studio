package com.dlvn.mcustomerportal.adapter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * @author nn.tai
 * @date Jan 4, 2018
 * @Do TODO
 */
public class TransactionDetailModel implements Parcelable {

    @SerializedName("PolicyNo")
    @Expose
    private String policyNo;
    @SerializedName("ReceiptNo")
    @Expose
    private String receiptNo;
    @SerializedName("ReceiptType")
    @Expose
    private String receiptType;
    @SerializedName("Amount")
    @Expose
    private String amount;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public TransactionDetailModel(Parcel in) {
        policyNo = in.readString();
        receiptNo = in.readString();
        receiptType = in.readString();
        amount = in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(policyNo);
        dest.writeString(receiptNo);
        dest.writeString(receiptType);
        dest.writeString(amount);
    }

    public static final Creator<TransactionDetailModel> CREATOR = new Creator<TransactionDetailModel>() {

        @Override
        public TransactionDetailModel[] newArray(int size) {
            // TODO Auto-generated method stub
            return new TransactionDetailModel[size];
        }

        @Override
        public TransactionDetailModel createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new TransactionDetailModel(source);
        }
    };

}
