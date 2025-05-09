package com.repositorio.biblioteca.utils;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.google.common.base.Strings;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BibliotecaUtils {

    private BibliotecaUtils() {

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }

    public static String getUUID(){
        Date date = new Date();
        long time = date.getTime();
        return "FACTURA-" + time;
    }

    public static JsonArray getJsonArrayFromString(String data) throws JsonSyntaxException {
        return JsonParser.parseString(data).getAsJsonArray();
    }

    public static Map<String, Object> getMapFromJson(String data) {
        if (!Strings.isNullOrEmpty(data))
            return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
            }.getType());
        return new HashMap<>();
    }

    public static Boolean isFileExist(String path) {
        log.info("Dentro de isFileExist: {}", path);
        try {
            File file = new File(path);
            return file.exists();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


}