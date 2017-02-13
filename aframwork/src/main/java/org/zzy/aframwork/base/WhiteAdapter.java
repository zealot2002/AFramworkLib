package org.zzy.aframwork.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by admin on 16/11/7.
 */
public abstract class WhiteAdapter<T> extends BaseAdapter {

    public static final String TAG = "WhiteAdapter";//类标识
    protected List<T> list;//数据列表
    protected Context context;//上下文
    protected LayoutInflater layoutInflater;//布局填充器
    protected int layoutId;//布局Id

    /**
     * WhiteAdapter的构造函数，在这里初始化各项属性值
     * @param context  上下文
     * @param layoutId 布局Id
     * @param list     数据列表
     */
    public WhiteAdapter(Context context, int layoutId, List<T> list) {
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);//获取布局填充器实例
    }
    public WhiteAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        layoutInflater = LayoutInflater.from(context);//获取布局填充器实例
    }
    public void setList(List<T> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
    }

    @Override
    public T getItem(int position) {
        return list == null?null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //复用ViewHolder
        WhiteViewHolder whiteViewHolder = WhiteViewHolder.getInstance(context, convertView, parent, layoutId, position);
        //用于实现数据设置的开放方法
        initialize(whiteViewHolder, getItem(position), position);
        //返回View
        return whiteViewHolder.getConvertView();
    }
    //数据设置的抽象方法
    public abstract void initialize(WhiteViewHolder holder, T item, int position);

}