package com.ankittlabs.therecipemaster.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;
    private String mQuery;
    private int mPageNumber;

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

    public LiveData<Recipe> getRecipe() {
        return recipeApiClient.getRecipe();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        mQuery = query;
        mPageNumber = pageNumber;
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        recipeApiClient.searchRecipeApi(query, pageNumber);
    }

    public void searchOnNextPage() {
        searchRecipeApi(mQuery, mPageNumber + 1);
    }

    public void searchRecipeById(String recipeId){
        recipeApiClient.searchRecipebyId(recipeId);
    }

    public void cancelRequestMethod() {
        recipeApiClient.cancelRequest();
    }
}
