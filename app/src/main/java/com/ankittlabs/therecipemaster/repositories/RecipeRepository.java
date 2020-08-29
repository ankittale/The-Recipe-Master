package com.ankittlabs.therecipemaster.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository() {
        recipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipeApiClient.getRecipes();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        recipeApiClient.searchRecipeApi(query, pageNumber);
    }
}
