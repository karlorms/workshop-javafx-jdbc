package model.dao;

import model.entities.Department;

import java.util.List;

public interface DepartmentDao {

    int insert(Department department);
    void update(Department department);
    void deleteById(Integer id);
    Department findById(Integer id);
    Department findByName(String name);

    List<Department> findAll();
}
