package com.repositorio.biblioteca.serviceImpl;

import com.google.common.base.Strings;
import com.repositorio.biblioteca.JWT.JwtFilter;
import com.repositorio.biblioteca.POJO.Category;
import com.repositorio.biblioteca.constants.BibliotecaConstants;
import com.repositorio.biblioteca.dao.CategoryDao;
import com.repositorio.biblioteca.service.CategoryService;
import com.repositorio.biblioteca.utils.BibliotecaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
       try {
         if (jwtFilter.isAdmin()) {
           if (validateCategoryMap(requestMap, false)){
             categoryDao.save(getCategoryFromMap(requestMap, false));
             return BibliotecaUtils.getResponseEntity("Categoria agregada exitosamente", HttpStatus.OK);
           }
         }
         else {
            return BibliotecaUtils.getResponseEntity(BibliotecaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
         }
       } catch (Exception ex) {
           ex.printStackTrace();
       }
       return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
       if (requestMap.containsKey("name")) {
           if (requestMap.containsKey("id") && validateId) {
               return true;
           } else if (!validateId) {
               return true;
           }
       }
       return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
      Category category = new Category();
      if (isAdd) {
          category.setId(Integer.parseInt(requestMap.get("id")));
      }
      category.setName(requestMap.get("name"));
      return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
      try {
        if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
           log.info("Dentro de categoria");
           return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
        }
        return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
      } catch (Exception ex) {
          ex.printStackTrace();
      }
      return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
          if (jwtFilter.isAdmin()) {
            if (validateCategoryMap(requestMap, true)) {
               Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
               if (!optional.isEmpty()) {
                   categoryDao.save(getCategoryFromMap(requestMap, true));
                   return BibliotecaUtils.getResponseEntity("Categoria actualizada exitosamente", HttpStatus.OK);
               } else {
                   return BibliotecaUtils.getResponseEntity("El ID de la categoria no existe", HttpStatus.OK);
               }
            }
            return BibliotecaUtils.getResponseEntity(BibliotecaConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
          }
          else {
              return BibliotecaUtils.getResponseEntity(BibliotecaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
          }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
