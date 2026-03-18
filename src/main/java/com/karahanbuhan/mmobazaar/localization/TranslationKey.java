package com.karahanbuhan.mmobazaar.localization;

public enum TranslationKey {
    // Logging
    LOG_VAULT_NOT_FOUND("log.vault.not-found"),
    LOG_STORAGE_SECTION_MISSING("log.storage.section-missing"),
    LOG_JDBC_LOADING_FAILED("log.jdbc.loading-failed"),
    LOG_STORAGE_BACKEND_MISSING("log.storage.backend-missing"),
    LOG_LOADED_BAZAARS("log.loaded-bazaars"),
    LOG_ENABLED("log.enabled"),
    LOG_SAVED_BAZAARS("log.saved-bazaars"),
    LOG_SAVE_FAILED("log.save-failed"),
    LOG_DISABLED("log.disabled"),
    LOG_DEFAULT_LANG_ERROR("log.default-lang-error"),
    LOG_FALLBACK_LANG_FATAL("log.fallback-lang-fatal"),
    LOG_TRANSACTION_FAILED("log.transaction-failed"),
    LOG_CONNECTION_FAILED("log.connection-failed"),
    LOG_QUERY_FAILED("log.query-failed"),
    LOG_CORRUPTED_BAZAAR("log.corrupted-bazaar"),

    // Items
    ITEM_BAZAAR_BAG_NAME("item.bazaar-bag.name"),
    ITEM_BAZAAR_BAG_LORE_USAGE("item.bazaar-bag.lore.usage"),
    ITEM_BAZAAR_BAG_LORE_COST_HEADER("item.bazaar-bag.lore.cost-header"),
    ITEM_BAZAAR_BAG_LORE_COST_LINE("item.bazaar-bag.lore.cost-line"),

    // GUI components
    GUI_BANK_BUTTON_NAME("gui.bank-button.name"),
    GUI_BANK_BUTTON_LORE_CURRENT("gui.bank-button.lore.current"),
    GUI_BANK_BUTTON_LORE_WITHDRAW("gui.bank-button.lore.withdraw"),
    GUI_TIME_LEFT_BUTTON_NAME("gui.time-left-button.name"),
    GUI_TIME_LEFT_BUTTON_LORE_REMAINING("gui.time-left-button.lore.remaining"),
    GUI_TIME_LEFT_BUTTON_LORE_EXTEND("gui.time-left-button.lore.extend"),
    GUI_DELETE_BAZAAR_BUTTON_NAME("gui.delete-bazaar-button.name"),
    GUI_DELETE_BAZAAR_BUTTON_LORE("gui.delete-bazaar-button.lore"),
    GUI_ROTATE_BAZAAR_BUTTON_NAME("gui.rotate-bazaar-button.name"),
    GUI_ROTATE_BAZAAR_BUTTON_LORE("gui.rotate-bazaar-button.lore"),
    GUI_CONFIRM_TITLE("gui.confirm.title"),
    GUI_CONFIRM_BUTTON_NAME("gui.confirm.button.name"),
    GUI_CONFIRM_BUTTON_LORE_PRICE("gui.confirm.button.lore.price"),
    GUI_CONFIRM_BUTTON_LORE_SELLER("gui.confirm.button.lore.seller"),
    GUI_CONFIRM_BUTTON_LORE_CLICK("gui.confirm.button.lore.click"),
    GUI_CONFIRM_PREVIEW_LORE_PRICE("gui.confirm.preview.lore.price"),
    GUI_CONFIRM_PREVIEW_LORE_CLICK("gui.confirm.preview.lore.click"),
    GUI_CONFIRM_CANCEL_NAME("gui.confirm.cancel.name"),
    GUI_CREATE_TITLE("gui.create.title"),
    GUI_CREATE_TEXT("gui.create.text"),
    GUI_ENTER_PRICE_TITLE("gui.enter-price.title"),
    GUI_EDIT_PRICE_TITLE("gui.edit-price.title"),
    GUI_BAZAAR_CLOSED_PREFIX("gui.bazaar.closed-prefix"),

    GUI_LORE_LISTING_PRICE("gui.lore.listing.price"),
    GUI_LORE_LISTING_SELLER("gui.lore.listing.seller"),
    GUI_LORE_LISTING_CLICK_TO_BUY("gui.lore.listing.click-to-buy"),
    GUI_LORE_LISTING_LEFT_CLICK_EDIT("gui.lore.listing.left-click-edit"),
    GUI_LORE_LISTING_RIGHT_CLICK_REMOVE("gui.lore.listing.right-click-remove"),

    // Time
    TIME_UNIT_DAY("time.unit.day"),
    TIME_UNIT_HOUR("time.unit.hour"),
    TIME_UNIT_MINUTE("time.unit.minute"),

    // Player messages
    MSG_BAZAAR_NAME_EMPTY("msg.bazaar.name-empty"),
    MSG_BAZAAR_NEED_BAG("msg.bazaar.need-bag"),
    MSG_BAZAAR_NEED_MONEY("msg.bazaar.need-money"),
    MSG_BAZAAR_LIMIT_REACHED("msg.bazaar.limit-reached"),
    MSG_TRANSACTION_FAILED("msg.transaction.failed"),
    MSG_BAZAAR_CREATED("msg.bazaar.created"),
    MSG_BAZAAR_CREATE_FAILED("msg.bazaar.create-failed"),
    MSG_BAZAAR_CREATE_CANCELLED("msg.bazaar.create-cancelled"),
    MSG_ITEM_LISTED("msg.item.listed"),
    MSG_INVALID_PRICE("msg.invalid-price"),
    MSG_PRICE_UPDATED("msg.price.updated"),
    MSG_PRICE_UPDATE_FAILED("msg.price.update-failed"),
    MSG_ITEM_MISMATCH("msg.item.mismatch"),
    MSG_LISTING_REMOVED("msg.listing.removed"),
    MSG_WITHDRAW_SUCCESS("msg.withdraw.success"),
    MSG_BAZAAR_CLOSED("msg.bazaar.closed"),
    MSG_BAZAAR_CLOSED_REFUND("msg.bazaar.closed.refund"),
    MSG_EXTEND_NEED_MONEY("msg.extend.need-money"),
    MSG_EXTEND_SUCCESS("msg.extend.success"),
    MSG_EXTEND_LIMIT("msg.extend.limit"),
    MSG_BAZAAR_ROTATED("msg.bazaar.rotated"),
    MSG_NOT_ENOUGH_MONEY("msg.not-enough-money"),
    MSG_ITEM_NO_LONGER_AVAILABLE("msg.item.no-longer-available"),
    MSG_PRICE_CHANGED("msg.price.changed"),
    MSG_INVENTORY_FULL("msg.inventory.full"),
    MSG_PURCHASE_SUCCESS("msg.purchase.success"),
    MSG_PURCHASE_NOTIFY_OWNER("msg.purchase.notify-owner"),
    MSG_PURCHASE_CANCELLED("msg.purchase.cancelled"),
    MSG_BAZAAR_UPDATING("msg.bazaar.updating"),
    MSG_PLAYER_NOT_FOUND("msg.player-not-found"),
    MSG_AMOUNT_POSITIVE("msg.amount-positive"),
    MSG_BAG_GIVEN("msg.bag.given"),
    MSG_COMMAND_USAGE("msg.command-usage"),
    MSG_BAG_ADDED("msg.bag.added"),
    MSG_BAGS_ADDED("msg.bags.added"),
    MSG_TOO_CLOSE("msg.too-close"),
    MSG_BAZAAR_CREATED_ANNOUNCE("msg.bazaar.created-announce");

    private final String path;

    TranslationKey(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
