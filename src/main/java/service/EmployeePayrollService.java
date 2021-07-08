
package service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;

public class EmployeePayrollService {
	// declared private variables
	public EmployeePayrollDBService employeePayrollDBService;
	private List<EmployeePayrollData> employeePayrollList;

	/**
	 * 
	 */
	public EmployeePayrollService() {
		this.employeePayrollDBService = EmployeePayrollDBService.getInstance();
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
	 * created readEmployeePayrollData method to read data from database
	 * 
	 * @param name   : first argument of the method
	 * @param salary : second argument of the method
	 * @throws DatabaseConnectionException
	 */
	public void updateEmployeeSalary(String name, double salary) throws DatabaseConnectionException {
		int result = new EmployeePayrollDBService().updateEmployeePayrollDataUsingPreparedStatement(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null)
			employeePayrollData.setSalary(salary);
	}

	/**
	 * created private getEmployeePayrollData method to get data from database by
	 * using stream api to filter data
	 * @param name : first argument of the method
	 * @return  employeePayrollData
	 */
	public EmployeePayrollData getEmployeePayrollData(String name) {
		EmployeePayrollData employeePayrollData;
		employeePayrollData = this.employeePayrollList.stream()
				.filter(employeePayrollDataItem -> employeePayrollDataItem.getName().equals(name)).findFirst()
				.orElse(null);
		return employeePayrollData;
	}

	/**
	 * created checkEmployeePayrollInSyncWithDB method to check whether updated data
	 * is synced with database or not
	 * @param name : first argument of the method
	 * @return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name)
	 * @throws DatabaseConnectionException
	 */
	public boolean checkEmployeePayrollInSyncWithDB(String name) throws DatabaseConnectionException {
		List<EmployeePayrollData> employeePayrollDataList = new EmployeePayrollDBService()
				.getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}
	
	/**
	 * created getEmployeePayrollDateByStartDate method to get data from database in particular date range
	 * @param startDate : start date in LocalDate format
	 * @param endDate : end date in LocalDate format
	 * @return this.employeePayrollDBService.getEmployeePayrollDataByStartingDate(startDate, endDate)
	 * @throws EmployeePayrollJDBCException
	 */
	public List<EmployeePayrollData> getEmployeePayrollDataByStartDate(LocalDate startDate, LocalDate endDate)throws DatabaseConnectionException {
		return this.employeePayrollDBService.getEmployeePayrollDataByStartingDate(startDate, endDate);
	}
	
	/**
	 * created performOperationByGender method to get maximum, minimum, count, sum and average 
	 * of salary by gender data from database
	 * @param column : salary
	 * @param operation  average, min, max, count and sum 
	 * @return this.employeePayrollDBService.performVariousOperations(column,operation)
	 * @throws DatabaseConnectionException
	 */
	public Map<String, Double> performOperationByGender(String column,String operation) throws DatabaseConnectionException {
		return this.employeePayrollDBService.performVariousOperations(column,operation);
	}
	
	/**
	 * added new employee to employeePayrollList
	 * @param name : first argument of the method
	 * @param salary : second argument of the method
	 * @param startdate : fourth argument of the method
	 * @param gender : third argument of the method
	 * @throws EmployeePayrollJDBCException
	 */
	public void addEmployeeToPayroll(String name, double salary, String gender, LocalDate startdate) throws DatabaseConnectionException {
		employeePayrollList.add(employeePayrollDBService.addEmployeeToPayroll(name,salary,gender,startdate));
	}
}