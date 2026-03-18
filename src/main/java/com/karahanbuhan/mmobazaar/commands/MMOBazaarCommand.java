package com.karahanbuhan.mmobazaar.commands;

import com.karahanbuhan.mmobazaar.MMOBazaarContext;
import com.karahanbuhan.mmobazaar.localization.TranslationKey;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MMOBazaarCommand implements CommandExecutor {
    private final MMOBazaarContext context;

    public MMOBazaarCommand(MMOBazaarContext context) {
        this.context = context;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found.");
                return true;
            }

            // Check if amount is something valid and greater than 0
            int amount = 1;
            if (args.length >= 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                    if (amount <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cAmount must be a positive number.");
                    return true;
                }
            }

            context.bagFactory.giveBazaarBag(target, amount);

            sender.sendMessage(context.localization.get(TranslationKey.MSG_BAG_GIVEN, amount, target.getName()));
            target.sendMessage(amount == 1 ? context.localization.get(TranslationKey.MSG_BAG_ADDED) : context.localization.get(TranslationKey.MSG_BAGS_ADDED, amount));

            return true;
        }

        sender.sendMessage("§cUsage: /mmobazaar give <player> [amount]");
        return true;
    }
}
