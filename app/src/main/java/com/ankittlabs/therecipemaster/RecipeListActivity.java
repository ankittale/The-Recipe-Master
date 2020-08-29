package com.ankittlabs.therecipemaster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.utils.Testing;
import com.ankittlabs.therecipemaster.viewmodels.RecipeViewModel;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private static final String TAG = "RecipeListActivity";
    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        //View Model Setup
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testRetrofit();
            }
        });
        subscribeObserver();
    }

    //subscribe the data
    private void subscribeObserver() {
        recipeViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null) {
                    Testing.printRecipe(recipes, "recipes");
                }
            }
        });
    }

    private void searchRecipeApi(String query, int pageNumber) {
        recipeViewModel.searchRecipeApi(query, pageNumber);
    }

    private void testRetrofit() {
        searchRecipeApi("chicken", 1);
    }
}