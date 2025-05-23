package com.repositorio.libreria.ControllerImpl;

import com.repositorio.libreria.constants.LibreriaConstants;
import com.repositorio.libreria.Controller.UserController;
import com.repositorio.libreria.service.UserService;
import com.repositorio.libreria.utils.LibreriaUtils;
import com.repositorio.biblioteca.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserControllerImpl implements UserController {

    //private static final Logger logger = LoggerFactory.getLogger(UserControllerImpl.class);

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
      return LibreriaUtils.getResponseEntity(
              LibreriaConstants.SOMETHING_WENT_WRONG,
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
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
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
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
           return userService.checkToken();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
          return userService.changePassword(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
          return userService.forgotPassword(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
