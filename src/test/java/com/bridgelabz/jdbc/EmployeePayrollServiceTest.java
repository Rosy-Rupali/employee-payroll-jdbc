/********************************************************************************************
 * @Purpose : Test Cases for Employee Payroll Database Service program to 
 * perform various operations in database. 
 * @author Rosy Rupali
 * @version 1.0
 * @since 07-07-2021
 ************************************************************************************************/
package com.bridgelabz.jdbc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;
import service.EmployeePayrollService;

public class EmployeePayrollServiceTest {

	// UC2
	@Test
	public void givenEmployeePayrollInDB_WhenDataRetrieved_ShouldMatchEmployeeCount()
			throws DatabaseConnectionException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData();
		Assert.assertEquals(3, employeePayrollData.size());
	}

	// UC3
	@Test
	public void givenEmployeePayrollInDB_WhenUpdated_ShouldSyncWithDB() throws DatabaseConnectionException {
		List<EmployeePayrollData> employeePayrollData;
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollData = employeePayrollService.readEmployeePayrollData();
		employeePayrollService.updateEmployeeSalary("Terisa", 30000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}

	// UC4
	@Test
	public void givenEmployeePayrollInDB_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB()
			throws DatabaseConnectionException {
		List<EmployeePayrollData> employeePayrollData;
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollData = employeePayrollService.readEmployeePayrollData();
		employeePayrollService.updateEmployeeSalary("Terisa", 300000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}

	// UC5
	@Test
	public void givenEmployeePayrollDataWhenRetrievedBasedOnStartDateShouldReturnProperResult()
			throws DatabaseConnectionException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData();
		LocalDate startDate = LocalDate.parse("2019-01-31");
		LocalDate endDate = LocalDate.parse("2020-01-31");
		List<EmployeePayrollData> matchingRecords = employeePayrollService.getEmployeePayrollDataByStartDate(startDate,
				endDate);
		Assert.assertEquals(matchingRecords.get(0), employeePayrollService.getEmployeePayrollData("Terisa"));
	}

	// UC6
	@Test

	public void givenEmployeePerformed_VariousOperations_ShouldGiveProperResult() throws DatabaseConnectionException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData();
		Map<String, Double> averageSalaryByGender = employeePayrollService.performOperationByGender("salary", "MAX");
		Assert.assertEquals(30000000.0, averageSalaryByGender.get("F"), 0.0);
	}

	// UC7 and UC8
	@Test
	public void givenNewEmployee_WhenAdded_ShouldGiveProperResult() throws DatabaseConnectionException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData();
		employeePayrollService.addEmployeeToPayroll("Mark", 5000000.00, "M", LocalDate.now());
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
		Assert.assertTrue(result);
	}
}
