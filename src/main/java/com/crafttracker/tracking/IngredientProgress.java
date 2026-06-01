package com.crafttracker.tracking;

/**
 * Kombiniert den Bedarf einer Zutat mit der aktuell verfügbaren Menge.
 */
public class IngredientProgress {

    private final IngredientNeed need;
    private final int available;

    public IngredientProgress(IngredientNeed need, int available) {
        this.need = need;
        this.available = available;
    }

    public IngredientNeed need() {
        return this.need;
    }

    public int available() {
        return this.available;
    }

    /**
     * Wie viele Einheiten noch fehlen (0 wenn ausreichend vorhanden).
     */
    public int missing() {
        return Math.max(0, this.need.required() - this.available);
    }

    /**
     * Ob die Zutat vollständig abgedeckt ist.
     */
    public boolean isSatisfied() {
        return this.available >= this.need.required();
    }
}
