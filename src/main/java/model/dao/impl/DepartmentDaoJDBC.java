package model.dao.impl;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("insert into department (Name) values (?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, department.getName());

            int rows = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rows > 0){
                while (rs.next()){
                    System.out.println("Rows affected: " + rows + " Id gerenated: " + rs.getInt(1));
                }
            } else {
                System.out.println("No rows affected!");
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Rolled back! Caused by: " + e.getMessage());
            } catch (SQLException e1) {
                throw new DbIntegrityException("Cant rollback! Caused by: " + e1.getMessage());
            }
        } finally {
            DB.closePreparedStatement(ps);
            DB.closeResultset(rs);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("update department set Name = ? where id = ?");
            ps.setString(1, department.getName());
            ps.setInt(2, department.getId());

            int rows  = ps.executeUpdate();

            System.out.println("Rows affected: " + rows);

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Rolled back! Caused by: " + e.getMessage());
            } catch (SQLException e1) {
                throw new DbIntegrityException("Cant rollback! Caused by: " + e1.getMessage());
            }
        }finally {
            DB.closePreparedStatement(ps);
        }

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("delete from department where id = ?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if(rows == 0){
                throw new DbException("Delete error! No Id found!");
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Rolled back! Caused by: " + e.getMessage());
            } catch (SQLException e1) {
                throw new DbIntegrityException("Cant rollback! Caused by: " + e1.getMessage());
            }
        }finally {
            DB.closePreparedStatement(ps);
        }

    }

    @Override
    public Department findById(Integer id) {
        Department dep;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            rs = st.executeQuery("SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = " + id + " "
                    + "ORDER BY Name");

            if (rs.next()) {
                return dep = new Department(rs.getInt("DepartmentId"),
                        rs.getString("DepName"));
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new DbIntegrityException("Cant roll back! Caused by: " + e1.getMessage());
            }
            System.out.println("Rolled back! Caused by: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultset(rs);
        }
        return null;
    }

    @Override
    public List<Department> findAll() {
        List<Department> dep = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            rs = st.executeQuery("select * from department");

            while (rs.next()) {
                dep.add(new Department(rs.getInt("Id"),
                        rs.getString("Name")));
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Rolled back! Caused by: " + e.getMessage());
            } catch (SQLException e1) {
                throw new DbIntegrityException("Cant rollback! Caused by: " + e1.getMessage());
            }
        } finally {
            DB.closeStatement(st);
            DB.closeResultset(rs);
        }

        return dep;
    }
}
