package com.ankittlabs.therecipemaster;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankittlabs.therecipemaster.adapter.RecipeViewAdapter;
import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.viewmodels.RecipeViewModel;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";
    RecyclerView recipeRecyclerView;
    private RecipeViewAdapter recipeViewAdapter;
    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeRecyclerView = findViewById(R.id.recycler_recipe);
        //View Model Setup
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        subscribeObserver();
        initRecyclerView();
        initSearchView();
    }

    //subscribe the data
    private void subscribeObserver() {
        recipeViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null) {
                    recipeViewAdapter.setRecipes(recipes);
                }
            }
        });
    }

    private void initRecyclerView() {
        recipeViewAdapter = new RecipeViewAdapter(this);
        recipeRecyclerView.setAdapter(recipeViewAdapter);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void searchRecipeApi(String query, int pageNumber) {
        recipeViewModel.searchRecipeApi(query, pageNumber);
    }

    private void initSearchView() {
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recipeViewAdapter.displayLoading();
                recipeViewModel.searchRecipeApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(int position) {

    }
}