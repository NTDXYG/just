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
import com.example.just.Bean.Story;
import com.example.just.Bean.Taobao;
import com.example.just.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/27.
 */

public class LoveAdapter extends RecyclerView.Adapter<LoveAdapter.ViewHolder> {

    private Context mContext;
    private List<Taobao> mTaobaoList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView storyImage;
        TextView storyName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            storyImage = (ImageView) view.findViewById(R.id.love_image);
            storyName = (TextView) view.findViewById(R.id.love_name);
        }
    }

    public LoveAdapter(List<Taobao> taobaoList) {

        mTaobaoList.clear();
        mTaobaoList = taobaoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.love_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Taobao taobao = mTaobaoList.get(position);
                Intent intent;
                if (taobao.getImg().contains("alicdn.com")) {
                    intent = new Intent(mContext, TaobaoDetailActivity.class);
                } else {
                    intent = new Intent(mContext, DetailActivity.class);
                }
                intent.putExtra("image", taobao.getImg());
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Taobao taobao = mTaobaoList.get(position);
        holder.storyName.setText(taobao.getTitle());
        Glide.with(mContext).load(taobao.getImg()).into(holder.storyImage);

    }


    @Override
    public int getItemCount() {
        return mTaobaoList.size();
    }
}
