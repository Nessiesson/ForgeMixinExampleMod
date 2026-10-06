package com.example.examplemod.recipe;

import net.minecraft.item.ItemStack;

public class MuffleFurnaceRecipe
{
    private final ItemStack input;
    private final ItemStack ingredient;
    private final ItemStack output;
    private final int cookTime;

    public MuffleFurnaceRecipe(
            ItemStack input,
            ItemStack ingredient,
            ItemStack output,
            int cookTime)
    {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
        this.cookTime = cookTime;
    }

    public ItemStack getInput()
    {
        return input.copy();
    }

    public ItemStack getIngredient()
    {
        return ingredient.copy();
    }

    public ItemStack getOutput()
    {
        return output.copy();
    }

    public int getCookTime()
    {
        return cookTime;
    }

    public boolean matches(
            ItemStack inputStack,
            ItemStack ingredientStack)
    {
        if (inputStack.isEmpty() || ingredientStack.isEmpty())
        {
            return false;
        }

        return inputStack.getItem() == input.getItem()
                && inputStack.getMetadata() == input.getMetadata()
                && ingredientStack.getItem() == ingredient.getItem()
                && ingredientStack.getMetadata() == ingredient.getMetadata()
                && inputStack.getCount() >= input.getCount()
                && ingredientStack.getCount() >= ingredient.getCount();
    }
}