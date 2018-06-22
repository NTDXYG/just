package com.example.just.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.just.Bean.Cate;
import com.example.just.R;
import java.util.List;

import static com.example.just.Avtivity.MainActivity.getList;
import static com.example.just.Avtivity.MainActivity.id;

/**
 * Created by Administrator on 2018/6/8.
 */

public class CateAdapter extends RecyclerView.Adapter<CateAdapter.ViewHolder> {

    private List<Cate> mCateList;
    public TextView preCateName;

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cateName;

        public ViewHolder(View itemView) {
            super(itemView);
            cateName = itemView.findViewById(R.id.cate_name);
            cateName.setTextColor(itemView.getResources().getColor(R.color.colorTextPrimary));
        }
    }

    public CateAdapter(List<Cate> cateList) {
        mCateList = cateList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cate_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preCateName != null){
                    preCateName.setTextColor(view.getResources().getColor(R.color.colorTextPrimary));
                }
                preCateName = viewHolder.cateName;
                int position = viewHolder.getAdapterPosition();
                Cate cate = mCateList.get(position);
                //切换颜色未做
                preCateName.setTextColor(view.getResources().getColor(R.color.colorPrimary));
                id = String.valueOf(position);
                getList(id);

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cate cate = mCateList.get(position);
        holder.cateName.setText(cate.getName());
    }

    @Override
    public int getItemCount() {
        return mCateList.size();
    }


}
