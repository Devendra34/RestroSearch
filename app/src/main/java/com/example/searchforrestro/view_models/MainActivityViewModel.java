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
    private MutableLiveData<List<Restaurant_>> resroLiveData;
    private MutableLiveData<String> resultsFound;

    public MainActivityViewModel() {
        resroLiveData = new MutableLiveData<>();
        resultsFound = new MutableLiveData<>();
    }

    public LiveData<List<Restaurant_>> getResroLiveData() {
        return resroLiveData;
    }

    public LiveData<String> getResultsFound() {
        return resultsFound;
    }

    public void queryRestaurants(String query, int startFrom) {
        Call<Example> exampleCall = ZomatoApi.getService().queryData();

        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: unsuccessful");
                    return;
                }
//                Example example = response.body();
//                resultsFound.setValue(String.valueOf(example.getResultsFound()));
//
//                List<Restaurant_> restaurants = new ArrayList<>();
//                for (Restaurant restaurant1 : example.getRestaurants()) {
//                    if(restaurant1.getRestaurant() != null){
//                        restaurants.add(restaurant1.getRestaurant());
//                    }
//                }
//                resroLiveData.setValue(restaurants);

                Log.d(TAG, "onResponse: Successful\n");
                Example example = response.body();

                StringBuilder s = new StringBuilder();
                s.append("Results found = ").append(example.getResultsFound());
                s.append("Results start = ").append(example.getResultsStart());
                s.append("Results shown = ").append(example.getResultsShown());
                s.append("No. of Restaurants = ").append(example.getRestaurants().size());
                s.append("\n\n");

                for (Restaurant restaurant1 : example.getRestaurants()) {

                    Restaurant_ restaurant = restaurant1.getRestaurant();

                    if(restaurant == null) {
                        Log.d(TAG, "onResponse: null restaurnat");
                        continue;
                    }

                    s.append("res_id = ").append(restaurant.getId()).append("\n");
                    s.append("res_name = ").append(restaurant.getName()).append("\n");
                    s.append("res_currency = ").append(restaurant.getCurrency()).append("\n");
                    s.append("res_price range = ").append(restaurant.getPriceRange()).append("\n");
                    s.append("res_cuisines = ").append(restaurant.getCuisines()).append("\n");
                    s.append("res_phone numbers = ").append(restaurant.getPhoneNumbers()).append("\n");
//                        s.append("Photos:").append("\n");
//                        for (com.example.firstflickerapp.model.search_model.Photo photo: restaurant.getPhotos()) {
//                            s.append(photo.getPhoto().getUrl()).append("\n");
//                        }
                    s.append("res_ratings = ").append(restaurant
                            .getUserRating().getAggregateRating());
                }


                Log.d(TAG, "onResponse: \n"+s.toString());

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

                Log.d(TAG, "onFailure: "+ t.toString());
            }
        });
    }
}
