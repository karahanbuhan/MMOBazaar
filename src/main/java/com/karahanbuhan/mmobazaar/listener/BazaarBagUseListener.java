package com.karahanbuhan.mmobazaar.listener;

import com.karahanbuhan.mmobazaar.MMOBazaarContext;
import com.karahanbuhan.mmobazaar.gui.component.CreatePrompt;
import com.karahanbuhan.mmobazaar.logic.BazaarCreationValidator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BazaarBagUseListener implements Listener {
    private final MMOBazaarContext context;

    public BazaarBagUseListener(MMOBazaarContext context) {
        this.context = context;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Ignore offhand and empty interacts, right click only
        if (event.getHand() != EquipmentSlot.HAND) return;
        final ItemStack item = event.getItem();
        if (item == null || item.getType().isAir()) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        // Check if item is bazaar bag
        if (!context.bagFactory.isBazaarBag(item)) return;

        event.setCancelled(true); // Cancel in case player does actually interact with something

        final Player player = event.getPlayer();
        final BazaarCreationValidator.Result result = BazaarCreationValidator.canCreate(context, player);
        if (result.isSuccess()) {
            new CreatePrompt(context, item).open(player);
        } else {
            result.respond(player, context);
        }
    }
}