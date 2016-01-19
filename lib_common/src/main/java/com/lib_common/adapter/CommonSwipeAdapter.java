package com.lib_common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 左右滑动删除的的adapter
 *
 * @param <T>
 * @author loar
 */
public abstract class CommonSwipeAdapter<T> extends BaseSwipeAdapter {

    private Context context;
    private List<T> datas;
    private int layoutId;
    private int swipeId;

    /**
     * @param mContext
     * @param datas
     * @param layoutId
     * @param swipeId  swipelayout的id
     */
    public CommonSwipeAdapter(Context mContext, List<T> datas, int layoutId,
                              int swipeId) {
        this.context = mContext;
        setDatas(datas);
        this.layoutId = layoutId;
        this.swipeId = swipeId;
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
        }
        notifyDataSetChanged();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return swipeId;
    }

    // ATTENTION: Never bind listener or fill values in generateView.
    // You have to do that in fillValues method.
    @Override
    public View generateView(int position, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(context, layoutId, null, null);
        return holder.getConvertView();
    }

    @Override
    public void fillValues(int position, View convertView) {
        ViewHolder holder = ViewHolder.getInstance(context, position,
                convertView, null);
        dealViews(holder, datas, position);
    }

    public abstract void dealViews(ViewHolder holder, List<T> beans,
                                   int position);

    @Override
    public int getCount() {
        int size = datas == null ? 0 : datas.size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ==================== 数据页 ======================================================

    private int pageSize = 15;
    private int pageIndex = 1;
    private int firstPage = pageIndex;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 设置请求第一页的下标从firstopage开始
     *
     * @param firstPage
     */
    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
        setPageIndex(firstPage);
    }

    public void resetPageIndex() {
        this.pageIndex = firstPage;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 下一页
     */
    public void nextPage() {
        this.pageIndex++;
    }

    /**
     * 上一页
     */
    public void prePage() {
        this.pageIndex--;
    }

    public boolean isFirstPage() {
        return firstPage == pageIndex;
    }

    /**
     * 根据总数判断是否还有下一页
     *
     * @return
     */
    public boolean hasMorePage() {
        return pageSize * (pageIndex - firstPage + 1) < getCount();
    }

    // ==================== 是否请求中 ========

    private boolean isRequesting;

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isRequesting() {
        return isRequesting;
    }
}
