package com.repositorio.biblioteca.wrapper;

import com.repositorio.biblioteca.POJO.Book;
import lombok.Data;

@Data
public class BookWrapper {

    Integer id;
    String name;
    String author;
    String coverImage;
    String isbn;
    String description;
    String publisher;
    Integer categoryId;
    String categoryName;
    String status;
    Integer price;
    Integer page;

    public BookWrapper() {

    }

    public BookWrapper(Integer id, String name, String author, String description, String isbn, String publisher,
                       String coverImage, Integer page, Integer price, String status,
                       Integer categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.coverImage = coverImage;
        this.isbn = isbn;
        this.description = description;
        this.publisher = publisher;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
        this.price = price;
        this.page = page;

    }

    public BookWrapper(Integer id, String name){
         this.id = id;
         this.name = name;
    }

    public BookWrapper(Integer id, String name, String author, String description, String isbn, String publisher,
                       String coverImage, Integer page, Integer price){
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.isbn = isbn;
        this.publisher = publisher;
        this.coverImage = coverImage;
        this.page = page;
        this.price = price;

    }
}
