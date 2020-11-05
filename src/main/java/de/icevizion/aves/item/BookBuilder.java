package de.icevizion.aves.item;

import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Each book has a limit of 50 pages with 256 characters per page
 */

public final class BookBuilder extends ItemBuilder {

    private final BookMeta bookMeta;

    public BookBuilder() {
        super(Material.WRITTEN_BOOK);
        bookMeta = getItemMeta();
    }

    /**
     * Adds new pages to the end of the book
     * @param page A list of strings, each being a page
     * @return
     */

    public BookBuilder addPage(String... page) {
        int length = 0;
        for (int i = 0; i < page.length; i++) {
            length += page[i].length();
        }

        Preconditions.checkArgument(length > 255, "The maximum limit of characters per page is 255");

        bookMeta.addPage(page);
        stack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * Add a new page to the book
     * @param side The page side
     * @param content The content of a page
     * @return
     */

    public BookBuilder addPage(int side, String content) {
        Preconditions.checkArgument(side > 100, "The maximum limit of pages in a book is 100");
        Preconditions.checkArgument(content.length() > 255, "The maximum limit of characters per page is 255");
        bookMeta.setPage(side, content);
        stack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * Adds new pages to the end of the book
     * @param page A list of strings, each being a page
     * @return
     */

    public BookBuilder addPages(List<String> page) {
        Preconditions.checkArgument(page.size() > 100, "The maximum limit of pages in a book is 100");
        bookMeta.setPages(page);
        stack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * Clears all pages from a book.
     * @return
     */

    public BookBuilder clearPages() {
        bookMeta.setPages(new ArrayList<>());
        stack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * Sets the author of the book
     * @param author The author to set
     * @return
     */

    public BookBuilder setAuthor(String author) {
        bookMeta.setAuthor(author);
        stack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * Sets the title of the book
     * @param title The title to set
     * @return
     */

    public BookBuilder setTitle(String title) {
        bookMeta.setTitle(title);
        stack.setItemMeta(bookMeta);
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