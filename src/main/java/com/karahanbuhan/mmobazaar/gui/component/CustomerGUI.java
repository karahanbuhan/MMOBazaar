package com.karahanbuhan.mmobazaar.gui.component;

import com.karahanbuhan.mmobazaar.MMOBazaarContext;
import com.karahanbuhan.mmobazaar.bazaar.BazaarData;
import com.karahanbuhan.mmobazaar.bazaar.BazaarListing;
import com.karahanbuhan.mmobazaar.gui.api.BazaarGUI;
import com.karahanbuhan.mmobazaar.gui.api.ClickableGUI;
import com.karahanbuhan.mmobazaar.localization.TranslationKey;
import com.karahanbuhan.mmobazaar.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CustomerGUI implements ClickableGUI, BazaarGUI {
    private final MMOBazaarContext context;
    private final BazaarData data;

    public CustomerGUI(MMOBazaarContext context, BazaarData data) {
        this.context = context;
        this.data = data;
    }

    public Inventory getInventory() {
        Inventory gui = Bukkit.createInventory(null, 27, data.getName());

        for (Map.Entry<Integer, BazaarListing> entry : data.getListings().entrySet()) {
            int slot = entry.getKey();
            BazaarListing listing = entry.getValue();

            gui.setItem(slot, ItemUtils.withBazaarLore(context.localization, listing.getItem().clone(), listing.getPrice(), Bukkit.getOfflinePlayer(data.getOwner()).getName()));
        }

        return gui;
    }

    @Override
    public void handleClick(Player player, InventoryClickEvent event) {
        event.setCancelled(true);

        int slot = event.getRawSlot();
        BazaarListing listing = data.getListings().get(slot);
        if (listing == null) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) return;

        ItemStack original = listing.getItem();

        // Security check: must match listing item
        if (!ItemUtils.stripListingLore(clicked, 4).isSimilar(original)) {
            player.sendMessage(context.localization.get(TranslationKey.MSG_ITEM_MISMATCH));
            return;
        }

        // Open confirmation GUI
        context.gui.openConfirmGUI(player, new ConfirmGUI(context, data, listing));
    }

    @Override
    public BazaarData getBazaar() {
        return data;
    }
}