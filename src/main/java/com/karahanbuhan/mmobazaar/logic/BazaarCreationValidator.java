package com.karahanbuhan.mmobazaar.logic;

import com.karahanbuhan.mmobazaar.MMOBazaarContext;
import com.karahanbuhan.mmobazaar.item.BazaarBagFactory;
import com.karahanbuhan.mmobazaar.localization.TranslationKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BazaarCreationValidator {
    public enum Result {
        SUCCESS(null), INSUFFICIENT_FUNDS(TranslationKey.MSG_BAZAAR_NEED_MONEY), BAZAAR_LIMIT_REACHED(TranslationKey.MSG_BAZAAR_LIMIT_REACHED), MISSING_BAZAAR_BAG(TranslationKey.MSG_BAZAAR_NEED_BAG);

        private final TranslationKey messageKey;

        Result(TranslationKey key) {
            this.messageKey = key;
        }

        public void respond(Player player, MMOBazaarContext context) {
            if (this == SUCCESS) return;
            player.sendMessage(context.localization.get(messageKey, getArgs(context)));
        }

        private Object[] getArgs(MMOBazaarContext context) {
            return switch (this) {
                case INSUFFICIENT_FUNDS -> new Object[]{context.config.getCreationFee()};
                case BAZAAR_LIMIT_REACHED -> new Object[]{context.config.getMaxBazaarsPerPlayer()};
                default -> new Object[]{};
            };
        }

        public boolean isSuccess() {
            return this == SUCCESS;
        }
    }

    public static Result canCreate(MMOBazaarContext context, Player player) {
        double balance = context.vaultHook.getEconomy().getBalance(player);
        double fee = context.config.getCreationFee();

        int existing = context.bazaarManager.getBazaarsByOwner(player.getUniqueId()).size();
        int limit = context.config.getMaxBazaarsPerPlayer();

        if (!hasBazaarBag(player, context.bagFactory)) return Result.MISSING_BAZAAR_BAG;
        if (balance < fee) return Result.INSUFFICIENT_FUNDS;
        if (existing >= limit) return Result.BAZAAR_LIMIT_REACHED;

        return Result.SUCCESS;
    }

    private static boolean hasBazaarBag(Player player, BazaarBagFactory factory) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (factory.isBazaarBag(item)) return true;
        }
        return false;
    }
}
