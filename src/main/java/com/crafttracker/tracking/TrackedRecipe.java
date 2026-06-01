package com.crafttracker.tracking;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public record TrackedRecipe(String title, List<IngredientNeed> ingredients) {
   public static TrackedRecipe empty() {
      return new TrackedRecipe("", List.of());
   }

   public boolean isEmpty() {
      return this.ingredients.isEmpty();
   }

   public List<IngredientProgress> progressFor(ItemStack[] inventoryStacks) {
      List<IngredientProgress> result = new ArrayList<>(this.ingredients.size());
      for(IngredientNeed need : this.ingredients) {
         int available = 0;
         for (ItemStack stack : inventoryStacks) {
            if (!stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, need.template())) {
               available += stack.getCount();
            }
         }
         result.add(new IngredientProgress(need, available));
      }
      return result;
   }
}
