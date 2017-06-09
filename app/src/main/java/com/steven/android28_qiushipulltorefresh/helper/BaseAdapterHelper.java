package com.steven.android28_qiushipulltorefresh.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseAdapterHelper<T> extends BaseAdapter {

    public Context context = null;
    // <T>T可以指定任意的类型作为参数
    public List<T> list = null;
    public LayoutInflater inflater = null;

    public BaseAdapterHelper(Context context , List<T> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void reloadListView(List<T> data, boolean isClear) {
        if (isClear) {
            list.clear();
        }
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position, convertView, parent, list, inflater);
    }
// BaseAdapterHelper被继承后,getView也被继承,所以抽象方法getItemView被重写就写了getView
    public abstract View getItemView(int position, View convertView, ViewGroup parent, List<T>
            list, LayoutInflater inflater);

}
