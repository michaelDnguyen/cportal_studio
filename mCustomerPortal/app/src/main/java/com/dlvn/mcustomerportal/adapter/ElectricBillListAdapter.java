package com.dlvn.mcustomerportal.adapter;

import java.io.File;
import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.PdfViewActivity;
import com.dlvn.mcustomerportal.adapter.model.ElectricBillModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetTaxInvoiceRequest;
import com.dlvn.mcustomerportal.services.model.response.GetTaxInvoiceResponse;
import com.dlvn.mcustomerportal.services.model.response.GetTaxInvoiceResult;
import com.dlvn.mcustomerportal.utils.FileUtils;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Adapter danh sách hợp đồng
 *
 * @author nn.tai
 * @date Dec 7, 2017
 */
public class ElectricBillListAdapter extends BaseAdapter {

    private static final String TAG = "ElectricBillListAdapter";

    List<ElectricBillModel> lstData;
    Context context;
    ServicesRequest svRequester;

    public ElectricBillListAdapter(Context c, List<ElectricBillModel> data, ServicesRequest svr) {
        context = c;
        lstData = data;
        svRequester = svr;
    }

    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Object getItem(int position) {
        return lstData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<ElectricBillModel> data) {
        if (lstData != data)
            lstData.addAll(data);
        notifyDataSetChanged();
    }

    public void reFreshData(List<ElectricBillModel> data) {
        if (lstData != data) {
            lstData.clear();
            lstData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public List<ElectricBillModel> getData() {
        return lstData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ElectricBillModel item = lstData.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_electricbill, null);
            holder = new ViewHolder();

            holder.tvMaSoHD = convertView.findViewById(R.id.tvSoHoaDon);
            holder.tvSoHopDong = convertView.findViewById(R.id.tvSoHopDong);
            holder.tvKhachHang = convertView.findViewById(R.id.tvKhachHang);
            holder.tvSoTien = convertView.findViewById(R.id.tvAmount);
            holder.tvNgayLap = convertView.findViewById(R.id.tvNgayLap);

            holder.btnPDF = convertView.findViewById(R.id.lloPDF);
            holder.btnXML = convertView.findViewById(R.id.lloXML);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (item != null) {
            holder.tvMaSoHD.setText(item.getTaxInvoiceID());
            holder.tvSoHopDong.setText(item.getPolicyNo());
            holder.tvKhachHang.setText(item.getInvoiceSign());
            holder.tvSoTien.setText(Utilities.formatMoneyToVND(item.getAmount()));
            holder.tvNgayLap.setText(item.getPrintedDate());

            holder.btnPDF.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.message_alert_download_file))
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    File fileDir = new File(
                                            context.getExternalFilesDir(null) + File.separator + item.getPolicyNo());
                                    if (!fileDir.exists())
                                        fileDir.mkdir();

                                    File filePdf = new File(fileDir.getAbsoluteFile() + File.separator + item.getPolicyNo() + ".pdf");
                                    File fileXml = new File(fileDir.getAbsoluteFile() + File.separator + item.getPolicyNo() + ".xml");

                                    if (filePdf.exists()) {
//                                        FileUtils.openFile(context, filePdf);
                                        Intent intent = new Intent(context, PdfViewActivity.class);
                                        intent.putExtra(PdfViewActivity.INT_FILEPATH_PDF, filePdf.getAbsolutePath());
                                        context.startActivity(intent);
                                    } else
                                        new doDownloadTaxInvoiceTask(context, item, filePdf, fileXml).execute();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Không", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });

            holder.btnXML.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
                    builder.setMessage(context.getString(R.string.message_alert_download_file))
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    File fileDir = new File(
                                            context.getExternalFilesDir(null) + File.separator + item.getPolicyNo());
                                    if (!fileDir.exists())
                                        fileDir.mkdir();

                                    File filePdf = new File(fileDir.getAbsoluteFile() + File.separator + item.getPolicyNo() + ".pdf");
                                    File fileXml = new File(fileDir.getAbsoluteFile() + File.separator + item.getPolicyNo() + ".xml");

                                    if (fileXml.exists())
                                        FileUtils.openFile(context, fileXml);
                                    else
                                        new doDownloadTaxInvoiceTask(context, item, filePdf, fileXml).execute();
                                    dialog.dismiss();

                                }
                            }).setNegativeButton("Không", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            });
        }

        return convertView;
    }

    /**
     * Download file XML & PDF
     */
    private class doDownloadTaxInvoiceTask extends AsyncTask<Void, Void, Response<GetTaxInvoiceResponse>> {

        Context context;
        ElectricBillModel model;
        File filePdf, fileXml;

        public doDownloadTaxInvoiceTask(Context c, ElectricBillModel mo, File pdf, File xml) {
            this.context = c;
            this.model = mo;
            filePdf = pdf;
            fileXml = xml;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Response<GetTaxInvoiceResponse> doInBackground(Void... voids) {
            Response<GetTaxInvoiceResponse> res = null;

            try {
                GetTaxInvoiceRequest data = new GetTaxInvoiceRequest();

                data.setDeviceToken(CustomPref.getFirebaseToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);
                data.setAPIToken(CustomPref.getAPIToken(context));

                data.setClientID(CustomPref.getUserID(context));
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));

                data.setPolicyNo(model.getPolicyNo().trim());
                data.setAction(Constant.ACTION_TAXINVOICE_DETAIL);
                data.setInvoiceID(model.getTaxInvoiceID());
                data.setInvoiceSign(model.getInvoiceSign());

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetTaxInvoiceResponse> call = svRequester.GetTaxInvoice(request);
                res = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            return res;
        }

        @Override
        protected void onPostExecute(Response<GetTaxInvoiceResponse> success) {
            super.onPostExecute(success);

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetTaxInvoiceResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                GetTaxInvoiceResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        myLog.e(TAG, "Get Tax Invoice: " + result.getErrLog());

                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(context, context.getString(R.string.message_alert_relogin));
                                        }
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveAPIToken(context, result.getNewAPIToken());

                                        if (result.getElectricBill() != null) {
                                            ElectricBillModel mo = result.getElectricBill().get(0);
                                            if (mo != null) {

                                                //decode file Pdf
                                                try {
                                                    File fileTem = FileUtils.convertStringToDecodeByteBase64(mo.getpDFFile(), filePdf);
                                                    if (fileTem.exists()) {
                                                        Intent intent = new Intent(context, PdfViewActivity.class);
                                                        intent.putExtra(PdfViewActivity.INT_FILEPATH_PDF, filePdf.getAbsolutePath());
                                                        context.startActivity(intent);
                                                    }
//                                                    FileUtils.openFile(context, filePdf);
                                                } catch (Exception e) {
                                                    myLog.printTrace(e);
                                                }
                                                //decode file Xml
                                                try {
                                                    FileUtils.writeStringToFile(Constant.XML_SCHEMA_HEADER + mo.getXMLFile(), fileXml);
                                                } catch (Exception e) {
                                                    myLog.printTrace(e);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            }
        }
    }

    private class ViewHolder {
        TextView tvMaSoHD, tvSoHopDong, tvKhachHang, tvSoTien, tvNgayLap;
        LinearLayout btnPDF, btnXML;
    }
}
