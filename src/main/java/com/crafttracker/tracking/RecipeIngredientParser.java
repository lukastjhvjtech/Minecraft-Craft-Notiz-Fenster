package com.crafttracker.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Ingredient;

public final class RecipeIngredientParser {
   private RecipeIngredientParser() {
   }

   public static TrackedRecipe fromLayout(IRecipeLayoutDrawable<?> layout) {
      List<IngredientNeed> needs = new ArrayList<>();

      for(IRecipeSlotView slotView : layout.getRecipeSlotsView().getSlotViews()) {
         if (slotView.getRole() == RecipeIngredientRole.INPUT) {
            Optional<ItemStack> displayed = slotView.getDisplayedItemStack();
            if (displayed.isPresent() && !displayed.get().isEmpty()) {
               resolveDeepNeeds(needs, displayed.get(), displayed.get().getCount(), 0);
            }
         }
      }

      return new TrackedRecipe(buildTitle(layout), needs);
   }

   private static void resolveDeepNeeds(List<IngredientNeed> needs, ItemStack stack, int amount, int depth) {
      if (depth > 5 || isBaseResource(stack)) {
         addNeed(needs, stack, amount, depth);
         return;
      }

      try {
         Minecraft mc = Minecraft.getInstance();
         if (mc.level != null) {
            RecipeManager recipeManager = mc.level.getRecipeManager();
            Optional<RecipeHolder<?>> recipeOpt = recipeManager.getRecipes().stream()
    .filter(holder -> holder.value().getResultItem(mc.level.registryAccess()).getItem() == stack.getItem())
    .findFirst();

            if (recipeOpt.isPresent()) {
               Recipe<?> recipe = recipeOpt.get().value();
               for (Ingredient ingredient : recipe.getIngredients()) {
                  if (!ingredient.isEmpty()) {
                     ItemStack subIngredient = ingredient.getItems()[0];
                     resolveDeepNeeds(needs, subIngredient, amount * subIngredient.getCount(), depth + 1);
                  }
               }
               return;
            }
         }
      } catch (Exception e) {
         // Absturzsicherung
      }

      addNeed(needs, stack, amount, depth);
   }

   private static void addNeed(List<IngredientNeed> needs, ItemStack stack, int amount, int depth) {
      for(int i = 0; i < needs.size(); ++i) {
         IngredientNeed existing = needs.get(i);
         if (ItemStack.isSameItemSameComponents(existing.template(), stack) && existing.depth() == depth) {
            needs.set(i, new IngredientNeed(stack, existing.required() + amount, depth));
            return;
         }
      }
      needs.add(new IngredientNeed(stack, amount, depth));
   }

   private static boolean isBaseResource(ItemStack stack) {
      String path = stack.getItem().toString();
      return path.contains("iron_ingot") || path.contains("andesite") || path.contains("copper_ingot") 
          || path.contains("zinc_ingot") || path.contains("coal") || path.contains("log") || path.contains("planks");
   }

   private static String buildTitle(IRecipeLayoutDrawable<?> layout) {
      IRecipeCategory<?> category = layout.getRecipeCategory();
      for(IRecipeSlotView slotView : layout.getRecipeSlotsView().getSlotViews()) {
         if (slotView.getRole() == RecipeIngredientRole.OUTPUT) {
            Optional<ItemStack> output = slotView.getDisplayedItemStack();
            if (output.isPresent() && !output.get().isEmpty()) {
               return output.get().getHoverName().getString();
            }
         }
      }
      return category.getTitle().getString();
   }
}
