package com.example.examplemod.recipe;

import com.example.examplemod.init.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MuffleFurnaceRecipes
{
    private static final List<MuffleFurnaceRecipe> RECIPES =
            new ArrayList<MuffleFurnaceRecipe>();

    public static void registerRecipes()
    {
        RECIPES.clear();

        addRecipe(
                new ItemStack(ModItems.RAW_IRON),
                new ItemStack(Items.COAL, 1, 0),
                new ItemStack(Items.IRON_INGOT),
                300
        );

        addRecipe(
                new ItemStack(ModItems.RAW_COPPER),
                new ItemStack(Items.COAL, 1, 0),
                new ItemStack(ModItems.COPPER_INGOT),
                200
        );
    }

    public static void addRecipe(
            ItemStack input,
            ItemStack ingredient,
            ItemStack output,
            int cookTime)
    {
        RECIPES.add(
                new MuffleFurnaceRecipe(
                        input,
                        ingredient,
                        output,
                        cookTime
                )
        );
    }

    public static MuffleFurnaceRecipe getRecipe(
            ItemStack input,
            ItemStack ingredient)
    {
        for (MuffleFurnaceRecipe recipe : RECIPES)
        {
            if (recipe.matches(input, ingredient))
            {
                return recipe;
            }
        }

        return null;
    }

    public static List<MuffleFurnaceRecipe> getRecipes()
    {
        return RECIPES;
    }
}