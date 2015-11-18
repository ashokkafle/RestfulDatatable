
package com.datatable.restApps;


import com.datatable.model.Employee;
import com.datatable.model.QueryRunner;
import java.sql.SQLException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.jboss.resteasy.annotations.Form;

/**
 *
 * @author Ashok
 */
@Path("/")
public class DatatableRestService {
    
    @POST
    @Path("/add")
    public void addEmployee(@Form Employee form) {        
        try {      
            QueryRunner qRunner = new QueryRunner();
            qRunner.insertEmployee(form);
        }
        catch(ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @POST
    @Path("/edit")
    public void editEmployee(@Form Employee form) {
        try {
            String ids = form.getId();
            String[] idArray = ids.split(",");
            String firstName = form.getFirstName();
            String lastName = form.getLastName();
            String position = form.getPosition();
            String office = form.getOffice();
            String startDate = form.getStartDate();
            String salary = form.getSalary();
            for(String s : idArray) {
                QueryRunner qRunner = new QueryRunner();
                Employee emp = qRunner.loadSingleEmployee(s);
                
                if(!firstName.equals("multiple")) {
                    emp.setFirstName(firstName);
                }
                if(!lastName.equals("multiple")) {
                    emp.setLastName(lastName);
                }
                if(!position.equals("multiple")) {
                    emp.setPosition(position);
                }
                if(!office.equals("multiple")) {
                    emp.setOffice(office);
                }
                if(!startDate.equals("multiple")) {
                    emp.setStartDate(startDate);
                }
                if(!salary.equals("multiple")) {
                    emp.setSalary(salary);
                }
                
                qRunner = new QueryRunner();
                qRunner.editEmployee(emp);
            }
        } 
        catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }       
    }
    
    @POST
    @Path("/delete")
    public void deleteEmployee(@Form Employee form) {
        try {
            String ids = form.getId();           
            String[] idArray = ids.split(",");            
            for(String s : idArray) {
                QueryRunner qRunner = new QueryRunner();
                qRunner.deleteEmployee(Integer.parseInt(s));
            }
        } 
        catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}