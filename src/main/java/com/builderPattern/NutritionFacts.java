package com.builderPattern;

/**
 * Created by Administrator on 2017/7/14/014.
 */
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final int sodium;
    private final int carbohydrate;

    public static class Builder{
        //必要的参数
        private final int servingSize;
        private final int servings;

        //可选参数
        private int calories = 0;
        private int fat = 0;
        private int carbohydrate = 0;
        private int sodium = 0;

        public Builder(int servingSize,int servings){
            this.servingSize = servingSize;
            this.servings=servings;
        }

        public Builder withCalories(int val){
            calories = val;
            return this;
        }

        public Builder withCarbohydrate(int val){
            carbohydrate = val;
            return this;
        }

        public Builder withSodium(int val){
            sodium = val;
            return this;
        }

        public NutritionFacts build(){
            return new NutritionFacts(this);
        }


    }

    private NutritionFacts(Builder builder){
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
        fat = builder.fat;
        sodium = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }



}
