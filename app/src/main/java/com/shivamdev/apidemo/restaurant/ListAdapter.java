package com.shivamdev.apidemo.restaurant;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shivamdev.apidemo.R;
import com.shivamdev.apidemo.main.CommonUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivamchopra on 06/06/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.DataHolder> {

    // Android Stuff
    private LayoutInflater inflater;
    private Context mContext;

    private List<RestaurantData> dataList;
    private Location userLocation;

    public ListAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        dataList = new ArrayList<>();
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        CommonUtils.loadImage(mContext, dataList.get(position).logoUrl,
                R.drawable.ic_rest, holder.ivImage);
        holder.tvName.setText(dataList.get(position).restaurantName);
        holder.tvAddress.setText(dataList.get(position).address);

        Location restLoc = new Location("rest");
        restLoc.setLatitude(Double.parseDouble(dataList.get(position).lat));
        restLoc.setLongitude(Double.parseDouble(dataList.get(position).lng));
        float distInMeters = 0;
        if (userLocation != null) {
            distInMeters = restLoc.distanceTo(userLocation);
            DecimalFormat df = new DecimalFormat("0.00##");
            String result = df.format(distInMeters);
            holder.tvDistance.setText(String.format(mContext.getString(R.string.distance), result));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void refreshList(List<RestaurantData> data, Location userLocation) {
        dataList.clear();
        this.dataList.addAll(data);
        this.userLocation = userLocation;
        notifyDataSetChanged();
    }

    public class DataHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvAddress;
        private TextView tvDistance;

        public DataHolder(View view) {
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.iv_image);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvAddress = (TextView) view.findViewById(R.id.tv_address);
            tvDistance = (TextView) view.findViewById(R.id.tv_distance);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
