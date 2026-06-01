package com.crafttracker.tracking;

import net.minecraft.world.item.ItemStack;

public record IngredientNeed(ItemStack template, int required, int depth) {
   
   public IngredientNeed(ItemStack template, int required) {
      this(template, required, 0);
   }

   public IngredientNeed(ItemStack template, int required, int depth) {
      template = template.copy();
      template.setCount(1);
      this.template = template;
      this.required = required;
      this.depth = depth;
   }

   public ItemStack displayStack() {
      ItemStack stack = this.template.copy();
      stack.setCount(Math.min(this.required, stack.getMaxStackSize()));
      return stack;
   }
}
