package com.dlvn.mcustomerportal.adapter.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * @author nn.tai
 * @date Jan 4, 2018
 * @Do TODO
 */
public class TransactionModel implements Parcelable{

	@SerializedName("CreateDate")
	@Expose
	private String createDate;
	@SerializedName("TransactionDetailID")
	@Expose
	private String transactionDetailID;
	@SerializedName("PolicyNo")
	@Expose
	private String policyNo;
	@SerializedName("Refno")
	@Expose
	private String refno;
	@SerializedName("Amount")
	@Expose
	private String amount;
	@SerializedName("Status")
	@Expose
	private String status;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTransactionDetailID() {
		return transactionDetailID;
	}

	public void setTransactionDetailID(String transactionDetailID) {
		this.transactionDetailID = transactionDetailID;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public TransactionModel(Parcel in){
		transactionDetailID = in.readString();
		policyNo = in.readString();
		amount = in.readString();
		status = in.readString();
		createDate = in.readString();
		refno = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(transactionDetailID);
		dest.writeString(policyNo);
		dest.writeString(amount);
		dest.writeString(status);
		dest.writeString(createDate);
		dest.writeString(refno);
	}

	public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {

		@Override
		public TransactionModel[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TransactionModel[size];
		}

		@Override
		public TransactionModel createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new TransactionModel(source);
		}
	};
}
