package com.repositorio.biblioteca.dao;

import com.repositorio.biblioteca.POJO.Book;
import com.repositorio.biblioteca.wrapper.BookWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookDao extends JpaRepository<Book, Integer> {

    List<BookWrapper> getAllBook();

    @Modifying
    @Transactional
    Integer updateBookStatus(@Param("status") String status, @Param("id") Integer id);


    List<BookWrapper> getBookByCategory(@Param("id") Integer id);

    BookWrapper getBookById(@Param("id") Integer id);

}
