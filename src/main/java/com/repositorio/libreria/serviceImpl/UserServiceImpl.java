package com.repositorio.libreria.serviceImpl;

import com.google.common.base.Strings;
import com.repositorio.libreria.JWT.CustomerUsersDetailsService;
import com.repositorio.libreria.JWT.JwtFilter;
import com.repositorio.libreria.JWT.JwtUtil;
import com.repositorio.libreria.Model.User;
import com.repositorio.libreria.constants.LibreriaConstants;
import com.repositorio.libreria.Repository.UserRepository;
import com.repositorio.libreria.service.UserService;
import com.repositorio.libreria.utils.LibreriaUtils;
import com.repositorio.libreria.utils.EmailUtils;
import com.repositorio.biblioteca.wrapper.UserWrapper;
import com.repositorio.libreria.constants.LibreriaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    // Se puede borrar
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Registro de usuario {}", requestMap);
        try {
            if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                    && requestMap.containsKey("email") && requestMap.containsKey("password")) {

                if (userRepository.findByEmailId(requestMap.get("email")) == null) {
                    User user = getUserFromMap(requestMap);
                    userRepository.save(user);

                    // Enviar credenciales al correo
                    String to = user.getEmail();
                    String subject = "Credenciales de acceso - Librería Crisol";
                    String htmlBody = "<p><strong>Hola " + user.getName() + ",</strong></p>"
                            + "<p>Gracias por registrarte en <strong>Librería Crisol</strong>. Estas son tus credenciales de acceso:</p>"
                            + "<p><strong>Correo electrónico:</strong> " + user.getEmail() + "<br>"
                            + "<strong>Contraseña:</strong> " + requestMap.get("password") + "</p>"
                            + "<p style=\"margin-top: 20px;\">Te recomendamos cambiar tu contraseña al iniciar sesión.</p>"
                            + "<a href=\"http://localhost:4200/\" style=\"display:inline-block;padding:10px 15px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;\">"
                            + "Iniciar sesión"
                            + "</a>"
                            + "<p style=\"margin-top: 20px;\">Saludos,<br>Equipo de Librería Crisol</p>";

                    emailUtils.sendEmail(to, subject, htmlBody);

                    return LibreriaUtils.getResponseEntity("Registro exitoso. Revisa tu correo.", HttpStatus.OK);
                }
                return LibreriaUtils.getResponseEntity("Ya existe el correo", HttpStatus.BAD_REQUEST);

            } else {
                return LibreriaUtils.getResponseEntity(LibreriaConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
       if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
               && requestMap.containsKey("email") && requestMap.containsKey("password")) {
           return true;
       }
       return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        //user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Login usuario");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()){
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(), customerUsersDetailsService.getUserDetail().getRole()) + "\"}",
                     HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<String>("{\"message\":\""+"Espere la aprobacion del administrador."+"\"}", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Credenciales erroneas"+"\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
          if (jwtFilter.isAdmin()){
             return new ResponseEntity<>(userRepository.getAllUser(), HttpStatus.OK);
          } else{
              return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
          }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {

            if (jwtFilter.isAdmin()){
              Optional<User> optional = userRepository.findById(Integer.parseInt(requestMap.get("id")));
              if (!optional.isEmpty()){
                userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userRepository.getAllAdmin());
                return LibreriaUtils.getResponseEntity("Actualizacion del estado del usuario exitosa", HttpStatus.OK);
              }
              else {
               LibreriaUtils.getResponseEntity("El ID del usuario no existe", HttpStatus.OK);
              }
            } else {
                return LibreriaUtils.getResponseEntity(LibreriaConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
      allAdmin.remove(jwtFilter.getCurrentUser());
      if (status!=null && status.equalsIgnoreCase("true")){
         emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Cuenta aprobada", "USUARIO: "+ user +" \n esta aprobado por \nADMINISTRADOR: " + jwtFilter.getCurrentUser(), allAdmin);
      } else {
          emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Cuenta deshabilitada", "USUARIO: "+ user +" \n esta deshabilitada por \nADMINISTRADOR: " + jwtFilter.getCurrentUser(), allAdmin);
      }
    }

    @Override
    public ResponseEntity<String> checkToken() {
       return LibreriaUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
          User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
          if (!userObj.equals(null)) {
            if (userObj.getPassword().equals(requestMap.get("oldPassword"))){
                  userObj.setPassword(requestMap.get("newPassword"));
                  userRepository.save(userObj);
                  return LibreriaUtils.getResponseEntity("Contraseña actualizada correctamente", HttpStatus.OK);
            }
            return LibreriaUtils.getResponseEntity("Contraseña antigua incorrecta", HttpStatus.BAD_REQUEST);
          }
          return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
          User user = userRepository.findByEmail(requestMap.get("email"));
          if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
              emailUtils.forgotMail(user.getEmail(), "Credenciales para Libreria Crisol", user.getPassword());
              return LibreriaUtils.getResponseEntity("Revisa tu correo para ver las credenciales", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

