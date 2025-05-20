package com.repositorio.libreria.ControllerImpl;

import com.repositorio.libreria.Model.Bill;
import com.repositorio.libreria.constants.LibreriaConstants;
import com.repositorio.libreria.Controller.BillController;
import com.repositorio.libreria.service.BillService;
import com.repositorio.libreria.utils.LibreriaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BillControllerImpl implements BillController {

    @Autowired
    BillService billService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
          return billService.generateReport(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            return billService.getBills();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            return billService.getPdf(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
           return billService.deleteBill(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return LibreriaUtils.getResponseEntity(LibreriaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
