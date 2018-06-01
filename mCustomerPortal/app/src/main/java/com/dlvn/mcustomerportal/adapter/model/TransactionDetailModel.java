package com.dlvn.mcustomerportal.adapter.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * @author nn.tai
 * @date Jan 4, 2018
 * @Do TODO
 */
public class TransactionDetailModel implements Parcelable{

	String loaiGD;
	String soTien;
	String trangThai;

	public TransactionDetailModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransactionDetailModel(String loaiGD, String soTien, String trangThai) {
		super();
		this.loaiGD = loaiGD;
		this.soTien = soTien;
		this.trangThai = trangThai;
	}
	
	public TransactionDetailModel(Parcel in) {
		loaiGD = in.readString();
		soTien = in.readString();
		trangThai = in.readString();
	}

	public String getLoaiGD() {
		return loaiGD;
	}

	public void setLoaiGD(String loaiGD) {
		this.loaiGD = loaiGD;
	}

	public String getSoTien() {
		return soTien;
	}

	public void setSoTien(String soTien) {
		this.soTien = soTien;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(loaiGD);
		dest.writeString(soTien);
		dest.writeString(trangThai);
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
