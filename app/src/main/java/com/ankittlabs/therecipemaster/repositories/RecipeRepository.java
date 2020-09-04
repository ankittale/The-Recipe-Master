package com.ankittlabs.therecipemaster.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;
    private MutableLiveData<Boolean> mIsQueryExhausted=new MutableLiveData<Boolean>();
    private MediatorLiveData<List<Recipe>> mRecipes= new MediatorLiveData<List<Recipe>>();
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
        initMediators();
    }

    private void initMediators(){
        LiveData<List<Recipe>> recipeListAPISource= recipeApiClient.getRecipes();
        mRecipes.addSource(recipeListAPISource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes!=null){
                    mRecipes.setValue(recipes);
                    doneQuery(recipes);
                }else {
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Recipe> recipeList){
        if (recipeList!=null){
            if (recipeList.size()%30!=0){
                mIsQueryExhausted.setValue(true);
            }else {
                mIsQueryExhausted.setValue(false);
            }
        }
    }

    public LiveData<List<Recipe>> getRecipes() {
        //return recipeApiClient.getRecipes();
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe() {
        return recipeApiClient.getRecipe();
    }

    public void searchRecipeApi(String query, int pageNumber) {
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        recipeApiClient.searchRecipeApi(query, pageNumber);
    }

    public void searchOnNextPage() {
        searchRecipeApi(mQuery, mPageNumber + 1);
    }

    public void searchRecipeById(String recipeId){
        recipeApiClient.searchRecipeById(recipeId);
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }

    public void cancelRequestMethod() {
        recipeApiClient.cancelRequest();
    }

    public LiveData<Boolean> getTimeoutRequest() {
        return recipeApiClient.getTimeoutRequest();
    }
}
