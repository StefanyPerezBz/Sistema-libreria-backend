package com.repositorio.biblioteca.serviceImpl;

import com.google.gson.JsonArray;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.repositorio.biblioteca.JWT.JwtFilter;
import com.repositorio.biblioteca.Model.Bill;
import com.repositorio.biblioteca.constants.BibliotecaConstants;
import com.repositorio.biblioteca.Repository.BillRepository;
import com.repositorio.biblioteca.service.BillService;
import com.repositorio.biblioteca.utils.BibliotecaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillRepository billRepository;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Generando informe para factura");
        try {
            String fileName;
            if (validateRequestMap(requestMap)){
                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                    fileName = (String) requestMap.get("uuid");
                } else {
                    fileName = BibliotecaUtils.getUUID();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }

                String data = "Nombre: "+requestMap.get("name") +"\n"
                        +"Numero de contacto: " +requestMap.get("contactNumber") + "\n"
                        + "Corre electronico: "+requestMap.get("email") +"\n"
                        +"Metodo de pago: "+requestMap.get("paymentMethod");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(BibliotecaConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));
                document.open();
                setRectangleInPdf(document);

                Paragraph chunk = new Paragraph("Sistema de Gestion de Biblioteca", getFont("Header"));
                chunk.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
                document.add(paragraph);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JsonArray jsonArray = BibliotecaUtils.getJsonArrayFromString((String) requestMap.get("bookDetails"));


                for (int i = 0; i < jsonArray.size(); i++) {
                    addRows(table, BibliotecaUtils.getMapFromJson(jsonArray.get(i).getAsJsonObject().toString()));
                }
                document.add(table);

                Paragraph footer = new Paragraph("Total : " +requestMap.get("totalAmount")+ "\n" +
                        "Gracias por visitar. Vuelva a visitarnos de nuevo", getFont("Data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
            }
            return BibliotecaUtils.getResponseEntity("Datos requeridos no encontrados", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if (jwtFilter.isAdmin()) {
            list = billRepository.getAllBills();
        } else {
          list = billRepository.getBillByUsername(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Generando pdf para factura", requestMap);
        try {
           byte[]  byteArray  = new byte[0];
           if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap))
               return  new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
               String filePath = BibliotecaConstants.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";
               if (BibliotecaUtils.isFileExist(filePath)) {
                 byteArray = getByteArray(filePath);
                 return new ResponseEntity<>(byteArray, HttpStatus.OK);
               } else {
                   requestMap.put("isGenerate", false);
                   generateReport(requestMap);
                   byteArray = getByteArray(filePath);
                   return new ResponseEntity<>(byteArray, HttpStatus.OK);
               }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    private byte[] getByteArray(String filePath) throws Exception {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Generando columnas");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Generando tablas");
        Stream.of("Nombre", "Categoria", "Cantidad", "Precio", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell(new Phrase(columnTitle));
                    header.setBackgroundColor(Color.ORANGE);
                    header.setBorderWidth(2);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(header);
                });
    }


    private Font getFont(String type) {
        log.info("Generando font");
        switch (type){
            case "Header":  // Corregido el nombre de "Hader" a "Header"
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, Color.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;

            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Color.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;

            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Generando pdf para factura");
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBackgroundColor(Color.WHITE);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid( (String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
            bill.setBookDetail((String) requestMap.get("bookDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billRepository.save(bill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("bookDetails") &&
                requestMap.containsKey("totalAmount");
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            Optional optional = billRepository.findById(id);
            if (!optional.isEmpty()){
              billRepository.deleteById(id);
              return BibliotecaUtils.getResponseEntity("La factura ha sido eliminada exitosamente", HttpStatus.OK);
            }
            return BibliotecaUtils.getResponseEntity("El ID de la factura no existe", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BibliotecaUtils.getResponseEntity(BibliotecaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
