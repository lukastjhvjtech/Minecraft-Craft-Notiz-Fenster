package com.crafttracker.tracking;

import net.minecraft.world.item.ItemStack;

/**
 * Repräsentiert eine benötigte Zutat inkl. Menge und Crafting-Tiefe.
 *
 * @param stack    Der Item-Typ (ItemStack ohne Menge, nur Item-Identität)
 * @param required Die insgesamt benötigte Anzahl
 * @param depth    Die Tiefe im Rezeptbaum (0 = Endprodukt, 1 = direkt benötigt, usw.)
 */
public record IngredientNeed(ItemStack stack, int required, int depth) {

    /**
     * Gibt eine Kopie mit erhöhter Menge zurück (nützlich beim Aggregieren).
     */
    public IngredientNeed withAdditional(int extra) {
        return new IngredientNeed(this.stack, this.required + extra, this.depth);
    }

    /**
     * Prüft, ob der gegebene ItemStack das gleiche Item (ignoriert Menge) repräsentiert.
     */
    public boolean matches(ItemStack other) {
        return ItemStack.isSameItemSameComponents(this.stack, other);
    }
}
