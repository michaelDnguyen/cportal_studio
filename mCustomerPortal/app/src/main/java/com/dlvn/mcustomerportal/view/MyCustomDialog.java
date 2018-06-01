package com.dlvn.mcustomerportal.view;

import com.dlvn.mcustomerportal.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCustomDialog extends Dialog {

	public MyCustomDialog(Context context) {
		super(context);
	}

	public MyCustomDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * @author nn.tai
	 */
	public static class Builder {
		private Context context;
		private int topImageId;
		private String title;
		private String message;
		private Drawable drawable = null;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private boolean isCancelOnTouchOut = true;

		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Đặt icon trên cùng
		 */
		public Builder setTopImage(int id) {
			this.topImageId = id;
			return this;
		}

		/**
		 * Set message title
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Set message content
		 * 
		 * @param message
		 * @return
		 */
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setDrawable(Drawable drawable_id) {
			this.drawable = drawable_id;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set listener + title for OK button - possitive button
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set listener + title for Cancel button - negative button
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setCancelOnTouchOut(boolean isCancelOnTouchOut) {
			this.isCancelOnTouchOut = isCancelOnTouchOut;
			return this;
		}

		/**
		 * Create AlertDialog
		 * 
		 * @return
		 */
		public MyCustomDialog create() {
			LayoutInflater inflater = LayoutInflater.from(context);

			final MyCustomDialog dialog = new MyCustomDialog(context, R.style.DialogStyle);

			View layout = null;
			if (null != contentView) {
				layout = contentView;
			} else {
				layout = inflater.inflate(R.layout.my_custom_dialog, null);
			}

			// Set icon into Top
			ImageView top_image = (ImageView) layout.findViewById(R.id.top_image);
			if (0 == topImageId) {
				top_image.setVisibility(View.GONE);
			} else {
				top_image.setImageResource(topImageId);
			}

			// Set title
			TextView titleView = (TextView) layout.findViewById(R.id.title);
			if (null == title) {
				titleView.setVisibility(View.GONE);
			} else {
				if (drawable != null) {
					titleView.setCompoundDrawables(drawable, null, null, null);
				}
				titleView.setText(title);
			}
			// Set message content
			TextView messageView = (TextView) layout.findViewById(R.id.message);
			if (null == message) {
				messageView.setVisibility(View.GONE);
			} else {
				messageView.setText(message);
			}

			ImageView imvThanhNgan = (ImageView) layout.findViewById(R.id.imvThanhNgan);

			// Setup button action Positive
			RelativeLayout sure_layout = (RelativeLayout) layout.findViewById(R.id.sure_layout);
			TextView sure_text = (TextView) layout.findViewById(R.id.sure_text);
			if (TextUtils.isEmpty(positiveButtonText) || null == positiveButtonClickListener) {
				sure_layout.setVisibility(View.GONE);
				imvThanhNgan.setVisibility(View.GONE);
			} else {
				sure_text.setText(positiveButtonText);
				sure_layout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
					}
				});
			}

			// Setup button action Negative
			RelativeLayout quit_layout = (RelativeLayout) layout.findViewById(R.id.quit_layout);

			TextView quit_text = (TextView) layout.findViewById(R.id.quit_text);
			if (TextUtils.isEmpty(negativeButtonText) || null == negativeButtonClickListener) {
				quit_layout.setVisibility(View.GONE);
				imvThanhNgan.setVisibility(View.GONE);
			} else {
				quit_text.setText(negativeButtonText);
				quit_layout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
					}
				});
			}

			if (null != positiveButtonText && null == negativeButtonText) {
				if (null == positiveButtonClickListener) {
					sure_layout.setVisibility(View.VISIBLE);

					sure_layout.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
				}
			}

			// Setup layout and set contentView for show dialog
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			dialog.setContentView(layout, params);
			dialog.setCanceledOnTouchOutside(isCancelOnTouchOut);
			dialog.setCancelable(isCancelOnTouchOut);

			return dialog;
		}
	}

}
