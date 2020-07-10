/**
 *Copyright 2020 Mohamed Amgd
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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mohamedamgd.newsapp.R;
import com.mohamedamgd.newsapp.models.News;
import com.mohamedamgd.newsapp.viewModels.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    //View model
    private NewsViewModel mViewModel;

    private ArrayList<News> mNewsArrayList;
    private RecyclerView mRecyclerView;
    private NewsRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mPullToRefresh;

    //Empty view
    private View mEmptyView;
    private ImageView mEmptyViewImage;
    private TextView mEmptyViewTitle;
    private TextView mEmptyViewDescription;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        mEmptyView = getActivity().findViewById(R.id.empty_view);
        mEmptyViewImage = getActivity().findViewById(R.id.empty_view_image);
        mEmptyViewTitle = getActivity().findViewById(R.id.empty_view_title);
        mEmptyViewDescription = getActivity().findViewById(R.id.empty_view_description);
        mNewsArrayList = new ArrayList<>();

        // Initialize the recycler view
        initRecyclerView();

        // check if the main activity provided the bundle or not
        // this fragment cannot run without the provided data in the bundle
        if (getArguments() == null) return;

        // make the view model get the news data by using the data in bundle
        mViewModel.getNews(getArguments());

        // Initialize the swipe refresh layout and set its on refresh action
        mPullToRefresh = getActivity().findViewById(R.id.pull_to_refresh);
        mPullToRefresh.setColorSchemeResources(R.color.colorPrimary);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.getNews(getArguments());
            }

        });

        // observe the change in the news live data and update the recycler view with the new data
        mViewModel.mNewsMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> newsList) {
                mNewsArrayList.clear();
                mNewsArrayList.addAll(newsList);
                mAdapter.notifyDataSetChanged();
                mViewModel.checkState();
            }
        });

        // observe the change in the state and change the empty view to the suitable state
        mViewModel.mNewsDataState.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                switch (s) {
                    case "done":
                        mPullToRefresh.setRefreshing(false);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyView.setVisibility(View.GONE);
                        break;
                    case "loading":
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.GONE);
                        mPullToRefresh.setRefreshing(true);
                        break;
                    case "no internet":
                        mPullToRefresh.setRefreshing(false);
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        mEmptyViewImage.setImageResource(R.drawable.no_internet);
                        mEmptyViewTitle.setText(getString(R.string.no_internet_title));
                        mEmptyViewDescription.setText(getString(R.string.no_internet_description));
                        break;
                    case "nothing found":
                        mPullToRefresh.setRefreshing(false);
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        mEmptyViewImage.setImageResource(R.drawable.nothing_found);
                        mEmptyViewTitle.setText(getString(R.string.nothing_found_title));
                        if (getArguments().getString("type").equals("favorites")) {
                            mEmptyViewDescription.setText(getString(R.string.nothing_found_description_fav));
                        } else {
                            mEmptyViewDescription.setText(getString(R.string.nothing_found_description));
                        }
                        break;
                }
            }
        });
    }

    public void initRecyclerView() {
        mRecyclerView = getActivity().findViewById(R.id.news_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new NewsRecyclerAdapter(getContext(), mNewsArrayList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NewsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onNewsItemClick(int position) {
                // Find the current news that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri uri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the news URL
                Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);

                // Send the intent to launch a new activity
                startActivity(webIntent);
            }

            @Override
            public void onNewsItemLongClick(int position) {
                // Find the current news that was clicked on
                News currentNews = mAdapter.getItem(position);

                //Toast the title of this news
                Toast.makeText(getContext(), currentNews.getTitle(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStarClick(int position) {
                // Find the current news that was clicked on
                News currentNews = mAdapter.getItem(position);

                // notify the view model with the action
                mViewModel.onStarClicked(currentNews);

                // update the news list
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onShareClick(int position) {
                // Create a new intent of a send type
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                // set send data type
                sharingIntent.setType("text/plain");

                // Adding the url of the news to the send data
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        mAdapter.getItem(position).getUrl());

                // Send the intent to launch a new activity
                startActivity(Intent.createChooser(sharingIntent,
                        getString(R.string.share_intent_title)));
            }
        });
    }

}