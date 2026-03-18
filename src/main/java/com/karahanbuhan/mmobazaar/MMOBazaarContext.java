package com.karahanbuhan.mmobazaar;

import com.karahanbuhan.mmobazaar.config.BazaarConfig;
import com.karahanbuhan.mmobazaar.economy.VaultHook;
import com.karahanbuhan.mmobazaar.bazaar.BazaarManager;
import com.karahanbuhan.mmobazaar.gui.GUIManager;
import com.karahanbuhan.mmobazaar.item.BazaarBagFactory;
import com.karahanbuhan.mmobazaar.localization.LocalizationManager;
import com.karahanbuhan.mmobazaar.storage.api.BazaarStorage;

public class MMOBazaarContext {
    public final MMOBazaar plugin;
    public final LocalizationManager localization;
    public final BazaarConfig config;
    public final VaultHook vaultHook;
    public final BazaarManager bazaarManager;
    public final BazaarBagFactory bagFactory;
    public final GUIManager gui;
    public final BazaarStorage storage;

    public MMOBazaarContext(MMOBazaar plugin, LocalizationManager localization, VaultHook vaultHook, BazaarManager manager, BazaarBagFactory bagFactory, GUIManager gui, BazaarConfig config, BazaarStorage storage) {
        this.plugin = plugin;
        this.localization = localization;
        this.vaultHook = vaultHook;
        this.bazaarManager = manager;
        this.bagFactory = bagFactory;
        this.gui = gui;
        this.config = config;
        this.storage = storage;
    }
}
