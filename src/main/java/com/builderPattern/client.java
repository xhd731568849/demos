package com.builderPattern;

/**
 * Created by Administrator on 2017/7/14/014.
 */
public class client {
    public static void main(String[] args) {
        NutritionFacts nutritionFacts = new NutritionFacts.
                Builder(240,8).withCalories(1).withCarbohydrate(27).build();
    }
}
