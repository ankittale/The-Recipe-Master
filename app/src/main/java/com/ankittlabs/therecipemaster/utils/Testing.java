package com.ankittlabs.therecipemaster.utils;

import android.util.Log;

import com.ankittlabs.therecipemaster.model.Recipe;

import java.util.List;

public class Testing {

    private static final String TAG = "Testing";

    public static void printRecipe(List<Recipe> recipes, String tag) {
        for (Recipe recipe : recipes) {
            Log.d(TAG, "onChanged: " + recipe.getTitle());
        }
    }
}
