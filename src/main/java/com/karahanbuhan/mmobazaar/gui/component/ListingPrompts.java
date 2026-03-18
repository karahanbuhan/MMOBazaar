package com.karahanbuhan.mmobazaar.gui.component;

import com.karahanbuhan.mmobazaar.MMOBazaarContext;
import com.karahanbuhan.mmobazaar.bazaar.BazaarData;
import com.karahanbuhan.mmobazaar.bazaar.BazaarListing;
import com.karahanbuhan.mmobazaar.localization.TranslationKey;
import com.karahanbuhan.mmobazaar.util.ItemUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ListingPrompts {
    private final MMOBazaarContext context;
    private final BazaarData data;
    private final OwnerGUI ownerGui;

    public ListingPrompts(MMOBazaarContext context, BazaarData data, OwnerGUI ownerGui) {
        this.context = context;
        this.data = data;
        this.ownerGui = ownerGui;
    }

    public void openListingPrompt(Player player, Inventory inventory, ItemStack item, int slot) {
        new AnvilGUI.Builder().onClick((_s, stateSnapshot) -> {
            try {
                double price = Double.parseDouble(stateSnapshot.getText());
                if (price <= 0) throw new NumberFormatException();

                data.addListing(slot, item.clone(), price);
                inventory.setItem(slot, ItemUtils.withOwnerLore(context.localization, item, price));
                Bukkit.getScheduler().runTaskAsynchronously(context.plugin, () -> context.storage.saveBazaar(data));
                context.gui.closeCustomerAndConfirmGUIsForAllPlayers(data);

                player.sendMessage(context.localization.get(TranslationKey.MSG_ITEM_LISTED, price));
            } catch (NumberFormatException e) {
                player.sendMessage(context.localization.get(TranslationKey.MSG_INVALID_PRICE));
                // Item will be given back in onClose() as slot is going to be empty
            }
            return List.of(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(ownerGui::getInventory));
        }).onClose(stateSnapshot -> {
            if (!data.getListings().containsKey(slot)) {
                returnItem(player, item);
            }
        }).text("10.0").itemLeft(new ItemStack(Material.NAME_TAG)).title(context.localization.get(TranslationKey.GUI_ENTER_PRICE_TITLE)).plugin(context.plugin).open(player);
    }

    public void openEditPrompt(Player player, int slot, BazaarListing listing) {
        new AnvilGUI.Builder().plugin(context.plugin).title(context.localization.get(TranslationKey.GUI_EDIT_PRICE_TITLE)).text(String.valueOf(listing.getPrice())).itemLeft(new ItemStack(Material.NAME_TAG)).onClick((clickedSlot, state) -> {
            if (clickedSlot != AnvilGUI.Slot.OUTPUT) return List.of();

            try {
                double newPrice = Double.parseDouble(state.getText());
                if (newPrice <= 0) throw new NumberFormatException();

                boolean updated = data.changeListingPrice(slot, newPrice);
                if (updated) {
                    Bukkit.getScheduler().runTaskAsynchronously(context.plugin, () -> context.storage.saveBazaar(data));
                    context.gui.closeCustomerAndConfirmGUIsForAllPlayers(data);

                    player.sendMessage(context.localization.get(TranslationKey.MSG_PRICE_UPDATED, newPrice));
                } else {
                    player.sendMessage(context.localization.get(TranslationKey.MSG_PRICE_UPDATE_FAILED));
                }
            } catch (NumberFormatException e) {
                player.sendMessage(context.localization.get(TranslationKey.MSG_INVALID_PRICE));
            }

            return List.of(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(ownerGui::getInventory));
        }).open(player);
    }

    private void returnItem(Player player, ItemStack item) {
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), leftover);
        }
    }
}