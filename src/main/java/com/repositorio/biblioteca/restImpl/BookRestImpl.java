package com.repositorio.biblioteca.restImpl;

import com.repositorio.biblioteca.constants.BibliotecaConstants;
import com.repositorio.biblioteca.rest.BookRest;
import com.repositorio.biblioteca.service.BookService;
import com.repositorio.biblioteca.utils.BibliotecaUtils;
import com.repositorio.biblioteca.wrapper.BookWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class BookRestImpl implements BookRest {

    @Autowired
    BookService bookService;

    @Override
    public ResponseEntity<String> addNewBook(Map<String, String> requestMap) {
        try {
           return bookService.addNewBook(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<BookWrapper>> getAllBook() {
        try {
          return bookService.getAllBook();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateBook(Map<String, String> requestMap) {
        try {
           return bookService.updateBook(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteBook(Integer id) {
        try {
          return bookService.deleteBook(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            return bookService.updateStatus(requestMap);
        } catch (Exception ex) {
             ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<BookWrapper>> getByCategory(Integer id) {
        try {
          return bookService.getByCategory(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<BookWrapper> getBookById(Integer id) {
        try {
          return bookService.getBookById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new BookWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
