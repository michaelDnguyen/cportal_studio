package com.dlvn.mcustomerportal.utils;

import java.util.Calendar;

import com.dlvn.mcustomerportal.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author nn.tai
 * @create Nov 6, 2017
 */
public class DialogUtils {

	public static void showDatePicker(Context context, String title, final EditText datePicker) {
		try {
			// Process to get Current Date
			int mYear, mMonth, mDay;
			String date = datePicker.getText().toString();
			final Calendar c = Calendar.getInstance();
			if (!TextUtils.isEmpty(date)) {
				String arr[] = date.split("/");
				if (arr.length != 3) {
					showAlertCustomDialog(context, context.getString(R.string.message_error_date));
					return;
				}
				mYear = Integer.valueOf(arr[2]);
				mMonth = Integer.valueOf(arr[1]) - 1;
				mDay = Integer.valueOf(arr[0]);
			} else {
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH);
				mDay = c.get(Calendar.DAY_OF_MONTH);
			}

			// Launch Date Picker Dialog
			DatePickerDialog dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

					StringBuilder dr = new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear + 1)
							.append("/").append(year);
					datePicker.setText(dr.toString());
					// NÃ„Â�BH = BMBH
				}
			}, mYear, mMonth, mDay);

			dpd.getDatePicker().setCalendarViewShown(false);
			dpd.setTitle(title);
			dpd.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void showAlertCustomDialog(Context context, String message) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.alert_dialog_ok);
		dialog.setCanceledOnTouchOutside(false);

		TextView txt = (TextView) dialog.findViewById(R.id.txtMessage);
		txt.setText(message);
		Button btnOk = (Button) dialog.findViewById(R.id.alert_ok);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		if (!((Activity) context).isFinishing())
			dialog.show();
	}

	public static void showAlertCustomDialog(Context context, String message, OnClickListener listener) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.alert_dialog_ok);
		dialog.setCanceledOnTouchOutside(false);

		TextView txt = (TextView) dialog.findViewById(R.id.txtMessage);
		txt.setText(message);
		Button btnOk = (Button) dialog.findViewById(R.id.alert_ok);
		btnOk.setOnClickListener(listener);
		if (!((Activity) context).isFinishing())
			dialog.show();
	}

	public static void showAlertDialogWithCallback(final Context context, final String errorMessage,
			DialogInterface.OnClickListener okCallback) {
		AlertDialog alter;
		alter = new AlertDialog.Builder(context).setMessage(errorMessage).setCancelable(false)
				.setPositiveButton(context.getString(R.string.confirm_yes), okCallback).create();
		alter.setCanceledOnTouchOutside(false);
		if (!((Activity) context).isFinishing())
			alter.show();
	}

	public static void showAlertDialogWithCallback(final Context context, final String errorMessage,
			DialogInterface.OnClickListener okCallback, DialogInterface.OnClickListener cancelCallback) {
		AlertDialog alter;
		alter = new AlertDialog.Builder(context).setMessage(errorMessage)
				.setPositiveButton(context.getString(R.string.confirm_no), cancelCallback)
				.setNegativeButton(context.getString(R.string.confirm_yes), okCallback).create();
		alter.setCanceledOnTouchOutside(false);
		if (!((Activity) context).isFinishing())
			alter.show();
	}

	public static void showAlertDialog(final Context context, final String errorMessage) {
		AlertDialog alter;
		alter = new AlertDialog.Builder(context).setMessage(errorMessage)
				.setPositiveButton(context.getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		alter.setCanceledOnTouchOutside(false);
		if (!((Activity) context).isFinishing())
			alter.show();
	}
}
