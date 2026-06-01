package com.crafttracker.client;

import com.crafttracker.tracking.IngredientProgress;
import com.crafttracker.tracking.RecipeTracker;
import com.crafttracker.tracking.TrackedRecipe;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = "crafttracker")
public class RecipeTrackerHud {
   private static final int padding = 6;
   private static final int iconSize = 12;
   private static final int lineHeight = 10;

   @SubscribeEvent
   public static void onRenderGui(RenderGuiEvent.Post event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || mc.options.hideGui) return;

      TrackedRecipe active = RecipeTracker.getActive();
      if (active.isEmpty()) return;

      GuiGraphics graphics = event.getGuiGraphics();
      ItemStack[] inv = RecipeTracker.collectInventoryStacks(mc);
      List<IngredientProgress> progress = active.progressFor(inv);

      int x = 10;
      int y = 10;
      int rowY = y + 15;
      int nameX = x + padding + iconSize + 3;
      int statusRight = x + 150;

      graphics.fill(x, y, x + 160, rowY + (progress.size() * (lineHeight + 1)) + 3, 0xAA000000);
      graphics.drawString(mc.font, Component.literal(active.title()), x + padding, y + 3, 0xFFFFFF00, false);

      for(IngredientProgress entry : progress) {
         int indent = entry.need().depth() * 8; 
         int rowNameX = nameX + indent;

         ItemStack icon = entry.need().displayStack();
         graphics.pose().pushPose();
         float itemScale = (float)iconSize / 16.0F;
         graphics.pose().translate((float)(x + padding + indent), (float)rowY, 0.0F);
         graphics.pose().scale(itemScale, itemScale, 1.0F);
         graphics.renderItem(icon, 0, 0);
         graphics.renderItemDecorations(mc.font, icon, 0, 0);
         graphics.pose().popPose();
         
         int nameColor = entry.isComplete() ? 0xFF00FF00 : 0xFFFFFFFF;
         String name = entry.need().template().getHoverName().getString();
         
         graphics.drawString(mc.font, Component.literal(name), rowNameX, rowY + 2, nameColor, false);
         String status = entry.statusText();
         graphics.drawString(mc.font, status, statusRight - mc.font.width(status), rowY + 2, nameColor, false);
         rowY += lineHeight + 1;
      }
   }
}
