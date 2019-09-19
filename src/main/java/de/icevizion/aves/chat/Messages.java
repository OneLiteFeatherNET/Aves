package de.icevizion.aves.chat;

public enum Messages {

    NO_TEAMS_ALLOWED("§cIn dieser Runde sind keine Teams erlaubt!"),
    TEAMS_ALLOWD("§cIn dieser Runde sind Teams erlaubt"),
    NOT_ENOUGH_PLAYERS("§4✖ §fZu wenige Spieler online §4✖"),
    SHOP_URL("Unseren Shop findest du unter: ");

    final String message;

    Messages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
