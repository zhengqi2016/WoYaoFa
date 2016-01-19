package com.woyaofa.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.util.PCAUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.ApiAddress;
import com.woyaofa.bean.PCABean;
import com.woyaofa.ui.widget.LinkageDialogView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 地址添加
 */
public class AddressAddActivity extends MBaseActivity {

    public static final String BUNDLE_KEY_ADDRESS_TYPE = "addressType";

    @Bind(R.id.activity_address_add_ll_root)
    LinearLayout llRoot;
    @Bind(R.id.activity_address_add_et_address)
    EditText etAddresses;
    @Bind(R.id.activity_address_add_et_name)
    EditText etName;
    @Bind(R.id.activity_address_add_et_phone)
    EditText etPhone;
    @Bind(R.id.activity_address_add_et_street)
    EditText etStreet;
    @Bind(R.id.activity_address_add_tv_pca)
    TextView tvPca;
    @Bind(R.id.activity_address_add_tv_submit)
    TextView tvSubmit;

    private String province;
    private String city;
    private String district;
    private String street;
    private String detail;
    private String name;
    private String phone;

    private int type;

    private LinkageDialogView linkageDialogView;
    private PCABean pca;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_address_add);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        type = bundle.getInt(BUNDLE_KEY_ADDRESS_TYPE);
    }

    private void init() {
//        controlKeyboardLayout(llRoot, tvSubmit);
    }

    @OnClick(R.id.activity_address_add_tv_submit)
    public void onSubmit(View v) {
        detail = etAddresses.getText().toString();
        name = etName.getText().toString();
        phone = etPhone.getText().toString();
        street = etStreet.getText().toString();

        if (StringUtil.isEmpty(detail)) {
            ToastUtil.toastAlways(this, "请填写详细地址");
            return;
        }
        if (StringUtil.isEmpty(name)) {
            ToastUtil.toastAlways(this, "请填写姓名");
            return;
        }
        if (StringUtil.isEmpty(phone)) {
            ToastUtil.toastAlways(this, "请填写手机号");
            return;
        }
        if (StringUtil.isEmpty(street)) {
            ToastUtil.toastAlways(this, "请填写街道");
            return;
        }
        if (StringUtil.isEmpty(province)) {
            ToastUtil.toastAlways(this, "请选择省份");
            return;
        }
        if (StringUtil.isEmpty(city)) {
            ToastUtil.toastAlways(this, "请选择城市");
            return;
        }
        if (StringUtil.isEmpty(district)) {
            ToastUtil.toastAlways(this, "请选择地区");
            return;
        }

        RequestBody body = new FormEncodingBuilder()
                .add("accountId", "" + MApplication.getApp().getAccountWithLogin().getId())
                .add("name", name)
                .add("phone", phone)
                .add("province", province)
                .add("city", city)
                .add("district", district)
                .add("street", street)
                .add("detail", detail)
                .add("type", "" + type)
                .build();
        ApiAddress.getInstance().postAdd(this, body);
        showLoading();
    }

    @OnClick(R.id.activity_address_add_tv_pca)
    public void onPca(View v) {
        showPCADialog();
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
    protected void onResume() {
        super.onResume();
        pca = PCAUtil.getPCA(this, "pca.json", PCABean.class);
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                MApplication.getApp().updateActivity(AddressBookActivity.class);
            }
        });
        onBack(null);
    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastAlways(AddressAddActivity.this, "添加失败，请重试");
            }
        });
    }

    @Override
    public void setRequestFinish(String url) {
        super.setRequestFinish(url);
        hideLoading();
    }
}
