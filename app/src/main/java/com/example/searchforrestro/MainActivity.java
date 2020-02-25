package com.example.searchforrestro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.searchforrestro.adapters.RestroAdapter;
import com.example.searchforrestro.models.Restaurant_;
import com.example.searchforrestro.view_models.MainActivityViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MainActivityViewModel viewModel;
    private SearchView searchView;
    private TextView resultsFoundTV;

    private RestroAdapter restroAdapter;
    private boolean isRecyclerViewInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tools);
        recyclerView = findViewById(R.id.rv);
        resultsFoundTV = findViewById(R.id.results_found_tv);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        recyclerView.setVisibility(View.GONE);
        setSupportActionBar(toolbar);

//        viewModel.queryRestaurants("",0);
        viewModel.getResultsFound().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s == null || "".equals(s)) {
                    resultsFoundTV.setVisibility(View.GONE);
                    return;
                }
                resultsFoundTV.setVisibility(View.VISIBLE);
                resultsFoundTV.setText(s);
            }
        });

        viewModel.getResroLiveData().observe(this, new Observer<List<Restaurant_>>() {
            @Override
            public void onChanged(List<Restaurant_> restaurant_s) {
                if (!isRecyclerViewInit) {
                    initRecyclerView();
                } else {
                    restroAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.VISIBLE);
        restroAdapter = new RestroAdapter(this,viewModel.getResroLiveData().getValue());
        recyclerView.setAdapter(restroAdapter);
        isRecyclerViewInit = true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText == null) {
                    newText = "";
                }
                viewModel.queryRestaurants(newText,0);
                return true;
            }
        });
        return true;
    }
}
