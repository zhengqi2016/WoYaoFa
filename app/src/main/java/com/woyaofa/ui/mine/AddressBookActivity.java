package com.woyaofa.ui.mine;

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
import com.woyaofa.bean.UserBean;
import com.woyaofa.util.NavUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 地址薄
 */
public class AddressBookActivity extends MBaseActivity {

    @Bind(R.id.activity_address_book_srl_addresses)
    SwipeRefreshLayout srlAddresses;
    @Bind(R.id.activity_address_book_rv_addresses)
    RecyclerView rvAddresses;
    @Bind(R.id.activity_address_book_tv_send)
    TextView tvSend;
    @Bind(R.id.activity_address_book_tv_receive)
    TextView tvReceive;
    @Bind(R.id.activity_address_book_tv_add)
    TextView tvAdd;

    private LinearLayoutManager layoutManager;
    private List<AddressBean> datas;
    private RecyclerAdapter<AddressBean> adapter;
    private int type;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_address_book);
        init();
        setListener();
    }

    private void init() {
        srlAddresses.setColorSchemeColors(Color.parseColor("#ff0000"), Color.parseColor("#00ff00"), Color.parseColor("#0000ff"));
        srlAddresses.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        rvAddresses.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter<AddressBean>(this, datas, R.layout.adapter_activity_address_book) {
            @Override
            public void onBindViewHolder(ViewHolder vh, List<AddressBean> datas, int position) {
                final AddressBean bean = datas.get(position);
                LinearLayout llRoot = vh.getViewById(R.id.address_book_ll_root, LinearLayout.class);
                TextView tvAddress = vh.getViewById(R.id.address_book_tv_address, TextView.class);
                TextView tvName = vh.getViewById(R.id.address_book_tv_name, TextView.class);
                ImageView ivDefault = vh.getViewById(R.id.address_book_iv_default, ImageView.class);
                TextView tvPhone = vh.getViewById(R.id.address_book_tv_phone, TextView.class);
                try {
                    ivDefault.setVisibility(View.GONE);
                    if (MApplication.getApp().getAccountWithLogin().getUser().getAddressBook() != null && MApplication.getApp().getAccountWithLogin().getUser().getAddressBook().getId() == bean.getId() && type == ApiAddress.TYPE_SEND) {
                        ivDefault.setVisibility(View.VISIBLE);
                    }
                    tvAddress.setText(bean.getProvince() + " " + bean.getCity() + " " + bean.getDistrict() + " " + bean.getStreet() + " " + bean.getDetail());
                    tvName.setText(bean.getName());
                    tvPhone.setText(bean.getPhone());

                    llRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(AddressEditActivity.BUNDLE_KEY_BEAN, bean);
                            if (type != ApiAddress.TYPE_SEND) {
                                bundle.putBoolean(AddressEditActivity.BUNDLE_KEY_NO_DEFAULT, true);
                            }
                            NavUtil.goToNewAct(AddressBookActivity.this, AddressEditActivity.class, bundle);
                        }
                    });
                } catch (Exception e) {
                    ToastUtil.log("", e.getMessage());
                }
            }
        };
        rvAddresses.setAdapter(adapter);
        rvAddresses.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = DensityUtil.dip2px(AddressBookActivity.this, 3);
//                if (parent.getChildPosition(view) == 0) {
//                    outRect.top = 0;
//                }
            }
        });

    }

    private void setListener() {
        srlAddresses.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.resetPageIndex();
                loadData();
            }
        });
        rvAddresses.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @OnClick(R.id.activity_address_book_tv_send)
    public void onSend(View v) {
        if (type != ApiAddress.TYPE_SEND) {
            tvReceive.setBackground(null);
            tvReceive.setTextColor(getResources().getColor(R.color.white));
            tvSend.setTextColor(getResources().getColor(R.color.head_bg));
            tvSend.setBackgroundResource(R.drawable.white_half_rounded_left);
            type = ApiAddress.TYPE_SEND;
            tvAdd.setText("添加发货人地址");
            loadData();
        }
    }

    @OnClick(R.id.activity_address_book_tv_receive)
    public void onReceive(View v) {
        if (type != ApiAddress.TYPE_RECEIVE) {
            tvSend.setBackground(null);
            tvSend.setTextColor(getResources().getColor(R.color.white));
            tvReceive.setBackgroundResource(R.drawable.white_half_rounded_right);
            tvReceive.setTextColor(getResources().getColor(R.color.head_bg));
            type = ApiAddress.TYPE_RECEIVE;
            tvAdd.setText("添加收货人地址");
            loadData();
        }
    }

    @OnClick(R.id.activity_address_book_tv_add)
    public void onAdd(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt(AddressAddActivity.BUNDLE_KEY_ADDRESS_TYPE, type);
        NavUtil.goToNewAct(this, AddressAddActivity.class, bundle);
    }

    @Override
    public void loadData() {
        super.loadData();
        try {
            List<Object> body = new ArrayList<>();
            body.add(MApplication.getApp().getAccountWithLogin().getId());
            body.add(type);
            body.add(adapter.getPageIndex());
            body.add(adapter.getPageSize());
            ApiAddress.getInstance().getList(this, body);
            srlAddresses.setRefreshing(true);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    private void loadMore() {
        if (!adapter.hasMorePage()) {
            ToastUtil.toastAlways(this, "没有更多的啦！");
            return;
        }
        adapter.nextPage();
        loadData();
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            adapter.setDatas(datas);
        }
    };

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        List<AddressBean> list = GsonUtil.getInstance().toJsonArr(jo.getAsJsonObject(Api.KEY_DATA).getAsJsonArray(Api.KEY_LIST).toString(), new TypeToken<List<AddressBean>>() {
        });
        datas = new ArrayList<>();
        if (adapter.isFirstPage()) {
            datas = list;
        } else {
            datas.addAll(list);
        }
        updateView(updateRunnable);
    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        if (jo.getAsJsonPrimitive(Api.KEY_STATUS).getAsString().equals("2001")) {
            datas = new ArrayList<>();
            updateView(updateRunnable);
        } else {
            updateView(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastAlways(AddressBookActivity.this, "加载数据失败，请重新尝试！");
                }
            });
        }
    }

    @Override
    public void setRequestFinish(String url) {
        super.setRequestFinish(url);
        updateView(new Runnable() {
            @Override
            public void run() {
                srlAddresses.setRefreshing(false);
            }
        });
    }
}
