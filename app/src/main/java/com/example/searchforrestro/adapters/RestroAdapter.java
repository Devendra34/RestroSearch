package com.example.searchforrestro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.searchforrestro.MainActivity;
import com.example.searchforrestro.R;
import com.example.searchforrestro.models.Restaurant;
import com.example.searchforrestro.models.Restaurant_;
import com.example.searchforrestro.repository.ZomatoApi;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestroAdapter extends RecyclerView.Adapter<RestroAdapter.RestroViewHolder> {

    private static final String TAG = "debug";

    private Context context;
    private List<Restaurant> restaurants;

    public RestroAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.resto_card_view;
    }

    @NonNull
    @Override
    public RestroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestroViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestroViewHolder holder, int position) {

        Restaurant_ restaurant = restaurants.get(position).getRestaurant();
        if (restaurant == null) {
            return;
        }
        if(restaurant.getPhotos() != null && restaurant.getPhotos().size()>0) {
            Glide.with(context)
                    .load(restaurant.getPhotos().get(0).getPhoto().getUrl())
                    .error(R.drawable.ic_restro)
                    .placeholder(R.drawable.ic_restro)
                    .centerCrop()
                    .into(holder.restroImg);
        }
        holder.name.setText(restaurant.getName());
        holder.cuisines.setText(restaurant.getCuisines());
        String ratingsText = "Avg. Ratings: " + restaurant.getUserRating().getAggregateRating();
        holder.ratings.setText(ratingsText);
//        final int res_id = restaurant.getR().getResId();
//        holder.root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Call<Restaurant_> restaurant_call = ZomatoApi.getService().queryRestaurant(res_id);
//
//                restaurant_call.enqueue(new Callback<Restaurant_>() {
//                    @Override
//                    public void onResponse(Call<Restaurant_> call, Response<Restaurant_> response) {
//                        if(!response.isSuccessful()) {
//                            Log.d(TAG, "onResponse: unsuccessful restro");
//                        } else {
//                            Restaurant_ r = response.body();
//                            Log.d(TAG, "\nonResponse: Res name = "+r.getName());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Restaurant_> call, Throwable t) {
//
//                    }
//                });
//            }
//        });
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    static class RestroViewHolder extends RecyclerView.ViewHolder {

        ImageView restroImg;
        TextView name, cuisines,ratings;
        LinearLayout root;

        RestroViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            restroImg = itemView.findViewById(R.id.resto_photo);
            name = itemView.findViewById(R.id.restro_name);
            cuisines = itemView.findViewById(R.id.restro_cusines);
            ratings = itemView.findViewById(R.id.restro_ratings);
        }
    }

}
