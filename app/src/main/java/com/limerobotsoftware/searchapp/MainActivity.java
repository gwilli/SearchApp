package com.limerobotsoftware.searchapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;

import com.limerobotsoftware.searchapp.data.viewmodel.ProductViewModel;
import com.limerobotsoftware.searchapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_QUERY = "EXTRA_QUERY";

    private ProductViewModel viewModel;
    private ActivityMainBinding binding;
    private SearchView searchView;
    private String query;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query = savedInstanceState == null ? "" : savedInstanceState.getString(EXTRA_QUERY);
        viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getSupportActionBar().setTitle("");

        adapter = new ProductAdapter(this);
        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.searchRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setQuery(query, true);
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(calculateSearchMaxWidth(0));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            Runnable runnable;
            Handler handler = new Handler();

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                handler.removeCallbacks(runnable);
                doQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeCallbacks(runnable);
                runnable = () -> doQuery(newText);
                handler.postDelayed(runnable, 500);
                return true;
            }

            private void doQuery(String newText) {
                query = newText;
                if (!TextUtils.isEmpty(query)) {
                    viewModel.searchProduct(query);
                    if (!viewModel.getProducts().hasActiveObservers()) {
                        viewModel.getProducts().observe(MainActivity.this, products -> {
                            adapter.setProducts(products);
                            viewModel.getProducts().removeObservers(MainActivity.this);
                        });
                    }
                } else {
                    adapter.setProducts(new ArrayList<>());
                }
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_QUERY, query);
    }

    private int calculateSearchMaxWidth(int margindp) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels - Math.round(margindp * metrics.density);
    }
}
