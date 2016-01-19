package com.woyaofa.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib_common.util.DensityUtil;
import com.lib_common.util.ToastUtil;
import com.woyaofa.R;

/**
 *
 */
public class ItemView extends LinearLayout {

    private Context context;
    private ImageView ivIcon;
    private TextView tvText;
    private LinearLayout llRoot;
    private int textSize;
    private int iconSrc;
    private String text;
    private int textColor;
    private int paddingLeft;
    private boolean isNeedArrow;
    private ImageView ivArrow;

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ItemView(Context context) {
        super(context, null);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;

        if (attrs != null) {
            TypedArray a = null;
            try {
                a = context.obtainStyledAttributes(attrs, R.styleable.mAttr);
                textSize = DensityUtil.px2sp(context, a.getDimensionPixelSize(R.styleable.mAttr_textSize, 12));
                textColor = a.getColor(R.styleable.mAttr_textColor, getResources().getColor(android.R.color.black));
                text = a.getString(R.styleable.mAttr_text);
                iconSrc = a.getResourceId(R.styleable.mAttr_iconSrc, -1);
                paddingLeft = a.getDimensionPixelSize(R.styleable.mAttr_paddingLeft, 0);
                isNeedArrow = a.getBoolean(R.styleable.mAttr_isNeedArrow, true);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }

        inflate(context, R.layout.view_item, this);
        ivIcon = (ImageView) findViewById(R.id.view_item_iv_icon);
        ivArrow = (ImageView) findViewById(R.id.view_item_iv_arrow);
        tvText = (TextView) findViewById(R.id.view_item_tv_text);
        llRoot = (LinearLayout) findViewById(R.id.view_item_ll_root);

        setText(text, textSize, textColor);
        tvText.setPadding(paddingLeft, 0, 0, 0);
        if (isNeedArrow) {
            ivArrow.setVisibility(View.VISIBLE);
        } else {
            ivArrow.setVisibility(View.GONE);
        }

        setIcon(iconSrc);

        this.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(v);
                }
            }
        });
    }

    private void setIcon(int iconSrc) {
        ivIcon.setImageResource(iconSrc);
    }

    public void setText(String text, float textSize, int textColor) {
        tvText.setText(text);
        tvText.setTextSize(textSize);
        tvText.setTextColor(textColor);
    }

    private OnClickListener clickListener;

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnClickListener {
        void onClick(View v);
    }

}
