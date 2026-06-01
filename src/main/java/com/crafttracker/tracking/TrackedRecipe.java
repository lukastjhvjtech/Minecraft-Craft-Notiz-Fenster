package com.crafttracker.tracking;

import java.util.List;

/**
 * Ein getracktes Crafting-Rezept mit Titel und allen benötigten Zutaten.
 */
public record TrackedRecipe(String title, List<IngredientNeed> ingredients) {

    /**
     * Gibt für jede Zutat den Fortschritt (Bedarf vs. verfügbar) zurück.
     */
    public List<IngredientProgress> calculateProgress(java.util.function.Predicate<IngredientNeed> availabilityChecker) {
        return this.ingredients.stream()
                .map(need -> {
                    // Platzhalter: Hier müsstest du deine Inventar-Logik einbauen
                    int available = availabilityChecker.test(need) ? need.required() : 0;
                    return new IngredientProgress(need, available);
                })
                .toList();
    }

    /**
     * Summiert alle benötigten Einheiten über alle Zutaten.
     */
    public int totalRequiredItems() {
        int total = 0;
        for (IngredientNeed need : this.ingredients) {
            total += need.required();
        }
        return total;
    }
}
