package com.woyaofa.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.google.gson.JsonObject;
import com.lib_common.activity.ImageListActivity;
import com.lib_common.activity.ImageListActivity.ImageBean;
import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.util.DensityUtil;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.ToastUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiComment;
import com.woyaofa.api.ApiImage;
import com.woyaofa.bean.OrderBean;
import com.woyaofa.ui.main.MainActivity;
import com.woyaofa.util.NavUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 评价
 */
public class CommentActivity extends MBaseActivity {

    public static final String BUNDLE_KEY_BEAN = "jsonBean";
    public static final int CODE_REQUEST = 1354;

    @Bind(R.id.activity_evaluation_gv_images)
    GridView gvImages;
    @Bind(R.id.activity_evaluation_rb_score)
    RatingBar rbScore;
    @Bind(R.id.activity_evaluation_et_text)
    EditText etText;

    private List<ImageBean> datas;
    private CommonAdapter<ImageBean> adapter;
    private ImageBean addImage = new ImageBean(0, "" + R.mipmap.add_image, null, null);
    private AbsListView.LayoutParams params;
    private OrderBean orderBean;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_evaluation);
        init();
    }

    @Override
    public void dealIntent(Bundle bundle) {
        super.dealIntent(bundle);
        orderBean = GsonUtil.getInstance().toJsonObj(bundle.getString(BUNDLE_KEY_BEAN),OrderBean.class);
    }

    private void init() {
        hideKeyBoard();
        int w = DensityUtil.getScreenW(this);
        double wdp = (w - DensityUtil.sp2px(CommentActivity.this, 10) * 3) / 4 * 1.1;
        params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, (int) wdp);
        adapter = new CommonAdapter<ImageBean>(this, datas, R.layout.adapter_activity_evaluation) {
            @Override
            public void dealViews(ViewHolder holder, final List<ImageBean> datas, int position) {
                ImageBean bean = datas.get(position);
                final ArrayList<ImageBean> selectImages = new ArrayList<>(datas);
                selectImages.remove(addImage);
                LinearLayout llRoot = holder.getViewById(R.id.adapter_activity_evaluation_ll_root, LinearLayout.class);
                ImageView ivImage = holder.getViewById(R.id.adapter_activity_evaluation_iv_image, ImageView.class);
                MImageLoader.displayWithDefaultOptions(CommentActivity.this, bean.getImageUrl(), ivImage);

                llRoot.setLayoutParams(params);
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ImageListActivity.BUNDLE_KEY_SELECTED_IMAGES, selectImages);
                        ImageListActivity.Config config = new ImageListActivity.Config();
                        config.maxSelectNum = 7;
                        bundle.putSerializable(ImageListActivity.BUNDLE_KEY_CONFIG, config);
                        NavUtil.goToNewActForResult(CommentActivity.this, ImageListActivity.class, bundle, CODE_REQUEST);
                    }
                });
            }
        };
        gvImages.setAdapter(adapter);

        datas = new ArrayList<>();
        datas.add(addImage);
        updateView(new Runnable() {
            @Override
            public void run() {
                adapter.setDatas(datas);
            }
        });
    }

    @OnClick(R.id.activity_evaluation_tv_submit)
    public void onSubmit(View v) {
        if (etText.getText().toString().length() < 5) {
            ToastUtil.toastAlways(this, "评论内容不要少于5个字哦！");
            return;
        }
        float s = rbScore.getRating();
        RequestBody body = new FormEncodingBuilder()
                .add("lineId", orderBean.getLine().getId() + "")
                .add("content", etText.getText().toString())
                .add("score", "" + ((int) rbScore.getRating()))
                .add("accountId", "" + MApplication.getApp().getAccountWithLogin().getId())
                .add("orderId", "" + orderBean.getId())
                .build();
        ApiComment.getInstance().postAdd(this, body);
        showLoading();
    }

    private void uploadImage(String id) {
        if (datas.size()<=1){
            updateView(new Runnable() {

                @Override
                public void run() {
                    hideLoading();
                    ToastUtil.toastAlways(CommentActivity.this, "评价成功！");
                    MApplication.getApp().updateActivity(MainActivity.class);
                    onBack(null);
                }
            });
            return;
        }
        MultipartBuilder bm = new MultipartBuilder();
        bm.type(MultipartBuilder.FORM);
        datas.remove(addImage);
        for (int i = 0; i < datas.size(); i++) {
            ImageBean bean = datas.get(i);
            bm.addFormDataPart("image" + i, "image" + i + ".png", RequestBody.create(MediaType.parse("image/*"), new File(bean.getImageUrl())));
        }
        bm.addFormDataPart("commentId", id);
        ApiImage.getInstance().postComment(this, bm.build());
    }

    @Override
    public void setRequestSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        if (url.equals(ApiComment.URL_ADD)) {
            uploadImage(jo.getAsJsonPrimitive(Api.KEY_DATA).getAsString());
        } else if (url.equals(ApiImage.URL_COMMENT)) {
            updateView(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastAlways(CommentActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
                    MApplication.getApp().updateActivity(MainActivity.class);
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
                ToastUtil.toastAlways(CommentActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
            }
        });
    }

    @Override
    public void setRequestFinish(String url) {
        super.setRequestFinish(url);
        if (url.equals(ApiImage.URL_COMMENT)) {
            updateView(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST && resultCode == ImageListActivity.CODE_RESULT) {
            datas = (List<ImageBean>) data.getSerializableExtra(ImageListActivity.BUNDLE_KEY_SELECTED_IMAGES);
            datas.add(addImage);
            updateView(new Runnable() {
                @Override
                public void run() {
                    adapter.setDatas(datas);
                }
            });
        }
    }
}
