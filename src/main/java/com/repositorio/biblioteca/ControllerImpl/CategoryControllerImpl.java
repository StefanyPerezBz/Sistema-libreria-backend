package com.repositorio.biblioteca.ControllerImpl;

import com.repositorio.biblioteca.Model.Category;
import com.repositorio.biblioteca.constants.BibliotecaConstants;
import com.repositorio.biblioteca.Controller.CategoryController;
import com.repositorio.biblioteca.service.CategoryService;
import com.repositorio.biblioteca.utils.BibliotecaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryControllerImpl implements CategoryController {

    @Autowired
    CategoryService categoryService;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
       try {
         return categoryService.addNewCategory(requestMap);
       } catch (Exception ex) {
         ex.printStackTrace();
       }
       return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
          return categoryService.getAllCategory(filterValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
          return categoryService.updateCategory(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
