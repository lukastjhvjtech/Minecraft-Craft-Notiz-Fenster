package com.crafttracker.tracking;

import net.minecraft.world.item.ItemStack;

public class IngredientProgress {
    private final IngredientNeed need;
    private final int available;

    public IngredientProgress(IngredientNeed need, int available) {
        this.need = need;
        this.available = available;
    }

    // Ermöglicht RecipeTrackerHud den Zugriff auf das IngredientNeed-Objekt (für depth, displayStack, etc.)
    public IngredientNeed need() {
        return this.need;
    }

    public int getAvailable() {
        return this.available;
    }

    // Gibt den Text für die Statusanzeige im HUD zurück (z. B. "2 / 5")
    public String statusText() {
        int amountNeeded = this.need.amount(); // Holt die benötigte Anzahl aus IngredientNeed
        return this.available + " / " + amountNeeded;
    }

    public boolean isComplete() {
        return this.available >= this.need.amount();
    }
}
