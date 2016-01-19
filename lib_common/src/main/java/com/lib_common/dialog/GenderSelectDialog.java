package com.lib_common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lib_common.R;


/**
 * 性别弹窗
 *
 * @author loar
 */
public class GenderSelectDialog extends Dialog {
    public static final String MALE = "0";
    public static final String FEMALE = "1";
    private Context context;
    private TextView tvCancel;
    private TextView tvOk;
    private RadioGroup rgGender;
    private RadioButton rbFemale;
    private RadioButton rbMale;
    private String gender = MALE;

    public GenderSelectDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected GenderSelectDialog(Context context, boolean cancelable,
                                 OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public GenderSelectDialog(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gender_select);
        try {
            rgGender = (RadioGroup) findViewById(R.id.gender_select_rg_gender);
            rbFemale = (RadioButton) findViewById(R.id.gender_select_rb_female);
            rbMale = (RadioButton) findViewById(R.id.gender_select_rb_male);
            tvOk = (TextView) findViewById(R.id.gender_select_tv_ok);
            tvCancel = (TextView) findViewById(R.id.gender_select_tv_cancel);
            setListener();
            initGender();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        tvOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int id = rgGender.getCheckedRadioButtonId();
                if (id == R.id.gender_select_rb_female) {
                    gender = FEMALE;
                } else if (id == R.id.gender_select_rb_male) {
                    gender = MALE;
                }
                if (selectCallBack != null) {
                    selectCallBack.ok(gender);
                }
                dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private void initGender() {
        if (MALE.equals(gender)) {
            rbMale.setChecked(true);
        } else if (FEMALE.equals(gender)) {
            rbFemale.setChecked(true);
        }
    }

    private SelectCallBack selectCallBack;

    public void setSelectCallBack(SelectCallBack selectCallBack) {
        this.selectCallBack = selectCallBack;
    }

    public interface SelectCallBack {
        void ok(String gender);
    }

}
