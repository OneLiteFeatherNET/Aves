package de.cosmiqglow.aves.item;

import de.cosmiqglow.aves.item.util.CursorDirection;
import de.cosmiqglow.aves.item.util.CursorType;
import de.cosmiqglow.aves.item.util.MapText;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class RenderMapBuilder extends MapBuilder {

    private World world;
    private MapView map;
    private BufferedImage image;
    private List<MapText> texts;
    private MapCursorCollection cursors;
    private boolean rendered;
    private boolean staticRender;

    public RenderMapBuilder(MapType type) {
        super(type);
        this.cursors = new MapCursorCollection();
        this.texts = new ArrayList<>();
    }

    public RenderMapBuilder setWorld(World world) {
        this.world = world;
        return this;
    }

    public RenderMapBuilder setImage(BufferedImage image) {
        this.image = image;
        return this;
    }

    public RenderMapBuilder addText(int x, int y, MapFont font, String text) {
        this.texts.add(new MapText(x, y, font, text));
        return this;
    }

    public RenderMapBuilder addCursor(int x, int y, CursorDirection direction, CursorType type) {
        cursors.addCursor(x, y, (byte) direction.getId(), (byte) type.getId());
        return this;
    }

    public RenderMapBuilder setRenderOnce(boolean staticRender) {
        this.staticRender = staticRender;
        return this;
    }

    @Override
    public ItemStack build() {
        List<MapRenderer> old = map.getRenderers();
        map = Bukkit.createMap(Bukkit.getWorlds().get(0));
        map.setScale(MapView.Scale.NORMAL);
        addRenderer(new MapRenderer() {
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                if (rendered && staticRender)
                    return;
                if (player == null || !player.isOnline()) {
                    old.forEach(map::addRenderer);
                } else {
                    if (image != null)
                        mapCanvas.drawImage(0, 0, image);
                    texts.forEach(text -> mapCanvas.drawText(text.getX(), text.getY(), text.getFont(), text.getString()));
                    mapCanvas.setCursors(cursors);
                    rendered = true;
                }
            }
        });

        return stack;
    }
}
