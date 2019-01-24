package com.dlvn.mcustomerportal.adapter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model data of Hoa Don Dien Tu
 *
 * @author nn.tai
 * @date Dec 18, 2017
 */
public class ElectricBillModel {

    @SerializedName("TaxInvoiceID")
    @Expose
    private String taxInvoiceID;
    @SerializedName("InvoiceSign")
    @Expose
    private String invoiceSign;
    @SerializedName("PolicyNo")
    @Expose
    private String policyNo;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("PrintedDate")
    @Expose
    private String printedDate;
    //    @SerializedName("PDFFile")
//    @Expose
//    private String pDFFile;
    @SerializedName("PDFFile")
    @Expose
    private List<Integer> pDFFile = null;
    @SerializedName("XMLFile")
    @Expose
    private String xMLFile;

    public List<Integer> getpDFFile() {
        return pDFFile;
    }

    public void setpDFFile(List<Integer> pDFFile) {
        this.pDFFile = pDFFile;
    }

    public String getXMLFile() {
        return xMLFile;
    }

    public void setXMLFile(String xMLFile) {
        this.xMLFile = xMLFile;
    }

    public String getTaxInvoiceID() {
        return taxInvoiceID;
    }

    public void setTaxInvoiceID(String taxInvoiceID) {
        this.taxInvoiceID = taxInvoiceID;
    }

    public String getInvoiceSign() {
        return invoiceSign;
    }

    public void setInvoiceSign(String invoiceSign) {
        this.invoiceSign = invoiceSign;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrintedDate() {
        return printedDate;
    }

    public void setPrintedDate(String printedDate) {
        this.printedDate = printedDate;
    }
}
