package com.woyaofa.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lib_common.adapter.ViewHolder;
import com.woyaofa.R;
import com.woyaofa.bean.PCABean;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.WheelViewAdapter;


public class LinkageDialogView extends Dialog {

    private Context context;
    private WheelView wv0;
    private WheelView wv1;
    private WheelView wv2;
    private TextView tvOk;
    private TextView tvCancel;
    private PCABean pca;
    private List<PCABean.C> cs = new ArrayList<>();
    private List<PCABean.A> as = new ArrayList<>();
    private String aName;
    private String cName;
    private String pName;

    public LinkageDialogView(Context context) {
        super(context);
        this.context = context;
    }

    public LinkageDialogView(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected LinkageDialogView(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_linkage_dialog);
        init();
    }

    private void init() {
        tvOk = (TextView) findViewById(R.id.linkage_dialog_tv_ok);
        tvCancel = (TextView) findViewById(R.id.linkage_dialog_tv_cancel);
        wv1 = (WheelView) findViewById(R.id.linkage_dialog_wv_1);
        wv2 = (WheelView) findViewById(R.id.linkage_dialog_wv_2);
        wv0 = (WheelView) findViewById(R.id.linkage_dialog_wv_0);
        wv0.setVisibleItems(5);
        wv1.setVisibleItems(5);
        wv2.setVisibleItems(5);
        wv0.setViewAdapter(new WheelViewAdapter() {
            @Override
            public int getItemsCount() {
                return pca.getPs().size();
            }

            @Override
            public View getItem(int index, View convertView, ViewGroup parent) {
                ViewHolder holder = ViewHolder.getInstance(context, R.layout.adapter_view_linkage_dialog, convertView, parent);
                TextView tv = holder.getViewById(R.id.linkage_dialog_tv, TextView.class);
                tv.setText(pca.getPs().get(index).toString());
                return holder.getConvertView();
            }

            @Override
            public View getEmptyItem(View convertView, ViewGroup parent) {
                return null;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }
        });
        updateAdapter12();

        wv0.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                pName = pca.getPs().get(newValue).getName();
                cs = pca.getPs().get(newValue).getCs();
                cName = cs.get(0).getName();
                as = cs.get(0).getAs();
                aName = as.get(0).getName();
                updateAdapter12();
                wv1.setCurrentItem(0);
            }
        });
        wv1.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                cName = cs.get(newValue).getName();
                as = cs.get(newValue).getAs();
                aName = as.get(0).getName();
                updateAdapter2();
                wv2.setCurrentItem(0);
            }
        });
        wv2.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                aName = as.get(newValue).getName();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOkListener != null) {
                    onOkListener.okClick(LinkageDialogView.this, v, pName, cName, aName);
                }
            }
        });
    }

    public void setPca(PCABean pca) {
        this.pca = pca;
        pName = pca.getPs().get(0).getName();
        cs = pca.getPs().get(0).getCs();
        cName = cs.get(0).getName();
        as = cs.get(0).getAs();
        aName = as.get(0).getName();
    }

    public void updateAdapter12() {
        wv1.setViewAdapter(new WheelViewAdapter() {
            @Override
            public int getItemsCount() {
                return cs.size();
            }

            @Override
            public View getItem(int index, View convertView, ViewGroup parent) {
                ViewHolder holder = ViewHolder.getInstance(context, R.layout.adapter_view_linkage_dialog, convertView, parent);
                TextView tv = holder.getViewById(R.id.linkage_dialog_tv, TextView.class);
                tv.setText(cs.get(index).toString());
                return holder.getConvertView();
            }

            @Override
            public View getEmptyItem(View convertView, ViewGroup parent) {
                return null;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }
        });
        updateAdapter2();
    }

    public void updateAdapter2() {
        wv2.setViewAdapter(new WheelViewAdapter() {
            @Override
            public int getItemsCount() {
                return as.size();
            }

            @Override
            public View getItem(int index, View convertView, ViewGroup parent) {
                ViewHolder holder = ViewHolder.getInstance(context, R.layout.adapter_view_linkage_dialog, convertView, parent);
                TextView tv = holder.getViewById(R.id.linkage_dialog_tv, TextView.class);
                tv.setText(as.get(index).toString());
                return holder.getConvertView();
            }

            @Override
            public View getEmptyItem(View convertView, ViewGroup parent) {
                return null;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {
            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }
        });
    }

    private OnOkListener onOkListener;

    public void setOnOkListener(OnOkListener onOkListener) {
        this.onOkListener = onOkListener;
    }

    public static interface OnOkListener {
        void okClick(LinkageDialogView linkageDialogView, View v, String pName, String cName, String aName);
    }
}
