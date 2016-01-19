package com.lib_common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.lib_common.R;


/**
 * 提示对话框
 * 
 * @author loar
 * 
 */
public class TipDialog extends Dialog {
	private static TipDialog dialog;
	private Context context;
	private View view;
	private TextView tvOk;
	private TextView tvCancel;
	private TextView tvContent;
	private TextView tvTitle;
	private View vDivider;

	public TipDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	protected TipDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public TipDialog(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		view = View.inflate(context, R.layout.dialog_tip, null);
		tvContent = (TextView) view.findViewById(R.id.dialog_tip_tv_content);
		tvOk = (TextView) view.findViewById(R.id.dialog_tip_tv_ok);
		tvCancel = (TextView) view.findViewById(R.id.dialog_tip_tv_cancel);
		tvTitle = (TextView) view.findViewById(R.id.dialog_tip_tv_title);
		vDivider = (View) view.findViewById(R.id.dialog_tip_v_divider);
		setContentView(view);
		setCancelable(true);
		setCanceledOnTouchOutside(false);

		tvCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TipDialog.this.dismiss();
			}
		});
		tvOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TipDialog.this.dismiss();
			}
		});
	}

	public static TipDialog getInstance(Context context) {
		dialog = new TipDialog(context, R.style.tipDialog);
		return dialog;
	}

	public TipDialog setCanOutsideCancel(boolean canCancel) {
		this.setCanceledOnTouchOutside(canCancel);
		return this;
	}

	public TipDialog setTitle(String title, String color, String bgColor) {
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(title);
		if (color != null) {
			tvTitle.setTextColor(Color.parseColor(color));
		}
		if (bgColor != null) {
			tvTitle.setBackgroundColor(Color.parseColor(bgColor));
		}
		return this;
	}

	public TipDialog setContent(String content, String color, String bgColor) {
		tvContent.setVisibility(View.VISIBLE);
		tvContent.setText(content);
		if (color != null) {
			tvContent.setTextColor(Color.parseColor(color));
		}
		if (bgColor != null) {
			tvContent.setBackgroundColor(Color.parseColor(bgColor));
		}
		return this;
	}

	/**
	 * 
	 * @param text
	 * @param size
	 * @param color
	 * @param bgResId
	 * @param isVisibility
	 * @return
	 */
	public TipDialog setCancelView(String text, float size, String color,
			int bgResId, boolean isVisibility) {
		tvCancel.setText(text);
		if (size != 0) {
			tvCancel.setTextSize(size);
		}
		if (color != null) {
			tvCancel.setTextColor(Color.parseColor(color));
		}
		if (bgResId != 0) {
			tvCancel.setBackgroundResource(bgResId);
		}
		if (isVisibility) {
			tvCancel.setVisibility(View.VISIBLE);
			if (tvOk.getVisibility() == View.GONE) {
				vDivider.setVisibility(View.GONE);
			}
		} else {
			tvCancel.setVisibility(View.GONE);
			vDivider.setVisibility(View.GONE);
		}
		return this;
	}

	/**
	 * 
	 * @param text
	 * @param size
	 * @param color
	 * @param bgResId
	 * @param isVisibility
	 * @return
	 */
	public TipDialog setOkView(String text, float size, String color,
			int bgResId, boolean isVisibility) {
		tvOk.setText(text);
		if (size != 0) {
			tvOk.setTextSize(size);
		}
		if (color != null) {
			tvOk.setTextColor(Color.parseColor(color));
		}
		if (bgResId != 0) {
			tvOk.setBackgroundResource(bgResId);
		}
		if (isVisibility) {
			tvOk.setVisibility(View.VISIBLE);
			if (tvCancel.getVisibility() == View.GONE) {
				vDivider.setVisibility(View.GONE);
			}
		} else {
			tvOk.setVisibility(View.GONE);
			vDivider.setVisibility(View.GONE);
		}
		return this;
	}

	public TipDialog setOnListener(final OnMOKListener okListener,
			final OnMCancelListener cancelListener) {
		if (cancelListener != null) {
			tvCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (cancelListener != null) {
						cancelListener.onClick(TipDialog.this, tvOk);
					}
				}
			});
		}
		if (okListener != null) {
			tvOk.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (okListener != null) {
						okListener.onClick(TipDialog.this, tvCancel);
					}
				}
			});
		}
		return this;
	}

	public interface OnMOKListener {
		void onClick(TipDialog dialog, View view);
	}

	public interface OnMCancelListener {
		void onClick(TipDialog dialog, View view);
	}
}
