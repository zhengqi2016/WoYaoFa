package com.woyaofa.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.dialog.TipDialog;
import com.lib_common.util.PCAUtil;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.ApiAddress;
import com.woyaofa.bean.AddressBean;
import com.woyaofa.bean.PCABean;
import com.woyaofa.ui.widget.LinkageDialogView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 地址编辑
 */
public class AddressEditActivity extends MBaseActivity {

    public static final String BUNDLE_KEY_BEAN = "bean";
    public static final String BUNDLE_KEY_NO_DEFAULT = "default";

    @Bind(R.id.activity_address_edit_et_address)
    EditText etAddresses;
    @Bind(R.id.activity_address_edit_tv_pca)
    TextView tvPca;
    @Bind(R.id.activity_address_edit_et_name)
    EditText etName;
    @Bind(R.id.activity_address_edit_et_phone)
    EditText etPhone;
    @Bind(R.id.activity_address_edit_et_street)
    EditText etStreet;
    @Bind(R.id.activity_address_edit_tv_default_address)
    TextView tvDefault;

    private AddressBean addressBean;
    private PCABean pca;
    private LinkageDialogView linkageDialogView;
    private String province;
    private String city;
    private String district;
    private boolean noDefault;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_address_edit);
        init();
        setListener();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        addressBean = (AddressBean) bundle.getSerializable(BUNDLE_KEY_BEAN);
        noDefault = bundle.getBoolean(BUNDLE_KEY_NO_DEFAULT, false);
    }

    private void init() {
        hvHead.setRight("修改", 0);
        if (addressBean != null) {
            province = addressBean.getProvince();
            city = addressBean.getCity();
            district = addressBean.getDistrict();
            etName.setText(addressBean.getName());
            etPhone.setText(addressBean.getPhone());
            etStreet.setText(addressBean.getStreet());
            etAddresses.setText(addressBean.getDetail());
            tvPca.setText(addressBean.getProvince() + " " + addressBean.getCity() + " " + addressBean.getDistrict());
        }
        if (noDefault) {
            tvDefault.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        hvHead.setOnClickRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestSubmit();
            }
        });
    }

    private void onRequestSubmit() {
        RequestBody body = new FormEncodingBuilder()
                .add("id", "" + addressBean.getId())
                .add("name", etName.getText().toString())
                .add("phone", etPhone.getText().toString())
                .add("province", province)
                .add("city", city)
                .add("district", district)
                .add("street", etStreet.getText().toString())
                .add("detail", etAddresses.getText().toString())
                .add("type", "" + addressBean.getType())
                .build();
        ApiAddress.getInstance().postModify(this, body);
        showLoading();
    }

    public void showPCADialog() {
        if (linkageDialogView == null) {
            if (pca == null) {
                pca = PCAUtil.getPCA(this, "pca.json", PCABean.class);
            }
            linkageDialogView = new LinkageDialogView(this);
            linkageDialogView.setTitle("城市选择");
            linkageDialogView.setPca(pca);
            linkageDialogView.setOnOkListener(new LinkageDialogView.OnOkListener() {
                @Override
                public void okClick(LinkageDialogView linkageDialogView, View v, String pName, String cName, String aName) {
                    province = pName;
                    city = cName;
                    district = aName;
                    tvPca.setText(pName + " " + cName + " " + aName);
                    linkageDialogView.dismiss();
                }
            });
        }
        linkageDialogView.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        pca = PCAUtil.getPCA(this, "pca.json", PCABean.class);
    }

    @OnClick(R.id.activity_address_edit_tv_pca)
    public void onPca(View v) {
        showPCADialog();
    }

    @OnClick(R.id.activity_address_edit_tv_default_address)
    public void onDefaultAddress(View v) {
        List<Object> body = new ArrayList<>();
        body.add(addressBean.getId());
        body.add(MApplication.getApp().getAccount().getUser().getId());
        ApiAddress.getInstance().getSetDefault(this, body);
    }

    @OnClick(R.id.activity_address_edit_tv_delete)
    public void onDelete(View v) {
        new TipDialog(this, R.style.tipDialog)
                .setContent("确认退出吗？", null, null)
                .setOnListener(new TipDialog.OnMOKListener() {
                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        dialog.dismiss();
                        requestDelete();
                    }
                }, new TipDialog.OnMCancelListener() {
                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        dialog.dismiss();
                    }
                });

    }

    private void requestDelete() {
        List<Object> body = new ArrayList<>();
        body.add(addressBean.getId());
        ApiAddress.getInstance().getDelete(this, body);
    }

    private Runnable sucRunnable = new Runnable() {
        @Override
        public void run() {
            ToastUtil.toastAlways(AddressEditActivity.this, "操作成功");
            MApplication.getApp().updateActivity(AddressBookActivity.class);
            onBack(null);
        }
    };

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        if (url.equals(ApiAddress.URL_SET_DEFAULT)) {
            MApplication.getApp().getAccountWithLogin().getUser().setAddressBook(addressBean);
            updateView(new Runnable() {
                @Override
                public void run() {
                    MApplication.getApp().updateActivity(AddressBookActivity.class);
                    onBack(null);
                }
            });
        } else {
            updateView(sucRunnable);
        }
    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastAlways(AddressEditActivity.this, "操作失败，请重新尝试！");
            }
        });
    }

    @Override
    public void setRequestFinish(String url) {
        super.setRequestFinish(url);
        updateView(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        });
    }
}
