package com.ankittlabs.therecipemaster.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository recipeRepository;
    private boolean mDidRetrieveRecipe;
    private String recipeId;

    public RecipeViewModel() {
        mDidRetrieveRecipe=false;
        recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe(){
        return recipeRepository.getRecipe();
    }

    public void searchRecipeById(String recipeId){
        this.recipeId=recipeId;
        recipeRepository.searchRecipeById(recipeId);
    }

    public String getRecipeId() {
        return recipeId;
    }

    public LiveData<Boolean> getTimeoutRequest() {
        return recipeRepository.getTimeoutRequest();
    }

    public void setRetrieveRecipe(boolean retrieveRecipe){
        mDidRetrieveRecipe=retrieveRecipe;
    }

    public boolean didRetrievedRecipe(){
        return mDidRetrieveRecipe;
    }
}
