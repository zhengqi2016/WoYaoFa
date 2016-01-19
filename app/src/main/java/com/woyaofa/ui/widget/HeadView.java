package com.woyaofa.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.woyaofa.R;


public class HeadView extends RelativeLayout {

    private Context context;
    private TextView tvTitle;
    private TextView tvRight;
    private TextView tvLeft;
    private RelativeLayout rlLeft;
    private RelativeLayout rlRight;
    private String text;
    private boolean isNeedRight;
    private boolean isNeedLeft;
    private int iconSrc;

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HeadView(Context context) {
        super(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        if (attrs != null) {
            TypedArray a = null;
            try {
                a = context.obtainStyledAttributes(attrs, R.styleable.mAttr);
                text = a.getString(R.styleable.mAttr_text);
                isNeedRight = a.getBoolean(R.styleable.mAttr_isNeedRight, false);
                isNeedLeft = a.getBoolean(R.styleable.mAttr_isNeedLeft, false);
                iconSrc = a.getResourceId(R.styleable.mAttr_iconSrc, 0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }

        inflate(context, R.layout.view_head, this);
        tvTitle = (TextView) findViewById(R.id.head_tv_title);
        tvRight = (TextView) findViewById(R.id.head_tv_right);
        tvLeft = (TextView) findViewById(R.id.head_tv_left);
        rlLeft = (RelativeLayout) findViewById(R.id.head_rl_left);
        rlRight = (RelativeLayout) findViewById(R.id.head_rl_right);
        setRight(null, iconSrc);
        setTitle(text);
        if (isNeedRight) {
            rlRight.setVisibility(View.VISIBLE);
        } else {
            rlRight.setVisibility(View.INVISIBLE);
        }
        if (isNeedLeft) {
            rlLeft.setVisibility(View.VISIBLE);
        } else {
            rlLeft.setVisibility(View.INVISIBLE);
        }

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setLeft(String text, int bgResid) {
        if (text == null) {
            rlLeft.setVisibility(View.GONE);
        } else {
            rlLeft.setVisibility(View.VISIBLE);
        }
        tvLeft.setText(text);
        if (bgResid != 0) {
            tvLeft.setBackgroundResource(bgResid);
        }
    }

    public void setRight(String text, int bgResid) {
        if (text == null) {
            rlRight.setVisibility(View.GONE);
        } else {
            rlRight.setVisibility(View.VISIBLE);
        }
        tvRight.setText(text);
        if (bgResid != 0) {
            tvRight.setBackgroundResource(bgResid);
        }
    }

    public void setOnClickLeftListener(OnClickListener onClickLeftListener) {
        rlLeft.setOnClickListener(onClickLeftListener);
    }

    public void setOnClickRightListener(OnClickListener onClickRightListener) {
        rlRight.setOnClickListener(onClickRightListener);
    }

}
