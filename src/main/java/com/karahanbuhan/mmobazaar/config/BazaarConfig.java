package com.karahanbuhan.mmobazaar.config;

import org.bukkit.configuration.file.FileConfiguration;

public class BazaarConfig {
    private final String locale;
    private final int maxBazaarsPerPlayer;
    private final double creationFee;
    private final double extensionFee;

    public BazaarConfig(FileConfiguration config) {
        this.locale = config.getString("locale");
        this.maxBazaarsPerPlayer = config.getInt("bazaar.max-per-player", 3);
        this.creationFee = config.getDouble("bazaar.creation-fee", 1000.0);
        this.extensionFee = config.getDouble("bazaar.extension-fee", 1000.0);
    }

    public String getLocale() {
        return locale;
    }

    public int getMaxBazaarsPerPlayer() {
        return maxBazaarsPerPlayer;
    }

    public double getCreationFee() {
        return creationFee;
    }

    public double getExtensionFee() {
        return extensionFee;
    }

}
