package com.repositorio.biblioteca.Repository;

import com.repositorio.biblioteca.Model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BillRepository extends JpaRepository<Bill, Integer> {

    List<Bill> getAllBills();

    List<Bill> getBillByUsername(@Param("username") String username);



}
