package com.lib_common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> datas;
    private int layoutId;

    /**
     * @param context
     * @param datas    数据集
     * @param layoutId adapter布局的资源id
     */
    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        super();
        this.context = context;
        this.datas = datas;
        this.layoutId = layoutId;
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

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(context, layoutId,
                convertView, parent);
        dealViews(holder, datas, position);
        return holder.getConvertView();
    }

    public abstract void dealViews(ViewHolder holder, List<T> datas, int position);

    // ==================== 数据页 ========

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
