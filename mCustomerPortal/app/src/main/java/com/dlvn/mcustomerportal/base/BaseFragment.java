package com.dlvn.mcustomerportal.base;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public abstract class BaseFragment extends Fragment {
	
	public abstract String getTagFragment();

	public abstract boolean isAddToBackStack();

	private View root;
	protected Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	public <V extends View> V findView(int viewId) {
		return (V) root.findViewById(viewId);
	}

	/*
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) { Log.d("lifeCycle",
	 * "onCreateView - " + this.getClass().getName());
	 * 
	 * //set lai current fragment root = inflater.inflate(layoutID(), container,
	 * false); initViews(root, savedInstanceState); return root; }
	 */

	ProgressDialog mProgressDialog;

	protected void showProgressDialog(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setCancelable(false);
		}
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
	}

	protected void dismissProgressDialog() {
		if (mProgressDialog == null)
			throw new RuntimeException();
		mProgressDialog.dismiss();
	}
}
