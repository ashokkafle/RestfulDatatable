
package com.datatable.model;

import java.util.Objects;
import javax.ws.rs.FormParam;

/**
 *
 * @author Ashok
 */
public class EmployeeForm {
    
    @FormParam("id")
    private String id;
    
    @FormParam("firstName")
    private String firstName;
    
    @FormParam("lastName")
    private String lastName;
    
    @FormParam("position")
    private String position;
    
    @FormParam("office")
    private String office;
    
    @FormParam("startDate")
    private String startDate;
    
    @FormParam("salary")
    private String salary;
    
    public EmployeeForm() {
    }

    public EmployeeForm(String id, String firstName, String lastName, String position, String office, String startDate, String salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.office = office;
        this.startDate = startDate;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.firstName);
        hash = 59 * hash + Objects.hashCode(this.lastName);
        hash = 59 * hash + Objects.hashCode(this.position);
        hash = 59 * hash + Objects.hashCode(this.office);
        hash = 59 * hash + Objects.hashCode(this.startDate);
        hash = 59 * hash + Objects.hashCode(this.salary);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmployeeForm other = (EmployeeForm) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        if (!Objects.equals(this.office, other.office)) {
            return false;
        }
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        if (!Objects.equals(this.salary, other.salary)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Employee {" + 
            "id = " + id + 
            ", firstName = " + firstName + 
            ", lastName = " + lastName + 
            ", position = " + position + 
            ", office = " + office + 
            ", startDate = " + startDate + 
            ", salary = " + salary + 
            '}';
    }       
}
