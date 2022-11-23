package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepo extends JpaRepository<Book, Long> {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    //List<Book> findBookByAuthor_Name(String name);
    List<Book> findBooksByPublishDate(String publishDate);

    @Override
    <S extends Book> S save(S entity);
}
