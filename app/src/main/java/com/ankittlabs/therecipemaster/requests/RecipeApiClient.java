package com.ankittlabs.therecipemaster.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ankittlabs.therecipemaster.AppExecutors;
import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.requests.responses.RecipeResponse;
import com.ankittlabs.therecipemaster.requests.responses.RecipeSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.ankittlabs.therecipemaster.utils.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";
    private RetrieveRecipeRunnable retrieveRecipeRunnable;
    private RetrieveSingleRecipeRunnable retrieveSingleRecipeRunnable;

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private MutableLiveData<Recipe> mRecipe;
    private MutableLiveData<Boolean> timeoutRequest;

    public static RecipeApiClient getInstance() {
        if (instance == null) {
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
        timeoutRequest = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipe;
    }

    public LiveData<Boolean> getTimeoutRequest() {
        return timeoutRequest;
    }

    public void searchRecipeApi(String query, int pageNumber) {
        if (retrieveRecipeRunnable != null) {
            retrieveRecipeRunnable = null;
        }
        retrieveRecipeRunnable = new RetrieveRecipeRunnable(query, pageNumber);
        final Future handler = AppExecutors.getInstance().networkIO().submit(retrieveRecipeRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Let user know about timeout occurs
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchRecipeById(String recipeID) {
        if (retrieveSingleRecipeRunnable != null) {
            retrieveSingleRecipeRunnable = null;
        }
        retrieveSingleRecipeRunnable = new RetrieveSingleRecipeRunnable(recipeID);
        final Future handler = AppExecutors.getInstance().networkIO().submit(retrieveSingleRecipeRunnable);
        timeoutRequest.setValue(false);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Let user know about timeout occurs
                timeoutRequest.postValue(true);
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipeRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveRecipeRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());
                    if (pageNumber == 1) {
                        mRecipes.postValue(list);
                    } else {
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                } else {
                    String errorString = response.errorBody().toString();
                    Log.e(TAG, "run: " + errorString);
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber) {
            return ServiceGenerator.getRecipeApi().searchRecipe(query, String.valueOf(pageNumber));
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: Cancel Recipe Request");
            cancelRequest = true;
        }
    }

    private class RetrieveSingleRecipeRunnable implements Runnable {

        private String recipeId;
        boolean cancelRequest;

        public RetrieveSingleRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipeBasedID(recipeId).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    Recipe recipe = ((RecipeResponse) response.body()).getRecipe();
                    mRecipe.postValue(recipe);
                } else {
                    String errorString = response.errorBody().toString();
                    Log.e(TAG, "run: " + errorString);
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeResponse> getRecipeBasedID(String recipeId) {
            return ServiceGenerator.getRecipeApi().getRecipe(recipeId);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: Cancel Recipe Request");
            cancelRequest = true;
        }
    }

    public void cancelRequest() {
        if (retrieveRecipeRunnable != null) {
            retrieveRecipeRunnable.cancelRequest();
        }
        if (retrieveSingleRecipeRunnable != null) {
            retrieveSingleRecipeRunnable.cancelRequest();
        }
    }
}
