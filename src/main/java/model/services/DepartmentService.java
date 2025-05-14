package model.services;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class DepartmentService {

    private DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    public int insertDepartment(Department department) {
        return departmentDao.insert(department);
    }

    public void updateDepartment(Department department){
            departmentDao.update(department);
    }

    public Department findById(Integer id){
        return departmentDao.findById(id);
    }

    public Department findByName(String name){
        return departmentDao.findByName(name);
    }

    public void deleteDepartment(Department department){
        departmentDao.deleteById(department.getId());
    }
}
