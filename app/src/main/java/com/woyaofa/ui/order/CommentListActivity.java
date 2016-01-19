package com.woyaofa.ui.order;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lib_common.adapter.RecyclerAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiComment;
import com.woyaofa.bean.CommentBean;
import com.woyaofa.bean.LineBean;
import com.woyaofa.util.LevelUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 评价列表
 */
public class CommentListActivity extends MBaseActivity {

    public final static String BUNDLE_KEY_BEAN = "bean";

    @Bind(R.id.comment_list_rv_items)
    RecyclerView rvItems;
    @Bind(R.id.comment_list_srl_comment)
    SwipeRefreshLayout srlItems;
    @Bind(R.id.comment_list_tv_score)
    TextView tvScore;
    @Bind(R.id.comment_list_rb_rating)
    RatingBar rbScore;

    private LinearLayoutManager layoutManager;
    private List<CommentBean> datas;
    private RecyclerAdapter<CommentBean> adapter;
    private LinearLayoutManager itemLayoutManager;
    private LineBean lineBean;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_comment_list);
        init();
        setListener();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        lineBean = GsonUtil.getInstance().toJsonObj(bundle.getString(BUNDLE_KEY_BEAN), LineBean.class);
    }

    private void init() {
        itemLayoutManager = new LinearLayoutManager(this);
        itemLayoutManager.setOrientation(LinearLayout.HORIZONTAL);

        srlItems.setColorSchemeColors(Color.parseColor("#ff0000"), Color.parseColor("#00ff00"), Color.parseColor("#0000ff"));
        srlItems.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        rvItems.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter<CommentBean>(this, datas, R.layout.adapter_comment_list) {
            @Override
            public void onBindViewHolder(ViewHolder vh, List<CommentBean> datas, int position) {
                CommentBean bean = datas.get(position);
                ImageView ivLogo = vh.getViewById(R.id.comment_list_iv_logo, ImageView.class);
                TextView tvContent = vh.getViewById(R.id.comment_list_tv_content, TextView.class);
                TextView tvName = vh.getViewById(R.id.comment_list_tv_name, TextView.class);
                TextView tvScore = vh.getViewById(R.id.comment_list_tv_score, TextView.class);
                TextView tvLevel = vh.getViewById(R.id.comment_list_tv_level, TextView.class);
                RatingBar rbRating = vh.getViewById(R.id.comment_list_rb_rating, RatingBar.class);
                RecyclerView rvImages = vh.getViewById(R.id.comment_list_rv_images, RecyclerView.class);
                try{
                    tvName.setText(bean.getUser().getName());
                    tvScore.setText(bean.getScore() + "分");
                    tvContent.setText(bean.getContent());
                    rbRating.setRating(bean.getScore());
                    MImageLoader.displayWithDefaultOptions(CommentListActivity.this, bean.getUser().getLogo(), ivLogo);
                    setImages(bean, rvImages);
                    tvLevel.setText(LevelUtil.computeLevel(bean.getUser().getCredit()));
                }catch (Exception e){
                  ToastUtil.printErr(e);
                }
            }
        };
        rvItems.setAdapter(adapter);
        rvItems.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = DensityUtil.dip2px(CommentListActivity.this, 3);
            }
        });
    }

    public void setListener() {
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

    private void setImages(CommentBean bean, RecyclerView rvImages) {
        if (bean.getImages() == null) {
            rvImages.setVisibility(View.GONE);
        } else {
            rvImages.setVisibility(View.VISIBLE);
            RecyclerAdapter<String> itemAdapter = null;
            if (rvImages.getTag() == null) {
                itemAdapter = new RecyclerAdapter<String>(CommentListActivity.this, null, R.layout.adapter_comment_list_image) {
                    @Override
                    public void onBindViewHolder(ViewHolder vh, List<String> datas, int position) {
                        String bean = datas.get(position);
                        ImageView ivLogo = vh.getViewById(R.id.comment_list_image_iv_logo, ImageView.class);
                        MImageLoader.displayWithDefaultOptions(CommentListActivity.this, bean, ivLogo);
                    }
                };
                rvImages.setLayoutManager(itemLayoutManager);
                rvImages.setAdapter(itemAdapter);
            } else {
                itemAdapter = (RecyclerAdapter<String>) rvImages.getTag();
            }
            itemAdapter.setDatas(bean.getImages());
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        List<Object> body = new ArrayList<>();
        body.add(lineBean.getId());
        body.add(adapter.getPageIndex());
        body.add(adapter.getPageSize());
        ApiComment.getInstance().getList(this, body);
        srlItems.setRefreshing(true);
    }

    private void loadMore() {
        if (!adapter.hasMorePage()) {
            ToastUtil.toastAlways(this, "没有更多的评价了哦！");
            return;
        }
        adapter.nextPage();
        loadData();
    }

    @Override
    public void setRequestSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        if (jo.has(Api.KEY_DATA)){
            datas = GsonUtil.getInstance().toJsonArr(jo.getAsJsonObject(Api.KEY_DATA).getAsJsonArray(Api.KEY_LIST).toString(), new TypeToken<List<CommentBean>>() {
            });
            updateView(new Runnable() {
                @Override
                public void run() {
                    adapter.setDatas(datas);
//                rbScore.setRating(1.4f);
//                tvScore.setText(1.4 + "");
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
                ToastUtil.toastAlways(CommentListActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
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
