package com.shivamdev.twitterapidemo.activity;

import android.os.Bundle;

import com.shivamdev.twitterapidemo.R;
import com.shivamdev.twitterapidemo.twitter.TwitterFragment;

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
        TwitterFragment twitterFragment = TwitterFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.ll_tweets_fragment, twitterFragment, FRAG_TAG).commit();
    }
}