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
import com.lib_common.dialog.TipDialog;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiCollection;
import com.woyaofa.bean.CollectionBean;
import com.woyaofa.bean.LineBean;
import com.woyaofa.ui.WebActivity;
import com.woyaofa.ui.order.PlaceAnOrderActivity;
import com.woyaofa.util.NavUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的收藏
 */
public class MyCollectionActivity extends MBaseActivity {

    @Bind(R.id.activity_my_collection_srl_addresses)
    SwipeRefreshLayout srlAddresses;
    @Bind(R.id.activity_my_collection_rv_addresses)
    RecyclerView rvAddresses;
    @Bind(R.id.activity_my_collection_tv_line)
    TextView tvLine;
    @Bind(R.id.activity_my_collection_tv_company)
    TextView tvCompany;

    private LinearLayoutManager layoutManager;
    private List<CollectionBean> companyDatas;
    private RecyclerAdapter<CollectionBean> companyAdapter;
    private RecyclerAdapter<CollectionBean> lineAdapter;
    private List<CollectionBean> lineDatas;
    private String action = "line";
    private CollectionBean deleteCompany;
    private CollectionBean deleteLine;
    private TipDialog tipDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_my_collection);
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
        companyAdapter = new RecyclerAdapter<CollectionBean>(this, companyDatas, R.layout.adapter_activity_my_collection_company) {
            @Override
            public void onBindViewHolder(ViewHolder vh, List<CollectionBean> datas, int position) {
                final CollectionBean bean = datas.get(position);
                LinearLayout llRoot = vh.getViewById(R.id.my_collection_campany_ll_root, LinearLayout.class);
                ImageView ivLogo = vh.getViewById(R.id.my_collection_campany_iv_logo, ImageView.class);
                TextView tvCompany = vh.getViewById(R.id.my_collection_campany_tv_company, TextView.class);
                TextView tvLine = vh.getViewById(R.id.my_collection_campany_tv_line, TextView.class);
                TextView tvDistance = vh.getViewById(R.id.my_collection_campany_tv_distance, TextView.class);
                TextView tvVolume = vh.getViewById(R.id.my_collection_campany_tv_volume, TextView.class);
                TextView tvCancel = vh.getViewById(R.id.my_collection_company_tv_cancel, TextView.class);
                TextView tvContact = vh.getViewById(R.id.my_collection_company_tv_contact, TextView.class);
                try {
                    MImageLoader.displayWithDefaultOptions(MyCollectionActivity.this, bean.getCompany().getLogo(), ivLogo);
                    tvCompany.setText(bean.getCompany().getName());
                    tvDistance.setText(bean.getCompany().getAddress() + "  11km");
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteCompany = bean;
                            showTip("取消收藏", bean.getId() + "");
                        }
                    });
                    tvContact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavUtil.callPhone(MyCollectionActivity.this, bean.getCompany().getPhone());
                        }
                    });
                    llRoot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString(WebActivity.BUNDLE_KEY_RIGHT, "投诉");
                            bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_COMPANY_INDEX);
                            bundle.putString(WebActivity.BUNDLE_KEY_JSON, GsonUtil.getInstance().toJsonString(bean.getCompany()));
                            NavUtil.goToNewAct(MyCollectionActivity.this, WebActivity.class, bundle);
                        }
                    });
                    StringBuilder sb = new StringBuilder();
                    int c = 0;
                    int volume = 0;
                    for (LineBean line : bean.getCompany().getLines()) {
                        sb.append(bean.getCompany().getCity() + bean.getCompany().getDistrict() + ">" + line.getEndCity() + line.getEndDistrict() + "  ");
                        c++;
                        volume += line.getVolume();
                        if (c >= 3) break;
                    }
                    StringUtil.setDifferentFontTextView(tvLine, "主营线路：", "#5a5a5a", 13, sb.toString(), "#000000", 13, MyCollectionActivity.this);
                    tvVolume.setText("成交量：" + volume);
                } catch (Exception e) {
                    ToastUtil.printErr(e);
                }

            }
        };
        lineAdapter = new RecyclerAdapter<CollectionBean>(this, lineDatas, R.layout.adapter_activity_my_collection_line) {
            @Override
            public void onBindViewHolder(ViewHolder vh, List<CollectionBean> datas, int position) {
                final CollectionBean bean = datas.get(position);
                LinearLayout llRoot = vh.getViewById(R.id.my_collection_line_ll_root, LinearLayout.class);
                ImageView ivLogo = vh.getViewById(R.id.my_collection_line_iv_logo, ImageView.class);
                TextView tvCompany = vh.getViewById(R.id.my_collection_line_tv_company, TextView.class);
                TextView tvLine = vh.getViewById(R.id.my_collection_line_tv_line, TextView.class);
                TextView tvDistance = vh.getViewById(R.id.my_collection_line_tv_distance, TextView.class);
                TextView tvPrice = vh.getViewById(R.id.my_collection_line_tv_price, TextView.class);
                TextView tvDuration = vh.getViewById(R.id.my_collection_line_tv_duration, TextView.class);
                TextView tvVolume = vh.getViewById(R.id.my_collection_line_tv_volume, TextView.class);
                TextView tvCancel = vh.getViewById(R.id.my_collection_line_tv_cancel, TextView.class);
                TextView tvContact = vh.getViewById(R.id.my_collection_line_tv_contact, TextView.class);
                TextView tvOrder = vh.getViewById(R.id.my_collection_line_tv_order, TextView.class);
                try {
                    MImageLoader.displayWithDefaultOptions(MyCollectionActivity.this, bean.getLine().getCompany().getLogo(), ivLogo);
                    tvCompany.setText(bean.getLine().getCompany().getName());
                    tvLine.setText(bean.getLine().getBeginCity() + bean.getLine().getBeginDistrict() + " > " + bean.getLine().getEndCity() + bean.getLine().getEndDistrict());
                    tvPrice.setText("￥：" + bean.getLine().getMinPrice() + "-" + bean.getLine().getMaxPrice() + "/kg  " + bean.getLine().getLightMinPrice() + "-" + bean.getLine().getLightMaxPrice() + "元/m³");
                    tvDuration.setText("时效：" + bean.getLine().getMinDay() + "-" + bean.getLine().getMaxDay() + "天");
                    tvDistance.setText(bean.getLine().getCompany().getAddress() + "  11km");
                    tvVolume.setText("成交量：" + bean.getLine().getVolume());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteLine = bean;
                        showTip("取消收藏", bean.getId() + "");
                    }
                });
                tvContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NavUtil.callPhone(MyCollectionActivity.this, bean.getLine().getPhone());
                    }
                });
                tvOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(PlaceAnOrderActivity.BUNDLE_KEY_BEAN, GsonUtil.getInstance().toJsonString(bean));
                        NavUtil.goToNewAct(MyCollectionActivity.this, PlaceAnOrderActivity.class, bundle);
                    }
                });
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_LINE_DETAIL);
                        bundle.putString(WebActivity.BUNDLE_KEY_JSON, GsonUtil.getInstance().toJsonString(bean.getLine()));
                        NavUtil.goToNewAct(MyCollectionActivity.this, WebActivity.class, bundle);
                    }
                });
            }
        };
        rvAddresses.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = DensityUtil.dip2px(MyCollectionActivity.this, 3);
            }
        });

        lineAdapter.setDatas(lineDatas);
        rvAddresses.setAdapter(lineAdapter);
    }

    private void setListener() {
        srlAddresses.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                companyAdapter.resetPageIndex();
                lineAdapter.resetPageIndex();
                loadData();
            }
        });
        rvAddresses.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == rvAddresses.getAdapter().getItemCount()) {
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

    public void showTip(String text, final String id) {
        if (tipDialog == null) {
            tipDialog = new TipDialog(this, R.style.tipDialog);
        }
        tipDialog.setContent("真的要" + text + "吗？", null, null);
        tipDialog.setOnListener(new TipDialog.OnMOKListener() {
            @Override
            public void onClick(TipDialog dialog, View view) {
                requestUnCollection(id);
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

    @OnClick(R.id.activity_my_collection_tv_line)
    public void onLine(View v) {
        tvCompany.setBackground(null);
        tvCompany.setTextColor(getResources().getColor(R.color.white));
        tvLine.setTextColor(getResources().getColor(R.color.head_bg));
        tvLine.setBackgroundResource(R.drawable.white_half_rounded_left);
        rvAddresses.setAdapter(lineAdapter);
        action = "line";
        lineAdapter.resetPageIndex();
        loadData();
    }

    @OnClick(R.id.activity_my_collection_tv_company)
    public void onCompany(View v) {
        tvLine.setBackground(null);
        tvLine.setTextColor(getResources().getColor(R.color.white));
        tvCompany.setBackgroundResource(R.drawable.white_half_rounded_right);
        tvCompany.setTextColor(getResources().getColor(R.color.head_bg));
        rvAddresses.setAdapter(companyAdapter);
        action = "company";
        companyAdapter.resetPageIndex();
        loadData();
    }

    @Override
    public void loadData() {
        super.loadData();
        List<Object> body = new ArrayList<>();
        body.add(MApplication.getApp().getAccountWithLogin().getId());
        if (action.equals("line")) {
            body.add(ApiCollection.TYPE_LINE);
        } else if (action.equals("company")) {
            body.add(ApiCollection.TYPE_COMPANY);
        }
        ApiCollection.getInstance().getList(this, body);
        srlAddresses.setRefreshing(true);
    }

    public void loadMore() {
        if (action.equals("line")) {
            if (lineAdapter.hasMorePage()) {
                lineAdapter.nextPage();
                loadData();
                return;
            }
        } else if (action.equals("company")) {
            if (companyAdapter.hasMorePage()) {
                companyAdapter.nextPage();
                loadData();
                return;
            }
        }
        ToastUtil.toastAlways(this, "没有更多的数据了哦！");
    }

    public void requestUnCollection(String id) {
        RequestBody body = new FormEncodingBuilder()
                .add("accountId", MApplication.getApp().getAccountWithLogin().getId() + "")
                .add("collectionId", id)
                .build();
        ApiCollection.getInstance().postDelete(this, body);
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        if (url.equals(ApiCollection.URL_LIST)) {
            List<CollectionBean> list = GsonUtil.getInstance().toJsonArr(jo.getAsJsonArray(Api.KEY_DATA).toString(), new TypeToken<List<CollectionBean>>() {
            });
            if (action.equals("line")) {
                if (lineAdapter.isFirstPage()) {
                    lineDatas = list;
                } else {
                    lineDatas.addAll(list);
                }
                updateView(new Runnable() {
                    @Override
                    public void run() {
                        lineAdapter.setDatas(lineDatas);
                        rvAddresses.setAdapter(lineAdapter);
                    }
                });
            } else if (action.equals("company")) {
                if (companyAdapter.isFirstPage()) {
                    companyDatas = list;
                } else {
                    companyDatas.addAll(list);
                }
                updateView(new Runnable() {
                    @Override
                    public void run() {
                        companyAdapter.setDatas(companyDatas);
                        rvAddresses.setAdapter(companyAdapter);
                    }
                });
            }
        } else if (url.equals(ApiCollection.URL_DELETE)) {
            if (action.equals("line")) {
                lineDatas.remove(deleteLine);
                updateView(new Runnable() {
                    @Override
                    public void run() {
                        lineAdapter.setDatas(lineDatas);
                    }
                });
            } else if (action.equals("company")) {
                companyDatas.remove(deleteCompany);
                updateView(new Runnable() {
                    @Override
                    public void run() {
                        companyAdapter.setDatas(companyDatas);
                    }
                });
            }
        }
    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        updateView(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastAlways(MyCollectionActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
            }
        });
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
