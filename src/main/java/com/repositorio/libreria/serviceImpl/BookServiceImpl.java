package com.repositorio.libreria.serviceImpl;

import com.repositorio.libreria.JWT.JwtFilter;
import com.repositorio.libreria.Model.Book;
import com.repositorio.libreria.Model.Category;
import com.repositorio.libreria.constants.LibreriaConstants;
import com.repositorio.libreria.Repository.BookRepository;
import com.repositorio.libreria.service.BookService;
import com.repositorio.libreria.utils.LibreriaUtils;
import com.repositorio.biblioteca.wrapper.BookWrapper;
import com.repositorio.libreria.constants.LibreriaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewBook(Map<String, String> requestMap) {
      try {
        if (jwtFilter.isAdmin()){
          if (validateBookMap(requestMap, false)){
            bookRepository.save(getBookFromMap(requestMap, false));
            return LibreriaUtils.getResponseEntity("Libro agregado exitosamente", HttpStatus.OK);
          }
          return LibreriaUtils.getResponseEntity(LibreriaConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
        else
            return LibreriaUtils.getResponseEntity(LibreriaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
      } catch (Exception ex) {
          ex.printStackTrace();
      }
      return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateBookMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Book getBookFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Book book = new Book();
        if (isAdd){
            book.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            book.setStatus("true");
        }

        book.setCategory(category);
        book.setName(requestMap.get("name"));
        book.setAuthor(requestMap.get("author"));
        book.setDescription(requestMap.get("description"));
        book.setIsbn(requestMap.get("isbn"));
        book.setPublisher(requestMap.get("publisher"));
        book.setCoverImage(requestMap.get("coverImage"));
        book.setPage(Integer.parseInt(requestMap.get("page")));
        book.setPrice(Integer.parseInt(requestMap.get("price")));
        return book;
    }

    @Override
    public ResponseEntity<List<BookWrapper>> getAllBook() {
        try {
          return new ResponseEntity<>(bookRepository.getAllBook(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateBook(Map<String, String> requestMap) {
        try {
           if (jwtFilter.isAdmin()){
             if (validateBookMap(requestMap, true)){
                Optional<Book> optional =  bookRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()){
                  Book book = getBookFromMap(requestMap, true);
                  book.setStatus(optional.get().getStatus());
                  bookRepository.save(book);
                  return LibreriaUtils.getResponseEntity("Libro actualizado exitosamente", HttpStatus.OK);
                }
                else {
                    return LibreriaUtils.getResponseEntity("El ID del libro no existe", HttpStatus.OK);
                }
             } else {
                 return LibreriaUtils.getResponseEntity(LibreriaConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
             }
           }
           else {
               return LibreriaUtils.getResponseEntity(LibreriaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
           }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteBook(Integer id) {
        try {
           if (jwtFilter.isAdmin()){
             Optional optional =  bookRepository.findById(id);
             if (!optional.isEmpty()){
                bookRepository.deleteById(id);
                return LibreriaUtils.getResponseEntity("Libro eliminado exitosamente", HttpStatus.OK);
             }
             return LibreriaUtils.getResponseEntity("El ID del libro no existe", HttpStatus.OK);
           } else {
               return LibreriaUtils.getResponseEntity(LibreriaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
           }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
          if (jwtFilter.isAdmin()){
           Optional optional =  bookRepository.findById(Integer.parseInt(requestMap.get("id")));
           if (!optional.isEmpty()){
               bookRepository.updateBookStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
               return LibreriaUtils.getResponseEntity("El estado del libro se actualizado exitosamente", HttpStatus.OK);
           }
           return LibreriaUtils.getResponseEntity("El ID del libro no existe", HttpStatus.OK);
          }
          else {
              return LibreriaUtils.getResponseEntity(LibreriaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
          }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<BookWrapper>> getByCategory(Integer id) {
        try {
          return new ResponseEntity<>(bookRepository.getBookByCategory(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<BookWrapper> getBookById(Integer id) {
        try {
           return new ResponseEntity<>(bookRepository.getBookById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new BookWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
