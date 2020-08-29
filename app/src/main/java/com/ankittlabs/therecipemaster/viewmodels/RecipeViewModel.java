package com.ankittlabs.therecipemaster.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.repositories.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository recipeRepository;

    public RecipeViewModel() {
        recipeRepository= RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipeRepository.getRecipes();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        recipeRepository.searchRecipeApi(query, pageNumber);
    }
}
