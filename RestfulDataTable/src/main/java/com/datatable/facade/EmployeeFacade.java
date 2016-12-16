package com.datatable.facade;

import com.datatable.entities.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ashok
 */
@Stateless
public class EmployeeFacade extends AbstractFacade<Employee> {

	@PersistenceContext(unitName = "com_RestfulDataTable_PU")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public EmployeeFacade() {
		super(Employee.class);
	}

}
