package com.crafttracker.tracking;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public final class RecipeTracker {
   private static TrackedRecipe active = TrackedRecipe.empty();

   private RecipeTracker() {
   }

   public static TrackedRecipe getActive() {
      return active;
   }

   public static void setActive(TrackedRecipe recipe) {
      active = recipe == null ? TrackedRecipe.empty() : recipe;
   }

   public static void clear() {
      active = TrackedRecipe.empty();
   }

   public static ItemStack[] collectInventoryStacks(Minecraft minecraft) {
      if (minecraft.player == null) {
         return new ItemStack[0];
      } else {
         Inventory inventory = minecraft.player.getInventory();
         int size = inventory.getContainerSize();
         ItemStack[] stacks = new ItemStack[size];

         for(int i = 0; i < size; ++i) {
            stacks[i] = inventory.getItem(i);
         }

         return stacks;
      }
   }
}
