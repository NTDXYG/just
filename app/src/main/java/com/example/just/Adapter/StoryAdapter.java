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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.just.Avtivity.DetailActivity;
import com.example.just.Avtivity.MainActivity;
import com.example.just.Bean.Story;
import com.example.just.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.example.just.Avtivity.MainActivity.getList;
import static com.example.just.Avtivity.MainActivity.id;

/**
 * Created by Administrator on 2018/5/21.
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    private Context mContext;
    private List<Story> mStoryList = new ArrayList<>();
    public static int count;
    public static final int page = 15;



    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView storyImage;
        TextView storyName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            storyImage = (ImageView) view.findViewById(R.id.story_image);
            storyName = (TextView) view.findViewById(R.id.story_name);
        }
    }

    public StoryAdapter(List<Story> storyList) {
        if (count == 0) {
            mStoryList.clear();
            mStoryList = storyList;
            if (storyList.size() < page) {
                for (int i = page; i < storyList.size(); i++) {
                    mStoryList.remove(i);
                }
            }
        } else {
            mStoryList.clear();
            mStoryList = storyList;
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.story_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();

                if (position < getItemCount() - 1) {
                    Story story = mStoryList.get(position);
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("image", story.getImage());
                    intent.putExtra("url", story.getUrl());
                    intent.putExtra("title", story.getName());
                    mContext.startActivity(intent);
                } else {
                    if (count == 0) {
                        Toasty.success(view.getContext(), "Load more", Toast.LENGTH_SHORT,true).show();
                        count++;
                        getList(id);
                    } else {
                        Toasty.info(view.getContext(), "No more", Toast.LENGTH_SHORT,true).show();
                    }

                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position < getItemCount() - 1) {
            Story story = mStoryList.get(position);
            holder.storyName.setText(story.getName());
            Glide.with(mContext).load(story.getImage()).into(holder.storyImage);
        } else {
            if (count == 0) {
                holder.storyName.setText("加载更多");
                Glide.with(mContext).load("http://img95.699pic.com/photo/50062/6796.jpg_wh300.jpg").into(holder.storyImage);
            }else{
                holder.storyName.setText("到底啦！");
                Glide.with(mContext).load("http://img0.imgtn.bdimg.com/it/u=2572191445,1977281439&fm=27&gp=0.jpg").into(holder.storyImage);
            }
        }
    }



    @Override
    public int getItemCount() {
        if (count == 0) {
            if (page > mStoryList.size()) {
                return mStoryList.size() + 1;
            } else {
                return page + 1;
            }
        } else {
            return mStoryList.size() + 1;
        }
    }


}
