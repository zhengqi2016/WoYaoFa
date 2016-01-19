package com.woyaofa.ui.order;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib_common.activity.BaseActivity;
import com.lib_common.adapter.CommonExpandableAdapter;
import com.lib_common.adapter.ViewHolder;
import com.woyaofa.R;
import com.woyaofa.ui.widget.HeadView;
import com.woyaofa.ui.widget.LinkageDialogView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 发快递
 */
public class SendExpressActivity extends BaseActivity {

    @Bind(R.id.activity_send_express_hv_head)
    HeadView hvHead;

    @Bind(R.id.activity_send_express_elv_expresses)
    ExpandableListView elvExpresses;
    private CommonExpandableAdapter<String, String> adapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentViews(R.layout.activity_send_express);
        init();
        setListener();
    }

    private void init() {
        LinkedHashMap<String, List<String>> datas = new LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            ArrayList<String> child = new ArrayList<String>();
            child.add("child" + i);
            datas.put("" + i, child);
        }
        adapter = new CommonExpandableAdapter<String, String>(this, datas, R.layout.adapter_activity_send_express_group, R.layout.adapter_activity_send_express_child) {
            @Override
            public void dealGroupViews(ViewHolder holder, List<String> keys, int groupPosition, boolean isExpanded) {
                TextView tvText = holder.getViewById(R.id.adapter_activity_send_express_group_tv_text, TextView.class);
                tvText.setText(keys.get(groupPosition));
            }

            @Override
            public void dealChildViews(ViewHolder holder, List<String> childDatas, int groupPosition, int childPosition, boolean isLastChild) {
                LinearLayout llRoot = holder.getViewById(R.id.adapter_activity_send_express_child_ll_root, LinearLayout.class);
                TextView tvText = holder.getViewById(R.id.adapter_activity_send_express_child_tv_text, TextView.class);
                tvText.setText(childDatas.get(childPosition));
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                });
            }
        };
        elvExpresses.setAdapter(adapter);
        updateView(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    elvExpresses.expandGroup(i);
                }
            }
        });
    }

    private void setListener() {
        hvHead.setOnClickLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack(v);
            }
        });
        elvExpresses.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    public void showDialog() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("" + i);
        }
        LinkageDialogView linkageDialogView = new LinkageDialogView(this);
        linkageDialogView.show();
    }
}
