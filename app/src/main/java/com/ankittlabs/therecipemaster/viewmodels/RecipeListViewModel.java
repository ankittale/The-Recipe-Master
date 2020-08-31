package com.ankittlabs.therecipemaster.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    private boolean performingQuery;
    private boolean viewRecipe;

    public RecipeListViewModel() {
        performingQuery = false;
        recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipeRepository.getRecipes();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        viewRecipe = true;
        recipeRepository.searchRecipeApi(query, pageNumber);
    }

    public void searchNextPage() {
        if (!performingQuery && viewRecipe) {
            recipeRepository.searchOnNextPage();
        }
    }

    public boolean isViewRecipe() {
        return viewRecipe;
    }

    public void setViewRecipe(boolean viewRecipe) {
        this.viewRecipe = viewRecipe;
    }

    public void setPerformingQuery(boolean isPerformingQuery) {
        performingQuery = isPerformingQuery;
    }

    public boolean isPerformingQuery() {
        return performingQuery;
    }

    public boolean onBackPressed() {
        if (performingQuery) {
            recipeRepository.cancelRequestMethod();
            performingQuery = false;
        }
        if (viewRecipe) {
            viewRecipe = false;
            return false;
        }
        return true;
    }
}
