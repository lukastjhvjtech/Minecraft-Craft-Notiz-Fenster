package com.crafttracker.tracking;

public class IngredientProgress {
    private final IngredientNeed need;
    private int available;

    public IngredientProgress(IngredientNeed need) {
        this.need = need;
        this.available = 0;
    }

    public IngredientNeed getNeed() {
        return this.need;
    }

    public int getAvailable() {
        return this.available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getAmountNeeded() {
        // Falls IngredientNeed ein Record oder eine public Variable ist, nutzt man: this.need.amount() bzw. this.need.amount
        // Falls IngredientNeed eine normale Klasse mit Getter ist, nutzt man: this.need.getAmount()
        // Wir nutzen hier die sicherste Variante für klassische Java-Klassen:
        return this.need.getAmount(); 
    }

    public boolean isComplete() {
        return this.available >= this.need.getAmount();
    }
}
