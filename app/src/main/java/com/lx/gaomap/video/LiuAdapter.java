package com.lx.gaomap.video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lx.gaomap.R;
import com.lx.gaomap.bean.Fruit;

import java.util.List;

public class LiuAdapter extends RecyclerView.Adapter<LiuAdapter.ViewHolder> {

    private final List<Fruit> mFruitList;
    private OnItemClickListener onItemClickListener;

    public LiuAdapter(List<Fruit> list) {
        mFruitList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Fruit fruit = mFruitList.get(position);
        holder.imageView.setImageResource(fruit.getImageId());
        holder.textView.setText(fruit.getName());

        holder.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClickListener(position, fruit.getName());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        private final ImageView imageView;
        private final LinearLayout llView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            llView = itemView.findViewById(R.id.llView);

        }
    }


    public interface OnItemClickListener {
        void OnItemClickListener(int i, String name);
    }

    public void setOnItemClickListener(OnItemClickListener OnItemClickListener) {
        onItemClickListener = OnItemClickListener;
    }


}
