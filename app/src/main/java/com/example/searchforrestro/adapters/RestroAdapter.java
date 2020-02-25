package com.example.searchforrestro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.searchforrestro.MainActivity;
import com.example.searchforrestro.R;
import com.example.searchforrestro.models.Restaurant_;


import java.util.List;

public class RestroAdapter extends RecyclerView.Adapter<RestroAdapter.RestroViewHolder> {

    private Context context;
    private List<Restaurant_> restaurants;

    public RestroAdapter(Context context, List<Restaurant_> restaurants) {
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

        Glide.with(context).load(
                restaurants.get(position).getPhotosUrl()
        ).into(holder.restroImg);
        holder.name.setText(restaurants.get(position).getName());
        holder.cuisines.setText(restaurants.get(position).getCuisines());
        holder.ratings.setText(restaurants.get(position).getUserRating().getAggregateRating());
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    class RestroViewHolder extends RecyclerView.ViewHolder {

        ImageView restroImg;
        TextView name, cuisines,ratings;

        public RestroViewHolder(@NonNull View itemView) {
            super(itemView);
            restroImg = itemView.findViewById(R.id.resto_photo);
            name = itemView.findViewById(R.id.restro_name);
            cuisines = itemView.findViewById(R.id.restro_cusines);
            ratings = itemView.findViewById(R.id.restro_ratings);
        }
    }
}
