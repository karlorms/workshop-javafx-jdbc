package model.dao;

import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public interface SellerDao {

    int insert(Seller seller);
    void update(Seller seller);
    void deleteById(Integer id);
    Seller findById(Integer id);

    List<Seller> findByDepartment(Department department);

    List<Seller> findAll();

    Seller findByName(String name);
}
