package com.woyaofa.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.woyaofa.R;

import java.util.ArrayList;
import java.util.List;


public class NavBarView extends RelativeLayout {

    private Integer[] resIds = {R.id.nav_bar_rl_tab0, R.id.nav_bar_rl_tab1, R.id.nav_bar_rl_tab2};
    private List<RelativeLayout> rls = new ArrayList<>();

    private Integer[] iconIds = {R.id.nav_bar_iv_tab0, R.id.nav_bar_iv_tab1, R.id.nav_bar_iv_tab2};
    public List<ImageView> iconViews = new ArrayList<>();

    private int lastSelectedIndex;

    public NavBarView(Context context) {
        super(context, null);
    }

    public NavBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.view_nav_bar, this);

        for (int i = 0; i < iconIds.length; i++) {
            ImageView iv = (ImageView) rootView.findViewById(iconIds[i]);
            iconViews.add(i, iv);
        }
        iconViews.get(0).setSelected(true);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabClick(v);
            }
        };

        for (int i = 0; i < resIds.length; i++) {
            RelativeLayout rl = (RelativeLayout) rootView.findViewById(resIds[i]);
            rl.setTag(i);
            rl.setOnClickListener(listener);
            rls.add(i, rl);
        }

    }

    public void onTabClick(View v) {
        Integer p = (Integer) v.getTag();
        if (onTabChangedCallback != null) {
            if (onTabChangedCallback.onTabChanged(v, p)) {
                selectTab(p);
            }
        }
    }

    public void selectTab(int index) {
        unSelectedAllIcons();
        iconViews.get(index).setSelected(true);
    }

    private void unSelectedAllIcons() {
        for (ImageView iv : iconViews) {
            iv.setSelected(false);
        }
    }

    private OnTabChangedCallback onTabChangedCallback;

    public void setOnTabChangedCallback(
            OnTabChangedCallback onTabChangedCallback) {
        this.onTabChangedCallback = onTabChangedCallback;
    }

    public interface OnTabChangedCallback {
        boolean onTabChanged(View view, int index);
    }
}
