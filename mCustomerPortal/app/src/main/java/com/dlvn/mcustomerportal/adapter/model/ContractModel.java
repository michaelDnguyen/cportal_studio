package com.dlvn.mcustomerportal.adapter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model data of Hop Dong BH
 * @author nn.tai
 * @date Dec 18, 2017
 */
public class ContractModel implements Parcelable {

	String soHopDong;
	String tenSanPham;
	boolean isActive;
	long amount;
	String activeDate;
	String endDate;

	public ContractModel(String soHopDong, String tenSanPham, boolean isActive, long amount, String activeDate,
			String endDate) {
		super();
		this.soHopDong = soHopDong;
		this.tenSanPham = tenSanPham;
		this.isActive = isActive;
		this.amount = amount;
		this.activeDate = activeDate;
		this.endDate = endDate;
	}

	public ContractModel(Parcel in) {
		soHopDong = in.readString();
		tenSanPham = in.readString();
		isActive = in.readByte() != 0;
		amount = in.readLong();
		activeDate = in.readString();
		endDate = in.readString();
	}

	public ContractModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSoHopDong() {
		return soHopDong;
	}

	public void setSoHopDong(String soHopDong) {
		this.soHopDong = soHopDong;
	}

	public String getTenSanPham() {
		return tenSanPham;
	}

	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(soHopDong);
		dest.writeString(tenSanPham);
		dest.writeByte((byte) (isActive ? 1 : 0));
		dest.writeLong(amount);
		dest.writeString(activeDate);
		dest.writeString(endDate);
	}
	
	public static final Creator<ContractModel> CREATOR = new Creator<ContractModel>() {
		
		@Override
		public ContractModel[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ContractModel[size];
		}
		
		@Override
		public ContractModel createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ContractModel(source);
		}
	};

}
