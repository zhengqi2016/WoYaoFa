package com.woyaofa.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.adapter.RecyclerAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.dialog.TipDialog;
import com.lib_common.fragment.BaseFragment;
import com.lib_common.util.DateUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.woyaofa.MApplication;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiOrder;
import com.woyaofa.bean.OrderBean;
import com.woyaofa.ui.WebActivity;
import com.woyaofa.ui.order.CommentActivity;
import com.woyaofa.ui.widget.HeadView;
import com.woyaofa.util.NavUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class OrderFrg extends BaseFragment {

    @Bind(R.id.hv_head)
    HeadView hvHead;

    @Bind(R.id.frg_order_srl_items)
    SwipeRefreshLayout srlItems;
    @Bind(R.id.frg_order_rv_items)
    RecyclerView rvItems;
    @Bind(R.id.frg_order_tv_complete)
    TextView tvComplete;
    @Bind(R.id.frg_order_tv_delivery)
    TextView tvDelivery;
    @Bind(R.id.frg_order_tv_evaluate)
    TextView tvEvaluate;
    @Bind(R.id.frg_order_tv_pending)
    TextView tvPending;
    @Bind(R.id.frg_order_tv_receipt)
    TextView tvReceipt;

    private List<TextView> tabs = new ArrayList<>();

    private RecyclerAdapter<OrderBean> adapter;
    private List<OrderBean> datas;
    private LinearLayoutManager layoutManager;
    private int status;
    private String statusStr = "待受理";

    @Override
    public View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToastUtil.log("frg", "MineFrg");
        return inflater.inflate(R.layout.frg_order, null, false);
    }

    @Override
    public void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        loadData();
    }

    private void init() {
        hvHead.setTitle(getResources().getString(R.string.app_name));
        tabs.add(tvComplete);
        tabs.add(tvDelivery);
        tabs.add(tvEvaluate);
        tabs.add(tvPending);
        tabs.add(tvReceipt);

        srlItems.setColorSchemeColors(Color.parseColor("#ff0000"), Color.parseColor("#00ff00"), Color.parseColor("#0000ff"));
        srlItems.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        rvItems.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter<OrderBean>(getContext(), datas, R.layout.adapter_frg_order) {

            @Override
            public void onBindViewHolder(ViewHolder vh, List<OrderBean> datas, int position) {
                final OrderBean bean = datas.get(position);
                LinearLayout llRoot = vh.getViewById(R.id.adapter_frg_order_ll_root, LinearLayout.class);
                LinearLayout llCompany = vh.getViewById(R.id.adapter_frg_order_ll_company, LinearLayout.class);
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
                tvTime.setText("下单时间：" + DateUtil.datetimeToString(new Date(bean.getBuyTime()), "yyyy-MM-dd HH:mm"));
                MImageLoader.displayWithDefaultOptions(getContext(), bean.getLine().getCompany().getLogo(), ivLogo);
                tvState.setText(statusStr);
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebActivity.BUNDLE_KEY_JSON, GsonUtil.getInstance().toJsonString(bean));
                        bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_ORDER_DETAIL);
                        NavUtil.goToNewAct(getContext(), WebActivity.class, bundle);
                    }
                });
                llCompany.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_COMPANY_INDEX);
                        bundle.putString(WebActivity.BUNDLE_KEY_JSON, GsonUtil.getInstance().toJsonString(bean.getLine().getCompany()));
                        NavUtil.goToNewAct(getContext(), WebActivity.class, bundle);
                    }
                });
                tvOption1.setVisibility(View.VISIBLE);
                tvOption2.setVisibility(View.VISIBLE);
                tvOption3.setVisibility(View.VISIBLE);
                tvOption1.setTextColor(Color.parseColor("#808080"));
                tvOption2.setTextColor(Color.parseColor("#808080"));
                tvOption3.setTextColor(Color.parseColor("#808080"));
                tvOption1.setBackgroundResource(R.drawable.gray_border_rounded_corner_state);
                tvOption2.setBackgroundResource(R.drawable.gray_border_rounded_corner_state);
                tvOption3.setBackgroundResource(R.drawable.gray_border_rounded_corner_state);
                if (bean.getStatus() == ApiOrder.STATUS_COMPLETE) {
                    tvOption1.setVisibility(View.GONE);
                    tvOption2.setText("查看物流");
                    tvOption3.setText("删除订单");
                } else if (bean.getStatus() == ApiOrder.STATUS_DELIVERY) {
                    tvOption1.setVisibility(View.GONE);
                    tvOption2.setText("申请取消");
                    tvOption3.setText("提醒发货");
                    tvOption3.setBackgroundResource(R.drawable.blue_border_rounded_corner_state);
                    tvOption3.setTextColor(Color.parseColor("#2e82fe"));
                } else if (bean.getStatus() == ApiOrder.STATUS_EVALUATE) {
                    tvOption3.setText("评价");
                    tvOption2.setText("查看物流");
                    tvOption1.setText("删除订单");
                    tvOption3.setBackgroundResource(R.drawable.blue_border_rounded_corner_state);
                    tvOption3.setTextColor(Color.parseColor("#2e82fe"));
                } else if (bean.getStatus() == ApiOrder.STATUS_PENDING) {
                    tvOption1.setVisibility(View.GONE);
                    tvOption2.setText("取消订单");
                    tvOption3.setText("提醒受理");
                    tvOption3.setBackgroundResource(R.drawable.blue_border_rounded_corner_state);
                    tvOption3.setTextColor(Color.parseColor("#2e82fe"));
                } else if (bean.getStatus() == ApiOrder.STATUS_RECEIPT || bean.getStatus() == ApiOrder.STATUS_CONFIRM) {
                    tvOption3.setText("确认收货");
                    tvOption2.setText("查看物流");
                    tvOption1.setText("申请理赔");
                    tvOption3.setBackgroundResource(R.drawable.blue_border_rounded_corner_state);
                    tvOption3.setTextColor(Color.parseColor("#2e82fe"));
                }
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = ((TextView) v).getText().toString();
                        if ("查看物流".equals(text)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(WebActivity.BUNDLE_KEY_TITLE, "查看物流");
                            bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_LOGISTICS_INFO);
                            bundle.putString(WebActivity.BUNDLE_KEY_JSON, bean.getNumber());
                            NavUtil.goToNewAct(getContext(), WebActivity.class, bundle);
                        } else if ("评价".equals(text)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(CommentActivity.BUNDLE_KEY_BEAN, GsonUtil.getInstance().toJsonString(bean));
                            NavUtil.goToNewAct(getContext(), CommentActivity.class, bundle);
                        } else if ("申请理赔".equals(text)) {
                            Bundle bundle = new Bundle();
                            bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_CLAIM);
                            bundle.putString(WebActivity.BUNDLE_KEY_TITLE, "申请理赔");
                            bundle.putString(WebActivity.BUNDLE_KEY_JSON, GsonUtil.getInstance().toJsonString(bean));
                            NavUtil.goToNewAct(getContext(), WebActivity.class, bundle);
                        } else {
                            showTip(text, bean);
                        }
                    }
                };
                tvOption1.setOnClickListener(clickListener);
                tvOption2.setOnClickListener(clickListener);
                tvOption3.setOnClickListener(clickListener);
            }
        };
        rvItems.setAdapter(adapter);
        setListener();
        loadData();
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

    @Override
    public void loadData() {
        super.loadData();
        if (MApplication.getApp().getAccount() != null) {
            List<Object> body = new ArrayList<>();
            body.add(MApplication.getApp().getAccount().getId());
            body.add(status);
            body.add(adapter.getPageIndex());
            body.add(adapter.getPageSize());
            ApiOrder.getInstance().getList(this, body);
            srlItems.setRefreshing(true);
        } else {
            if (!hidden)
                ToastUtil.toastAlways(getContext(), "登录后才能看到自己的记录哦！");
            srlItems.setRefreshing(false);
        }
    }

    public void loadMore() {
        if (!adapter.hasMorePage()) {
            ToastUtil.toastAlways(getActivity(), "没有更多的信息了！");
            return;
        }
        adapter.nextPage();
        loadData();
    }

    public void requestDelete(OrderBean orderBean) {
        RequestBody body = new FormEncodingBuilder()
                .add("orderId", orderBean.getId() + "")
                .build();
        ApiOrder.getInstance().postDelete(this, body);
    }

    /**
     * 提醒发货
     *
     * @param orderBean
     */
    public void requestRemindDelivery(OrderBean orderBean) {
        RequestBody body = new FormEncodingBuilder()
                .add("id", orderBean.getId() + "")
                .build();
        ApiOrder.getInstance().postRemindDelivery(this, body);
    }

    /**
     * 提醒受理
     *
     * @param orderBean
     */
    public void requestReminderProcessing(OrderBean orderBean) {
        RequestBody body = new FormEncodingBuilder()
                .add("id", orderBean.getId() + "")
                .build();
        ApiOrder.getInstance().postReminderProcessing(this, body);
    }

    /**
     * 取消和申请取消的请求
     *
     * @param orderBean
     */
    public void requestCancel(OrderBean orderBean) {
        List<Object> body = new ArrayList<>();
        body.add(orderBean.getId());
        body.add(status);
        ApiOrder.getInstance().getCancel(this, body);
    }

    public void requestConfirmReceipt(OrderBean orderBean) {
        List<Object> body = new ArrayList<>();
        body.add(orderBean.getId());
        body.add(orderBean.getLine().getId());
        ApiOrder.getInstance().getConfirmReceipt(this, body);
    }

    @OnClick(R.id.frg_order_tv_complete)
    public void onComplete(View v) {
        changeTabs((TextView) v);
    }

    @OnClick(R.id.frg_order_tv_delivery)
    public void onDelivery(View v) {
        changeTabs((TextView) v);
    }

    @OnClick(R.id.frg_order_tv_receipt)
    public void onReceipt(View v) {
        changeTabs((TextView) v);
    }

    @OnClick(R.id.frg_order_tv_pending)
    public void onPending(View v) {
        changeTabs((TextView) v);
    }

    @OnClick(R.id.frg_order_tv_evaluate)
    public void onEvaluate(View v) {
        changeTabs((TextView) v);
    }

    private void changeTabs(TextView v) {
        for (TextView tab : tabs) {
            tab.setTextColor(getResources().getColor(R.color.text_content));
            tab.setBackground(null);
        }
        v.setBackgroundResource(R.drawable.blue_border_down);
        v.setTextColor(Color.parseColor("#2e82fe"));
        if (v.getId() == R.id.frg_order_tv_evaluate) {
            status = ApiOrder.STATUS_EVALUATE;
            statusStr = "待评价";
        } else if (v.getId() == R.id.frg_order_tv_delivery) {
            status = ApiOrder.STATUS_DELIVERY;
            statusStr = "待发货";
        } else if (v.getId() == R.id.frg_order_tv_complete) {
            status = ApiOrder.STATUS_COMPLETE;
            statusStr = "已完成";
        } else if (v.getId() == R.id.frg_order_tv_pending) {
            status = ApiOrder.STATUS_PENDING;
            statusStr = "待受理";
        } else if (v.getId() == R.id.frg_order_tv_receipt) {
            status = ApiOrder.STATUS_RECEIPT;
            statusStr = "待收货";
        }
        adapter.resetPageIndex();
        loadData();
    }

    private TipDialog tipDialog;

    private void showTip(final String text, final OrderBean orderBean) {
        if (tipDialog == null) {
            tipDialog = new TipDialog(getContext(), R.style.tipDialog);
        }
        tipDialog.setContent("真的要" + text + "吗？", null, null);
        tipDialog.setOnListener(new TipDialog.OnMOKListener() {
            @Override
            public void onClick(TipDialog dialog, View view) {
                if ("删除订单".equals(text)) {
                    requestDelete(orderBean);
                } else if ("申请取消".equals(text) || "取消订单".equals(text)) {
                    requestCancel(orderBean);
                } else if ("提醒发货".equals(text)) {
//                    requestRemindDelivery(orderBean);
                    ToastUtil.toastAlways(getContext(), "提醒成功！");
                } else if ("提醒受理".equals(text)) {
//                    requestReminderProcessing(orderBean);
                    ToastUtil.toastAlways(getContext(), "提醒成功！");
                } else if ("确认收货".equals(text)) {
                    requestConfirmReceipt(orderBean);
                }
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
    public void setRequestSuc(String url, String statusCode, final JsonObject jo) {
        if (url.equals(ApiOrder.URL_LIST)) {
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
        } else {
            updateView(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastAlways(getContext(), jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
                    adapter.resetPageIndex();
                    loadData();
                }
            });
        }

    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, final JsonObject jo) {
        updateView(new Runnable() {
            @Override
            public void run() {
                if (!hidden)
                    ToastUtil.toastAlways(getContext(), jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
                adapter.setDatas(null);
            }
        });
    }

    @Override
    public void setRequestErr(String url, String statusCode, Response response) {
        updateView(new Runnable() {
            @Override
            public void run() {
                if (!hidden)
                    ToastUtil.toastAlways(getContext(), "服务器繁忙，请重新尝试！");
                adapter.setDatas(null);
            }
        });
    }

    @Override
    public void setRequestException(String url, Request request, IOException e) {
        updateView(new Runnable() {
            @Override
            public void run() {
                if (!hidden) {
                    ToastUtil.toastAlways(getContext(), "网络好像出了问题，请检查网络哦！");
                    adapter.setDatas(null);
                }

            }
        });
    }

    @Override
    public void setRequestFinish(String url) {
        updateView(new Runnable() {
            @Override
            public void run() {
                srlItems.setRefreshing(false);
            }
        });
    }

    private boolean hidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
    }
}
