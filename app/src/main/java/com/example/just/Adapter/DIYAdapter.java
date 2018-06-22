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
import com.example.just.Bean.Story;
import com.example.just.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/15.
 */

public class DIYAdapter extends RecyclerView.Adapter<DIYAdapter.ViewHolder> {
    private Context mContext;
    private List<Story> mStoryList = new ArrayList<>();
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView storyImage;
        TextView storyName;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            storyImage = (ImageView) view.findViewById(R.id.diy_image);
            storyName = (TextView) view.findViewById(R.id.diy_name);
        }
    }

    public DIYAdapter(List<Story> storyList){

        mStoryList.clear();
        mStoryList = storyList;
    }

    @Override
    public DIYAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.diy_item,
                parent, false);
        final DIYAdapter.ViewHolder holder = new DIYAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Story story = mStoryList.get(position);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("image",story.getImage());
                intent.putExtra("url",story.getUrl());
                intent.putExtra("title",story.getName());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(DIYAdapter.ViewHolder holder, int position) {
        Story story = mStoryList.get(position);
        holder.storyName.setText(story.getName());
        Glide.with(mContext).load(story.getImage()).into(holder.storyImage);
    }


    @Override
    public int getItemCount() {
        return mStoryList.size();
    }
}
