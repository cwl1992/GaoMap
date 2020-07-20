package com.lx.gaomap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.amap.api.services.core.PoiItem;

import java.util.List;

/**
 * Created by kxn on 2019/6/11 0011.
 */
public class PoiAdapter extends BaseAdapter {
    Context context;
    List<PoiItem> list;

    public PoiAdapter(Context context, List<PoiItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_address_hint, null);
            holder = new ViewHolder();
            holder.tvAddress = view.findViewById(R.id.tvAddress);
            holder.tvName = view.findViewById(R.id.tvName);
            view.setTag(holder);
        } else {
            holder = ((ViewHolder) view.getTag());
        }

        holder.tvName.setText(list.get(i).getTitle());
        holder.tvAddress.setText(list.get(i).getSnippet());
        return view;
    }

    class ViewHolder {
        TextView tvName, tvAddress;
    }

}

