package com.lib_common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.lib_common.R;


/**
 * 性别弹窗
 * 
 * @author loar
 * 
 */
public class LoadingDialog extends Dialog {
	private static LoadingDialog dialog;
	private Context context;

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	protected LoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public LoadingDialog(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
	}

	public static LoadingDialog getInstance(Context context) {
		dialog = new LoadingDialog(context, R.style.dialogStyleCenter);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}

	public void setCanOutsideCancel(boolean canCancel) {
		this.setCanceledOnTouchOutside(canCancel);
	}

	public void showLoading() {
		if (!this.isShowing()) {
			this.show();
		}
	}

	public void dismissLoading() {
		if (this.isShowing()) {
			this.dismiss();
		}
	}

	private OnClickListener clickListener;

	public void setClickListener(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public interface OnClickListener {
		void onOutsideClick(View v);
	}
}
