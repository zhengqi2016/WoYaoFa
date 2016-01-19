package com.woyaofa.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.activity.ImageListActivity;
import com.lib_common.adapter.RecyclerAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.ToastUtil;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiAddress;
import com.woyaofa.bean.AddressBean;
import com.woyaofa.ui.widget.HeadView;
import com.woyaofa.util.NavUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 地址选择器
 */
public class AddressSelectorActivity extends MBaseActivity {

    public static final int CODE_RESULT = 1013;
    public static final String BUNDLE_KEY_SELECTED = "selected";
    public static final String BUNDLE_KEY_ACTION = "action";
    public static final String ACTION_SEND = "send";
    public static final String ACTION_RECIEVE = "recieve";

    @Bind(R.id.address_selector_rv_items)
    RecyclerView rvItems;
    @Bind(R.id.address_selector_srl_addresses)
    SwipeRefreshLayout srlItems;

    private LinearLayoutManager layoutManager;
    private List<AddressBean> datas;
    private RecyclerAdapter<AddressBean> adapter;

    private ImageListActivity.ImageBean addImage = new ImageListActivity.ImageBean(0, "http://www.51ps.com/upfile/2007/11/200711234317140356694.jpg", null, null);
    private Serializable selectBean;
    private String action;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_address_selector);
        init();
        setListener();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        action = bundle.getString(BUNDLE_KEY_ACTION);
    }

    private void init() {
        hvHead.setRight("管理", 0);
        srlItems.setColorSchemeColors(Color.parseColor("#ff0000"), Color.parseColor("#00ff00"), Color.parseColor("#0000ff"));
        srlItems.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        rvItems.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter<AddressBean>(this, datas, R.layout.adapter_activity_address_book) {
            @Override
            public void onBindViewHolder(ViewHolder vh, List<AddressBean> datas, int position) {
                final AddressBean bean = datas.get(position);
                LinearLayout llRoot = vh.getViewById(R.id.address_book_ll_root, LinearLayout.class);
                TextView tvAddress = vh.getViewById(R.id.address_book_tv_address, TextView.class);
                TextView tvName = vh.getViewById(R.id.address_book_tv_name, TextView.class);
                TextView tvPhone = vh.getViewById(R.id.address_book_tv_phone, TextView.class);
                ImageView ivDefault = vh.getViewById(R.id.address_book_iv_default, ImageView.class);
                try {
                    ivDefault.setVisibility(View.GONE);
                    if (MApplication.getApp().getAccountWithLogin().getUser().getAddressBook() != null && MApplication.getApp().getAccountWithLogin().getUser().getAddressBook().getId() == bean.getId()) {
                        ivDefault.setVisibility(View.VISIBLE);
                    }
                    tvPhone.setText(bean.getPhone());
                    tvAddress.setText(bean.getProvince() + bean.getCity() + bean.getDistrict() + bean.getDetail());
                    tvName.setText(bean.getName());
                    llRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectBean = bean;
                            setResult();
                        }
                    });
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }
            }
        };
        rvItems.setAdapter(adapter);
        rvItems.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = DensityUtil.dip2px(AddressSelectorActivity.this, 3);
            }
        });
    }

    public void setListener() {
        hvHead.setOnClickRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtil.goToNewAct(AddressSelectorActivity.this, AddressBookActivity.class);
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();
        try {
            List<Object> body = new ArrayList<>();
            body.add(MApplication.getApp().getAccountWithLogin().getId());
            if (ACTION_SEND.equals(action)) {
                body.add(ApiAddress.TYPE_SEND);
            } else if (ACTION_RECIEVE.equals(action)) {
                body.add(ApiAddress.TYPE_RECEIVE);
            }
            body.add(adapter.getPageIndex());
            body.add(adapter.getPageSize());
            ApiAddress.getInstance().getList(this, body);
            showLoading();
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    protected void setResult() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY_SELECTED, selectBean);
        intent.putExtras(bundle);
        setResult(CODE_RESULT, intent);
        onBack(null);
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        datas = GsonUtil.getInstance().toJsonArr(jo.getAsJsonObject(Api.KEY_DATA).getAsJsonArray(Api.KEY_LIST).toString(), new TypeToken<List<AddressBean>>() {
        });
        updateView(new Runnable() {
            @Override
            public void run() {
                adapter.setDatas(datas);
            }
        });
    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastAlways(AddressSelectorActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
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
