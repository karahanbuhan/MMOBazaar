package com.karahanbuhan.mmobazaar.util;

import com.karahanbuhan.mmobazaar.localization.LocalizationManager;
import com.karahanbuhan.mmobazaar.localization.TranslationKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    public static ItemStack withBazaarLore(LocalizationManager localization, ItemStack item, double price, String sellerName) {
        return withExtraLore(item, List.of(
                "",
                localization.get(TranslationKey.GUI_LORE_LISTING_PRICE, price),
                localization.get(TranslationKey.GUI_LORE_LISTING_SELLER, sellerName),
                localization.get(TranslationKey.GUI_LORE_LISTING_CLICK_TO_BUY)
        ));
    }

    public static ItemStack withOwnerLore(LocalizationManager localization, ItemStack item, double price) {
        return withExtraLore(item, List.of(
                "",
                localization.get(TranslationKey.GUI_LORE_LISTING_PRICE, price),
                localization.get(TranslationKey.GUI_LORE_LISTING_LEFT_CLICK_EDIT),
                localization.get(TranslationKey.GUI_LORE_LISTING_RIGHT_CLICK_REMOVE)
        ));
    }

    public static ItemStack stripListingLore(ItemStack item, int linesToRemove) {
        ItemStack clone = item.clone();
        ItemMeta meta = clone.getItemMeta();

        if (meta != null && meta.hasLore() && meta.getLore() != null) {
            List<String> lore = new ArrayList<>(meta.getLore());
            int newSize = Math.max(0, lore.size() - linesToRemove);
            lore = lore.subList(0, newSize);
            meta.setLore(lore);
            clone.setItemMeta(meta);
        }

        return clone;
    }

    private static ItemStack withExtraLore(ItemStack base, List<String> extra) {
        ItemStack clone = base.clone();
        ItemMeta meta = clone.getItemMeta();

        if (meta != null) {
            List<String> lore = getSafeLore(meta);
            lore.addAll(extra);
            meta.setLore(lore);
            clone.setItemMeta(meta);
        }

        return clone;
    }

    private static List<String> getSafeLore(ItemMeta meta) {
        return (meta != null && meta.hasLore() && meta.getLore() != null)
                ? new ArrayList<>(meta.getLore())
                : new ArrayList<>();
    }
}
