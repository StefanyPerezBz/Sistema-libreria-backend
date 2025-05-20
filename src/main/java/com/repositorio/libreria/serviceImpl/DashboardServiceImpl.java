package com.repositorio.libreria.serviceImpl;

import com.repositorio.libreria.Repository.BillRepository;
import com.repositorio.libreria.Repository.BookRepository;
import com.repositorio.libreria.Repository.CategoryRepository;
import com.repositorio.libreria.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BillRepository billRepository;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();

        map.put("category", categoryRepository.count());
        map.put("book", bookRepository.count());
        map.put("bill", billRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
