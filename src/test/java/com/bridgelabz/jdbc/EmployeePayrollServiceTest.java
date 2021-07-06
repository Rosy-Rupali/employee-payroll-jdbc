
package com.bridgelabz.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;
import service.EmployeePayrollService;

public class EmployeePayrollServiceTest {
	 @Test
	    public void givenEmployeePayrollInDB_WhenDataRetrieved_ShouldMatchEmployeeCount() throws DatabaseConnectionException{
	        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
	        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData();
	        Assert.assertEquals(4, employeePayrollData.size());
	    }

}
