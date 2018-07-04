package com.example.just.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.just.Avtivity.DetailActivity;
import com.example.just.Avtivity.TaobaoDetailActivity;
import com.example.just.Bean.Taobao;
import com.example.just.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yg on 18-7-2.
 * 淘宝列表的适配器
 */

public class TaobaoAdapter extends RecyclerView.Adapter<TaobaoAdapter.ViewHolder> {

    private Context mContext;
    private List<Taobao> mTaobaoList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image;
        TextView price;
        TextView count;
        TextView name;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            image = (ImageView) view.findViewById(R.id.taobao_image);
            name = (TextView) view.findViewById(R.id.taobao_name);
            price = view.findViewById(R.id.taobao_price);
            count = view.findViewById(R.id.taobao_count);
        }
    }

    public TaobaoAdapter(List<Taobao> taobaoList){
        mTaobaoList.clear();
        mTaobaoList = taobaoList;
    }

    @Override
    public TaobaoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.taobao_item,
                parent, false);
        final TaobaoAdapter.ViewHolder holder = new TaobaoAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Taobao taobao = mTaobaoList.get(position);
                Intent intent = new Intent(mContext, TaobaoDetailActivity.class);
                StringBuilder sb = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                intent.putExtra("image", sb.append("http:").append(taobao.getImg()).toString());
                intent.putExtra("url", taobao.getUrl());
                intent.putExtra("title", taobao.getTitle());
                intent.putExtra("priceWap", taobao.getPriceWap());
                intent.putExtra("priceWithRate", taobao.getPriceWithRate());
                intent.putExtra("nick", taobao.getNick());
                intent.putExtra("sold", taobao.getSold());
                intent.putExtra("numiid", taobao.getNumiid());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(TaobaoAdapter.ViewHolder holder, int position) {
        Taobao taobao = mTaobaoList.get(position);
        holder.name.setText(taobao.getTitle());
        holder.count.setText(taobao.getSold());
        holder.price.setText(taobao.getPriceWithRate());
        StringBuilder sb = new StringBuilder();
        Glide.with(mContext).load(sb.append("http:").append(taobao.getImg()).toString()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mTaobaoList.size();
    }
}
