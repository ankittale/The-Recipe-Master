package com.ankittlabs.therecipemaster;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.ankittlabs.therecipemaster.model.Recipe;

public class RecipeActivity extends BaseActivity {

    private AppCompatImageView mRecipeImage;
    private TextView mRecipeTitle, mRecipeScore;
    private LinearLayout mRecipeIngredientContainer;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mRecipeImage=findViewById(R.id.recipe_image);
        mRecipeTitle=findViewById(R.id.recipe_title);
        mRecipeScore=findViewById(R.id.recipe_social_score);
        mRecipeIngredientContainer=findViewById(R.id.ingredients_container);
        mScrollView=findViewById(R.id.parent);
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if (getIntent().hasExtra("recipe")){
            Recipe recipe=getIntent().getParcelableExtra("recipe");
        }
    }
}
