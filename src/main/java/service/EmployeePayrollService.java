
package service;

import java.util.List;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;

public class EmployeePayrollService {
	public EmployeePayrollDBService employeePayrollDBService;
	private List<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService() {
		this.employeePayrollDBService = new EmployeePayrollDBService();
	}

	public List<EmployeePayrollData> readEmployeePayrollData() throws DatabaseConnectionException {
		this.employeePayrollList = this.employeePayrollDBService.readData();
		return this.employeePayrollList;
	}

	public void updateEmployeeSalary(String name, double salary) throws DatabaseConnectionException {
		int result = new EmployeePayrollDBService().updateEmployeeDataUsingStatement(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		return this.employeePayrollList.stream()
				.filter(employeePayrollObject -> employeePayrollObject.getName().equals(name)).findFirst().orElse(null);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) throws DatabaseConnectionException {
		List<EmployeePayrollData> employeePayrollDataList = new EmployeePayrollDBService()
				.getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}
}