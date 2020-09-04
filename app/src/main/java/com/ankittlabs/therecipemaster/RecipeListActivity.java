package com.ankittlabs.therecipemaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankittlabs.therecipemaster.adapter.RecipeViewAdapter;
import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.utils.VerticalItemDecorator;
import com.ankittlabs.therecipemaster.viewmodels.RecipeListViewModel;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";
    RecyclerView recipeRecyclerView;
    private RecipeViewAdapter recipeViewAdapter;
    private RecipeListViewModel recipeListViewModel;
    private SearchView searchView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        recipeRecyclerView = findViewById(R.id.recycler_recipe);
        //View Model Setup
        recipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        subscribeObserver();
        initRecyclerView();
        initSearchView();

        if (!recipeListViewModel.isViewRecipe()) {
            displaySearchCategory();
        }
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
    }

    //subscribe the data
    private void subscribeObserver() {
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null) {
                    if (recipeListViewModel.isViewRecipe()) {
                        recipeListViewModel.setPerformingQuery(false); //Query is completed
                        recipeViewAdapter.setRecipes(recipes);
                    }
                }
            }
        });

        recipeListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Log.d(TAG, "onChanged: : Query Exhausted");
                }
            }
        });
    }

    private void initRecyclerView() {
        recipeViewAdapter = new RecipeViewAdapter(this);
        VerticalItemDecorator verticalItemDecorator = new VerticalItemDecorator(30);
        recipeRecyclerView.addItemDecoration(verticalItemDecorator);
        recipeRecyclerView.setAdapter(recipeViewAdapter);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    recipeListViewModel.searchNextPage();
                }
            }
        });
    }

    private void initSearchView() {
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recipeViewAdapter.displayLoading();
                recipeListViewModel.searchRecipeApi(query, 1);
                searchView.clearFocus();
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
        Intent intent = new Intent(RecipeListActivity.this, RecipeActivity.class);
        intent.putExtra("recipe", recipeViewAdapter.getRecipeDetails(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        recipeViewAdapter.displayLoading();
        recipeListViewModel.searchRecipeApi(category, 1);
        searchView.clearFocus();
    }

    private void displaySearchCategory() {
        recipeListViewModel.setViewRecipe(false);
        recipeViewAdapter.displaySearchCategory();
    }

    @Override
    public void onBackPressed() {
        if (recipeListViewModel.onBackPressed()) {
            super.onBackPressed();
        } else {
            displaySearchCategory();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_category) {
            displaySearchCategory();
        }
        return super.onOptionsItemSelected(item);
    }
}