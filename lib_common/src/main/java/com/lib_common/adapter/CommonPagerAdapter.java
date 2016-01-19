package com.lib_common.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonPagerAdapter<T> extends PagerAdapter {

	private int layoutId;
	private Context context;
	private List<T> datas;

	public CommonPagerAdapter(Context context, List<T> datas, int layoutId) {
		super();
		this.context = context;
		this.layoutId = layoutId;
		setDatas(datas);
	}

	public void setDatas(List<T> datas) {
		if (datas == null) {
			this.datas = new ArrayList<T>();
		} else {
			this.datas = datas;
		}
		notifyDataSetChanged();
	}

	public void addDatas(List<T> datas) {
		if (datas != null) {
			this.datas.addAll(datas);
			notifyDataSetChanged();
		}
	}

	// viewpager中的组件数量
	@Override
	public int getCount() {
		return datas.size();
	}

	// 滑动切换的时候销毁当前的组件
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	// 每次滑动的时候生成的组件
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ViewHolder holder = ViewHolder.getInstance(context, layoutId, null,
				container);
		dealViews(holder, datas, position);
		((ViewPager) container).addView(holder.getConvertView());
		return holder.getConvertView();
	}

	public abstract void dealViews(ViewHolder holder, List<T> datas,
			int position);

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
}
