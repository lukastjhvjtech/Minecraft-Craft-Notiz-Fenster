package com.crafttracker.tracking;

import net.minecraft.world.item.crafting.Ingredient;

public class IngredientProgress {
    private final Ingredient ingredient;
    private final int needed;
    private final int available;

    public IngredientProgress(Ingredient ingredient, int needed, int available) {
        this.ingredient = ingredient;
        this.needed = needed;
        this.available = available;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getNeeded() {
        return this.needed;
    }

    public int getAvailable() {
        return this.available;
    }

    public boolean isComplete() {
        return this.available >= this.needed;
    }
}
