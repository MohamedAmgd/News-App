/**
 * Copyright 2020 Mohamed Amgd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.mohamedamgd.newsapp.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mohamedamgd.newsapp.R;
import com.mohamedamgd.newsapp.models.News;

import java.util.ArrayList;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private ArrayList<News> mNewsList;
    private Context mContext;
    private OnItemClickListener mListener;

    public NewsRecyclerAdapter(Context context, ArrayList<News> newsList) {
        mContext = context;
        mNewsList = newsList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        return new NewsViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News currentItem = mNewsList.get(position);

        // Title
        holder.mTitleText.setText(currentItem.getTitle());

        //Clear news image for recycling
        holder.mNewsImage.setImageResource(0);

        //news image
        Glide.with(mContext)
                .load(currentItem.getUrlToImage())
                .centerCrop()
                .override(1024, 768)
                .dontAnimate()
                .thumbnail(Glide.with(mContext).load(R.drawable.circle_loading).override(1024, 768))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.mNewsImage.setImageResource(R.drawable.error_image);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.mNewsImage);


        // star image
        if (currentItem.isStared()) {
            holder.mStarImage.setImageResource(R.drawable.ic_star_yellow_24dp);
        } else {
            holder.mStarImage.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public News getItem(int position) {
        return mNewsList.get(position);
    }

    public interface OnItemClickListener {
        void onNewsItemClick(int position);

        void onNewsItemLongClick(int position);

        void onStarClick(int position);

        void onShareClick(int position);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView mNewsImage;
        public TextView mTitleText;
        public ImageView mStarImage;
        public ImageView mShareImage;

        public NewsViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mNewsImage = itemView.findViewById(R.id.image);
            mTitleText = itemView.findViewById(R.id.title);
            mStarImage = itemView.findViewById(R.id.star);
            mShareImage = itemView.findViewById(R.id.share);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onNewsItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onNewsItemLongClick(position);
                        }
                    }
                    return false;
                }
            });

            mStarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onStarClick(position);
                        }
                    }
                }
            });

            mShareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onShareClick(position);
                        }
                    }
                }
            });
        }
    }

}
