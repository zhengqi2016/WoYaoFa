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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.adapter.RecyclerAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.bean.KVbean;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.PCAUtil;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiCompany;
import com.woyaofa.api.ApiLine;
import com.woyaofa.bean.CompanyBean;
import com.woyaofa.bean.LineBean;
import com.woyaofa.bean.PCABean;
import com.woyaofa.ui.WebActivity;
import com.woyaofa.ui.widget.LinkageDialogView;
import com.woyaofa.ui.widget.LinkageItemView;
import com.woyaofa.util.LocationUtil;
import com.woyaofa.util.NavUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 找物流
 */
public class FindLogisticsActivity extends MBaseActivity {

    @Bind(R.id.find_logistics_srl_items)
    SwipeRefreshLayout srlItems;
    @Bind(R.id.find_logistics_rv_items)
    RecyclerView rvItems;
    @Bind(R.id.find_logistics_liv_condition)
    LinkageItemView livCondition;
    @Bind(R.id.find_logistics_tv_to)
    TextView tvTo;
    @Bind(R.id.find_logistics_tv_from)
    TextView tvFrom;

    private LinearLayoutManager layoutManager;
    private List<CompanyBean> companyDatas;
    private RecyclerAdapter<CompanyBean> companyAdapter;
    private RecyclerAdapter<LineBean> lineAdapter;
    private List<LineBean> lineDatas;
    private PCABean pca;
    private LinkageDialogView linkageDialogView;
    private String fProvince;
    private String fCity;
    private String fDistrict;
    private String tProvince;
    private String tCity;
    private String tDistrict;
    private LatLng fLatLng = new LatLng(0, 0);
    private LatLng tLatLng = new LatLng(0, 0);
    private String distance = "0";
    private List<KVbean> distances = new ArrayList<>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_find_logistics);
        init();
        setListener();
    }

    private void init() {
        srlItems.setColorSchemeColors(Color.parseColor("#ff0000"), Color.parseColor("#00ff00"), Color.parseColor("#0000ff"));
        srlItems.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        rvItems.setLayoutManager(layoutManager);
        companyAdapter = new RecyclerAdapter<CompanyBean>(this, companyDatas, R.layout.adapter_activity_my_collection_company) {
            @Override
            public void onBindViewHolder(ViewHolder vh, List<CompanyBean> datas, int position) {
                final CompanyBean bean = datas.get(position);
                LinearLayout llRoot = vh.getViewById(R.id.my_collection_campany_ll_root, LinearLayout.class);
                LinearLayout llButtons = vh.getViewById(R.id.my_collection_company_ll_buttons, LinearLayout.class);
                ImageView ivLogo = vh.getViewById(R.id.my_collection_campany_iv_logo, ImageView.class);
                TextView tvName = vh.getViewById(R.id.my_collection_campany_tv_company, TextView.class);
                TextView tvLine = vh.getViewById(R.id.my_collection_campany_tv_line, TextView.class);
                TextView tvVolume = vh.getViewById(R.id.my_collection_campany_tv_volume, TextView.class);
                TextView tvDistance = vh.getViewById(R.id.my_collection_campany_tv_distance, TextView.class);
                ImageView ivCiac = vh.getViewById(R.id.my_collection_campany_iv_ciac, ImageView.class);
                ImageView ivQc = vh.getViewById(R.id.my_collection_campany_iv_qc, ImageView.class);
                llButtons.setVisibility(View.GONE);
                ivCiac.setVisibility(View.GONE);
                ivQc.setVisibility(View.GONE);
                if (bean.getqC() != null && bean.getqC() != 0) {
                    ivQc.setVisibility(View.VISIBLE);
                }
                if (bean.getcAIC() != null && bean.getcAIC() != 0) {
                    ivCiac.setVisibility(View.VISIBLE);
                }
                MImageLoader.displayWithDefaultOptions(FindLogisticsActivity.this, bean.getLogo(), ivLogo);
                tvName.setText(bean.getName());
                DecimalFormat df = new DecimalFormat("0.0");
                tvDistance.setText(bean.getAddress() + "  " + df.format(bean.getDistance() / 1000) + "km");
                int volume = 0;
                StringBuilder sb = new StringBuilder();
                int c = 0;
                for (LineBean line : bean.getLines()) {
                    sb.append(bean.getCity() + bean.getDistrict() + ">" + line.getEndCity() + line.getEndDistrict() + "  ");
                    c++;
                    volume += line.getVolume();
                    if (c >= 3) break;
                }
                tvVolume.setText("成交量：" + volume + "笔");
                tvLine.setText(null);
                StringUtil.setDifferentFontTextView(tvLine, "主营线路：", "#5a5a5a", 13, sb.toString(), "#000000", 13, FindLogisticsActivity.this);
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebActivity.BUNDLE_KEY_RIGHT, "投诉");
                        bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_COMPANY_INDEX);
                        bundle.putString(WebActivity.BUNDLE_KEY_JSON, GsonUtil.getInstance().toJsonString(bean));
                        NavUtil.goToNewAct(FindLogisticsActivity.this, WebActivity.class, bundle);
                    }
                });
            }
        };
        lineAdapter = new RecyclerAdapter<LineBean>(this, lineDatas, R.layout.adapter_activity_my_collection_line) {
            @Override
            public void onBindViewHolder(ViewHolder vh, List<LineBean> datas, int position) {
                final LineBean bean = datas.get(position);
                LinearLayout llRoot = vh.getViewById(R.id.my_collection_line_ll_root, LinearLayout.class);
                RelativeLayout rlButtons = vh.getViewById(R.id.my_collection_line_rl_buttons, RelativeLayout.class);
                ImageView ivLogo = vh.getViewById(R.id.my_collection_line_iv_logo, ImageView.class);
                TextView tvLine = vh.getViewById(R.id.my_collection_line_tv_line, TextView.class);
                TextView tvCompany = vh.getViewById(R.id.my_collection_line_tv_company, TextView.class);
                TextView tvPrice = vh.getViewById(R.id.my_collection_line_tv_price, TextView.class);
                TextView tvDuration = vh.getViewById(R.id.my_collection_line_tv_duration, TextView.class);
                TextView tvDistance = vh.getViewById(R.id.my_collection_line_tv_distance, TextView.class);
                TextView tvVolume = vh.getViewById(R.id.my_collection_line_tv_volume, TextView.class);
                ImageView ivCiac = vh.getViewById(R.id.my_collection_line_iv_ciac, ImageView.class);
                ImageView ivQc = vh.getViewById(R.id.my_collection_line_iv_qc, ImageView.class);
                rlButtons.setVisibility(View.GONE);
                ivCiac.setVisibility(View.GONE);
                ivQc.setVisibility(View.GONE);
                if (bean.getCompany().getqC() != null && bean.getCompany().getqC() != 0) {
                    ivQc.setVisibility(View.VISIBLE);
                }
                if (bean.getCompany().getcAIC() != null && bean.getCompany().getcAIC() != 0) {
                    ivCiac.setVisibility(View.VISIBLE);
                }
                MImageLoader.displayWithDefaultOptions(FindLogisticsActivity.this, bean.getCompany().getLogo(), ivLogo);
                tvLine.setText(bean.getBeginCity() + bean.getBeginDistrict() + " > " + bean.getEndCity() + bean.getEndDistrict());
                tvCompany.setText(bean.getCompany().getName());
                tvVolume.setText("成交量：" + bean.getVolume() + "笔");
                tvPrice.setText("￥：" + bean.getMinPrice() + "-" + bean.getMaxPrice() + "元/kg  " + bean.getLightMinPrice() + "-" + bean.getLightMaxPrice() + "元/m³");
                tvDuration.setText("时效：" + bean.getMinDay() + "-" + bean.getMaxDay() + "天");
//                DecimalFormat df = new DecimalFormat("0.0");
                tvDistance.setText(bean.getCompany().getAddress());
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_LINE_DETAIL);
                        bundle.putString(WebActivity.BUNDLE_KEY_JSON, GsonUtil.getInstance().toJsonString(bean));
                        NavUtil.goToNewAct(FindLogisticsActivity.this, WebActivity.class, bundle);
                    }
                });
            }
        };
        rvItems.setAdapter(companyAdapter);

        livCondition.setDatas(distances);


        LocationUtil.getInstance().startLocation(this, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        fProvince = amapLocation.getProvince();//省信息
                        fCity = amapLocation.getCity();//城市信息
                        fDistrict = amapLocation.getDistrict();//城区信息
                        fLatLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                        tvFrom.setText(fDistrict);
                        LocationUtil.getInstance().stopLocation();
                        reLoad();
                    }
                } else {
//                    显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                    ToastUtil.toast(FindLogisticsActivity.this, "location Error, ErrCode:"
//                            + amapLocation.getErrorCode() + ", errInfo:"
//                            + amapLocation.getErrorInfo());
                }
            }
        });
    }

    private void setListener() {
        livCondition.setOnItemClickListener(new LinkageItemView.OnItemClickListener() {
            @Override
            public void onClick(PopupWindow popupWindow, View v, KVbean s) {
                distance = s.getValue();
                popupWindow.dismiss();
                reLoad();
            }
        });

        srlItems.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                tvTo.setText("目的地");
//                tProvince = null;
                reLoad();
            }
        });
        rvItems.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int count;
                if (tProvince == null || tCity == null || tDistrict == null) {
                    count = companyAdapter.getItemCount();
                } else {
                    count = lineAdapter.getItemCount();
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == count) {
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
        if (fLatLng.latitude == 0 || fLatLng.longitude == 0) {
            return;
        }
        if (tProvince == null || tCity == null || tDistrict == null) {
            List<Object> body = new ArrayList<>();
            body.add("1000000");
            body.add(fLatLng.latitude);
            body.add(fLatLng.longitude);
            body.add(fProvince.replace("省", "").replace("市", "") + "," + fCity.replace("市", "") + "," + fDistrict);
            ApiCompany.getInstance().getList(this, body);
        } else {
            RequestBody body = new FormEncodingBuilder()
                    .add("type", distance)
                    .add("lat", fLatLng.latitude + "")
                    .add("lng", fLatLng.longitude + "")
                    .add("begin", fProvince.replace("省", "").replace("市", "") + "," + fCity.replace("市", "") + "," + fDistrict)
                    .add("end", tProvince + "," + tCity + "," + tDistrict)
                    .build();
            ApiLine.getInstance().postList(this, body);
        }
        srlItems.setRefreshing(true);
    }

    private void reLoad() {
        companyAdapter.resetPageIndex();
        lineAdapter.resetPageIndex();
        loadData();
    }

    private void loadMore() {
        if (tProvince == null || tCity == null || tDistrict == null) {
            if (!companyAdapter.hasMorePage()) {
                ToastUtil.toastAlways(this, "没有更多的信息了哦！");
                return;
            }
        } else {
            if (!lineAdapter.hasMorePage()) {
                ToastUtil.toastAlways(this, "没有更多的信息了哦！");
                return;
            }
        }
        companyAdapter.nextPage();
        lineAdapter.nextPage();
        loadData();
    }

    @OnClick(R.id.find_logistics_ll_from)
    public void onFrom(View v) {
        showPCADialog(tvFrom);
    }

    @OnClick(R.id.find_logistics_ll_to)
    public void onTo(View v) {
        showPCADialog(tvTo);
    }

    public void initPca() {
        if (linkageDialogView == null) {
            if (pca == null) {
                pca = PCAUtil.getPCA(this, "pca.json", PCABean.class);
            }
            linkageDialogView = new LinkageDialogView(this);
            linkageDialogView.setTitle("城市选择");
            linkageDialogView.setPca(pca);
        }
    }

    public void showPCADialog(final TextView tv) {
        initPca();
        linkageDialogView.setOnOkListener(new LinkageDialogView.OnOkListener() {
            @Override
            public void okClick(LinkageDialogView linkageDialogView, View v, String pName, String cName, String aName) {
                tv.setText(aName);
                if (tv.getId() == R.id.find_logistics_tv_from) {
                    fProvince = pName;
                    fCity = cName;
                    fDistrict = aName;
                    reLoad();
                } else if (tv.getId() == R.id.find_logistics_tv_to) {
                    tProvince = pName;
                    tCity = cName;
                    tDistrict = aName;
                    livCondition.setText("时效最快", 0, 0);
                    distances.add(new KVbean("时效最快", "" + ApiLine.TYPE_LIST_EFFECIVENESS, null, null));
                    distances.add(new KVbean("评论最多", "" + ApiLine.TYPE_LIST_EVALUATE, null, null));
                    distances.add(new KVbean("成交最多", "" + ApiLine.TYPE_LIST_VOLUME, null, null));
                    livCondition.setDatas(distances);
                    reLoad();
                }
                companyAdapter.setDatas(null);
                lineAdapter.setDatas(null);
                linkageDialogView.dismiss();
            }
        });
        linkageDialogView.show();
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        if (url.equals(ApiCompany.URL_LIST)) {
            List<CompanyBean> list = GsonUtil.getInstance().toJsonArr(jo.getAsJsonArray(Api.KEY_DATA).toString(), new TypeToken<List<CompanyBean>>() {
            });
            if (companyAdapter.isFirstPage()) {
                companyDatas = list;
            } else {
                companyDatas.addAll(list);
            }
            updateView(new Runnable() {
                @Override
                public void run() {
                    companyAdapter.setDatas(companyDatas);
                    rvItems.setAdapter(companyAdapter);
                }
            });
        } else if (url.equals(ApiLine.URL_LIST)) {
            List<LineBean> list = GsonUtil.getInstance().toJsonArr(jo.getAsJsonArray(Api.KEY_DATA).toString(), new TypeToken<List<LineBean>>() {
            });
            if (lineAdapter.isFirstPage()) {
                lineDatas = list;
            } else {
                lineDatas.addAll(list);
            }
            updateView(new Runnable() {
                @Override
                public void run() {
                    lineAdapter.setDatas(lineDatas);
                    rvItems.setAdapter(lineAdapter);
                }
            });
        }

    }

    @Override
    public void setRequestNotSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestNotSuc(url, statusCode, jo);
        if (jo.getAsJsonPrimitive(Api.KEY_STATUS).getAsString().equals("2001")) {
            companyDatas = null;
            lineDatas = null;
            updateView(new Runnable() {
                @Override
                public void run() {
                    companyAdapter.setDatas(companyDatas);
                    lineAdapter.setDatas(lineDatas);
                    ToastUtil.toastAlways(FindLogisticsActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
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
                srlItems.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (pca == null)
//            pca = PCAUtil.getPCA(this, "pca.json", PCABean.class);
        initPca();
    }

    private void getAddressGeo(double latitude, double longitude) {
        LocationUtil.getInstance().getAddressGeo(this, latitude, longitude, new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                if (rCode == 0) {
                    if (result != null && result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        fProvince = result.getRegeocodeAddress().getProvince();//省信息
                        fCity = result.getRegeocodeAddress().getCity();//城市信息
                        fDistrict = result.getRegeocodeAddress().getDistrict();//城区信息
                        tvFrom.setText(fCity);
                        LocationUtil.getInstance().stopLocation();
                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int code) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtil.getInstance().stopLocation();
    }
}
