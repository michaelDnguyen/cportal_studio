package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetailModel implements Parcelable {

    @SerializedName("ClientId")
    @Expose
    String maKhachHang;
    @SerializedName("FullName")
    @Expose
    String tenKhachHang;
    String soHopDong;

    String cmnd;
    @SerializedName("Email")
    @Expose
    String email;
    @SerializedName("ContactPhone")
    @Expose
    String phone;
    String phoneHome, phoneWork;
    String dob;
    String gender;
    @SerializedName("HomeAddress")
    @Expose
    String addressHome;
    @SerializedName("BusinessAddress")
    @Expose
    String addressWork;

    String tenNguoiNop;

    String phiBHGoc;
    String phiAplGoc;
    String phiOplGoc;

    String phiBaoHiem;
    String phiApl;
    String phiOpl;

    String tongSoTien;
    // status of transaction: 0-sucess; -1-init; 2,3,4,5...-failed
    int status;

    @SerializedName("ClientType")
    @Expose
    String clientType;
    String titleType;

    public PaymentDetailModel() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PaymentDetailModel(Parcel in) {
        maKhachHang = in.readString();
        tenKhachHang = in.readString();
        soHopDong = in.readString();

        cmnd = in.readString();
        email = in.readString();
        phone = in.readString();
        phoneHome = in.readString();
        phoneWork = in.readString();
        dob = in.readString();
        gender = in.readString();
        addressHome = in.readString();
        addressWork = in.readString();

        phiBHGoc = in.readString();
        phiAplGoc = in.readString();
        phiOplGoc = in.readString();

        phiBaoHiem = in.readString();
        phiApl = in.readString();
        phiOpl = in.readString();

        tongSoTien = in.readString();
        status = in.readInt();
        clientType = in.readString();
        titleType = in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(maKhachHang);
        dest.writeString(tenKhachHang);
        dest.writeString(soHopDong);

        dest.writeString(cmnd);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(phoneHome);
        dest.writeString(phoneWork);
        dest.writeString(dob);
        dest.writeString(gender);
        dest.writeString(addressHome);
        dest.writeString(addressWork);

        dest.writeString(phiBHGoc);
        dest.writeString(phiAplGoc);
        dest.writeString(phiOplGoc);
        dest.writeString(phiBaoHiem);
        dest.writeString(phiApl);
        dest.writeString(phiOpl);
        dest.writeString(tongSoTien);
        dest.writeInt(status);
        dest.writeString(clientType);
        dest.writeString(titleType);
    }

    public static final Creator<PaymentDetailModel> CREATOR = new Creator<PaymentDetailModel>() {

        @Override
        public PaymentDetailModel[] newArray(int size) {
            // TODO Auto-generated method stub
            return new PaymentDetailModel[size];
        }

        @Override
        public PaymentDetailModel createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new PaymentDetailModel(source);
        }
    };

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoHopDong() {
        return soHopDong;
    }

    public void setSoHopDong(String soHopDong) {
        this.soHopDong = soHopDong;
    }

    public String getPhiBHGoc() {
        return phiBHGoc;
    }

    public void setPhiBHGoc(String phiBHGoc) {
        this.phiBHGoc = phiBHGoc;
    }

    public String getPhiAplGoc() {
        return phiAplGoc;
    }

    public void setPhiAplGoc(String phiAplGoc) {
        this.phiAplGoc = phiAplGoc;
    }

    public String getPhiOplGoc() {
        return phiOplGoc;
    }

    public void setPhiOplGoc(String phiOplGoc) {
        this.phiOplGoc = phiOplGoc;
    }

    public String getPhiBaoHiem() {
        return phiBaoHiem;
    }

    public void setPhiBaoHiem(String phiBaoHiem) {
        this.phiBaoHiem = phiBaoHiem;
    }

    public String getPhiApl() {
        return phiApl;
    }

    public void setPhiApl(String phiApl) {
        this.phiApl = phiApl;
    }

    public String getPhiOpl() {
        return phiOpl;
    }

    public void setPhiOpl(String phiOpl) {
        this.phiOpl = phiOpl;
    }

    public String getTongSoTien() {
        return tongSoTien;
    }

    public void setTongSoTien(String tongSoTien) {
        this.tongSoTien = tongSoTien;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTenNguoiNop() {
        return tenNguoiNop;
    }

    public void setTenNguoiNop(String tenNguoiNop) {
        this.tenNguoiNop = tenNguoiNop;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhoneHome() {
        return phoneHome;
    }

    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

    public String getPhoneWork() {
        return phoneWork;
    }

    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddressHome() {
        return addressHome;
    }

    public void setAddressHome(String addressHome) {
        this.addressHome = addressHome;
    }

    public String getAddressWork() {
        return addressWork;
    }

    public void setAddressWork(String addressWork) {
        this.addressWork = addressWork;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }
}
