package com.example.ifgan.googlebooks;


public class Book {

    /** Name of the book */
    private String mTitle;

    /**
     *Constructs a new object.
     *
     * @param title is the name of the book
     */

    public Book (String title)
    {
        mTitle = title;
    }

    /**
     * Returns the name of the book.
     */
    public String getName() {
        return mTitle;
    }

}
