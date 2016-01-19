package com.woyaofa.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.activity.ImageListActivity;
import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.dialog.DateTimePickerDialog;
import com.lib_common.util.DateUtil;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiImage;
import com.woyaofa.api.ApiOrder;
import com.woyaofa.bean.AddressBean;
import com.woyaofa.bean.LineBean;
import com.woyaofa.ui.mine.AddressSelectorActivity;
import com.woyaofa.util.NavUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 下订单
 */
public class PlaceAnOrderActivity extends MBaseActivity {

    public static final String BUNDLE_KEY_BEAN = "bean";

    public static final int CODE_REQUEST_CAMERA = 114325;
    public static final int CODE_REQUEST_FROM_ADDRESS = 1510;
    public static final int CODE_REQUEST_TO_ADDRESS = 1511;

    @Bind(R.id.place_an_order_gv_images)
    GridView gvImages;
    @Bind(R.id.place_an_order_et_info)
    EditText etInfo;
    @Bind(R.id.place_an_order_tv_from_name)
    TextView tvFromName;
    @Bind(R.id.place_an_order_tv_from_address)
    TextView tvFromAddress;
    @Bind(R.id.place_an_order_tv_from_phone)
    TextView tvFromPhone;
    @Bind(R.id.place_an_order_tv_to_name)
    TextView tvToName;
    @Bind(R.id.place_an_order_tv_to_address)
    TextView tvToAddress;
    @Bind(R.id.place_an_order_tv_to_phone)
    TextView tvToPhone;
    @Bind(R.id.place_an_order_ll_from_address)
    LinearLayout llFromAddress;
    @Bind(R.id.place_an_order_ll_to_address)
    LinearLayout llToAddress;
    @Bind(R.id.place_an_order_tv_add_to_address)
    TextView tvAddToAddress;
    @Bind(R.id.place_an_order_tv_add_from_address)
    TextView tvAddFromAddress;
    @Bind(R.id.place_an_order_tv_time)
    TextView tvTime;

    private List<ImageListActivity.ImageBean> datas = new ArrayList<>();
    private CommonAdapter<ImageListActivity.ImageBean> adapter;

    private ImageListActivity.ImageBean addImage = new ImageListActivity.ImageBean(0, "" + R.mipmap.add_image, null, null);
    private AbsListView.LayoutParams params;
    private LineBean bean;

    private AddressBean fromAddress;
    private AddressBean toAddress;
    private String bookTime = "现在";
    private DateTimePickerDialog dateTimePickerDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_place_an_order);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        String jsonStr = bundle.getString(BUNDLE_KEY_BEAN);
        bean = GsonUtil.getInstance().toJsonObj(jsonStr, LineBean.class);
    }

    private void init() {
        hideKeyBoard();
        int w = DensityUtil.getScreenW(this);
        double wdp = (w - DensityUtil.sp2px(this, 10) * 3) / 4 * 1.1;
        params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, (int) wdp);
        adapter = new CommonAdapter<ImageListActivity.ImageBean>(this, datas, R.layout.adapter_activity_evaluation) {
            @Override
            public void dealViews(ViewHolder holder, final List<ImageListActivity.ImageBean> datas, int position) {
                ImageListActivity.ImageBean bean = datas.get(position);
                final ArrayList<ImageListActivity.ImageBean> selectImages = new ArrayList<>(datas);
                selectImages.remove(addImage);
                LinearLayout llRoot = holder.getViewById(R.id.adapter_activity_evaluation_ll_root, LinearLayout.class);
                ImageView ivImage = holder.getViewById(R.id.adapter_activity_evaluation_iv_image, ImageView.class);
                MImageLoader.displayWithDefaultOptions(PlaceAnOrderActivity.this, bean.getImageUrl(), ivImage);

                llRoot.setLayoutParams(params);
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImageListActivity.BUNDLE_KEY_SELECTED_IMAGES, selectImages);
                        ImageListActivity.Config config = new ImageListActivity.Config();
                        config.maxSelectNum = 7;
                        bundle.putSerializable(ImageListActivity.BUNDLE_KEY_CONFIG, config);
                        NavUtil.goToNewActForResult(PlaceAnOrderActivity.this, ImageListActivity.class, bundle, CODE_REQUEST_CAMERA);
                    }
                });
            }
        };
        gvImages.setAdapter(adapter);
        datas.add(addImage);
        fromAddress=MApplication.getApp().getAccountWithLogin().getUser().getAddressBook();
        setFromAddress();
        setToAddress();
        updateView(updateRunnable);
    }

    @OnClick({R.id.place_an_order_ll_from_address, R.id.place_an_order_tv_add_from_address})
    public void onFromAddress(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(AddressSelectorActivity.BUNDLE_KEY_ACTION, AddressSelectorActivity.ACTION_SEND);
        bundle.putSerializable(AddressSelectorActivity.BUNDLE_KEY_SELECTED, fromAddress);
        NavUtil.goToNewActForResult(this, AddressSelectorActivity.class, bundle, CODE_REQUEST_FROM_ADDRESS);
    }

    @OnClick({R.id.place_an_order_ll_to_address, R.id.place_an_order_tv_add_to_address})
    public void onToAddress(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(AddressSelectorActivity.BUNDLE_KEY_ACTION, AddressSelectorActivity.ACTION_RECIEVE);
        bundle.putSerializable(AddressSelectorActivity.BUNDLE_KEY_SELECTED, fromAddress);
        NavUtil.goToNewActForResult(this, AddressSelectorActivity.class, bundle, CODE_REQUEST_TO_ADDRESS);
    }

    @OnClick(R.id.place_an_order_ll_time)
    public void onDateTime(View v) {
        if (dateTimePickerDialog == null) {
            dateTimePickerDialog = new DateTimePickerDialog(this, R.style.tipDialog);
            dateTimePickerDialog.setOnListener(new DateTimePickerDialog.OnMOKListener() {
                @Override
                public void onClick(DateTimePickerDialog dialog, View view, int year, int month, int dayOfMonth, int hour, int minute) {
                    bookTime = year + "-" + (month + 1) + "-" + dayOfMonth + " " + hour + ":" + minute + ":00";
                    tvTime.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日 " + hour + ":" + minute);
                    dialog.dismiss();
                }
            }, new DateTimePickerDialog.OnMCancelListener() {
                @Override
                public void onClick(DateTimePickerDialog dialog, View view) {
                    dialog.dismiss();
                }
            });
        }
        dateTimePickerDialog.show();
    }

    @OnClick(R.id.place_an_order_tv_submit)
    public void onSubmit(View v) {
        if (fromAddress == null) {
            ToastUtil.toastAlways(this, "请选择发货地址！");
            return;
        }
        if (toAddress == null) {
            ToastUtil.toastAlways(this, "请选择收货地址！");
            return;
        }
        if (bookTime.equals("现在")) {
            bookTime = DateUtil.datetimeToString(new Date(), DateUtil.DEFAULT_PATTERN);
        }
        RequestBody body = new FormEncodingBuilder()
                .add("beginName", fromAddress.getName())
                .add("beginPhone", fromAddress.getPhone())
                .add("beginAddress", fromAddress.getProvince() + " " + fromAddress.getCity() + " " + fromAddress.getDistrict() + " " + fromAddress.getStreet() + " " + fromAddress.getDetail())
                .add("endName", toAddress.getName())
                .add("endPhone", toAddress.getPhone())
                .add("endAddress", toAddress.getProvince() + " " + toAddress.getCity() + " " + toAddress.getDistrict() + " " + toAddress.getStreet() + " " + toAddress.getDetail())
                .add("detail", etInfo.getText().toString())
                .add("lineId", bean.getId() + "")
                .add("accountId", MApplication.getApp().getAccountWithLogin().getId() + "")
                .add("dueTime", DateUtil.stringToDatetime(bookTime, DateUtil.DEFAULT_PATTERN).getTime() + "").build();
        ApiOrder.getInstance().postAdd(this, body);
        showLoading();
    }

    private void uploadImages(String orderId) {
        if (datas.size() <= 1) {
            updateView(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastAlways(PlaceAnOrderActivity.this, "下单成功");
                    hideLoading();
                    onBack(null);
                }
            });
            return;
        }
        MultipartBuilder mb = new MultipartBuilder();
        mb.type(MultipartBuilder.FORM);
        datas.remove(addImage);
        for (int i = 0; i < datas.size(); i++) {
            ImageListActivity.ImageBean bean = datas.get(i);
            mb.addFormDataPart("image" + i, "image" + i + ".png", RequestBody.create(MediaType.parse("image/png"), new File(bean.getImageUrl())));
        }
        mb.addFormDataPart("orderId", orderId);
        ApiImage.getInstance().postPlaceAnOrder(this, mb.build());
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            adapter.setDatas(datas);
        }
    };

    private void setFromAddress() {
        if (fromAddress != null) {
            tvFromName.setText(fromAddress.getName());
            tvFromAddress.setText(fromAddress.getProvince() + " " + fromAddress.getCity() + " " + fromAddress.getDistrict() + " " + fromAddress.getStreet() + " " + fromAddress.getDetail());
            tvFromPhone.setText(fromAddress.getPhone());
            llFromAddress.setVisibility(View.VISIBLE);
            tvAddFromAddress.setVisibility(View.GONE);
        } else {
            llFromAddress.setVisibility(View.GONE);
            tvAddFromAddress.setVisibility(View.VISIBLE);
        }
    }

    private void setToAddress() {
        if (toAddress != null) {
            tvToName.setText(toAddress.getName());
            tvToAddress.setText(toAddress.getProvince() + " " + toAddress.getCity() + " " + toAddress.getDistrict() + " " + toAddress.getStreet() + " " + toAddress.getDetail());
            tvToPhone.setText(toAddress.getPhone());
            llToAddress.setVisibility(View.VISIBLE);
            tvAddToAddress.setVisibility(View.GONE);
        } else {
            llToAddress.setVisibility(View.GONE);
            tvAddToAddress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST_CAMERA && resultCode == ImageListActivity.CODE_RESULT) {
            datas = (List<ImageListActivity.ImageBean>) data.getSerializableExtra(ImageListActivity.BUNDLE_KEY_SELECTED_IMAGES);
            datas.add(addImage);
            updateView(updateRunnable);
        } else if (requestCode == CODE_REQUEST_FROM_ADDRESS && resultCode == AddressSelectorActivity.CODE_RESULT) {
            fromAddress = (AddressBean) data.getSerializableExtra(AddressSelectorActivity.BUNDLE_KEY_SELECTED);
            setFromAddress();
        } else if (requestCode == CODE_REQUEST_TO_ADDRESS && resultCode == AddressSelectorActivity.CODE_RESULT) {
            toAddress = (AddressBean) data.getSerializableExtra(AddressSelectorActivity.BUNDLE_KEY_SELECTED);
            setToAddress();
        }
    }

    @Override
    public void setRequestSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        if (url.equals(ApiOrder.URL_ADD)) {
            uploadImages(jo.getAsJsonPrimitive(Api.KEY_DATA).getAsString());
        }
        if (url.equals(ApiImage.URL_PLACE_AN_ORDER)) {
            updateView(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastAlways(PlaceAnOrderActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
                    onBack(null);
                }
            });
        }
    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                ToastUtil.toastAlways(PlaceAnOrderActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
            }
        });
    }

    @Override
    public void setRequestErr(String url, String statusCode, Response response) {
        super.setRequestErr(url, statusCode, response);
        updateView(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        });
    }

    @Override
    public void setRequestException(String url, Request request, IOException e) {
        super.setRequestException(url, request, e);
        updateView(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        });

    }

    @Override
    public void setRequestFinish(String url) {
        super.setRequestFinish(url);
        if (url.equals(ApiImage.URL_PLACE_AN_ORDER)) {
            hideLoading();
        }
    }
}
