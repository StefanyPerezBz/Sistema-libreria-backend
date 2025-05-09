package com.repositorio.biblioteca.restImpl;

import com.repositorio.biblioteca.constants.BibliotecaConstants;
import com.repositorio.biblioteca.rest.UserRest;
import com.repositorio.biblioteca.service.UserService;
import com.repositorio.biblioteca.utils.BibliotecaUtils;
import com.repositorio.biblioteca.wrapper.UserWrapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    //private static final Logger logger = LoggerFactory.getLogger(UserRestImpl.class);

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.error("Error durante el proceso de registro de usuario", ex);
        }
      return BibliotecaUtils.getResponseEntity(
              BibliotecaConstants.SOMETHING_WENT_WRONG,
              HttpStatus.INTERNAL_SERVER_ERROR
      );
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
             return userService.login(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>( new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
          return userService.update(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
           return userService.checkToken();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
          return userService.changePassword(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
          return userService.forgotPassword(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
