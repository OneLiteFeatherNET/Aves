package de.icevizion.aves.bossbar;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;

public class BossBarBuilder {

    private BossBar bossBar;
    private BarColor barColor;
    private BarStyle barStyle;
    private BarFlag barFlag;

    public BossBarBuilder() {
        bossBar = Bukkit.createBossBar(null, BarColor.PURPLE, BarStyle.SEGMENTED_6);
    }

    public BossBarBuilder(NamespacedKey key) {
        bossBar = Bukkit.createBossBar(key,null, BarColor.PURPLE, BarStyle.SEGMENTED_6);
    }

    public BossBarBuilder(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    /**
     * Add a title to the BossBar
     * @param name The name for the BossBar
     * @return
     */

    public BossBarBuilder setTitle(String name) {
        bossBar.setTitle(name);
        return this;
    }

    /**
     * Set the color of the BossBar
     * @param color The color for the BossBar
     * @return
     */

    public BossBarBuilder setColor(BarColor color) {
        bossBar.setColor(color);
        return this;
    }

    /**
     * Set the style fo the BossBar
     * @param style The style for the BossBar
     * @return
     */

    public BossBarBuilder setStyle(BarStyle style) {
        bossBar.setStyle(style);
        return this;
    }

    /**
     * Set the visibility of the BossBar
     * @param visible The visibility state for the BossBar as boolean
     * @return
     */

    public BossBarBuilder setVisible(boolean visible) {
        bossBar.setVisible(visible);
        return this;
    }

    /**
     * Set the progress of the BossBar
     * @param progess The progress
     * @return
     */

    public BossBarBuilder setProgress(double progess) {
        if(Math.max(0.0, progess) == Math.min(progess, 1.0)) {
            bossBar.setProgress(progess);
        } else {
            throw new IllegalArgumentException("The value must be between 0.0 and 1");
        }
        return this;
    }

    /**
     * Add a flag to the BossBar
     * @param flag The specific flag for the BossBar
     * @return
     */

    public BossBarBuilder addFlag(BarFlag flag) {
        bossBar.addFlag(flag);
        return this;
    }

    /**
     * Add a player to be BossBar
     * @param player The player to add
     * @return
     */

    public BossBarBuilder withPlayer(Player player) {
        bossBar.addPlayer(player);
        return this;
    }

    /**
     * Add a list of player to the BossBar
     * @param players The list of players to add
     * @return
     */

    public BossBarBuilder withPlayers(List<Player> players) {
        for (Player player : players) {
            withPlayer(player);
        }
        return this;
    }

    /**
     * Returns the created BossBar
     * @return The underlying BossBar
     */

    public BossBar build() {
        return bossBar;
    }
}
