package com.woyaofa.ui.mine;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.adapter.RecyclerAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.DateUtil;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiCollection;
import com.woyaofa.api.ApiMessage;
import com.woyaofa.bean.MessageBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * 我的消息
 */
public class MessageListActivity extends MBaseActivity {

    @Bind(R.id.message_list_srl_items)
    SwipeRefreshLayout srlItems;
    @Bind(R.id.message_list_rv_items)
    RecyclerView rvItems;

    private LinearLayoutManager layoutManager;
    private List<MessageBean> datas;
    private RecyclerAdapter<MessageBean> adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_message_list);
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
        adapter = new RecyclerAdapter<MessageBean>(this, datas, R.layout.adapter_activity_message_list) {
            @Override
            public void onBindViewHolder(ViewHolder vh, List<MessageBean> datas, int position) {
                final MessageBean bean = datas.get(position);
                TextView tvContent = vh.getViewById(R.id.message_list_tv_content, TextView.class);
                TextView tvTime = vh.getViewById(R.id.message_list_tv_time, TextView.class);
                TextView tvTitle = vh.getViewById(R.id.message_list_tv_title, TextView.class);
                tvContent.setText(bean.getMessage());
                tvTime.setText(DateUtil.datetimeToString(new Date(bean.getCreateTime()), DateUtil.DEFAULT_PATTERN));
                tvTitle.setText(bean.getTitle());
            }
        };
        rvItems.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = DensityUtil.dip2px(MessageListActivity.this, 3);
            }
        });
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

    @Override
    public void loadData() {
        super.loadData();
        List<Object> body = new ArrayList<>();
        body.add(adapter.getPageIndex());
        body.add(adapter.getPageSize());
        ApiMessage.getInstance().getList(this, body);
        srlItems.setRefreshing(true);
    }

    public void loadMore() {
        if (adapter.hasMorePage()) {
            loadData();
            return;
        }
        ToastUtil.toastAlways(this, "没有更多的数据了哦！");
    }

    @Override
    public void setRequestSuc(String url, String statusCode, JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        List<MessageBean> list = GsonUtil.getInstance().toJsonArr(jo.getAsJsonObject(Api.KEY_DATA).getAsJsonArray(Api.KEY_LIST).toString(), new TypeToken<List<MessageBean>>() {
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
                ToastUtil.toastAlways(MessageListActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
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
