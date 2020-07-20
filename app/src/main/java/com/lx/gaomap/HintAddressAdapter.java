package com.lx.gaomap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Tip;

import java.util.List;


public class HintAddressAdapter extends BaseAdapter {
    Context context;
    List<Tip> list;

    public HintAddressAdapter(Context context, List<Tip> list) {
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

        holder.tvName.setText(list.get(i).getName());
        holder.tvAddress.setText(list.get(i).getAddress());
        return view;
    }

    class ViewHolder {
        TextView tvName, tvAddress;
    }

}
