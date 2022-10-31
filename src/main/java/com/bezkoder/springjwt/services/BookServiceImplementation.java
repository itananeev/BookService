package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bezkoder.springjwt.repository.BooksRepo;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImplementation implements BookService {
    @Autowired
    private final BooksRepo booksRepo;

    public BookServiceImplementation(BooksRepo booksRepo){
        this.booksRepo = booksRepo;
    }

    @Override
    public List<Book> findAll(){
        return booksRepo.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return booksRepo.findById(id); }

    @Override
    public List<Book> findBookByAuthor_Name(String author) {
        return booksRepo.findBookByAuthor_Name(author); }

    @Override
    public List<Book> findBooksByPublishDate(String publishDate){
        return booksRepo.findBooksByPublishDate(publishDate);
    }

    @Override
    public Book createBook(Book b){
        return booksRepo.save(b);
    }

    @Override
    public Book updateBook(Long id, Book b){
        Optional<Book> book = findById(id);
        if(book.isPresent()){
            book.get().setDescription(b.getDescription());
            book.get().setTitle(b.getTitle());
            book.get().setPublishDate(b.getPublishDate());
            return booksRepo.save(book.get());
        }
        else{
            return null;
        }
    }

    @Override
    public void deleteBook(Long id){
        booksRepo.deleteById(id);
    }

    @Override
    public boolean exists(Long id){
        return booksRepo.existsById(id);
    }
}
