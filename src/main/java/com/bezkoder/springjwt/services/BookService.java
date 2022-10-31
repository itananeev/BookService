package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    List<Book> findBookByAuthor_Name(String name);
    List<Book> findBooksByPublishDate(String publishDate);

    Book createBook(Book b);
    Book updateBook(Long id, Book b);
    void deleteBook(Long id);
    boolean exists(Long id);
}
