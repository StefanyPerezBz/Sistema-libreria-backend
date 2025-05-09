package com.repositorio.biblioteca.service;

import com.repositorio.biblioteca.wrapper.BookWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface BookService {

    ResponseEntity<String> addNewBook(Map<String, String> requestMap);

    ResponseEntity<List<BookWrapper>> getAllBook();

    ResponseEntity<String> updateBook(Map<String, String> requestMap);

    ResponseEntity<String> deleteBook(Integer id);

    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    ResponseEntity<List<BookWrapper>> getByCategory(Integer id);

    ResponseEntity<BookWrapper> getBookById(Integer id);
}
