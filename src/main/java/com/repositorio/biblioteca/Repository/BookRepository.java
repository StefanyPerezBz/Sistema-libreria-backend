package com.repositorio.biblioteca.Repository;

import com.repositorio.biblioteca.Model.Book;
import com.repositorio.biblioteca.wrapper.BookWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<BookWrapper> getAllBook();

    @Modifying
    @Transactional
    Integer updateBookStatus(@Param("status") String status, @Param("id") Integer id);


    List<BookWrapper> getBookByCategory(@Param("id") Integer id);

    BookWrapper getBookById(@Param("id") Integer id);

}
