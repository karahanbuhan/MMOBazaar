package com.karahanbuhan.mmobazaar.item;

import com.karahanbuhan.mmobazaar.MMOBazaarContext;
import com.karahanbuhan.mmobazaar.gui.GUIManager;
import com.karahanbuhan.mmobazaar.localization.LocalizationManager;
import com.karahanbuhan.mmobazaar.localization.TranslationKey;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BazaarBagFactory {
    private final LocalizationManager localization;

    private final double creationCost;

    private final String displayName;
    private final List<String> lore;
    private final int customModelData;
    private final Material baseMaterial;

    // TODO Custom persistent data for this item to update prices in lore, checking easily etc.

    public BazaarBagFactory(MMOBazaarContext context) {
        this(context.localization, context.config.getCreationFee());
    }

    public BazaarBagFactory(LocalizationManager localization, double creationCost) {
        this.localization = localization;
        this.creationCost = creationCost;

        displayName = localization.get(TranslationKey.ITEM_BAZAAR_BAG_NAME);
        lore = List.of(localization.get(TranslationKey.ITEM_BAZAAR_BAG_LORE_USAGE));
        customModelData = 7001;
        baseMaterial = Material.RABBIT_HIDE;
    }

    public void giveBazaarBag(Player player, int amount) {
        if (amount <= 0) return;

        ItemStack bag = create();
        bag.setAmount(Math.min(amount, bag.getMaxStackSize())); // max 64

        int fullStacks = amount / bag.getMaxStackSize();
        int remainder = amount % bag.getMaxStackSize();

        for (int i = 0; i < fullStacks; i++) {
            player.getInventory().addItem(bag.clone());
        }

        if (remainder > 0) {
            ItemStack last = bag.clone();
            last.setAmount(remainder);
            player.getInventory().addItem(last);
        }
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(baseMaterial);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);

            List<String> fullLore = new ArrayList<>(lore); // existing base lore
            fullLore.add(""); // spacing line
            fullLore.add(localization.get(TranslationKey.ITEM_BAZAAR_BAG_LORE_COST_HEADER));
            fullLore.add(localization.get(TranslationKey.ITEM_BAZAAR_BAG_LORE_COST_LINE, creationCost));

            meta.setCustomModelData(customModelData);
            meta.setLore(fullLore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean isBazaarBag(ItemStack item) {
        if (item == null || item.getType() != baseMaterial || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasCustomModelData()) return false;
        return meta.getCustomModelData() == customModelData;
    }
}
