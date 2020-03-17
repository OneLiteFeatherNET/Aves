package de.icevizion.aves.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

/**
 *
 * Each book has a limit of 50 pages with 256 characters per page
 */

public final class BookBuilder extends ItemBuilder {

    public BookBuilder() {
        super(Material.WRITTEN_BOOK);
    }

    /**
     * Adds new pages to the end of the book
     * @param page A list of strings, each being a page
     * @return
     */

    public BookBuilder addPage(String... page) {
        BookMeta meta = getItemMeta();
        meta.addPage(page);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Add a new page to the book
     * @param side The page side
     * @param content The content of a page
     * @return
     */

    public BookBuilder addPage(int side, String content) {
        BookMeta meta = getItemMeta();
        meta.setPage(side, content);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Adds new pages to the end of the book
     * @param page A list of strings, each being a page
     * @return
     */

    public BookBuilder addPages(List<String> page) {
        BookMeta meta = getItemMeta();
        meta.setPages(page);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the author of the book
     * @param author The author to set
     * @return
     */

    public BookBuilder setAuthor(String author) {
        BookMeta meta = getItemMeta();
        meta.setAuthor(author);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the title of the book
     * @param title The title to set
     * @return
     */

    public BookBuilder setTitle(String title) {
        BookMeta meta = getItemMeta();
        meta.setTitle(title);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Override the default method to return another meta
     * @return The {@link BookMeta}
     */

    @Override
    protected BookMeta getItemMeta() {
        return (BookMeta) super.getItemMeta();
    }
}