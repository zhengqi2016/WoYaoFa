package com.woyaofa.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.woyaofa.R;


public class InputDialog extends Dialog {

    private Context context;
    private EditText etContent;
    private TextView tvCancel;
    private TextView tvOk;
    private TextView tvTitle;
    private View view;

    public InputDialog(Context context) {
        super(context);
        this.context = context;
    }

    public InputDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected InputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        init();
    }

    private void init() {
//        view = View.inflate(context, R.layout.dialog_input, null);
        etContent = (EditText) findViewById(R.id.input_et_content);
        tvCancel = (TextView) findViewById(R.id.input_tv_cancel);
        tvOk = (TextView) findViewById(R.id.input_tv_ok);
        tvTitle = (TextView) findViewById(R.id.input_tv_title);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCallBackListener != null) {
                    onCallBackListener.ok(InputDialog.this, etContent.getText().toString());
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCallBackListener != null) {
                    onCallBackListener.cancel(InputDialog.this);
                }
            }
        });
        etContent.requestFocus();
    }

    public void setInit(String title, String content, String hint) {
        etContent.setText(content);
        etContent.setHint(hint);
        tvTitle.setText(title);
    }

    private OnCallBackListener onCallBackListener;

    public void setOnCallBackListener(OnCallBackListener onCallBackListener) {
        this.onCallBackListener = onCallBackListener;
    }

    public static interface OnCallBackListener {

        void ok(InputDialog inputDialog, String content);

        void cancel(InputDialog inputDialog);
    }
}
