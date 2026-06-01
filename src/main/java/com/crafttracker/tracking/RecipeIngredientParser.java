package com.crafttracker.tracking;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Parst Rezepte und löst auch verschachtelte Crafting-Bäume auf
 * (z. B. wenn eine Zutat selbst gecraftet werden muss).
 */
public class RecipeIngredientParser {

    private static final int MAX_DEPTH = 10; // Verhindert Endlos-Rekursion bei zirkulären Rezepten

    /**
     * Hauptmethode: Baut eine Liste aller benötigten Zutaten für einen Stack auf.
     */
    public static List<IngredientNeed> resolveAllNeeds(ItemStack target, int amount) {
        List<IngredientNeed> needs = new ArrayList<>();
        resolveDeepNeeds(needs, target, amount, 0);
        return needs;
    }

    /**
     * Rekursive Auflösung: Fügt den Stack hinzu und steigt ggf. in Unterezepte ab.
     */
    private static void resolveDeepNeeds(List<IngredientNeed> needs, ItemStack stack, int amount, int depth) {
        if (depth > MAX_DEPTH) {
            return; // Sicherheitsschranke
        }

        // TODO: Hier deine Rezept-Lookup-Logik einbauen
        // Beispiel: RecipeManager nach Rezept durchsuchen, das `stack` als Output hat.
        // Wenn ein Rezept gefunden wurde, rekursiv für jede Input-Zutat aufrufen.
        // Wenn kein Rezept existiert (Rohstoff), als Need hinzufügen:

        addNeed(needs, stack, amount, depth);
    }

    /**
     * Fügt einen Bedarf hinzu oder aggregiert ihn mit bereits vorhandenen Einträgen
     * desselben Items (berücksichtigt dabei die maximale Tiefe).
     */
    private static void addNeed(List<IngredientNeed> needs, ItemStack stack, int amount, int depth) {
        // Prüfen, ob das Item bereits gelistet ist
        for (int i = 0; i < needs.size(); i++) {
            IngredientNeed existing = needs.get(i);
            if (existing.matches(stack)) {
                // Aggregieren: Menge erhöhen, Tiefe beibehalten (oder Minimum nehmen)
                int newDepth = Math.min(existing.depth(), depth);
                needs.set(i, new IngredientNeed(stack, existing.required() + amount, newDepth));
                return;
            }
        }
        // Neu hinzufügen
        needs.add(new IngredientNeed(stack, amount, depth));
    }
}
