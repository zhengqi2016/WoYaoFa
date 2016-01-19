package com.lib_common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lib_common.R;


/**
 * 提示对话框
 *
 * @author loar
 */
public class DateTimePickerDialog extends Dialog {
    private Context context;
    private View view;
    private TextView tvOk;
    private TextView tvCancel;
    private TextView tvTitle;
    private View vDivider;
    private DatePicker dpDate;
    private TimePicker tpTime;
    private int hourOfDay;
    private int minute;

    public DateTimePickerDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected DateTimePickerDialog(Context context, boolean cancelable,
                                   OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public DateTimePickerDialog(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        view = View.inflate(context, R.layout.dialog_date_time_picker, null);
        tvOk = (TextView) view.findViewById(R.id.dialog_date_time_picker_tv_ok);
        tvCancel = (TextView) view.findViewById(R.id.dialog_date_time_picker_tv_cancel);
        tvTitle = (TextView) view.findViewById(R.id.dialog_date_time_picker_tv_title);
        dpDate = (DatePicker) view.findViewById(R.id.dialog_date_time_picker_dp_date);
        tpTime = (TimePicker) view.findViewById(R.id.dialog_date_time_picker_tp_time);
        vDivider = (View) view.findViewById(R.id.dialog_date_time_picker_v_divider);
        tpTime.setIs24HourView(true);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        tpTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                DateTimePickerDialog.this.hourOfDay = hourOfDay;
                DateTimePickerDialog.this.minute = minute;
            }
        });
    }

    public DateTimePickerDialog setCanOutsideCancel(boolean canCancel) {
        this.setCanceledOnTouchOutside(canCancel);
        return this;
    }

    public DateTimePickerDialog setTitle(String title, String color, String bgColor) {
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

    /**
     * @param text
     * @param size
     * @param color
     * @param bgResId
     * @param isVisibility
     * @return
     */
    public DateTimePickerDialog setCancelView(String text, float size, String color,
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
     * @param text
     * @param size
     * @param color
     * @param bgResId
     * @param isVisibility
     * @return
     */
    public DateTimePickerDialog setOkView(String text, float size, String color,
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

    public DateTimePickerDialog setOnListener(final OnMOKListener okListener,
                                              final OnMCancelListener cancelListener) {
        if (cancelListener != null) {
            tvCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (cancelListener != null) {
                        cancelListener.onClick(DateTimePickerDialog.this, tvOk);
                    }
                }
            });
        }
        if (okListener != null) {
            tvOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (okListener != null) {
                        okListener.onClick(DateTimePickerDialog.this, tvCancel, dpDate.getYear(), dpDate.getMonth(), dpDate.getDayOfMonth(), hourOfDay, minute);
                    }
                }
            });
        }
        return this;
    }

    public interface OnMOKListener {
        void onClick(DateTimePickerDialog dialog, View view, int year, int month, int dayOfMonth, int hour, int minute);
    }

    public interface OnMCancelListener {
        void onClick(DateTimePickerDialog dialog, View view);
    }
}
