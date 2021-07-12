/********************************************************************************************
 * @Purpose Employee Payroll Database Service program to perform various operations in database. 
 * @author Rosy Rupali
 * @version 1.0
 * @since 07-07-2021
 ************************************************************************************************/
package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;

public class EmployeePayrollDBService {

	private static EmployeePayrollDBService employeePayrollDBService;
	private PreparedStatement preparedStatementForUpdation;
	private PreparedStatement employeePayrollDataStatement;

	// created default constructor
	public EmployeePayrollDBService() {

	}

	/**
	 * created singleton design pattern to single instance by using getInstance()
	 * 
	 * @return employeePayrollDBService
	 */
	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null) {
			employeePayrollDBService = new EmployeePayrollDBService();
		}
		return employeePayrollDBService;
	}

	/**
	 * created getConnection() method to make connection with mysql database
	 * 
	 * @return connection
	 * @throws DatabaseConnectionException
	 */
	private Connection getConnection() throws DatabaseConnectionException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String user = "root";
		String password = "Database123@";
		Connection connection;
		System.out.println("Connecting to database: " + jdbcURL);
		try {
			connection = DriverManager.getConnection(jdbcURL, user, password);
			System.out.println("Connection is SuccessFull!!! " + connection);
			return connection;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to establish the connection");
		}
	}

	/**
	 * created a readData() method to read data from database table
	 * 
	 * @return employeePayrollList
	 * @throws DatabaseConnectionException
	 */
	public List<EmployeePayrollData> readData() throws DatabaseConnectionException {
		String sql = "select * from employee_payroll; ";
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getEmployeePayrollListFromResultset(resultSet);
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to get data.Please check table");
		}
	}

	/**
	 * created private method to get all data from database table added try and
	 * catch block to throw sql exception
	 * 
	 * @param resultSet
	 * @return employeePayrollList
	 * @throws DatabaseConnectionException
	 */
	private List<EmployeePayrollData> getEmployeePayrollListFromResultset(ResultSet resultSet)
			throws DatabaseConnectionException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String objectname = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				String gender = resultSet.getString("gender");
				LocalDate start = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, objectname, salary, gender, start));
			}
			return employeePayrollList;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to use the result set");
		}
	}

	/**
	 * This method is use to execute the update statement of the query
	 * 
	 * @param name
	 * @param salary
	 * @return total rows updated
	 * @throws DatabaseConnectionException
	 */
	public int updateEmployeeDataUsingStatement(String name, double salary) throws DatabaseConnectionException {
		String sql = String.format("update employee_payroll set salary=%.2f where name='%s'", salary, name);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(sql);
			return rowsAffected;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable To update data in database");
		}
	}

	/**
	 * This method is use to execute the update statement of the query using
	 * prepared statement
	 * 
	 * @param name
	 * @param salary
	 * @return total rows updated
	 * @throws DatabaseConnectionException
	 */
	public int updateEmployeePayrollDataUsingPreparedStatement(String name, double salary)
			throws DatabaseConnectionException {
		if (this.preparedStatementForUpdation == null) {
			this.prepareStatementForEmployeePayroll();
		}
		try {
			preparedStatementForUpdation.setDouble(1, salary);
			preparedStatementForUpdation.setString(2, name);
			int rowsAffected = preparedStatementForUpdation.executeUpdate();
			return rowsAffected;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to use prepared statement");
		}
	}

	/**
	 * prepareStatementForEmployeePayroll method for single query to get the data
	 * from database added try and catch block to throw sql exception
	 * 
	 * @throws DatabaseConnectionException
	 */
	private void prepareStatementForEmployeePayroll() throws DatabaseConnectionException {
		try {
			Connection connection = this.getConnection();
			String sql = "update employee_payroll set salary=? where name=?";
			this.preparedStatementForUpdation = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to create prepare statement");
		}
	}

	/**
	 * created getEmployeePayrollDataFromDB method to get data from database
	 * 
	 * @param name
	 * @return employeePayrollList
	 * @throws DatabaseConnectionException
	 */
	public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws DatabaseConnectionException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		if (this.employeePayrollDataStatement == null) {
			this.prepareStatementForEmployeePayrollDataRetrieval();
		}
		try (Connection connection = this.getConnection()) {
			this.employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollListFromResultset(resultSet);
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to read data");
		}
		return employeePayrollList;
	}

	/**
	 * prepareStatementForEmployeePayrollDataRetrival method for single query to get
	 * the data from database added try and catch block to throw sql exception
	 * 
	 * @throws DatabaseConnectionException
	 */
	private void prepareStatementForEmployeePayrollDataRetrieval() throws DatabaseConnectionException {
		try {
			Connection connection = this.getConnection();
			String sql = "select * from employee_payroll where name=?";
			this.employeePayrollDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to create prepare statement");
		}
	}

	/**
	 * created method to retrieve data from data base in a particular date range
	 * 
	 * @param startDate in LocalDate format
	 * @param endDate   in LocalDate format
	 * @return this.getEmployeePayrollListFromResultset(resultSet)
	 * @throws DatabaseConnectionException
	 */
	public List<EmployeePayrollData> getEmployeePayrollDataByStartingDate(LocalDate startDate, LocalDate endDate)
			throws DatabaseConnectionException {
		String sql = String.format(
				"select * from employee_payroll where start between cast('%s' as date) and cast('%s' as date);",
				startDate.toString(), endDate.toString());
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getEmployeePayrollListFromResultset(resultSet);
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Connection Failed.");
		}
	}

	/**
	 * created performVariousOperation method to get average, min, max, count and
	 * sum of salary group by gender data from database by using mysql query in the
	 * method and mapped gender and average, min, max, count and sum of Salary
	 * 
	 * @param column    : salary
	 * @param operation : average, min, max, count and sum
	 * @return the salary used by various operations mapped with gender
	 * @throws DatabaseConnectionException
	 */
	public Map<String, Double> performVariousOperations(String column, String operation)
			throws DatabaseConnectionException {
		String sql = String.format("select gender , %s(%s) from employee_payroll group by gender;", operation, column);
		Map<String, Double> mapValues = new HashMap<>();
		try (Connection connection = this.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				mapValues.put(resultSet.getString(1), resultSet.getDouble(2));
			}
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Connection Failed.");
		}
		return mapValues;
	}

	/**
	 * added new employee and payroll details to the table in database
	 * 
	 * @param name      : first argument of the method
	 * @param salary    : second argument of the method
	 * @param startdate : fourth argument of the method
	 * @param gender    : third argument of the method
	 * @return employeePayrollData
	 * @throws DatabaseConnectionException
	 */
	public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate startdate, String gender)
			throws DatabaseConnectionException {
		int employeeId = -1;
		Connection connection = null;
		EmployeePayrollData employeePayrollData = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (Exception e) {
			throw new DatabaseConnectionException("Error");
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO employee_payroll (name,gender,salary,start) VALUES ('%s','%s','%s','%s')", name,
					gender, salary, startdate);
			int rowsAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if (rowsAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(1);
			}
			employeePayrollData = new EmployeePayrollData(employeeId, name, salary, gender, startdate);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DatabaseConnectionException("Could Not Add");
		}
		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format(
					"INSERT INTO payroll_details (employeeId,salary,deductions,taxable_pay,tax,net_pay) VALUES "
							+ "('%s','%s','%s','%s','%s','%s')",
					employeeId, salary, deductions, taxablePay, tax, netPay);
			int rowsAffected = statement.executeUpdate(sql);
			if (rowsAffected == 1)
				employeePayrollData = new EmployeePayrollData(employeeId, name, salary, gender, startdate);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DatabaseConnectionException("Not Able to add");
		}
		try {
			connection.commit();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return employeePayrollData;
	}
}
