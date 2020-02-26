package com.example.searchforrestro.view_models;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.searchforrestro.models.Example;
import com.example.searchforrestro.models.Restaurant;
import com.example.searchforrestro.models.Restaurant_;
import com.example.searchforrestro.repository.ZomatoApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = "debug";
    private MutableLiveData<List<Restaurant>> resroLiveData;
    private MutableLiveData<Integer> resultsFoundLiveData;
    private MutableLiveData<Integer> startFromLiveData;

    public MainActivityViewModel() {
        resroLiveData = new MutableLiveData<>();
        resultsFoundLiveData = new MutableLiveData<>();
        startFromLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Restaurant>> getResroLiveData() {
        return resroLiveData;
    }

    public LiveData<Integer> getResultsFound() {
        return resultsFoundLiveData;
    }

    public MutableLiveData<Integer> getStartFrom() {
        return startFromLiveData;
    }

    public void queryRestaurants(String query, boolean loadMore) {

        int startFrom;
        if (loadMore) {
            startFrom = startFromLiveData.getValue() + 20;
        } else {
            startFrom = 0;
        }
        Call<Example> exampleCall = ZomatoApi.getService().queryData(query, startFrom, 3);

        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: unsuccessful");
                    return;
                }
                Example example = response.body();
                List<Restaurant> restaurants;
                if (loadMore) {
                    restaurants = new ArrayList<>(resroLiveData.getValue());
                    restaurants.addAll(example.getRestaurants());
                } else {
                    resultsFoundLiveData.setValue(example.getResultsFound());
                    restaurants = example.getRestaurants();
                }

                resroLiveData.setValue(restaurants);
                startFromLiveData.setValue(example.getResultsStart());
                Log.d(TAG, "onResponse: Successful\n");
//                Example example = response.body();
//
                StringBuilder s = new StringBuilder();
                s.append("Results found = ").append(example.getResultsFound());
                s.append("Results start = ").append(example.getResultsStart());
                s.append("Results shown = ").append(example.getResultsShown());
                s.append("No. of Restaurants = ").append(example.getRestaurants().size());
                s.append("\n\n");

//                for (Restaurant restaurant1 : example.getRestaurants()) {
//
//                    Restaurant_ restaurant = restaurant1.getRestaurant();
//
//                    if (restaurant == null) {
//                        Log.d(TAG, "onResponse: null restaurnat");
//                        continue;
//                    }
//
//                    s.append("res_id = ").append(restaurant.getId()).append("\n");
//                    s.append("res_name = ").append(restaurant.getName()).append("\n");
//                    s.append("res_currency = ").append(restaurant.getCurrency()).append("\n");
//                    s.append("res_price range = ").append(restaurant.getPriceRange()).append("\n");
//                    s.append("res_cuisines = ").append(restaurant.getCuisines()).append("\n");
//                    s.append("res_phone numbers = ").append(restaurant.getPhoneNumbers()).append("\n");
////                        s.append("Photos:").append("\n");
////                        for (com.example.firstflickerapp.model.search_model.Photo photo: restaurant.getPhotos()) {
////                            s.append(photo.getPhoto().getUrl()).append("\n");
////                        }
//                    s.append("res_ratings = ").append(restaurant
//                            .getUserRating().getAggregateRating());
//                }


                Log.d(TAG, "onResponse: \n" + s.toString());

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }
}
