package com.repositorio.libreria.serviceImpl;

import com.google.common.base.Strings;
import com.repositorio.libreria.JWT.JwtFilter;
import com.repositorio.libreria.Model.Category;
import com.repositorio.libreria.constants.LibreriaConstants;
import com.repositorio.libreria.Repository.CategoryRepository;
import com.repositorio.libreria.service.CategoryService;
import com.repositorio.libreria.utils.LibreriaUtils;
import com.repositorio.libreria.constants.LibreriaConstants;
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
    CategoryRepository categoryRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
       try {
         if (jwtFilter.isAdmin()) {
           if (validateCategoryMap(requestMap, false)){
             categoryRepository.save(getCategoryFromMap(requestMap, false));
             return LibreriaUtils.getResponseEntity("Categoria agregada exitosamente", HttpStatus.OK);
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
           return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(), HttpStatus.OK);
        }
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
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
               Optional optional = categoryRepository.findById(Integer.parseInt(requestMap.get("id")));
               if (!optional.isEmpty()) {
                   categoryRepository.save(getCategoryFromMap(requestMap, true));
                   return LibreriaUtils.getResponseEntity("Categoria actualizada exitosamente", HttpStatus.OK);
               } else {
                   return LibreriaUtils.getResponseEntity("El ID de la categoria no existe", HttpStatus.OK);
               }
            }
            return LibreriaUtils.getResponseEntity(LibreriaConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
          }
          else {
              return LibreriaUtils.getResponseEntity(LibreriaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
          }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
