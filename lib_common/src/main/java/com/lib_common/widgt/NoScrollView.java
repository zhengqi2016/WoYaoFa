package com.lib_common.widgt;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class NoScrollView extends ScrollView {

	public NoScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public NoScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
