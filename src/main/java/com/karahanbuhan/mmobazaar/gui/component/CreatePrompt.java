package com.karahanbuhan.mmobazaar.gui.component;

import com.karahanbuhan.mmobazaar.MMOBazaarContext;
import com.karahanbuhan.mmobazaar.localization.TranslationKey;
import com.karahanbuhan.mmobazaar.logic.BazaarCreationValidator;
import net.milkbowl.vault.economy.EconomyResponse;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CreatePrompt {
    /***
     * Used for checking if the player cancelled anvil gui while creating a bazaar.
     */
    private final Set<UUID> creationSession = ConcurrentHashMap.newKeySet();

    private final MMOBazaarContext context;
    private final ItemStack bag;

    public CreatePrompt(MMOBazaarContext context, ItemStack bag) {
        this.context = context;
        this.bag = bag;
    }

    public void open(Player player) {
        creationSession.remove(player.getUniqueId()); // Remove in case we weren't able to complete previously

        new AnvilGUI.Builder().plugin(context.plugin).title(context.localization.get(TranslationKey.GUI_CREATE_TITLE)).text(context.localization.get(TranslationKey.GUI_CREATE_TEXT)).itemLeft(new ItemStack(Material.NAME_TAG)).onClick((slot, state) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) return List.of(); // only act on confirmation

            String name = state.getText().trim();
            if (name.isEmpty()) {
                player.sendMessage(context.localization.get(TranslationKey.MSG_BAZAAR_NAME_EMPTY));
                return List.of(AnvilGUI.ResponseAction.close());
            }

            BazaarCreationValidator.Result result = BazaarCreationValidator.canCreate(context, player);
            switch (result) {
                case MISSING_BAZAAR_BAG -> {
                    player.sendMessage(context.localization.get(TranslationKey.MSG_BAZAAR_NEED_BAG));
                    return List.of(AnvilGUI.ResponseAction.close());
                }
                case INSUFFICIENT_FUNDS -> {
                    player.sendMessage(context.localization.get(TranslationKey.MSG_BAZAAR_NEED_MONEY, context.config.getCreationFee()));
                    return List.of(AnvilGUI.ResponseAction.close());
                }
                case BAZAAR_LIMIT_REACHED -> {
                    player.sendMessage(context.localization.get(TranslationKey.MSG_BAZAAR_LIMIT_REACHED, context.config.getMaxBazaarsPerPlayer()));
                    return List.of(AnvilGUI.ResponseAction.close());
                }
            }

            EconomyResponse withdraw = context.vaultHook.getEconomy().withdrawPlayer(player, context.config.getCreationFee());
            if (!withdraw.transactionSuccess()) {
                player.sendMessage(context.localization.get(TranslationKey.MSG_TRANSACTION_FAILED));
                return List.of(AnvilGUI.ResponseAction.close());
            }

            return context.bazaarManager.createBazaar(player, name).map(data -> List.of(AnvilGUI.ResponseAction.run(() -> {
                creationSession.add(player.getUniqueId());
                bag.setAmount(bag.getAmount() - 1);
                player.sendMessage(context.localization.get(TranslationKey.MSG_BAZAAR_CREATED, context.config.getCreationFee()));
            }), AnvilGUI.ResponseAction.close())).orElseGet(() -> {
                player.sendMessage(context.localization.get(TranslationKey.MSG_BAZAAR_CREATE_FAILED));
                return List.of(AnvilGUI.ResponseAction.close());
            });
        }).onClose(state -> {
            if (!creationSession.contains(player.getUniqueId())) {
                player.sendMessage(context.localization.get(TranslationKey.MSG_BAZAAR_CREATE_CANCELLED));
            }
        }).open(player);
    }
}