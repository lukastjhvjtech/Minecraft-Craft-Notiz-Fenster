package com.crafttracker.client;

import com.crafttracker.tracking.RecipeTracker;
import com.crafttracker.tracking.TrackedRecipe;
import com.mojang.brigadier.CommandDispatcher;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class TrackServerCommand {
   private static HttpServer server = null;
   private static final int PORT = 8080;

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(Commands.literal("crafttracker")
         .then(Commands.literal("server")
            .then(Commands.literal("start").executes(context -> startServer(context.getSource())))
            .then(Commands.literal("stop").executes(context -> stopServer(context.getSource())))
         )
      );
   }

   private static int startServer(CommandSourceStack source) {
      if (server != null) {
         source.sendFailure(Component.literal("Der Localhost-Server läuft bereits auf Port " + PORT));
         return 0;
      }

      try {
         server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
         server.createContext("/tracked", new RecipeApiHandler());
         server.setExecutor(null);
         server.start();

         source.sendSuccess(() -> Component.literal("Localhost-Server gestartet auf Port " + PORT), false);
         return 1;
      } catch (IOException e) {
         source.sendFailure(Component.literal("Fehler beim Starten des Servers: " + e.getMessage()));
         return 0;
      }
   }

   private static int stopServer(CommandSourceStack source) {
      if (server == null) {
         source.sendFailure(Component.literal("Der Server läuft aktuell nicht."));
         return 0;
      }

      server.stop(0);
      server = null;
      source.sendSuccess(() -> Component.literal("Localhost-Server wurde beendet."), false);
      return 1;
   }

   static class RecipeApiHandler implements HttpHandler {
      @Override
      public void handle(HttpExchange exchange) throws IOException {
         TrackedRecipe active = RecipeTracker.getActive();
         String jsonResponse = "{\"recipe\": \"" + active.title() + "\", \"empty\": " + active.isEmpty() + "}";
         byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

         exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
         exchange.getResponseHeaders().set("Content-Type", "application/json");
         exchange.sendResponseHeaders(200, responseBytes.length);
         
         OutputStream os = exchange.getResponseBody();
         os.write(responseBytes);
         os.close();
      }
   }
}
