package com.datatable.restApps;

import com.datatable.facade.EmployeeFacade;
import com.datatable.entities.Employee;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Ashok
 */
@Stateless
@Path("/")
public class DatatableRestService {

	@EJB
	private EmployeeFacade employeeEJB;

	@POST
	@Path("/add")
	public void addEmployee(
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("position") String position,
			@FormParam("office") String office,
			@FormParam("startDate") String startDate,
			@FormParam("salary") String salary
	) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Employee emp = new Employee(null, firstName, lastName, position, office, format.parse(startDate), Long.parseLong(salary));
			employeeEJB.create(emp);
		} catch (ParseException | NumberFormatException e) {
			System.out.println(e.getMessage());
		}
	}

	@POST
	@Path("/edit")
	public void editEmployee(
			@FormParam("ids") String ids,
			@FormParam("firstName") String firstName,
			@FormParam("lastName") String lastName,
			@FormParam("position") String position,
			@FormParam("office") String office,
			@FormParam("startDate") String startDate,
			@FormParam("salary") String salary
	) {
		try {
			String[] idArray = ids.split(",");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			for (String id : idArray) {
				Employee emp = employeeEJB.find(Integer.parseInt(id));

				if (!firstName.equals("multiple")) {
					emp.setFirstName(firstName);
				}
				if (!lastName.equals("multiple")) {
					emp.setLastName(lastName);
				}
				if (!position.equals("multiple")) {
					emp.setPosition(position);
				}
				if (!office.equals("multiple")) {
					emp.setOffice(office);
				}
				if (!startDate.equals("multiple")) {
					emp.setStartDate(format.parse(startDate));
				}
				if (!salary.equals("multiple")) {
					emp.setSalary(Long.parseLong(salary));
				}

				employeeEJB.edit(emp);
			}
		} catch (ParseException | NumberFormatException e) {
			System.out.println(e.getMessage());
		}
	}

	@POST
	@Path("/delete")
	public void deleteEmployee(@FormParam("ids") String ids) {
		try {
			String[] idArray = ids.split(",");
			for (String id : idArray) {
				Employee emp = employeeEJB.find(Integer.parseInt(id));
				employeeEJB.remove(emp);
			}
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
	}
}
