package com.ankittlabs.therecipemaster.requests;

import com.ankittlabs.therecipemaster.requests.responses.RecipeResponse;
import com.ankittlabs.therecipemaster.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("q") String query,
            @Query("page") String page
    );

    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("rId") String recipeId
    );
}
