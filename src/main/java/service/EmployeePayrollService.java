
package service;

import java.util.List;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;

public class EmployeePayrollService {
	//declared private variables
	public EmployeePayrollDBService employeePayrollDBService;
	private List<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService() {
		this.employeePayrollDBService = new EmployeePayrollDBService().getInstance();
	}

	/**
	 * created readEmployeePayrollData method to read data from database
	 * 
	 * @return this.employeePayrollList
	 * @throws DatabaseConnectionException
	 */
	public List<EmployeePayrollData> readEmployeePayrollData() throws DatabaseConnectionException {
		this.employeePayrollList = this.employeePayrollDBService.readData();
		return this.employeePayrollList;
	}

	/**
	 * created updateEmployeeSalary method to update employee salary to database
	 * 
	 * @param name   : first argument of the method
	 * @param salary : second argument of the method
	 * @throws DatabaseConnectionException
	 */
	public void updateEmployeeSalary(String name, double salary) throws DatabaseConnectionException {
		int result = new EmployeePayrollDBService().updateEmployeeDataUsingStatement(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);
	}

	/**
	 * created private getEmployeePayrollData method to get data from database by
	 * using stream api to filter data
	 * 
	 * @param name : first argument of the method
	 * @return employeePayrollData
	 */
	private EmployeePayrollData getEmployeePayrollData(String name) {
		EmployeePayrollData employeePayrollData;
		employeePayrollData = this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.getName().equals(name)).findFirst()
				.orElse(null);
		return employeePayrollData;
	}

	/**
	 * created checkEmployeePayrollInSyncWithDB method to check whether updated data
	 * is synced with database or not
	 * 
	 * @param name : first argument of the method
	 * @return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name)
	 * @throws DatabaseConnectionException
	 */
	public boolean checkEmployeePayrollInSyncWithDB(String name) throws DatabaseConnectionException {
		List<EmployeePayrollData> employeePayrollDataList = new EmployeePayrollDBService()
				.getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}
}