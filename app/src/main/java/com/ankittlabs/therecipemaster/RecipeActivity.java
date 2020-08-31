package com.ankittlabs.therecipemaster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ankittlabs.therecipemaster.model.Recipe;
import com.ankittlabs.therecipemaster.viewmodels.RecipeViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class RecipeActivity extends BaseActivity {

    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeScore;
    private LinearLayout mRecipeIngredientContainer;
    private ScrollView mScrollView;

    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeTitle = findViewById(R.id.recipe_title);
        mRecipeScore = findViewById(R.id.recipe_social_score);
        mRecipeIngredientContainer = findViewById(R.id.ingredients_container);
        mScrollView = findViewById(R.id.parent);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        subscribeObserver();
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            recipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }

    private void subscribeObserver() {
        recipeViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe != null) {
                    if (recipe.getRecipe_id().equals(recipeViewModel.getRecipeId())) {
                        setRecipeProperties(recipe);
                        recipeViewModel.setRetrieveRecipe(true);
                    }
                }
            }
        });

        recipeViewModel.getTimeoutRequest().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean && !recipeViewModel.didRetrievedRecipe()){
                    Log.d("TAG", "onChanged: Timed  out");
                }
            }
        });
    }

    private void setRecipeProperties(Recipe recipe) {
        if (recipe != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImage_url())
                    .into(mRecipeImage);

            mRecipeTitle.setText(recipe.getTitle());
            mRecipeScore.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
            mRecipeIngredientContainer.removeAllViews();
            for (String ingredients : recipe.getIngredients()) {
                TextView textView = new TextView(this);
                textView.setText(ingredients);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mRecipeIngredientContainer.addView(textView);
            }
        }
        showParentLayout();
        showProgressBar(false);
    }

    private void showParentLayout() {
        mScrollView.setVisibility(View.VISIBLE);
    }
}
