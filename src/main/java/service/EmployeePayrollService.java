
package service;

import java.util.List;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;

public class EmployeePayrollService {

	public EmployeePayrollDBService employeePayrollDBService;

	public EmployeePayrollService() {
		this.employeePayrollDBService = new EmployeePayrollDBService();
	}
	public List<EmployeePayrollData> readEmployeePayrollData() throws DatabaseConnectionException{
		return this.employeePayrollDBService.readData();
	}
}
