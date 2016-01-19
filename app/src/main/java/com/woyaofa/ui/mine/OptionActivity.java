package com.woyaofa.ui.mine;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lib_common.activity.ImageListActivity;
import com.lib_common.dialog.GenderSelectDialog;
import com.lib_common.dialog.TipDialog;
import com.lib_common.util.DateUtil;
import com.lib_common.util.FileUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.PhotoUtil;
import com.lib_common.util.ToastUtil;
import com.lib_common.widgt.RoundImageView;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.woyaofa.MApplication;
import com.woyaofa.MBaseActivity;
import com.woyaofa.R;
import com.woyaofa.api.Api;
import com.woyaofa.api.ApiUser;
import com.woyaofa.bean.AccountBean;
import com.woyaofa.ui.widget.InputDialog;
import com.woyaofa.util.NavUtil;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 设置
 */
public class OptionActivity extends MBaseActivity {

    public static final int CODE_REQUEST_IMAGE_LIST = 1205;
    public static final int CODE_REQUEST_PIC_CROP = 1206;

    @Bind(R.id.activity_option_iv_logo)
    RoundImageView ivLogo;
    @Bind(R.id.activity_option_tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.activity_option_tv_gender)
    TextView tvGender;
    @Bind(R.id.activity_option_et_nickname)
    TextView etNickname;
    private GenderSelectDialog genderSelectDialog;
    private static Uri tempUri;
    private DatePickerDialog datePickerDialog;

    private String nickname;
    private String gender;
    private long birthday;
    private InputDialog inputDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_option);
    }

    @Override
    public void loadData() {
        super.loadData();
        try {
            nickname = MApplication.getApp().getAccount().getUser().getName();
            gender = MApplication.getApp().getAccount().getUser().getSex() + "";
            birthday = MApplication.getApp().getAccount().getUser().getBirthday();
            tvBirthday.setText(DateUtil.datetimeToString(new Date(MApplication.getApp().getAccount().getUser().getBirthday()), "yyyy-MM-dd"));
            tvGender.setText(turnGender(MApplication.getApp().getAccount().getUser().getSex() + ""));
            etNickname.setText(MApplication.getApp().getAccount().getUser().getName());
            MImageLoader.displayWithDefaultOptions(this, MApplication.getApp().getAccount().getUser().getLogo(), ivLogo);
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }

    @OnClick(R.id.activity_option_ll_logo)
    public void onLogo(View v) {
        Bundle bundle = new Bundle();
        ImageListActivity.Config config = new ImageListActivity.Config();
        config.maxSelectNum = 1;
        bundle.putSerializable(ImageListActivity.BUNDLE_KEY_CONFIG, config);
        NavUtil.goToNewActForResult(this, ImageListActivity.class, bundle, CODE_REQUEST_IMAGE_LIST);
    }

    @OnClick(R.id.activity_option_ll_nickname)
    public void onNickname(View v) {
//        if (etNickname.isEnabled()) {
//            showKeyBoard(etNickname);
//            nickname = etNickname.getText().toString();
//            if (StringUtil.isEmpty(nickname)) {
//                ToastUtil.toastAlways(this, "昵称不能为空！");
//                return;
//            }
//            if (!nickname.equals(MApplication.getApp().getAccount().getUser().getName())) {
//                etNickname.setEnabled(false);
//            } else {
//                etNickname.setEnabled(false);
//            }
//        } else {
//            etNickname.setEnabled(true);
//        }
        if (inputDialog == null) {
            inputDialog = new InputDialog(this, R.style.tipDialog);
            inputDialog.setOnCallBackListener(new InputDialog.OnCallBackListener() {
                @Override
                public void ok(InputDialog inputDialog, String content) {
                    if (!nickname.equals(content)) {
                        nickname = content;
                        etNickname.setText(content);
                        requestNickName(content);
                    } else {
                    }
                    inputDialog.dismiss();
                }

                @Override
                public void cancel(InputDialog inputDialog) {
                    inputDialog.dismiss();
                }
            });
        }
        inputDialog.show();
        inputDialog.setInit("修改昵称", nickname, null);
    }

    private void requestNickName(String nickname) {
        RequestBody body = new FormEncodingBuilder().add(
                "id", MApplication.getApp().getAccount().getUser().getId() + "")
                .add("name", nickname)
                .build();
        ApiUser.getInstance().postModify(OptionActivity.this, body);
    }

    @OnClick(R.id.activity_option_ll_gender)
    public void onGender(View v) {
        if (genderSelectDialog == null) {
            genderSelectDialog = new GenderSelectDialog(this,
                    R.style.toast_box_dialog);
            genderSelectDialog.setGender("" + MApplication.getApp().getAccount().getUser().getSex());
            genderSelectDialog.setSelectCallBack(new GenderSelectDialog.SelectCallBack() {

                @Override
                public void ok(String g) {
                    gender = g;
                    tvGender.setText(turnGender(gender));
                    RequestBody body = new FormEncodingBuilder().add(
                            "id", MApplication.getApp().getAccount().getUser().getId() + "")
                            .add("sex", gender)
                            .build();
                    ApiUser.getInstance().postModify(OptionActivity.this, body);
                }
            });
        }
        genderSelectDialog.show();
    }

    public String turnGender(String gender) {
        if (GenderSelectDialog.FEMALE.equals(gender)) {
            return "女";
        }
        return "男";
    }

    @OnClick(R.id.activity_option_ll_birthday)
    public void onBirthday(View v) {
        if (datePickerDialog == null) {
            int[] ytd;
            ytd = DateUtil.getYTD(new Date(MApplication.getApp().getAccount().getUser().getBirthday()));
            datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            birthday = DateUtil.stringToDatetime(str, "yyyy-MM-dd").getTime();
                            tvBirthday.setText(str);
                            RequestBody body = new FormEncodingBuilder().add(
                                    "id", MApplication.getApp().getAccount().getUser().getId() + "")
                                    .add("birthday", birthday + "")
                                    .build();
                            ApiUser.getInstance().postModify(OptionActivity.this, body);
                            datePickerDialog.dismiss();
                        }
                    }, ytd[0], ytd[1], ytd[2]);
        }
        datePickerDialog.show();
    }

    @OnClick(R.id.activity_option_ll_safe)
    public void onSafe(View v) {
        NavUtil.goToNewAct(this, SafeActivity.class);
    }

    @OnClick(R.id.activity_option_tv_logout)
    public void onLogout(View v) {
        new TipDialog(this, R.style.tipDialog)
                .setContent("确认退出吗？", null, null)
                .setOnListener(new TipDialog.OnMOKListener() {
                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        dialog.dismiss();
                        MApplication.getApp().logout();
                        MApplication.getApp().updateActivities();
                        onBack(null);
                        MApplication.getApp().getAccountWithLogin();
                    }
                }, new TipDialog.OnMCancelListener() {
                    @Override
                    public void onClick(TipDialog dialog, View view) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void setRequestSuc(String url, String statusCode, final JsonObject jo) {
        super.setRequestSuc(url, statusCode, jo);
        if (url.equals(ApiUser.URL_FACE)) {
//            RequestBody body = new FormEncodingBuilder()
//                    .add("username", MApplication.getApp().getAccount().getName()).add("password", MD5Util.md5(MApplication.getApp().getAccount().getPassword())).build();
//            ApiAccount.getInstance().postLogin(this, body);
            MApplication.getApp().requestOnBack();
//        } else if (url.equals(ApiAccount.URL_LOGIN)) {
//            AccountBean accountBean = GsonUtil.getInstance().toJsonObj(jo.getAsJsonObject(Api.KEY_DATA), AccountBean.class);
//            accountBean.setPassword(MApplication.getApp().getAccount().getPassword());
//            MApplication.getApp().saveUserSync(accountBean);
        } else {
            AccountBean account = MApplication.getApp().getAccount();
            account.getUser().setName(nickname);
            account.getUser().setBirthday(birthday);
            account.getUser().setSex(Integer.valueOf(gender));
            MApplication.getApp().saveUserSync(account);
            updateView(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastAlways(OptionActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
                    MApplication.getApp().updateActivities();
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
                ToastUtil.toastAlways(OptionActivity.this, jo.getAsJsonPrimitive(Api.KEY_MSG).getAsString());
                loadData();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CODE_REQUEST_IMAGE_LIST
                    && resultCode == ImageListActivity.CODE_RESULT) {
                List<ImageListActivity.ImageBean> entities = (ArrayList<ImageListActivity.ImageBean>) data
                        .getSerializableExtra(ImageListActivity.BUNDLE_KEY_SELECTED_IMAGES);
                if (entities == null || entities.size() == 0) {
                    return;
                }
                tempUri = NavUtil.goToPicCrop(this, "file://"
                        + entities.get(0).getImageUrl(), CODE_REQUEST_PIC_CROP);
            } else if (requestCode == CODE_REQUEST_PIC_CROP
                    && resultCode == RESULT_OK) {
                String url = tempUri.getPath();
//                InputStream is = PhotoUtil.uploadCompressBitmap(this, url);
//                File file = FileUtil.inputstreamToFile(is, "face.png");
                RequestBody body = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("image", "face.png", RequestBody.create(MediaType.parse("image/*"), new File(url)))
                        .addFormDataPart("userId", "" + MApplication.getApp().getAccountWithLogin().getUser().getId())
                        .addFormDataPart("accountId", "" + MApplication.getApp().getAccountWithLogin().getId())
                        .build();
                ApiUser.getInstance().postFace(this, body);
                showLoading();
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }


}
