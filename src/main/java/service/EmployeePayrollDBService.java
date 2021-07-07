package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;

public class EmployeePayrollDBService {

	private static EmployeePayrollDBService employeePayrollDBService;
	private PreparedStatement preparedStatementForUpdation;
	private PreparedStatement employeePayrollDataStatement;

	//created default constructor
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
				LocalDate start = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, objectname, salary, start));
			}
			return employeePayrollList;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to use the result set");
		}
	}

	/**
	 * This method is use to execute the update statement of the query
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
	 * This method is use to execute the update statement of the query using prepared statement
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
	 * prepareStatementForEmployeeData method for single query to get the data from database
     * added try and catch block to throw sql exception
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
	 * created getEmployeePayrollData method to get data from database
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
	 * prepareStatementForEmployeeData method for single query to get the data from
	 * database added try and catch block to throw sql exception
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
}
