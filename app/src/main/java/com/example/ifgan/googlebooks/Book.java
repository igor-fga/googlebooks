package com.example.ifgan.googlebooks;


public class Book {

    /** Name of the book */
    private String mTitle;

    /** Name of the author */
    private String mAuthor;

    /**
     *Constructs a new object.
     *
     * @param title is the name of the book
     */

    public Book (String title, String author)
    {
        mTitle = title;
        mAuthor = author;
    }

    /**
     * Returns the name of the book.
     */
    public String getName() {
        return mTitle;
    }

    /**
     * Returns the name of the author.
     */
    public String getAuthor() {
        return mAuthor;
    }

}
