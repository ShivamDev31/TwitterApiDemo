package com.shivamdev.twitterapidemo.twitter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivamdev.twitterapidemo.R;
import com.shivamdev.twitterapidemo.main.CommonUtils;
import com.shivamdev.twitterapidemo.main.LogToast;
import com.shivamdev.twitterapidemo.twitter.TwitterData.Statuses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivamchopra on 04/06/16.
 */
public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.TweetsHolder> {
    // Constants
    private static final String TAG = TwitterAdapter.class.getSimpleName();

    // Android Stuff
    private LayoutInflater inflater;
    private Context mContext;

    private List<Statuses> tweetsList;

    public TwitterAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        tweetsList = new ArrayList<>();
    }

    @Override
    public TweetsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tweet_item, parent, false);
        return new TweetsHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetsHolder holder, int position) {
        CommonUtils.loadImage(mContext, tweetsList.get(position).user.userImageUrl,
                R.drawable.twitter_logo, holder.ivUserImage);
        holder.tvTweet.setText(tweetsList.get(position).tweetText);
        holder.tvUserName.setText(tweetsList.get(position).user.userName);
        if (!tweetsList.get(position).user.userLocation.contains("#")) {
            holder.tvUserLocation.setText(tweetsList.get(position).user.userLocation);
        } else {
            holder.tvUserLocation.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return tweetsList.size();
    }

    public void refreshList(List<Statuses> tweets) {
        tweetsList.clear();
        this.tweetsList.addAll(tweets);
        notifyDataSetChanged();
    }

    public class TweetsHolder extends RecyclerView.ViewHolder {
        private ImageView ivUserImage;
        private TextView tvTweet;
        private TextView tvUserName;
        private TextView tvUserLocation;


        public TweetsHolder(View view) {
            super(view);
            ivUserImage = (ImageView) view.findViewById(R.id.iv_user_image);
            tvTweet = (TextView) view.findViewById(R.id.tv_tweet);
            tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
            tvUserLocation = (TextView) view.findViewById(R.id.tv_user_location);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogToast.toast(mContext, tweetsList.get(getLayoutPosition()).createdAt.replace("+0000", ""));
                }
            });
        }
    }
}
