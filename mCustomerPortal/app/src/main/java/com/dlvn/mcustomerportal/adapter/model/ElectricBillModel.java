package com.dlvn.mcustomerportal.adapter.model;

/**
 * Model data of Hoa Don Dien Tu
 * 
 * @author nn.tai
 * @date Dec 18, 2017
 */
public class ElectricBillModel {

	String maHoaDon;
	String maHopDong;
	String tenKhachHang;
	String soTienBH;
	String ngayLap;

	String pathPDF;
	String pathXML;

	public ElectricBillModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ElectricBillModel(String maHoaDon, String maHopDong, String tenKhachHang, String soTienBH, String ngayLap,
			String pathPDF, String pathXML) {
		super();
		this.maHoaDon = maHoaDon;
		this.maHopDong = maHopDong;
		this.tenKhachHang = tenKhachHang;
		this.soTienBH = soTienBH;
		this.ngayLap = ngayLap;
		this.pathPDF = pathPDF;
		this.pathXML = pathXML;
	}

	public String getMaHopDong() {
		return maHopDong;
	}

	public void setMaHopDong(String maHopDong) {
		this.maHopDong = maHopDong;
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

	public String getTenKhachHang() {
		return tenKhachHang;
	}

	public void setTenKhachHang(String tenKhachHang) {
		this.tenKhachHang = tenKhachHang;
	}

	public String getSoTienBH() {
		return soTienBH;
	}

	public void setSoTienBH(String soTienBH) {
		this.soTienBH = soTienBH;
	}

	public String getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(String ngayLap) {
		this.ngayLap = ngayLap;
	}

	public String getPathPDF() {
		return pathPDF;
	}

	public void setPathPDF(String pathPDF) {
		this.pathPDF = pathPDF;
	}

	public String getPathXML() {
		return pathXML;
	}

	public void setPathXML(String pathXML) {
		this.pathXML = pathXML;
	}

}
