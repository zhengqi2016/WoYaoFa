package com.lib_common.widgt;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

public class NoScrollStaggeredGridLayoutManager extends
		StaggeredGridLayoutManager {

	private List<MeasuredDimension> measuredDimensions;

	public NoScrollStaggeredGridLayoutManager(int spanCount, int orientation) {
		super(spanCount, orientation);
	}

	@Override
	public void onMeasure(RecyclerView.Recycler recycler,
			RecyclerView.State state, int widthSpec, int heightSpec) {
		measuredDimensions = new ArrayList<MeasuredDimension>();
		for (int i = 0; i < getSpanCount(); i++) {
			MeasuredDimension obj = new MeasuredDimension();
			measuredDimensions.add(obj);
		}
		final int widthMode = View.MeasureSpec.getMode(widthSpec);
		final int heightMode = View.MeasureSpec.getMode(heightSpec);
		final int widthSize = View.MeasureSpec.getSize(widthSpec);
		final int heightSize = View.MeasureSpec.getSize(heightSpec);
		int width = 0;
		int height = 0;
		MeasuredDimension temp = new MeasuredDimension();
		for (int i = 0; i < getItemCount(); i++) {
			MeasuredDimension item = measuredDimensions.get(i % getSpanCount());
			measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(i,
					View.MeasureSpec.UNSPECIFIED),
					View.MeasureSpec.makeMeasureSpec(i,
							View.MeasureSpec.UNSPECIFIED), temp);
			item.width = temp.width + item.width;
			item.height = temp.height + item.height;
//			if (getOrientation() == HORIZONTAL) {
//				item.width = temp.width + item.width;
//				if (i == 0) {
//					item.height = temp.height + item.height;
//				}
//			} else {
//				item.height = temp.height + item.height;
//				if (i == 0) {
//					item.width = temp.width + item.width;
//				}
//			}
		}
		temp.height = 0;
		temp.width = 0;
		for (int i = 0; i < measuredDimensions.size(); i++) {
			if (measuredDimensions.get(i).width > temp.width) {
				temp.width = measuredDimensions.get(i).width;
			}
			if (measuredDimensions.get(i).height > temp.height) {
				temp.height = measuredDimensions.get(i).height;
			}
		}
		if (getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
			height = temp.height;
		} else if (getOrientation() == StaggeredGridLayoutManager.HORIZONTAL) {
			width = temp.width / getSpanCount();
		}

		switch (widthMode) {
		case View.MeasureSpec.EXACTLY:
			width = widthSize;
		case View.MeasureSpec.AT_MOST:
		case View.MeasureSpec.UNSPECIFIED:
		}

		switch (heightMode) {
		case View.MeasureSpec.EXACTLY:
			height = heightSize;
		case View.MeasureSpec.AT_MOST:
		case View.MeasureSpec.UNSPECIFIED:
		}

		setMeasuredDimension(width, height);
	}

	private void measureScrapChild(RecyclerView.Recycler recycler,
			int position, int widthSpec, int heightSpec,
			MeasuredDimension measuredDimension) {
		View view = recycler.getViewForPosition(position);
//		View view =getChildAt(position);
		if (view != null) {
			RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view
					.getLayoutParams();
			int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
					getPaddingLeft() + getPaddingRight(), p.width);
			int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
					getPaddingTop() + getPaddingBottom(), p.height);
			view.measure(childWidthSpec, childHeightSpec);
			measuredDimension.width = view.getMeasuredWidth() + p.leftMargin
					+ p.rightMargin;
			measuredDimension.height = view.getMeasuredHeight()
					+ p.bottomMargin + p.topMargin;
			recycler.recycleView(view);
		}
	}

	class MeasuredDimension {
		public int height;
		public int width;
	}
}
