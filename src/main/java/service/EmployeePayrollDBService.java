package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;

public class EmployeePayrollDBService {
	
	/**
	 * created getConnection() to make connection with mysql database
	 * @return connection
	 * @throws DatabaseConnectionException : custom exception
	 */
	private Connection getConnection() throws DatabaseConnectionException{
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName = "root";
		String password = "Database123@";
		Connection connection ;
		System.out.println("Connecting to database: "+jdbcURL);
		try {
			connection = DriverManager.getConnection(jdbcURL, userName, password);
			System.out.println("Connection is successful!!!" + connection);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e.getMessage());
		}
		return connection;
	
	}
	
	/**
	 * created a readData() to read data from database table
	 * @return employeePayrollDataList
	 * @throws DatabaseConnectionException
	 */
	public List<EmployeePayrollData> readData() throws DatabaseConnectionException{
		String query = "select * from employee_payroll;";
		List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
		try (Connection connection = this.getConnection()){
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			employeePayrollDataList = this.getEmployeePayrollData(resultSet);
		}catch(Exception e) {
			throw new DatabaseConnectionException(e.getMessage());
		}
		return employeePayrollDataList;
		
	}

	/**
	 * created private getEmployeePayrollData method to get all data from database table
	 * added try and catch block to throw custom exception
	 * @param resultSet
	 * @return employeePayrollDataList
	 * @throws DatabaseConnectionException
	 */
	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) throws DatabaseConnectionException {
		List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
		try {
			while(resultSet.next()) {
				int id  = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("basic_pay");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
		} catch (Exception e) {
			throw new DatabaseConnectionException(e.getMessage());
		}
		return employeePayrollDataList;
	}
	
}
