package com.lib_common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class CommonExpandableAdapter<k, v> extends BaseExpandableListAdapter {
    private final Context context;
    private final int groupLayoutRes;
    private final int childLayoutRes;
    private LinkedHashMap<k, List<v>> datas;
    private List<k> keys;

    public CommonExpandableAdapter(Context context, LinkedHashMap<k, List<v>> datas, int groupLayoutRes, int childLayoutRes) {
        this.context = context;
        this.datas = datas;
        this.groupLayoutRes = groupLayoutRes;
        this.childLayoutRes = childLayoutRes;
        keys = new ArrayList<>();
        for (Map.Entry<k, List<v>> entry : datas.entrySet()) {
            keys.add(entry.getKey());
        }
    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(keys.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return keys.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(keys.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(context, groupLayoutRes,
                convertView, parent);
        dealGroupViews(holder, keys, groupPosition, isExpanded);
        return holder.getConvertView();
    }

    public abstract void dealGroupViews(ViewHolder holder, List<k> keys, int groupPosition, boolean isExpanded);


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getInstance(context, childLayoutRes,
                convertView, parent);
        dealChildViews(holder, datas.get(keys.get(groupPosition)), groupPosition, childPosition, isLastChild);
        return holder.getConvertView();
    }

    public abstract void dealChildViews(ViewHolder holder, List<v> childDatas, int groupPosition, int childPosition, boolean isLastChild);

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // ==================== 数据页 ========

    private int pageSize = 15;
    private int pageIndex = 1;
    private int totalSize = pageSize + 1;
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

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
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
        return pageSize * (pageIndex - firstPage + 1) < totalSize;
    }

    /**
     * 根据返回的集合数量判断是否还有下一页
     *
     * @param count
     * @return
     */
    public boolean hasMorePage(int count) {
        return count == pageSize;
    }

    // ==================== 是否请求中 ========================================================================

    private boolean isRequesting;

    public void setIsRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    public boolean isRequesting() {
        return isRequesting;
    }
}
