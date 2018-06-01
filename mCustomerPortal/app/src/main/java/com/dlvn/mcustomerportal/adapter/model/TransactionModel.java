package com.dlvn.mcustomerportal.adapter.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * @author nn.tai
 * @date Jan 4, 2018
 * @Do TODO
 */
public class TransactionModel implements Parcelable {

	String maGiaoDich;
	String soHD;
	String soTien;
	String trangThai;
	String ngayGD;
	List<TransactionDetailModel> lstDetail;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(maGiaoDich);
		dest.writeString(soHD);
		dest.writeString(soTien);
		dest.writeString(trangThai);
		dest.writeString(ngayGD);
		dest.writeList(lstDetail);
	}

	public TransactionModel(Parcel in) {
		maGiaoDich = in.readString();
		soHD = in.readString();
		soTien = in.readString();
		trangThai = in.readString();
		ngayGD = in.readString();

		lstDetail = new ArrayList<>();
		in.readList(lstDetail, TransactionDetailModel.class.getClassLoader());
//		lstDetail = in.readArrayList(TransactionModel.class.getClassLoader());
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

	public TransactionModel(String maGiaoDich, String soHD, String soTien, String trangThai, String ngayGD,
			List<TransactionDetailModel> lstDetail) {
		super();
		this.maGiaoDich = maGiaoDich;
		this.soHD = soHD;
		this.soTien = soTien;
		this.trangThai = trangThai;
		this.ngayGD = ngayGD;
		this.lstDetail = lstDetail;
	}

	public String getMaGiaoDich() {
		return maGiaoDich;
	}

	public void setMaGiaoDich(String maGiaoDich) {
		this.maGiaoDich = maGiaoDich;
	}

	public String getSoHD() {
		return soHD;
	}

	public void setSoHD(String soHD) {
		this.soHD = soHD;
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

	public String getNgayGD() {
		return ngayGD;
	}

	public void setNgayGD(String ngayGD) {
		this.ngayGD = ngayGD;
	}

	public List<TransactionDetailModel> getLstDetail() {
		return lstDetail;
	}

	public void setLstDetail(List<TransactionDetailModel> lstDetail) {
		this.lstDetail = lstDetail;
	}

}
