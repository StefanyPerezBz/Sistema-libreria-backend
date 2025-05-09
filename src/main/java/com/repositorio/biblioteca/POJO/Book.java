package com.repositorio.biblioteca.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Book.getAllBook",
query = "select new com.repositorio.biblioteca.wrapper.BookWrapper(p.id, p.name, p.author, p.description, p.isbn, p.publisher, p.coverImage, p.page, p.price, p.status, p.category.id, p.category.name) from Book p")

@NamedQuery(name = "Book.updateBookStatus",
query = "update Book p set p.status=:status where p.id=:id")

@NamedQuery(name = "Book.getBookByCategory",
query = "select  new com.repositorio.biblioteca.wrapper.BookWrapper(p.id, p.name) from Book p where p.category.id=:id and p.status='true'")

@NamedQuery(name = "Book.getBookById",
query = "select new com.repositorio.biblioteca.wrapper.BookWrapper(p.id, p.name, p.author, p.description, p.isbn, p.publisher, p.coverImage, p.page, p.price) from Book p where p.id=:id")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "book")
public class Book implements Serializable {

    private static final long serialVersionUid = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "coverImage")
    private String coverImage;

    @Column(name = "page")
    private Integer page;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status")
    private String status;
}
