package com.lib_common.listener;

import java.lang.reflect.Field;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;

import com.lib_common.util.ToastUtil;


public abstract class OnPageChangeListener implements
		ViewPager.OnPageChangeListener {
	private EdgeEffectCompat leftEdge;

	private EdgeEffectCompat rightEdge;

	public OnPageChangeListener(ViewPager viewPager) {
		try {
			Field leftEdgeField = viewPager.getClass().getDeclaredField(
					"mLeftEdge");
			Field rightEdgeField = viewPager.getClass().getDeclaredField(
					"mRightEdge");
			if (leftEdgeField != null && rightEdgeField != null) {
				leftEdgeField.setAccessible(true);
				rightEdgeField.setAccessible(true);
				leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewPager);
				rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewPager);
			}
		} catch (Exception e) {
			ToastUtil.printErr(e);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (leftEdge != null && rightEdge != null) {
			leftEdge.finish();
			rightEdge.finish();
			leftEdge.setSize(0, 0);
			rightEdge.setSize(0, 0);
		}

	}
}