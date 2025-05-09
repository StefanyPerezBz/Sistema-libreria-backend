package com.repositorio.biblioteca.serviceImpl;

import com.repositorio.biblioteca.dao.BillDao;
import com.repositorio.biblioteca.dao.BookDao;
import com.repositorio.biblioteca.dao.CategoryDao;
import com.repositorio.biblioteca.rest.DashboardRest;
import com.repositorio.biblioteca.service.BillService;
import com.repositorio.biblioteca.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BookDao bookDao;

    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();

        map.put("category", categoryDao.count());
        map.put("book", bookDao.count());
        map.put("bill", billDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
