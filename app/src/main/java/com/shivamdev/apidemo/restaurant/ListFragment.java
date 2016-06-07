package com.shivamdev.apidemo.restaurant;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shivamdev.apidemo.R;
import com.shivamdev.apidemo.main.LogToast;
import com.shivamdev.apidemo.main.MainApplication;
import com.shivamdev.apidemo.network.TwitterApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.rx.ObservableFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shivamchopra on 06/06/16.
 */
public class ListFragment extends Fragment {

    private static final String TAG = ListFragment.class.getSimpleName();
    private static final String SEARCH = "q";
    private static final String COUNT = "count";
    private static final String SEARCH_NAME = "cleartax";
    private static final int COUNT_NO = 100;

    // Android stuff
    private ListAdapter adapter;
    private SwipeRefreshLayout refreshList;
    private ProgressBar pbLoader;
    private LinearLayout llListLayout;
    private LinearLayout llErrorLayout;
    private TextView tvCommonWords;
    private TextView tvLocation;
    private TextView tvErrorMessage;

    // Rxjava stuff
    private TwitterApi twitterApi;
    private CompositeSubscription compositeSubscription;

    private Location loc;
    private String address;

    private enum State {
        LOADING, ERROR, COMPLETED
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
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
        RecyclerView rvTweetsList = (RecyclerView) view.findViewById(R.id.rv_list);
        refreshList = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        refreshList.setColorSchemeColors(getActivity().getResources().getColor(R.color.aqua_blue));
        pbLoader = (ProgressBar) view.findViewById(R.id.pb_loader);
        llListLayout = (LinearLayout) view.findViewById(R.id.ll_layout);
        llErrorLayout = (LinearLayout) view.findViewById(R.id.ll_error);
        //tvCommonWords = (TextView) view.findViewById(R.id.tv_common_words);
        tvLocation = (TextView) view.findViewById(R.id.tv_location);
        tvErrorMessage = (TextView) view.findViewById(R.id.tv_error_message);
        adapter = new ListAdapter(getActivity());
        rvTweetsList.setAdapter(adapter);
        rvTweetsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshList.setOnRefreshListener(new RefreshData());

        loc = getUserLocation(getActivity());

        getTweets();
    }

    private void getTweets() {
        Map<String, Object> query = new HashMap<>();
        query.put(SEARCH, SEARCH_NAME);
        query.put(COUNT, COUNT_NO);

        changeStatus(State.LOADING);

        Subscription subs = twitterApi.getRestaurantData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<RestaurantData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogToast.log(TAG, "onError: " + e);
                        changeStatus(State.ERROR);
                    }

                    @Override
                    public void onNext(List<RestaurantData> twitterData) {
                        if (twitterData.size() == 0) {
                            tvErrorMessage.setText(R.string.no_tweets_found);
                            changeStatus(State.ERROR);
                        } else {
                            changeStatus(State.COMPLETED);
                            loc = getUserLocation(getActivity());
                            adapter.refreshList(twitterData, loc);
                        }
                    }
                });
        compositeSubscription.add(subs);
    }

    private class RefreshData implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refreshList.setRefreshing(true);
            getTweets();
        }
    }

    private void changeStatus(State state) {
        switch (state) {
            case LOADING:
                refreshList.setRefreshing(false);
                showError(false);
                dataLoading(true);
                break;
            case ERROR:
                showError(true);
                break;
            case COMPLETED:
                showError(false);
                dataLoading(false);
                break;
        }
    }

    private void dataLoading(boolean visible) {
        if (visible) {
            pbLoader.setVisibility(View.VISIBLE);
            llListLayout.setVisibility(View.GONE);
        } else {
            pbLoader.setVisibility(View.GONE);
            llListLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showError(boolean visible) {
        if (visible) {
            llErrorLayout.setVisibility(View.VISIBLE);
            llListLayout.setVisibility(View.GONE);
            pbLoader.setVisibility(View.GONE);
            tvCommonWords.setVisibility(View.GONE);
        } else {
            llErrorLayout.setVisibility(View.GONE);
            llListLayout.setVisibility(View.VISIBLE);
        }
    }

    private Location getUserLocation(Context context) {
        Observable<Location> locationObservable = ObservableFactory.from(SmartLocation.with(context).location());
        locationObservable.subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                loc = location;
                getAddressFromLocation(getActivity(), loc);
            }
        });
        return loc;
    }

    private String getAddressFromLocation(Context context, Location location) {
        SmartLocation.with(context).geocoding().reverse(location, new OnReverseGeocodingListener() {
            @Override
            public void onAddressResolved(Location location, List<Address> list) {
                address = list.get(0).getAddressLine(0);
                if (!TextUtils.isEmpty(address)) {
                    tvLocation.setText(address);
                } else {
                    tvLocation.setText(R.string.loc_not_avail);
                }
            }
        });
        return address;
    }


    @Override
    public void onStop() {
        super.onStop();
        SmartLocation.with(getActivity()).geocoding().stop();
        SmartLocation.with(getActivity()).location().stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}