package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.Client;
import com.bezkoder.springjwt.models.Book;
import com.bezkoder.springjwt.payload.ResponseWithMessage;
import com.bezkoder.springjwt.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor

@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private Client client;

    @RabbitListener(queues = "${queue.name}")
    public String getBookTitle(String title) {
        return title;
    }

    @GetMapping
    public ResponseEntity<ResponseWithMessage<List<Book>>> getAll(){
        List<Book> books;
        try {
            books = bookService.findAll();
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Posts repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseWithMessage<>(books, null), HttpStatus.OK);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<ResponseWithMessage<Optional<Book>>> getById(@PathVariable Long id){
        Optional<Book> result;
        try {
            result = bookService.findById(id);
            getBookTitle(result.get().getTitle());
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Book repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ResponseWithMessage<>(result, null), HttpStatus.OK);
    }

//    @GetMapping(params = "name")
//    public ResponseEntity<ResponseWithMessage<List<Book>>> getByAuthor_Name(@RequestParam String name){
//        // TODO: Change this request to take the username from the cookie (case: corresponding user) or user has role admin
//        List<Book> results;
//        try {
//            results = bookService.findBookByAuthor_Name(name);
//        } catch (DataAccessException e) {
//            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Book repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(new ResponseWithMessage<>(results, null), HttpStatus.OK);
//    }

    @PostMapping
    public ResponseEntity<ResponseWithMessage<Book>> postBook(@CookieValue(name="bezkoder") String cookie, @RequestBody Book book){
        try {
            //put in search service
            String username = (String) client.sendMessageAndReceiveResponse(cookie, "roytuts");
            //
            book.setDescription(username);
            Book newBook = bookService.createBook(book);
            return new ResponseEntity<>(new ResponseWithMessage<>(newBook, "Book successfully created"), HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Book repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path="{id}/{b}")
    public ResponseEntity<ResponseWithMessage<Book>> editBook(@PathVariable Long id, @RequestBody Book b){
        // TODO: Authorize the user who created the answer is the same as this one requesting this action & exists in the database
        try {
            if(bookService.exists(id)) {
                Book updatedBook = bookService.updateBook(id, b);
                return new ResponseEntity<>(new ResponseWithMessage<>(updatedBook, "Book successfully updated"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Book not found"), HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Book repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path="{id}")
    public ResponseEntity<ResponseWithMessage<Book>> deleteBook(@PathVariable Long id) {
        // TODO: Authorize the user who created the answer is the same as this one requesting this action & exists in the database
        try {
            if (bookService.exists(id)) {
                bookService.deleteBook(id);
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Book successfully deleted"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseWithMessage<>(null, "Book not found"), HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Book repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseWithMessage<>(null, "Something went wrong..."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}