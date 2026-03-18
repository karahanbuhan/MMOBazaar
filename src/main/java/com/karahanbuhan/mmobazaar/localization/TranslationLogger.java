package com.karahanbuhan.mmobazaar.localization;

import com.karahanbuhan.mmobazaar.util.LogUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class TranslationLogger {

    private final Logger logger;
    private final LocalizationManager localization;

    public TranslationLogger(JavaPlugin plugin, LocalizationManager localization) {
        this.logger = plugin.getLogger();
        this.localization = localization;
    }

    public void info(TranslationKey key, Object... args) {
        logger.info(localization.get(key, args));
    }

    public void warning(TranslationKey key, Object... args) {
        logger.warning(localization.get(key, args));
    }

    public void severe(TranslationKey key, Object... args) {
        logger.severe(localization.get(key, args));
    }

    public void exception(TranslationKey contextKey, Throwable t, Object... args) {
        String contextMessage = localization.get(contextKey, args);
        LogUtil.logException(logger, contextMessage, t);
    }
}