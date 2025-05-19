package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public int insert(Seller seller) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("insert into seller "
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                    + "values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, seller.getName());
            ps.setString(2, seller.getEmail());
            ps.setDate(3, Date.valueOf(seller.getBirthDate()));
            ps.setDouble(4, seller.getBaseSalary());
            ps.setInt(5, seller.getDepartment().getId());

            int rows = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rows > 0){
                while (rs.next()){
                    return rs.getInt(1);
                }
            } else {
                throw new DbException("Unexpected error! No rows affected!");

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
        return 0;
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("update seller "
                    + "set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                    + "where Id = ?");

            ps.setString(1, seller.getName());
            ps.setString(2, seller.getEmail());
            ps.setDate(3, Date.valueOf(seller.getBirthDate()));
            ps.setDouble(4, seller.getBaseSalary());
            ps.setInt(5, seller.getDepartment().getId());
            ps.setInt(6, seller.getId());

            int rows = ps.executeUpdate();

            System.out.println("Rows affected: " + rows);

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
        }

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement("delete from seller where id = ?");
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
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
    public Seller findById(Integer id) {
        Seller seller = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            rs = st.executeQuery("SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = " + id);

            if (rs.next()) {
                return seller = new Seller(rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getDouble("BaseSalary"),
                        new Department(rs.getInt("DepartmentId"), rs.getString("DepName")));
            }
            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Rolled back! Caused by: " + e.getMessage());
            } catch (SQLException e1) {
                throw new DbIntegrityException("Cant rollback! Caused by: " + e1.getMessage());
            } finally {
                DB.closeStatement(st);
                DB.closeResultset(rs);
            }
        }
        return null;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        List<Seller> seller = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            rs = st.executeQuery("SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = " + department.getId() + " "
                    + "ORDER BY Name");

            Map<Integer, Department> map = new HashMap<>();
            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = new Department(rs.getInt("DepartmentId"),
                            rs.getString("DepName"));
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                seller.add(new Seller(rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getDouble("BaseSalary"),
                        dep));
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
        //seller.forEach(System.out::println);
        return seller;
    }

    @Override
    public List<Seller> findAll() {
        Statement st = null;
        ResultSet rs = null;
        List<Seller> seller = new ArrayList<>();
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            rs = st.executeQuery("SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "ORDER BY Name");

            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = new Department(rs.getInt("DepartmentId"),
                            rs.getString("DepName"));
                }

                seller.add(new Seller(rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getDouble("BaseSalary"),
                        dep));

                conn.commit();
            }
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

        return seller;
    }

    @Override
    public Seller findByName(String name) {
        return null;
    }
}
