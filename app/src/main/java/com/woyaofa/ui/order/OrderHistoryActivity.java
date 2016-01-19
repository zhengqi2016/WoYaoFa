package com.woyaofa.ui.order;

import android.graphics.Color;
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
import com.lib_common.dialog.TipDialog;
import com.lib_common.util.DateUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiOrder;
import com.woyaofa.bean.OrderBean;
import com.woyaofa.ui.WebActivity;
import com.woyaofa.util.NavUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * 历史订单
 */
public class OrderHistoryActivity extends MBaseActivity {

    @Bind(R.id.frg_order_ll_tabs)
    LinearLayout llTabs;
    @Bind(R.id.frg_order_rv_items)
    RecyclerView rvItems;
    @Bind(R.id.frg_order_srl_items)
    SwipeRefreshLayout srlItems;
    private LinearLayoutManager layoutManager;
    private List<OrderBean> datas;
    private RecyclerAdapter<OrderBean> adapter;
    private TipDialog tipDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.frg_order);
        init();
        setListener();
    }

    private void init() {
        hvHead.setTitle("历史订单");
        hvHead.setLeft("", 0);
        llTabs.setVisibility(View.GONE);

        srlItems.setColorSchemeColors(Color.parseColor("#ff0000"), Color.parseColor("#00ff00"), Color.parseColor("#0000ff"));
        srlItems.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        rvItems.setLayoutManager(layoutManager);
        adapter = new com.lib_common.adapter.RecyclerAdapter<OrderBean>(this, datas, R.layout.adapter_frg_order) {
            @Override
            public void onBindViewHolder(ViewHolder vh, List<OrderBean> datas, int position) {
                final OrderBean bean = datas.get(position);
                LinearLayout llRoot = vh.getViewById(R.id.adapter_frg_order_ll_root, LinearLayout.class);
                ImageView ivLogo = vh.getViewById(R.id.adapter_frg_order_iv_logo, ImageView.class);
                TextView tvCompany = vh.getViewById(R.id.adapter_frg_order_tv_company, TextView.class);
                TextView tvOrderId = vh.getViewById(R.id.adapter_frg_order_tv_order_id, TextView.class);
                TextView tvInfo = vh.getViewById(R.id.adapter_frg_order_tv_info, TextView.class);
                TextView tvOption1 = vh.getViewById(R.id.adapter_frg_order_tv_option1, TextView.class);
                TextView tvOption2 = vh.getViewById(R.id.adapter_frg_order_tv_option2, TextView.class);
                TextView tvOption3 = vh.getViewById(R.id.adapter_frg_order_tv_option3, TextView.class);
                TextView tvPrice = vh.getViewById(R.id.adapter_frg_order_tv_price, TextView.class);
                TextView tvLineFrom = vh.getViewById(R.id.adapter_frg_order_tv_line_from, TextView.class);
                TextView tvLineTo = vh.getViewById(R.id.adapter_frg_order_tv_line_to, TextView.class);
                TextView tvState = vh.getViewById(R.id.adapter_frg_order_tv_state, TextView.class);
                TextView tvTime = vh.getViewById(R.id.adapter_frg_order_tv_time, TextView.class);
                tvCompany.setText(bean.getLine().getCompany().getName());
                tvOrderId.setText("运单号：" + bean.getNumber());
                tvInfo.setText("物品信息：" + bean.getDetail());
                tvPrice.setText("" + bean.getFee());
                tvLineFrom.setText(bean.getLine().getBeginCity() + bean.getLine().getBeginDistrict());
                tvLineTo.setText(bean.getLine().getEndCity() + bean.getLine().getEndDistrict());
                tvTime.setText("下单时间：" + DateUtil.datetimeToString(new Date(bean.getBuyTime()), "yyyy-MM-dd hh:mm"));
                MImageLoader.displayWithDefaultOptions(OrderHistoryActivity.this, bean.getLine().getCompany().getLogo(), ivLogo);
                tvState.setText("已完成");
                tvOption1.setVisibility(View.GONE);
                tvOption2.setVisibility(View.VISIBLE);
                tvOption3.setVisibility(View.VISIBLE);
                tvOption2.setText("永久删除");
                tvOption3.setText("查看物流");
                tvOption2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTip(bean);
                    }
                });
                tvOption3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebActivity.BUNDLE_KEY_JSON, bean.getNumber());
                        bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_LOGISTICS_INFO);
                        bundle.putString(WebActivity.BUNDLE_KEY_TITLE, "物流详情");
                        NavUtil.goToNewAct(OrderHistoryActivity.this, WebActivity.class, bundle);
                    }
                });
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebActivity.BUNDLE_KEY_JSON, GsonUtil.getInstance().toJsonString(bean));
                        bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_ORDER_DETAIL);
                        NavUtil.goToNewAct(OrderHistoryActivity.this, WebActivity.class, bundle);
                    }
                });
            }
        };
        rvItems.setAdapter(adapter);
    }

    private void setListener() {
        srlItems.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.resetPageIndex();
                loadData();
            }
        });
        rvItems.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == rvItems.getAdapter().getItemCount()) {
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

    private void showTip(final OrderBean bean) {
        if (tipDialog == null) {
            tipDialog = new TipDialog(this, R.style.tipDialog);
        }
        tipDialog.setContent("真的要删除吗？", null, null);
        tipDialog.setOnListener(new TipDialog.OnMOKListener() {
            @Override
            public void onClick(TipDialog dialog, View view) {
                RequestBody body = new FormEncodingBuilder()
                        .add("id", bean.getId() + "")
                        .build();
                ApiOrder.getInstance().postDelete(OrderHistoryActivity.this, body);
                dialog.dismiss();
            }
        }, new TipDialog.OnMCancelListener() {
            @Override
            public void onClick(TipDialog dialog, View view) {
                dialog.dismiss();
            }
        });
        tipDialog.show();
    }

    @Override
    public void loadData() {
        super.loadData();
        List<Object> body = new ArrayList<>();
        body.add(MApplication.getApp().getAccountWithLogin().getId());
        body.add(ApiOrder.STATUS_COMPLETE);
        body.add(adapter.getPageIndex());
        body.add(adapter.getPageSize());
        ApiOrder.getInstance().getList(this, body);
        srlItems.setRefreshing(true);
    }

    private void loadMore() {
        if (!adapter.hasMorePage()) {
            ToastUtil.toast(this, "没有更多数据了哦！");
            return;
        }
        adapter.nextPage();
        loadData();
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        List<OrderBean> list = GsonUtil.getInstance().toJsonArr(jo.getAsJsonObject(Api.KEY_DATA).getAsJsonArray(Api.KEY_LIST).toString(), new TypeToken<List<OrderBean>>() {
        });
        if (adapter.isFirstPage()) {
            datas = list;
        } else {
            datas.addAll(list);
        }
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
                ToastUtil.toastAlways(OrderHistoryActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
            }
        });
    }

    @Override
    public void setRequestFinish(String url) {
        super.setRequestFinish(url);
        updateView(new Runnable() {
            @Override
            public void run() {
                srlItems.setRefreshing(false);
            }
        });
    }
}
