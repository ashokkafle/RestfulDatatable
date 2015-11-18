
package com.datatable.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ashok
 */
public class QueryRunner {
    
    private Connection conn = null;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
    public QueryRunner() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");        
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data_table","root", "ashok");
    }
    
    public void insertEmployee(Employee emp) {
        try {
            String insertSql = "INSERT INTO `employee` (`first_name`, `last_name`, `position`, `office`, `start_date`, `salary`) "
                    + "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, emp.getFirstName());
            insertStmt.setString(2, emp.getLastName());
            insertStmt.setString(3, emp.getPosition());
            insertStmt.setString(4, emp.getOffice());
            insertStmt.setDate(5, new Date(formatter.parse(emp.getStartDate()).getTime()));
            insertStmt.setLong(6, Long.parseLong(emp.getSalary()));

            insertStmt.executeUpdate();
        }
        catch(SQLException | ParseException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                conn.close();
            }
            catch(SQLException e) {
                //do nothing
            }
        }
    }
    
    public Map<Integer, Employee> loadAllEmployee() {
        Map<Integer, Employee> listOfEmployee = new HashMap<>();
        try {
            String selectSql = "SELECT * FROM `employee`";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            ResultSet rs = selectStmt.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id")+"");
                emp.setFirstName(rs.getString("first_name"));
                emp.setLastName(rs.getString("last_name"));
                emp.setPosition(rs.getString("position"));
                emp.setOffice(rs.getString("office"));
                emp.setStartDate(rs.getDate("start_date").toString());
                emp.setSalary(rs.getString("salary"));
                listOfEmployee.put(rs.getInt("id"), emp);
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                conn.close();
            }
            catch(SQLException e) {
                //do nothing
            }
        }
        return listOfEmployee;
    }
    
    public void deleteEmployee(int id) {
        try {
            String deleteSql = "DELETE FROM `employee` WHERE `id` = ? LIMIT 1";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setInt(1, id);
            
            deleteStmt.executeUpdate();
        } 
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                conn.close();
            }
            catch(SQLException e) {
                //do nothing
            }
        }
    }
    
    public void editEmployee(Employee emp) {
        try {
            String insertSql = "UPDATE `employee` SET `first_name` = ?, `last_name` = ?, `position` = ?, `office` = ?, `start_date` = ?, `salary` = ? "
                    + "WHERE `id` = ? LIMIT 1";
            PreparedStatement updateStmt = conn.prepareStatement(insertSql);
            updateStmt.setString(1, emp.getFirstName());
            updateStmt.setString(2, emp.getLastName());
            updateStmt.setString(3, emp.getPosition());
            updateStmt.setString(4, emp.getOffice());
            updateStmt.setDate(5, new Date(formatter.parse(emp.getStartDate()).getTime()));
            updateStmt.setLong(6, Long.parseLong(emp.getSalary()));
            updateStmt.setInt(7, Integer.parseInt(emp.getId()));

            updateStmt.executeUpdate();
        }
        catch(SQLException | ParseException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                conn.close();
            }
            catch(SQLException e) {
                //do nothing
            }
        }
    }
    
    public Employee loadSingleEmployee(String id) {
        Employee emp = new Employee();
        try {
            String selectSql = "SELECT * FROM `employee` WHERE `id` = ? LIMIT 1";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = selectStmt.executeQuery();
            while (rs.next()) {
                emp.setId(rs.getInt("id")+"");
                emp.setFirstName(rs.getString("first_name"));
                emp.setLastName(rs.getString("last_name"));
                emp.setPosition(rs.getString("position"));
                emp.setOffice(rs.getString("office"));
                emp.setStartDate(rs.getDate("start_date").toString());
                emp.setSalary(rs.getString("salary"));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                conn.close();
            }
            catch(SQLException e) {
                //do nothing
            }
        }
        return emp;
    }
}
