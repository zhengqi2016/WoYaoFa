package com.woyaofa.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lib_common.fragment.BaseFragment;
import com.lib_common.util.GsonUtil;
import com.lib_common.util.MImageLoader;
import com.lib_common.util.StringUtil;
import com.lib_common.util.ToastUtil;
import com.lib_common.widgt.RoundImageView;
import com.woyaofa.MApplication;
import com.woyaofa.R;
import com.woyaofa.bean.AccountBean;
import com.woyaofa.ui.WebActivity;
import com.woyaofa.ui.mine.AddressBookActivity;
import com.woyaofa.ui.mine.MyCollectionActivity;
import com.woyaofa.ui.mine.OptionActivity;
import com.woyaofa.ui.order.OrderHistoryActivity;
import com.woyaofa.util.LevelUtil;
import com.woyaofa.util.NavUtil;

import butterknife.Bind;
import butterknife.OnClick;

public class MineFrg extends BaseFragment {

    @Bind(R.id.frg_index_iv_logo)
    RoundImageView ivLogo;
    @Bind(R.id.frg_index_tv_name)
    TextView tvName;
    @Bind(R.id.frg_index_tv_credit)
    TextView tvCredit;

    @Override
    public View onContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ToastUtil.log("frg", "MineFrg");
        return inflater.inflate(R.layout.frg_mine, null, false);
    }

    @Override
    public void init(View rootView, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadData();
    }

    @Override
    public void loadData() {
        try {
            if (MApplication.getApp().getAccount() != null) {
                MImageLoader.displayWithDefaultOptions(getActivity(), MApplication.getApp().getAccount().getUser().getLogo(), ivLogo);
                tvName.setText(null);
                StringUtil.setDifferentFontTextView(tvName, MApplication.getApp().getAccount().getUser().getName(), "#ffffff", 12, "  " + LevelUtil.computeLevel(MApplication.getApp().getAccount().getUser().getCredit()), "#fbc22b", 12, getActivity());
                tvCredit.setText("积分: " + MApplication.getApp().getAccount().getUser().getCredit() + "");
            }
        } catch (Exception e) {
            ToastUtil.printErr(e);
        }
    }


    @OnClick(R.id.frg_mine_iv_address_book)
    public void onAddressBook(View v) {
        NavUtil.goToNewAct(getContext(), AddressBookActivity.class);
    }

    @OnClick(R.id.frg_mine_iv_collection)
    public void onCollection(View v) {
        NavUtil.goToNewAct(getContext(), MyCollectionActivity.class);
    }

    @OnClick(R.id.frg_mine_iv_contact)
    public void onContact(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.BUNDLE_KEY_TITLE, "联系我们");
        bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_CONTACT_US);
        NavUtil.goToNewAct(getContext(), WebActivity.class, bundle);
    }

    @OnClick(R.id.frg_mine_iv_feedback)
    public void onFeedback(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.BUNDLE_KEY_TITLE, "意见反馈");
        bundle.putString(WebActivity.BUNDLE_KEY_URL, WebActivity.URL_FILE_FEEDBACK);
        bundle.putString(WebActivity.BUNDLE_KEY_JSON, GsonUtil.getInstance().toJsonString(MApplication.getApp().getAccountWithLogin()));
        NavUtil.goToNewAct(getContext(), WebActivity.class, bundle);
    }

    @OnClick(R.id.frg_mine_iv_orders)
    public void onOrders(View v) {
        NavUtil.goToNewAct(getContext(), OrderHistoryActivity.class);
    }

    @OnClick({R.id.frg_index_ll_option, R.id.frg_index_iv_logo})
    public void onOption(View v) {
        NavUtil.goToNewAct(getContext(), OptionActivity.class);
    }


}
