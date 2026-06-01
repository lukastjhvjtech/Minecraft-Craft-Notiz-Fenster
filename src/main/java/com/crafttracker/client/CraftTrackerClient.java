package com.crafttracker.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

@Mod(value = "crafttracker", dist = Dist.CLIENT)
public class CraftTrackerClient {

   public CraftTrackerClient(ModContainer modContainer, IEventBus modEventBus) {
      NeoForge.EVENT_BUS.addListener(RegisterClientCommandsEvent.class, event -> {
         TrackServerCommand.register(event.getDispatcher());
      });
   }
}
