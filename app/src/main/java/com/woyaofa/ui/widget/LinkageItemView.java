package com.woyaofa.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lib_common.adapter.CommonAdapter;
import com.lib_common.adapter.ViewHolder;
import com.lib_common.bean.KVbean;
import com.lib_common.util.DensityUtil;
import com.woyaofa.R;

import java.util.List;


public class LinkageItemView extends LinearLayout {

    private Context context;
    private TextView tvText;
    private PopupWindow popupWindow;
    private List<KVbean> datas;
    private int textSize;
    private int textColor;
    private String text;

    public LinkageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LinkageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LinkageItemView(Context context) {
        super(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        if (attrs != null) {
            TypedArray a = null;
            try {
                a = context.obtainStyledAttributes(attrs, R.styleable.mAttr);
                textSize = DensityUtil.px2sp(context, a.getDimensionPixelSize(R.styleable.mAttr_textSize, 12));
                textColor = a.getColor(R.styleable.mAttr_textColor, getResources().getColor(android.R.color.black));
                text = a.getString(R.styleable.mAttr_text);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }
        inflate(context, R.layout.view_linkage_item, this);
        tvText = (TextView) findViewById(R.id.linkage_item_tv_text);
        setText(text, textColor, textSize);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow == null || !popupWindow.isShowing())
                    showPopup();
            }
        });
    }

    public void setText(String text, int textColor, float size) {
        tvText.setText(text);
        if (textColor != 0)
            tvText.setTextColor(textColor);
        if (size != 0)
            tvText.setTextSize(size);
    }

    public void setDatas(List<KVbean> datas) {
        this.datas = datas;
    }


    private void showPopup() {
        if (popupWindow == null) {
            ListView lv = new ListView(context);
            int p = DensityUtil.dip2px(context, 1);
            lv.setDividerHeight(p);
            lv.setBackgroundResource(R.drawable.orange_straight_corner);
            lv.setPadding(p, p, p, p);
            lv.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lv.setAdapter(new CommonAdapter<KVbean>(context, datas, R.layout.adapter_view_linkage_item) {
                @Override
                public void dealViews(ViewHolder holder, final List<KVbean> datas, final int position) {
                    TextView tv = holder.getViewById(R.id.linkage_item_tv_text, TextView.class);
                    tv.setText(datas.get(position).getKey());
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvText.setText(datas.get(position).getKey());
                            if (onItemClickListener != null) {
                                onItemClickListener.onClick(popupWindow, v, datas.get(position));
                            }
                        }
                    });
                }
            });

            popupWindow = new PopupWindow(lv, getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white_state));
        }
        popupWindow.showAsDropDown(this);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(PopupWindow popupWindow, View v, KVbean kv);
    }

}
