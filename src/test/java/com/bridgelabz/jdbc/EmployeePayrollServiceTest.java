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

	@Test
	public void givenEmployeePayrollInDB_WhenDataRetrieved_ShouldMatchEmployeeCount()
			throws DatabaseConnectionException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData();
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenEmployeePayrollInDB_WhenUpdated_ShouldSyncWithDB() throws DatabaseConnectionException {
		List<EmployeePayrollData> employeePayrollData;
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollData = employeePayrollService.readEmployeePayrollData();
		employeePayrollService.updateEmployeeSalary("Terisa", 30000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}
	
	 @Test
	    public void givenEmployeePayrollDataWhenRetrievedBasedOnStartDateShouldReturnProperResult() throws DatabaseConnectionException {
				EmployeePayrollService employeePayrollService = new EmployeePayrollService();
				employeePayrollService.readEmployeePayrollData();
				LocalDate startDate = LocalDate.parse("2019-01-31");
				LocalDate endDate = LocalDate.parse("2020-01-31");
				List<EmployeePayrollData> matchingRecords = employeePayrollService
						.getEmployeePayrollDataByStartDate(startDate, endDate);
				Assert.assertEquals(matchingRecords.get(0), employeePayrollService.getEmployeePayrollData("Terisa"));
		}
	 
	 @Test
	 
	    public void givenEmployeePerformed_VariousOperations_ShouldGiveProperResult() throws DatabaseConnectionException {
			EmployeePayrollService employeePayrollService = new EmployeePayrollService();
			employeePayrollService.readEmployeePayrollData();
			Map<String, Double> averageSalaryByGender=employeePayrollService.performOperationByGender("salary","MAX");
			Assert.assertEquals(30000000.0, averageSalaryByGender.get("F"), 0.0);
	}
}
