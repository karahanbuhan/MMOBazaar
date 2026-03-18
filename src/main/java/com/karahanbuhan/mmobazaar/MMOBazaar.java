package com.karahanbuhan.mmobazaar;

import com.karahanbuhan.mmobazaar.bazaar.BazaarData;
import com.karahanbuhan.mmobazaar.bazaar.BazaarManager;
import com.karahanbuhan.mmobazaar.commands.MMOBazaarCommand;
import com.karahanbuhan.mmobazaar.config.BazaarConfig;
import com.karahanbuhan.mmobazaar.config.StorageConfig;
import com.karahanbuhan.mmobazaar.economy.VaultHook;
import com.karahanbuhan.mmobazaar.gui.GUIManager;
import com.karahanbuhan.mmobazaar.item.BazaarBagFactory;
import com.karahanbuhan.mmobazaar.listener.BazaarBagUseListener;
import com.karahanbuhan.mmobazaar.listener.BazaarInteractionListener;
import com.karahanbuhan.mmobazaar.listener.GUIListener;
import com.karahanbuhan.mmobazaar.localization.LocalizationManager;
import com.karahanbuhan.mmobazaar.localization.TranslationKey;
import com.karahanbuhan.mmobazaar.localization.TranslationLogger;
import com.karahanbuhan.mmobazaar.storage.SQLStorageFactory;
import com.karahanbuhan.mmobazaar.storage.api.BazaarStorage;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Objects;

public class MMOBazaar extends JavaPlugin {
    public static NamespacedKey BAZAAR_ID_KEY;

    private MMOBazaarContext context;
    private TranslationLogger logger;

    // TODO Sound effects!

    @Override
    public void onEnable() {
        BAZAAR_ID_KEY = new NamespacedKey(this, "bazaar-id");

        // Configuration
        final BazaarConfig config;
        saveDefaultConfig();
        config = new BazaarConfig(getConfig());

        LocalizationManager localization = new LocalizationManager(this, config.getLocale());
        // Prepare translation logger for multi-language support in console
        logger = new TranslationLogger(this, localization);

        ConfigurationSection storageSection = getConfig().getConfigurationSection("storage");
        if (storageSection == null) {
            logger.severe(TranslationKey.LOG_STORAGE_SECTION_MISSING);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        final StorageConfig storageConfig = new StorageConfig(this, storageSection);

        // Vault Integration
        final VaultHook vaultHook = new VaultHook();
        if (!vaultHook.setup()) {
            logger.severe(TranslationKey.LOG_VAULT_NOT_FOUND);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load storage
        SQLStorageFactory storageFactory = new SQLStorageFactory(getLogger());
        BazaarStorage storage = null;
        try {
            storage = storageFactory.create(storageConfig);
        } catch (ClassNotFoundException e) {
            logger.severe(TranslationKey.LOG_JDBC_LOADING_FAILED);
        }
        if (storage == null) {
            logger.severe(TranslationKey.LOG_STORAGE_BACKEND_MISSING);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        storage.init();

        // Setup MMOBazaar
        final BazaarManager bazaarManager = new BazaarManager(this, storage);

        // Load bazaars from storage
        storage.loadAllBazaars().ifPresent(loadedBazaars -> {
            logger.info(TranslationKey.LOG_LOADED_BAZAARS, loadedBazaars.size());
            loadedBazaars.forEach(bazaarManager::registerBazaar);
        });

        final BazaarBagFactory bagFactory = new BazaarBagFactory(localization, config.getCreationFee());
        final GUIManager gui = new GUIManager();

        // Setup context bundle for easier access to MMOBazaar
        context = new MMOBazaarContext(this, localization, vaultHook, bazaarManager, bagFactory, gui, config, storage);

        // Register listeners
        final PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BazaarBagUseListener(context), this);
        pm.registerEvents(new BazaarInteractionListener(context), this);
        pm.registerEvents(new GUIListener(context), this);

        // Set command executor
        Objects.requireNonNull(getCommand("mmobazaar")).setExecutor(new MMOBazaarCommand(context));

        logger.info(TranslationKey.LOG_ENABLED);
    }

    @Override
    public void onDisable() {
        if (context == null) return;

        // Close all GUIs, in case it is a server reload to prevent item dupe
        context.gui.closeAllGUIs();

        // Save all bazaars in case
        if (context.storage != null) {
            try {
                Collection<BazaarData> bazaars = context.bazaarManager.getAllBazaars();
                context.storage.saveBazaars(bazaars);
                logger.info(TranslationKey.LOG_SAVED_BAZAARS, bazaars.size());
            } catch (Exception e) {
                logger.exception(TranslationKey.LOG_SAVE_FAILED, e);
            }
        }

        logger.info(TranslationKey.LOG_DISABLED);
    }
}
