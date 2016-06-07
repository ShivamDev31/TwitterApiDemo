package com.shivamdev.apidemo.activity;

import android.os.Bundle;

import com.shivamdev.apidemo.R;
import com.shivamdev.apidemo.restaurant.ListFragment;

public class MainActivity extends BaseActivity {

    // Constants
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FRAG_TAG = "tweet_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbar(getString(R.string.tweets), true);
        addTwitterFragment();
    }

    private void addTwitterFragment() {
        ListFragment listFragment = ListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.ll_tweets_fragment, listFragment, FRAG_TAG).commit();
    }
}