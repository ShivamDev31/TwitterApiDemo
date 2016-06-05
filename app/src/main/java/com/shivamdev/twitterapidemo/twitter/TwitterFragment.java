package com.shivamdev.twitterapidemo.twitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shivamdev.twitterapidemo.R;
import com.shivamdev.twitterapidemo.main.LogToast;
import com.shivamdev.twitterapidemo.main.MainApplication;
import com.shivamdev.twitterapidemo.network.TwitterApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shivamchopra on 04/06/16.
 */
public class TwitterFragment extends Fragment {

    private static final String TAG = TwitterFragment.class.getSimpleName();
    private static final String SEARCH = "q";
    private static final String COUNT = "count";
    private static final String SEARCH_NAME = "cleartax";
    private static final int COUNT_NO = 100;

    // Android stuff
    private TwitterAdapter adapter;
    private SwipeRefreshLayout refreshTweets;
    private ProgressBar pbLoader;
    private LinearLayout llTweetsLayout;
    private LinearLayout llErrorLayout;
    private TextView tvCommonWords;
    private TextView tvErrorMessage;

    // Rxjava stuff
    private TwitterApi twitterApi;
    private CompositeSubscription compositeSubscription;

    private enum State {
        LOADING, ERROR, COMPLETED
    }

    public static TwitterFragment newInstance() {
        TwitterFragment fragment = new TwitterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterApi = MainApplication.getInstance().component().getTwitterApi();
        compositeSubscription = new CompositeSubscription();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvTweetsList = (RecyclerView) view.findViewById(R.id.rv_tweets_list);
        refreshTweets = (SwipeRefreshLayout) view.findViewById(R.id.srl_refrest_tweets);
        refreshTweets.setColorSchemeColors(getActivity().getResources().getColor(R.color.aqua_blue));
        pbLoader = (ProgressBar) view.findViewById(R.id.pb_tweets_loader);
        llTweetsLayout = (LinearLayout) view.findViewById(R.id.ll_tweets_layout);
        llErrorLayout = (LinearLayout) view.findViewById(R.id.ll_error_twitter);
        tvCommonWords = (TextView) view.findViewById(R.id.tv_common_words);
        tvErrorMessage = (TextView) view.findViewById(R.id.tv_error_message);
        adapter = new TwitterAdapter(getActivity());
        rvTweetsList.setAdapter(adapter);
        rvTweetsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshTweets.setOnRefreshListener(new RefreshTweets());
        getTweets();
    }

    private void getTweets() {
        Map<String, Object> query = new HashMap<>();
        query.put(SEARCH, SEARCH_NAME);
        query.put(COUNT, COUNT_NO);

        changeStatus(State.LOADING);

        Subscription subs = twitterApi.getTweetsData(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TwitterData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogToast.log(TAG, "onError: " + e);
                        changeStatus(State.ERROR);
                    }

                    @Override
                    public void onNext(TwitterData twitterData) {
                        if (twitterData.statuses.size() == 0) {
                            tvErrorMessage.setText(R.string.no_tweets_found);
                            changeStatus(State.ERROR);
                        } else {

                            StringBuilder stringBuilder = new StringBuilder();

                            for (int i = 0; i < twitterData.statuses.size(); i++) {
                                stringBuilder.append(twitterData.statuses.get(i).tweetText);
                            }
                            tvCommonWords.setText(String.format(getString(R.string.three_common_words),
                                    getThreeCommonWords(getWordsCount(stringBuilder.toString()))));

                            changeStatus(State.COMPLETED);
                            adapter.refreshList(twitterData.statuses);
                        }
                    }
                });
        compositeSubscription.add(subs);
    }

    private class RefreshTweets implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refreshTweets.setRefreshing(true);
            getTweets();
        }
    }

    private void changeStatus(State state) {
        switch (state) {
            case LOADING:
                refreshTweets.setRefreshing(false);
                showError(false);
                tweetsLoading(true);
                break;
            case ERROR:
                showError(true);
                break;
            case COMPLETED:
                showError(false);
                tweetsLoading(false);
                break;
        }
    }

    private void tweetsLoading(boolean visible) {
        if (visible) {
            pbLoader.setVisibility(View.VISIBLE);
            llTweetsLayout.setVisibility(View.GONE);
        } else {
            pbLoader.setVisibility(View.GONE);
            llTweetsLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showError(boolean visible) {
        if (visible) {
            llErrorLayout.setVisibility(View.VISIBLE);
            llTweetsLayout.setVisibility(View.GONE);
            pbLoader.setVisibility(View.GONE);
            tvCommonWords.setVisibility(View.GONE);
        } else {
            llErrorLayout.setVisibility(View.GONE);
            llTweetsLayout.setVisibility(View.VISIBLE);
        }
    }

    private Map<String, Integer> getWordsCount(String text) {
        Map<String, Integer> wordMap = new HashMap<>();
        String[] str = text.toString().split(" ");
        for (int i = 0; i < str.length; i++) {
            if (wordMap.containsKey(str[i])) {
                wordMap.put(str[i], wordMap.get(str[i]) + 1);
            } else {
                wordMap.put(str[i], 1);
            }
        }
        return wordMap;
    }

    private String getThreeCommonWords(Map<String, Integer> wordMap) {
        StringBuilder commonBuilder = new StringBuilder();
        Set<Map.Entry<String, Integer>> set = wordMap.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<>(set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        for (int i = 0; i < 3; i++) {
            commonBuilder.append(list.get(i).getKey() + ", ");
        }

        String common = commonBuilder.toString();
        return common.substring(0, common.length() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}