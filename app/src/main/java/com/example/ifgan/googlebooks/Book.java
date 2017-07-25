package com.example.ifgan.googlebooks;


public class Book {

    /** Name of the book */
    private String mName;

    /**
     *Constructs a new object.
     *
     * @param name is the name of the book
     */

    public Book (String name)
    {
        mName = name;
    }

    /**
     * Returns the name of the book.
     */
    public String getName() {
        return mName;
    }

}
