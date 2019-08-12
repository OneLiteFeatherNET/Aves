package de.cosmiqglow.aves.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public final class BookBuilder extends ItemBuilder {

    public BookBuilder() {
        super(Material.WRITTEN_BOOK);
    }

    public BookBuilder addPage(String... page) {
        BookMeta meta = getItemMeta();
        meta.addPage(page);
        stack.setItemMeta(meta);
        return this;
    }

    public BookBuilder setGeneration(BookMeta.Generation generation) {
        BookMeta meta = getItemMeta();
        meta.setGeneration(generation);
        stack.setItemMeta(meta);
        return this;
    }

    public BookBuilder addPage(int side, String content) {
        BookMeta meta = getItemMeta();
        meta.setPage(side, content);
        stack.setItemMeta(meta);
        return this;
    }

    public BookBuilder addPages(List<String> page) {
        BookMeta meta = getItemMeta();
        meta.setPages(page);
        stack.setItemMeta(meta);
        return this;
    }

    public BookBuilder setAuthor(String author) {
        BookMeta meta = getItemMeta();
        meta.setAuthor(author);
        stack.setItemMeta(meta);
        return this;
    }

    public BookBuilder setTitle(String title) {
        BookMeta meta = getItemMeta();
        meta.setTitle(title);
        stack.setItemMeta(meta);
        return this;
    }

    @Override
    protected BookMeta getItemMeta() {
        return (BookMeta) super.getItemMeta();
    }
}