package com.repositorio.libreria.Repository;

import com.repositorio.libreria.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> getAllCategory();
}
