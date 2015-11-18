
package com.datatable.servlet;

import com.datatable.dataTable.DataTableParameters;
import com.datatable.entities.Employee;
import com.datatable.facade.EmployeeFacade;
import com.datatable.util.DataTableParamModel;
import com.datatable.util.DataTablesParamUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ashok
 */
public class ServerProcessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @EJB 
    private EmployeeFacade employeeEJB;
    
    @Override
    public void init() {
        //Called once in lifecycle
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadData(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadData(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
    private void loadData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        List<Employee> listOfEmployee = employeeEJB.findAll();
        DataTableParamModel param = DataTablesParamUtility.getParameters(request);
        
        final int sortColumnIndex = param.iSortColumnIndex;
        final int sortDirection = param.sSortDirection.equals("asc") ? -1 : 1;        
        
        DataTableParameters dataTableParam = new DataTableParameters();
        List<Employee> filteredListOfEmployee = new LinkedList<>();

        /**
         * Filtering data according to search input
         */
        for(Employee e: listOfEmployee) {
            if (e.getFirstName().toLowerCase().contains(param.sSearch.toLowerCase())
                    || e.getLastName().toLowerCase().contains(param.sSearch.toLowerCase())
                    || e.getPosition().toLowerCase().contains(param.sSearch.toLowerCase())
                    || e.getOffice().toLowerCase().contains(param.sSearch.toLowerCase())) {
                filteredListOfEmployee.add(e);
            }
        }

        dataTableParam.setiTotalRecords(listOfEmployee.size());
        dataTableParam.setiTotalDisplayRecords(filteredListOfEmployee.size());
        
        /**
         * Sorting data
         */
        Collections.sort(filteredListOfEmployee, new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                switch (sortColumnIndex) {
                    case 1:
                        return e1.getFirstName().compareTo(e2.getFirstName()) * sortDirection;
                    case 2:
                        return e1.getLastName().compareTo(e2.getLastName()) * sortDirection;
                    case 3:                        
                        return e1.getPosition().compareTo(e2.getPosition()) * sortDirection;
                    case 4:
                        return e1.getOffice().compareTo(e2.getOffice()) * sortDirection;
                    case 5:
                        return e1.getStartDate().compareTo(e2.getStartDate()) * sortDirection;
                    case 6:
                        return Long.valueOf(e1.getSalary()).compareTo(Long.valueOf(e2.getSalary())) * sortDirection;
                }
                return 0;
            }
        });
        
        /**
         * Pagination of data
         */
        if (filteredListOfEmployee.size() < param.iDisplayStart + param.iDisplayLength) {
            filteredListOfEmployee = filteredListOfEmployee.subList(param.iDisplayStart, filteredListOfEmployee.size());
        } else {
            filteredListOfEmployee = filteredListOfEmployee.subList(param.iDisplayStart, param.iDisplayStart + param.iDisplayLength);
        }

        dataTableParam.setAaData(filteredListOfEmployee);
        dataTableParam.setsEcho(param.sEcho);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(dataTableParam);
        try (PrintWriter out = response.getWriter()) {
            out.print(json);
        }
    }
}
